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
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	private View newV;

	private View layout;
	
	private Context context;
	
	public static final String DRAG_EVENT_ORIGIN_ITEMBOX = "itemboxdrag";

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
		
		layout.setOnDragListener(this);
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
		active = v;	
		
		
		int idSelected = v.getId();
		int objectType = ObjectIdMapper.mapType(idSelected);
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			active.setActivated(true);

			v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
			
			newV = requestListener.requestObject(objectType, event);
			newV.setBackgroundColor(context.getResources().getColor(R.color.fresh_aqua));
			
			newV.setVisibility(View.INVISIBLE);
			break;
		
		case MotionEvent.ACTION_UP:
			
			active.setActivated(false);
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			if (newV != null)
			{
				ClipData.Item item = new ClipData.Item(DRAG_EVENT_ORIGIN_ITEMBOX);
				
				ClipData clipData = new ClipData((CharSequence) String.valueOf(objectType), new String[]
				{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
	
				newV.startDrag(clipData, new View.DragShadowBuilder(newV), newV, 0);
				
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


	@Override
	public boolean onDrag(View inProcess, DragEvent event)
	{
		Log.d("itembox", "ondrag");
		
		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			
			Log.d("drag ", "started");
			break;
		
		case DragEvent.ACTION_DROP:
			
			Log.d("dropped", "deleted");
			break;
			
		case DragEvent.ACTION_DRAG_ENDED:
			
			
			Log.d("drag ", "ended");
			active.setActivated(false);
			active = null;
			break;
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
