package uibuilder;

import helpers.ZoomOutPageTransformer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import data.ProjectPagerAdapter;
import de.ur.rk.uibuilder.R;

/**
 * Displays an overview over the screens created by the user. Screens can be created and deleted here.
 * @author funklos edited and stuff added by jonesses
 *
 */

public class ProjectManagerActivity extends FragmentActivity implements OnClickListener, OnItemClickListener
{
	public static final int REQUEST_SCREEN = 0x00;
	public static final int REQUEST_PROJECT = 0x01;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_manager_paged);

		
		
		setupUi();
		setupActionBar();
		//setupDatabaseConnection();
		setupInteraction();
	}

	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	
	private void setupFragments()
	{
		// TODO Auto-generated method stub
		mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ProjectPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        mPager.setAdapter(mPagerAdapter);
        
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setupFragments();
	}

	/**
	 * if a grid item is showing its delete-option-screen, back press is hiding
	 * the delete screen
	 */
	@Override
	public void onBackPressed()
	{
		if (mPager.getCurrentItem() == 0) 
		{
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } 
		else 
        {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_manager_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{

		case R.id.project_manager_menu_new_project:	
			
			Intent startWizard = new Intent(ProjectManagerActivity.this, NewProjectWizard.class);
			startActivityForResult(startWizard, REQUEST_PROJECT);
			break;

		default:
			break;
		}
		return true;
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
		if (resultCode == RESULT_OK && requestCode == REQUEST_PROJECT) 
		{
			setupFragments();
			

		}
	}

	/**
	 * Setup listeners for user interaction
	 */
	private void setupInteraction()
	{

	}



	/**
	 * Restart the loader to update the cusror data.
	 */
	private void invalidated()
	{
		//manager.restartLoader(ScreenProvider.LOADER_ID_PROJECTS, null, this);
	}

	/**
	 * initial setup for interaction
	 */
	private void setupUi()
	{
		//newProjectButton = (Button) findViewById(R.id.new_screen_button);
		//projectName = (EditText) findViewById(R.id.activity_manager_new_screen_name);
		/*
		projectList = (ListView) findViewById(R.id.project_manager_list);
		projectList.setOnItemClickListener(this);
		projectList.addHeaderView(getLayoutInflater().inflate(R.layout.activity_manager_new_screen_layout, null));*/
	}

	/**
	 * customize actionbar to match the overall ui-style of the app
	 */
	private void setupActionBar()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}

	/**
	 * Called when a new screen is requested by the user.
	 */
	private void createNewProject()
	{
		putInDatabase();
	}

	/**
	 * Insert a new row in the Screenproviders screens table. Fetch the values
	 * to insert by @see getNewScreenValues
	 * 
	 */
	private void putInDatabase()
	{/*
		ContentResolver res = getContentResolver();
		Uri inserted = res.insert(ScreenProvider.CONTENT_URI_PROJECTS, getNewScreenValues());
		int id = Integer.valueOf(inserted.getPathSegments().get(1));
		
		ContentValues testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 1.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 1");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);
		
		testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 2.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 2");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);
		
		testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 3.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 3");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);
		
		testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 1.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 1");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);
		
		testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 2.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 2");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);
		
		testValues = new ContentValues();
		testValues.put(ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT, id);
		testValues.put(ScreenProvider.KEY_SECTION_DESCRIPTION, "section description 3.1");
		testValues.put(ScreenProvider.KEY_SECTION_NAME, "section name 3");
		
		res.insert(ScreenProvider.CONTENT_URI_SECTIONS, testValues);*/
	}

	/**
	 * Generates the content Value object to insert into the screen provider
	 * 
	 * @return
	 */
/*	private ContentValues getNewScreenValues()
	{
		ContentValues values = new ContentValues();

		String now = generateDate();

		values.put(ScreenProvider.KEY_PROJECTS_DESCRIPTION, 0);
		values.put(ScreenProvider.KEY_PROJECTS_DATE, now);
		values.put(ScreenProvider.KEY_PROJECTS_NAME, projectName.getText().toString());

		return values;
	}*/

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

	@Override
	public void onClick(View v)
	{
		int id = (Integer) v.getTag();
		
		Toast.makeText(this, "launching " + String.valueOf(id), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		Log.d("onlistitem", "click");
		arg1.setSelected(true);
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
		/*
		Intent start = new Intent(getApplicationContext(), UiBuilderActivity.class);
		start.putExtra(DATABASE_SCREEN_ID, (int) id);

		startActivityForResult(start, REQUEST_SCREEN);
		overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
		*/
	}

}
