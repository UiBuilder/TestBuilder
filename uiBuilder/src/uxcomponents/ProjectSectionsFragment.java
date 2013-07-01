package uxcomponents;

import helpers.Log;
import projects.NewProjectWizard;
import uibuilder.ScreenManagerActivity;
import android.app.Fragment;
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
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class ProjectSectionsFragment extends Fragment implements LoaderCallbacks<Cursor>, OnItemClickListener, OnItemLongClickListener, OnClickListener
{
	private View root;
	private LinearLayout rootLayout, sectionListHeader;
	
	private LoaderManager manager;
	private SectionAdapter sectionAdapter;
	private ListView sectionList;
	private TextView projectNameView;
	
	private int projectId;
	//private String projectName;
	
	public static final String START_WIZARD_FOR_NEW_SCREENS = "new screen for project";
	public static final String 
								START_EDITING_PROJECT_ID = "editProjectwithId",
								START_EDITING_PROJECT_NAME = "editProjectName",
								START_EDITING_PROJECT_DESC = "editProjectDesc"
								;
	
	private static final int 
	SCREENMENU_ACTION_DELETE = 0X00,
	SCREENMENU_ACTION_EDIT = 0X01;
	
	public static final String SECTION_ID = "sectionid";
	public static final String SECTION_NAME = "sectionname";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("sections Fragment", "onCreateView called");

		Bundle values = getArguments();
		projectId = values.getInt(ScreenProvider.KEY_ID);
		//projectName = values.getString(ScreenProvider.KEY_PROJECTS_NAME);
		
		Log.d("sections received associated project id", String.valueOf(projectId));
		
		
		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_project_fragment_sections, container, false);
			rootLayout = (LinearLayout) root;
			sectionList = (ListView) root.findViewById(R.id.project_sections_fragment_sections);
			sectionListHeader = (LinearLayout) inflater.inflate(R.layout.activity_project_manager_section_list_header, null);
			//((TextView)sectionListHeader.findViewById(R.id.screens_listheader_title)).setText(projectName);
		}
		
		setupList();
		return root;
	}
	
	private void setupList()
	{	
		sectionList.addHeaderView(sectionListHeader);
		sectionList.setOnItemClickListener(this);
		sectionList.setOnItemLongClickListener(this);
		this.registerForContextMenu(sectionList);
		
		Button newScreen = (Button) sectionListHeader.findViewById(R.id.project_manager_list_item_sections_newScreen);

		newScreen.setOnClickListener(this);
		
		setupDatabaseConnection();
	}
	
	private void setupDatabaseConnection()
	{
		sectionAdapter = new SectionAdapter(getActivity(), null, true, R.layout.project_manager_list_item_section_container);
		manager = getActivity().getLoaderManager();
		manager.initLoader(projectId, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
		return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
	{
		sectionAdapter.swapCursor(arg1);
		sectionAdapter.notifyDataSetChanged();
		
		sectionList.setAdapter(sectionAdapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View listitem, int arg2, long id)
	{
		sectionList.setItemChecked(arg2, true);
		
		Bundle sectionId = (Bundle) listitem.getTag();	

		personalSectionSelectedListener.loadMyAssociatedScreens(sectionId);
		collabSectionSelectedListener.loadCollabAssociatedScreens(sectionId);
		headerSectionSelectedListener.updateUserPath(sectionId);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View listitem, int arg2,
			long arg3)
	{
		listitem.setSelected(true);
		
		return false;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.project_manager_list_item_sections_newScreen:
			
			Intent startWizard = new Intent(getActivity(), NewProjectWizard.class);
			
			startWizard.putExtra(START_WIZARD_FOR_NEW_SCREENS, projectId);
			startActivity(startWizard);
			getActivity().overridePendingTransition(R.anim.activity_transition_from_top_in, R.anim.activity_transition_to_bottom_out);
			break;

		
		default:
			break;
		}
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
		//menu.add(ContextMenu.NONE, SCREENMENU_ACTION_EDIT, ContextMenu.NONE, "Edit Screen Preferences");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Bundle screenBundle = (Bundle) info.targetView.getTag();
		int screenId = screenBundle.getInt(ScreenProvider.KEY_ID);
		
		switch (item.getItemId())
		{
		case SCREENMENU_ACTION_DELETE:
			
			//Toast.makeText(getActivity(), "deleting " + String.valueOf(screenId), Toast.LENGTH_SHORT).show();
			deleteScreenFromDb(screenId);
			break;
			
		case SCREENMENU_ACTION_EDIT:
			
			//Toast.makeText(getActivity(), "editing " + String.valueOf(screenId), Toast.LENGTH_SHORT).show();
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
	
	private String getScreenName(View v)
	{
		TextView screenNameView = (TextView) v.findViewById(R.id.project_manager_list_item_section_container_name);
		String screenName = String.valueOf(screenNameView.getText());
		return screenName;
	}
	
	public interface sectionSelectedListener
	{
		void loadMyAssociatedScreens(Bundle screenId);
		
		void loadCollabAssociatedScreens(Bundle screenId);
		
		void updateUserPath(Bundle screenId);
	}

	private static sectionSelectedListener personalSectionSelectedListener, collabSectionSelectedListener, headerSectionSelectedListener;

	public static void setPersonalSectionSelectedListener(
			sectionSelectedListener listener)
	{
		ProjectSectionsFragment.personalSectionSelectedListener = listener;
	}
	
	public static void setCollabSectionSelectedListener(
			sectionSelectedListener listener)
	{
		ProjectSectionsFragment.collabSectionSelectedListener = listener;
	}
	
	public static void setHeaderSectionSelectedListener(
			sectionSelectedListener listener)
	{
		ProjectSectionsFragment.headerSectionSelectedListener = listener;
	}
}

