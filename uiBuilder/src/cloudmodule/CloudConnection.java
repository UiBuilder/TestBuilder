package cloudmodule;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import projects.ProjectManagerActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import data.ScreenProvider;

public class CloudConnection
{
	private ContentResolver resolver;
	private Context context;
	
	public static final String PARSE_PROJECT = "project";
	
	public CloudConnection(ScreenProvider screenProvider, ContentResolver resolver)
	{
		this.resolver = resolver;
		this.context = screenProvider.getContext();
		Parse.initialize(context, "CJnqP0stzTozwnVqLtdREHEhI1y2kdKXAZ31SbxC", "GE9Ogahzy7djtjU66k2vSQA5GBEe2fQIUJ354t6u");
		//PushService.startServiceIfRequired(context);
		 
		Log.d("cloud init", "starting");

	}
	


	public void createProject(final ContentValues values, final long id, String collabs)
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
/*	
	private void createNewChannelforProject(String cloudProjectId)
	{
		// TODO Auto-generated method stub
		String newProjectChannelName = "project_" + cloudProjectId;
		
		//PushService.subscribe(context, newProjectChannelName, ProjectManagerActivity.class);

		
		Set<String> setOfAllSubscriptions = PushService.getSubscriptions(context);
		
		String[] subs = new String[setOfAllSubscriptions.size()];
		
				setOfAllSubscriptions.toArray(subs);
				
		for (String s: subs)
				Log.d("subscription inserted", s); 
	}*/
}
