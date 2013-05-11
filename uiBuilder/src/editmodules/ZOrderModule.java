package editmodules;

import java.util.ArrayList;

import uibuilder.EditmodeFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the z-order alignment.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author funklos
 *
 */
public class ZOrderModule extends Module
{
	private LinearLayout box;

	private Button toFront;
	private Button toBack;
	
	public ZOrderModule(EditmodeFragment context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_z_order, null);
		
		toFront = (Button) box.findViewById(R.id.editmode_z_order_front);;
		toBack = (Button) box.findViewById(R.id.editmode_z_order_back);
	}

	
	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		adaptToContext();
		
		return box;
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
			ViewGroup parent = (ViewGroup) container.getParent();

			switch (v.getId())
			{
			case R.id.editmode_z_order_back:
				
				pushToBack(parent);
				break;

			case R.id.editmode_z_order_front:
				
				bringToFront();
				break;

			default:
				break;
			}
			
			parent.invalidate();
		}

		/**
		 * Sends the requesting view to background.
		 * @param parent
		 */
		private void pushToBack(ViewGroup parent)
		{
			parent.removeView(container);
			ArrayList<View> allItems = new ArrayList<View>();

			allItems.add(container);

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
		}

		/**
		 * Pulls the requesting view to the foreground
		 */
		private void bringToFront()
		{
			container.bringToFront();
		}
	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box, super.context));
		
		toFront.setOnClickListener(new ZorderModuleListener());
		toBack.setOnClickListener(new ZorderModuleListener());	
	}

}
