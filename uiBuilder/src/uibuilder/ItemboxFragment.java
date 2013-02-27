package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.ur.rk.uibuilder.R;

public class ItemboxFragment extends Fragment implements OnClickListener, OnTouchListener
{
	private Button createButton, createTextView, createImage;
	private Activity act;
	
	private int selection;
	
	private static final int BUTTON = 1, TEXTVIEW = 2, IMAGEVIEW = 3; 
	
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
		View root = inflater.inflate(R.layout.layout_itembox_fragment, container, false);
		
		createButton = (Button) root.findViewById(R.id.new_element_button);
		createButton.setId(BUTTON);
		
		createTextView = (Button) root.findViewById(R.id.new_element_textview);
		createTextView.setId(TEXTVIEW);
		
		createImage = (Button) root.findViewById(R.id.new_element_imageview);
		createImage.setId(IMAGEVIEW);
		
		createButton.setOnTouchListener(this);
		createTextView.setOnTouchListener(this);
		createImage.setOnTouchListener(this);
		
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v)
	{
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1)
	{
		switch (v.getId())
		{
		case BUTTON:
			selection = BUTTON;
			Log.d("itembox reports: set to", String.valueOf(selection));
			break;

		case TEXTVIEW:
			selection = TEXTVIEW;
			Log.d("itembox reports: set to", String.valueOf(selection));
			break;
			
		case IMAGEVIEW:
			selection = IMAGEVIEW;
			Log.d("itembox reports: set to", String.valueOf(selection));
			break;
		default:
			break;
		}		
		return false;
	}

}
