package editmodules;

import java.util.ArrayList;

import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class ModuleZOrder extends Module
{
	private View box;
	
	private View requesting;
	
	public ModuleZOrder(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = super.inflater.inflate(R.layout.editmode_entry_z_order, null);
	}

	@Override
	public View getInstance(View inProgress)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

	}
	
	private class ZorderModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			ViewGroup parent = (ViewGroup) currentView.getParent();

			switch (v.getId())
			{
			case R.id.editmode_z_order_back:
				parent.removeView(currentView);
				ArrayList<View> allItems = new ArrayList<View>();

				allItems.add(currentView);

				int number = parent.getChildCount();
				for (int i = 0; i < number; i++)
				{
					allItems.add(parent.getChildAt(i));
				}
				parent.removeAllViews();

				for (View child : allItems)
				{
					parent.addView(child);
				}
				parent.invalidate();

				break;

			case R.id.editmode_z_order_front:
				currentView.bringToFront();
				parent.invalidate();
				break;

			default:
				break;
			}
		}
	}

}
