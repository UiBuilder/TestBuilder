package data;

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
}
