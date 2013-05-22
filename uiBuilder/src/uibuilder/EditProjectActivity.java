package uibuilder;

import helpers.OptionsArrayAdapter;
import android.app.ActionBar;
import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ViewFlipper;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class EditProjectActivity extends Activity implements LoaderCallbacks<Cursor>, OnItemClickListener, OnClickListener
{
	private ViewFlipper flipper;
	private int projectId;
	private String projectName, projectDescription;
	
	private ContentResolver resolver;
	private LoaderManager manager;
	
	//OPTIONSPAGE
	private ListView optionsList;
	private Options[] optionListItems;
	
	//NAME AND DESCRIPTION PAGE
	private EditText namedescProjectName;
	private EditText namedescProjectDesc;	
	
	//SCREENS OPTIONS
	private SectionAdapter sectionAdapter;
	private ListView sectionList;
	
	private OptionsArrayAdapter optionsListAdapter;
	
	private Animation
	slide_top_in,
	slide_bottom_in,
	slide_top_out,
	slide_bottom_out;

	@Override
	public void onBackPressed()
	{
		if (flipper.getDisplayedChild() == 0)
		{
			super.onBackPressed();
			overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out);
		}
		else
		{
			flipper.setInAnimation(slide_bottom_out);
			flipper.setOutAnimation(slide_top_in);
			flipper.setDisplayedChild(0);
		}
	}
	

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		initHelpers();
		setupAnimations();
		
		optionsList.setItemChecked(0, true);
		
		//optionsList.getChildAt(0).setActivated(true);
		activeListItemPos = 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_project_root);
		setupActionBar();
		
		Intent startIntent = getIntent();
		int tempId = startIntent.getIntExtra(ProjectDisplay.START_EDITING_PROJECT_ID, -1);
		Log.d("Edit Project received id ", String.valueOf(tempId));
		if (tempId != -1)
		{
			projectId = tempId;
			projectName = startIntent.getStringExtra(ProjectDisplay.START_EDITING_PROJECT_NAME);
			projectDescription = startIntent.getStringExtra(ProjectDisplay.START_EDITING_PROJECT_DESC);
		}
			
		setupMainUI();
		setupOptionsPage();
		setupNameDescPage();
		setupIconPage();
		setupScreenNameDescPage();
		setupCollabsPage();
	}
	
	private void setupCollabsPage()
	{
		// TODO Auto-generated method stub
		
	}


	private void setupScreenNameDescPage()
	{
		sectionList = (ListView) findViewById(R.id.project_edit_screens_screenlist);
			
		sectionAdapter = new SectionAdapter(getApplicationContext(), null, true, R.layout.project_manager_list_item_section_container_small);
	}


	private void setupIconPage()
	{
		// TODO Auto-generated method stub
		
	}


	private void setupNameDescPage()
	{
		namedescProjectDesc = (EditText) findViewById(R.id.project_edit_nameanddesc_description);
		namedescProjectName = (EditText) findViewById(R.id.project_edit_nameanddesc_projectname);
		
		namedescProjectDesc.setText(projectDescription);
		namedescProjectName.setText(projectName);
		
		Button save = (Button) findViewById(R.id.project_edit_nameanddesc_submit);
		save.setOnClickListener(this);
	}
	
	private void setupOptionsPage()
	{
		optionsList = (ListView) findViewById(R.id.project_edit_optionsscreen_optionslist);
		
		String[] optionsListNames = getResources().getStringArray(R.array.project_options_list_names);
		String[] optionsListDescs = getResources().getStringArray(R.array.project_options_list_descriptions);
		
		optionListItems = Options.getOptions(optionsListNames, optionsListDescs);
		optionsListAdapter = new OptionsArrayAdapter(getApplicationContext(), 0, optionListItems);
		
		optionsList.setAdapter(optionsListAdapter);
		optionsList.setOnItemClickListener(this);
		
	}

	private void initHelpers()
	{
		resolver = getContentResolver();
		manager = getLoaderManager();
		
		getProjectData();
	}

	private void getProjectData()
	{
		manager.initLoader(ScreenProvider.LOADER_ID_SECTIONS, null, this);
	}

	private void setupMainUI()
	{

		flipper = (ViewFlipper) findViewById(R.id.project_edit_flipper);
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
	
	/**
	 * 
	 */
	private void setupAnimations()
	{
		slide_top_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_top_in);
        slide_bottom_in = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_bottom_in);
        slide_top_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_top_out);
        slide_bottom_out = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_bottom_out);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		switch (id)
		{
		
		case ScreenProvider.LOADER_ID_SECTIONS:
			
			String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
			return new CursorLoader(this, ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);

		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		switch (loader.getId())
		{
		
		case ScreenProvider.LOADER_ID_SECTIONS:
			
			setProjectRelatedSectionsValues();
			break;

		}
		
		sectionAdapter.swapCursor(cursor);
		sectionAdapter.notifyDataSetChanged();

		sectionList.setAdapter(sectionAdapter);
	}

	private void setProjectRelatedSectionsValues()
	{
		// TODO Auto-generated method stub
		
	}
	
	
	private void setProjectValues(Cursor cursor)
	{
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		switch (loader.getId())
		{
		case ScreenProvider.LOADER_ID_PROJECTS:
			
			break;
		
		case ScreenProvider.LOADER_ID_SECTIONS:
			
			sectionAdapter.swapCursor(null);
			break;

		}
	}
	
	public static class Options
	{
		public String name, description;
		
		public Options (String name, String description)
		{
			this.name = name;
			this.description = description;
		}
		
		public static Options[] getOptions (String[] nameString, String[] descString)
		{
			Options[] options = new Options[nameString.length];
			
			for (int i=0; i<nameString.length; i++)
			{
				options[i] = new Options(nameString[i], descString[i]);
			}
			return options;
		}
	}
	
	private int activeListItemPos;

	@Override
	public void onItemClick(AdapterView<?> list, View item, int id, long arg3)
	{
		// TODO Auto-generated method stub
		Log.d("edit list item is", String.valueOf(id));
		
		switch (list.getId())
		{
		case R.id.project_edit_optionsscreen_optionslist:
			
			if (id != activeListItemPos)
			{
				if (id > activeListItemPos)
				{
					setFlipperMovement(MOVE_FORWARD);
				}
				else
				{
					setFlipperMovement(MOVE_BACK);
				}
				flipper.setDisplayedChild(id);
				
				optionsList.setItemChecked(activeListItemPos, false);
				optionsList.setItemChecked(id, true);
				
				//optionsList.getChildAt(activeListItemPos).setActivated(false);
				item.setActivated(true);
				activeListItemPos = id;
			}
			break;

		default:
			break;
		}
	}

	private static final int MOVE_BACK = 0X00, MOVE_FORWARD = 0X01;
	
	private void setFlipperMovement(int direction)
	{
		switch (direction)
		{
		case MOVE_BACK:
			
			flipper.setInAnimation(slide_top_in);
			flipper.setOutAnimation(slide_bottom_out);
			break;

		case MOVE_FORWARD:
			
			flipper.setInAnimation(slide_bottom_in);
			flipper.setOutAnimation(slide_top_out);
			break;

		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.project_edit_nameanddesc_submit:
			
			projectName = namedescProjectName.getText().toString();
			projectDescription = namedescProjectDesc.getText().toString();
			
			ContentValues values = new ContentValues();
			values.put(ScreenProvider.KEY_PROJECTS_DESCRIPTION, projectDescription);
			values.put(ScreenProvider.KEY_PROJECTS_NAME, projectName);
			
			Uri projectUpdate = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_PROJECTS, projectId);
			resolver.update(projectUpdate, values, null, null);
			
			setFlipperMovement(MOVE_BACK);
			flipper.setDisplayedChild(0);
			break;

		default:
			break;
		}
		
	}
}
