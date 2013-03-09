package uibuilder;

import helpers.ImageTools;
import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity implements
		onUiElementSelectedListener, onObjectSelectedListener
{

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	public static final int ITEMBOX = 0;
	public static final int EDITBOX = 1;
	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private FragmentManager fManager;
	private ViewGroup container;
	private ImageTools exporter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);

		LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		container = (ViewGroup) inf.inflate(R.layout.layout_fragment_container, null);
		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());

		setupUi();

	}

	/**
	 * Create UI-Fragment instances and set the activity as listener for changes
	 */
	private void setupUi()
	{
		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();

		ItemboxFragment.setOnUiElementSelectedListener(this);
		DesignFragment.setOnObjectSelectedListener(this);
		
		performInitTransaction();
	}

	/**
	 * Initial fragmenttransaction to display the fragments.
	 * Editbox is added but hidden to guarantee access
	 */
	private void performInitTransaction()
	{
		FragmentTransaction init = fManager.beginTransaction();
		init.add(R.id.fragment_sidebar, editbox);
		init.add(R.id.fragment_sidebar, itembox);
		init.add(R.id.fragment_design, designbox);
		//init.add(R.id.fragment_sidebar, editbox);
		init.hide(editbox);
		init.hide(itembox);
		init.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		init.commit();
		
		objectSelected(false);
	}

	/**
	 * Adapt the sidebar to create or edit mode.
	 * Is called from the interface implementation.
	 * @param sidebarType specifies which of the sidebars to display
	 */
	
	public void displaySidebar(int sidebarType)
	{
		Log.d("DisplaySidebar", "is Called");
		FragmentTransaction outSwapper = fManager.beginTransaction();
		//FragmentTransaction inSwapper = fManager.beginTransaction();
		outSwapper.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		//inSwapper.setCustomAnimations(R.animator.to_right_in, R.animator.to_right_out);
		switch (sidebarType)
		{
		case ITEMBOX:
			
			outSwapper.hide(editbox);
			outSwapper.show(itembox);
			break;

		case EDITBOX:
			//swapper.show(editbox);
			//swapper.hide(itembox);
			Log.d("switched sideBarType", "result Editbox, replacing");
			
			//wapper.replace(R.id.fragment_sidebar, editbox);
			outSwapper.show(editbox);
			outSwapper.hide(itembox);
			//swapper.show(editbox);
			
			//swapper.hide(itembox);
			
			//swapper.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			break;
		}
		//outSwapper.addToBackStack(null);
		//inSwapper.commit();
		outSwapper.commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.design, menu);
	    return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_export_jpeg:
			
			exporter.requestBitmap(designbox.getView(), getContentResolver());
		
			
			break;

		default:
			break;
		}
		return true;
	}

	/**Interface onUiElementSelected method
	 * 
	 * implemented to notify the designbox of the chosen type
	 * of interface element.
	 */
	@Override
	public void typeChanged(int id)
	{
		designbox.setSelection(id);
	}
	
	/**Interface onObjectSelected method
	 * 
	 * sets a reference to the object in progress
	 * @param view the selected view
	 */
	@Override
	public void objectChanged(View view)
	{
		lastTouch = view;
	}

	private View lastTouch;

	
	/**
	 * 
	 */
	@Override
	public void objectSelected(boolean selected)
	{

		if (!selected)
		{
			displaySidebar(ITEMBOX);
		} 
		else
		{
			displaySidebar(EDITBOX);
			editbox.adaptLayoutToContext(lastTouch);
		}
	}


}
