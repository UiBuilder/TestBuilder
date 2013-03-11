package helpers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import de.ur.rk.uibuilder.R;

public class IconAdapter extends BaseAdapter {
    
    private Context context;
    ArrayList<Integer> iconRefs;
    

	public IconAdapter(Context context, ArrayList<Integer> iconRefs)
	{
		super();
		this.context = context;
		this.iconRefs = iconRefs;
	}

	@Override
	public int getCount() 
	{
		return iconRefs.size();
	}

	@Override
	public Object getItem(int position)
	{
		return iconRefs.get(position);
	}
	
	@Override
	public long getItemId(int position) 
	{
		return 0;
	}
 
 	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			//if (convertView == null) 
			{
				//gridView = new View(context);
				gridView = inflater.inflate(R.layout.layout_editbox_icon_grid_item, parent, false);
				
				ImageView image = (ImageView) gridView.findViewById(R.id.editmode_grid_item_image);
				
				image.setScaleType(ScaleType.CENTER_INSIDE);
				
				Log.d("pos ist", String.valueOf(position));
				int ref = iconRefs.get(position).intValue();
				image.setImageResource(ref);
				
				image.setPadding(5, 5, 5, 5);
			} /*else 
			{
				gridView = convertView;
			}*/
			
			return gridView;
	}

}
