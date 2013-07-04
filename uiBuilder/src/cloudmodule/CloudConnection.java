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
	
	public void createSection(ContentValues values)
	{
		if (cloudActive)
		{
			Log.d("creating section in cloud", "called");
			
			final int sectionId = values.getAsInteger(ScreenProvider.KEY_ID);
			
			final ParseObject section = new ParseObject(CloudConstants.TYPE_SECTIONS);
			
			section.put(ScreenProvider.KEY_SECTION_NAME, values.getAsString(ScreenProvider.KEY_SECTION_NAME));
			section.put(ScreenProvider.KEY_SECTION_DESCRIPTION, values.getAsString(ScreenProvider.KEY_SECTION_DESCRIPTION));
			
			section.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					// TODO Auto-generated method stub
					if (e == null)
					{
						String cloudSectionId = section.getObjectId();
						
						Uri localSectionUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, sectionId);
						
						ContentValues cloudIdValues = new ContentValues();
						cloudIdValues.put(ScreenProvider.KEY_SECTION_PARSE_ID, cloudSectionId);
						
						resolver.update(localSectionUri, cloudIdValues, null, null);
						Log.d("section uploaded and updated with cloud id", cloudSectionId);
					}
				}
			});
		}
	}
	
	public void createObject(ContentValues objectvalues)
	{
		if (cloudActive)
		{
			Log.d("creating object in cloud", "called");
			
			final int objectId = objectvalues.getAsInteger(ScreenProvider.KEY_ID);
			final int associatedScreenId = objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_SCREEN);
			
			Uri localScreenUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, associatedScreenId);
			
			Cursor screen = resolver.query(localScreenUri, new String[] {ScreenProvider.KEY_SCREEN_CLOUD_PARSE_ID}, null, null, null);
			screen.moveToFirst();
			String screenCloudId = screen.getString(screen.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_CLOUD_PARSE_ID));
			screen.close();
			
			final ParseObject object = new ParseObject(CloudConstants.TYPE_OBJECTS);
			
			object.put(ScreenProvider.KEY_OBJECTS_SCREEN_PARSE_ID, screenCloudId);
			
			putValuesInObject(objectvalues, object);
			
			object.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					// TODO Auto-generated method stub
					if (e == null)
					{
						String cloudId = object.getObjectId();
						
						Uri localObjectUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_OBJECTS, objectId);
						
						ContentValues cloudIdValues = new ContentValues();
						cloudIdValues.put(ScreenProvider.KEY_OBJECTS_PARSE_ID, cloudId);
						
						resolver.update(localObjectUri, cloudIdValues, null, null);
						Log.d("object uploaded and updated with cloud id", cloudId);
					}
				}
			});
		}
	}
	
	public void updateObject(ContentValues newValues)
	{
		if (cloudActive)
		{
			final String objectId = newValues.getAsString(ScreenProvider.KEY_OBJECTS_PARSE_ID);
			
			Log.d("cloud about to update object", objectId);
			
			final ParseObject objectToUpdate = new ParseObject(CloudConstants.TYPE_OBJECTS);
			objectToUpdate.setObjectId(objectId);
			
			putValuesInObject(newValues, objectToUpdate);
			objectToUpdate.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					if (e == null)
					Log.d("object updatet in cloud", "success");
					
					else
					{
						Log.d("cloud object update ", "failed: " + e.getLocalizedMessage());
						
					}
				}
			});
		}
	}

	/**
	 * @param objectvalues
	 * @param object
	 */
	private void putValuesInObject(ContentValues objectvalues,
			final ParseObject object)
	{
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ALIGNMENT) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_ALIGNMENT, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ALIGNMENT));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUND) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUND, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUND));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_COLUMNS_NUM) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_COLUMNS_NUM, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_COLUMNS_NUM));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_CONTENT) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_CONTENT, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_CONTENT));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_FONTSIZE) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_FONTSIZE, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_FONTSIZE));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_HEIGHT) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_HEIGHT, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_HEIGHT));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ICNSRC) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_ICNSRC, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ICNSRC));
		//object.put(ScreenProvider.KEY_OBJECTS_VIEW_IMGSRC, objectvalues.getAsString(ScreenProvider.KEY_OBJECTS_VIEW_IMGSRC));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_LAYOUT) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_LAYOUT, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_LAYOUT));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_RATING) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_RATING, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_RATING));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_STARSNUM) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_STARSNUM, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_STARSNUM));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_TYPE) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_TYPE, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_TYPE));
		
		if (objectvalues.getAsString(ScreenProvider.KEY_OBJECTS_VIEW_USERTEXT) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_USERTEXT, objectvalues.getAsString(ScreenProvider.KEY_OBJECTS_VIEW_USERTEXT));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_WIDTH) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_WIDTH, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_WIDTH));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_XPOS) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_XPOS, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_XPOS));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_YPOS) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_YPOS, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_YPOS));
		
		if (objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ZORDER) != null)
		object.put(ScreenProvider.KEY_OBJECTS_VIEW_ZORDER, objectvalues.getAsInteger(ScreenProvider.KEY_OBJECTS_VIEW_ZORDER));
	}
	
	public void createScreen(ContentValues screenValues)
	{
		if (cloudActive)
		{
			final ParseObject screen = new ParseObject(CloudConstants.TYPE_SCREENS);
			final int localScreenId = screenValues.getAsInteger(ScreenProvider.KEY_ID);
			
			Uri localSection = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, screenValues.getAsInteger(ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION));
			Cursor sectionCursor = resolver.query(localSection, new String[] {ScreenProvider.KEY_SECTION_PARSE_ID}, null, null, null);
			
			sectionCursor.moveToFirst();
			String associatedCloudSectionId = sectionCursor.getString(sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_PARSE_ID));
			sectionCursor.close();
			
			screen.put(ScreenProvider.KEY_SCREEN_NAME, screenValues.getAsString(ScreenProvider.KEY_SCREEN_NAME));
			screen.put(ScreenProvider.KEY_SCREEN_ASSOCIATED_CLOUD_SECTION, associatedCloudSectionId);
			
			screen.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					if (e == null)
					{
						Log.d("saved screen to cloud", "success");
						
						String cloudObjectId = screen.getObjectId();
						Log.d("saved screen id", cloudObjectId);
						
						ContentValues screenIdValue = new ContentValues();
						screenIdValue.put(ScreenProvider.KEY_SCREEN_CLOUD_PARSE_ID, cloudObjectId);
						
						Uri localScreenUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, localScreenId);
						resolver.update(localScreenUri, screenIdValue, null, null);
					}
					
				}
			});
		}
	}


	public void createProject(final ContentValues values, final long id, String collabs)
	{
		if (cloudActive)
		{	
			final ParseObject project = new ParseObject(CloudConstants.TYPE_PROJECT);
			
			
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
					
					ContentValues sectionValues = new ContentValues();
					sectionValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_CLOUD_PROJECT, cloudProjectId);
					
					Cursor sections = resolver.query(ScreenProvider.CONTENT_URI_SECTIONS, new String[] {ScreenProvider.KEY_ID}, ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + "=" + String.valueOf(id), null, null);
					int idIdx = sections.getColumnIndex(ScreenProvider.KEY_ID);
					
					while (sections.moveToNext())
					{
						int sectionId = sections.getInt(idIdx);
						Uri sectionUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, sectionId);
						
						resolver.update(sectionUri, sectionValues, null, null);
					}
					sections.close();
					
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
		cursor.close();
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
	
	public void deleteObject(final String objectId, final String type)
	{
		if (cloudActive && objectId != null)
		{
			Log.d("cloud deleting type", type);
			
			ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery(type);
			
			deleteQuery.getInBackground(objectId, new GetCallback<ParseObject>() 
			{
			  public void done(ParseObject object, ParseException e) 
			  {
			    if (e == null) 
			    {
			    	object.deleteInBackground(new DeleteCallback()
					{
						
						@Override
						public void done(ParseException e)
						{
							if (e == null)
							{
								if (type.equalsIgnoreCase(CloudConstants.TYPE_PROJECT))
								{
									unsubscribeFromProject(objectId);
								}
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

	public void updateSection(final ContentValues cloudValues)
	{
		String cloudId = cloudValues.getAsString(ScreenProvider.KEY_SECTION_PARSE_ID);
		
		if (cloudActive && cloudId != null)
		{
			Log.d("cloud", "updating section " + cloudId);
			
			ParseObject section = new ParseObject(CloudConstants.TYPE_SECTIONS);
			section.setObjectId(cloudId);
			
			String name = cloudValues.getAsString(ScreenProvider.KEY_SECTION_NAME);
			String desc = cloudValues.getAsString(ScreenProvider.KEY_SECTION_DESCRIPTION);
			String cloudProject = cloudValues.getAsString(ScreenProvider.KEY_SECTION_ASSOCIATED_CLOUD_PROJECT);
			
			if (name != null)
			{
				section.put(ScreenProvider.KEY_SECTION_NAME, name);
				Log.d("updating name to", name);
			}
			
			if (desc != null)
			{
				section.put(ScreenProvider.KEY_SECTION_DESCRIPTION, desc);
				Log.d("updating description to", desc);
			}
			
			if (cloudProject != null)
			{
				section.put(ScreenProvider.KEY_SECTION_ASSOCIATED_CLOUD_PROJECT, cloudProject);
				Log.d("updating associated cloud project id to", cloudProject);
			}
			
			//if (cloudId != null)
			//section.put(ScreenProvider.KEY_SECTION_CLOUD_PARSE_ID, cloudId);
			
			section.saveInBackground(new SaveCallback()
			{
				
				@Override
				public void done(ParseException e)
				{
					if (e == null)
					{
						Log.d("cloud section", "update cpmplete");
					}
				}
			});
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery(CloudConstants.TYPE_SECTIONS);
			query.whereEqualTo(CloudConstants.OBJECT_ID, cloudId);
		}
	}

	public void updateProject(ContentValues projectCloudValues)
	{
		if (cloudActive)
		{
			Log.d("cloud updating project with id", projectCloudValues.getAsString(ScreenProvider.KEY_PROJECTS_PARSE_ID));
			
			ParseObject project = new ParseObject(CloudConstants.TYPE_PROJECT);
			project.setObjectId(projectCloudValues.getAsString(ScreenProvider.KEY_PROJECTS_PARSE_ID));
			
			project.put(ScreenProvider.KEY_PROJECTS_NAME, projectCloudValues.getAsString(ScreenProvider.KEY_PROJECTS_NAME));
			project.put(ScreenProvider.KEY_PROJECTS_DESCRIPTION, projectCloudValues.getAsString(ScreenProvider.KEY_PROJECTS_DESCRIPTION));
			
			project.saveEventually(new SaveCallback()
			{
				
				@Override
				public void done(ParseException arg0)
				{
					// TODO Auto-generated method stub
					Log.d("project in cloud ", "updated");
				}
			});
		}	
	}
}
