package helpers;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.ObjectValues;

public class ChildGrabber
{
	private ArrayList<View> childrenList;
	private int order = 0;
	
	public ArrayList<View> getChildren(View layout)
	{
		childrenList = new ArrayList<View>();
		
		recursiveWalkThrough(layout);
		Log.d("Childgrabber", "about to return arrayList with size "+String.valueOf(childrenList.size()));

		order = 0;
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
				Log.d("recursive walk", "called");
			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{
			Bundle tag = (Bundle) layout.getTag();
			tag.putInt(ObjectValues.ZORDER, order++);
			
			childrenList.add(layout);
			
			//THIS IS FOR LOGGING ONLY!
			Bundle bundle = (Bundle) layout.getTag();
			Log.d("Childgrabber", "added to ArrayList: "+ bundle.getInt(ObjectValues.TYPE));
			
			

		}
		
	}

}
