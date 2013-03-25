package helpers;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChildGrabber
{
	static ArrayList<View> childrenList = new ArrayList<View>();
	
	public static ArrayList<View> getChildren(View layout)
	{
		setDisplayMode(layout);
		return childrenList;
	}
	
	public static void setDisplayMode(View layout)
	{

		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				setDisplayMode(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			//Bundle tagBundle = (Bundle) layout.getTag();

			//int style = tagBundle.getInt(displayStyle);

			//layout.setBackgroundResource(style);
			childrenList.add(layout);
			
			

		}
		
	}

}
