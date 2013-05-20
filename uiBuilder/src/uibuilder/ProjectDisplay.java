package uibuilder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import data.SectionAdapter;
import data.ProjectHolder;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectDisplay extends Fragment implements OnClickListener, LoaderCallbacks<Cursor>
{
	private String 
	
			projectName,
			projectDate,
			projectDescription;
	
	private int projectId;
	
	private LinearLayout root;
	private ListView sectionList;
	private LoaderManager manager;
	private SectionAdapter sectionAdapter;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("projectdisplay ", "oncreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("projectdisplay ", "oncreateView");
		root = (LinearLayout) inflater.inflate(R.layout.activity_project_manager_list_item_layout, null);
		sectionList = (ListView) root.findViewById(R.id.project_manager_list_item_sections);
		
		Bundle values = getArguments();
		
		projectName = values.getString(ProjectHolder.nameArg);
		projectDescription = values.getString(ProjectHolder.descArg);
		projectDate = values.getString(ProjectHolder.dateArg);
		
		projectId = values.getInt(ProjectHolder.idArg);
		
		
		TextView numberOfScreensView = (TextView) root.findViewById(R.id.project_manager_list_item_numberofscreens);
		TextView titleView = (TextView) root.findViewById(R.id.project_manager_list_item_title);
		TextView dateView = (TextView) root.findViewById(R.id.project_manager_list_item_date);
		
		titleView.setText(projectName);
		dateView.setText(projectDate);
		
		// TODO Auto-generated method stub
		return root; 
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		int id = (Integer) v.getTag();
		
		Toast.makeText(getActivity(), "launching " + String.valueOf(id), Toast.LENGTH_SHORT).show();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args)
	{
		switch (loaderId)
		{	
		case ScreenProvider.LOADER_ID_SECTIONS:
			
			//String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
			//return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);

		default:
			
			String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
			return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
			
		}
		//return null;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor)
	{
		switch (loader.getId())
		{	
		case ScreenProvider.LOADER_ID_SECTIONS:
			
			
			break;

		default:
			break;
		}
		Log.d("loader", "finished");
		sectionAdapter.swapCursor(newCursor);
		sectionAdapter.notifyDataSetChanged();

		sectionList.setAdapter(sectionAdapter);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		// TODO Auto-generated method stub
		switch (loader.getId()) 
		{
		case ScreenProvider.LOADER_ID_PROJECTS:
			
			break;

		default:
			break;
		}
		sectionAdapter.swapCursor(null);
	}
	
	private void setupDatabaseConnection()
	{
		manager = getActivity().getLoaderManager();
		manager.initLoader(projectId, null, this);		
		
		sectionAdapter = new SectionAdapter(getActivity(), null, true, this);
		/*
		ContentResolver res = getActivity().getContentResolver();
		
		String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
		Cursor sectionCursor = res.query(ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
		
		sectionAdapter.swapCursor(sectionCursor);
		sectionAdapter.notifyDataSetChanged();
		
		sectionList.setAdapter(sectionAdapter);*/
	}


	@Override
	public void onResume()
	{
		
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("projectdisplay", "onresume");
		setupDatabaseConnection();
	}

	@Override
	public void onPause()
	{
		manager.destroyLoader(projectId);
		// TODO Auto-generated method stub
		Log.d("display", "pausing");
		super.onPause();
	}
	
}
