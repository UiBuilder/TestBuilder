package uxcomponents;

import uibuilder.ItemboxFragment;
import uibuilder.ItemboxFragment.onObjectRequestedListener;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class ScreenFlowScreensFragment extends Fragment implements LoaderCallbacks<Cursor>, OnItemLongClickListener, OnDragListener, OnItemClickListener
{
	private View root;
	
	private SectionAdapter adapter;
	private ListView screenList;
	private int projectId;
	
	private LoaderManager manager;
	private LayoutInflater inflater;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupDatabaseConnection();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (root == null)
		{
			this.inflater = inflater;
			root = inflater.inflate(R.layout.layout_project_screenflow_screens, null);
			screenList = (ListView) root.findViewById(R.id.screenflow_screens);
			screenList.setOnItemLongClickListener(this);
			screenList.setOnItemClickListener(this);
			//root.setOnDragListener(this);
		}
		Bundle values = getArguments();
		projectId = values.getInt(ScreenProvider.KEY_ID);
		
		
		return root; 
	}
	
	private void setupDatabaseConnection()
	{
		manager = getLoaderManager();
		adapter = new SectionAdapter(getActivity(), null, true, SectionAdapter.TYPE_SCREENFLOW);
		
		manager.initLoader(ScreenProvider.LOADER_ID_SCREENFLOWER, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + String.valueOf(projectId);
		return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
		adapter.swapCursor(arg1);
		adapter.notifyDataSetChanged();
		Log.d("cursor size in screenflow", String.valueOf(arg1.getCount()));
		screenList.setAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View screen, int arg2,
			long arg3) {
		
		return true;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public interface onScreenFlowObjectRequest
	{
		View addScreen(Bundle values);
	}

	private static onScreenFlowObjectRequest requestListener;

	public static void setOnScreenRequestedListener(
			onScreenFlowObjectRequest listener)
	{
		ScreenFlowScreensFragment.requestListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View screen, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
Log.d("list", "lonclicked");
		
		View screenView = requestListener.addScreen((Bundle) screen.getTag());
		
		
		ClipData.Item item = new ClipData.Item("screen");
		
		ClipData clipData = new ClipData("hello", new String[]
		{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

		screenView.startDrag(clipData, new View.DragShadowBuilder(screenView), screenView, 0);
		
		ViewGroup parent = (ViewGroup) screenView.getParent();
		//parent.removeView(screenView);
		
	}
}
