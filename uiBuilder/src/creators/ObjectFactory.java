package creators;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;

public class ObjectFactory
{

	private Context ref;
	private Generator generator;

	private static final String LOGTAG = "OBJECTFACTORY says:";

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
		
	}

	/**
	 * called to create new objects
	 * @param which the requested object type       
	 * @return the generated object
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

	/**
	 * called to re-generate objects from database
	 * @param bundle
	 * @return
	 */
	public View getElement(Bundle bundle)
	{
			return generator.generate(bundle);
	}
}
