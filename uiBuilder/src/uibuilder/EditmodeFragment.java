package uibuilder;

import java.util.zip.Inflater;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment implements OnClickListener
{
	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreate called");
		// TODO Auto-generated method stub


		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");
		this.inflater = inflater;
		this.layoutView = inflater.inflate(R.layout.layout_editmode_fragment, container, false);
		layoutView.setOnClickListener(this);
		layout = (LinearLayout)layoutView;
		return layoutView;
	}

	protected void adaptLayoutToContext(View view)
	{
		int tag = (Integer.valueOf(view.getTag().toString()));

		switch (tag)
		{
		case R.id.element_button:
			
			layout.post(new Runnable()
			{
				
				@Override
				public void run()
				{
					layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));

					
				}
			});
			break;
		case R.id.element_checkbox:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
			layout.addView(inflater.inflate(R.layout.editmode_entry_item_count, null));
			break;
		case R.id.element_edittext:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
			break;
		case R.id.element_imageview:
			layout.addView(inflater.inflate(R.layout.editmode_entry_align_content, null));
			break;
		case R.id.element_numberpick:
			break;
		case R.id.element_radiogroup:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
			layout.addView(inflater.inflate(R.layout.editmode_entry_item_count, null));
			break;
		case R.id.element_ratingbar:
			layout.addView(inflater.inflate(R.layout.editmode_entry_item_count, null));
			break;
		case R.id.element_search:
			break;
		case R.id.element_switch:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
			break;
		case R.id.element_textview:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
			layout.addView(inflater.inflate(R.layout.editmode_entry_align_content, null));
			break;
		case R.id.element_timepicker:
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{

		}

	}
}
