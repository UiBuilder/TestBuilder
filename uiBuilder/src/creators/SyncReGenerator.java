package creators;

import helpers.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import data.ObjectValues;

public class SyncReGenerator
{
	private Generator generator;
	
	public SyncReGenerator(Generator generator)
	{
		this.generator = generator;
	}
	
	public void regenerate(Bundle[] params)
	{
		for (Bundle bundle: params)
		{
			View v = reGenerate(bundle);
			listener.objectGenerated(v);
		}
	}
	

	/**
	 * Use this method to generate Views from a Bundle object.
	 * 
	 * @author jonesses
	 * @param databaseBundle
	 *            : a Bundle containing every piece of data to describe a View
	 *            in the layout
	 * @return the View that the databaseBundle described
	 */
	private View reGenerate(Bundle databaseBundle)
	{
		int type = databaseBundle.getInt(ObjectValues.TYPE);

		RelativeLayout xmlView = (RelativeLayout) generator.generate(type);
		View generatedView = xmlView.getChildAt(0);
		
		Bundle properties = (Bundle) xmlView.getTag();
		
		RelativeLayout.LayoutParams params = null;
		params = new RelativeLayout.LayoutParams(databaseBundle.getInt(ObjectValues.WIDTH), databaseBundle.getInt(ObjectValues.HEIGHT));
		params.leftMargin = databaseBundle.getInt(ObjectValues.X_POS);
		params.topMargin = databaseBundle.getInt(ObjectValues.Y_POS);
		
		xmlView.setLayoutParams(params);

		switch (type) 
		{
		case ObjectIdMapper.OBJECT_ID_BUTTON:

			((Button) generatedView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((Button) generatedView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));
			((Button) generatedView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));

			break;

		case ObjectIdMapper.OBJECT_ID_TEXTVIEW:

			((TextView) generatedView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((TextView) generatedView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));
			((TextView) generatedView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			break;

		case ObjectIdMapper.OBJECT_ID_IMAGEVIEW:

			generatedView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUNDCOLOR));
			properties.putInt(ObjectValues.BACKGROUNDCOLOR, databaseBundle.getInt(ObjectValues.BACKGROUNDCOLOR));
			
			Log.d("measured imageview", String.valueOf(xmlView.getMeasuredHeight()));
			properties.putString(ObjectValues.IMG_SRC, databaseBundle.getString(ObjectValues.IMG_SRC));
			properties.putInt(ObjectValues.ICN_SRC, databaseBundle.getInt(ObjectValues.ICN_SRC));
			
			Log.d("background REset to ", String.valueOf(databaseBundle.getInt(ObjectValues.BACKGROUNDCOLOR)));
			break;

		case ObjectIdMapper.OBJECT_ID_EDITTEXT:

			((EditText) generatedView).setHint(databaseBundle.getString(ObjectValues.USER_TEXT));
			((EditText) generatedView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));
			((EditText) generatedView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			break;

		case ObjectIdMapper.OBJECT_ID_RADIOGROUP:

			((TextView) generatedView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));

			break;

		case ObjectIdMapper.OBJECT_ID_SWITCH:

			((Switch) generatedView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));

			break;

		case ObjectIdMapper.OBJECT_ID_CHECKBOX:

			((TextView) ((LinearLayout) generatedView).getChildAt(0)).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			properties.putInt(ObjectValues.FONTSIZE, databaseBundle.getInt(ObjectValues.FONTSIZE));

			break;

		case ObjectIdMapper.OBJECT_ID_LISTVIEW:

			properties.putInt(ObjectValues.EXAMPLE_LAYOUT, databaseBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			properties.putInt(ObjectValues.EXAMPLE_CONTENT, databaseBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			break;

		case ObjectIdMapper.OBJECT_ID_RATINGBAR:

			((RatingBar) generatedView).setRating(databaseBundle.getInt(ObjectValues.RATING));
			((RatingBar) generatedView).setNumStars(databaseBundle.getInt(ObjectValues.STARS_NUM));
			properties.putInt(ObjectValues.STARS_NUM, databaseBundle.getInt(ObjectValues.STARS_NUM));
			properties.putInt(ObjectValues.RATING, databaseBundle.getInt(ObjectValues.RATING));
			break;

		case ObjectIdMapper.OBJECT_ID_SEEKBAR:
		case ObjectIdMapper.OBJECT_ID_NUMBERPICKER:
		case ObjectIdMapper.OBJECT_ID_TIMEPICKER:
		case ObjectIdMapper.OBJECT_ID_SPINNER:
			break;

		case ObjectIdMapper.OBJECT_ID_GRIDVIEW:

			properties.putInt(ObjectValues.EXAMPLE_LAYOUT, databaseBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			properties.putInt(ObjectValues.EXAMPLE_CONTENT, databaseBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			properties.putInt(ObjectValues.COLUMNS_NUM, databaseBundle.getInt(ObjectValues.COLUMNS_NUM));

			((GridView) generatedView).setNumColumns(databaseBundle.getInt(ObjectValues.COLUMNS_NUM));
			break;

		default:

			throw new NoClassDefFoundError();
		}

		//properties.putInt(ObjectValues.BACKGROUND_PRES, databaseBundle.getInt(ObjectValues.BACKGROUND_PRES));
		//properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

		//xmlView.setBackgroundResource(R.drawable.object_background_default);

		properties.putInt(ObjectValues.DATABASE_ID, databaseBundle.getInt(ObjectValues.DATABASE_ID));

		xmlView.setTag(properties);

		xmlView.measure(databaseBundle.getInt(ObjectValues.WIDTH), databaseBundle.getInt(ObjectValues.HEIGHT));

		// generator.samples.setSampleAdapter(xmlView);
		return xmlView;
	}
	
	/**
	 * Notify the listener that a new view has been regenerated from a bundle
	 * and can now be added to the viewtree and further processed.
	 * 
	 * @author funklos
	 * 
	 */
	public interface OnObjectGeneratedListener
	{
		void objectGenerated(View newItem);
	}

	private static OnObjectGeneratedListener listener;

	public static void setOnObjectGeneratedListener(OnObjectGeneratedListener listener)
	{
		SyncReGenerator.listener = listener;
	}
}
