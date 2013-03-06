package creators;

import helpers.Log;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import de.ur.rk.uibuilder.R;

public class Generator
{

	public static final String OBJECT_TAG = "isObject";

	private int idCount;
	/** Variable zur dynamischen Vergabe laufender IDs */

	private Context context;
	private OnTouchListener manipulator;
	private ObjectFactory factory;
	private LayoutInflater inflater;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, OnTouchListener mp, ObjectFactory fucktory)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;
		this.factory = fucktory;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected View generate(int id)
	{
		View xmlView;
		RelativeLayout.LayoutParams params =
		null;
		
		switch (id)
		{
		case R.id.element_button:
			xmlView = buildButton();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_textview:
			xmlView = buildTextview();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_imageview:
			xmlView = buildImageView();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_edittext:
			xmlView = buildEditText();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_radiogroup:
			xmlView = buildRadioButtons();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_switch:
			xmlView = buildSwitch();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_checkbox:
			xmlView = buildCheckBox();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_search:
			xmlView = buildSearchView();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_numberpick:
			xmlView = buildNumberPicker();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_ratingbar:
			xmlView = buildRatingBar();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_seekbar:
			xmlView = buildSeekBar();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_timepicker:
			xmlView = buildTimePicker();
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			break;

		case R.id.element_container:
			xmlView = buildRelativeContainer();
			break;

		default:
			throw new NoClassDefFoundError();
		}
		
		xmlView.setLayoutParams(params);
		xmlView.setOnTouchListener(manipulator);
		xmlView.setId(idCount++);
		xmlView.setTag(id);
		xmlView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// factory.setMinDimensions(xmlView);

		return xmlView;
	}

	private View buildTimePicker()
	{
		RelativeLayout xmlTimePickerContainer = createContainer();

		TimePicker xmlTimePicker = (TimePicker) inflater.inflate(R.layout.item_timepicker_layout, null);
		xmlTimePicker.setIs24HourView(true);
		xmlTimePicker.setEnabled(false);

		RelativeLayout.LayoutParams pickerparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pickerparams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		// pickerparams.addRule(RelativeLayout., anchor)

		xmlTimePicker.setLayoutParams(pickerparams);
		xmlTimePickerContainer.addView(xmlTimePicker);

		return xmlTimePickerContainer;
	}

	private View buildSeekBar()
	{
		RelativeLayout xmlSeekBarContainer = createContainer();

		SeekBar xmlSeekBar = (SeekBar) inflater.inflate(R.layout.item_seekbar_layout, null);
		xmlSeekBar.setEnabled(false);
		xmlSeekBar.setActivated(true);

		xmlSeekBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		xmlSeekBarContainer.addView(xmlSeekBar);

		return xmlSeekBarContainer;
	}

	private View buildRatingBar()
	{
		RelativeLayout xmlRatingBarContainer = createContainer();

		RatingBar xmlRatingBar = (RatingBar) inflater.inflate(R.layout.item_ratingbar_layout, null);
		xmlRatingBar.setEnabled(true);
		xmlRatingBar.setActivated(true);

		xmlRatingBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		xmlRatingBarContainer.addView(xmlRatingBar);

		return xmlRatingBarContainer;
	}

	private View buildNumberPicker()
	{
		RelativeLayout xmlPickerLayout = createContainer();

		NumberPicker xmlPicker = (NumberPicker) inflater.inflate(R.layout.item_numberpicker_layout, null);

		xmlPicker.setEnabled(false);
		xmlPicker.setMaxValue(5);
		xmlPicker.setMinValue(1);
		xmlPicker.setWrapSelectorWheel(false);
		xmlPicker.setValue(3);

		xmlPicker.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		xmlPickerLayout.addView(xmlPicker);

		return xmlPickerLayout;
	}

	private LinearLayout buildRelativeContainer()
	{
		LinearLayout relativeLayout = new LinearLayout(context);
		relativeLayout.setBackgroundResource(R.drawable.default_button_border);
		relativeLayout.setOrientation(LinearLayout.HORIZONTAL);

		relativeLayout.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View v, DragEvent event)
			{
				switch (event.getAction())
				{
				case DragEvent.ACTION_DRAG_STARTED:
					// Do nothing
					break;

				case DragEvent.ACTION_DRAG_ENTERED:

					break;
				case DragEvent.ACTION_DRAG_EXITED:

					break;

				case DragEvent.ACTION_DROP:
					// Dropped, reassign View to ViewGroup
					View view = (View) event.getLocalState();
					ViewGroup owner = (ViewGroup) view.getParent();
					owner.removeView(view);
					LinearLayout container = (LinearLayout) v;
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					container.addView(view);

					break;
				case DragEvent.ACTION_DRAG_ENDED:

				default:
					break;
				}
				return true;
			}
		});
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);

		relativeLayout.setLayoutParams(params);
		return relativeLayout;

	}

	private RelativeLayout createContainer()
	{
		RelativeLayout xmlPickerLayout = new RelativeLayout(context)
		{

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev)
			{
				Log.d("CONTAINER", "INTERCEPTING MOTION EVENT");
				return true;
			}

		};
		xmlPickerLayout.setBackgroundResource(R.drawable.default_button_border);
		xmlPickerLayout.setClickable(true);
		xmlPickerLayout.setFocusable(true);
		xmlPickerLayout.setFocusableInTouchMode(true);
		xmlPickerLayout.setEnabled(true);
		xmlPickerLayout.setMotionEventSplittingEnabled(false);
		xmlPickerLayout.setFilterTouchesWhenObscured(false);
		return xmlPickerLayout;
	}

	private View buildSearchView()
	{
		RelativeLayout xmlSearchViewContainer = createContainer();

		SearchView xmlSearchView = (SearchView) inflater.inflate(R.layout.item_searchview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlSearchView.setLayoutParams(params);

		xmlSearchView.setEnabled(false);
		xmlSearchViewContainer.addView(xmlSearchView);

		return xmlSearchViewContainer;
	}

	private View buildCheckBox() //
	{
		LinearLayout xmlCheckBox = (LinearLayout) inflater.inflate(R.layout.item_checkbox_layout, null);

		return xmlCheckBox;
	}

	private View buildSwitch()
	{
		Switch xmlSwitch = (Switch) inflater.inflate(R.layout.item_switch_layout, null);

		return xmlSwitch;
	}

	private View buildRadioButtons()
	{
		// askForSpecification(ObjectFactory.ID_RADIOBUTTONS);

		//RadioGroup xmlRadioGroup = (RadioGroup) inflater.inflate(R.layout.item_radiogroup_layout, null);

		RadioButton xmlRadioButton = (RadioButton) inflater.inflate(R.layout.item_radiobutton_layout, null);

		//xmlRadioGroup.addView(xmlRadioButton);

		return xmlRadioButton;
	}

	/**
	 * Generate new TextView from xml resource
	 * 
	 * @return new TextView
	 */
	private TextView buildTextview()
	{
		TextView xmlTextView = (TextView) inflater.inflate(R.layout.item_textview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlTextView.setLayoutParams(params);

		return xmlTextView;
	}

	/**
	 * Generate new ImageView from xml resource
	 * 
	 * @return the newly generated ImageView
	 */
	private ImageView buildImageView()
	{
		ImageView xmlImageView = (ImageView) inflater.inflate(R.layout.item_imageview_layout, null);

		return xmlImageView;
	}

	/**
	 * Generate new EditText from xml resource
	 * 
	 * @return new Edittext
	 */
	private EditText buildEditText()
	{
		EditText xmlEditText = (EditText) inflater.inflate(R.layout.item_edittext_layout, null);

		return xmlEditText;
	}

	/**
	 * Generate new Button from xml resource
	 * 
	 * @return new Button
	 */
	private Button buildButton()
	{
		Button xmlButton = (Button) inflater.inflate(R.layout.item_button_layout, null);

		return xmlButton;
	}
	/*
	 * private void askForSpecification(int which) { switch (which) { case
	 * ObjectFactory.ID_RADIOBUTTONS:
	 * 
	 * askForNumber().show();
	 * 
	 * break;
	 * 
	 * default: break; }
	 * 
	 * }
	 * 
	 * private AlertDialog askForNumber() {
	 * 
	 * View npView = inflater.inflate(R.layout.item_numberpicker_layout, null);
	 * NumberPicker np = (NumberPicker) npView.findViewById(R.id.np);
	 * np.setMaxValue(5); np.setMinValue(1); np.setWrapSelectorWheel(false);
	 * 
	 * return new AlertDialog.Builder(context.getApplicationContext())
	 * .setTitle("Number of Items") .setView(npView) .setPositiveButton("OK",
	 * new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int whichButton) {
	 * 
	 * } }) .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) {
	 * 
	 * } }) .create();
	 * 
	 * }
	 */
}
