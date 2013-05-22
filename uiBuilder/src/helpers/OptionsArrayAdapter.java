package helpers;

import de.ur.rk.uibuilder.R;
import uibuilder.EditProjectActivity.Options;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OptionsArrayAdapter extends ArrayAdapter<Options>
{
	private Options[] options;
	private Context context;

	public OptionsArrayAdapter(Context context, int textViewResourceId,
			Options[] objects)
	{
		super(context, textViewResourceId, objects);
		
		this.context = context;
		this.options = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
        if (v == null) 
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_edit_project_optionslist_item, null);
        }
        Options o = options[position];
        if (o != null) {
                TextView name = (TextView) v.findViewById(R.id.project_edit_optionsscreen_optionslist_item_name);
                TextView desc = (TextView) v.findViewById(R.id.project_edit_optionsscreen_optionslist_item_description);
                if (name != null) 
                {
                      name.setText(o.name);                            
                }
                if(desc != null)
                {
                      desc.setText(o.description);
                }
        }
        return v;
	}

	
}
