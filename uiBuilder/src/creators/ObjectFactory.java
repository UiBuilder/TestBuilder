package creators;

import uibuilder.EditmodeFragment;
import uibuilder.EditmodeFragment.onObjectEditedListener;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;

public class ObjectFactory implements onObjectEditedListener
{

	private Context ref;
	private Generator generator;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * Resources
	 */

	int[] highResIcns;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c
	 *            Referenz auf die Activity
	 */
	public ObjectFactory(Context c, OnTouchListener l)
	{
		ref = c;
		generator = new Generator(ref, l, this);
		
		EditmodeFragment.setOnObjectEditedListener(this);
	}



	/**
	 * 
	 * @param which
	 *            Definiert die Art des zu erzeugenden Elementes
	 * @return Das erzeugte Element
	 */
	public View getElement(int which)
	{
		try
		{
			return generator.generate(which);
		} catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			Log.d(LOGTAG, "id ist " + String.valueOf(which));
			return null;
		}
	}

	public View getElement(Bundle bundle)
	{
//		try
//		{
			return generator.generate(bundle);
//		} catch (Exception e)  
//		{
//			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
//			return null;
//		}
	}

	/**
	 * @author funklos
	 */
	@Override
	public void setIconResource(View active, int pos)
	{

	}



	@Override
	public void setSampleContent(View active, int id)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public void refreshAdapter(View active, int id)
	{
		// TODO Auto-generated method stub
		
	}



	@Override
	public void gridColumnsChanged(View active, int col)
	{
		// TODO Auto-generated method stub
		
	}
}
