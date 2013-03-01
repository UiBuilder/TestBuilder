package uibuilder;

import creators.ObjectFactory;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.ur.rk.uibuilder.R;

public class ItemboxFragment extends Fragment implements OnClickListener, OnTouchListener
{
	private Button createButton, createTextView, createImage, createEditText, createRadioGroup, createSwitch;

	private View layout;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		layout = inflater.inflate(R.layout.layout_itembox_fragment, container, false);
		
		setupLibraryUi();
		return layout;
	}

	private void setupLibraryUi()
	{
		createButton = (Button) layout.findViewById(R.id.new_element_button);
		createButton.setId(ObjectFactory.ID_BUTTON);
		
		createTextView = (Button) layout.findViewById(R.id.new_element_textview);
		createTextView.setId(ObjectFactory.ID_TEXTVIEW);
		
		createImage = (Button) layout.findViewById(R.id.new_element_imageview);
		createImage.setId(ObjectFactory.ID_IMAGEVIEW);
		
		createEditText = (Button) layout.findViewById(R.id.new_element_edittext);
		createEditText.setId(ObjectFactory.ID_EDITTEXT);
		
		createRadioGroup = (Button) layout.findViewById(R.id.new_element_radiogroup);
		createRadioGroup.setId(ObjectFactory.ID_RADIOBUTTONS);
		
		createSwitch = (Button) layout.findViewById(R.id.new_element_switch);
		createSwitch.setId(ObjectFactory.ID_SWITCH);
		
		createButton.setOnTouchListener(this);
		createTextView.setOnTouchListener(this);
		createImage.setOnTouchListener(this);
		createEditText.setOnTouchListener(this);
		createRadioGroup.setOnTouchListener(this);
		createSwitch.setOnTouchListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v)
	{
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:
			
			switch (v.getId())
			{
			case ObjectFactory.ID_BUTTON:
				
				listener.typeChanged(ObjectFactory.ID_BUTTON);
				Log.d("itembox reports: set to", "Button");
				break;

			case ObjectFactory.ID_TEXTVIEW:
				
				listener.typeChanged(ObjectFactory.ID_TEXTVIEW);
				Log.d("itembox reports: set to", "TextView");
				break;
				
			case ObjectFactory.ID_IMAGEVIEW:
				
				listener.typeChanged(ObjectFactory.ID_IMAGEVIEW);
				Log.d("itembox reports: set to", "ImageView");
				break;
				
			case ObjectFactory.ID_EDITTEXT:
				
				listener.typeChanged(ObjectFactory.ID_EDITTEXT);
				Log.d("itembox reports: set to", "Edittext");
				break;
				
			case ObjectFactory.ID_RADIOBUTTONS:
				
				listener.typeChanged(ObjectFactory.ID_RADIOBUTTONS);
				Log.d("itembox reports: set to", "Radiogroup");
				break;
				
			case ObjectFactory.ID_SWITCH:
				
				listener.typeChanged(ObjectFactory.ID_SWITCH);
				Log.d("itembox reports: set to", "Switch");
			default:
				break;
			}		
			
			break;

		default:
			break;
		}
		return false;
	}

	public interface onUiElementSelectedListener
	{
		void typeChanged(int id);
	}
	
	private static onUiElementSelectedListener listener; 
	
	public static void setOnUiElementSelectedListener (onUiElementSelectedListener listener)
	{
		ItemboxFragment.listener = listener;
	}
}
