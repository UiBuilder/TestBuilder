package uxcomponents;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.parse.ParseUser;

import uibuilder.UiBuilderActivity;
import uxcomponents.ProjectSectionsFragment.sectionSelectedListener;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import data.ScreenAdapter;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectMyScreensFragment extends Fragment implements sectionSelectedListener, LoaderCallbacks<Cursor>, OnItemClickListener, OnItemLongClickListener, OnClickListener
{
	private View root = null;
	private LinearLayout rootLayout, listHeader;
	
	private LoaderManager manager;
	private ScreenAdapter adapter;
	private ListView screenList;
	
	private int thisSection;
	private int colorCode;
	
	private Button newScreen;
	private EditText sketchName;
	TextView titleView;
	
	public static final String DATABASE_SCREEN_ID = "screen";
	public static final int REQUEST_SCREEN = 0x00;
	public static final String RESULT_SCREEN_ID = "edited_screen";
	public static final String RESULT_IMAGE_PATH = "image_path";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ProjectSectionsFragment.setPersonalSectionSelectedListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("myscreens Fragment", "onCreateView called");

		if (root == null)
		{
			Log.d("root is null", "");
			root = inflater.inflate(R.layout.layout_project_fragment_myscreens, container, false);
			rootLayout = (LinearLayout) root;
			screenList = (ListView) root.findViewById(R.id.project_myscreens_fragment_myscreens);
			listHeader = (LinearLayout) inflater.inflate(R.layout.layout_project_manager_myscreens_header, null);
			//listHeader.setVisibility(View.INVISIBLE);
			
			Bundle startingBundle = getArguments();
			colorCode = startingBundle.getInt(ScreenProvider.KEY_PROJECTS_COLOR);
			setupList();
		}
		return root;
	}
	
	private void setupList()
	{	
		screenList.addHeaderView(listHeader);
		screenList.setOnItemClickListener(this);
		screenList.setOnItemLongClickListener(this);
		
		this.registerForContextMenu(screenList);
		
		newScreen = (Button) listHeader.findViewById(R.id.project_manager_myscreens_header_create);
		titleView = (TextView) listHeader.findViewById(R.id.myscreens_listheader_title);
		titleView.setTextColor(colorCode);
		//sketchName = (EditText) listHeader.findViewById(R.id.project_manager_myscreens_header_projectname);

		newScreen.setOnClickListener(this);
	}
	
	private void loadScreens()
	{
		manager = getLoaderManager();
		
		if (manager.getLoader(ScreenProvider.LOADER_ID_SCREENS) == null)
		{
			manager.initLoader(ScreenProvider.LOADER_ID_SCREENS, null, this);
		}
		else
		{
			manager.restartLoader(ScreenProvider.LOADER_ID_SCREENS, null, this);
		}
		
		adapter = new ScreenAdapter(getActivity(), null, R.layout.screen_list_item);
	}

	@Override
	public void loadMyAssociatedScreens(Bundle sectionId)
	{
		Log.d("loading my screens", "called");
		thisSection = sectionId.getInt(ScreenProvider.KEY_ID);
		
		loadScreens();
	}

	private void setTitle()
	{
		titleView.setText("My sketches");
	}

	@Override
	public void loadCollabAssociatedScreens(Bundle sectionId)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String currentUser;
		try {
			currentUser = ParseUser.getCurrentUser().getObjectId();
		} catch (Exception e) {
			currentUser = "";
		}
		Log.d("loader created for section with id", String.valueOf(thisSection));
		String selection = ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION + " = " + "'" + String.valueOf(thisSection) + "'" + 
								" AND " + ScreenProvider.KEY_SCREEN_OWNER + " = " + "'" + currentUser + "'";
		
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
		setTitle();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		createNewScreen();
	}
	
	
	private static final int 
	SCREENMENU_ACTION_DELETE = 0X00,
	SCREENMENU_ACTION_EDIT = 0X01;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
		
		//String screenName = getScreenName(v);
		//menu.setHeaderTitle("Options for \"" + screenName + "\"");
		
		menu.add(ContextMenu.NONE, SCREENMENU_ACTION_DELETE, ContextMenu.NONE, "Delete Screen");
		//menu.add(ContextMenu.NONE, SCREENMENU_ACTION_EDIT, ContextMenu.NONE, "Edit Screen Preferences");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Bundle screenBundle = (Bundle) info.targetView.getTag();
		int screenId = screenBundle.getInt(ScreenProvider.KEY_ID);
		
		
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
		Uri id = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, screenId);
		getActivity().getContentResolver().delete(id, null, null); 
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
		arg1.setSelected(true);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		startForEditing(arg1, arg3);
	}
	
	private void createNewScreen()
	{
		putInDatabase();
	}

	/**
	 * Insert a new row in the Screenproviders screens table. Fetch the values
	 * to insert by @see getNewScreenValues
	 * 
	 */
	private void putInDatabase()
	{
		ContentResolver res = getActivity().getContentResolver();
		res.insert(ScreenProvider.CONTENT_URI_SCREENS, getNewScreenValues());
	}

	/**
	 * Generates the content Value object to insert into the screen provider
	 * 
	 * @return
	 */
	private ContentValues getNewScreenValues()
	{
		ContentValues values = new ContentValues();

		String now = generateDate();
		String userId = "";
		
		if (ParseUser.getCurrentUser() != null)
		{
			userId = ParseUser.getCurrentUser().getObjectId();
		}

		values.put(ScreenProvider.KEY_SCREEN_PREVIEW, 0);
		values.put(ScreenProvider.KEY_SCREEN_DATE, now);
		values.put(ScreenProvider.KEY_SCREEN_NAME, "");
		values.put(ScreenProvider.KEY_SCREEN_ASSOCIATED_SECTION, thisSection);
		values.put(ScreenProvider.KEY_SCREEN_OWNER, userId); 

		return values;
	}
	
	private String generateDate()
	{
		Calendar currentDateCal = Calendar.getInstance();
		currentDateCal.setTimeZone(TimeZone.getDefault());

		Date date = currentDateCal.getTime();

		return DateFormat.format("dd.MM.yyyy, kk:mm", date).toString();
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
		Intent start = new Intent(getActivity().getApplicationContext(), UiBuilderActivity.class);
		
		Bundle tag = (Bundle)screen.getTag();
		tag.putInt(UiBuilderActivity.MODE, UiBuilderActivity.MODE_EDIT);
		start.putExtras(tag);

		startActivityForResult(start, REQUEST_SCREEN);
		getActivity().overridePendingTransition(R.anim.activity_transition_from_right_in, R.anim.activity_transition_to_left_out);
	}

	@Override
	public void updateUserPath(Bundle screenId)
	{
		// TODO Auto-generated method stub
		
	}
}
