package de.ur.rk.uibuilder.creators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;

public class ObjectFactory {

	public static final int ID_BUTTON = 1; /** Konstante für Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante für TextViews */
	
	private Context ref;
	private int idCount;
	
	private int displayWidth;
	private int displayHeight;
	
	
	/**
	 * KONSTRUKTOR
	 * 
	 * @param c Referenz auf die Activity
	 */
	public ObjectFactory(Context c) {
		super();
		// TODO Auto-generated constructor stub
		ref = c;
		idCount = 1;
		
		measure();
	}

	/**
	 * Aktuelle Displaygröße ermitteln
	 */
	private void measure() {
		// TODO Auto-generated method stub
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
		switch (which)
		{
			case ID_BUTTON: return newButton();
				
			case ID_TEXTVIEW: return newTextview();
				
		}
		
		return null;
	}

	/**
	 * Methode zur Generierung eines neuen TextView-Objekts.
	 * Default Eigenschaften werden gesetzt.
	 * @return Neuer TextView
	 */
	private View newTextview() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Methode zur Generierung eines neuen Button-Objekts.
	 * Default Eigenschaften werden gesetzt.
	 * @return Neuer Button 
	 */
	private View newButton() {
		// TODO Auto-generated method stub
		Button newB = new Button(ref);
		
		newB.setText("Button");
		newB.setId(idCount);
		idCount++;
		
		@SuppressWarnings("deprecation")
		ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		
		newB.setLayoutParams(params);
		newB.setX(0);
		newB.setY(0);
		
		return newB;
	}
}
