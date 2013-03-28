package editmodules;

import uibuilder.EditmodeFragment.ListLayoutModuleListener;
import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ListModule extends Module
{
	private View box;
	private View requesting;
	
	public ListModule(Context context)
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
		box = super.inflater.inflate(R.layout.editmode_entry_choose_list_config, null);
				
		LinearLayout layoutTypeOne = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_1);
		LinearLayout layoutTypeTwo = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_2);
		LinearLayout layoutTypeThree = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_3);
		LinearLayout layoutTypeFour = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_4);
		LinearLayout layoutTypeFive = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_5);
		LinearLayout layoutTypeSix = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_6);

		ListLayoutModuleListener listLayoutListener = new ListLayoutModuleListener();

		layoutTypeOne.setOnClickListener(listLayoutListener);
		layoutTypeTwo.setOnClickListener(listLayoutListener);
		layoutTypeThree.setOnClickListener(listLayoutListener);
		layoutTypeFour.setOnClickListener(listLayoutListener);
		layoutTypeFive.setOnClickListener(listLayoutListener);
		layoutTypeSix.setOnClickListener(listLayoutListener);
		
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
	
	private class ListLayoutModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View listLayout)
		{
			int id = listLayout.getId();

			switch (id)
			{
			case R.id.editmode_list_included_layout_1:
			case R.id.editmode_list_included_layout_2:
			case R.id.editmode_list_included_layout_3:
			case R.id.editmode_list_included_layout_4:
			case R.id.editmode_list_included_layout_5:
			case R.id.editmode_list_included_layout_6:
				editListener.refreshAdapter(currentView, id);
				break;

			default:
				break;
			}
		}
	}


}
