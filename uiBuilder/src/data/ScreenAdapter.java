package data;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class ScreenAdapter extends CursorAdapter
{

	private int titleIdx;
	private int dateIdx;
	private int idIdx;
	
	private LayoutInflater inflater;

	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

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
		
		TextView titleView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_title);
		TextView dateView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_date);
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
		int id = cursor.getInt(idIdx);
		
		view.setId(id);
		titleView.setText(title);
		dateView.setText(date);
	}


	@Override
	public View newView(Context con, Cursor cursor, ViewGroup root)
	{
		View newItem = inflater.inflate(R.layout.activity_manager_grid_item_layout, root, false);
		
		bindView(newItem, con, cursor);
		
		return newItem;	
	}
}
