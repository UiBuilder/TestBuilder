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

	private RelativeLayout root;

	private TheBoss manipulator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_design_fragment,
		        container, false);
		this.root = (RelativeLayout) root.findViewById(R.id.design_area);
		
		manipulator = new TheBoss(getActivity().getApplicationContext(), this.root);
		
		this.root.setOnTouchListener(manipulator);
		this.root.setOnDragListener(manipulator);
		
		measure();
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		measure();
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}
	
	

	/**
	 * Aktuelle Displaygröße ermitteln
	 */
	private void measure() 
	{
		root.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		int displayHeight = getResources().getDisplayMetrics().heightPixels;
		int displayWidth = getResources().getDisplayMetrics().widthPixels;
		
		int rootHeight = root.getMeasuredHeight();
		int rootWidth = rootHeight/16*9;
		
		Log.d("root on attac w", String.valueOf(root.getMeasuredWidth()));
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) root.getLayoutParams();
		params.width = rootWidth;
		params.height = rootHeight;
		
		//root.setLayoutParams(params);
	}

}
