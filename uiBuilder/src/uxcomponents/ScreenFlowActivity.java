package uxcomponents;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class ScreenFlowActivity extends Activity
{

	private ScreenFlowDesignFragment design;
	private ScreenFlowScreensFragment screens;
	private FragmentManager fragmentManager;
	
	private FrameLayout sections;
	private RelativeLayout designArea;
	
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_project_screenflow_root);
		fragmentManager = getFragmentManager();
		
		initFragments();
	}

	private void initFragments()
	{
		design = new ScreenFlowDesignFragment();
		screens = new ScreenFlowScreensFragment();
		
		sections = (FrameLayout) findViewById(R.id.screenflow_section_container);
		designArea = (RelativeLayout) findViewById(R.id.screenflow_design_area);
		
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
