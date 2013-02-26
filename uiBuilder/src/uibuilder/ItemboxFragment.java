package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class ItemboxFragment extends Fragment
{
	private Button createButton, createTextView, createImage;
	private Activity act;
	
	@Override
	public void onAttach(Activity activity)
	{
		this.act = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		return inflater.inflate(R.layout.layout_itembox_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*
		createButton = (Button) act.findViewById(R.id.new_element_button);
		createTextView = (Button) act.findViewById(R.id.new_element_textview);
		createImage = (Button) act.findViewById(R.id.new_element_imageview);
	
		LinearLayout itembox = (LinearLayout) act.findViewById(R.id.itembox_layout);
		itembox.requestLayout();
		
		createButton.setTag(ObjectFactory.ID_BUTTON);
		createTextView.setTag(ObjectFactory.ID_TEXTVIEW);
		createImage.setTag(ObjectFactory.ID_IMAGEVIEW);
			
		createButton.setOnClickListener((OnClickListener) act);
		createTextView.setOnClickListener((OnClickListener) act);
		createImage.setOnClickListener((OnClickListener) act);*/
	
	}

}
