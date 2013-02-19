package creators;

import helpers.Log;
import manipulators.TheBoss;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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

		generatedB.setText("Button");
		generatedB.setId(idCount);
		idCount++;

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		generatedB.setLayoutParams(params);
		generatedB.setEnabled(true);
		generatedB.setOnLongClickListener(manipulator);

		return generatedB;
	}

	protected RelativeLayout newDragMenu(View inProgress)
	
	{
		Log.d("dragmenu", "called");
		RelativeLayout overlay = new RelativeLayout(context);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		overlay.setLayoutParams(params);
		
		params = new RelativeLayout.LayoutParams(inProgress.getLayoutParams());
		
		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(params);
		
		Button drag = new Button(context);
		modified.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		drag.setId(1111);
		overlay.addView(drag, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		Button left = new Button(context);
		left.setBackgroundResource(android.R.color.background_dark);
		modified.addRule(RelativeLayout.ALIGN_TOP, drag.getId());
		modified.addRule(RelativeLayout.LEFT_OF, drag.getId());
		modified.height = inProgress.getHeight();
		modified.width = inProgress.getHeight();
		overlay.addView(left, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		Button right = new Button(context);
		right.setBackgroundResource(android.R.color.background_dark);
		modified.addRule(RelativeLayout.ALIGN_TOP, drag.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, drag.getId());
		modified.height = inProgress.getHeight();
		modified.width = inProgress.getHeight();
		overlay.addView(right, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		Button top = new Button(context);
		top.setBackgroundResource(android.R.color.background_dark);
		modified.addRule(RelativeLayout.ABOVE, drag.getId());
		modified.addRule(RelativeLayout.ALIGN_LEFT, drag.getId());
		modified.addRule(RelativeLayout.ALIGN_RIGHT, drag.getId());
		modified.height = inProgress.getHeight();
		modified.width = inProgress.getWidth();
		overlay.addView(top, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		Button bottom = new Button(context);
		bottom.setBackgroundResource(android.R.color.background_dark);
		modified.addRule(RelativeLayout.BELOW, drag.getId());
		modified.addRule(RelativeLayout.ALIGN_LEFT, drag.getId());
		modified.addRule(RelativeLayout.ALIGN_RIGHT, drag.getId());
		modified.height = inProgress.getHeight();
		modified.width = inProgress.getWidth();
		overlay.addView(bottom, modified);
		
		Log.d("bottom", String.valueOf(bottom.getWidth()));
		return overlay;
	}
}
