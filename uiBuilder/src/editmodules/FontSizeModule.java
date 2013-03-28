package editmodules;

import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;

public class FontSizeModule extends Module
{
	private View box;
	private View requesting;
	
	private NumberPicker picker;
	
	public FontSizeModule(Context context)
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
		box = super.inflater.inflate(R.layout.editmode_entry_text_size, null);

		picker = (NumberPicker) box.findViewById(R.id.item_edit_editsize_picker);
		picker.setOnValueChangedListener(new FontsizeModuleListener());
		picker.setMinValue(5);
		picker.setMaxValue(130);
	}

	@Override
	public View getInstance(View inProgress)
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
