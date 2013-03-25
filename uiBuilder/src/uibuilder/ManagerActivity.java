package uibuilder;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import data.DataBase;
import data.ScreenAdapter;
import de.ur.rk.uibuilder.R;

public class ManagerActivity extends Activity implements LoaderCallbacks<Cursor>
{
	private EditText screenName;
	
	private GridView grid;
	private ScreenAdapter adapter;
	public static final String DATABASE_SCREEN_ID = "screen";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		getLoaderManager().initLoader(DataBase.SCREENS_LOADER, null, this);
		
		Button newScreen = (Button) findViewById(R.id.new_screen_button);
		screenName = (EditText) findViewById(R.id.activity_manager_new_screen_name);
		
		newScreen.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (screenName.getText().toString() != null)
				startForNewScreen();
			}
		});
		
		adapter = new ScreenAdapter(getApplicationContext(), null, true);

		grid = (GridView)findViewById(R.id.manager_activity_project_grid);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View screen, int position,
					long id)
			{
				Log.d("itemid", String.valueOf(id));
				startForEditing(screen, id);
			}
		});
		
	}
	
	@Override
	protected void onResume()
	{
		getLoaderManager().restartLoader(DataBase.SCREENS_LOADER, null, this);
		super.onResume();
	}

	private void startForNewScreen()
	{
		putInDatabase();
		
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		
		startActivity(start);
	}
	
	private void putInDatabase()
	{
		ContentResolver res = getContentResolver();
		ContentValues values = new ContentValues();
		
		Time time = new Time();
		time.setToNow();
		
		values.put(DataBase.KEY_SCREEN_DATE, time.toString());
		values.put(DataBase.KEY_SCREEN_NAME, screenName.getText().toString());
		
		res.insert(DataBase.CONTENT_URI_SCREENS, values);
		getLoaderManager().restartLoader(DataBase.SCREENS_LOADER, null, this);
	}

	private void startForEditing(View screen, long id)
	{
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		start.putExtra(DATABASE_SCREEN_ID, (int)id);
		
		startActivity(start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String[] projection = { DataBase.KEY_ID, DataBase.KEY_SCREEN_DATE, DataBase.KEY_SCREEN_NAME };
	   
	    return new CursorLoader(getApplicationContext(), DataBase.CONTENT_URI_SCREENS, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor)
	{
		adapter.swapCursor(newCursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		adapter.swapCursor(null);		
	}

}
