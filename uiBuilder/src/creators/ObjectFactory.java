package creators;

import manipulators.TheBoss;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class ObjectFactory 
{

	public static final int ID_BUTTON = 1; /** Konstante für Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante für TextViews */

	
	private Context ref;
	private Generator generator;
	
	private int displayWidth;
	private int displayHeight;
	
	private static final String LOGTAG = "OBJECTFACTORY says:";
	
	/**
	 * KONSTRUKTOR
	 * 
	 * @param c Referenz auf die Activity
	 */
	public ObjectFactory(Context c, TheBoss mp) 
	{
		super();

		ref = c;
		generator = new Generator(ref, mp);

		measure();
	}

	/**
	 * Aktuelle Displaygröße ermitteln
	 */
	private void measure() 
	{
		displayHeight = ref.getResources().getDisplayMetrics().heightPixels;
		displayWidth = ref.getResources().getDisplayMetrics().widthPixels;
	}
	
	
	/**
	 * 
	 * @param which Definiert die Art des zu erzeugenden Elementes
	 * @return Das erzeugte Element
	 */
	public View getElement (int which)
	{
		try
		{
			switch (which)
			{
			case ID_BUTTON: return generator.newButton();
				
			case ID_TEXTVIEW: return generator.newTextview();
		
			default: throw new NoClassDefFoundError();
			}
		}
		catch (Exception e)
		{

			Log.d(LOGTAG, "Übergebene ID existiert nicht.");


			return null;
		}
	}
}
