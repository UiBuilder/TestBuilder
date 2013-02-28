package uibuilder;

import helpers.Log;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
		
		RelativeLayout designarea = (RelativeLayout) findViewById(R.id.design_area);
		designarea.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Log.d("designw", String.valueOf(designarea.getMeasuredWidth()));
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
}
