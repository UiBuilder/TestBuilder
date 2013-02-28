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
		View root = inflater.inflate(R.layout.layout_design_fragment,
		        container, false);
		this.root = (RelativeLayout) root.findViewById(R.id.design_area);
		
		manipulator = new TheBoss(getActivity().getApplicationContext(), this.root);
		
		this.root.setOnTouchListener(manipulator);
		this.root.setOnDragListener(manipulator);
		
		measure();
		return root;
	}
	
	public void setSelection(int id)
	{
		manipulator.setObjectType(id);
	}
	

	/**
	 * Aktuelle Displaygröße ermitteln
	 */
	private void measure() 
	{
		int displayHeight = getResources().getDisplayMetrics().heightPixels;
		int displayWidth = getResources().getDisplayMetrics().widthPixels;
		
		int rootHeight = root.getMeasuredHeight();
		int rootWidth = rootHeight/16*9;
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) root.getLayoutParams();
		params.width = rootWidth;
		params.height = rootHeight;
		
		root.setLayoutParams(params);
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

}
