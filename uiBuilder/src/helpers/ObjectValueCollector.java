package helpers;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import creators.Generator;
import de.ur.rk.uibuilder.R;

public class ObjectValueCollector
{

	public static String ID = "id", X_POS = "xPos", Y_POS = "yPos",
			WIDTH = "width", HEIGHT = "height", USER_TEXT = "userText",
			RATING = "rating", CONTENT = "content", COLUMNS_NUM = "columnsNum",
			LAYOUT = "layout", STARS_NUM = "starsNum", ALIGNMENT = "alignment",
			FONTSIZE = "fontSize", IMG_SRC = "imageSource", ICN_SRC = "iconSource",
			BACKGROUND_COLOR = "backgroundColor";


	public static ContentValues getValuePack(View object)
	{
		Bundle objectBundle = (Bundle)object.getTag();
		ContentValues valuesBundle = new ContentValues();
		
		int id = object.getId();
		int tagId = objectBundle.getInt(Generator.ID);
		int xPos = (int) object.getX();
		int yPos = (int) object.getY();
		int width = object.getMeasuredWidth();
		int height = object.getMeasuredHeight();
		
		valuesBundle.put(ID, tagId);
		
		valuesBundle.put(X_POS, xPos);
		valuesBundle.put(Y_POS, yPos);
		valuesBundle.put(WIDTH, width);
		valuesBundle.put(HEIGHT, height);
		
		
		switch(tagId)
		{
		case R.id.element_button:
			valuesBundle.put(USER_TEXT, (String) ((Button) object).getText());
			valuesBundle.put(FONTSIZE, (int) ((Button)object).getTextSize());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			break;
			
		case R.id.element_checkbox:
		case R.id.element_radiogroup:
		case R.id.element_switch:
			TextView textView = (TextView) ((LinearLayout) object).getChildAt(0);
			valuesBundle.put(USER_TEXT, (String) textView.getText());
			break;
			
		case R.id.element_edittext:
			valuesBundle.put(USER_TEXT, ((String)((TextView)object).getText()));
			valuesBundle.put(ALIGNMENT, ((TextView)object).getGravity());
			valuesBundle.put(FONTSIZE, (int) ((TextView)object).getTextSize());
			break;
			
		case R.id.element_grid:
			valuesBundle.put(COLUMNS_NUM, ((GridView)object).getNumColumns());
			valuesBundle.put(CONTENT, objectBundle.getInt(Generator.EXAMPLE_CONTENT));
			valuesBundle.put(LAYOUT, objectBundle.getInt(Generator.EXAMPLE_LAYOUT));
			break;
			
		case R.id.element_imageview:
			if(objectBundle.getInt(Generator.ICN_SRC) == 0)
			{
				valuesBundle.put(IMG_SRC, objectBundle.getString(Generator.IMG_SRC));
			}else
			{
				valuesBundle.put(ICN_SRC, objectBundle.getInt(Generator.ICN_SRC));
			}
			
			break;
			
		case R.id.element_list:
			valuesBundle.put(CONTENT, objectBundle.getInt(Generator.EXAMPLE_CONTENT));
			valuesBundle.put(LAYOUT, objectBundle.getInt(Generator.EXAMPLE_LAYOUT));
			break;
			
		case R.id.element_numberpick:
			break;
			
		case R.id.element_ratingbar:
			valuesBundle.put(STARS_NUM, ((RatingBar) ((ViewGroup) object).getChildAt(0)).getNumStars());
			valuesBundle.put(RATING, (int)((RatingBar) ((ViewGroup) object).getChildAt(0)).getRating());		
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			break;
			
		case R.id.element_seekbar:
			break;
			
		case R.id.element_textview:
			valuesBundle.put(USER_TEXT, ((String)((TextView)object).getText()));
			valuesBundle.put(ALIGNMENT, ((TextView)object).getGravity());
			valuesBundle.put(FONTSIZE, (int) ((TextView)object).getTextSize());
			valuesBundle.put(BACKGROUND_COLOR, objectBundle.getInt(Generator.CREATION_STYLE));
			break;
			
		case R.id.element_timepicker:
			break;
		}
		
		return valuesBundle;
		
	}
}