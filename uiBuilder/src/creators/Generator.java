package creators;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import data.Bundler;
import data.ObjectValues;
import data.SampleAdapter;
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

	private Context context;
	private OnTouchListener manipulator;
	private LayoutInflater inflater;
	private FromXmlBuilder builder;

	private SampleAdapter samples;
	
	private Resources res;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, OnTouchListener mp)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;

		samples = new SampleAdapter(ref);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		Bundle properties = Bundler.getDefaultBundle(id, res);
		params = new RelativeLayout.LayoutParams((int) (properties.getInt(ObjectValues.MINWIDTH)* 2), (int) (properties.getInt(ObjectValues.MINHEIGHT) * 2));

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
			params = new RelativeLayout.LayoutParams((properties.getInt(ObjectValues.MINWIDTH)), (int) (properties.getInt(ObjectValues.MINHEIGHT) * 2));

			
			break;

		case R.id.element_checkbox:
			xmlView = builder.buildCheckBox();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(ObjectValues.MINWIDTH)* 2), (int) (properties.getInt(ObjectValues.MINHEIGHT)));

			
			break;

		case R.id.element_list:
			xmlView = builder.buildListView();
			
			break;

		case R.id.element_numberpick:
			xmlView = builder.buildNumberPicker();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(ObjectValues.MINWIDTH)), (int) (properties.getInt(ObjectValues.MINHEIGHT)));

			
			break;

		case R.id.element_ratingbar:
			xmlView = builder.buildRatingBar();
			params = new RelativeLayout.LayoutParams(properties.getInt(ObjectValues.MINWIDTH) * 6, properties.getInt(ObjectValues.MINHEIGHT));

			
			break;

		case R.id.element_seekbar:
			xmlView = builder.buildSeekBar();
			params = new RelativeLayout.LayoutParams(properties.getInt(ObjectValues.MINWIDTH), properties.getInt(ObjectValues.MINHEIGHT) * 2);

			
			break;

		case R.id.element_timepicker:
			xmlView = builder.buildTimePicker();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(ObjectValues.MINWIDTH)), (int) (properties.getInt(ObjectValues.MINHEIGHT)));

			
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
		
		xmlView.setBackgroundResource(properties.getInt(ObjectValues.BACKGROUND_EDIT));
		xmlView.setId(idCount++);
		xmlView.setTag(properties);
		xmlView.setOnTouchListener(manipulator);
		
		xmlView.measure(properties.getInt(ObjectValues.MINWIDTH), properties.getInt(ObjectValues.MINHEIGHT));
		 
		samples.setSampleAdapter(xmlView);
		return xmlView;
	}

	
}
