package uibuilder;

import helpers.ImageTools;
import uibuilder.DeleteFragment.onDeleteRequestListener;
import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity implements
		onUiElementSelectedListener, onObjectSelectedListener, onDeleteRequestListener
{

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	public static final int ITEMBOX = 0;
	public static final int EDITBOX = 1;
	public static final int DELETEBOX = 2;
	
	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private FragmentManager fManager;
	private ViewGroup container;
	private ImageTools exporter;
	private DeleteFragment deletebox;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);

		LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		container = (ViewGroup) inf.inflate(R.layout.layout_fragment_container, null);
		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());

		ActionBar bar = getActionBar();
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.NAVIGATION_MODE_STANDARD);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
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
		deletebox = new DeleteFragment();

		ItemboxFragment.setOnUiElementSelectedListener(this);
		DesignFragment.setOnObjectSelectedListener(this);
		DeleteFragment.onDeleteRequestListener(this);
		
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
		init.add(R.id.fragment_sidebar, deletebox);
		init.add(R.id.fragment_design, designbox);
		

		init.hide(editbox);
		init.hide(itembox);
		init.hide(deletebox);
		
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
		outSwapper.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		
		switch (sidebarType)
		{
		case ITEMBOX:
			
			outSwapper.hide(editbox);
			outSwapper.show(itembox);
			break;

		case EDITBOX:

			Log.d("switched sideBarType", "result Editbox, replacing");
			
			outSwapper.show(editbox);
			outSwapper.hide(itembox);

			break;
			
		case DELETEBOX:
			
			outSwapper.hide(editbox);
			outSwapper.show(deletebox);
			break;
		}
		outSwapper.commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.design, menu);
	    return true;
	}
	
	
	/**
	 * switch on the selected item
	 * action export: call imagetools to process the exporting request
	 * @author funklos
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_export_jpeg:
			
			exporter.requestBitmap(designbox.getView(), getContentResolver());
		
			Toast.makeText(getApplicationContext(), "Layout has been saved to the gallery", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return true;
	}

	/**Interface onUiElementSelected method
	 * @author funklos
	 * implemented to notify the designbox of the chosen type
	 * of interface element.
	 */
	@Override
	public void typeChanged(int id)
	{
		designbox.setSelection(id);
	}
	
	/**Interface onObjectSelected method
	 * @author funklos
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
	 * Interface callback method to switch between itembox, when no item is selected,
	 * and editbox when an item is selected
	 */
	@Override
	public void objectSelected(boolean selected)
	{

		if (!selected)
		{
			displaySidebar(ITEMBOX);
			Log.d("item was deleted", "showing itembox");
		} 
		else
		{
			displaySidebar(EDITBOX);
			editbox.adaptLayoutToContext(lastTouch);
		}
	}

	@Override
	public void objectDragging()
	{
		displaySidebar(DELETEBOX);
	}
	
	/**
	 * deletebox interface callback
	 */
	@Override
	public void requestDelete()
	{
		// TODO Auto-generated method stub
		designbox.performDelete();
	}


}
