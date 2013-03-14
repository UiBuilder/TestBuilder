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
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			Bundle tagBundle = (Bundle) layout.getTag();

			int id = tagBundle.getInt(Generator.ID);
			int presentationStyle = tagBundle.getInt(Generator.PRESENTATION_STYLE);

			Log.d("presentationMode", "startin switch with " + id);

			layout.setBackgroundResource(presentationStyle);

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
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			Bundle tagBundle = (Bundle) layout.getTag();

			int creationStyle = tagBundle.getInt(Generator.CREATION_STYLE);
			layout.setBackgroundResource(creationStyle);
		}

	}

}
