package creators;

import de.ur.rk.uibuilder.R;
import manipulators.TheBoss;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class ObjectFactory 
{

	public static final int ID_BUTTON = 1; /** Konstante für Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante für TextViews */
	public static final int ID_LONG_CLICK_MENU = 3; /** Konstante für das Kontextmenü beim Verschieben*/
	public static final int ID_EDITTEXT = 4;
	public static final int ID_LISTVIEW = 5;
	public static final int ID_IMAGEVIEW = 6;
	public static final int ID_RADIOBUTTON = 7;
	
	protected final int PADDING_SMALL, PADDING_MEDIUM, PADDING_LARGE;
	
	protected final int TEXTVIEW_MIN_WIDTH, TEXTVIEW_MIN_HEIGHT;
	
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
		//super();

		ref = c;
		generator = new Generator(ref, mp, this);
		
		Resources res = c.getResources();
		
		PADDING_SMALL = res.getDimensionPixelSize(R.dimen.default_padding_small);
		PADDING_MEDIUM = res.getDimensionPixelSize(R.dimen.default_padding_medium);
		PADDING_LARGE = res.getDimensionPixelSize(R.dimen.default_padding_large);
		
		TEXTVIEW_MIN_HEIGHT = res.getDimensionPixelSize(R.dimen.textview_min_height);
		TEXTVIEW_MIN_WIDTH = res.getDimensionPixelSize(R.dimen.textview_min_width);

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
	
	public boolean isResizable(View v)
	{
		int thisWidth = v.getWidth();
		int thisHeight = v.getHeight();
		
		switch (Integer.valueOf( (String)v.getTag() ))
		{
		case ID_TEXTVIEW:
			
			if (thisWidth >= TEXTVIEW_MIN_WIDTH && thisHeight >= TEXTVIEW_MIN_HEIGHT)
				return true;
			break;

		default:
			break;
		}
		
		return false;
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
			case ID_BUTTON: return generator.generate(ID_BUTTON);
				
			case ID_TEXTVIEW: return generator.generate(ID_TEXTVIEW);
			
			case ID_IMAGEVIEW: return generator.generate(ID_IMAGEVIEW);
			
			//case ID_LONG_CLICK_MENU: return generator.newDragMenu();
		
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
