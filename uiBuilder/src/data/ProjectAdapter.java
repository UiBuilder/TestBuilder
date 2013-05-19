package data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;


/**
 * Used to bind the provided cursor to the gridView in @see ManagerActivity
 * Maps the cursor data to the corresponding views provided by the layout.
 * @author funklos
 *
 */
public class ProjectAdapter extends CursorAdapter 
{

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return super.getItem(position);
	}


	private int titleIdx;
	private int dateIdx;
	private int idIdx;
	private int descIdx;
	//private int previewIdx;

	private LayoutInflater inflater;
	private ContentResolver resolver;

	public ProjectAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resolver = context.getContentResolver();
	}
	

	/**
	 * Responsible for mapping and inserting data.
	 * Calls an async task to display the preview images.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		titleIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_NAME);
		dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_PROJECTS_DATE);
		idIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		int id = cursor.getInt(idIdx);
		
		String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(id);
		
		Cursor sectionCursor = resolver.query(ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
		
		int descIdx = sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_DESCRIPTION);
		int nameIdx = sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_NAME);
		
		LinearLayout container = (LinearLayout) view.findViewById(R.id.project_manager_list_item_sections);
		
		while (sectionCursor.moveToNext())
		{
			LinearLayout section = (LinearLayout) inflater.inflate(R.layout.project_manager_list_item_section_container, null);
			
			TextView name = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_name);
			TextView description = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_description);
			
			name.setText(sectionCursor.getString(nameIdx));
			description.setText(sectionCursor.getString(descIdx));
			
			container.addView(section);
		}
		sectionCursor.close();
		
		
		TextView titleView = (TextView) view.findViewById(R.id.project_manager_list_item_title);
		TextView dateView = (TextView) view.findViewById(R.id.project_manager_list_item_date);
		//TextView sectionView = (TextView) view.findViewById(R.id.project_manager_list_item_section_name);
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
		//String sectionName = cursor.getString(sectionNameIdx);
		//sectionView.setText(sectionName);

		titleView.setText(title);
		dateView.setText(date);
		
		view.invalidate();
		
/*		
 * 
		Log.d("screenadapter", "bindview");
		dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_DATE);
		titleIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_NAME);
		idIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		previewIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_PREVIEW);
*/		
		
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(R.layout.activity_project_manager_list_item_layout, root, false);
	
		//bindView(view, context, cursor);
/*		
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
}
