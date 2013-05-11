package editmodules;

import uibuilder.EditmodeFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import data.SampleAdapter;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the sample layout of a listView.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author funklos
 *
 */
public class ListLayoutModule extends Module
{
	private LinearLayout box;
	
	private SampleAdapter samples;
	
	LinearLayout 
			layoutTypeOne,
			layoutTypeTwo,
			layoutTypeThree,
			layoutTypeFour,
			layoutTypeFive,
			layoutTypeSix;
	
	public ListLayoutModule(EditmodeFragment context)
	{
		super(context);
		samples = new SampleAdapter(super.context);
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
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_list_config, null);
				
		layoutTypeOne = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_1);
		layoutTypeTwo = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_2);
		layoutTypeThree = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_3);
		layoutTypeFour = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_4);
		layoutTypeFive = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_5);
		layoutTypeSix = (LinearLayout) box.findViewById(R.id.editmode_list_included_layout_6);

		setListeners();	
	}

	/**
	 * 
	 */
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box, super.context));
		ListLayoutModuleListener listLayoutListener = new ListLayoutModuleListener();

		layoutTypeOne.setOnClickListener(listLayoutListener);
		layoutTypeTwo.setOnClickListener(listLayoutListener);
		layoutTypeThree.setOnClickListener(listLayoutListener);
		layoutTypeFour.setOnClickListener(listLayoutListener);
		layoutTypeFive.setOnClickListener(listLayoutListener);
		layoutTypeSix.setOnClickListener(listLayoutListener);
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
	
	private class ListLayoutModuleListener implements OnClickListener
	{
		@Override
		public void onClick(View listLayout)
		{
			int id = listLayout.getId();

			samples.setSampleLayout(container, id);
		}
	}


}
