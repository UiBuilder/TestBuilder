package uibuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import data.ScreenAdapter;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ManagerActivity extends Activity implements LoaderCallbacks<Cursor>
{
	public static final int REQUEST_SCREEN = 0x00;
	public static final String RESULT_SCREEN_ID = "edited_screen";
	public static final String RESULT_IMAGE_PATH = "image_path";
	
	private EditText screenName;
	private Button newScreenButton;
	private GridView grid;
	
	private ScreenAdapter adapter;
	public static final String DATABASE_SCREEN_ID = "screen";
	private LoaderManager manager;
	
	private boolean deleteInProgress;
	private RelativeLayout deleteScreenShowing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
				
		setupUi();	
		setupActionBar();		
		setupDatabaseConnection();	
		setupInteraction();
	}	
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	

	@Override
	public void onBackPressed()
	{
		//if a grid item is showing its delete-option-screen, back press is hiding the delete screen
		if (deleteInProgress)
		{
			returnToNormalMode(deleteScreenShowing);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	
	/**
	 * Create a new loader for the screen preview grid, querying the ScreenProviders screens table.
	 * Async database query
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{  
	    return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_SCREENS, null, null, null, null);
	}

	/**
	 * After finishing the dataloading the adapter receives a new cursor to display the containing data.
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor)
	{
		Log.d("loader", "finished");
		adapter.swapCursor(newCursor);	
		adapter.notifyDataSetChanged();
		
		grid.setAdapter(adapter);
		grid.invalidateViews();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		adapter.swapCursor(null);		
	}
	
	/**
	 * The result is delivered by the uibuilderactivity, represented as an imagepath where a screenshot 
	 * can be fetched, to be associated with the edited screen.
	 * The database entry for the screen id delivered by the intent is updated with the associated 
	 * imagepath.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == REQUEST_SCREEN)
		{	
			String imagePath = data.getStringExtra(RESULT_IMAGE_PATH);
			int id = data.getIntExtra(RESULT_SCREEN_ID, -1);
			
			if (id != -1)
			{
				ContentValues image = new ContentValues();
				image.put(ScreenProvider.KEY_SCREEN_PREVIEW, imagePath);
				
				ContentResolver res = getContentResolver();
				Uri imageUpdate = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, id);
				
				res.update(imageUpdate, image, null, null);
				//invalidated();
			}
		}	
	}

	/**
	 * Setup listeners for user interaction
	 */
	private void setupInteraction()
	{
		//the button to create a new screen
		newScreenButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				startForNewScreen();
			}
		});	
		
		//the griditems should start the editing activity with the corresponding id
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
		
		//onlongclick is showing the items delete option screen
		grid.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View item,
					int arg2, final long id)
			{
				final RelativeLayout hidden = (RelativeLayout) item.findViewById(R.id.activity_manager_griditem_deletebox);
				
				setToDeleteMode(hidden);
				
				setupDeleteScreenInteraction(item, id, hidden);
						
				return true;
			}

			/**
			 * Setup the interaction for the delete screen which is showing after a long press on a
			 * grid item.
			 * 
			 * @param item the grid item being clicked
			 * @param id the associated database id
			 * @param hidden the grid items deletescreen layout
			 */
			private void setupDeleteScreenInteraction(final View item,
					final long id, final RelativeLayout hidden)
			{
				Button confirmDeleteButton = (Button) item.findViewById(R.id.activity_manager_griditem_deletebutton);
				
				confirmDeleteButton.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						deleteScreenFromDatabase(id);

					}
				});
				
				//a click on the surrounding box, of an uncertain user, should just hide the deletescreen
				RelativeLayout deleteBox = (RelativeLayout) confirmDeleteButton.getParent();
				deleteBox.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						returnToNormalMode(hidden);
					}
				});
				
				//return to normal mode when cancel is clicked
				Button cancelButton = (Button) deleteBox.findViewById(R.id.activity_manager_griditem_cancelbutton);
				cancelButton.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{

						returnToNormalMode(hidden);
					}
				});
			}
			
			/**
			 * @param id
			 */
			private void deleteScreenFromDatabase(long id)
			{
				ContentResolver cres = getContentResolver();
				
				//delete the screen in screens table, automatically deletes all depending entries in tables
				Uri uri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, id);
				cres.delete(uri, null, null);
				invalidated();
				
				deleteInProgress = false;
			}
		});
	}
	/**
	 * @param deleteScreen
	 */
	private void setToDeleteMode(RelativeLayout deleteScreen)
	{
		deleteScreen.setVisibility(View.VISIBLE);
		deleteInProgress = true;
		deleteScreenShowing = deleteScreen;
	}

	
	/**
	 * @param item
	 * @param hidden
	 */
	private void returnToNormalMode(RelativeLayout deleteScreen)
	{
		deleteScreen.setVisibility(View.INVISIBLE);
		deleteInProgress = false;
		deleteScreenShowing = null;
	}

	/**
	 * 
	 */
	private void setupDatabaseConnection()
	{
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.SCREENS_LOADER, null, this);
		adapter = new ScreenAdapter(getApplicationContext(), null, true);

	}
	
	private void invalidated()
	{
		manager.restartLoader(ScreenProvider.SCREENS_LOADER, null, this);
	}

	/**
	 * 
	 */
	private void setupUi()
	{
		newScreenButton = (Button) findViewById(R.id.new_screen_button);
		screenName = (EditText) findViewById(R.id.activity_manager_new_screen_name);
		grid = (GridView)findViewById(R.id.manager_activity_project_grid);
	}

	/**
	 * customize actionbar
	 */
	private void setupActionBar()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}

	private void startForNewScreen()
	{
		putInDatabase();
	}
	
	private void putInDatabase()
	{
		ContentResolver res = getContentResolver();
		ContentValues values = new ContentValues();
		
		Time time = new Time();
		String tz = Time.getCurrentTimezone();
		time.switchTimezone(tz);
		time.setToNow();

	    
		values.put(ScreenProvider.KEY_SCREEN_PREVIEW, 0);
		values.put(ScreenProvider.KEY_SCREEN_DATE, time.format3339(false));
		values.put(ScreenProvider.KEY_SCREEN_NAME, screenName.getText().toString());
		
		res.insert(ScreenProvider.CONTENT_URI_SCREENS, values);
		
		invalidated();
	}

	private void startForEditing(View screen, long id)
	{
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		start.putExtra(DATABASE_SCREEN_ID, (int)id);
		
		startActivityForResult(start, REQUEST_SCREEN);
		overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
	}


}
