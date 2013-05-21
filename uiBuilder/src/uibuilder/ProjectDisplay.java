package uibuilder;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import data.ProjectHolder;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class ProjectDisplay extends Fragment implements OnClickListener, LoaderCallbacks<Cursor>, OnItemClickListener, OnItemLongClickListener
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
		
		Button newScreen = (Button) sectionListHeader.findViewById(R.id.project_manager_list_item_sections_newScreen);
		newScreen.setOnClickListener(this);
		
		// TODO Auto-generated method stub
		return root; 
	}

	/**
	 * 
	 */
	private void displayProjectProperties()
	{
		
		TextView projectTitleView = (TextView) root.findViewById(R.id.project_manager_display_project_title);
		TextView projectDateView = (TextView) root.findViewById(R.id.project_manager_display_project_date);
		TextView projectDescriptionView = (TextView) root.findViewById(R.id.project_manager_display_project_description);
		
		projectTitleView.setText(projectName);
		projectDateView.setText(projectDate);
		projectDescriptionView.setText(projectDescription);
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
		sectionList.setOnItemLongClickListener(this);
		this.registerForContextMenu(sectionList);
	}

	public static final String START_WIZARD_FOR_NEW_SCREENS = "new screen for project";
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.project_manager_list_item_sections_newScreen:
			
			Intent startWizard = new Intent(getActivity(), NewProjectWizard.class);
			
			startWizard.putExtra(START_WIZARD_FOR_NEW_SCREENS, projectId);
			startActivity(startWizard);
			break;

		default:
			break;
		}
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
		Log.d("adapter stable ids", String.valueOf(sectionAdapter.hasStableIds()));
		
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
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int screenId = (Integer) info.targetView.getTag();
		
		
		switch (item.getItemId())
		{
		case SCREENMENU_ACTION_DELETE:
			
			Toast.makeText(getActivity(), "deleting " + String.valueOf(screenId), Toast.LENGTH_SHORT).show();
			deleteScreenFromDb(screenId);
			break;
			
		case SCREENMENU_ACTION_EDIT:
			
			Toast.makeText(getActivity(), "editing " + String.valueOf(screenId), Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}


	private void deleteScreenFromDb(int screenId)
	{
		Uri id = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SECTIONS, screenId);
		getActivity().getContentResolver().delete(id, null, null); 
		
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
		
		String screenName = getScreenName(v);
		menu.setHeaderTitle("Options for \"" + screenName + "\"");
		
		menu.add(ContextMenu.NONE, SCREENMENU_ACTION_DELETE, ContextMenu.NONE, "Delete Screen");
		menu.add(ContextMenu.NONE, SCREENMENU_ACTION_EDIT, ContextMenu.NONE, "Edit Screen Preferences");
	}


	/**
	 * @param v
	 * @return
	 */
	private String getScreenName(View v)
	{
		TextView screenNameView = (TextView) v.findViewById(R.id.project_manager_list_item_section_container_name);
		String screenName = String.valueOf(screenNameView.getText());
		return screenName;
	}

	private static final int 
					SCREENMENU_ACTION_DELETE = 0X00,
					SCREENMENU_ACTION_EDIT = 0X01;
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View listitem, int arg2, long id)
	{
		// TODO Auto-generated method stub
		
		Log.d("sectionlist", "itemclick");
		
		int sectionId = (Integer) listitem.getTag();	
		
		Toast.makeText(getActivity(), "launching " + String.valueOf(projectId), Toast.LENGTH_SHORT).show();
		
		startScreenManager(sectionId, listitem);
	}

	public static final String SECTION_ID = "sectionid";
	public static final String SECTION_NAME = "sectionname";

	private void startScreenManager(int sectionId, View listitem)
	{
		Intent start = new Intent(getActivity(), ScreenManagerActivity.class);
		start.putExtra(SECTION_ID, sectionId);
		start.putExtra(SECTION_NAME, getScreenName(listitem));
		
		startActivity(start);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View listitem, int arg2,
			long arg3)
	{
		listitem.setSelected(true);
		
		return false;
	}
	
}
