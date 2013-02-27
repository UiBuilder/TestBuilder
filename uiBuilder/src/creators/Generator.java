package creators;

import helpers.Log;
import manipulators.TheBoss;
import android.content.Context;
import android.view.LayoutInflater;
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
	private LayoutInflater inflater;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, TheBoss mp, ObjectFactory fucktory)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;
		this.factory = fucktory;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	protected View generate (int id)
	{
			View xmlView = null;
			switch (id)
			{
			case ObjectFactory.ID_BUTTON:
				xmlView = newButton();
				break;
				
			case ObjectFactory.ID_TEXTVIEW:
				xmlView = newTextview();
				break;
				
			case ObjectFactory.ID_IMAGEVIEW:
				xmlView = newImageView();
				break;
	
			default:
				throw new NoClassDefFoundError();
			}
			xmlView.setOnTouchListener(manipulator);
			xmlView.setId(idCount++);
	
			xmlView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		return xmlView;
	}

	/**
	 * Methode zur Generierung eines neuen TextView-Objekts. Default
	 * Eigenschaften werden gesetzt.
	 * 
	 * @return Neuer TextView
	 */
	private TextView newTextview()
	{
		TextView xmlTextView = (TextView) inflater.inflate(R.layout.textview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlTextView.setLayoutParams(params);

		return xmlTextView;
	}

	/**
	 * Generate new ImageView
	 * 
	 * @return the newly generated ImageView
	 */
	private ImageView newImageView()
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

	private EditText newEditText()
	{
		return null;
	}

	/**
	 * Methode zur Generierung eines neuen Button-Objekts. Default Eigenschaften
	 * werden gesetzt.
	 * 
	 * @return Neuer Button
	 */
	private Button newButton()
	{	
		Button xmlButton = (Button) inflater.inflate(R.layout.button_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlButton.setLayoutParams(params);
		
		return xmlButton;
	}

}
