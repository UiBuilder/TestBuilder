package data;

import android.content.ContentValues;
import android.os.Bundle;

public class NewScreenHolder
{
	public static final String 
						nameArg = "name",
						dateArg = "date",
						descArg = "description",
						idArg = "id"
						;
	
	
	public String sectionName;
	
	public String sectionDate;
	
	public String sectionDescription;
	
	public int sectionId;
	
	public String cloudProjectId;
	
	public ContentValues getBundle()
	{
		ContentValues values = new ContentValues();
		
		values.put(ScreenProvider.KEY_SECTION_NAME, sectionName);
		values.put(ScreenProvider.KEY_SECTION_DESCRIPTION, sectionDescription);
		values.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, sectionId);
		values.put(ScreenProvider.KEY_SECTION_ASSOCIATED_CLOUD_PROJECT, cloudProjectId);
		
		return values;
	}
}
