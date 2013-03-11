package helpers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import de.ur.rk.uibuilder.R;

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
		
		/*
		 * Media
		 */
		add(Integer.valueOf(android.R.drawable.ic_media_ff));
		add(Integer.valueOf(android.R.drawable.ic_media_next));
		add(Integer.valueOf(android.R.drawable.ic_media_pause));
		add(Integer.valueOf(android.R.drawable.ic_media_play));
		add(Integer.valueOf(android.R.drawable.ic_media_previous));
		add(Integer.valueOf(android.R.drawable.ic_media_rew));
		
		/*
		 * Menu
		 */
		add(Integer.valueOf(android.R.drawable.ic_menu_add));
		add(Integer.valueOf(android.R.drawable.ic_menu_agenda));
		add(Integer.valueOf(android.R.drawable.ic_menu_call));
		add(Integer.valueOf(android.R.drawable.ic_menu_camera));
		add(Integer.valueOf(android.R.drawable.ic_menu_compass));
		add(Integer.valueOf(android.R.drawable.ic_menu_crop));
		add(Integer.valueOf(android.R.drawable.ic_menu_day));
		add(Integer.valueOf(android.R.drawable.ic_menu_delete));
		add(Integer.valueOf(android.R.drawable.ic_menu_directions));
		add(Integer.valueOf(android.R.drawable.ic_menu_edit));
		add(Integer.valueOf(android.R.drawable.ic_menu_gallery));
		add(Integer.valueOf(android.R.drawable.ic_menu_help));
		add(Integer.valueOf(android.R.drawable.ic_menu_info_details));
		add(Integer.valueOf(android.R.drawable.ic_menu_manage));
		add(Integer.valueOf(android.R.drawable.ic_menu_mapmode));
		add(Integer.valueOf(android.R.drawable.ic_menu_month));
		add(Integer.valueOf(android.R.drawable.ic_menu_mylocation));
		add(Integer.valueOf(android.R.drawable.ic_menu_myplaces));
		add(Integer.valueOf(android.R.drawable.ic_menu_revert));
		add(Integer.valueOf(android.R.drawable.ic_menu_rotate));
		add(Integer.valueOf(android.R.drawable.ic_menu_save));
		add(Integer.valueOf(android.R.drawable.ic_menu_send));
		add(Integer.valueOf(android.R.drawable.ic_menu_search));
		add(Integer.valueOf(android.R.drawable.ic_menu_set_as));
		add(Integer.valueOf(android.R.drawable.ic_menu_share));
		
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
