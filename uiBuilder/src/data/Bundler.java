package data;

import creators.ObjectFactory;
import manipulators.Overlay;
import helpers.Log;
import android.content.ContentValues;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class Bundler
{


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
		
		
		
		

		valuesBundle.put(ObjectValues.DATABASE_ID, id);
		valuesBundle.put(ObjectValues.TYPE, tagId);
		valuesBundle.put(ObjectValues.X_POS, xPos);
		valuesBundle.put(ObjectValues.Y_POS, yPos);
		valuesBundle.put(ObjectValues.WIDTH, width);
		valuesBundle.put(ObjectValues.HEIGHT, height);
		Log.d("ObjectValueCollector", "values put.");

		switch (tagId)
		{
		case R.id.element_button:
			valuesBundle.put(ObjectValues.USER_TEXT, (String) ((Button) object).getText());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((Button) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put Button, with: "
					+ (String) ((Button) object).getText() + " and Size:"
					+ (int) ((Button) object).getTextSize());

			break;

		case R.id.element_checkbox:

			TextView textView = (TextView) ((LinearLayout) object).getChildAt(0);
			valuesBundle.put(ObjectValues.USER_TEXT, (String) textView.getText());
			Log.d("ObjectValueCollector", "put checkbox, with: "
					+ (String) textView.getText());
			break;

		case R.id.element_radiogroup:
			valuesBundle.put(ObjectValues.USER_TEXT, ((RadioButton) object).getText().toString());
			Log.d("ObjectValueCollector", "put radiogroup, with: ");
			break;

		case R.id.element_switch:
			valuesBundle.put(ObjectValues.USER_TEXT, ((Switch) object).getText().toString());
			Log.d("ObjectValueCollector", "put Switch, with: ");
			break;

		case R.id.element_edittext:
			valuesBundle.put(ObjectValues.USER_TEXT, (((EditText) object).getHint()).toString());
			valuesBundle.put(ObjectValues.ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put edittext, with: ");
			break;

		case R.id.element_grid:
			valuesBundle.put(ObjectValues.COLUMNS_NUM, ((GridView) ((RelativeLayout) object).getChildAt(0)).getNumColumns());
			valuesBundle.put(ObjectValues.EXAMPLE_CONTENT, objectBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			valuesBundle.put(ObjectValues.EXAMPLE_LAYOUT, objectBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put Grid, with: ");
			break;

		case R.id.element_imageview:
			valuesBundle.put(ObjectValues.IMG_SRC, objectBundle.getString(ObjectValues.IMG_SRC));
			valuesBundle.put(ObjectValues.ICN_SRC, objectBundle.getInt(ObjectValues.ICN_SRC));


			Log.d("ObjectValueCollector", "put Imageview, with:"+ objectBundle.getString(ObjectValues.IMG_SRC));
			break;

		case R.id.element_list:
			valuesBundle.put(ObjectValues.EXAMPLE_CONTENT, objectBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			valuesBundle.put(ObjectValues.EXAMPLE_LAYOUT, objectBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put List, with: ");
			break;

		case R.id.element_numberpick:
			Log.d("ObjectValueCollector", "put Numberpicker, with: ");
			break;

		case R.id.element_ratingbar:
			valuesBundle.put(ObjectValues.STARS_NUM, objectBundle.getInt(ObjectValues.STARS_NUM));
			valuesBundle.put(ObjectValues.RATING, objectBundle.getInt(ObjectValues.RATING));
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put ratingbar, with: ");
			break;

		case R.id.element_seekbar:
			Log.d("ObjectValueCollector", "put Seekbar, with: ");

			break;

		case R.id.element_textview:
			valuesBundle.put(ObjectValues.USER_TEXT, ((String) ((TextView) object).getText()));
			valuesBundle.put(ObjectValues.ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(ObjectValues.FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			Log.d("ObjectValueCollector", "put Textview, with: ");
			break;

		case R.id.element_timepicker:
			Log.d("ObjectValueCollector", "put TimePicker, with: ");
			break;
		}
		valuesBundle.put(ObjectValues.BACKGROUND_EDIT, objectBundle.getInt(ObjectValues.BACKGROUND_EDIT));
		valuesBundle.put(ObjectValues.BACKGROUND_PRES,  objectBundle.getInt(ObjectValues.BACKGROUND_PRES));

		return valuesBundle;

	}
	
	public static Bundle getValueBundle(int which, Resources res)
	{
		Log.d("getValueBundle", "called");
		
		Bundle tagBundle = new Bundle();
		int width = 0;
		int height = 0;
		int scaleType = 0;
		int presMode = R.drawable.presentation_default_object;
		int createMode = R.drawable.object_background_default;

		switch (which)
		{
		case R.id.element_button:

			width = res.getInteger(R.integer.button_factor_width);
			height = res.getInteger(R.integer.button_factor_height);
			scaleType = Overlay.BOTH;
			createMode = R.drawable.object_background_default_button;
			presMode = R.drawable.presentation_button_default;
			
			break;

		case R.id.element_textview:
			
			width = res.getInteger(R.integer.textview_factor_width);
			height = res.getInteger(R.integer.textview_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_imageview:
			
			width = res.getInteger(R.integer.image_factor_width);
			height = res.getInteger(R.integer.image_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.IMG_SRC, 0);
			tagBundle.putInt(ObjectValues.ICN_SRC, 0);
			
			break;

		case R.id.element_edittext:

			width = res.getInteger(R.integer.edittext_factor_width);
			height = res.getInteger(R.integer.edittext_factor_height);
			scaleType = Overlay.BOTH;
			createMode = R.drawable.object_background_default_edittext;
			presMode = R.drawable.presentation_border_medium;
			
			break;

		case R.id.element_radiogroup:

			width = res.getInteger(R.integer.radio_factor_width);
			height = res.getInteger(R.integer.radio_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_switch:
			
			width = res.getInteger(R.integer.switch_factor_width);
			height = res.getInteger(R.integer.switch_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_checkbox:
			
			width = res.getInteger(R.integer.checkbox_factor_width);
			height = res.getInteger(R.integer.checkbox_factor_height);
			scaleType = Overlay.BOTH;

			break;

		case R.id.element_list:
			
			width = res.getInteger(R.integer.list_factor_width);
			height = res.getInteger(R.integer.list_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_hipster);
			tagBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, R.layout.item_listview_example_layout_1);

			break;

		case R.id.element_numberpick:
			
			width = res.getInteger(R.integer.numberpicker_factor_width);
			height = res.getInteger(R.integer.numberpicker_factor_height);
			scaleType = Overlay.VERTICAL;

			break;

		case R.id.element_ratingbar:
			
			width = res.getInteger(R.integer.ratingbar_factor_width);
			height = res.getInteger(R.integer.ratingbar_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.STARS_NUM, 5);
			tagBundle.putInt(ObjectValues.RATING, 4);
			
			break;

		case R.id.element_seekbar:
			
			width = res.getInteger(R.integer.seekbar_factor_width);
			height = res.getInteger(R.integer.seekbar_factor_height);
			scaleType = Overlay.HORIZONTAL;

			break;

		case R.id.element_timepicker:
			
			width = res.getInteger(R.integer.timepicker_factor_width);
			height = res.getInteger(R.integer.timepicker_factor_height);
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
		case R.id.element_grid:
			
			width = res.getInteger(R.integer.grid_factor_width);
			height = res.getInteger(R.integer.grid_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_bacon);
			tagBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, R.layout.item_gridview_example_layout_3);
			
			
			break;
			
		default:
			Log.d("bundle ", "not built");
			throw new NoClassDefFoundError();
		}
		
		width *= ObjectFactory.SNAP_GRID_INTERVAL;
		height *= ObjectFactory.SNAP_GRID_INTERVAL;
		
		tagBundle.putInt(ObjectValues.BACKGROUND_PRES, presMode);
		tagBundle.putInt(ObjectValues.BACKGROUND_EDIT, createMode);
		tagBundle.putInt(ObjectValues.SCALETYPE, scaleType);
		tagBundle.putInt(ObjectValues.MINHEIGHT, height);
		tagBundle.putInt(ObjectValues.MINWIDTH, width);
		tagBundle.putInt(ObjectValues.TYPE, which);
		

		return tagBundle;
	}
}
