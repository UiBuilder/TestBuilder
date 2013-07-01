package projects;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import cloudmodule.CloudConnection;
import cloudmodule.CloudConstants;

import com.parse.ParseObject;
import com.parse.ParseUser;

import data.DateGenerator;
import data.NewScreenHolder;
import data.ProjectHolder;
import data.ScreenProvider;

public class Project
{
	private ProjectHolder projectHolder;
	private ArrayList<NewScreenHolder> screenHolder;
	private ArrayList<ParseUser> collabList;

	private CloudConnection cloud;
	private ParseObject cloudProject;
	
	private ContentResolver resolver;
	private DateGenerator date;
	
	private Context context;
	
	private boolean notNew = false;
	
	//private String 
	
	public Project(Context c)
	{
		// TODO Auto-generated constructor stub
		this.context = c;
		
		setupHelpers();
	}
	
	public Project(Context c, Cursor cursor)
	{
		this.context = c;
		setupHelpers();
		
		recreateFromCursor(cursor);
	}
	
	private void recreateFromCursor(Cursor cursor)
	{
		newProjectHolderFromCursor(cursor);
		fetchUsersFromCloud(cursor);
	}
	
	public void setCloudProject(ParseObject project)
	{
		cloudProject = project;
	}

	private void fetchUsersFromCloud(Cursor cursor)
	{
		cloud.queryProject(cursor, true);
	}

	private void newProjectHolderFromCursor(Cursor cursor)
	{
		// TODO Auto-generated method stub
		int dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DATE);
		int nameIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
		int descIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DESCRIPTION);
		int sharedIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_SHARED);
		int projIdIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		int cloudIdIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_PARSE_ID);
		
		cursor.moveToFirst();
		
		projectHolder.projectDate = cursor.getString(dateIdx);
		projectHolder.projectDescription = cursor.getString(descIdx);
		projectHolder.projectName = cursor.getString(nameIdx);
		projectHolder.projectShared = cursor.getString(sharedIdx);
		projectHolder.projectId = cursor.getInt(projIdIdx);
		projectHolder.cloudId = cursor.getString(cloudIdIdx);
	}

	protected void setNotNew()
	{
		notNew = true;
	}
	
	protected List<ParseUser> getUsers()
	{
		return collabList;
	}
	
	
	/**
	 * 
	 */
	private void setupHelpers()
	{
		screenHolder = new ArrayList<NewScreenHolder>();

		projectHolder = new ProjectHolder();
        resolver = context.getContentResolver();
		date = new DateGenerator();
		
		collabList = new ArrayList<ParseUser>();
		
		cloud = CloudConnection.establish(context, resolver);
	}
	
	protected void prepare(String name, String description)
	{
		projectHolder.projectDate = date.generateDate();
		projectHolder.projectName = name;
		projectHolder.projectDescription = description;
		projectHolder.projectShared = CloudConstants.PROJECT_SHARED_FALSE;
	}
	
	protected void addScreen(String sectionName, String sectionDesc)
	{
		NewScreenHolder holder = new NewScreenHolder();
		
		if (notNew)
		{
			holder.sectionId = projectHolder.projectId;
		}

		holder.sectionName = sectionName;
		holder.sectionDescription = sectionDesc;
		
		screenHolder.add(holder);
		
		//notifyUser();
		//resetFields();
		//displayResults();
	}
	
	protected ArrayList<NewScreenHolder> getScreens()
	{
		return screenHolder;
	}
	
	protected void addUser(ParseUser collab)
	{
		if (checkList(collab) == -1)
		collabList.add(collab);
		
		if (cloudProject != null)
		updateCollabs();
	}
	
	protected void removeUser(ParseUser user)
	{
		int idx = checkList(user);
		collabList.remove(idx);
		
		Log.d("removing user", user.getEmail());
		
		if (cloudProject != null)
		updateCollabs();
	}
	
	protected int checkList(ParseUser user)
	{
		// TODO Auto-generated method stub
		for (int i=0; i<collabList.size(); i++)
		{
			if (collabList.get(i).hasSameId(user))
			{
				Log.d("actual user", user.getObjectId());
				Log.d("in list", collabList.get(i).getObjectId());
				return i;
			}		
		}
		return -1;
	}
	
	protected void setShared(String projectShared)
	{

		projectHolder.projectShared = projectShared;

	}
	
	protected void setProjectId(int id)
	{
		projectHolder.projectId = id;
	}
	
	protected void create()
	{
		Log.d("project", "create called");
		ContentValues values = projectHolder.getValues();
		
		if (!notNew)
		{		
			if (projectHolder.projectShared.equalsIgnoreCase(CloudConstants.PROJECT_SHARED_TRUE))
			{
				Log.d("project is a shared one", String.valueOf(projectHolder.projectShared));
				addselfToCollabs();
				String collabs = convertIdsToString(getIds());
				values.put(CloudConstants.PROJECT_COLLABS, collabs);
			}
			Uri inserted = resolver.insert(ScreenProvider.CONTENT_URI_PROJECTS, values);
			
		
			String path = inserted.getPathSegments().get(1);
			projectHolder.projectId = Integer.valueOf(path);
			Log.d("insert project path full", path);
			Log.d("id in holder", String.valueOf(projectHolder.projectId));
		}
		else
		{

		}
		
		insertNewScreens();
	}
	
	protected void updateCollabs()
	{
		cloud.updateCollabs(cloudProject, convertIdsToString(getIds()));
	}
	
	
	private void addselfToCollabs()
	{
		// TODO Auto-generated method stub
		addUser(ParseUser.getCurrentUser());
	}
	

	private void insertNewScreens()
	{
		ContentValues[] screenValues = new ContentValues[screenHolder.size()];
		
		int i = 0;
		for (NewScreenHolder holder: screenHolder)
		{
			holder.sectionId = projectHolder.projectId;
			
			if (notNew)
			{
				holder.cloudProjectId = projectHolder.cloudId;
			}
			//setProjectId(holder);
			screenValues[i] = holder.getBundle();
			i++;
		}
		
		resolver.bulkInsert(ScreenProvider.CONTENT_URI_SECTIONS, screenValues);		
	}


	private String convertIdsToString(String[] ids)
	{
		String idString = "";
		for (int i=0; i<ids.length; i++)
		{
			idString += ids[i] + " ";
		}
		return idString;
	}
	

	private String[] getIds()
	{
		
		String[] ids = new String[collabList.size()];
		
		
		for (int i=0; i<collabList.size();i++)
		{
			ids[i] = collabList.get(i).getObjectId();
			Log.d("id at i", ids[i]);
		}
		return ids;
	}

	public void setColor(int colorResource)
	{
		projectHolder.projectColor = colorResource;
		
	}
	
}
