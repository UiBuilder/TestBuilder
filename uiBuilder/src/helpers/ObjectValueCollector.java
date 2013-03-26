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
import data.DataBase;
import de.ur.rk.uibuilder.R;

public class ObjectValueCollector
{

	public static final String

	TYPE = "id", X_POS = "xPos", Y_POS = "yPos", WIDTH = "width",
			HEIGHT = "height",

			USER_TEXT = "userText", RATING = "rating", CONTENT = "content",
			COLUMNS_NUM = "columnsNum", LAYOUT = "layout",
			STARS_NUM = "starsNum", ALIGNMENT = "alignment",
			FONTSIZE = "fontSize", IMG_SRC = "imageSource",
			ICN_SRC = "iconSource", BACKGROUND_COLOR = "backgroundColor", BACKGROUND_PRES = "presentationModeBackground";

	public static ContentValues getValuePack(View object)
	{
		Bundle objectBundle = (Bundle) object.getTag();
		ContentValues valuesBundle = new ContentValues();

		int id = objectBundle.getInt(Generator.ID);
		int tagId = objectBundle.getInt(Generator.TYPE);
		int xPos = (int) object.getX();
		int yPos = (int) object.getY();
		int width = object.getMeasuredWidth();
		int height = object.getMeasuredHeight();

		valuesBundle.put(DataBase.KEY_ID, id);
		valuesBundle.put(TYPE, tagId);
		valuesBundle.put(X_POS, xPos);
		valuesBundle.put(Y_POS, yPos);
		valuesBundle.put(WIDTH, width);
		valuesBundle.put(HEIGHT, height);
		Log.d("ObjectValueCollector", "values put.");

		switch (tagId)
		{
		case R.id.element_button:
			valuesBundle.put(USER_TEXT, (String) ((Button) object).getText());
			valuesBundle.put(FONTSIZE, (int) ((Button) object).getTextSize());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			Log.d("ObjectValueCollector", "put Button, with: "
					+ (String) ((Button) object).getText() + " and Size:"
					+ (int) ((Button) object).getTextSize());

			break;

		case R.id.element_checkbox:

			TextView textView = (TextView) ((LinearLayout) object).getChildAt(0);
			valuesBundle.put(USER_TEXT, (String) textView.getText());
			Log.d("ObjectValueCollector", "put checkbox, with: "
					+ (String) textView.getText());
			break;

		case R.id.element_radiogroup:
			valuesBundle.put(USER_TEXT, ((RadioButton) object).getText().toString());
			Log.d("ObjectValueCollector", "put radiogroup, with: ");
			break;

		case R.id.element_switch:
			valuesBundle.put(USER_TEXT, ((Switch) object).getText().toString());
			Log.d("ObjectValueCollector", "put Switch, with: ");
			break;

		case R.id.element_edittext:
			valuesBundle.put(USER_TEXT, (((EditText) object).getText()).toString());
			valuesBundle.put(ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			Log.d("ObjectValueCollector", "put edittext, with: ");
			break;

		case R.id.element_grid:
			valuesBundle.put(COLUMNS_NUM, ((GridView) ((RelativeLayout) object).getChildAt(0)).getNumColumns());
			valuesBundle.put(CONTENT, objectBundle.getInt(Generator.EXAMPLE_CONTENT));
			valuesBundle.put(LAYOUT, objectBundle.getInt(Generator.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put Grid, with: ");
			break;

		case R.id.element_imageview:
			if (objectBundle.getInt(Generator.ICN_SRC) == 0)
			{
				valuesBundle.put(IMG_SRC, objectBundle.getString(Generator.IMG_SRC));
			} else
			{
				valuesBundle.put(ICN_SRC, objectBundle.getInt(Generator.ICN_SRC));
			}
			Log.d("ObjectValueCollector", "put Imageview, with: ");
			break;

		case R.id.element_list:
			valuesBundle.put(CONTENT, objectBundle.getInt(Generator.EXAMPLE_CONTENT));
			valuesBundle.put(LAYOUT, objectBundle.getInt(Generator.EXAMPLE_LAYOUT));
			Log.d("ObjectValueCollector", "put List, with: ");
			break;

		case R.id.element_numberpick:
			Log.d("ObjectValueCollector", "put Numberpicker, with: ");
			break;

		case R.id.element_ratingbar:
			valuesBundle.put(STARS_NUM, ((RatingBar) ((ViewGroup) object).getChildAt(0)).getNumStars());
			valuesBundle.put(RATING, (int) ((RatingBar) ((ViewGroup) object).getChildAt(0)).getRating());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			Log.d("ObjectValueCollector", "put ratingbar, with: ");
			break;

		case R.id.element_seekbar:
			Log.d("ObjectValueCollector", "put Seekbar, with: ");

			break;

		case R.id.element_textview:
			valuesBundle.put(USER_TEXT, ((String) ((TextView) object).getText()));
			valuesBundle.put(ALIGNMENT, ((TextView) object).getGravity());
			valuesBundle.put(FONTSIZE, (int) ((TextView) object).getTextSize());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			Log.d("ObjectValueCollector", "put Textview, with: ");
			break;

		case R.id.element_timepicker:
			Log.d("ObjectValueCollector", "put TimePicker, with: ");
			break;
		}
		valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));

		return valuesBundle;

	}
}
