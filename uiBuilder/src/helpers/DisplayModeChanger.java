package helpers;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import creators.Generator;

public class DisplayModeChanger
{
	public static void setDisplayMode(View layout, String displayStyle)
	{

		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				setDisplayMode(((ViewGroup) layout).getChildAt(i), displayStyle);

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			Bundle tagBundle = (Bundle) layout.getTag();

			int id = tagBundle.getInt(Generator.ID);
			int style = tagBundle.getInt(displayStyle);


			layout.setBackgroundResource(style);

		}
	}



}
