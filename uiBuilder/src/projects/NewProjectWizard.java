package projects;

import java.util.ArrayList;
import java.util.List;

import cloudmodule.UserConstants;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import data.DateGenerator;
import data.NewScreenHolder;
import data.ProjectHolder;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class NewProjectWizard extends Activity implements OnClickListener, OnCheckedChangeListener, OnQueryTextListener
{
	private ViewFlipper flipper;
	private int flipperState;
	
	private ContentResolver resolver;
	private DateGenerator date;
	
	private ProjectHolder projectHolder;
	private ArrayList<NewScreenHolder> screenHolder;
	
	private Animation
			slide_in_left,
			slide_in_right,
			slide_out_left,
			slide_out_right;
	
	private LinearLayout resultSet;
	
	private TextView 
			screenName,
			screenDesc,
			projectName,
			projectdesc;
	
	private boolean screensRequested = false;
	private int projectId = 0;
	
	//find users
	private SearchView searchfield;
	private LinearLayout searchResults;
	private ArrayList<ParseUser> collabList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_project_wizard_root);
		

        setupHelpers();
		
		Intent startIntent = getIntent();
		
		projectId = startIntent.getIntExtra(ProjectDisplay.START_WIZARD_FOR_NEW_SCREENS, 0);
		if (projectId != 0)
		{
			screensRequested = true;
		}
			
		
		setupActionBar();
		
		setupAnimations();
        
        setupUi();
        setupSearchUserPage();
	}
	
	
	
	private void setupSearchUserPage()
	{
		searchfield = (SearchView) findViewById(R.id.project_wizard_flipper_collab_section_searchfield);
		searchfield.setOnQueryTextListener(this);
		
		searchResults = (LinearLayout) findViewById(R.id.project_wizard_flipper_collab_section_results);
		collabList = new ArrayList<ParseUser>();
	}



	@Override
	public void onBackPressed()
	{
		if (screensRequested || flipper.getDisplayedChild() == 0)
		{
			super.onBackPressed();
			overridePendingTransition(R.anim.activity_transition_from_bottom_in, R.anim.activity_transition_to_top_out);
		}
		else
		{
			flipper.setInAnimation(slide_out_right);
			flipper.setOutAnimation(slide_in_left);
			flipper.showPrevious();
		}
	}



	/**
	 * 
	 */
	private void setupHelpers()
	{
		screenHolder = new ArrayList<NewScreenHolder>();

		projectHolder = new ProjectHolder();
        resolver = getContentResolver();
		date = new DateGenerator();
	}
	/**
	 * 
	 */
	private void setupAnimations()
	{
		slide_in_left = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_left_in);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_right_in);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_left_out);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_right_out);
	}
	/**
	 * 
	 */
	private void setupUi()
	{
		flipper = (ViewFlipper) findViewById(R.id.project_wizard_flipper);
		
		Switch toggleCollab = (Switch) findViewById(R.id.project_wizard_flipper_collab_toggle);
		toggleCollab.setOnCheckedChangeListener(this);
		
		Button goToScreens = (Button) findViewById(R.id.project_wizard_flipper_step1_ok);
        goToScreens.setOnClickListener(this);
        
        Button goToCollab = (Button) findViewById(R.id.project_wizard_flipper_step2_ok);
        goToCollab.setOnClickListener(this);
        
        Button step2back = (Button) findViewById(R.id.project_wizard_flipper_step2_back);
        step2back.setOnClickListener(this);
        
        Button collabBack = (Button) findViewById(R.id.project_wizard_flipper_collab_back);
        collabBack.setOnClickListener(this);
        
        Button step2AddScreen = (Button) findViewById(R.id.project_wizard_flipper_step2_addScreen);
        step2AddScreen.setOnClickListener(this);
        
        Button finish = (Button) findViewById(R.id.project_wizard_flipper_collab_finish);
        finish.setOnClickListener(this);
        
        screenDesc = (TextView) findViewById(R.id.project_wizard_flipper_step2_screendescription);
        screenName = (TextView) findViewById(R.id.project_wizard_flipper_step2_screenname);
        
        projectName = (TextView) findViewById(R.id.project_wizard_flipper_step1_projectname);
        projectdesc = (TextView) findViewById(R.id.project_wizard_flipper_step1_description); 
        
        resultSet = (LinearLayout) findViewById(R.id.project_wizard_flipper_step2_results);
        
        if (screensRequested)
		{
			flipperState = 1;
			step2back.setVisibility(View.INVISIBLE);
			//loadExistingScreens();
		}
		else
		{
			flipperState = 0;
		}
        flipper.setDisplayedChild(flipperState);
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
		case R.id.project_wizard_flipper_step1_ok:
			
			putValuesInProjectholder();
			insertNewProject();
			flipper.setInAnimation(slide_in_right);
			flipper.setOutAnimation(slide_out_left);
			flipper.showNext();
			break;
	
		case R.id.project_wizard_flipper_step2_back:
		case R.id.project_wizard_flipper_collab_back:
			
			flipper.setInAnimation(slide_out_right);
			flipper.setOutAnimation(slide_in_left);
			flipper.showPrevious();
			break;
			
		case R.id.project_wizard_flipper_step2_addScreen:
			
			addScreenToHolder();
			break;
			
		case R.id.project_wizard_flipper_step2_ok:
			
			flipper.setInAnimation(slide_in_right);
			flipper.setOutAnimation(slide_out_left);
			flipper.showNext();
			break;
			
		case R.id.project_wizard_flipper_collab_finish:
			
			insertNewScreens();
			returnToManager();
			break;
			
		default:
			break;
		}
	}
	
	
	private void insertNewScreens()
	{
		ContentValues[] screenValues = new ContentValues[screenHolder.size()];
		
		int i = 0;
		for (NewScreenHolder holder: screenHolder)
		{
			screenValues[i] = holder.getBundle();
			i++;
		}
		
		resolver.bulkInsert(ScreenProvider.CONTENT_URI_SECTIONS, screenValues);
		
	}
	
	private void putValuesInProjectholder()
	{
		projectHolder.projectDate = date.generateDate();
		projectHolder.projectName = String.valueOf(projectName.getText());
		projectHolder.projectDescription = String.valueOf(projectdesc.getText());
		
	}
	
	
	
	private void insertNewProject()
	{
		ContentValues values = projectHolder.getValues();
		
		if (projectHolder.projectId == 0)
		{
			Uri inserted = resolver.insert(ScreenProvider.CONTENT_URI_PROJECTS, values);
			String path = inserted.getPathSegments().get(1);
			projectHolder.projectId = Integer.valueOf(path);
			Log.d("insert project path full", path);
			Log.d("id in holder", String.valueOf(projectHolder.projectId));
		}
		else
		{
			String where = String.valueOf(projectHolder.projectId);
			resolver.update(ScreenProvider.CONTENT_URI_PROJECTS, values, where, null);
		}
		
	}
	private void addScreenToHolder()
	{
		NewScreenHolder holder = new NewScreenHolder();
		
		if (screensRequested)
		{
			/*int nameIdx = existing.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_NAME);
			int descIdx = existing.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_DESCRIPTION);
			
			holder.sectionName = existing.getString(nameIdx);*/
			holder.sectionId = projectId;
			//holder.sectionDescription = existing.getString(descIdx);
		}
		else
		{
			
			holder.sectionId = projectHolder.projectId;
		}
		holder.sectionName = String.valueOf(screenName.getText());
		holder.sectionDescription = String.valueOf(screenDesc.getText());
		
		screenHolder.add(holder);
		
		notifyUser();
		resetFields();
		displayResults();
	}
	private void displayResults()
	{
		resultSet.removeAllViews();
		
		for (NewScreenHolder holder: screenHolder)
		{
			LinearLayout resultItem = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_project_wizard_resultset_item, null);
			TextView resultName = (TextView) resultItem.findViewById(R.id.project_wizard_flipper_step2_results_title);
			resultName.setText(holder.sectionName);
			
			resultSet.addView(resultItem);
		}
	}
	private void notifyUser()
	{
		// TODO Auto-generated method stub
		Toast.makeText(this, "Screen added!", Toast.LENGTH_SHORT).show();
	}

	private void resetFields()
	{
		// TODO Auto-generated method stub
		screenDesc.setText("");
		screenName.setText("");
		screenName.requestFocus();
	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if (isChecked)
		{
			ParseUser currentUser = ParseUser.getCurrentUser();
			
			if (currentUser != null)
			{
				findViewById(R.id.project_wizard_flipper_collab_section).setVisibility(View.VISIBLE);
			}
		}
		
	}



	@Override
	public boolean onQueryTextChange(String arg0)
	{
		// TODO Auto-generated method stub
		searchResults.removeAllViews();
		return false;
	}



	@Override
	public boolean onQueryTextSubmit(String arg0)
	{
		searchfield.clearFocus();
		
		ParseQuery query = ParseUser.getQuery();

		
		query.whereEqualTo("username", arg0);
		query.findInBackground(new FindCallback() 
		{

		@Override
		public void done(List<ParseObject> arg0, ParseException e)
		{
			searchResults.removeAllViews();
			View item;
			
			if (e == null && arg0.size() != 0) 
			{
				ParseUser user = (ParseUser)arg0.get(0);
				String name = user.getUsername();
				String mail = user.getEmail();
				String id = user.getObjectId();
				String displayName = user.getString(UserConstants.USER_DISPLAY_NAME);
				
				Log.d("query for parse user", displayName);
				Log.d("query for parse usermail", mail);
				Log.d("query for parse id", id);
	
				
				if (ParseUser.getCurrentUser().hasSameId(user))
				{
					item = new TextView(getApplicationContext());
					((TextView) item).setText("This is you, dumbass");
					
				}
				else
				{
					item = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_project_wizard_collaboration_resultset_item, null);
					TextView userName = (TextView) item.findViewById(R.id.project_wizard_flipper_collab_section_results_item_collabName);
					userName.setText(name);
					
					Button addUser = (Button) item.findViewById(R.id.project_wizard_flipper_collab_section_results_item_addToCollabs);
					addUser.setTag(user);
					addUser.setOnClickListener(new OnClickListener()
					{
						
						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							ParseUser user = (ParseUser) v.getTag();
							
							collabList.add(user);
							Log.d("user added", user.getEmail());
						}
					});
				}
				
			}
			else
			{
				item = new TextView(getApplicationContext());
				((TextView) item).setText("Sorry, no results.");
			}
			searchResults.addView(item);
		}
		});
		
		return true;
	}
}
