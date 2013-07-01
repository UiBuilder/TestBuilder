package uxcomponents;

import uxcomponents.ProjectSectionsFragment.sectionSelectedListener;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectCollabsFragment extends Fragment implements sectionSelectedListener
{
	private View root;
	private LinearLayout rootLayout;
	private TextView userPath;
	private String selectedProject;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("projectcollabs Fragment", "onCreateView called");

		Bundle values = getArguments();
		selectedProject = values.getString(ScreenProvider.KEY_PROJECTS_NAME);
		
		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_project_fragment_collabs, container, false);
			rootLayout = (LinearLayout) root;
			userPath = (TextView) root.findViewById(R.id.user_path);
			
			userPath.setText(selectedProject);
			ProjectSectionsFragment.setHeaderSectionSelectedListener(this);
		}
		return root;
	}

	@Override
	public void loadMyAssociatedScreens(Bundle screenId)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadCollabAssociatedScreens(Bundle screenId)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateUserPath(Bundle screenId)
	{
		String screenName = screenId.getString(ScreenProvider.KEY_SECTION_NAME);
		userPath.setText(selectedProject + " - " + screenName);
	}
}
