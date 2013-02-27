package creators;

import de.ur.rk.uibuilder.R;
import manipulators.TheBoss;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjectFactory 
{

	public static final int ID_BUTTON = 1; /** Konstante für Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante für TextViews */
	public static final int ID_LONG_CLICK_MENU = 3; /** Konstante für das Kontextmenü beim Verschieben*/
	public static final int ID_EDITTEXT = 4;
	public static final int ID_LISTVIEW = 5;
	public static final int ID_IMAGEVIEW = 6;
	public static final int ID_RADIOBUTTON = 7;
	
	protected int TEXTVIEW_MIN_WIDTH, TEXTVIEW_MIN_HEIGHT;
	
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
	 * TODO
	 * @param v
	 * @return
	 */
	//klappt auch nicht
	public boolean isResizable(View v, int distance)
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
	
	
	//GEHT SO NICHT, oder könnte klappen muss nur richtig eingesetzt werden
	//das setzen mit set klappt, die abfrage aber nicht, WO SOLL ABGEFRAGT WERDEN?
	//Am besten bevor neue params gesetzt werden abfragen?
	
	//testweise in generator.generate gesetzt, wrap content measures werden auch richtig gesetzt
	public void setMinDimensions(View v)
	{
		if (v instanceof TextView)
		{
			TEXTVIEW_MIN_HEIGHT = v.getMeasuredHeight();
			TEXTVIEW_MIN_WIDTH = v.getMeasuredWidth();
			Log.d("minwidth", String.valueOf(TEXTVIEW_MIN_WIDTH));
			Log.d("minheight", String.valueOf(TEXTVIEW_MIN_HEIGHT));
		}
	}
	/*
	public boolean getMinWidth(View v, int newWidth)
	{
		if (width >= TEXTVIEW_MIN_WIDTH)
		{
			return width;
		}
		if (v instanceof TextView)
		{
			if (newWidth >= TEXTVIEW_MIN_WIDTH)
				return true;
		}
		Log.d("minwidth", "false");
		return false;
	}
	
	public int getMinHeight(int height)
	{
		if (height >= TEXTVIEW_MIN_HEIGHT)
		{
			return height;
		}
		return TEXTVIEW_MIN_HEIGHT;
	}*/
	
	/**
	 * 
	 * @param which Definiert die Art des zu erzeugenden Elementes
	 * @return Das erzeugte Element
	 */
	public View getElement (int which)
	{
		try
		{
			return generator.generate(which);
		}
		catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			return null;
		}
	}

}
