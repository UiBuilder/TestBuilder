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
import android.view.ViewGroup;

/**
 * Async Database Insertion and update for user generated objects.
 * Fetches a list of all objects in the designArea via an instance of the ChildGrabber class. 
 * 
 * @author funklos for async and @author jonesses writeObjects
 *
 */
public class ToDatabaseObjectWriter extends AsyncTask<View, Void, Void> 
{
	private ChildGrabber grabber;
	private int screenId;
	private Context context;
	private View root = null;

	/**
	 * Constructor
	 * @param screenId
	 * @param context
	 */
	public ToDatabaseObjectWriter(int screenId, Context context)
	{
		this.screenId = screenId;
		this.context = context;
		grabber = new ChildGrabber();
	}

	/**
	 * async processing
	 */
	@Override
	protected Void doInBackground(View... params)
	{
		root = params[0];
		
		return writeObjects(root);
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		ViewGroup rootAsGroup = (ViewGroup) root;
		rootAsGroup.removeAllViews();
		
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	/**
	 * async processed
	 * let the ChildGrabber instance fetch a list of references for all objects contained by the designArea.
	 * Check each views tag bundle if it already contains a database row id, update those who contain an id, by updating the 
	 * database with the contentValues object fetched by the Bundler.
	 * 
	 * The contentValues of the items without database id are put in an arrayList and bulkInserted as new database entries.
	 * 
	 * @param root
	 * @return
	 */
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
			
			Log.d("writing object with id", String.valueOf(databaseID));
			
			if (databaseID != 0)
			{
				Log.d("database insert", "update");
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
}
