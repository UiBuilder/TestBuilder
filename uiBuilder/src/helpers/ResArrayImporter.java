package helpers;

import android.content.Context;
import android.content.res.TypedArray;



public class ResArrayImporter
{
	
	/**
	 * @author funklos
	 * For better maintenance the icon resources should be referenced via an XML integer array.
	 * The problem:
	 * It is not possible to declare an array of resource references in XML, if you try to, then all
	 * entries are '0'.
	 * This is the only workaround which really works.
	 * Solution from: http://www.anddev.org/xml_integer_array_resource_references_getintarray-t9268.html
	 * @param c context to obtain resources
	 * @param id the resource array to load
	 * @return an array containing the references
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
