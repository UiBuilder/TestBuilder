package data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;


/**
 * Used to bind the provided cursor to the gridView in @see ManagerActivity
 * Maps the cursor data to the corresponding views provided by the layout.
 * @author funklos
 *
 */
public class SectionAdapter extends CursorAdapter 
{
	
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return super.getItem(position);
	}


	private int titleIdx;
	private int dateIdx;
	private int sectionIdIdx;
	private int descIdx;
	//private int previewIdx;

	private LayoutInflater inflater;
	private ContentResolver resolver;

	public SectionAdapter(Context context, Cursor c, boolean autoRequery)
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
	public void bindView(View section, final Context context, Cursor sectionCursor)
	{
		
		TextView name = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_name);
		TextView description = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_description);

		
		section.setTag(sectionCursor.getInt(sectionIdIdx));
		name.setText(sectionCursor.getString(titleIdx));
		description.setText(sectionCursor.getString(descIdx));
		
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		Log.d("projectadapter", "newView called");
		
		descIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_DESCRIPTION);
		titleIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_NAME);
		sectionIdIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		View view = inflater.inflate(R.layout.project_manager_list_item_section_container, root, false);
		
		return view;	
	}
}
