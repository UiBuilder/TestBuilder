package uibuilder;

import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);

		LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		container = (ViewGroup) inf.inflate(R.layout.layout_fragment_container, null);
		fManager = getFragmentManager();

		setupUi();

	}

	/**
	 * 
	 */
	private void setupUi()
	{
		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();

		ItemboxFragment.setOnUiElementSelectedListener(this);
		DesignFragment.setOnObjectSelectedListener(this);

		FragmentTransaction init = fManager.beginTransaction();
		init.add(R.id.fragment_sidebar, editbox);
		init.add(R.id.fragment_sidebar, itembox);
		init.add(R.id.fragment_design, designbox);
		
		init.hide(editbox);
		init.commit();
	}

	public void displaySidebar(int sidebarType)
	{
		Log.d("DisplaySidebar", "is Called");
		FragmentTransaction swapper = fManager.beginTransaction();

		switch (sidebarType)
		{
		case ITEMBOX:

			Log.d("switched sideBarType", "result Itembox, replacing");

			swapper.hide(editbox);
			swapper.show(itembox);
			break;

		case EDITBOX:
			Log.d("switched sideBarType", "result Editbox, replacing");

			swapper.hide(itembox);
			swapper.show(editbox);
			//swapper.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			break;
		}
		swapper.addToBackStack(null);
		swapper.commit();
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
	public void objectChanged(View view)
	{
		lastTouch = view;
	}

	private View lastTouch;

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
