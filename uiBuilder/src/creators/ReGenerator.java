package creators;

import helpers.Log;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

public class ReGenerator extends AsyncTask<ArrayList<Bundle>, View, Void>
{
	private Generator generator;
	
	public ReGenerator(Generator generator)
	{
		this.generator = generator;
	}


	@Override
	protected Void doInBackground(ArrayList<Bundle>... params)
	{
		for (Bundle bundle : params[0])
		{
			Log.d("async", "regeneration");
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
		properties.putInt(ObjectValues.BACKGROUND_PRES, databaseBundle.getInt(ObjectValues.BACKGROUND_PRES));


		switch (type)
		{
		case R.id.element_button:
			
			((Button)xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((Button)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			
			properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

			
			break;

		case R.id.element_textview:
			
			((TextView)xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			((TextView)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			((TextView)xmlView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			
			properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));


			
			break;

		case R.id.element_imageview:
			
			Log.d("measured imageview", String.valueOf(xmlView.getMeasuredHeight()));
			//xmlView.setBackgroundResource(R.drawable.object_background_default);
			//insertImageToView(properties,databaseBundle, xmlView);
			properties.putString(ObjectValues.IMG_SRC, databaseBundle.getString(ObjectValues.IMG_SRC));
			properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

			break;

		case R.id.element_edittext:
			
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			((EditText)xmlView).setGravity(databaseBundle.getInt(ObjectValues.ALIGNMENT));
			((EditText)xmlView).setTextSize(databaseBundle.getInt(ObjectValues.FONTSIZE));

			
			break;

		case R.id.element_radiogroup:
			
			((TextView) xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));

			xmlView.setBackgroundResource(R.drawable.object_background_default);

			
			break;

		case R.id.element_switch:
			
			((TextView) xmlView).setText(databaseBundle.getString(ObjectValues.USER_TEXT));
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_checkbox:
			
			((TextView) ((LinearLayout)xmlView).getChildAt(0)).setText(databaseBundle.getString(ObjectValues.USER_TEXT));

			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_list:
			
			xmlView.setBackgroundResource(R.drawable.object_background_default);
			break;

		case R.id.element_numberpick:
			
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_ratingbar:
			
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));
			((RatingBar)((RelativeLayout)xmlView).getChildAt(0)).setRating(databaseBundle.getInt(ObjectValues.RATING));
			((RatingBar)((RelativeLayout)xmlView).getChildAt(0)).setNumStars(databaseBundle.getInt(ObjectValues.STARS_NUM));
			properties.putInt(ObjectValues.BACKGROUND_EDIT, databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

			
			break;

		case R.id.element_seekbar:
			
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_timepicker:
			
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_grid:
			

			((GridView)((RelativeLayout)xmlView).getChildAt(0)).setNumColumns(databaseBundle.getInt(ObjectValues.COLUMNS_NUM));
			xmlView.setBackgroundResource(R.drawable.object_background_default);
			
			
			break;
		
		default:
			
			throw new NoClassDefFoundError();
		}
		params.leftMargin = databaseBundle.getInt(ObjectValues.X_POS);
		params.topMargin = databaseBundle.getInt(ObjectValues.Y_POS);
		xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValues.BACKGROUND_EDIT));

		xmlView.setLayoutParams(params);
		
		properties.putInt(ObjectValues.DATABASE_ID, databaseBundle.getInt(ObjectValues.DATABASE_ID));
		
		xmlView.setTag(properties);
		
		xmlView.measure(databaseBundle.getInt(ObjectValues.WIDTH), databaseBundle.getInt(ObjectValues.HEIGHT));
		
		return xmlView;
	}

	
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
