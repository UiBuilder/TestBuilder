package editmodules;

import uibuilder.EditmodeFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the fontsize.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author jonesses
 *
 */
public class FontSizeModule extends Module
{
	private LinearLayout box;
	private View requesting;
	
	private NumberPicker picker;
	
	public FontSizeModule(EditmodeFragment context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getValues()
	{
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_text_size, null);
		picker = (NumberPicker) box.findViewById(R.id.item_edit_editsize_picker);
		
		setDefaultValues();
	}

	/**
	 * 
	 */
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box));
		picker.setOnValueChangedListener(new FontsizeModuleListener());
	}

	/**
	 * 
	 */
	private void setDefaultValues()
	{
		picker.setMinValue(5);
		picker.setMaxValue(130);
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		
		adaptToContext();
		return box;
	}
	

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub
		picker.setValue((int) ((TextView) requesting).getTextSize());
	}
	
	private class FontsizeModuleListener implements OnValueChangeListener
	{

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal)
		{
			((TextView) requesting).setTextSize(newVal);
			requesting.invalidate();
		}
	}


}
