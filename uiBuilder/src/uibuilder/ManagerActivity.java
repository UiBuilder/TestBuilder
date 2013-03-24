package uibuilder;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import data.DataBase;
import data.ScreenAdapter;
import de.ur.rk.uibuilder.R;

public class ManagerActivity extends Activity implements LoaderCallbacks<Cursor>
{

	private GridView grid;
	private ScreenAdapter adapter;
	public static final String DATABASE_SCREEN_ID = "screen";
	
	private static final int SCREENS_LOADER = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		getLoaderManager().initLoader(SCREENS_LOADER, null, this);
		
		Button newScreen = (Button) findViewById(R.id.new_screen_button);
		newScreen.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
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
				startForEditing(screen);
			}
		});
		
	}
	
	private void startForNewScreen()
	{
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		
		startActivity(start);
	}
	
	private void startForEditing(View screen)
	{
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		start.putExtra(DATABASE_SCREEN_ID, screen.getId());
		
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
		String[] projection = { DataBase.KEY_ID, DataBase.KEY_DATE, DataBase.KEY_NAME };
	   
	    return new CursorLoader(getApplicationContext(), DataBase.CONTENT_URI, projection, null, null, null);
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