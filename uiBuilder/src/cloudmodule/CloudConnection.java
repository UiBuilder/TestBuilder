package cloudmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import projects.ProjectManagerActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import data.ScreenProvider;

public class CloudConnection
{
	private ContentResolver resolver;
	private Context context;
	
	private static ParseUser activeCloudUser;
	private static boolean cloudActive;
	
	private static CloudConnection self;
	
	public static final String PARSE_PROJECT = "project",
								PARSE_SECTION = "section"
								;	
	
	private CloudConnection(Context c, ContentResolver resolver)
	{
		Log.d("cloud init", "starting");
		
		this.resolver = resolver;
		this.context = c;
		
		Parse.initialize(c.getApplicationContext(), "CJnqP0stzTozwnVqLtdREHEhI1y2kdKXAZ31SbxC", "GE9Ogahzy7djtjU66k2vSQA5GBEe2fQIUJ354t6u");
		
		activeCloudUser = ParseUser.getCurrentUser();
		if (activeCloudUser != null)
		{
			cloudActive = true;
			Log.d("cloudservice", "cloud active");
		}
		else
		{
			Log.d("cloudservice", "cloud disabled");
		}
	}
	
	public static CloudConnection establish(Context c, ContentResolver resolver)
	{
		if (self == null)
		{
			self = new CloudConnection(c, resolver);
		}
		else
		{
			
		}
		return self;
	}
	
	public void updateCollabs(ParseObject project, String collabs)
	{
		String[] collabArray = collabs.split(" ");
		project.remove("collabs");
		project.addAll("collabs", Arrays.asList(collabArray));
		project.saveInBackground();
	}
	
	public void createSection(final ContentValues values)
	{
		if (cloudActive)
		{
			final int sectionId = values.getAsInteger(ScreenProvider.KEY_ID);
			
			final ParseObject section = new ParseObject(PARSE_SECTION);
			section.put(ScreenProvider.KEY_SECTION_NAME, values.getAsString(ScreenProvider.KEY_SECTION_NAME));
			section.put(ScreenProvider.KEY_SECTION_DESCRIPTION, values.getAsString(ScreenProvider.KEY_SECTION_DESCRIPTION));
			
			section.saveEventually(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					// TODO Auto-generated method stub
					if (e == null)
					{
						String cloudSectionId = section.getObjectId();
						
						Uri localSectionUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, sectionId);
						values.put(ScreenProvider.KEY_SECTION_CLOUD_PARSE_ID, cloudSectionId);
						resolver.update(localSectionUri, values, null, null);
						Log.d("section updated with cloud id", cloudSectionId);
					}
				}
			});
		}
	}


	public void createProject(final ContentValues values, final long id, String collabs)
	{
		if (cloudActive)
		{	
			final ParseObject project = new ParseObject(PARSE_PROJECT);
			
			
			String[] collabArray = collabs.split(" ");
			
			project.put(ScreenProvider.KEY_PROJECTS_NAME, values.getAsString(ScreenProvider.KEY_PROJECTS_NAME));
			project.put(ScreenProvider.KEY_PROJECTS_DESCRIPTION, values.getAsString(ScreenProvider.KEY_PROJECTS_DESCRIPTION));
			project.put(ScreenProvider.KEY_PROJECTS_SHARED, values.getAsString(ScreenProvider.KEY_PROJECTS_SHARED));
			project.addAll("collabs", Arrays.asList(collabArray));
			
			project.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException arg0)
				{
					// TODO Auto-generated method stub
					String cloudProjectId = project.getObjectId();
					
					values.put(ScreenProvider.KEY_PROJECTS_PARSE_ID, cloudProjectId);
					Log.d("updating with parse id ", cloudProjectId);
					Uri path = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_PROJECTS, id);
					
					resolver.update(path, values, null, null);
					
					Cursor sections = resolver.query(ScreenProvider.CONTENT_URI_SECTIONS, new String[] {ScreenProvider.KEY_ID}, ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + "=" + String.valueOf(id), null, null);
					int sectionIdIdx = sections.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
					
					
					while (sections.moveToNext())
					{
						ContentValues localValues = new ContentValues();
						localValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_CLOUD_PROJECT, cloudProjectId);
						
						int sectionId = sections.getInt(sectionIdIdx);
						Log.d("section found", String.valueOf(sectionId));
						Log.d("updating section with cloud project id", cloudProjectId);
						
						Uri sectionUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, sectionId);
						resolver.update(sectionUri, localValues, null, null);		
					}
					
					notifyCollabs(project);
					subscribe(cloudProjectId);
				}
	
				private void subscribe(String cloudProjectId)
				{
					// TODO Auto-generated method stub
					String projectChannel = CloudConstants.PROJECT_CHANNEL_PREFIX + cloudProjectId;
					
					PushService.subscribe(context, projectChannel, ProjectManagerActivity.class);
					Log.d("new project channel created", "subscribing to " + projectChannel);
				}
			});
		}
	}
	
	private void notifyCollabs(ParseObject project)
	{
		List<String> collabList = project.getList("collabs");
		String projectName = project.getString(ScreenProvider.KEY_PROJECTS_NAME);
		String currentUser = ParseUser.getCurrentUser().getObjectId();
		
		for (String id: collabList)
		{
			if (!id.equals(currentUser))
			{
				String userChannel = CloudConstants.USER_CHANNEL_PREFIX + id;
				
				ParsePush invite = new ParsePush();
				invite.setChannel(userChannel);
				invite.setMessage("You have been in invited to sketch in project: " + projectName);
				invite.sendInBackground(new SendCallback()
				{
					
					@Override
					public void done(ParseException arg0)
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}
	
	public void queryProject(Cursor cursor, final boolean withUsers)
	{
		Log.d("fetch project", "from cloud");
		// TODO Auto-generated method stub
		int parseIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_PARSE_ID);
		
		String cloudProjectId = cursor.getString(parseIdx);
		//Log.d("object id of loacal project", cloudProjectId);
		
		if(cloudProjectId != null)
		try
		{
			Log.d("object id of loacal project", cloudProjectId);
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery(CloudConstants.TYPE_PROJECT);
			query.get(cloudProjectId);
			
			query.findInBackground(new FindCallback<ParseObject>() 
			{
			     public void done(List<ParseObject> objects, ParseException e) 
			     {
			         if (e == null) 
			         {
			        	 Log.d("project fetched", String.valueOf(objects.size()));
			             objectsWereRetrievedSuccessfully(objects);
			         } else 
			         {
			             objectRetrievalFailed();
			         }
			     }

				private void objectRetrievalFailed()
				{
					// TODO Auto-generated method stub
					
				}

				private void objectsWereRetrievedSuccessfully(List<ParseObject> objects)
				{
					// TODO Auto-generated method stub
					ParseObject project = objects.get(0);
					Log.d("project id from fetched project", project.getObjectId());
					

					fromCloudLoaded.projectLoaded(project);
					
					if(withUsers)
					{
						List<String> collabs= project.getList("collabs");
						fetchUsers(collabs);
					}
				}
			});
		}
		catch (Exception e) {
				// TODO: handle exception
		}
		
	}
	
	public void queryUser(String identifier)
	{
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		
		Log.d("querying user", "called");
		
		query.whereEqualTo("username", identifier);
		query.findInBackground(new FindCallback<ParseUser>()
		{

			@Override
			public void done(List<ParseUser> users, ParseException e)
			{
				// TODO Auto-generated method stub
				if(e == null && !users.isEmpty())
				{
					Log.d("users found", String.valueOf(users.size()));
					fromCloudLoaded.userFound(users.get(0));		
				}
			}
		});
	}
	
	
	
	private void fetchUsers(List<String> collabs)
	{
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereContainedIn("objectId", collabs);
		
		query.findInBackground(new FindCallback<ParseUser>()
		{

			@Override
			public void done(List<ParseUser> users,
					ParseException arg1)
			{					
				ArrayList<ParseUser> collabList = new ArrayList<ParseUser>(users);
				Log.d("collablist is now", String.valueOf(collabList.size()));
				
				for (ParseUser user: collabList)
				{
					Log.d("user in project", user.getEmail());
				}
				
				fromCloudLoaded.usersLoaded(collabList);
			}		
		});
	}
	
	public void checkForNewCollabProjects()
	{
		
		try
		{
			ParseQuery<ParseObject> queryProjects = new ParseQuery<ParseObject>("project");
			queryProjects.whereEqualTo(CloudConstants.PROJECT_COLLABS, ParseUser.getCurrentUser().getObjectId());
			
			queryProjects.findInBackground(new FindCallback<ParseObject>()
			{
				
				@Override
				public void done(List<ParseObject> results, ParseException e)
				{
					// TODO Auto-generated method stub
					if (e == null)
					for (ParseObject newFoundCollabProject: results)
					{
						String projectId = newFoundCollabProject.getObjectId();
						String projectName = newFoundCollabProject.getString(ScreenProvider.KEY_PROJECTS_NAME);
						
						String channelName = CloudConstants.PROJECT_CHANNEL_PREFIX + projectId;
						Log.d("collaborating in", projectId);
						
						if(checkForChannel(channelName))
						{
							Log.d("subscriptioncheck", "already subscribed");
						}
						else
						{
							Log.d("subscriptioncheck", "new Channel found");
							Log.d("subscriptioncheck", "subscribing now");
							PushService.subscribe(context, channelName, ProjectManagerActivity.class);
							ParseInstallation.getCurrentInstallation().saveInBackground();
							
							sendJoinMessage(channelName, projectName);
							
							fromCloudLoaded.newCloudProjectFound(projectId);
						}	
					}
					else
					{
						Log.d("checkfornewcollabproject in projectmanager", "failed");
					}
				}
				
	
				private boolean checkForChannel(String project)
				{
					// TODO Auto-generated method stub
					
					Log.d("check for channel", project);
					Set<String> setOfAllSubscriptions = PushService.getSubscriptions(context);
					
					for (String subscribed: setOfAllSubscriptions)
					{
						
						if(subscribed.equalsIgnoreCase(project))
						{
							return true;
						}
						
					}
					return false;
				}
			});
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void sendJoinMessage(String channel, String projectName)
	{
		String newMember = ParseUser.getCurrentUser().getUsername();
		ParsePush push = new ParsePush();
		
		push.setChannel(channel);
		push.setMessage(newMember + " joined " + projectName);
		push.sendInBackground(new SendCallback()
		{
			
			@Override
			public void done(ParseException e)
			{
				// TODO Auto-generated method stub
				if(e != null)
				Log.d("push error", e.getMessage());
			}
		});
	}
	
	public void deleteProject(final String objectId)
	{
		
		ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery(CloudConstants.TYPE_PROJECT);
		
		deleteQuery.getInBackground(objectId, new GetCallback<ParseObject>() 
		{
		  public void done(ParseObject project, ParseException e) 
		  {
		    if (e == null) 
		    {
		    	project.deleteEventually(new DeleteCallback()
				{
					
					@Override
					public void done(ParseException e)
					{
						if (e == null)
						{
							unsubscribeFromProject(objectId);
							Log.d("cloud deleted", objectId);
						}
						
					}
				});
		    	
		    } 
		    else {
		      // something went wrong
		    }
		  }
		});
	}
	
	public void queryObjects(List<String> objectIds, String type)
	{
		//for (String objectId: objectIds)
		{
			Log.d("query for object type", type);
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery(type);
			query.whereContainedIn(CloudConstants.OBJECT_ID, objectIds);

			query.findInBackground(new FindCallback<ParseObject>() 
			{
			    public void done(List<ParseObject> results, ParseException e) 
			    {
			    	Log.d("query", "done");
			        if (e == null) 
			        {
			        	for (ParseObject object: results)
			        	{
			        		Log.d("retrieved objects", object.getObjectId());
			        		
			        	}
			        } 
			        else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
		}
		
		
	}
	
	
	private void unsubscribeFromProject(String objectId)
	{
		String projectIdPrefixed = CloudConstants.PROJECT_CHANNEL_PREFIX + objectId;
		
		PushService.unsubscribe(context, projectIdPrefixed);
		Log.d("cloud unsubscribed from", projectIdPrefixed);
	}


	//LISTEN FOR CLOUD MESSAGES
	public interface OnFromCloudLoadedListener
	{
		void usersLoaded(ArrayList<ParseUser> users);
		
		void userFound(ParseUser user);
		
		void projectLoaded(ParseObject project);
		
		void newCloudProjectFound(String cloudProjectId);
	}

	private static OnFromCloudLoadedListener fromCloudLoaded;

	public static void setOnFromCloudLoadedListener(
			OnFromCloudLoadedListener listener)
	{
		CloudConnection.fromCloudLoaded = listener;
	}
}
