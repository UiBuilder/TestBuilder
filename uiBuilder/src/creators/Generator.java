package creators;

import manipulators.TheBoss;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
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
			
		default:
			throw new NoClassDefFoundError();
		}
		xmlView.setOnTouchListener(manipulator);
		xmlView.setId(idCount++);

		xmlView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//factory.setMinDimensions(xmlView);
		return xmlView;
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
		
			    View npView = inflater.inflate(R.layout.dialog_numberpicker_layout, null);
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
			    
	}
}
