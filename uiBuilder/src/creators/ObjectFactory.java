package creators;

import uibuilder.DesignFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class ObjectFactory
{

	private Context ref;
	private Generator generator;

	private int displayWidth;
	private int displayHeight;
	private LayoutInflater inflater;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c
	 *            Referenz auf die Activity
	 */
	public ObjectFactory(Context c, OnTouchListener l)
	{
		ref = c;
		generator = new Generator(ref, l, this);
		inflater = (LayoutInflater) ref.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 
	 * @param which
	 *            Definiert die Art des zu erzeugenden Elementes
	 * @return Das erzeugte Element
	 */
	public View getElement(int which)
	{
		try
		{
			return generator.generate(which);
		} catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			Log.d(LOGTAG, "id ist " + String.valueOf(which));
			return null;
		}
	}

	
	
	/**
	 * round the provided value to meet the next gridvalue
	 * @param value
	 * @return
	 */
	public int snapToGrid(int value)
	{
		return Math.round((float) value / DesignFragment.SNAP_GRID_INTERVAL) * DesignFragment.SNAP_GRID_INTERVAL;
	}
	
	/**
	 * 
	 * @author funklos
	 * @param active item in progress
	 * @param from id of requesting operation
	 */
	public void modify(View active, int from)
	{
		Bundle tagBundle = (Bundle) active.getTag();

		int id = tagBundle.getInt(Generator.ID);
		
		switch (id)
		{
		case R.id.element_list:
			
			checkListType(active, from);
			
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 
	 * @param from
	 */
	private void checkListType(View active, int from)
	{	
		RelativeLayout container = (RelativeLayout) active;
		
		ListView listView = (ListView) container.getChildAt(0);
		final int listLayout;
		
		switch (from)
		{
		case R.id.editmode_list_included_layout_1:
			listLayout = R.layout.item_listview_example_layout_1;
			break;
			
		case R.id.editmode_list_included_layout_2:
			listLayout = R.layout.item_listview_example_layout_2;
			break;
			
		case R.id.editmode_list_included_layout_3:
			Log.d("factory", "registered list callback");
			listLayout = R.layout.item_listview_example_layout_3;
			break;
			
		case R.id.editmode_list_included_layout_4:
			listLayout = R.layout.item_listview_example_layout_4;
			break;
			
		case R.id.editmode_list_included_layout_5:
			listLayout = R.layout.item_listview_example_layout_5;
			break;	
			
		default:
			listLayout = 0;
			break;
		}
		
		setAdapter(listView, listLayout);
		
	}

	protected void setAdapter(View list, final int listLayout)
	{
		final String[] headers = ref.getResources().getStringArray(R.array.listview_listitem_layout_header);
		final String[] contents = ref.getResources().getStringArray(R.array.listview_listitem_layout_content);

		ArrayAdapter<String>listAdapter = new ArrayAdapter<String>(ref.getApplicationContext(), listLayout, headers)
		{

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View layout = inflater.inflate(listLayout, null);
				
				TextView header = (TextView) layout.findViewById(R.id.listview_listitem_header);
				header.setText(headers[position]);
				
				TextView content = (TextView) layout.findViewById(R.id.listview_listitem_content);
				content.setText(contents[position]);
				return layout;
			}
					
		};
		
		if (list instanceof ListView)
		{
			((ListView) list).setAdapter(listAdapter);
		}
		if (list instanceof GridView)
		{
			((GridView) list).setAdapter(listAdapter);
		}
	}
	
}
