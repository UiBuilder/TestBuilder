package creators;

import helpers.ResArrayImporter;
import uibuilder.DesignFragment;
import uibuilder.EditmodeFragment;
import uibuilder.EditmodeFragment.onObjectEditedListener;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import de.ur.rk.uibuilder.R;

public class ObjectFactory implements onObjectEditedListener
{

	private Context ref;
	private Generator generator;

	private int displayWidth;
	private int displayHeight;
	private LayoutInflater inflater;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	
	/**
	 * Resources
	 */
	String[] headers;
	String[] contents;
	int[] highResIcns;
	
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
		EditmodeFragment.setOnObjectEditedListener(this);
		
		setupResources();
	}

	private void setupResources()
	{
		headers = ref.getResources().getStringArray(R.array.listview_listitem_layout_header);
		contents = ref.getResources().getStringArray(R.array.listview_listitem_layout_content);
		
		highResIcns = ResArrayImporter.getRefArray(ref, R.array.icons_big);
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
		} 
		catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			Log.d(LOGTAG, "id ist " + String.valueOf(which));
			return null;
		}
	}
	
	/**
	 * 
	 * @author funklos
	 * @param active item in progress
	 * @param from id of requesting operation
	 */
	@Override
	public void refreshAdapter(View active, int from)
	{
		Bundle tagBundle = (Bundle) active.getTag();

		int id = tagBundle.getInt(Generator.ID);
		
		switch (id)
		{
		case R.id.element_list:
			
			refreshListAdapter(active, from);
			
			break;
			
		case R.id.element_grid:
			
			refreshGridAdapter(active, from);

		default:
			break;
		}
		
	}
	/**
	 * @author funklos
	 * @param active
	 * @param from
	 */
	private void refreshGridAdapter(View active, int from)
	{
		RelativeLayout container = (RelativeLayout) active;
		
		GridView gridView = (GridView) container.getChildAt(0);
		final int gridLayout;
		
		switch (from)
		{
		case R.id.editmode_grid_included_layout_1:
			gridLayout = R.layout.item_gridview_example_layout_1;
			break;
			
		case R.id.editmode_grid_included_layout_2:
			gridLayout = R.layout.item_gridview_example_layout_2;
			break;
			
		case R.id.editmode_grid_included_layout_3:
			gridLayout = R.layout.item_gridview_example_layout_3;
			break;

		default:
			gridLayout = 0;
			break;
		}
		
		setAdapter(gridView, gridLayout);
	}

	/**
	 * @author funklos
	 * @param from
	 */
	public void refreshListAdapter(View active, int from)
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
			listLayout = R.layout.item_listview_example_layout_3;
			break;
			
		case R.id.editmode_list_included_layout_4:
			listLayout = R.layout.item_listview_example_layout_4;
			break;
			
		case R.id.editmode_list_included_layout_5:
			listLayout = R.layout.item_listview_example_layout_5;
			break;	
			
		case R.id.editmode_list_included_layout_6:
			listLayout = R.layout.item_listview_example_layout_6;
			break;
			
		default:
			listLayout = 0;
			break;
		}
		
		setAdapter(listView, listLayout);
		
	}

	/**
	 * @author funklos
	 * @param list
	 * @param listLayout
	 */
	protected void setAdapter(View list, final int listLayout)
	{
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

	@Override
	public void gridColumnsChanged(View active, int col)
	{	
		ViewGroup container = (ViewGroup) active;
		
		GridView grid = (GridView) container.getChildAt(0);
		grid.setNumColumns(col);	
	}

	@Override
	public void setIconResource(View active, int pos)
	{
		int resourceId = (highResIcns[pos]);

		((ImageView) active).setScaleType(ScaleType.FIT_CENTER);
		((ImageView) active).setImageResource(resourceId);		
	}
	
}
