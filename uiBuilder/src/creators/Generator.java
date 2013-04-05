package creators;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import data.Bundler;
import data.ObjectValues;

/**
 * This class is used to determine which element is requested to be instantiated.
 * It uses a FromXmlBuilder instance the generate the item,
 * an onTouchListener provided as a parameter by the constructor to prepare the item for
 * user interaction,
 * and
 * accesses the Bundler class in a static way to fetch a default bundle, to add to the element
 * which will contain the objects properties in the future of its use.
 * @author funklos
 *
 */
public class Generator
{

	private static int idCount;
	/** Variable zur dynamischen Vergabe laufender IDs */

	private OnTouchListener manipulator;

	private FromXmlBuilder builder;
	
	private Resources res;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, OnTouchListener mp)
	{
		idCount = 1;
		manipulator = mp;
		
		builder = new FromXmlBuilder(ref);
		res = ref.getApplicationContext().getResources();
	}

	/**
	 * Use this method to create a View by passing an identifier of the type of view to be created.
	 * The view receives a bundle, for the storage of properties and a listener, to become 
	 * interactive.
	 * 
	 * @param id specifies the View to be created.
	 * @return the created View
	 */
	protected View generate(int id)
	{
		View xmlView;
		RelativeLayout.LayoutParams params = null;
		Bundle valueBundle = Bundler.getDefaultValueBundle(id, res);
		params = new RelativeLayout.LayoutParams(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));

		switch (id)
		{
		case ObjectIds.OBJECT_ID_BUTTON:
			xmlView = builder.buildButton();
			
			break;

		case ObjectIds.OBJECT_ID_TEXTVIEW:
			xmlView = builder.buildTextview();
			
			break;

		case ObjectIds.OBJECT_ID_IMAGEVIEW:
			xmlView = builder.buildImageView();
			
			break;

		case ObjectIds.OBJECT_ID_EDITTEXT:
			xmlView = builder.buildEditText();
			
			break;

		case ObjectIds.OBJECT_ID_RADIOGROUP:
			xmlView = builder.buildRadioButtons();
			
			break;

		case ObjectIds.OBJECT_ID_SWITCH:
			xmlView = builder.buildSwitch();

			
			break;

		case ObjectIds.OBJECT_ID_CHECKBOX:
			xmlView = builder.buildCheckBox();

			
			break;

		case ObjectIds.OBJECT_ID_LISTVIEW:
			xmlView = builder.buildListView();
			
			break;

		case ObjectIds.OBJECT_ID_NUMBERPICKER:
			xmlView = builder.buildNumberPicker();

			
			break;

		case ObjectIds.OBJECT_ID_RATINGBAR:
			xmlView = builder.buildRatingBar();

			
			break;

		case ObjectIds.OBJECT_ID_SEEKBAR:
			xmlView = builder.buildSeekBar();

			
			break;

		case ObjectIds.OBJECT_ID_TIMEPICKER:
			xmlView = builder.buildTimePicker();

			
			break;
/*
		case R.id.element_container:
			xmlView = buildRelativeContainer();
			
			break;
			*/
		case ObjectIds.OBJECT_ID_GRIDVIEW:
			xmlView = builder.buildGrid();
			
			break;
		
		default:
			throw new NoClassDefFoundError();
		}
		
		xmlView.setLayoutParams(params);
		
		xmlView.setBackgroundResource(valueBundle.getInt(ObjectValues.BACKGROUND_EDIT));
		xmlView.setId(idCount++);
		xmlView.setTag(valueBundle);
		xmlView.setOnTouchListener(manipulator);
		
		xmlView.measure(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));
		 
		return xmlView;
	}

	
}
