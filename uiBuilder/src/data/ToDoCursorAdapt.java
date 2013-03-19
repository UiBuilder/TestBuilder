package data;

import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ToDoCursorAdapt extends CursorAdapter
{
	
	private int contentIdx;
	private int dateIdx;
	private int importantIdx;
	private int doneIdx;
	
	private LayoutInflater inflater;

	public ToDoCursorAdapt(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
	}

	public ToDoCursorAdapt(Context context, Cursor c, int i)
	{
		super(context, c, i);
		
		inflater = LayoutInflater.from(context);
	}
	

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{

		contentIdx = cursor.getColumnIndex(ToDoContent.KEY_CONTENT);
		dateIdx = cursor.getColumnIndex(ToDoContent.KEY_DATE);
		importantIdx = cursor.getColumnIndex(ToDoContent.KEY_IMPORTANT);
		doneIdx = cursor.getColumnIndex(ToDoContent.KEY_DONE);
		
		TextView contentView = (TextView) view.findViewById(R.id.list_item_content);
		TextView dateView = (TextView) view.findViewById(R.id.list_item_date);
		TextView importanceView = (TextView) view.findViewById(R.id.list_item_indicator);
		
		String content = cursor.getString(contentIdx);
		String date = cursor.getString(dateIdx);
		
		
		int important = cursor.getInt(importantIdx);
		boolean done = Boolean.valueOf(cursor.getString(doneIdx));
		
		contentView.setText(content);
		dateView.setText(date);
		
		
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStrikeThruText(false);
		
		if (!done)
		{
			contentView.setBackgroundColor(context.getResources().getColor(R.color.white));
			
			if(important == ToDoContent.HIGH)
			{
				importanceView.setBackgroundColor(context.getResources().getColor(R.color.important));
			}
			if(important == ToDoContent.LOW)
			{
				importanceView.setBackgroundColor(context.getResources().getColor(R.color.item_bg_clr_yellow));
			}
		}
		else
		{
			importanceView.setBackgroundColor(context.getResources().getColor(R.color.grey));
			contentView.setBackgroundColor(context.getResources().getColor(R.color.grey));
			
			p.setStrikeThruText(true);
		}
		contentView.setPaintFlags(p.getFlags());
		
	}


	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup root)
	{
		View newItem = inflater.inflate(R.layout.item_layout, root, false);
		
		return newItem;
		
		
	}
}
