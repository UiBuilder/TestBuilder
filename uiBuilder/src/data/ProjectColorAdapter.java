package data;

import java.util.List;

import de.ur.rk.uibuilder.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProjectColorAdapter extends ArrayAdapter<Integer>
{
	private int[] colorrefs;

	public ProjectColorAdapter(Context context, int resource,
			int textViewResourceId, List<Integer> objects)
	{
		super(context, resource, textViewResourceId, objects);
		
		colorrefs = ResArrayImporter.getColors(context.getApplicationContext(), R.array.project_colors);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView colorview = new TextView(getContext());
		colorview.setLayoutParams(new LayoutParams(50, 50));
		colorview.setBackgroundColor(colorrefs[position]);
		colorview.setTag(colorrefs[position]);
		
		return colorview;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return colorrefs.length;
	}

}
