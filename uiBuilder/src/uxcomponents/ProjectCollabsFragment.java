package uxcomponents;

import uxcomponents.ProjectSectionsFragment.sectionSelectedListener;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ProjectCollabsFragment extends Fragment implements sectionSelectedListener, OnClickListener
{
	private View root;
	private LinearLayout rootLayout;
	private TextView userPath;
	private LayoutInflater inflater;
	private LinearLayout collabLayout;
	
	private String selectedProject;
	private int projectId;
	private int colorCode;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Log.d("projectcollabs fragment", "activity created");
		
		
	}
	
	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		getCollabs();
		
		
	}

	private void getCollabs()
	{
		ContentResolver resolver = getActivity().getContentResolver();
		
		String selection = ScreenProvider.KEY_COLLAB_OF_PROJECT + " = " + "'" + String.valueOf(projectId) + "'";
		Cursor collabs = resolver.query(ScreenProvider.CONTENT_URI_COLLABS, null, selection, null, null);
		
		displayCollabs(collabs);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("projectcollabs Fragment", "onCreateView called");
		this.inflater = inflater;
		
		Bundle values = getArguments();
		selectedProject = values.getString(ScreenProvider.KEY_PROJECTS_NAME);
		projectId = values.getInt(ScreenProvider.KEY_ID);
		colorCode = values.getInt(ScreenProvider.KEY_PROJECTS_COLOR);
		
		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_project_fragment_collabs, container, false);
			rootLayout = (LinearLayout) root;
			collabLayout = (LinearLayout) root.findViewById(R.id.collablayout);
			//root.setBackgroundColor(colorCode);
			
			userPath = (TextView) root.findViewById(R.id.user_path);
			
			userPath.setText(selectedProject);
			userPath.setBackgroundColor(colorCode);
			ProjectSectionsFragment.setHeaderSectionSelectedListener(this);
		}
		return root;
	}
	
	private View lastview;
	private int numberOfCollabs = 0;
	
	private void displayCollabs(Cursor collabs)
	{
		int nameIdx = collabs.getColumnIndexOrThrow(ScreenProvider.KEY_COLLAB_NAME);
		int pictureIdx = collabs.getColumnIndexOrThrow(ScreenProvider.KEY_COLLAB_PICTURE);
		int cloudIdIdx = collabs.getColumnIndexOrThrow(ScreenProvider.KEY_COLLAB_PARSEID);
		int localIdIdx = collabs.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		collabLayout.removeAllViews();
		
		while (collabs.moveToNext())
		{
			String collabname = collabs.getString(nameIdx);
			String id = collabs.getString(cloudIdIdx);
			int localId = collabs.getInt(localIdIdx);
			
			Log.d("collab in cursor", collabname);
				
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.collab_icon, null);
			
			Bundle userBundle = new Bundle();
			userBundle.putString(ScreenProvider.KEY_COLLAB_NAME, collabname);
			userBundle.putString(ScreenProvider.KEY_COLLAB_PARSEID, id);
			userBundle.putInt(ScreenProvider.KEY_ID, localId);
			
			
			/*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
			if(lastview == null)
			{
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			}
			else
			{
				params.addRule(RelativeLayout.LEFT_OF, lastview.getId());
			}
			lastview = item;
			item.setId(numberOfCollabs++);
			item.setLayoutParams(params);*/
			
			collabLayout.addView(item);
			ImageView icon = (ImageView) item.findViewById(R.id.collab_icon);
			icon.setOnClickListener(this);
			icon.setTag(userBundle);
		}
		collabs.close();
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
	
	public interface userSelectedListener
	{
		void displayUserDetails(Bundle userValues);
		
		void hideUserDetails();
	}

	private static userSelectedListener userSelectedListener;

	public static void setUserSelectedListener(
			userSelectedListener listener)
	{
		ProjectCollabsFragment.userSelectedListener = listener;
	}

	private View lastSelection;
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.layout_project_userdetails_remove:
			
			Log.d("delete", "called");
			Bundle userValues = (Bundle) v.getTag();
			String cloudId = userValues.getString(ScreenProvider.KEY_COLLAB_PARSEID);
			int localId = userValues.getInt(ScreenProvider.KEY_ID);
			Log.d("delete", String.valueOf(localId));
			
			ContentResolver resolver = getActivity().getContentResolver();
			String selection = ScreenProvider.KEY_COLLAB_PARSEID + " = " + "'" + cloudId + "'" + " AND " + ScreenProvider.KEY_COLLAB_OF_PROJECT + " = " + "'" + String.valueOf(projectId) + "'";
			
			Uri collabUri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_COLLABS, localId);
			int count = resolver.delete(collabUri, null, null);
			
			userSelectedListener.hideUserDetails();
			getCollabs();
			break;

		default:
			if (v.isActivated() && v == lastSelection)
			{
				userSelectedListener.hideUserDetails();
				v.setActivated(false);
				v.setBackgroundColor(getActivity().getResources().getColor(R.color.superlight_grey));
			}
			else
			{
				Bundle userBundle = (Bundle) v.getTag();
				userSelectedListener.displayUserDetails(userBundle);
				v.setActivated(true);
				if(lastSelection != null)
				lastSelection.setBackgroundColor(getActivity().getResources().getColor(R.color.superlight_grey));
				lastSelection = v;
				v.setBackgroundColor(getActivity().getResources().getColor(R.color.element_out_of_dropzone));
			}
			break;
		}	
		
	}
}
