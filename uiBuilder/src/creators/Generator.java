package creators;

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

	private int idCount; /** Variable zur dynamischen Vergabe laufender IDs */

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
		View xmlView;
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

		case ObjectFactory.ID_EDITTEXT:
			xmlView = newEditText();
			break;
			
		default:
			throw new NoClassDefFoundError();
		}
		xmlView.setOnTouchListener(manipulator);
		xmlView.setId(idCount++);

		xmlView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//factory.setMinDimensions(xmlView);
		return xmlView;
	}

	/**
	 * Generate new TextView from xml resource
	 * 
	 * @return new TextView
	 */
	private TextView newTextview()
	{
		TextView xmlTextView = (TextView) inflater.inflate(R.layout.item_textview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlTextView.setLayoutParams(params);

		return xmlTextView;
	}

	/**
	 * Generate new ImageView from xml resource
	 * 
	 * @return the newly generated ImageView
	 */
	private ImageView newImageView()
	{
		ImageView xmlImageView = (ImageView) inflater.inflate(R.layout.item_imageview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlImageView.setLayoutParams(params);

		return xmlImageView;
	}

	/**
	 * Generate new EditText from xml resource
	 * @return new Edittext
	 */
	private EditText newEditText()
	{
		EditText xmlEditText = (EditText) inflater.inflate(R.layout.item_edittext_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlEditText.setLayoutParams(params);
		
		return xmlEditText;
	}

	/**
	 * Generate new Button from xml resource
	 * 
	 * @return new Button
	 */
	private Button newButton()
	{	
		Button xmlButton = (Button) inflater.inflate(R.layout.item_button_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		xmlButton.setLayoutParams(params);
		
		return xmlButton;
	}

}
