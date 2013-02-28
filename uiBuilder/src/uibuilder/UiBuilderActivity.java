package uibuilder;

import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity implements onUiElementSelectedListener
{

	private ItemboxFragment itembox;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);
		
		itembox = (ItemboxFragment) getFragmentManager().findFragmentById(R.id.fragment_itembox);
		ItemboxFragment.setOnUiElementSelectedListener(this);
	}
	
	/**
	 * implemented Interface onUiElementSelected
	 */
	@Override
	public void typeChanged(int id)
	{
		// TODO Auto-generated method stub
		DesignFragment design = (DesignFragment) getFragmentManager().findFragmentById(R.id.fragment_design);
		design.setSelection(id);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		//measure();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	protected Builder createItemChooseDialog()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void test()
	{
	}
	
/*	
	

	*//**
	 * Aktuelle Displaygröße ermitteln
	 *//*
	private void measure() 
	{
		
		//root.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		int displayHeight = getResources().getDisplayMetrics().heightPixels;
		int displayWidth = getResources().getDisplayMetrics().widthPixels;
		
		int rootHeight = root.getMeasuredHeight();
		int rootWidth = rootHeight/16*9;
		
		//RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) root.getLayoutParams();
				//params.width = rootWidth;
		Log.d("root on attac w", String.valueOf(root.getWidth()));
		
		//RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) root.getLayoutParams();
		//params.width = rootWidth;
		//params.height = rootHeight;
		
		//root.setLayoutParams(params);
	}*/
}
