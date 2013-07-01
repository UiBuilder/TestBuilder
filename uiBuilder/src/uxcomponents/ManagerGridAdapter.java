package uxcomponents;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.ProjectHolder;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ManagerGridAdapter extends CursorAdapter
{
	private int projects;
	private Cursor projectCursor;
	private ContentResolver resolver;
	
	
	private ProjectHolder[] projectHolder;
	private LayoutInflater inflater;
	
	public ManagerGridAdapter(Context context, Cursor c, boolean autoRequery)
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
		int dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DATE);
		int descIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DESCRIPTION);
		int nameIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
		int idIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		int colorIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_COLOR);
		//int cloudIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_PARSE_ID);
		
		TextView titleView = (TextView) view.findViewById(R.id.grid_manager_grid_item_title);
		TextView descriptionView = (TextView) view.findViewById(R.id.grid_manager_grid_item_description);
		RelativeLayout projectBackground = (RelativeLayout) view.findViewById(R.id.grid_manager_grid_item_color);
		
		int backgroundResource = cursor.getInt(colorIdx);
		int localId = cursor.getInt(idIdx);
		String title = cursor.getString(nameIdx);
		String desc = cursor.getString(descIdx);
		
		projectBackground.setBackgroundColor(backgroundResource);
		titleView.setText(title);
		descriptionView.setText(desc);
		
		Bundle tag = new Bundle();
		tag.putString(ScreenProvider.KEY_PROJECTS_NAME, title);
		tag.putInt(ScreenProvider.KEY_ID, localId);
		view.setTag(tag);
		
		view.invalidate();
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(R.layout.grid_manager_grid_item, root, false);
		/*
		//bindView(view, context, cursor);
		
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
		Log.d("screenadapter", "newview");*/
		return view;	
	}
/*	

	public ManagerGridAdapter(FragmentManager fm, Context c)
	{
		// TODO Auto-generated constructor stub
		
		resolver = c.getContentResolver();
		
		projectCursor = resolver.query(ScreenProvider.CONTENT_URI_PROJECTS, null, null, null, null);
		
		projectHolder = new ProjectHolder[projectCursor.getCount()];
		
		int dateIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DATE);
		int descIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DESCRIPTION);
		int nameIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
		int idIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		int cloudIdx = projectCursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_PARSE_ID);
		
		int i = 0;
		while (projectCursor.moveToNext())
		{
			if (projectCursor.getInt(cloudIdx) != -1)
				Log.d("Cloud object id of project", String.valueOf(projectCursor.getString(cloudIdx)));
			
			projectHolder[i] = new ProjectHolder();
			
			projectHolder[i].projectDate = projectCursor.getString(dateIdx);
			projectHolder[i].projectDescription = projectCursor.getString(descIdx);
			projectHolder[i].projectName = projectCursor.getString(nameIdx);
			projectHolder[i].projectId = projectCursor.getInt(idIdx);
			
			i++;
		}

	}

	@Override
	public Fragment getItem(int pos)
	{
		// TODO Auto-generated method stub
		
		
		Bundle args = projectHolder[pos].getBundle();
		Fragment project = new ProjectDisplay();
		
		project.setArguments(args);
		
		return project;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return projectHolder.length;
	}
*/
}
