package data;

import helpers.Log;
import manipulators.Overlay;
import android.content.ContentValues;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import creators.ObjectFactory;
import creators.ObjectIdMapper;
import de.ur.rk.uibuilder.R;

/**
 * Defines static access methods which return object properties in a structured way.
 * GgetValuePack is used to collect all the data from the supplied items tag to be inserted in the database.
 * getDefaultValueBundle returns a default bundle object which is requested when a new view is generated.
 * @author funklos
 *
 */
public class Bundler
{

	/**
	 * Collects the items properties from the tagbundle
	 * and
	 * transfers them to a new contentValues object which will be inserted into the database.
	 * @author jonesses
	 * @param object the source view
	 * @return contentValues which can be directly inserted into the database.
	 */
	public static ContentValues getValuePack(View object)
	{
		Bundle objectBundle = (Bundle) object.getTag();
		ContentValues valuesBundle = new ContentValues();

		int id = objectBundle.getInt(ObjectValues.DATABASE_ID);
		int tagId = objectBundle.getInt(ObjectValues.TYPE);
		int xPos = (int) object.getX();
		int yPos = (int) object.getY();
		int width = object.getMeasuredWidth();
		int height = object.getMeasuredHeight();
		
		valuesBundle.put(ObjectValues.ZORDER, objectBundle.getInt(ObjectValues.ZORDER));
		valuesBundle.put(ObjectValues.DATABASE_ID, id);
		valuesBundle.put(ObjectValues.TYPE, tagId);
		valuesBundle.put(ObjectValues.X_POS, xPos);
		valuesBundle.put(ObjectValues.Y_POS, yPos);
		valuesBundle.put(ObjectValues.WIDTH, width);
		valuesBundle.put(ObjectValues.HEIGHT, height);
		Log.d("ObjectValueCollector", "values put.");

		switch (tagId)
		{
		case ObjectIdMapper.OBJECT_ID_BUTTON:
			valuesBundle.put(ObjectValues.USER_TEXT, (String) ((Button) object).getText());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((Button) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			valuesBundle.put(ObjectValues.ALIGNMENT, ((TextView) object).getGravity());

			Log.d("ObjectValueCollector", "put Button, with: "
					+ (String) ((Button) object).getText() + " and Size:"
					+ (int) ((Button) object).getTextSize());

			break;

		case ObjectIdMapper.OBJECT_ID_CHECKBOX:

			TextView textView = (TextView) ((LinearLayout) object).getChildAt(0);
			valuesBundle.put(ObjectValues.USER_TEXT, (String) textView.getText());
			Log.d("ObjectValueCollector", "put checkbox"
					+ (String) textView.getText());
			break;

		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:
			valuesBundle.put(ObjectValues.USER_TEXT, ((RadioButton) object).getText().toString());
			Log.d("ObjectValueCollector", "put radiogroup");
			break;

		case ObjectIdMapper.OBJECT_ID_SWITCH:
			valuesBundle.put(ObjectValues.USER_TEXT, ((Switch) object).getText().toString());
			Log.d("ObjectValueCollector", "put Switch");
			break;

		case ObjectIdMapper.OBJECT_ID_EDITTEXT:
			valuesBundle.put(ObjectValues.USER_TEXT, (((EditText) object).getHint()).toString());
			valuesBundle.put(ObjectValues.ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put edittext");
			break;

		case ObjectIdMapper.OBJECT_ID_GRIDVIEW:
			valuesBundle.put(ObjectValues.COLUMNS_NUM, ((GridView) ((RelativeLayout) object).getChildAt(0)).getNumColumns());
			valuesBundle.put(ObjectValues.EXAMPLE_CONTENT, objectBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			valuesBundle.put(ObjectValues.EXAMPLE_LAYOUT, objectBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put Grid");
			break;

		case ObjectIdMapper.OBJECT_ID_IMAGEVIEW:
			valuesBundle.put(ObjectValues.IMG_SRC, objectBundle.getString(ObjectValues.IMG_SRC));
			valuesBundle.put(ObjectValues.ICN_SRC, objectBundle.getInt(ObjectValues.ICN_SRC));


			Log.d("ObjectValueCollector", "put Imageview"+ objectBundle.getString(ObjectValues.IMG_SRC));
			break;

		case ObjectIdMapper.OBJECT_ID_LISTVIEW:
			valuesBundle.put(ObjectValues.EXAMPLE_CONTENT, objectBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			valuesBundle.put(ObjectValues.EXAMPLE_LAYOUT, objectBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put List, with: ");
			break;

		case ObjectIdMapper.OBJECT_ID_NUMBERPICKER:
			Log.d("ObjectValueCollector", "put Numberpicker");
			break;

		case ObjectIdMapper.OBJECT_ID_RATINGBAR:
			valuesBundle.put(ObjectValues.STARS_NUM, objectBundle.getInt(ObjectValues.STARS_NUM));
			valuesBundle.put(ObjectValues.RATING, objectBundle.getInt(ObjectValues.RATING));
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put ratingbar, with: ");
			break;

		case ObjectIdMapper.OBJECT_ID_SEEKBAR:
			Log.d("ObjectValueCollector", "put Seekbar ");

			break;

		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:
			valuesBundle.put(ObjectValues.USER_TEXT, ((String) ((TextView) object).getText()));
			valuesBundle.put(ObjectValues.ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put Textview");
			break;

		case ObjectIdMapper.OBJECT_ID_TIMEPICKER:
			Log.d("ObjectValueCollector", "put TimePicker ");
			break;
		}
		
		valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
		valuesBundle.put(ObjectValues.BACKGROUND_PRES,  objectBundle.getInt(ObjectValues.BACKGROUND_PRES));

		return valuesBundle;

	}
	
	/**
	 * Creates a default object bundle of the given type.
	 * Puts the default object properties of each type into the bundle.
	 * These will be overridden when the user interacts with the object.
	 * 
	 * We had to use the approach with the object tag bundle, because we need to track more properties as
	 * the views can supply via getter methods.
	 * 
	 * @param which the type of the requesting object.
	 * @param res the resources to fetch references from
	 * @return a bundle representing the default object properties
	 */
	public static Bundle getDefaultValueBundle(int which, Resources res)
	{
		Log.d("getValueBundle", "called");
		
		Bundle tagBundle = new Bundle();
		int minWidth = 0;
		int minHeight = 0;
		int defWidth = 0;
		int defHeight = 0;
		int scaleType = 0;
		int presMode = R.drawable.presentation_default_object;
		int createMode = R.drawable.object_background_default;	

		switch (which)
		{
		case ObjectIdMapper.OBJECT_ID_SPINNER:
			
			minWidth = res.getInteger(R.integer.button_factor_width);
			minHeight = res.getInteger(R.integer.button_factor_height);
			defWidth = res.getInteger(R.integer.button_factor_default_width);
			defHeight = res.getInteger(R.integer.button_factor_default_height);
			scaleType = Overlay.HORIZONTAL;
			createMode = R.drawable.object_background_default_button;
			presMode = R.drawable.presentation_button_default;
			break;
		
		case ObjectIdMapper.OBJECT_ID_BUTTON:

			minWidth = res.getInteger(R.integer.button_factor_width);
			minHeight = res.getInteger(R.integer.button_factor_height);
			defWidth = res.getInteger(R.integer.button_factor_default_width);
			defHeight = res.getInteger(R.integer.button_factor_default_height);
			scaleType = Overlay.BOTH;
			createMode = R.drawable.object_background_default_button;
			presMode = R.drawable.presentation_button_default;
			break;

		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:
			
			minWidth = res.getInteger(R.integer.textview_factor_width);
			minHeight = res.getInteger(R.integer.textview_factor_height);
			defWidth = res.getInteger(R.integer.textview_factor_default_width);
			defHeight = res.getInteger(R.integer.textview_factor_default_height);
			scaleType = Overlay.BOTH;
			break;

		case ObjectIdMapper.OBJECT_ID_IMAGEVIEW:
			
			minWidth = res.getInteger(R.integer.image_factor_width);
			minHeight = res.getInteger(R.integer.image_factor_height);
			defWidth = res.getInteger(R.integer.image_factor_default_width);
			defHeight = res.getInteger(R.integer.image_factor_default_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.IMG_SRC, 0);
			tagBundle.putInt(ObjectValues.ICN_SRC, 0);
			break;

		case ObjectIdMapper.OBJECT_ID_EDITTEXT:

			minWidth = res.getInteger(R.integer.edittext_factor_width);
			minHeight = res.getInteger(R.integer.edittext_factor_height);
			defWidth = res.getInteger(R.integer.edittext_factor_default_width);
			defHeight = res.getInteger(R.integer.edittext_factor_default_height);
			scaleType = Overlay.BOTH;
			createMode = R.drawable.object_background_default_edittext;
			presMode = R.drawable.presentation_border_medium;
			break;

		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:

			minWidth = res.getInteger(R.integer.radio_factor_width);
			minHeight = res.getInteger(R.integer.radio_factor_height);
			defWidth = res.getInteger(R.integer.radio_factor_default_width);
			defHeight = res.getInteger(R.integer.radio_factor_default_height);
			scaleType = Overlay.BOTH;
			break;

		case ObjectIdMapper.OBJECT_ID_SWITCH:
			
			minWidth = res.getInteger(R.integer.switch_factor_width);
			minHeight = res.getInteger(R.integer.switch_factor_height);
			defWidth = res.getInteger(R.integer.switch_factor_default_width);
			defHeight = res.getInteger(R.integer.switch_factor_default_height);
			scaleType = Overlay.BOTH;
			break;

		case ObjectIdMapper.OBJECT_ID_CHECKBOX:
			
			minWidth = res.getInteger(R.integer.checkbox_factor_width);
			minHeight = res.getInteger(R.integer.checkbox_factor_height);
			defWidth = res.getInteger(R.integer.checkbox_factor_default_width);
			defHeight = res.getInteger(R.integer.checkbox_factor_default_height);
			scaleType = Overlay.BOTH;
			break;

		case ObjectIdMapper.OBJECT_ID_LISTVIEW:
			
			minWidth = res.getInteger(R.integer.list_factor_width);
			minHeight = res.getInteger(R.integer.list_factor_height);
			defWidth = res.getInteger(R.integer.list_factor_default_width);
			defHeight = res.getInteger(R.integer.list_factor_default_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_hipster);
			tagBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, R.layout.item_listview_example_layout_1);
			break;

		case ObjectIdMapper.OBJECT_ID_NUMBERPICKER:
			
			minWidth = res.getInteger(R.integer.numberpicker_factor_width);
			minHeight = res.getInteger(R.integer.numberpicker_factor_height);
			defWidth = res.getInteger(R.integer.numberpicker_factor_default_width);
			defHeight = res.getInteger(R.integer.numberpicker_factor_default_height);
			scaleType = Overlay.VERTICAL;
			break;

		case ObjectIdMapper.OBJECT_ID_RATINGBAR:
			
			minWidth = res.getInteger(R.integer.ratingbar_factor_width);
			minHeight = res.getInteger(R.integer.ratingbar_factor_height);
			defWidth = res.getInteger(R.integer.ratingbar_factor_default_width);
			defHeight = res.getInteger(R.integer.ratingbar_factor_default_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.STARS_NUM, 5);
			tagBundle.putInt(ObjectValues.RATING, 4);
			break;

		case ObjectIdMapper.OBJECT_ID_SEEKBAR:
			
			minWidth = res.getInteger(R.integer.seekbar_factor_width);
			minHeight = res.getInteger(R.integer.seekbar_factor_height);
			defWidth = res.getInteger(R.integer.seekbar_factor_default_width);
			defHeight = res.getInteger(R.integer.seekbar_factor_default_height);
			scaleType = Overlay.HORIZONTAL;
			break;

		case ObjectIdMapper.OBJECT_ID_TIMEPICKER:
			
			minWidth = res.getInteger(R.integer.timepicker_factor_width);
			minHeight = res.getInteger(R.integer.timepicker_factor_height);
			defWidth = res.getInteger(R.integer.timepicker_factor_default_width);
			defHeight = res.getInteger(R.integer.timepicker_factor_default_height);
			scaleType = Overlay.VERTICAL;
			break;
/*
		case R.id.element_container:
			
			width = res.getInteger(R.integer.edittext_factor_width);
			height = res.getInteger(R.integer.edittext_factor_height);
			scaleType = Overlay.BOTH;
			presMode = R.drawable.presentation_border_light;

			break;
			*/
		case ObjectIdMapper.OBJECT_ID_GRIDVIEW:
			
			minWidth = res.getInteger(R.integer.grid_factor_width);
			minHeight = res.getInteger(R.integer.grid_factor_height);
			defWidth = res.getInteger(R.integer.grid_factor_default_width);
			defHeight = res.getInteger(R.integer.grid_factor_default_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_bacon);
			tagBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, R.layout.item_gridview_example_layout_3);
			tagBundle.putInt(ObjectValues.COLUMNS_NUM, R.integer.default_columns_count);
			break;
			
		default:
			Log.d("bundle ", "not built");
		}
		
		minWidth *= ObjectFactory.SNAP_GRID_INTERVAL;
		minHeight *= ObjectFactory.SNAP_GRID_INTERVAL;
		defWidth *= ObjectFactory.SNAP_GRID_INTERVAL;
		defHeight *= ObjectFactory.SNAP_GRID_INTERVAL;
		
		tagBundle.putInt(ObjectValues.FONTSIZE, 16);
		tagBundle.putInt(ObjectValues.BACKGROUND_PRES, presMode);
		tagBundle.putInt(ObjectValues.BACKGROUND_EDIT, createMode);
		tagBundle.putInt(ObjectValues.SCALETYPE, scaleType);
		tagBundle.putInt(ObjectValues.MINHEIGHT, minHeight);
		tagBundle.putInt(ObjectValues.MINWIDTH, minWidth);
		tagBundle.putInt(ObjectValues.DEFAULT_WIDTH, defWidth);
		tagBundle.putInt(ObjectValues.DEFAULT_HEIGHT, defHeight);
		tagBundle.putInt(ObjectValues.TYPE, which);
		tagBundle.putInt(ObjectValues.DATABASE_ID, 0);
		

		return tagBundle;
	}
}
