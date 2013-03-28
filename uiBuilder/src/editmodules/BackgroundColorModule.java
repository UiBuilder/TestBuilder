package editmodules;

import data.ObjectValues;
import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

	private View box;
	
	private View requesting;
	
	public BackgroundColorModule(Context context)
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
		box = super.inflater.inflate(R.layout.editmode_entry_set_background, null);
		box.setOnClickListener(new ExpansionListener(box));
		
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

	@Override
	public View getInstance(View inProgress)
	{
		requesting = inProgress;
		adaptToContext();
		return box;
	}
	
	private class BackgroundColorModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			Bundle bundle = (Bundle) requesting.getTag();

			switch (v.getId())
			{
			case R.id.editmode_background_red:
				requesting.setBackgroundResource(R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_red);

				break;

			case R.id.editmode_background_yellow:
				requesting.setBackgroundResource(R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_dark);

				break;

			case R.id.editmode_background_orange:
				requesting.setBackgroundResource(R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_orange);

				break;

			case R.id.editmode_background_green_light:
				requesting.setBackgroundResource(R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green_light);

				break;

			case R.id.editmode_background_green:
				requesting.setBackgroundResource(R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green);

				break;

			case R.id.editmode_background_aqua:
				requesting.setBackgroundResource(R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_aqua);

				break;

			case R.id.editmode_background_blue:
				requesting.setBackgroundResource(R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_blue);

				break;

			case R.id.editmode_background_grey_light:
				requesting.setBackgroundResource(R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_light);

				break;

			case R.id.editmode_background_grey:
				requesting.setBackgroundResource(R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey);

				break;

			case R.id.editmode_background_reset:
				requesting.setBackgroundResource(R.drawable.object_background_default);

				resetBackgroundToDefault(bundle);

			}

		}

		private void resetBackgroundToDefault(Bundle bundle)
		{
			bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default);

			switch (bundle.getInt(ObjectValues.TYPE))
			{
			case R.id.element_button:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default_button);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_button_default);
				break;

			case R.id.element_edittext:
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

}
