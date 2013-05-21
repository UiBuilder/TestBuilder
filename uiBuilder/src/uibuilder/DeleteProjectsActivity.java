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

public class DeleteProjectsActivity extends Activity implements OnClickListener
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
		
		setContentView(R.layout.activity_delete_project_root);
		
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
		Button cancel = (Button) findViewById(R.id.project_delete_cancel);
		Button delete = (Button) findViewById(R.id.project_delete_confirm);
		
		cancel.setOnClickListener(this);
		delete.setOnClickListener(this);
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
		case R.id.project_delete_cancel:

			returnToManager();
			break;
	
		case R.id.project_delete_confirm:
			
			performSelectedDeletion();
			break;
			
		}
	}
	private void performSelectedDeletion()
	{
		// TODO Auto-generated method stub
		
	}

}
