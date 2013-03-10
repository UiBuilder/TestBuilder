package creators;

import uibuilder.DesignFragment;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class ObjectFactory
{

	private Context ref;
	private Generator generator;

	private int displayWidth;
	private int displayHeight;
	
	private View selectedView;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c
	 *            Referenz auf die Activity
	 */
	public ObjectFactory(Context c, OnTouchListener l, View active)
	{
		ref = c;
		generator = new Generator(ref, l, this);

		selectedView = active;
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

	
	
	/**
	 * round the provided value to meet the next gridvalue
	 * @param value
	 * @return
	 */
	public int snapToGrid(int value)
	{
		return Math.round((float) value / DesignFragment.SNAP_GRID_INTERVAL) * DesignFragment.SNAP_GRID_INTERVAL;
	}
	
}
