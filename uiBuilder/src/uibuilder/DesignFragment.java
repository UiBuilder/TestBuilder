package uibuilder;


import manipulators.TheBoss;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class DesignFragment extends Fragment
{

	private RelativeLayout root;

	private TheBoss manipulator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_layout_design);
		
		
		 
		return inflater.inflate(R.layout.layout_design_fragment,
		        container, false);
	}



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_layout_design);

/*		linkElements();
		initHelpers();
		setListeners();*/
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.design, menu);
		return false;
	}*/
}
