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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}


	private int titleIdx;
	private int dateIdx;
	private int idIdx;
	
	private LayoutInflater inflater;

	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
	}

	public ScreenAdapter(Context context, Cursor c, int i)
	{
		super(context, c, i);
		
		inflater = LayoutInflater.from(context);
	}
	

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		dateIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_DATE);
		titleIdx = cursor.getColumnIndexOrThrow(DataBase.KEY_NAME);
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
		
		return newItem;	
	}
}
