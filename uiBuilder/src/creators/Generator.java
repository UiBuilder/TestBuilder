package creators;

import helpers.Log;
import manipulators.TheBoss;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class Generator
{	
	
	public static final String OBJECT_TAG = "isObject";

	private int idCount;
	/** Variable zur dynamischen Vergabe laufender IDs */

	private Context context;
	private TheBoss manipulator;
	private ObjectFactory factory;


	/**
	 * Konstruktor
	 */
	public Generator(Context ref, TheBoss mp, ObjectFactory fucktory)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;
		this.factory = fucktory;
	}

	/**
	 * Methode zur Generierung eines neuen TextView-Objekts. Default
	 * Eigenschaften werden gesetzt.
	 * 
	 * @return Neuer TextView
	 */
	protected TextView newTextview()
	{

		TextView textView = new TextView(context);
		//textView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
		textView.setBackgroundResource(R.drawable.default_button_border);
		textView.setId(idCount++);

		textView.setText(context.getResources().getString(R.string.textview_content_default));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		textView.setLayoutParams(params);
		//textView.setTag(ObjectFactory.ID_TEXTVIEW);
		int padding = factory.PADDING_SMALL;
		textView.setPadding(padding, padding, padding, padding);
		textView.setTextColor(context.getResources().getColor(R.color.text_dark));
		textView.setFocusableInTouchMode(true);
		textView.setEnabled(true);
		textView.setClickable(true);
		textView.setOnTouchListener(manipulator);

		return textView;
	}

	/**
	 * Generate new ImageView
	 * 
	 * @return the newly generated ImageView
	 */
	protected ImageView newImageView()
	{
		ImageView imageView = new ImageView(context);
		imageView.setBackgroundResource(R.drawable.default_button_border);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
		imageView.setLayoutParams(params);
		
		int padding = factory.PADDING_SMALL;
		
		imageView.setPadding(padding, padding, padding, padding);
		imageView.setOnTouchListener(manipulator);
		imageView.setClickable(true);
		imageView.setId(idCount++);

		return imageView;
	}

	protected EditText newEditText()
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
		generatedB.setTextColor(context.getResources().getColor(R.color.text_dark));
		generatedB.setBackgroundResource(R.drawable.default_button_border);
		generatedB.setId(idCount++);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		generatedB.setLayoutParams(params);
		generatedB.setEnabled(true);
		generatedB.setOnTouchListener(manipulator);

		return generatedB;
	}

}
