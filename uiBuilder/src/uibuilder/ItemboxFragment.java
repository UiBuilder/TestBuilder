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

public class ItemboxFragment extends Fragment implements OnClickListener,
		OnTouchListener
{
	private Button createButton, createTextView, createImage, createEditText,
			createRadioGroup, createSwitch, createCheckbox, createSearch,
			createNumberPicker, createRatingBar, createSeekBar,
			createTimePicker, createContainer;

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
		createButton = (Button) layout.findViewById(R.id.element_button);

		createTextView = (Button) layout.findViewById(R.id.element_textview);

		createImage = (Button) layout.findViewById(R.id.element_imageview);

		createEditText = (Button) layout.findViewById(R.id.element_edittext);

		createRadioGroup = (Button) layout.findViewById(R.id.element_radiogroup);

		createSwitch = (Button) layout.findViewById(R.id.element_switch);

		createCheckbox = (Button) layout.findViewById(R.id.element_checkbox);

		createSearch = (Button) layout.findViewById(R.id.element_search);

		createNumberPicker = (Button) layout.findViewById(R.id.element_numberpick);

		createRatingBar = (Button) layout.findViewById(R.id.element_ratingbar);

		createSeekBar = (Button) layout.findViewById(R.id.element_seekbar);

		createTimePicker = (Button) layout.findViewById(R.id.element_timepicker);

		createContainer = (Button) layout.findViewById(R.id.element_container);

		createButton.setOnTouchListener(this);
		createTextView.setOnTouchListener(this);
		createImage.setOnTouchListener(this);
		createEditText.setOnTouchListener(this);
		createRadioGroup.setOnTouchListener(this);
		createSwitch.setOnTouchListener(this);
		createCheckbox.setOnTouchListener(this);
		createSearch.setOnTouchListener(this);
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
	public void onClick(View v)
	{

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:

			listener.typeChanged(v.getId());
			break;

		default:
			break;
		}
		return true;
	}

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
