package uibuilder;

import java.util.ArrayList;

import data.NewScreenHolder;
import de.ur.rk.uibuilder.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewProjectWizard extends Activity implements OnClickListener
{
	private ViewFlipper flipper;
	private int flipperState;
	
	private ArrayList<NewScreenHolder> screenHolder;
	
	private Animation
			slide_in_left,
			slide_in_right,
			slide_out_left,
			slide_out_right;
	
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
		
		flipper = (ViewFlipper) findViewById(R.id.project_wizard_flipper);
		flipperState = 0;
		
		slide_in_left = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_left_in);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.activity_transition_from_right_in);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_left_out);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.activity_transition_to_right_out);
        
        Button step1Next = (Button) findViewById(R.id.project_wizard_flipper_step1_ok);
        step1Next.setOnClickListener(this);
        
        Button step2Next = (Button) findViewById(R.id.project_wizard_flipper_step2_ok);
        step2Next.setOnClickListener(this);
        
        Button step2back = (Button) findViewById(R.id.project_wizard_flipper_step2_back);
        step2back.setOnClickListener(this);
        
        Button step2AddScreen = (Button) findViewById(R.id.project_wizard_flipper_step2_addScreen);
        step2AddScreen.setOnClickListener(this);
        
        screenHolder = new ArrayList<NewScreenHolder>();
        
        screenDesc = (TextView) findViewById(R.id.project_wizard_flipper_step2_screendescription);
        screenName = (TextView) findViewById(R.id.project_wizard_flipper_step2_screenname);
		
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
		case R.id.project_wizard_flipper_step2_ok:
			
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
		default:
			break;
		}
	}
	
	
	private void addScreenToHolder()
	{
		NewScreenHolder holder = new NewScreenHolder();
		holder.sectionDescription = String.valueOf(screenDesc.getText());
		holder.sectionId = 0;
		holder.sectionName = String.valueOf(screenName.getText());
		
		screenHolder.add(holder);
		
		notifyUser();
		resetFields();
		
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
