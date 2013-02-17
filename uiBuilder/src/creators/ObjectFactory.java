package creators;

import java.util.ArrayList;

import manipulators.Manipulator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class ObjectFactory 
{
	public static final int ID_BUTTON = 1; /** Konstante f�r Buttons */
	public static final int ID_TEXTVIEW = 2; /** Konstante f�r TextViews */
	
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
		generator = new Generator();
		//manipulator = new Manipulator();
		buttonHolder = new ArrayList<Button>();
		//
		measure();
	}

	/**
	 * Aktuelle Displaygr��e ermitteln
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
			Log.d(LOGTAG, "�bergebene ID existiert nicht.");
			return null;
		}
	}
	

	private class Generator
	{
		private int idCount; /** Variable zur dynamischen Vergabe laufender IDs */
		private Manipulator manipulator;
		/**
		 * Konstruktor
		 */
		public Generator() 
		{
			idCount = 1;
			manipulator = new Manipulator(ref);
		}


		/**
		 * Methode zur Generierung eines neuen TextView-Objekts.
		 * Default Eigenschaften werden gesetzt.
		 * @return Neuer TextView
		 */
		private TextView newTextview() 
		{
			return null;
		}

		/**
		 * Methode zur Generierung eines neuen Button-Objekts.
		 * Default Eigenschaften werden gesetzt.
		 * 
		 * @return Neuer Button 
		 */
		private Button newButton() 
		{
			Button generatedB = new Button(ref);
			
			
			generatedB.setText("Button");
			generatedB.setId(idCount);
			idCount++;
			
			ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			
			generatedB.setLayoutParams(params);
			generatedB.setX(100);
			generatedB.setY(100);
			generatedB.setEnabled(true);
			
			generatedB.setOnLongClickListener(manipulator);
			buttonHolder.add(generatedB);
			return generatedB;
		}
	}
}
