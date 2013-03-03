package creators;

import helpers.Log;
import manipulators.TheBoss;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	private TheBoss manipulator;
	private ObjectFactory factory;
	private LayoutInflater inflater;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, TheBoss mp, ObjectFactory fucktory)
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
		case ObjectFactory.ID_BUTTON:
			xmlView = newButton();
			break;
			
		case ObjectFactory.ID_TEXTVIEW:
			xmlView = newTextview();
			break;
			
		case ObjectFactory.ID_IMAGEVIEW:
			xmlView = newImageView();
			break;

		case ObjectFactory.ID_EDITTEXT:
			xmlView = newEditText();
			break;
			
		case ObjectFactory.ID_RADIOBUTTONS:
			xmlView = newRadioButtons();
			break;
			
		case ObjectFactory.ID_SWITCH:
			xmlView = newSwitch();
			break;
			
		case ObjectFactory.ID_CHECKBOX:
			xmlView = newCheckBox();
			break;
			
		case ObjectFactory.ID_SEARCHVIEW:
			xmlView = newSearchView();
			break;
			
		case ObjectFactory.ID_NUMBERPICKER:
			xmlView = newNumberPicker();
			break;
			
		case ObjectFactory.ID_RATINGBAR:
			xmlView = newRatingBar();
			break;
			
		case ObjectFactory.ID_SEEKBAR:
			xmlView = newSeekBar();
			break;
			
		case ObjectFactory.ID_TIMEPICKER:
			xmlView = newTimePicker();
			break;
			
		default:
			//throw new NoClassDefFoundError();
			return null; 
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
		xmlTimePicker.setEnabled(true);
		xmlTimePicker.setActivated(true);
		xmlTimePicker.setFocusableInTouchMode(true);
		
		for (int i=0; i<xmlTimePicker.getChildCount();i++)
		{
			View v = xmlTimePicker.getChildAt(i);
			v.setEnabled(false);
			v.setClickable(true);
			v.setActivated(false);
			v.setFocusable(true);
			
			v.setFocusableInTouchMode(true);v.setOnTouchListener(new OnTouchListener()
			{
				
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					Log.d("item toch", "hui");
					return false;
				}
			});
		}
		xmlTimePicker.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		xmlTimePickerContainer.addView(xmlTimePicker);
		
		xmlTimePickerContainer.setFilterTouchesWhenObscured(true);
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
		
		xmlRatingBarContainer.setFilterTouchesWhenObscured(false);
		
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

		View up = xmlPicker.getChildAt(0);
		up.setEnabled(false);
		up.setClickable(true);
		up.setFocusableInTouchMode(true);
		
		
		View down = xmlPicker.getChildAt(2);
		down.setEnabled(false);
		down.setClickable(true);
		down.setFocusableInTouchMode(true);

		
		View middle = xmlPicker.getChildAt(1);
		middle.setEnabled(false);
		middle.setClickable(true);
		middle.setFocusableInTouchMode(true);
		middle.setPadding(0, 0, 0, 0);
		
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		xmlPickerLayout.addView(xmlPicker);
		xmlPickerLayout.setLayoutParams(params);
		
		return xmlPickerLayout;
	}

	private RelativeLayout createContainer()
	{
		RelativeLayout xmlPickerLayout = new RelativeLayout(context);
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
		RelativeLayout xmlSearchView = (RelativeLayout) inflater.inflate(R.layout.item_searchview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlSearchView.setLayoutParams(params);
		/*xmlSearchView.setActivated(false);
		xmlSearchView.setClickable(false);
		xmlSearchView.setFocusableInTouchMode(false);

		*/
		return xmlSearchView;
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
