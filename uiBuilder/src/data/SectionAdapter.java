package data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	public static final int TYPE_SCREENFLOW = R.layout.screen_list_item;
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	private int resourceLayout;

	private int titleIdx;
	private int dateIdx;
	private int sectionIdIdx;
	private int descIdx;
	//private int previewIdx;

	private LayoutInflater inflater;
	private ContentResolver resolver;

	public SectionAdapter(Context context, Cursor c, boolean autoRequery, int resource)
	{
		super(context, c, autoRequery);
		
		
		this.resourceLayout = resource;
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
		descIdx = sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_DESCRIPTION);
		titleIdx = sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_SECTION_NAME);
		sectionIdIdx = sectionCursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		switch (resourceLayout) {
		case TYPE_SCREENFLOW:
			
			
			break;

		default:
			
			break;
		}	
		TextView name = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_name);
		TextView description = (TextView) section.findViewById(R.id.project_manager_list_item_section_container_description);

		
		//section.setTag(sectionCursor.getInt(sectionIdIdx));
		name.setText(sectionCursor.getString(titleIdx));
		description.setText(sectionCursor.getString(descIdx));	
		
		Bundle tag = new Bundle();
		tag.putString(ScreenProvider.KEY_SECTION_NAME, sectionCursor.getString(titleIdx));
		tag.putInt(ScreenProvider.KEY_ID, sectionCursor.getInt(sectionIdIdx));
		
		section.setTag(tag);
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		Log.d("projectadapter", "newView called");
		
		View view = inflater.inflate(resourceLayout, root, false);
		
		return view;	
	}
}
