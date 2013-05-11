package uibuilder;

import creators.ObjectIdMapper;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import de.ur.rk.uibuilder.R;


/**
 * This class is representing the sidebar fragment and provides the user interface
 * to choose an object type from the library of supported types
 * 
 * @author funklos edited by jonesses
 *
 */
public class ItemboxFragment extends Fragment implements
		OnTouchListener, OnDragListener
{
	private View active;

	private View layout;
	
	private Context context;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		context = getActivity();
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
		
		ImageButton createSpinner = (ImageButton) layout.findViewById(R.id.element_spinner);

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
		createSpinner.setOnTouchListener(this);
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
		
		int idSelected = v.getId();
		int objectType = ObjectIdMapper.mapType(idSelected);
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			newV = requestListener.requestObject(objectType, event);
			//newV.setBackgroundColor(context.getResources().getColor(R.color.fresh_aqua));
			
			newV.setVisibility(View.INVISIBLE);
			v.setActivated(true);
			break;
		
		case MotionEvent.ACTION_UP:
			
			listener.typeChanged(objectType);
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			if (newV != null)
			{
			
			ClipData.Item item = new ClipData.Item("itembox");
			ClipData clipData = new ClipData((CharSequence) String.valueOf(objectType), new String[]
			{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

			newV.startDrag(clipData, new View.DragShadowBuilder(newV), newV, 0);
			//active.setActivated(false);
			v.setActivated(false);
			
			ViewGroup parent = (ViewGroup) newV.getParent();
			parent.removeView(newV);
			newV = null;
			}
			return false;

		default:
			break;
		}
		return true;
	}
	
	private View newV;

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

	@Override
	public boolean onDrag(View inProcess, DragEvent event)
	{
		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			Log.d("drag ", "started");
			break;
		
		case DragEvent.ACTION_DROP:
			
			View v = (View) event.getLocalState();
			ViewGroup parent = (ViewGroup) v.getParent();
			parent.removeView(v);
			break;
			
		case DragEvent.ACTION_DRAG_ENDED:
			
			active.setActivated(false);
			Log.d("drag ", "ended");
		
		}
		return true;
	}
	
	/**
	 * Object creation
	 * @author funklos
	 *
	 */
	public interface onObjectRequestedListener
	{
		View requestObject(int id, MotionEvent event);
	}

	private static onObjectRequestedListener requestListener;

	public static void setOnObjectRequestedListener(
			onObjectRequestedListener listener)
	{
		ItemboxFragment.requestListener = listener;
	}
	
}
