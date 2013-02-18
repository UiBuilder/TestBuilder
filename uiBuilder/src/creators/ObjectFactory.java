package creators;

import java.util.ArrayList;

import manipulators.Manipulator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjectFactory 
{

	public static final int ID_BUTTON = 1; /** Konstante für Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante für TextViews */

	
	private Context ref;
	private Generator generator;
	//private Manipulator manipulator;
	
	
	private int displayWidth;
	private int displayHeight;
	
	private ArrayList<Button> buttonHolder;
	private static final String LOGTAG = "OBJECTFACTORY says:";
	
	/**
	 * KONSTRUKTOR
	 * 
	 * @param c Referenz auf die Activity
	 */
	public ObjectFactory(Context c) 
	{
		super();

		ref = c;
		generator = new Generator(ref);
		//manipulator = new Manipulator();
		buttonHolder = new ArrayList<Button>();
		///////////////
		measure();
	}
	
	public Manipulator getManipulator()
	{
		return generator.getMani();
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
