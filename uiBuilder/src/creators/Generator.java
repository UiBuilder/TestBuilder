package creators;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import data.Bundler;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * 
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
	 * Use this method to create a View by passing an identifier of the type of view to be created
	 * 
	 * @param id specifies the View to be created.
	 * @return the created View
	 */
	protected View generate(int id)
	{
		View xmlView;
		RelativeLayout.LayoutParams params = null;
		Bundle valueBundle = Bundler.getValueBundle(id, res);
		params = new RelativeLayout.LayoutParams(valueBundle.getInt(ObjectValues.DEFAULT_WIDTH), valueBundle.getInt(ObjectValues.DEFAULT_HEIGHT));

		switch (id)
		{
		case R.id.element_button:
			xmlView = builder.buildButton();
			
			break;

		case R.id.element_textview:
			xmlView = builder.buildTextview();
			
			break;

		case R.id.element_imageview:
			xmlView = builder.buildImageView();
			
			break;

		case R.id.element_edittext:
			xmlView = builder.buildEditText();
			
			break;

		case R.id.element_radiogroup:
			xmlView = builder.buildRadioButtons();
			
			break;

		case R.id.element_switch:
			xmlView = builder.buildSwitch();

			
			break;

		case R.id.element_checkbox:
			xmlView = builder.buildCheckBox();

			
			break;

		case R.id.element_list:
			xmlView = builder.buildListView();
			
			break;

		case R.id.element_numberpick:
			xmlView = builder.buildNumberPicker();

			
			break;

		case R.id.element_ratingbar:
			xmlView = builder.buildRatingBar();

			
			break;

		case R.id.element_seekbar:
			xmlView = builder.buildSeekBar();

			
			break;

		case R.id.element_timepicker:
			xmlView = builder.buildTimePicker();

			
			break;
/*
		case R.id.element_container:
			xmlView = buildRelativeContainer();
			
			break;
			*/
		case R.id.element_grid:
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
		
		xmlView.measure(valueBundle.getInt(ObjectValues.MINWIDTH), valueBundle.getInt(ObjectValues.MINHEIGHT));
		 
		return xmlView;
	}

	
}
