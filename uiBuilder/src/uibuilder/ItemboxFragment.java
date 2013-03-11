package uibuilder;

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
import creators.ObjectFactory;
import de.ur.rk.uibuilder.R;
/**
 * 
 * @author funklos
 *
 */
public class ItemboxFragment extends Fragment implements
		OnTouchListener
{
	private View active;

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
		Button createButton = (Button) layout.findViewById(R.id.element_button);

		Button createTextView = (Button) layout.findViewById(R.id.element_textview);

		Button createImage = (Button) layout.findViewById(R.id.element_imageview);

		Button createEditText = (Button) layout.findViewById(R.id.element_edittext);

		Button createRadioGroup = (Button) layout.findViewById(R.id.element_radiogroup);

		Button createSwitch = (Button) layout.findViewById(R.id.element_switch);

		Button createCheckbox = (Button) layout.findViewById(R.id.element_checkbox);

		Button createList = (Button) layout.findViewById(R.id.element_list);

		Button createNumberPicker = (Button) layout.findViewById(R.id.element_numberpick);

		Button createRatingBar = (Button) layout.findViewById(R.id.element_ratingbar);

		Button createSeekBar = (Button) layout.findViewById(R.id.element_seekbar);

		Button createTimePicker = (Button) layout.findViewById(R.id.element_timepicker);

		Button createContainer = (Button) layout.findViewById(R.id.element_container);

		createButton.setOnTouchListener(this);
		createTextView.setOnTouchListener(this);
		createImage.setOnTouchListener(this);
		createEditText.setOnTouchListener(this);
		createRadioGroup.setOnTouchListener(this);
		createSwitch.setOnTouchListener(this);
		createCheckbox.setOnTouchListener(this);
		createList.setOnTouchListener(this);
		createNumberPicker.setOnTouchListener(this);
		createRatingBar.setOnTouchListener(this);
		createSeekBar.setOnTouchListener(this);
		createTimePicker.setOnTouchListener(this);
		createContainer.setOnTouchListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("Itembox Fragment", "onCreateView called");
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (v != active)
		{
			if (active != null)
			{
				active.setActivated(false);
			}
			
			active = v;
			active.setActivated(true);
		}
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:

			listener.typeChanged(v.getId());
			break;

		default:
			break;
		}
		return false;
	}

	/**
	 * 
	 * @author funklos
	 *
	 */
	public interface onUiElementSelectedListener
	{
		void typeChanged(int id);
	}

	private static onUiElementSelectedListener listener;

	public static void setOnUiElementSelectedListener(
			onUiElementSelectedListener listener)
	{
		ItemboxFragment.listener = listener;
	}
}
