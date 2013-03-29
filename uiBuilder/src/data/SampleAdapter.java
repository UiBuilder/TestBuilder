package data;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class SampleAdapter
{
	private String[] 
			headersActive,
			contentsActive,
			
			headersHipster,
			contentsHipster,
			
			headersBacon,
			contentsBacon;
	
	//private ArrayAdapter<String> listAdapter;
	private LayoutInflater inflater;
	private Context ref;
	
	
	public SampleAdapter(Context c)
	{
		ref = c;
		inflater = (LayoutInflater) ref.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setupResources();
	}
	
	private void setupResources()
	{
		headersHipster = ref.getResources().getStringArray(R.array.listview_listitem_layout_header_hipster);
		contentsHipster = ref.getResources().getStringArray(R.array.listview_listitem_layout_content_hipster);

		headersBacon = ref.getResources().getStringArray(R.array.listview_listitem_layout_header_bacon);
		contentsBacon = ref.getResources().getStringArray(R.array.listview_listitem_layout_content_bacon);

		headersActive = headersHipster;
		contentsActive = contentsHipster;
	}
	
	/**
	 * 
	 * @param active
	 * @param id
	 */
	public void setSampleContent(View active, int id)
	{
		Bundle bundle = (Bundle) active.getTag();
		
		switch (id)
		{
		case R.id.content_choose_hipster:

			bundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_hipster);
			break;

		case R.id.content_choose_bacon:

			bundle.putInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_bacon);
			break;

		default:
			break;
		}
		
		setSampleAdapter(active);
	}
	
	public void setSampleLayout(View active, int layout)
	{
		Bundle tagBundle = (Bundle) active.getTag();
		int id = tagBundle.getInt(ObjectValues.TYPE);

		switch (id)
		{
		case R.id.element_list:

			setSampleListLayout(active, layout);
			break;

		case R.id.element_grid:

			setSampleGridLayout(active, layout);
			break;
		}
		
		setSampleAdapter(active);
	}
	
	public void setSampleAdapter(View active)
	{

		ViewGroup container = (ViewGroup) active;
		View inner = container.getChildAt(0);
		
		Bundle tag = (Bundle) active.getTag();

		int content = tag.getInt(ObjectValues.EXAMPLE_CONTENT, R.id.content_choose_hipster);
		final int listLayout = tag.getInt(ObjectValues.EXAMPLE_LAYOUT, R.id.editmode_grid_included_layout_4);
		int	type = tag.getInt(ObjectValues.TYPE);		
		
		setActiveSampleContent(content);	
		ArrayAdapter<String> adapter = generateAdapter(listLayout);
		setInAppropriateWay(inner, type, adapter);
	}

	/**
	 * @param inner
	 * @param type
	 * @param adapter
	 */
	private void setInAppropriateWay(View inner, int type,
			ArrayAdapter<String> adapter)
	{
		switch (type)
		{
		case R.id.element_list:
			((ListView)inner).setAdapter(adapter);
			break;

		case R.id.element_grid:
			((GridView)inner).setAdapter(adapter);
			break;
		default:
			break;
		}
	}

	/**
	 * @param listLayout
	 * @return
	 */
	private ArrayAdapter<String> generateAdapter(final int listLayout)
	{
		return new ArrayAdapter<String>(ref, listLayout, headersActive)
		{
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View layout = inflater.inflate(listLayout, null);

				TextView header = (TextView) layout.findViewById(R.id.listview_listitem_header);
				header.setText(headersActive[position]);

				TextView content = (TextView) layout.findViewById(R.id.listview_listitem_content);
				content.setText(contentsActive[position]);

				return layout;
			}
		};
	}

	/**
	 * @param content
	 */
	private void setActiveSampleContent(int content)
	{
		switch (content)
		{
		case R.id.content_choose_hipster:
			headersActive = headersHipster;
			contentsActive = contentsHipster;
			break;

		case R.id.content_choose_bacon:
			headersActive = headersBacon;
			contentsActive = contentsBacon;
			break;
		}
	}

	
	/**
	 * @author funklos
	 * @param from
	 */
	public void setSampleListLayout(View active, int from)
	{

		Bundle bundle = (Bundle) active.getTag();
		int listLayout;

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

		bundle.putInt(ObjectValues.EXAMPLE_LAYOUT, listLayout);
	}
	
	/**
	 * @author funklos
	 * @param active
	 * @param from
	 */
	private void setSampleGridLayout(View active, int from)
	{
		Bundle bundle = (Bundle) active.getTag();

		int gridLayout;

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

		case R.id.editmode_grid_included_layout_4:
			gridLayout = R.layout.item_gridview_example_layout_4;
			break;

		default:
			gridLayout = 0;
			break;
		}
		bundle.putInt(ObjectValues.EXAMPLE_LAYOUT, gridLayout);
	}
	
}
