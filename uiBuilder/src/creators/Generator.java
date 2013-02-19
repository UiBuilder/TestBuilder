package creators;

import de.ur.rk.uibuilder.R;
import manipulators.TheBoss;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Generator
{

	private int idCount;
	/** Variable zur dynamischen Vergabe laufender IDs */

	private Context context;
	private TheBoss manipulator;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, TheBoss mp)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;
	}

	/**
	 * Methode zur Generierung eines neuen TextView-Objekts. Default
	 * Eigenschaften werden gesetzt.
	 * 
	 * @return Neuer TextView
	 */
	protected TextView newTextview()
	{
		return null;
	}

	/**
	 * Methode zur Generierung eines neuen Button-Objekts. Default Eigenschaften
	 * werden gesetzt.
	 * 
	 * @return Neuer Button
	 */
	protected Button newButton()
	{
		Button generatedB = new Button(context)
		{
			@Override
			public boolean performClick()
			{
				// TODO Auto-generated method stub
				return // super.performClick();
				true;
			}
		};

		generatedB.setText("ButtonButtonButton");
		generatedB.setId(idCount);
		idCount++;

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		generatedB.setLayoutParams(params);
		generatedB.setEnabled(true);
		generatedB.setOnLongClickListener(manipulator);

		return generatedB;
	}

	protected View newDragMenu()
	
	{//Call this experimental!!
		TextView genText = new TextView(context);
		genText.setText("DRAG");
		
		genText.setWidth(30);
		genText.setHeight(30);
		genText.setOnTouchListener(manipulator);
		genText.setEnabled(true);
		return genText;
	}
}
