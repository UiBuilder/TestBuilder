package creators;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
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
	
	private RelativeLayout.LayoutParams childParams, parentParams;

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
		RelativeLayout container = builder.createContainer();

		Bundle valueBundle = Bundler.getDefaultValueBundle(id, res);
		
		parentParams = new RelativeLayout.LayoutParams(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));
		childParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		childParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		switch (id)
		{
		case ObjectIdMapper.OBJECT_ID_BUTTON:
			xmlView = builder.buildButton();
			
			break;

		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:
			xmlView = builder.buildTextview();
			
			break;

		case ObjectIdMapper.OBJECT_ID_IMAGEVIEW:
			Log.d("building", "IMAGE");
			xmlView = builder.buildImageView();
			
			break;

		case ObjectIdMapper.OBJECT_ID_EDITTEXT:
			xmlView = builder.buildEditText();
			
			break;

		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:
			xmlView = builder.buildRadioButtons();
			
			break;

		case ObjectIdMapper.OBJECT_ID_SWITCH:
			xmlView = builder.buildSwitch();

			
			break;

		case ObjectIdMapper.OBJECT_ID_CHECKBOX:
			xmlView = builder.buildCheckBox();

			
			break;

		case ObjectIdMapper.OBJECT_ID_LISTVIEW:
			xmlView = builder.buildListView();
			
			break;

		case ObjectIdMapper.OBJECT_ID_NUMBERPICKER:
			xmlView = builder.buildNumberPicker();

			
			break;

		case ObjectIdMapper.OBJECT_ID_RATINGBAR:
			xmlView = builder.buildRatingBar();

			
			break;

		case ObjectIdMapper.OBJECT_ID_SEEKBAR:
			xmlView = builder.buildSeekBar();

			
			break;

		case ObjectIdMapper.OBJECT_ID_TIMEPICKER:
			xmlView = builder.buildTimePicker();

			
			break;
/*
		case R.id.element_container:
			xmlView = buildRelativeContainer();
			
			break;
			*/
		case ObjectIdMapper.OBJECT_ID_GRIDVIEW:
			xmlView = builder.buildGrid();
			
			break;
			
		case ObjectIdMapper.OBJECT_ID_SPINNER:
			xmlView = builder.buildSpinner();
			break;
		
		default:
			throw new NoClassDefFoundError();
		}
		
		xmlView.setLayoutParams(childParams);
		container.setLayoutParams(parentParams);
		
		container.addView(xmlView);

		
		//xmlView.setBackgroundResource(valueBundle.getInt(ObjectValues.BACKGROUND_EDIT));
		container.setId(idCount++);
		container.setTag(valueBundle);
		container.setOnTouchListener(manipulator);
		
		xmlView.measure(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));
		container.measure(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));
		 
		return container;
	}

	
}
