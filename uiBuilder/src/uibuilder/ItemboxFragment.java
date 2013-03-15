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
import android.widget.ImageButton;
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

		ImageButton createContainer = (ImageButton) layout.findViewById(R.id.element_container);

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
