package projects;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import data.ProjectCursorAdapter;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class DeleteProjectsActivity extends Activity implements OnClickListener, LoaderCallbacks<Cursor>, OnItemClickListener
{

	
	private ContentResolver resolver;
	private ListView projectList;
	
	private LoaderManager manager;
	private ProjectCursorAdapter projectAdapter;

	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_delete_project_root);
		
		Intent startIntent = getIntent();
		
		setupActionBar();
		
		setupAnimations();
        
        setupUi();

        resolver = getContentResolver();
        setupDatabaseConnection();

	}
	
	private void setupDatabaseConnection()
	{
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.LOADER_ID_PROJECTS, null, this);		
		
		projectAdapter = new ProjectCursorAdapter(getApplicationContext(), R.layout.activity_delete_project_adapter_layout, null, new String[] {ScreenProvider.KEY_PROJECTS_NAME}, new int[] {R.id.delete_project_adapter_project_title}, 0);
	}
	
	/**
	 * 
	 */
	private void setupAnimations()
	{

	}
	/**
	 * 
	 */
	private void setupUi()
	{
		Button cancel = (Button) findViewById(R.id.project_delete_cancel);
		Button delete = (Button) findViewById(R.id.project_delete_confirm);
		
		cancel.setOnClickListener(this);
		delete.setOnClickListener(this);
		
		projectList = (ListView) findViewById(R.id.project_delete_projectlist);
		projectList.setOnItemClickListener(this);
	}
	
	private void returnToManager()
	{
		finish();
		overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out);
	}

	
	/**
	 * customize actionbar to match the overall ui-style of the app
	 */
	private void setupActionBar()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setHomeButtonEnabled(true);
		
		bar.setTitle("New Project Wizard");
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}
	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.project_delete_cancel:

			returnToManager();
			break;
	
		case R.id.project_delete_confirm:
			
			performSelectedDeletion();
			break;
			
		}
	}
	private void performSelectedDeletion()
	{
		// TODO Auto-generated method stub
		int projectNumber = projectList.getCount();
		
		Log.d("performing deletion of ", String.valueOf(projectNumber));
		
		SparseBooleanArray checkedOrNot = projectList.getCheckedItemPositions();
		ArrayList<View> checkedProjects = new ArrayList<View>();
		
		for (int i=0; i<projectNumber; i++)
		{
			if(checkedOrNot.get(i))
			{
				checkedProjects.add(projectList.getChildAt(i));
			}
		}
		
		for (View projectView: checkedProjects)
		{
			int projectId = (Integer) projectView.getTag();
			
			Uri projectpath = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_PROJECTS, projectId);
			resolver.delete(projectpath, null, null);
		}
	}
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		// TODO Auto-generated method stub
		return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_PROJECTS, null, null, null, null);
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor)
	{
		// TODO Auto-generated method stub
		projectAdapter.swapCursor(newCursor);
		projectAdapter.notifyDataSetChanged();

		projectList.setAdapter(projectAdapter);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub
		projectAdapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View listItem, int itemId, long arg3)
	{
		// TODO Auto-generated method stub
		
		if (projectList.isItemChecked(itemId))
		{
			Log.d("listitem", "checking");
			projectList.setItemChecked(itemId, true);
			listItem.setActivated(true);
			listItem.invalidate();
		}
		else
		{
			projectList.setItemChecked(itemId, false);
			listItem.setActivated(false);
			listItem.invalidate();
		}
		
		int projectId = (Integer) listItem.getTag();
		Log.d("item checked", String.valueOf(projectList.isItemChecked(itemId)));
		Toast.makeText(getApplicationContext(), "Project id is " + String.valueOf(projectId), Toast.LENGTH_SHORT).show();
	}

}
