package data;

import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class ProjectCursorAdapter extends SimpleCursorAdapter
{

	public ProjectCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags)
	{
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);
		
		int projectIdIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		int projectId = cursor.getInt(projectIdIdx);
		
		view.setTag(projectId);
		view.setBackgroundResource(R.drawable.multiple_select_list_states);
	}

}
