package creators;

import helpers.Log;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * Creates new Objects from the given Bundle[] which contains the properties of the database entries belonging
 * to this screen.
 * The ReGenerator uses the supplied generator instance to fetch a new default item of the desired type
 * and resets its properties with the values provided by the bundle object.
 * 
 * The objects are generated in an async task and published via publishProgress to the UI thread, where a listener
 * callback is fired to notify the ObjectFactory to add a new object to the view tree of the designArea.
 * @author funklos
 *
 */
public class ReGenerator extends AsyncTask<Bundle[], View, Void>
{
	private Generator generator;
	
	public ReGenerator(Generator generator)
	{
		this.generator = generator;
	}


	@Override
	protected Void doInBackground(Bundle[]... params)
	{
		for (Bundle bundle : params[0])
		{
			Log.d("Regenerator", "doInBackground started.");
			View newItem = reGenerate(bundle);
			publishProgress(newItem);
		}
		return null;
	}

	
	@Override
	protected void onPostExecute(Void result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);

	}

	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(View... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		Log.d("Regenerator", "onProgressUpdate called, about to call objectGenerated Listener");
		listener.objectGenerated(values[0]);
	}
	
	
	/**
	 * Use this method to generate Views from a Bundle object.
	 * @author jonesses
	 * @param databaseBundle: a Bundle containing every piece of data to describe a View in the layout
	 * @return the View that the databaseBundle described
	 */
	private View reGenerate(Bundle databaseBundle)
	{
		int type = databaseBundle.getInt(ObjectValues.TYPE);
		
		View xmlView = generator.generate(type);
		
		Bundle properties = (Bundle) xmlView.getTag();
		RelativeLayout.LayoutParams params = null;
		params = new RelativeLayout.LayoutParams(databaseBundle.getInt(ObjectValues.WIDTH), databaseBundle.getInt(ObjectValues.HEIGHT));
		params.leftMargin = databaseBundle.getInt(ObjectValues.X_POS);
		params.topMargin = databaseBundle.getInt(ObjectValues.Y_POS);
		xmlView.setLayoutParams(params);


		switch (type)
		{
		case R.id.element_button:
			
			((Button)xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((Button)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			break;

		case R.id.element_textview:
			
			((TextView)xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((TextView)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			((TextView)xmlView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			break;

		case R.id.element_imageview:

			Log.d("measured imageview", String.valueOf(xmlView.getMeasuredHeight()));
			properties.putString(ObjectValues.IMG_SRC, databaseBundle.getString(ObjectValues.IMG_SRC));
			properties.putInt(ObjectValues.ICN_SRC, databaseBundle.getInt(ObjectValues.ICN_SRC));
			break;

		case R.id.element_edittext:
			
			((EditText) xmlView).setHint(databaseBundle.getString(ObjectValues.USER_TEXT));
			((EditText)xmlView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			((EditText)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			break;

		case R.id.element_radiogroup:
			
			((TextView) xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			break;

		case R.id.element_switch:
			
			((Switch) xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			break;

		case R.id.element_checkbox:
			
			((TextView) ((LinearLayout)xmlView).getChildAt(0)).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			break;

		case R.id.element_list:
			
			properties.putInt(ObjectValues.EXAMPLE_LAYOUT, databaseBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			properties.putInt(ObjectValues.EXAMPLE_CONTENT, databaseBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			break;

		case R.id.element_ratingbar:
			
			((RatingBar)((RelativeLayout)xmlView).getChildAt(0)).setRating(databaseBundle.getInt(ObjectValues.RATING));
			((RatingBar)((RelativeLayout)xmlView).getChildAt(0)).setNumStars(databaseBundle.getInt(ObjectValues.STARS_NUM));
			properties.putInt(ObjectValues.STARS_NUM, databaseBundle.getInt(ObjectValues.STARS_NUM));
			properties.putInt(ObjectValues.RATING, databaseBundle.getInt(ObjectValues.RATING));
			break;

		case R.id.element_seekbar: case R.id.element_numberpick: case R.id.element_timepicker:
			break;

		case R.id.element_grid:

			properties.putInt(ObjectValues.EXAMPLE_LAYOUT, databaseBundle.getInt(ObjectValues.EXAMPLE_LAYOUT));
			properties.putInt(ObjectValues.EXAMPLE_CONTENT, databaseBundle.getInt(ObjectValues.EXAMPLE_CONTENT));
			properties.putInt(ObjectValues.COLUMNS_NUM, databaseBundle.getInt(ObjectValues.COLUMNS_NUM));
			
			((GridView)((ViewGroup)xmlView).getChildAt(0)).setNumColumns(databaseBundle.getInt(ObjectValues.COLUMNS_NUM));
			break;
		
		default:
			
			throw new NoClassDefFoundError();
		}

		
		properties.putInt(ObjectValues.BACKGROUND_PRES, databaseBundle.getInt(ObjectValues.BACKGROUND_PRES));
		properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));
		
		xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

		
		properties.putInt(ObjectValues.DATABASE_ID, databaseBundle.getInt(ObjectValues.DATABASE_ID));
		
		xmlView.setTag(properties);
		
		xmlView.measure(databaseBundle.getInt(ObjectValues.WIDTH), databaseBundle.getInt(ObjectValues.HEIGHT));
		
		//generator.samples.setSampleAdapter(xmlView);
		return xmlView;
	}

	/**
	 * Notify the listener that a new view has been regenerated from a bundle and 
	 * can now be added to the viewtree and further processed.
	 * @author funklos
	 *
	 */
	public interface OnObjectGeneratedListener
	{
		void objectGenerated(View newItem);
	}

	private static OnObjectGeneratedListener listener;

	public static void setOnObjectGeneratedListener(
			OnObjectGeneratedListener listener)
	{
		ReGenerator.listener = listener;
	}
	
}
