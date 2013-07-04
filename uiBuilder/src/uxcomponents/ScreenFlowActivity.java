package uxcomponents;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ScreenFlowActivity extends Activity
{

	private ScreenFlowDesignFragment design;
	private ScreenFlowScreensFragment screens;
	private FragmentManager fragmentManager;
	
	private FrameLayout sections;
	private FrameLayout designArea;
	
	private int projectId;
	
	private Bundle startingBundle;
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		ArrayList<View> screens = design.getFlow();
		
		ContentResolver resolver = getContentResolver();
		
		for (View screen: screens)
		{
			
				TextView label = (TextView) screen.findViewById(R.id.screenname);
				String labelText = label.getText().toString();
				
				RelativeLayout.LayoutParams params = (LayoutParams) screen.getLayoutParams();
				
				int x = params.leftMargin;
				int y = params.topMargin;
				Log.d("inserting x", String.valueOf(x));
				
				ContentValues values = new ContentValues();
				values.put(ScreenProvider.KEY_FLOW_LABEL, labelText);
				values.put(ScreenProvider.KEY_FLOW_PROJECT_ID, projectId);
				values.put(ScreenProvider.KEY_FLOW_X, x);
				values.put(ScreenProvider.KEY_FLOW_Y, y);
			if (screen.getTag() == null)
			{	
				resolver.insert(ScreenProvider.CONTENT_URI_FLOW, values);
				Log.d("screen", "inserted");
			}
			else
			{
				Log.d("screen", "updated");
				Uri screenUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_FLOW, (Integer)screen.getTag());
				resolver.update(screenUri, values, null, null);
			}
		}
		
		finish();
		overridePendingTransition(R.anim.activity_transition_from_left_in, R.anim.activity_transition_to_right_out);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_project_screenflow_root);
		fragmentManager = getFragmentManager();
		
		startingBundle = getIntent().getExtras();
		projectId = startingBundle.getInt(ScreenProvider.KEY_ID);
		initFragments();
	}

	private void initFragments()
	{
		design = new ScreenFlowDesignFragment();
		design.setArguments(startingBundle);
		screens = new ScreenFlowScreensFragment();
		screens.setArguments(startingBundle);
		
		sections = (FrameLayout) findViewById(R.id.screenflow_section_container);
		designArea = (FrameLayout) findViewById(R.id.screenflow_design_area);
		
		FragmentTransaction init = fragmentManager.beginTransaction();
		init.add(sections.getId(), screens);
		init.add(designArea.getId(), design);
		
		init.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		init.commit();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
