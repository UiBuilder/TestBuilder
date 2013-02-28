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
		
		return root;
	}
	
	public void setSelection(int id)
	{
		manipulator.setObjectType(id);
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

}
