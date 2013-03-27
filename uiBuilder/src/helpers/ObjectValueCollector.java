package helpers;

import android.content.ContentValues;
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
import creators.Generator;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ObjectValueCollector
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
			valuesBundle.put(ObjectValues.USER_TEXT, (((EditText) object).getText()).toString());
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
			if (objectBundle.getInt(ObjectValues.ICN_SRC) == 0)
			{
				valuesBundle.put(ObjectValues.IMG_SRC, objectBundle.getString(ObjectValues.IMG_SRC));
			} else
			{
				valuesBundle.put(ObjectValues.ICN_SRC, objectBundle.getInt(ObjectValues.ICN_SRC));
			}
			Log.d("ObjectValueCollector", "put Imageview, with: ");
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
			valuesBundle.put(ObjectValues.STARS_NUM, ((RatingBar) ((ViewGroup) object).getChildAt(0)).getNumStars());
			valuesBundle.put(ObjectValues.RATING, (int) Math.round(((RatingBar) ((ViewGroup) object).getChildAt(0)).getRating()));
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

		return valuesBundle;

	}
}
