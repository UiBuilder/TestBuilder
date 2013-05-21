package uibuilder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import data.ProjectHolder;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class ProjectDisplay extends Fragment implements OnClickListener, LoaderCallbacks<Cursor>, OnItemClickListener
{
	private String 
	
			projectName,
			projectDate,
			projectDescription;
	
	private int projectId;
	
	private LinearLayout root;
	private ListView sectionList;
	private LinearLayout sectionListHeader;
	
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("projectdisplay ", "oncreateView");
		root = (LinearLayout) inflater.inflate(R.layout.activity_project_manager_pager_item_layout, null);
		sectionListHeader = (LinearLayout) inflater.inflate(R.layout.activity_project_manager_section_list_header, null);

		
		setupList();
		
		Bundle values = getArguments();
		
		setScreenVars(values);
		displayProjectProperties();
		
		// TODO Auto-generated method stub
		return root; 
	}

	/**
	 * 
	 */
	private void displayProjectProperties()
	{
		TextView numberOfScreensView = (TextView) root.findViewById(R.id.project_manager_list_item_numberofscreens);
		TextView titleView = (TextView) root.findViewById(R.id.project_manager_list_item_title);
		TextView dateView = (TextView) root.findViewById(R.id.project_manager_list_item_date);
		
		titleView.setText(projectName);
		dateView.setText(projectDate);
	}

	/**
	 * @param values
	 */
	private void setScreenVars(Bundle values)
	{
		projectName = values.getString(ProjectHolder.nameArg);
		projectDescription = values.getString(ProjectHolder.descArg);
		projectDate = values.getString(ProjectHolder.dateArg);
		
		projectId = values.getInt(ProjectHolder.idArg);
	}

	/**
	 * 
	 */
	private void setupList()
	{
		sectionList = (ListView) root.findViewById(R.id.project_manager_list_item_sections);
		sectionList.addHeaderView(sectionListHeader);
		sectionList.setOnItemClickListener(this);
	}


	@Override
	public void onClick(View v)
	{

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
		
		TextView headerContent = (TextView) sectionListHeader.findViewById(R.id.project_manager_list_item_sections_header);
		headerContent.setText("Project contains " + String.valueOf(newCursor.getCount()) + " sections:");
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
		
		sectionAdapter = new SectionAdapter(getActivity(), null, true);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View listitem, int arg2, long id)
	{
		// TODO Auto-generated method stub
		
		Log.d("sectionlist", "itemclick");
		
		int projectId = (Integer) listitem.getTag();
		

		Toast.makeText(getActivity(), "launching " + String.valueOf(projectId), Toast.LENGTH_SHORT).show();
	}
	
}
