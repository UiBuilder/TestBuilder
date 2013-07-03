package uxcomponents;

import de.ur.rk.uibuilder.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenFlowDesignFragment extends Fragment
{
	private View root;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_project_screenflow_design, null);
		}
		
		return root; 
	}
}
