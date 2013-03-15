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
import android.widget.LinearLayout;
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
		LinearLayout createButton = (LinearLayout) layout.findViewById(R.id.element_button);

		LinearLayout createTextView = (LinearLayout) layout.findViewById(R.id.element_textview);

		LinearLayout createImage = (LinearLayout) layout.findViewById(R.id.element_imageview);

		LinearLayout createEditText = (LinearLayout) layout.findViewById(R.id.element_edittext);

		LinearLayout createRadioGroup = (LinearLayout) layout.findViewById(R.id.element_radiogroup);

		LinearLayout createSwitch = (LinearLayout) layout.findViewById(R.id.element_switch);

		LinearLayout createCheckbox = (LinearLayout) layout.findViewById(R.id.element_checkbox);

		LinearLayout createList = (LinearLayout) layout.findViewById(R.id.element_list);
		
		LinearLayout createGrid = (LinearLayout) layout.findViewById(R.id.element_grid);

		LinearLayout createNumberPicker = (LinearLayout) layout.findViewById(R.id.element_numberpick);

		LinearLayout createRatingBar = (LinearLayout) layout.findViewById(R.id.element_ratingbar);

		LinearLayout createSeekBar = (LinearLayout) layout.findViewById(R.id.element_seekbar);

		LinearLayout createTimePicker = (LinearLayout) layout.findViewById(R.id.element_timepicker);

		LinearLayout createContainer = (LinearLayout) layout.findViewById(R.id.element_container);

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
		createGrid.setOnTouchListener(this);
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
