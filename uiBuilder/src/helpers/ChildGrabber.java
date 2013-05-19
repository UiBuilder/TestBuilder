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
import de.ur.rk.uibuilder.R;

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
	
	public static final int 
			MODE_RECURSIVE = 0X00,
			MODE_FLAT = 0X01;
			;
	/**
	 * Walks through the View passed to it and packs all the child elements of that view in an ArrayList.
	 * It also adds the Z-Order field to the view's tag-Bundle in order to save it to the database.
	 * @param layout is the View you want to have the children from
	 * @return ArrayList containing all the child Views collected.
	 */
	public ArrayList<View> getChildren(View layout, int mode)
	{
		childrenList = new ArrayList<View>();
		
		recursiveWalkThrough(layout, mode);
		Log.d("Childgrabber", "about to return arrayList with size " + String.valueOf(childrenList.size()));

		order = 0;
		return childrenList;
	}
	
	private void recursiveWalkThrough(View layout, int mode)
	{

		//if (layout.getId() == R.id.design_area)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				View item = ((ViewGroup) layout).getChildAt(i);
				Bundle tag = (Bundle) item.getTag();
				tag.putInt(ObjectValues.ZORDER, order++);
							
				childrenList.add(item);
				
				/*if (mode == MODE_RECURSIVE)
				{
					recursiveWalkThrough(((ViewGroup) layout).getChildAt(i), MODE_RECURSIVE);
				}
				else
					recursiveWalkThrough(((ViewGroup) layout).getChildAt(i), MODE_FLAT);
				Log.d("recursive walk", "called");*/
			}
		} 
		/*else //if (layout instanceof TextView || layout instanceof LinearLayout || layout instanceof RelativeLayout || layout instanceof ImageView)
		{
			Bundle tag = (Bundle) layout.getTag();
			tag.putInt(ObjectValues.ZORDER, order++);
						
			childrenList.add(layout);

			Log.d("Childgrabber", "added to ArrayList: "+ tag.getInt(ObjectValues.TYPE));
		}*/
		
	}

}
