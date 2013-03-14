package helpers;

import manipulators.Overlay;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import creators.Generator;
import de.ur.rk.uibuilder.R;

public class DisplayModeChanger
{
	public static final int CREATION = 0;
	public static final int PRESENTATION = 1;

	public static void setPresentationMode(View layout)
	{
		
		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				setPresentationMode(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout || layout instanceof ImageView)
		{
			Bundle tagBundle = (Bundle) layout.getTag();

			int id = tagBundle.getInt(Generator.ID);
			int style = tagBundle.getInt(Generator.CREATION_STYLE);
			
			Log.d("presentationMode", "startin switch with " + id);
			switch (id)

			{
			case R.id.element_button:
				if(style == R.drawable.object_background_default)
				{
					layout.setBackgroundResource(R.drawable.presentation_button_default);
				}
				break;

			

			case R.id.element_edittext:
				Log.d("DisplayModechanger", "Case edittext");
				layout.setBackgroundResource(R.drawable.presentation_border_medium);
				break;

			
				
			case R.id.element_container:
				layout.setBackgroundResource(R.drawable.presentation_border_light);
				break;
				
				
			default:
				layout.setBackgroundResource(R.drawable.presentation_default_object);
				break;
			}
		}
	}

	public static void setCreationMode(View layout)
	{
		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				setCreationMode(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout || layout instanceof ImageView)
		{
			
			layout.setBackgroundResource(R.drawable.object_background_default);
			
//			Bundle tagBundle = (Bundle) layout.getTag();
//
//			int id = tagBundle.getInt(Generator.ID);
//			switch (id)
//
//			{
//			case R.id.element_imageview:
//
//			case R.id.element_button:
//
//			case R.id.element_checkbox:
//
//			case R.id.element_edittext:
//
//			case R.id.element_numberpick:
//
//			case R.id.element_radiogroup:
//
//			case R.id.element_ratingbar:
//
//			case R.id.element_switch:
//
//			case R.id.element_textview:
//
//			case R.id.element_timepicker:
//
//			case R.id.element_seekbar:
//
//			case R.id.element_list:
//
//			default:
//				layout.setBackgroundResource(R.drawable.default_object_border);
//				break;
//			}
		}

	}

}
