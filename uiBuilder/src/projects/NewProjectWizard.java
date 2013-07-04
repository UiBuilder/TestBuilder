package projects;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cloudmodule.CloudConnection;
import cloudmodule.CloudConnection.OnFromCloudLoadedListener;
import cloudmodule.CloudConstants;

import com.parse.ParseObject;
import com.parse.ParseUser;

import data.NewScreenHolder;
import data.ProjectColorAdapter;
import de.ur.rk.uibuilder.R;

public class NewProjectWizard extends Activity implements OnClickListener, OnCheckedChangeListener, OnQueryTextListener, OnFromCloudLoadedListener
{
	private ViewFlipper flipper;
	private int flipperState;
	
	private Project project;
	private boolean cloudUser;
	ParseUser currentUser;
	
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
	private CloudConnection cloud;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_project_wizard_root);
		
		Intent startIntent = getIntent();
		projectId = startIntent.getIntExtra(ProjectDisplay.START_WIZARD_FOR_NEW_SCREENS, 0);
		
		checkForCloudUser();
        setupHelpers();	
		setupActionBar();	
		setupAnimations();    
        setupUi();
        setupSearchUserPage();
	}
	
	
	
	private void checkForCloudUser()
	{
		// TODO Auto-generated method stub
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		if (currentUser != null)
		{
			cloudUser = true;
		}
		else
		{
			
		}
	}



	private void setupSearchUserPage()
	{
		searchfield = (SearchView) findViewById(R.id.project_wizard_flipper_collab_section_searchfield);
		searchfield.setOnQueryTextListener(this);
		
		searchResults = (LinearLayout) findViewById(R.id.project_wizard_flipper_collab_section_results);
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
			flipToLast();
		}
	}



	/**
	 * 
	 */
	private void setupHelpers()
	{	
		if (cloudUser)
		{
			CloudConnection.setOnFromCloudLoadedListener(this);
			cloud = CloudConnection.establish(getApplicationContext(), getContentResolver());
		}
		
		project = new Project(getApplicationContext());
		project.setColor(getApplicationContext().getResources().getColor(R.color.superlight_grey));
		
		if (projectId != 0)
		{
			screensRequested = true;
			project.setNotNew();
			project.setProjectId(projectId);
		}
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
        if(ParseUser.getCurrentUser() == null)
        {
        	goToCollab.setText("Finish");
        }
        
        Button step2back = (Button) findViewById(R.id.project_wizard_flipper_step2_back);
        step2back.setOnClickListener(this);
        
        Button collabBack = (Button) findViewById(R.id.project_wizard_flipper_collab_back);
        collabBack.setOnClickListener(this);
        
        Button step2AddScreen = (Button) findViewById(R.id.project_wizard_flipper_step2_addScreen);
        step2AddScreen.setOnClickListener(this);
        
        Button finish = (Button) findViewById(R.id.project_wizard_flipper_collab_finish);
        finish.setOnClickListener(this);
        
        GridView colorGrid = (GridView) findViewById(R.id.color_grid);
        colorGrid.setAdapter(new ProjectColorAdapter(getApplicationContext(), 0, 0, null));
        colorGrid.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				int colorResource = (Integer) arg1.getTag();
				project.setColor(colorResource);
				flipper.setBackgroundColor(colorResource);
			}
		});
        
        
        screenDesc = (TextView) findViewById(R.id.project_wizard_flipper_step2_screendescription);
        screenName = (TextView) findViewById(R.id.project_wizard_flipper_step2_screenname);
        
        projectName = (TextView) findViewById(R.id.project_wizard_flipper_step1_projectname);
        projectdesc = (TextView) findViewById(R.id.project_wizard_flipper_step1_description); 
        
        resultSet = (LinearLayout) findViewById(R.id.project_wizard_flipper_step2_results);
        
        if (screensRequested)
		{
			flipperState = 1;
			step2back.setVisibility(View.INVISIBLE);
			goToCollab.setText("Done");
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
			
			project.prepare(String.valueOf(projectName.getText()), String.valueOf(projectdesc.getText()));
			flipToNext();
			break;
	
		case R.id.project_wizard_flipper_step2_back:
		case R.id.project_wizard_flipper_collab_back:
			
			flipToLast();
			break;
			
		case R.id.project_wizard_flipper_step2_addScreen:
			
			project.addScreen(String.valueOf(screenName.getText()), String.valueOf(screenDesc.getText()));
			displayResults();
			resetFields();
			break;
			
		case R.id.project_wizard_flipper_step2_ok:
			
			Log.d("new project for cloud user", String.valueOf(cloudUser));
			if (cloudUser && !screensRequested)
			{
				flipToNext();
			}
			else
			{
				project.create();
				returnToManager();
			}
			break;
			
		case R.id.project_wizard_flipper_collab_finish:
			
			project.setShared(CloudConstants.PROJECT_SHARED_TRUE);
			project.create();
			returnToManager();
			break;
			
		default:
			break;
		}
	}


	/**
	 * 
	 */
	private void flipToLast()
	{
		flipper.setInAnimation(slide_out_right);
		flipper.setOutAnimation(slide_in_left);
		flipper.showPrevious();
	}

	/**
	 * 
	 */
	private void flipToNext()
	{
		flipper.setInAnimation(slide_in_right);
		flipper.setOutAnimation(slide_out_left);
		flipper.showNext();
	}

	private void displayResults()
	{
		resultSet.removeAllViews();
		ArrayList<NewScreenHolder> screens = project.getScreens();
		
		for (NewScreenHolder holder: screens)
		{
			LinearLayout resultItem = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_project_wizard_resultset_item, null);
			TextView resultName = (TextView) resultItem.findViewById(R.id.project_wizard_flipper_step2_results_title);
			resultName.setText(holder.sectionName);
			
			resultSet.addView(resultItem);
		}
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
			if (cloudUser)
			{
				findViewById(R.id.project_wizard_flipper_collab_section).setVisibility(View.VISIBLE);
				project.setShared(CloudConstants.PROJECT_SHARED_TRUE);
			}

		}
		else
		{
			findViewById(R.id.project_wizard_flipper_collab_section).setVisibility(View.INVISIBLE);
			project.setShared(CloudConstants.PROJECT_SHARED_FALSE);
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
		
		cloud.queryUser(arg0);
		
		Log.d("querying in background", "started");
		return true;
	}



	@Override
	public void usersLoaded(ArrayList<ParseUser> users)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public void userFound(ParseUser user)
	{
		// TODO Auto-generated method stub
		View item = searchResults.findViewById(R.id.activity_project_wizard_collaboration_resultset_item);
		searchResults.setVisibility(View.VISIBLE);
		
		Log.d("query user", "done");
		
		if (user != null) 
		{
			String name = user.getUsername();
			String mail = user.getEmail();
			String id = user.getObjectId();
			String displayName = user.getString(CloudConstants.USER_DISPLAY_NAME);
			
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
				
				int listpos = project.checkList(user);
				Log.d("listpos", String.valueOf(listpos));
				
				if (listpos == -1)
				{
					addUser.setText("Add");
				}
				else
				{
					addUser.setText("Remove");
				}
				
				addUser.setOnClickListener(new OnClickListener()
				{
					
					
					@Override
					public void onClick(View v)
					{
						ParseUser foundUser = (ParseUser) v.getTag();
						
						
						if (((Button) v).getText().equals("Remove"))
						{
							project.removeUser(foundUser);
							
							searchResults.removeAllViews();
						}
						else
						{
							// TODO Auto-generated method stub
							project.addUser(foundUser);
							Log.d("user added", foundUser.getEmail());
							((Button) v).setText("Remove");
						}
					}

					/*private void removeFromList(ParseUser user)
					{
						int idx = checkList(user);
						collabList.remove(idx);
					}*/
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



	@Override
	public void projectLoaded(ParseObject project)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public void newCloudProjectFound(String cloudProjectId)
	{
		// TODO Auto-generated method stub
		
	}
}
