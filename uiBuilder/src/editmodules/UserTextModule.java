package editmodules;

import creators.ObjectIdMapper;
import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;


/**
 * Provides the interface to change the text content.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author jonesses
 *
 */
public class UserTextModule extends Module
{
	private EditText editText;

	private LinearLayout box;
	

	public UserTextModule(EditmodeFragment context)
	{
		super(context);
	}

	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_enter_text, null);

		editText = (EditText) box.findViewById(R.id.item_edit_edittext);
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
	public void getValues()
	{
		adaptToContext();

	}

	@Override
	protected void adaptToContext()
	{
		Bundle objectBundle = (Bundle) container.getTag();
		int id = objectBundle.getInt(ObjectValues.TYPE);
		
		switch (id)
		{
		case ObjectIdMapper.OBJECT_ID_EDITTEXT:
			
			editText.setText(((EditText) item).getHint());
			break;
			
		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:
		case ObjectIdMapper.OBJECT_ID_SWITCH:
		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:
		case ObjectIdMapper.OBJECT_ID_BUTTON:
			
			editText.setText(((TextView) item).getText());
			break;
			
		case ObjectIdMapper.OBJECT_ID_CHECKBOX:
			
			TextView textView = (TextView) ((LinearLayout) item).getChildAt(0);

			editText.setText(textView.getText());
			break;
			
		}	
	}

	@Override
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box, super.context));
		UserTextModuleListener textListener = new UserTextModuleListener();
		editText.addTextChangedListener(textListener);
	}
	
	
	/**
	 * Check if the requesting view is inside a container and set the text appropriately.
	 * @param string
	 */
	public void setViewText(String string)
	{
		Bundle objectBundle = (Bundle) container.getTag();
		int id = objectBundle.getInt(ObjectValues.TYPE);
		
		switch (id)
		{
		case ObjectIdMapper.OBJECT_ID_EDITTEXT:
			
			((EditText) item).setHint(string);
			break;
			
		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:
		case ObjectIdMapper.OBJECT_ID_SWITCH:
		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:
		case ObjectIdMapper.OBJECT_ID_BUTTON:
			
			((TextView) item).setText(string);
			break;
			
		case ObjectIdMapper.OBJECT_ID_CHECKBOX:
			
			TextView textView = (TextView) ((LinearLayout) item).getChildAt(0);

			textView.setText(string);
			break;
		}
	}

	private class UserTextModuleListener implements TextWatcher
	{

		@Override
		public void afterTextChanged(Editable s)
		{
			setViewText(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after)
		{

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{

		}
	}
}
