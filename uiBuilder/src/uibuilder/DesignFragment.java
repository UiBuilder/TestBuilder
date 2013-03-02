package uibuilder;


import manipulators.TheBoss;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import de.ur.rk.uibuilder.R;

public class DesignFragment extends Fragment
{

	private RelativeLayout designArea;

	private TheBoss manipulator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_design_fragment,
		        container, false);
		designArea = (RelativeLayout) root.findViewById(R.id.design_area);
		
		manipulator = new TheBoss(getActivity().getApplicationContext(), designArea);
		
		designArea.setOnTouchListener(manipulator);
		designArea.setOnDragListener(manipulator);
/*
		designArea.post(new Runnable()
		{
			

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Log.d("runn", String.valueOf(designArea.getMeasuredWidth()));
				
				int rootWidth = designArea.getMeasuredWidth();
				int rootHeight = rootWidth/16*9;
				
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
				params.width = rootWidth-100;
				params.height = rootHeight;
				
				designArea.setLayoutParams(params);
				designArea.forceLayout();
				
				Log.d("root width post", String.valueOf(designArea.getWidth()));
				Log.d("root height post", String.valueOf(designArea.getHeight()));
			}
		});*/
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

		super.onActivityCreated(savedInstanceState);
	}

	public void setSelection(int id)
	{
		manipulator.setObjectType(id);
	}


	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}
}
