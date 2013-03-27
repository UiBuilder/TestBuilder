package data;


import helpers.ImageTools;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class ScreenAdapter extends CursorAdapter
{

	private int titleIdx;
	private int dateIdx;
	private int idIdx;
	private int previewIdx;

	
	private ImageTools imageTools;
	private LayoutInflater inflater;

	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageTools = new ImageTools(context);
	}

	/**
	 * @deprecated
	 * @param context
	 * @param c
	 */
	public ScreenAdapter(Context context, Cursor c)
	{
		super(context, c, 0);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
	}
	

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		dateIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_SCREEN_DATE);
		titleIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_SCREEN_NAME);
		idIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_ID);
		previewIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_SCREEN_PREVIEW);
		
		TextView titleView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_title);
		TextView dateView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_date);
		ImageView preView = (ImageView) view.findViewById(R.id.activity_manager_griditem_layout_image);
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
		int id = cursor.getInt(idIdx);
		
		Log.d("binding view for screen id", String.valueOf(id));
		
		Cursor photoCursor = null;
		String photoFilePath;

		try 
		{	
			Uri image = ContentUris.withAppendedId(DataBase.CONTENT_URI_SCREENS, id);
			
	        photoCursor = context.getContentResolver().query(image, null, null, null, null );
	        
	        Log.d("cursor contains", String.valueOf(photoCursor.getCount()));
	        if ( photoCursor != null && photoCursor.getCount() == 1 ) 
	        {
	            photoCursor.moveToFirst();
	            Log.d("query succeeded", "true");
	            int pathIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_SCREEN_PREVIEW);
	            
	            photoFilePath = photoCursor.getString(pathIdx);
	            
	            if (!photoFilePath.equalsIgnoreCase("0"))
	    		{
	            	Log.d("photopath", photoFilePath);
	    			
	    			ImageTools.setPic(preView, photoFilePath);
	    		}
	            else
		        {
		        	Log.d("photocursor", "set default");
		        	preView.setImageDrawable(context.getResources().getDrawable(R.drawable.manager_blank_screen));
		        }
	        }
	        
	    } 
		catch (Exception e) 
		{
			Log.d("preview image NOT SET", e.getMessage());
		}	
		finally 
		{
	        if ( photoCursor != null ) 
	        {
	            photoCursor.close();
	        }
	    }
		
		Time creation = new Time();
		creation.parse3339(date);
		String creationS = creation.format("%d.%m.%Y %H:%M");
		

		titleView.setText(title);
		dateView.setText(creationS);
	}


	@Override
	public View newView(Context con, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(R.layout.activity_manager_grid_item_layout, root, false);
		Log.d("new view for grid", "called");

		return view;	
	}
}
