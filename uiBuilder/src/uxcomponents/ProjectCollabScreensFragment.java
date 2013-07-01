package uxcomponents;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

import uibuilder.UiBuilderActivity;
import uxcomponents.ProjectSectionsFragment.sectionSelectedListener;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import data.ScreenAdapter;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectCollabScreensFragment extends Fragment implements sectionSelectedListener, LoaderCallbacks<Cursor>, OnItemClickListener, OnItemLongClickListener, OnClickListener
{
	
	private View root = null;
	private LinearLayout rootLayout, listHeader;
	
	private LoaderManager manager;
	private ScreenAdapter adapter;
	private ListView screenList;
	
	private int thisSection;
	private String thisSectionName;
	
	private Button newScreen;
	private EditText sketchName;
	public static final String DATABASE_SCREEN_ID = "screen";
	public static final int REQUEST_SCREEN = 0x00;
	public static final String RESULT_SCREEN_ID = "edited_screen";
	public static final String RESULT_IMAGE_PATH = "image_path";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ProjectSectionsFragment.setCollabSectionSelectedListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("collabscreens Fragment", "onCreateView called");

		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_project_fragment_collabscreens, container, false);
			rootLayout = (LinearLayout) root;
			
			screenList = (ListView) root.findViewById(R.id.project_collabscreens_fragment_collabscreens);
			listHeader = (LinearLayout) inflater.inflate(R.layout.layout_project_manager_myscreens_header, null);
			listHeader.findViewById(R.id.project_manager_myscreens_header_create).setVisibility(View.INVISIBLE);

			setupList();
		}
		return root;
	}

	@Override
	public void loadMyAssociatedScreens(Bundle sectionId)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadCollabAssociatedScreens(Bundle sectionId)
	{
		//Log.d("projectcollabslist received callback to load for", String.valueOf(sectionId));
		
		thisSection = sectionId.getInt(ScreenProvider.KEY_ID);
		thisSectionName = sectionId.getString(ScreenProvider.KEY_SECTION_NAME);
		
		loadScreens();
	}
	

	private void setupList()
	{	
		screenList.addHeaderView(listHeader);
		screenList.setOnItemClickListener(this);
		screenList.setOnItemLongClickListener(this);
		this.registerForContextMenu(screenList);
		
		newScreen = (Button) listHeader.findViewById(R.id.project_manager_myscreens_header_create);
		//sketchName = (EditText) listHeader.findViewById(R.id.project_manager_myscreens_header_projectname);

		newScreen.setOnClickListener(this);
	}
	
	private void loadScreens()
	{
		manager = getLoaderManager();
		
		if (manager.getLoader(113) == null)
		{
			manager.initLoader(113, null, this);
		}
		else
		{
			manager.restartLoader(113, null, this);
		}
		
		adapter = new ScreenAdapter(getActivity(), null, R.layout.screen_list_item_collabs);
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		Log.d("loader created for section with id", String.valueOf(thisSection));
		
		String currentUser = ParseUser.getCurrentUser().getObjectId();
		String selection = ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION + " = " + "'" + String.valueOf(thisSection) + "'" + 
				" AND " + ScreenProvider.KEY_SCREEN_OWNER + " != " + "'" + currentUser + "'";

		
		return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SCREENS, null, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
	{
		// TODO Auto-generated method stub
		Log.d("cursor size", String.valueOf(arg1.getCount()));
		adapter.swapCursor(arg1);
		adapter.notifyDataSetChanged();
		
		Log.d("loader finished with count", String.valueOf(arg1.getCount()));

		screenList.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		if (arg1 != listHeader)
		startForViewing(arg1, arg3);
	}
	
	
	/**
	 * start a new @see UiBuilderActivity with the associated screen id, to be
	 * used as identifier for objects which will be inserted in the objects
	 * table. shows a slide animation when showing the new activity.
	 * 
	 * @param screen
	 * @param id
	 */
	private void startForViewing(View screen, long id)
	{
		Intent start = new Intent(getActivity().getApplicationContext(), UiBuilderActivity.class);
		
		Bundle tag = (Bundle)screen.getTag();
		tag.putInt(UiBuilderActivity.MODE, UiBuilderActivity.MODE_VIEW);
		start.putExtras(tag);

		startActivityForResult(start, REQUEST_SCREEN);
		getActivity().overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateUserPath(Bundle screenId)
	{
		// TODO Auto-generated method stub
		
	}
}
