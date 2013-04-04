package data;


import helpers.ImageTools;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;


/**
 * Used to bind the provided cursor to the gridView in @see ManagerActivity
 * Maps the cursor data to the corresponding views provided by the layout.
 * @author funklos
 *
 */
public class ScreenAdapter extends CursorAdapter
{

	private int titleIdx;
	private int dateIdx;
	private int idIdx;
	private int previewIdx;

	private LayoutInflater inflater;

	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	/**
	 * Responsible for mapping and inserting data.
	 * Calls an async task to display the preview images.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		
		Log.d("screenadapter", "bindview");
		dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_DATE);
		titleIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_NAME);
		idIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		previewIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_PREVIEW);
		
		TextView titleView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_title);
		TextView dateView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_date);
		
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
		

		titleView.setText(title);
		dateView.setText(date);
		
		view.invalidate();
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(R.layout.activity_manager_grid_item_layout, root, false);
		
		bindView(view, context, cursor);
		
		//had to set the image here, else the view would not refresh properly
		ImageView preView = (ImageView) view.findViewById(R.id.activity_manager_griditem_layout_image);
		String photoFilePath = cursor.getString(previewIdx);
		int id = cursor.getInt(idIdx);
		
		Log.d("binding view for screen id", String.valueOf(id));

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
		
		view.postInvalidate();
		Log.d("screenadapter", "newview");
		return view;	
	}
}
