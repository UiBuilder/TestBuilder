package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import de.ur.rk.uibuilder.R;


/**
 * This class is representing the sidebar fragment and provides the user interface
 * to choose an object type from the library of supported types
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

	/**
	 * Get references to the library elements and set onTouchlistener
	 */
	private void setupLibraryUi()
	{
		ImageButton createButton = (ImageButton) layout.findViewById(R.id.element_button);

		ImageButton createTextView = (ImageButton) layout.findViewById(R.id.element_textview);

		ImageButton createImage = (ImageButton) layout.findViewById(R.id.element_imageview);

		ImageButton createEditText = (ImageButton) layout.findViewById(R.id.element_edittext);

		ImageButton createRadioGroup = (ImageButton) layout.findViewById(R.id.element_radiogroup);

		ImageButton createSwitch = (ImageButton) layout.findViewById(R.id.element_switch);

		ImageButton createCheckbox = (ImageButton) layout.findViewById(R.id.element_checkbox);

		ImageButton createList = (ImageButton) layout.findViewById(R.id.element_list);
		
		ImageButton createGrid = (ImageButton) layout.findViewById(R.id.element_grid);

		ImageButton createNumberPicker = (ImageButton) layout.findViewById(R.id.element_numberpick);

		ImageButton createRatingBar = (ImageButton) layout.findViewById(R.id.element_ratingbar);

		ImageButton createSeekBar = (ImageButton) layout.findViewById(R.id.element_seekbar);

		ImageButton createTimePicker = (ImageButton) layout.findViewById(R.id.element_timepicker);

		//LinearLayout createContainer = (LinearLayout) layout.findViewById(R.id.element_container);

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
		//createContainer.setOnTouchListener(this);
		createGrid.setOnTouchListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("Itembox Fragment", "onCreateView called");
		super.onCreate(savedInstanceState);
	}

	/**
	 * Set activated states for visual feedback.
	 * Deactivate the former selection and notify
	 * the listener of the event.
	 */
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
	 * Interface to notify listeners that the user has selected another type
	 * of view he wants to create.
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
