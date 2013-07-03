package uxcomponents;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import data.ScreenProvider;
import data.SectionAdapter;
import de.ur.rk.uibuilder.R;

public class ScreenFlowScreensFragment extends Fragment implements LoaderCallbacks<Cursor>, OnItemLongClickListener, OnDragListener
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
		}
		Bundle values = getArguments();
		values.getInt(ScreenProvider.KEY_ID);
		
		setupDatabaseConnection();
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
		String selection = ScreenProvider.KEY_SECTION_ASSOCIATED_PROJECT + " = " + "'" + String.valueOf(projectId) + "'";
		
		return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_SECTIONS, null, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
		adapter.swapCursor(arg1);
		adapter.notifyDataSetChanged();
		
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
		
		View screenView = inflater.inflate(R.layout.screen_list_item, null);
		//screenView.startDrag(data, shadowBuilder, myLocalState, flags)
		return false;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
