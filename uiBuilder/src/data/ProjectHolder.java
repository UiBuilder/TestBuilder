package data;

import android.content.ContentValues;
import android.os.Bundle;

public class ProjectHolder
{
	public static final String 
						nameArg = "name",
						dateArg = "date",
						descArg = "description",
						idArg = "id"
						;
	

	
	public String projectName;
	
	public String projectDate;
	
	public String projectDescription;
	
	public String projectShared;
	
	public int projectId;
	
	public Bundle getBundle()
	{
		Bundle values = new Bundle();
		
		values.putString(descArg, projectDescription);
		values.putString(dateArg, projectDate);
		values.putString(nameArg, projectName);
		values.putInt(idArg, projectId);
		
		return values;
	}
	
	public ContentValues getValues()
	{
		ContentValues values = new ContentValues();
		
		values.put(ScreenProvider.KEY_PROJECTS_DESCRIPTION, projectDescription);
		values.put(ScreenProvider.KEY_PROJECTS_NAME, projectName);
		values.put(ScreenProvider.KEY_PROJECTS_DATE, projectDate);
		values.put(ScreenProvider.KEY_PROJECTS_SHARED, projectShared);
		
		return values;
	}
}
