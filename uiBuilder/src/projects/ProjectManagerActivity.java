package projects;

import helpers.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uibuilder.PreferencesActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cloudmodule.CloudConnection;
import cloudmodule.CloudConnection.OnFromCloudLoadedListener;
import cloudmodule.CloudConstants;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SendCallback;

import data.ProjectPagerAdapter;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

/**
 * Displays an overview over the screens created by the user. Screens can be created and deleted here.
 * @author funklos edited and stuff added by jonesses
 *
 */

public class ProjectManagerActivity extends FragmentActivity implements OnClickListener, OnItemClickListener, OnFromCloudLoadedListener
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

		setupInteraction();
		
		//Init cloud-service
		Parse.initialize(getApplicationContext(), "CJnqP0stzTozwnVqLtdREHEhI1y2kdKXAZ31SbxC", "GE9Ogahzy7djtjU66k2vSQA5GBEe2fQIUJ354t6u");
		
		//PushService.setDefaultPushCallback(getApplicationContext(), ProjectManagerActivity.class);
		PushService.startServiceIfRequired(getApplicationContext());
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		ParseInstallation ins = ParseInstallation.getCurrentInstallation();
		ins.getInstallationId();


		linkWithCloud();
	}
	
	private void linkWithCloud()
	{
		CloudConnection.setOnFromCloudLoadedListener(this);
		
		CloudConnection cloud = CloudConnection.establish(getApplicationContext(), getContentResolver());
		cloud.checkForNewCollabProjects();
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
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;
			
		case R.id.project_manager_menu_delete_project:
			
			Intent startDelete = new Intent(ProjectManagerActivity.this, DeleteProjectsActivity.class);
			startActivityForResult(startDelete, REQUEST_PROJECT);
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;
			
		case R.id.project_manager_preferences:
			
			Intent startPreferences = new Intent(ProjectManagerActivity.this, PreferencesActivity.class);
			startActivity(startPreferences);
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
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
	 * initial setup for interaction
	 */
	private void setupUi()
	{

	}

	/**
	 * customize actionbar to match the overall ui-style of the app
	 */
	private void setupActionBar()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
		bar.setSubtitle("Your Current Projects");
		bar.setTitle("Prime");
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}


	@Override
	public void onClick(View v)
	{
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		Log.d("onlistitem", "click");
		arg1.setSelected(true);
	}

	@Override
	public void usersLoaded(ArrayList<ParseUser> users)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userFound(ParseUser user)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void projectLoaded(ParseObject project)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newCloudProjectFound(String cloudProjectId)
	{
		Log.d("projectmanager received notification from cloud", "newCloudProjectFound");
		
	}

}
