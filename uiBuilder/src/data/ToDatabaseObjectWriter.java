package data;

import helpers.ChildGrabber;
import helpers.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.View;

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
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		ContentResolver cres = context.getContentResolver();
		
		for (View view : objectList)
		{
			ContentValues tempValues = ObjectValueCollector.getValuePack(view);
			tempValues.put(ScreenProvider.KEY_OBJECTS_SCREEN, screenId);

			int databaseID = tempValues.getAsInteger(ObjectValues.DATABASE_ID);
			tempValues.remove(ObjectValues.DATABASE_ID);
			
			if (databaseID != 0)
			{
				Uri uri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_OBJECTS, databaseID);
				cres.update(uri, tempValues, null, null);
			}
			else
			{
				values.add(tempValues);
			}
		}
		Log.d("contentvals", String.valueOf(values.size()));
		
		ContentValues[] a = new ContentValues[values.size()];
		values.toArray(a);
		cres.bulkInsert(ScreenProvider.CONTENT_URI_OBJECTS, a);
		
	}
}
