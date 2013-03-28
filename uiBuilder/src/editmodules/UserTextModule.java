package editmodules;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;



public class UserTextModule extends Module
{
	private EditText editText;

	private LinearLayout box;
	
	private View requesting;

	public UserTextModule(Context context)
	{
		super(context);
	}

	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_text_size, null);

		editText = (EditText) box.findViewById(R.id.item_edit_edittext);
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		adaptToContext();
		
		return box;
	}

	public void setViewText(String string)
	{
		if (requesting instanceof LinearLayout && requesting != null)
		{
			TextView textView = (TextView) ((LinearLayout) requesting).getChildAt(0);

			textView.setText(string);

		} else if (requesting instanceof EditText && requesting != null)
		{
			((EditText) requesting).setHint(string);

		} else if (requesting != null)
		{
			((TextView) requesting).setText(string);
		}
	}

	@Override
	public void getValues()
	{
		adaptToContext();

	}

	@Override
	protected void adaptToContext()
	{
		if (requesting instanceof LinearLayout)
		{
			TextView textView = (TextView) ((LinearLayout) requesting).getChildAt(0);
			if (requesting instanceof EditText)
			{
				editText.setText(textView.getHint());

			} else
			{
				editText.setText(textView.getText());

			}

		} else
		{
			if (requesting instanceof EditText)
			{
				editText.setText(((TextView)requesting).getHint());

			} else
			{
				editText.setText(((TextView)requesting).getText());

			}
		}		
	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box));

		//editText.addTextChangedListener(new UserTextModuleListener());
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{
			// TODO Auto-generated method stub

		}
	}
}
