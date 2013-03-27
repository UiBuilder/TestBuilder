package helpers;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChildGrabber
{
	private ArrayList<View> childrenList;
	
	public ArrayList<View> getChildren(View layout)
	{
		childrenList = new ArrayList<View>();
		
		recursiveWalkThrough(layout);
		Log.d("Childgrabber", "about to return arrayList ");

		return childrenList;
	}
	
	private void recursiveWalkThrough(View layout)
	{

		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				recursiveWalkThrough(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			
			childrenList.add(layout);
			
			//THIS IS FOR LOGGING ONLY!
			Bundle bundle = (Bundle) layout.getTag();
			Log.d("Childgrabber", "added to ArrayList: "+ bundle.getInt(ObjectValues.TYPE));
			
			

		}
		
	}

}
