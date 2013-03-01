package helpers;

import java.util.zip.Inflater;

import de.ur.rk.uibuilder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LibraryAdapter extends BaseAdapter
{
	private Context c;
	private String[] itemDescriptions;
	
	LayoutInflater inflater;
	
	public LibraryAdapter(Context c)
	{
		super();
		this.c = c;
		
		inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemDescriptions = c.getResources().getStringArray(R.array.itembox_items_string_array);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return itemDescriptions.length;
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convert, ViewGroup parent)
	{
		View gridItem;
		
		if (convert == null)
		{
			gridItem = new View(c);
			
			gridItem = inflater.inflate(R.layout.layout_itembox_item, null);
			
			TextView description = (TextView) gridItem.findViewById(R.id.itembox_gridItem_text);
			description.setText(itemDescriptions[pos]);
			
			
			
			return gridItem;
		}
		
		return null;
	}

}
