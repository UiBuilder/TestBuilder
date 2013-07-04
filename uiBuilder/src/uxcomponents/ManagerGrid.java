package uxcomponents;

import projects.DeleteProjectsActivity;
import projects.NewProjectWizard;
import uibuilder.PreferencesActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ManagerGrid extends Activity implements LoaderCallbacks<Cursor>, OnItemClickListener
{
	private ManagerGridAdapter adapter;
	private GridView grid;
	private LoaderManager manager;
	
	public static final int REQUEST_PROJECT = 0x01;
	
	private static final int 
	SCREENMENU_ACTION_DELETE = 0X00,
	SCREENMENU_ACTION_EDIT = 0X01;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_grid_manager_layout);
		
		setupUi();
		setupActionBar();
		setupDatabaseConnection();
		setupInteraction();
		
		Parse.initialize(getApplicationContext(), "CJnqP0stzTozwnVqLtdREHEhI1y2kdKXAZ31SbxC", "GE9Ogahzy7djtjU66k2vSQA5GBEe2fQIUJ354t6u");
		
		//PushService.setDefaultPushCallback(getApplicationContext(), ProjectManagerActivity.class);
		PushService.startServiceIfRequired(getApplicationContext());
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		ParseInstallation ins = ParseInstallation.getCurrentInstallation();
		ins.getInstallationId();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_manager_menu, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
		
		//String screenName = getScreenName(v);
		//menu.setHeaderTitle("Options for \"" + screenName + "\"");
		
		menu.add(ContextMenu.NONE, SCREENMENU_ACTION_DELETE, ContextMenu.NONE, "Delete Project");
		//menu.add(ContextMenu.NONE, SCREENMENU_ACTION_EDIT, ContextMenu.NONE, "Edit Screen Preferences");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Bundle projectBundle = (Bundle) info.targetView.getTag();
		int projectId = projectBundle.getInt(ScreenProvider.KEY_ID);
		
		switch (item.getItemId())
		{
		case SCREENMENU_ACTION_DELETE:
			
			deleteProjectFromDb(projectId);
			break;
			
		case SCREENMENU_ACTION_EDIT:
			
			//Toast.makeText(getActivity(), "editing " + String.valueOf(screenId), Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	private void deleteProjectFromDb(int projectId)
	{
		Uri id = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_PROJECTS, projectId);
		getContentResolver().delete(id, null, null); 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{

		case R.id.project_manager_menu_new_project:	
			
			Intent startWizard = new Intent(ManagerGrid.this, NewProjectWizard.class);
			startActivityForResult(startWizard, REQUEST_PROJECT);
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;
			
		/*case R.id.project_manager_menu_delete_project:
			
			Intent startDelete = new Intent(ManagerGrid.this, DeleteProjectsActivity.class);
			startActivityForResult(startDelete, REQUEST_PROJECT);
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;*/
			
		case R.id.project_manager_preferences:
			
			Intent startPreferences = new Intent(ManagerGrid.this, PreferencesActivity.class);
			startActivity(startPreferences);
			overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;

		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == REQUEST_PROJECT) 
		{
			manager.restartLoader(ScreenProvider.LOADER_ID_PROJECTS, null, this);
		}
	}

	private void setupInteraction()
	{
		// TODO Auto-generated method stub
		
	}

	private void setupDatabaseConnection()
	{
		// TODO Auto-generated method stub
		adapter = new ManagerGridAdapter(getApplicationContext(), null, true);
		
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.LOADER_ID_PROJECTS, null, this);
	}

	private void setupActionBar()
	{
		ActionBar bar = getActionBar();
		
		//Log.d("name is", name);
		//bar.setTitle(name);
		//bar.setDisplayHomeAsUpEnabled(true);
		//bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}

	private void setupUi()
	{
		grid = (GridView) findViewById(R.id.grid_manager_grid);
		grid.setOnItemClickListener(this);
		this.registerForContextMenu(grid);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_PROJECTS, null, null, null, ScreenProvider.KEY_PROJECTS_DATE);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
	{
		// TODO Auto-generated method stub
		adapter.swapCursor(arg1);
		adapter.notifyDataSetChanged();
		grid.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long projectId)
	{
		Intent showProject = new Intent(ManagerGrid.this, ProjectDisplayActivity.class);
		showProject.putExtras((Bundle)arg1.getTag());
		
		startActivityForResult(showProject, REQUEST_PROJECT);
		overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out); 
		
	}

}
