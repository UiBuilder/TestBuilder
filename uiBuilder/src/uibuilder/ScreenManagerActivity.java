package uibuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import projects.ProjectDisplay;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

/**
 * Displays an overview over the screens created by the user. Screens can be created and deleted here.
 * @author funklos edited and stuff added by jonesses
 *
 */

public class ScreenManagerActivity extends Activity implements LoaderCallbacks<Cursor>
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
	
	private int thisSection;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Intent startingIntent = getIntent();
		thisSection = startingIntent.getIntExtra(ProjectDisplay.SECTION_ID, 0);
		String thisName = startingIntent.getStringExtra(ProjectDisplay.SECTION_NAME);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);

		setupUi();
		setupActionBar(thisName);
		setupDatabaseConnection();
		setupInteraction();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	/**
	 * if a grid item is showing its delete-option-screen, back press is hiding
	 * the delete screen
	 */
	@Override
	public void onBackPressed()
	{
		if (deleteInProgress) 
		{
			returnToNormalMode(deleteScreenShowing);
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_manager_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{

		case R.id.manager_menu_action_about:	
			
			Intent aboutIntent = new Intent(ScreenManagerActivity.this, AboutActivity.class);
			startActivity(aboutIntent);
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * Create a new loader for the screen preview grid, querying the
	 * ScreenProviders screens table. Async database query
	 * USES thisSection int for identifying
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String selection = ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION + " = " + "'" + String.valueOf(thisSection) + "'";
		
		return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_SCREENS, null, selection, null, null);
	}

	/**
	 * After finishing the dataloading the adapter receives a new cursor to
	 * display the containing data.
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor)
	{
		Log.d("loader", "finished");
		adapter.swapCursor(newCursor);
		adapter.notifyDataSetChanged();

		grid.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		adapter.swapCursor(null);
	}

	/**
	 * The result is delivered by the uibuilderactivity, represented as an
	 * imagepath where a screenshot can be fetched, to be associated with the
	 * edited screen. The database entry for the screen id delivered by the
	 * intent is updated with the associated imagepath.
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
				invalidated();
			}
		}
	}

	/**
	 * Setup listeners for user interaction
	 */
	private void setupInteraction()
	{
		// the button to create a new screen
		newScreenButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				createNewScreen();
				
				resetDialog();
			}

			/**
			 * 
			 */
			private void resetDialog()
			{
				screenName.setText("");
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(screenName.getWindowToken(), 0);
			}
		});

		// the griditems should start the editing activity with the
		// corresponding id
		grid.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View screen, int position, long id)
			{
				Log.d("itemid", String.valueOf(id));
				startForEditing(screen, id);
			}
		});

		// onlongclick is showing the items delete option screen
		grid.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View item, int arg2, final long id)
			{
				final RelativeLayout hidden = (RelativeLayout) item.findViewById(R.id.activity_manager_griditem_deletebox);

				setToDeleteMode(hidden);

				setupDeleteScreenInteraction(item, id, hidden);

				return true;
			}

			/**
			 * Setup the interaction for the delete screen which is showing
			 * after a long press on a grid item.
			 * 
			 * @param item
			 *            the grid item being clicked
			 * @param id
			 *            the associated database id
			 * @param hidden
			 *            the grid items deletescreen layout
			 */
			private void setupDeleteScreenInteraction(final View item, final long id, final RelativeLayout hidden)
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

				// a click on the surrounding box, of an uncertain user, should
				// just hide the deletescreen
				RelativeLayout deleteBox = (RelativeLayout) confirmDeleteButton.getParent();
				deleteBox.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						returnToNormalMode(hidden);
					}
				});

				// return to normal mode when cancel is clicked
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
				
				// delete the screen in screens table, automatically deletes all
				// depending entries in tables
				Uri uri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, id);
				cres.delete(uri, null, null);
				invalidated();

				deleteInProgress = false;
			}
		});
	}

	/**
	 * Shows the items deleteScreen, which is initially invisible.
	 * 
	 * @param deleteScreen
	 *            the gridItems layout, which is requesting the operation
	 */
	private void setToDeleteMode(RelativeLayout deleteScreen)
	{
		if (deleteScreenShowing != null)
		{
			deleteScreenShowing.setVisibility(View.INVISIBLE);
		}
		deleteScreen.setVisibility(View.VISIBLE);
		deleteInProgress = true;
		deleteScreenShowing = deleteScreen;
	}

	/**
	 * Hides the deleteScreen part of the layout again.
	 * 
	 * @param item
	 *            the gridItems layout, which is requesting the operation
	 */
	private void returnToNormalMode(RelativeLayout deleteScreen)
	{
		grid.clearFocus();
		deleteScreen.setVisibility(View.INVISIBLE);
		deleteInProgress = false;
		deleteScreenShowing = null;
	}

	/**
	 * Initialize an async loader, to load the saved screens from the database
	 * contained in @see ScreenProvider and init a new adapter to be connected
	 * to the grid in @see onLoadFinished
	 */
	private void setupDatabaseConnection()
	{
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.LOADER_ID_SCREENS, null, this);
		//adapter = new ScreenAdapter(getApplicationContext(), null, true);
	}

	/**
	 * Restart the loader to update the cusror data.
	 */
	private void invalidated()
	{
		manager.restartLoader(ScreenProvider.LOADER_ID_SCREENS, null, this);
	}

	/**
	 * initial setup for interaction
	 */
	private void setupUi()
	{
		newScreenButton = (Button) findViewById(R.id.new_screen_button);
		screenName = (EditText) findViewById(R.id.activity_manager_new_screen_name);
		grid = (GridView) findViewById(R.id.manager_activity_project_grid);
	}

	/**
	 * customize actionbar to match the overall ui-style of the app
	 */
	private void setupActionBar(String name)
	{
		ActionBar bar = getActionBar();
		
		Log.d("name is", name);
		bar.setTitle(name);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}

	/**
	 * Called when a new screen is requested by the user.
	 */
	private void createNewScreen()
	{
		putInDatabase();
	}

	/**
	 * Insert a new row in the Screenproviders screens table. Fetch the values
	 * to insert by @see getNewScreenValues
	 * 
	 */
	private void putInDatabase()
	{
		ContentResolver res = getContentResolver();
		res.insert(ScreenProvider.CONTENT_URI_SCREENS, getNewScreenValues());
	}

	/**
	 * Generates the content Value object to insert into the screen provider
	 * 
	 * @return
	 */
	private ContentValues getNewScreenValues()
	{
		ContentValues values = new ContentValues();

		String now = generateDate();

		values.put(ScreenProvider.KEY_SCREEN_PREVIEW, 0);
		values.put(ScreenProvider.KEY_SCREEN_DATE, now);
		values.put(ScreenProvider.KEY_SCREEN_NAME, screenName.getText().toString());
		values.put(ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION, thisSection);

		return values;
	}

	/**
	 * generate a new date string based on the users timezone.
	 * 
	 * @return
	 */
	private String generateDate()
	{
		Calendar currentDateCal = Calendar.getInstance();
		currentDateCal.setTimeZone(TimeZone.getDefault());

		Date date = currentDateCal.getTime();

		return DateFormat.format("dd.MM.yyyy, kk:mm", date).toString();
	}

	/**
	 * start a new @see UiBuilderActivity with the associated screen id, to be
	 * used as identifier for objects which will be inserted in the objects
	 * table. shows a slide animation when showing the new activity.
	 * 
	 * @param screen
	 * @param id
	 */
	private void startForEditing(View screen, long id)
	{
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		start.putExtra(DATABASE_SCREEN_ID, (int) id);

		startActivityForResult(start, REQUEST_SCREEN);
		overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
	}

}
