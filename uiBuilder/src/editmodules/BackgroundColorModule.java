package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import creators.ObjectIdMapper;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the background color of an element.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author jonesses
 *
 */

public class BackgroundColorModule extends Module
{
	Button backgroundRed;
	Button backgroundOrange;
	Button backgroundYellow;
	Button backgroundGreenLight;
	Button backgroundGreen; 
	Button backgroundAqua; 
	Button backgroundBlue;
	Button backgroundGreyLight;
	Button backgroundGrey;
	Button backgroundReset;

	private LinearLayout box;

	
	public BackgroundColorModule(EditmodeFragment context)
	{
		super(context);
	}

	@Override
	public void getValues()
	{
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_set_background, null);
		
		backgroundRed = (Button) box.findViewById(R.id.editmode_background_red);
		backgroundOrange = (Button) box.findViewById(R.id.editmode_background_orange);
		backgroundYellow = (Button) box.findViewById(R.id.editmode_background_yellow);
		backgroundGreenLight = (Button) box.findViewById(R.id.editmode_background_green_light);
		backgroundGreen = (Button) box.findViewById(R.id.editmode_background_green);
		backgroundAqua = (Button) box.findViewById(R.id.editmode_background_aqua);
		backgroundBlue = (Button) box.findViewById(R.id.editmode_background_blue);
		backgroundGreyLight = (Button) box.findViewById(R.id.editmode_background_grey_light);
		backgroundGrey = (Button) box.findViewById(R.id.editmode_background_grey);
		backgroundReset = (Button) box.findViewById(R.id.editmode_background_reset);
	}
	
	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		adaptToContext();
		
		return box;
	}
	
	private class BackgroundColorModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			Bundle bundle = (Bundle) container.getTag();

			switch (v.getId())
			{
			case R.id.editmode_background_red:
				item.setBackgroundResource(R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_red);

				break;

			case R.id.editmode_background_yellow:
				item.setBackgroundResource(R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_dark);

				break;

			case R.id.editmode_background_orange:
				item.setBackgroundResource(R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_orange);

				break;

			case R.id.editmode_background_green_light:
				item.setBackgroundResource(R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green_light);

				break;

			case R.id.editmode_background_green:
				item.setBackgroundResource(R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green);

				break;

			case R.id.editmode_background_aqua:
				item.setBackgroundResource(R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_aqua);

				break;

			case R.id.editmode_background_blue:
				item.setBackgroundResource(R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_blue);

				break;

			case R.id.editmode_background_grey_light:
				item.setBackgroundResource(R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_light);

				break;

			case R.id.editmode_background_grey:
				item.setBackgroundResource(R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey);

				break;	

			case R.id.editmode_background_reset:
				item.setBackgroundResource(R.drawable.object_background_default);

				resetBackgroundToDefault(bundle);

			}

		}

		private void resetBackgroundToDefault(Bundle bundle)
		{
			bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default);
			bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_default_object);

			switch (bundle.getInt(ObjectValues.TYPE))
			{
			case ObjectIdMapper.OBJECT_ID_BUTTON:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default_button);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_button_default);
				break;

			case ObjectIdMapper.OBJECT_ID_EDITTEXT:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default_edittext);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_border_medium);
				break;

			default:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_default_object);
				break;

			}
		}
	}

	@Override
	protected void adaptToContext()
	{

	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box, super.context));
		BackgroundColorModuleListener backgroundColorListener = new BackgroundColorModuleListener();
		
		backgroundRed.setOnClickListener(backgroundColorListener);
		backgroundOrange.setOnClickListener(backgroundColorListener);
		backgroundYellow.setOnClickListener(backgroundColorListener);
		backgroundGreenLight.setOnClickListener(backgroundColorListener);
		backgroundGreen.setOnClickListener(backgroundColorListener);
		backgroundAqua.setOnClickListener(backgroundColorListener);
		backgroundBlue.setOnClickListener(backgroundColorListener);
		backgroundGreyLight.setOnClickListener(backgroundColorListener);
		backgroundGrey.setOnClickListener(backgroundColorListener);
		backgroundReset.setOnClickListener(backgroundColorListener);
	}

}
