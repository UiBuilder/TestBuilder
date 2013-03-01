package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.inflater = inflater;
		layout = (LinearLayout)getActivity().findViewById(R.id.fragment_editbox);
		adaptLayoutToContext();

		layoutView = inflater.inflate(R.layout.layout_editmode_fragment, container, false);
		adaptLayoutToContext();

		return layoutView;
	}

	private void adaptLayoutToContext()
	{
		View newEntry = inflater.inflate(R.layout.editmode_entry_enter_text, null);
		layout.addView(newEntry);
		// the layout elements should be put together here

	}

	@Override
	public void onDetach()
	{
		// TODO Auto-generated method stub
		super.onDetach();
	}

}
