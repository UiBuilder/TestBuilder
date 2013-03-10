package helpers;

import java.util.ArrayList;

import de.ur.rk.uibuilder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IconAdapter extends BaseAdapter {
    
    private Context context;
    ArrayList<Integer> iconRefs = new ArrayList<Integer>();
    
    public IconAdapter(Context c) 
    {
    	context = c;
    	
    	initElements();
    }
    
    private void initElements()
	{
		
		this.add(Integer.valueOf(android.R.drawable.ic_input_add));
		this.add(Integer.valueOf(android.R.drawable.ic_input_get));
		
		this.add(Integer.valueOf(android.R.drawable.ic_media_ff));
		this.add(Integer.valueOf(android.R.drawable.ic_media_next));
		this.add(Integer.valueOf(android.R.drawable.ic_media_pause));
		this.add(Integer.valueOf(android.R.drawable.ic_media_play));
		this.add(Integer.valueOf(android.R.drawable.ic_media_previous));
		this.add(Integer.valueOf(android.R.drawable.ic_media_rew));
		
		this.add(Integer.valueOf(android.R.drawable.ic_menu_add));
		
	}

	private void add(Integer element)
    {
    	iconRefs.add(element); 
    }

	@Override
	public int getCount() 
	{
		return iconRefs.size();
	}

	@Override
	public Object getItem(int position)
	{
	 // TODO Auto-generated method stub
		return iconRefs.get(position);
	}
	
	@Override
	public long getItemId(int position) 
	{
	 // TODO Auto-generated method stub
	return 0;
	}
 
 	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			if (convertView == null) 
			{
				gridView = new View(context);
				gridView = inflater.inflate(R.layout.layout_editbox_icon_grid_item, null);
				
				ImageView image = (ImageView) gridView.findViewById(R.id.editmode_grid_item_image);
				
				int ref = iconRefs.get(position).intValue();
				image.setImageResource(ref);
			} else 
			{
				gridView = (View) convertView;
			}
			
			return gridView;
	}

}
