package data;


import helpers.ImageTools;
import android.content.Context;
import android.database.Cursor;
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

	private LayoutInflater inflater;

	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d("screenadapter", "getview");
		
		/*if (position == 0)
		{
			return inflater.inflate(R.layout.activity_manager_new_screen_layout, null);
		}
		else */return super.getView(position, convertView, parent);
		
		//return super.getView(position, convertView, parent);
	}


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
		ImageView preView = (ImageView) view.findViewById(R.id.activity_manager_griditem_layout_image);
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
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
		
		Time creation = new Time();
		creation.parse3339(date);
		String creationS = creation.format("%d.%m.%Y %H:%M");
		

		titleView.setText(title);
		dateView.setText(creationS);

	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(R.layout.activity_manager_grid_item_layout, root, false);
		bindView(view, context, cursor);
		Log.d("screenadapter", "newview");
		return view;	
	}
}
