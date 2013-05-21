package uibuilder;

import java.util.ArrayList;

import data.DateGenerator;
import data.NewScreenHolder;
import data.ProjectHolder;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewProjectWizard extends Activity implements OnClickListener
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_project_wizard_root);
		
		Intent startIntent = getIntent();
		
		setupActionBar();
		
		setupAnimations();
        
        setupUi();
        
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
		flipperState = 0;
		
		Button step1Next = (Button) findViewById(R.id.project_wizard_flipper_step1_ok);
        step1Next.setOnClickListener(this);
        
        Button finish = (Button) findViewById(R.id.project_wizard_flipper_step2_ok);
        finish.setOnClickListener(this);
        
        Button step2back = (Button) findViewById(R.id.project_wizard_flipper_step2_back);
        step2back.setOnClickListener(this);
        
        Button step2AddScreen = (Button) findViewById(R.id.project_wizard_flipper_step2_addScreen);
        step2AddScreen.setOnClickListener(this);
        
        screenDesc = (TextView) findViewById(R.id.project_wizard_flipper_step2_screendescription);
        screenName = (TextView) findViewById(R.id.project_wizard_flipper_step2_screenname);
        
        projectName = (TextView) findViewById(R.id.project_wizard_flipper_step1_projectname);
        projectdesc = (TextView) findViewById(R.id.project_wizard_flipper_step1_description); 
        
        resultSet = (LinearLayout) findViewById(R.id.project_wizard_flipper_step2_results);
	}
/*	
	private void returnToManager()
	{
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra(ManagerActivity.RESULT_SCREEN_ID, screenId);
		returnIntent.putExtra(ManagerActivity.RESULT_IMAGE_PATH,imageUri.toString());
		setResult(RESULT_OK, returnIntent); 
		
		finish();
		overridePendingTransition(R.anim.activity_transition_from_left_in, R.anim.activity_transition_to_right_out);
	}
*/	
	
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
			
			flipper.setInAnimation(slide_out_right);
			flipper.setOutAnimation(slide_in_left);
			flipper.showPrevious();
			break;
			
		case R.id.project_wizard_flipper_step2_addScreen:
			
			addScreenToHolder();
			break;
			
		case R.id.project_wizard_flipper_step2_ok:
			
			insertNewScreens();
			finish();
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
		holder.sectionDescription = String.valueOf(screenDesc.getText());
		holder.sectionId = projectHolder.projectId;
		holder.sectionName = String.valueOf(screenName.getText());
		
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
}
