package creators;

import helpers.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

	private int idCount; /** Variable zur dynamischen Vergabe laufender IDs */

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
	
	protected View generate (int id)
	{
		View xmlView;
		switch (id)
		{
		case R.id.element_button:
			xmlView = newButton();
			break;
			
		case R.id.element_textview:
			xmlView = newTextview();
			break;
			
		case R.id.element_imageview:
			xmlView = newImageView();
			break;

		case R.id.element_edittext:
			xmlView = newEditText();
			break;
			
		case R.id.element_radiogroup:
			xmlView = newRadioButtons();
			break;
			
		case R.id.element_switch:
			xmlView = newSwitch();
			break;
			
		case R.id.element_checkbox:
			xmlView = newCheckBox();
			break;
			
		case R.id.element_search:
			xmlView = newSearchView();
			break;
			
		case R.id.element_numberpick:
			xmlView = newNumberPicker();
			break;
			
		case R.id.element_ratingbar:
			xmlView = newRatingBar();
			break;
			
		case R.id.element_seekbar:
			xmlView = newSeekBar();
			break;
			
		case R.id.element_timepicker:
			xmlView = newTimePicker();
			break;
			
		default:
			throw new NoClassDefFoundError(); 
		}
		xmlView.setOnTouchListener(manipulator);
		xmlView.setId(idCount++);

		xmlView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		//factory.setMinDimensions(xmlView);
		Log.d("pickergenerate", "rturning picker");
		return xmlView;
	}
	
	private View newTimePicker()
	{
		RelativeLayout xmlTimePickerContainer = createContainer();
		
		TimePicker xmlTimePicker = (TimePicker) inflater.inflate(R.layout.item_timepicker_layout, null);
		xmlTimePicker.setIs24HourView(true);
		xmlTimePicker.setEnabled(false);
		
		RelativeLayout.LayoutParams pickerparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pickerparams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		//pickerparams.addRule(RelativeLayout., anchor)
		
		xmlTimePicker.setLayoutParams(pickerparams);
		xmlTimePickerContainer.addView(xmlTimePicker);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlTimePickerContainer.setLayoutParams(params);
		
		return xmlTimePickerContainer;
	}

	private View newSeekBar()
	{
		RelativeLayout xmlSeekBarContainer = createContainer();
		
		SeekBar xmlSeekBar = (SeekBar) inflater.inflate(R.layout.item_seekbar_layout, null);
		xmlSeekBar.setEnabled(false);
		xmlSeekBar.setActivated(true);
		
		xmlSeekBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		xmlSeekBarContainer.addView(xmlSeekBar);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlSeekBarContainer.setLayoutParams(params);
		
		return xmlSeekBarContainer;
	}

	private View newRatingBar()
	{
		RelativeLayout xmlRatingBarContainer = createContainer();
		
		RatingBar xmlRatingBar = (RatingBar) inflater.inflate(R.layout.item_ratingbar_layout, null);
		xmlRatingBar.setEnabled(true);
		xmlRatingBar.setActivated(true);

		xmlRatingBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		xmlRatingBarContainer.addView(xmlRatingBar);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlRatingBarContainer.setLayoutParams(params);
		
		return xmlRatingBarContainer;
	}

	private View newNumberPicker()
	{
		RelativeLayout xmlPickerLayout = createContainer();
		
		NumberPicker xmlPicker = (NumberPicker) inflater.inflate(R.layout.item_numberpicker_layout, null);

		xmlPicker.setEnabled(false);
		xmlPicker.setMaxValue(5);
		xmlPicker.setMinValue(1);
		xmlPicker.setWrapSelectorWheel(false);
		xmlPicker.setValue(3);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		xmlPickerLayout.addView(xmlPicker);
		xmlPickerLayout.setLayoutParams(params);
		
		return xmlPickerLayout;
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

	private View newSearchView()
	{
		RelativeLayout xmlSearchViewContainer = createContainer();
		
		SearchView xmlSearchView = (SearchView) inflater.inflate(R.layout.item_searchview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlSearchView.setLayoutParams(params);

		xmlSearchView.setEnabled(false);
		xmlSearchViewContainer.addView(xmlSearchView);
		
		xmlSearchViewContainer.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return xmlSearchViewContainer;
	}

	private View newCheckBox()
	{
		RelativeLayout xmlCheckBox = (RelativeLayout) inflater.inflate(R.layout.item_checkbox_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlCheckBox.setLayoutParams(params);
		
		return xmlCheckBox;
	}

	private View newSwitch()
	{
		Switch xmlSwitch = (Switch) inflater.inflate(R.layout.item_switch_layout, null);
	
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlSwitch.setLayoutParams(params);
		
		return xmlSwitch;
	}

	private View newRadioButtons()
	{
		//askForSpecification(ObjectFactory.ID_RADIOBUTTONS);
		
		RadioGroup xmlRadioGroup = (RadioGroup) inflater.inflate(R.layout.item_radiogroup_layout, null);
		
		RadioButton xmlRadioButton = (RadioButton) inflater.inflate(R.layout.item_radiobutton_layout, null);
		RadioButton xmlRadioButton2 = (RadioButton) inflater.inflate(R.layout.item_radiobutton_layout, null);
		
		xmlRadioGroup.addView(xmlRadioButton);
		xmlRadioGroup.addView(xmlRadioButton2);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlRadioGroup.setLayoutParams(params);
		
		return xmlRadioGroup;
	}

	/**
	 * Generate new TextView from xml resource
	 * 
	 * @return new TextView
	 */
	private TextView newTextview()
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
	private ImageView newImageView()
	{
		ImageView xmlImageView = (ImageView) inflater.inflate(R.layout.item_imageview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlImageView.setLayoutParams(params);

		return xmlImageView;
	}

	/**
	 * Generate new EditText from xml resource
	 * @return new Edittext
	 */
	private EditText newEditText()
	{
		EditText xmlEditText = (EditText) inflater.inflate(R.layout.item_edittext_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlEditText.setLayoutParams(params);
		
		return xmlEditText;
	}

	/**
	 * Generate new Button from xml resource
	 * 
	 * @return new Button
	 */
	private Button newButton()
	{	
		Button xmlButton = (Button) inflater.inflate(R.layout.item_button_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlButton.setLayoutParams(params);
		
		return xmlButton;
	}
/*
	private void askForSpecification(int which)
	{
		switch (which)
		{
		case ObjectFactory.ID_RADIOBUTTONS:
			
			askForNumber().show();
			
			break;

		default:
			break;
		}
		
	}

	private AlertDialog askForNumber()
	{
		
			    View npView = inflater.inflate(R.layout.item_numberpicker_layout, null);
			    NumberPicker np = (NumberPicker) npView.findViewById(R.id.np);
			    np.setMaxValue(5);
			    np.setMinValue(1);
			    np.setWrapSelectorWheel(false);
			    
			    return new AlertDialog.Builder(context.getApplicationContext())
			        .setTitle("Number of Items")
			        .setView(npView)
			        .setPositiveButton("OK",
			            new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) 
			                {

			                }
			            })
			            .setNegativeButton("Cancel",
			                new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	
			                    }
			                })
			            .create();
			    
	}*/
}
