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

/**
 * This class collects all childelements from a given view and adds them to an ArrayList containing the Views.
 * 
 * 
 * @author jonesses
 *
 */

public class ChildGrabber
{
	private ArrayList<View> childrenList;
	private int order = 0;
	
	/**
	 * Walks through the View passed to it and packs all the child elements of that view in an ArrayList.
	 * It also adds the Z-Order field to the view's tag-Bundle in order to save it to the database.
	 * @param layout is the View you want to have the children from
	 * @return ArrayList containing all the child Views collected.
	 */
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
