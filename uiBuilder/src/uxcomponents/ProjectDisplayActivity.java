package uxcomponents;

import com.parse.ParseUser;

import helpers.Log;
import uibuilder.UiBuilderActivity;
import uxcomponents.ProjectCollabsFragment.userSelectedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectDisplayActivity extends Activity implements LoaderCallbacks<Cursor>, userSelectedListener, OnClickListener
{
	private int projectId;
	private String projectName;
	private int colorCode;
	private Bundle startingBundle;
	
	private TextView startScreenFlow;
	
	private ProjectCollabsFragment collabFragment;
	private ProjectMyScreensFragment myscreensFragment;
	private ProjectCollabScreensFragment collabscreensFragment;
	private ProjectSectionsFragment sectionsFragment;
	
	private FragmentManager fragmentManager;
	
	private LinearLayout fragmentContainerLayout;
	private FrameLayout fragmentCollabLayout, fragmentMyScreensLayout, fragmentCollabScreensLayout, fragmentSectionsLayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent startingIntent = getIntent();
		startingBundle = startingIntent.getExtras();
		
		projectId = startingBundle.getInt(ScreenProvider.KEY_ID);
		projectName = startingBundle.getString(ScreenProvider.KEY_PROJECTS_NAME);
		colorCode = startingBundle.getInt(ScreenProvider.KEY_PROJECTS_COLOR);
		
		Log.d("project id", String.valueOf(projectId));
		ProjectCollabsFragment.setUserSelectedListener(this);
		
		setupUI();
		//setActionBarStyle();
		queryProjectData();
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		//super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
	}

	private void queryProjectData()
	{
		LoaderManager manager = getLoaderManager();
		manager.initLoader(ScreenProvider.LOADER_ID_PROJECTS, null, this);	
	}

	private void setupUI()
	{
		setContentView(R.layout.layout_project_fragment_container);
		
		startScreenFlow = (TextView) findViewById(R.id.start_flow);
		startScreenFlow.setOnClickListener(this);
		
		fragmentContainerLayout = (LinearLayout) findViewById(R.id.layout_project_fragments_container);
		fragmentCollabLayout = (FrameLayout) findViewById(R.id.layout_project_collabs_fragment);
		fragmentMyScreensLayout = (FrameLayout) findViewById(R.id.layout_project_myscreens_fragment);
		fragmentCollabScreensLayout = (FrameLayout) findViewById(R.id.layout_project_collabscreens_fragment);
		
		setupFragments();
		performInitTransaction();
	}
	

	/**
	 * 
	 */
	private void setupFragments()
	{
		fragmentManager = getFragmentManager();
		
		collabFragment = new ProjectCollabsFragment();
		myscreensFragment = new ProjectMyScreensFragment();
		collabscreensFragment = new ProjectCollabScreensFragment();
		sectionsFragment = new ProjectSectionsFragment();
		
		myscreensFragment.setArguments(startingBundle);
		collabscreensFragment.setArguments(startingBundle);
		sectionsFragment.setArguments(startingBundle);
		collabFragment.setArguments(startingBundle);
	}
	
	private void performInitTransaction()
	{
		FragmentTransaction init = fragmentManager.beginTransaction();

		init.add(R.id.layout_project_collabs_fragment, collabFragment);
		init.add(R.id.layout_project_myscreens_fragment, myscreensFragment);
		init.add(R.id.layout_project_collabscreens_fragment, collabscreensFragment);
		init.add(R.id.layout_project_sections_fragment, sectionsFragment);

		//init.hide(editbox);
		//init.hide(itembox);
		//init.hide(deletebox);

		init.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		init.commit();
	}
	
	private void setActionBarStyle()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
		
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayOptions(ActionBar.NAVIGATION_MODE_STANDARD|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		Uri projectUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_PROJECTS, projectId);
		return new CursorLoader(getApplicationContext(), projectUri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
	{
/*		if (arg0.getId() == ScreenProvider.LOADER_ID_PROJECTS)
		if (arg1.moveToFirst())
		{
			int projectNameIdx = arg1.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
			
			ActionBar bar = getActionBar();
			bar.setTitle(arg1.getString(projectNameIdx));
		}
		*/
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayUserDetails(Bundle userValues)
	{
		String userName = userValues.getString(ScreenProvider.KEY_COLLAB_NAME);
		String userId = userValues.getString(ScreenProvider.KEY_COLLAB_PARSEID);
		
		Log.d("user id selected", userId);
		
		LinearLayout userDetails = (LinearLayout) findViewById(R.id.layout_project_userdetails);
		userDetails.setVisibility(View.VISIBLE);
		
		TextView userNameDisplay = (TextView) userDetails.findViewById(R.id.layout_project_userdetails_username);
		userNameDisplay.setText(userName);
		
		
		Button remove = (Button) userDetails.findViewById(R.id.layout_project_userdetails_remove);
		remove.setVisibility(View.VISIBLE);
		remove.setTag(userValues);
		remove.setOnClickListener(collabFragment);
		
		if (userId.equals(ParseUser.getCurrentUser().getObjectId()))
		{
			remove.setVisibility(View.GONE);
		}
	}

	@Override
	public void hideUserDetails()
	{
		// TODO Auto-generated method stub
		LinearLayout userDetails = (LinearLayout) findViewById(R.id.layout_project_userdetails);
		userDetails.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.start_flow:
			startScreenFlow();
			break;

		default:
			break;
		}
		
	}
	
	private void startScreenFlow()
	{
		Intent start = new Intent(this, ScreenFlowActivity.class);
		
		
		start.putExtras(startingBundle);

		startActivity(start);
		overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
	}
	
}
