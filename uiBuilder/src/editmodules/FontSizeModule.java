package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import data.ObjectValues;
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

	private Bundle valuesBundle;
	
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
		box.setOnClickListener(new ExpansionListener(box, super.context));
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
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		valuesBundle = (Bundle) container.getTag();
		adaptToContext();
		
		return box;
	}

	

	@Override
	protected void adaptToContext()
	{
		picker.setValue(valuesBundle.getInt(ObjectValues.FONTSIZE));
	}
	
	private class FontsizeModuleListener implements OnValueChangeListener
	{

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal)
		{
			valuesBundle.putInt(ObjectValues.FONTSIZE, newVal);

			((TextView) item).setTextSize(valuesBundle.getInt(ObjectValues.FONTSIZE));
			item.invalidate();
		}
	}
}
