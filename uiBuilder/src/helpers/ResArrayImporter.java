package helpers;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;

import de.ur.rk.uibuilder.R;



public class ResArrayImporter
{
	
	/**
	 * Solution from: http://www.anddev.org/xml_integer_array_resource_references_getintarray-t9268.html
	 * @param c
	 * @param id
	 * @return
	 */
	public static int[] getRefArray(Context c, int id)
	{
	    TypedArray ar = c.getResources().obtainTypedArray(id);
		int len = ar.length();
		
		int[] resIds = new int[len];
		 
		for (int i = 0; i < len; i++)
		 
		    resIds[i] = ar.getResourceId(i, 0);
		 
		ar.recycle();
		return resIds;
	}
}
