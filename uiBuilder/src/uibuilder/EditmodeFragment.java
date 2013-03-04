package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment implements OnClickListener
{
	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	private Button submit, set, alignLeft, alignRight, alignCenter;
	private EditText editText;
	private View currentView;

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
		// layoutView.setOnClickListener(this);
		submit = (Button) getActivity().findViewById(R.id.item_edit_edittext_submitbutton);
		set = (Button) getActivity().findViewById(R.id.item_edit_itemcount_submitbutton);
		alignCenter = (Button) getActivity().findViewById(R.id.item_edit_align_center_button);
		alignLeft = (Button) getActivity().findViewById(R.id.item_edit_align_left_button);
		alignRight = (Button) getActivity().findViewById(R.id.item_edit_align_right_button);

		submit.setOnClickListener(this);
		set.setOnClickListener(this);
		alignCenter.setOnClickListener(this);
		alignLeft.setOnClickListener(this);
		alignRight.setOnClickListener(this);

		editText = (EditText) getActivity().findViewById(R.id.item_edit_edittext);
		return layoutView;
	}

	protected void adaptLayoutToContext(View view)
	{
		int tag = (Integer.valueOf(view.getTag().toString()));
		layout = (LinearLayout) layoutView;
		currentView = view;
		switch (tag)
		{
		case R.id.element_button:
			layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));

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
		case R.id.item_edit_edittext_submitbutton:
			((TextView) currentView).setText(editText.getText().toString());
			break;
		case R.id.item_edit_align_center_button:
			break;
		}

	}
}
