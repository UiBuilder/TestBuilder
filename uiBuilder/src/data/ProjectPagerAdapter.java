package data;

import uibuilder.ProjectDisplay;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProjectPagerAdapter extends FragmentStatePagerAdapter
{
	private int projects;
	private Cursor projectCursor;
	private ContentResolver resolver;
	
	private ProjectHolder[] projectHolder;

	public ProjectPagerAdapter(FragmentManager fm, Context c)
	{
		super(fm);
		// TODO Auto-generated constructor stub
		
		resolver = c.getContentResolver();
		
		projectCursor = resolver.query(ScreenProvider.CONTENT_URI_PROJECTS, null, null, null, null);
		
		projectHolder = new ProjectHolder[projectCursor.getCount()];
		
		int dateIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DATE);
		int descIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DESCRIPTION);
		int nameIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
		int idIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		int i = 0;
		while (projectCursor.moveToNext())
		{
			projectHolder[i] = new ProjectHolder();
			
			projectHolder[i].projectDate = projectCursor.getString(dateIdx);
			projectHolder[i].projectDescription = projectCursor.getString(descIdx);
			projectHolder[i].projectName = projectCursor.getString(nameIdx);
			projectHolder[i].projectId = projectCursor.getInt(idIdx);
			
			i++;
		}

	}

	@Override
	public Fragment getItem(int pos)
	{
		// TODO Auto-generated method stub
		
		
		Bundle args = projectHolder[pos].getBundle();
		Fragment project = new ProjectDisplay();
		
		project.setArguments(args);
		
		return project;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return projectHolder.length;
	}

}
