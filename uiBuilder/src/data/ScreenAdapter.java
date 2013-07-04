package data;


import uibuilder.UiBuilderActivity;
import helpers.ImageTools;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
	private TextView messageBadge, newScreenBadge;
	
	int layout;

	private LayoutInflater inflater;

/*	public ScreenAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	*/
	public ScreenAdapter(Context context, Cursor c, int layout)
	{
		super(context, c, true);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}


	/**
	 * Responsible for mapping and inserting data.
	 * Calls an async task to display the preview images.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		Log.d("screenadapter", "bindview");
		messageBadge = (TextView) view.findViewById(R.id.message_badge);
		newScreenBadge = (TextView) view.findViewById(R.id.newscreen_badge);
		
		dateIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_DATE);
		titleIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_NAME);
		idIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		previewIdx = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_SCREEN_PREVIEW);
		
		/*TextView titleView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_title);
		TextView dateView = (TextView) view.findViewById(R.id.activity_manager_griditem_layout_date);
		
		String title = cursor.getString(titleIdx);
		String date = cursor.getString(dateIdx);
		
		titleView.setText(title);
		dateView.setText(date);*/
		
		ImageView preView = (ImageView) view.findViewById(R.id.screenshot);
		String photoFilePath = cursor.getString(previewIdx);
		int id = cursor.getInt(idIdx);
		
		String selection = ScreenProvider.KEY_COMMENTS_ASSOCIATED_SCREEN + " = " + "'" + String.valueOf(id) + "'";
		Cursor messages = context.getApplicationContext().getContentResolver().query(ScreenProvider.CONTENT_URI_COMMENTS, null, selection, null, null);
		
		int messageCount = messages.getCount();
		messages.close();
		
		Log.d("photopath is", photoFilePath);
		
		if (photoFilePath.equalsIgnoreCase("0"))
		{
			newScreenBadge.setVisibility(View.VISIBLE);
		}
		else
		{
			newScreenBadge.setVisibility(View.INVISIBLE);
		}
		
		if(messageCount == 0)
		{
			messageBadge.setVisibility(View.INVISIBLE);
		}
		else
		{
			messageBadge.setVisibility(View.VISIBLE);
			messageBadge.setText(String.valueOf(messageCount));
		}
		
		Bundle tag = new Bundle();
		tag.putInt(ScreenProvider.KEY_ID, id);
		tag.putInt(UiBuilderActivity.NUMBER_OF_COMMENTS, messageCount);
		
		view.setTag(tag);
		
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
		
		view.invalidate();
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root)
	{
		View view = inflater.inflate(layout, root, false);
		
		
		return view;	
	}
}
