package helpers;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

		if (layout instanceof ViewGroup && layout.getTag() == null) {
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++) {
				setPresentationMode(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout || layout instanceof RelativeLayout) {
			Bundle tagBundle = (Bundle) layout.getTag();

			int id = tagBundle.getInt(Generator.ID);
			switch (id)

			{
			case R.id.element_button:
				layout.setBackgroundResource(android.R.drawable.btn_default);

				break;

			case R.id.element_checkbox:

				layout.setBackgroundResource(android.R.drawable.checkbox_off_background);

				break;

			case R.id.element_edittext:

				layout.setBackgroundResource(android.R.drawable.edit_text);

				break;

			case R.id.element_imageview:

				break;

			case R.id.element_numberpick:
				layout.setBackgroundResource(android.R.drawable.spinner_background);

				break;
			case R.id.element_radiogroup:

				layout.setBackgroundResource(android.R.drawable.radiobutton_off_background);

				break;
			case R.id.element_ratingbar:
				layout.setBackgroundResource(android.R.drawable.menuitem_background);

				break;
			// case R.id.element_search:
			// moduleNothing.setVisibility(View.VISIBLE);
			// // moduleSearch.setVisibility(View.VISIBLE); collapsed etc
			// break;
			case R.id.element_switch:

				layout.setBackgroundResource(android.R.drawable.menuitem_background);

				break;
			case R.id.element_textview:
				layout.setBackgroundResource(android.R.drawable.menuitem_background);

				break;
			case R.id.element_timepicker:
				layout.setBackgroundResource(android.R.drawable.menuitem_background);

				break;

			case R.id.element_seekbar:
				layout.setBackgroundResource(android.R.drawable.menuitem_background);

				break;

			case R.id.element_list:
				layout.setBackgroundResource(android.R.drawable.menuitem_background);

			default:

				break;
			}
		}
	}

}
