package uibuilder;

import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity implements onUiElementSelectedListener
{

	private static final int ITEMBOX = 0;
	private static final int EDITBOX = 1;
	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private FragmentManager fManager;
	private FragmentTransaction fTransaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);
		fManager = getFragmentManager();
		fTransaction = fManager.beginTransaction();
		
		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();
		displaySidebar(ITEMBOX);
		//fTransaction.add(R.id.fragment_sidebar, itembox);
		fTransaction.add(R.id.fragment_design, designbox);
		
		ItemboxFragment.setOnUiElementSelectedListener(this);

	}
	public void displaySidebar(int sidebarType){
		
		switch (sidebarType){
		case ITEMBOX:
			fTransaction.replace(R.id.fragment_sidebar, itembox);
			
			break;
		case EDITBOX:
			fTransaction.replace(R.id.fragment_sidebar, editbox);
			break;
		}
		
		findViewById(R.id.fragment_sidebar).setVisibility(View.VISIBLE);
		fTransaction.commit();
	}
	
	/**
	 * implemented Interface onUiElementSelected
	 */
	@Override
	public void typeChanged(int id)
	{
		
		designbox.setSelection(id);
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

}
