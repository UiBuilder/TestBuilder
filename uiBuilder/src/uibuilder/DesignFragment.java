package uibuilder;


import helpers.Log;
import manipulators.TheBoss;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

		designArea.post(new Runnable()
		{
			

			@Override
			public void run()
			{
				
				int rootWidth = designArea.getMeasuredWidth();
				int rootHeight = rootWidth/16*9;
				
				Log.d("pre measured", String.valueOf(rootWidth));
				Log.d("pre measured", String.valueOf(rootHeight));
				
				int handleSize = getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
				int maxWidth = rootWidth - 2*handleSize;
				int maxHeight = rootHeight - 2*handleSize;
				
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
				params.width = matchWithGrid(maxWidth);
				params.height = matchWithGrid(maxHeight);
				
				designArea.setLayoutParams(params);
				designArea.forceLayout();
				
				Log.d("root width post", String.valueOf(params.width));
				Log.d("root height post", String.valueOf(params.height)); 
			}

			private int matchWithGrid(int size)
			{ 
				return (size / TheBoss.SNAP_GRID_INTERVAL) * TheBoss.SNAP_GRID_INTERVAL;
			}
		});
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
