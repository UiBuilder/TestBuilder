package uxcomponents;

import helpers.Log;

import java.util.ArrayList;

import uxcomponents.ScreenFlowScreensFragment.onScreenFlowObjectRequest;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ScreenFlowDesignFragment extends Fragment implements onScreenFlowObjectRequest, OnLongClickListener, OnDragListener
{
	private View root;
	private RelativeLayout relativeRoot;
	private RelativeLayout designArea;
	
	private ArrayList<View> screens;
	private int projectId;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		screens = new ArrayList<View>();
		
		loadScreens();
	}

	private void loadScreens()
	{
		ContentResolver resolver = getActivity().getContentResolver();
		String selection = ScreenProvider.KEY_FLOW_PROJECT_ID + " = " + String.valueOf(projectId);
		Cursor c = resolver.query(ScreenProvider.CONTENT_URI_FLOW, null, selection, null, null);
		
		int xIdx = c.getColumnIndexOrThrow(ScreenProvider.KEY_FLOW_X);
		int yIdx = c.getColumnIndexOrThrow(ScreenProvider.KEY_FLOW_Y);
		int labelIdx = c.getColumnIndexOrThrow(ScreenProvider.KEY_FLOW_LABEL);
		int projectIdx = c.getColumnIndexOrThrow(ScreenProvider.KEY_FLOW_PROJECT_ID);
		int idIdx = c.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
		
		while (c.moveToNext())
		{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = c.getInt(xIdx);
			params.topMargin = c.getInt(yIdx);
			
			Log.d("x", String.valueOf(c.getInt(xIdx)));
			
			View screen = getActivity().getLayoutInflater().inflate(R.layout.screen_list_item_flow, null);
			screen.setLayoutParams(params);
			
			screen.setOnLongClickListener(this);
			
			String screenName = c.getString(labelIdx);
			TextView label = (TextView) screen.findViewById(R.id.screenname);
			label.setText(screenName);
			
			relativeRoot.addView(screen);
			screens.add(screen);
			screen.setTag(c.getInt(idIdx));
		}
		c.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (root == null)
		{
			Bundle intentBundle = getArguments();
			projectId = intentBundle.getInt(ScreenProvider.KEY_ID);
			
			root = inflater.inflate(R.layout.layout_project_screenflow_design, null);
			relativeRoot = (RelativeLayout) root;
			ScreenFlowScreensFragment.setOnScreenRequestedListener(this);
			designArea = (RelativeLayout) root.findViewById(R.id.screenflow_area);
			designArea.setOnDragListener(this);
		}
		
		return root; 
	}

	public ArrayList<View> getFlow()
	{
		return screens;
	}

	@Override
	public View addScreen(Bundle values)
	{
		//Button button = new Button(getActivity());
		View screen = getActivity().getLayoutInflater().inflate(R.layout.screen_list_item_flow, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 100;
		params.topMargin = 100;
		screen.setLayoutParams(params);
		
		Log.d("addscreen ", "called");
		//screen.setScaleX(0.6f);
		//screen.setScaleY(0.6f);
		screen.setOnLongClickListener(this);
		
		String screenName = values.getString(ScreenProvider.KEY_SECTION_NAME);
		TextView label = (TextView) screen.findViewById(R.id.screenname);
		label.setText(screenName);
		
		relativeRoot.addView(screen);
		//screen.setVisibility(View.INVISIBLE);
		
		screens.add(screen);
		return screen;
	}

	@Override
	public boolean onLongClick(View arg0)
	{
		ClipData.Item item = new ClipData.Item("screen");
		
		ClipData clipData = new ClipData("hello", new String[]
		{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

		arg0.startDrag(clipData, new View.DragShadowBuilder(arg0), arg0, 0);
		relativeRoot.removeView(arg0);
		
		return true;
	}

	@Override
	public boolean onDrag(View v, DragEvent event)
	{
		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED: 
			Log.d("drag", "registered");
			break;
			
		case DragEvent.ACTION_DRAG_ENTERED:
				
			break;

		case DragEvent.ACTION_DRAG_LOCATION:
			break;

		case DragEvent.ACTION_DRAG_ENDED:
			
			break;

		case DragEvent.ACTION_DRAG_EXITED: 
			
			break;

		case DragEvent.ACTION_DROP: 
			// check minpositions, hide grid, display overlay at new position and reposition the element at droptarget
			
			//check if new object generated
			ClipData.Item item = event.getClipData().getItemAt(0);
			
			//if (item.getText().equals(ItemboxFragment.DRAG_EVENT_ORIGIN_ITEMBOX))
			{
				View screen = (View) event.getLocalState();
				screen.setVisibility(View.VISIBLE);
				
				int posX = Math.round(event.getX() - screen.getMeasuredWidth() / 2);
				int posY = Math.round(event.getY() - screen.getMeasuredHeight() / 2);
				RelativeLayout.LayoutParams activeParams = (RelativeLayout.LayoutParams) screen.getLayoutParams();
				
				activeParams.leftMargin = posX;
				activeParams.topMargin = posY;
				screen.setLayoutParams(activeParams);
				
				Log.d("passed", "drop");
				
				relativeRoot.addView(screen);
				relativeRoot.forceLayout();
				screen.forceLayout();
				/*
				newObjectEvent();
				
				activeItem = v;
				designArea.addView(v);
				overlay.generate(v);
				overlay.setVisibility(false);
				v.setVisibility(View.VISIBLE);
				
				manipulator.performDrop(event, v, overlay.getDrag());*/
				return true;
			}
			
			//manipulator.performDrop(event, activeItem, overlay.getDrag());
			//Log.d("action drop", "registered");
		
			//break;
		}
	
	return true;
	}
}
