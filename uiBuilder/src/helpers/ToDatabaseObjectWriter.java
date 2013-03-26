package helpers;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import data.DataBase;

public class ToDatabaseObjectWriter
{
	private ChildGrabber grabber;
	private int screenId;
	private Context context;

	

	public ToDatabaseObjectWriter(View view, int screenId, Context context)
	{
		this.screenId = screenId;
		this.context = context;
		grabber = new ChildGrabber();
		ArrayList<View> objectList;
		objectList = grabber.getChildren(view);
		
		writeObjects(objectList);
		
	}

	public void writeObjects(ArrayList<View> objectList)
	{
		for (View view : objectList)
		{
			ContentValues tempValues = ObjectValueCollector.getValuePack(view);
			tempValues.put(DataBase.KEY_OBJECTS_SCREEN, screenId);

			int databaseID = tempValues.getAsInteger(DataBase.KEY_ID);

			

			ContentResolver cres = context.getContentResolver();

			if (databaseID == 0)
			{
				cres.insert(DataBase.CONTENT_URI_OBJECTS, tempValues);
			} else
			{
				Uri uri = ContentUris.withAppendedId(DataBase.CONTENT_URI_OBJECTS, databaseID);
				cres.update(uri, tempValues, null, null);
			}
		}
	}
}
