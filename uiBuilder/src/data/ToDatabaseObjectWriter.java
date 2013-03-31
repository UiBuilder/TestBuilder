package data;

import helpers.ChildGrabber;
import helpers.Log;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

public class ToDatabaseObjectWriter extends AsyncTask<View, Void, Void> 
{
	private ChildGrabber grabber;
	private int screenId;
	private Context context;
	private View root = null;

	public ToDatabaseObjectWriter(int screenId, Context context)
	{
		this.screenId = screenId;
		this.context = context;
		grabber = new ChildGrabber();
	}

	private Void writeObjects(View root)
	{
		ArrayList<View> objectList = grabber.getChildren(root);
		
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		ContentResolver cres = context.getContentResolver();
		
		for (View view : objectList)
		{
			ContentValues tempValues = Bundler.getValuePack(view);
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
		
		ContentValues[] allProperties = new ContentValues[values.size()];
		values.toArray(allProperties);
		cres.bulkInsert(ScreenProvider.CONTENT_URI_OBJECTS, allProperties);
		return null;	
	}

	@Override
	protected Void doInBackground(View... params)
	{
		root = params[0];
		
		return writeObjects(root);
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
}
