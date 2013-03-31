package creators;

import helpers.CollisionChecker;
import helpers.GridSnapper;
import helpers.ImageTools;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import creators.ReGenerator.OnObjectGeneratedListener;
import data.FromDatabaseObjectCreator;
import data.FromDatabaseObjectCreator.OnObjectLoadedFromDatabaseListener;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

public class ObjectFactory implements OnObjectLoadedFromDatabaseListener, OnObjectGeneratedListener
{
	public static final int SNAP_GRID_INTERVAL = 15;
	
	private Context ref;
	private Generator generator;
	private ReGenerator reGenerator;
	
	private CollisionChecker checker;
	private ObjectManipulator manipulator;
	
	private View newItem;
	
	private RelativeLayout designArea;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c the context in which the factory resides
	 * @param listener the event listener the factory provides for new objects. this is passed to the generator to generate touchable objects
	 * @param designArea 
	 */
	public ObjectFactory(Context c, OnTouchListener listener, RelativeLayout designArea)
	{
		ref = c;
		this.designArea = designArea;
		
		generator = new Generator(ref, listener);
		
		checker = new CollisionChecker(designArea);
		manipulator = new ObjectManipulator(c, designArea);
		
		FromDatabaseObjectCreator.setOnObjectCreatedFromDatabaseListener(this);
		ReGenerator.setOnObjectGeneratedListener(this);
	}
	
	@Override
	public void objectsLoaded(ArrayList<Bundle> objectList)
	{
		getElements(objectList);
	}

	@Override
	public void objectGenerated(View newItem)
	{
		designArea.addView(newItem, newItem.getLayoutParams());
		newItem.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Bundle bundle = (Bundle) newItem.getTag();
		Log.d("object generated", "get bundle");
		switch (bundle.getInt(ObjectValues.TYPE))
		{
		case R.id.element_imageview:
			
			String source = bundle.getString(ObjectValues.IMG_SRC);
			ImageTools.setPic(newItem, source);
			break;

		default:
			break;
		}	
	}
	
	
	/**
	 * called to create new objects
	 * @param which the requested object type       
	 * @return the generated object
	 */
	public View getElement(int which, MotionEvent event)
	{
		try
		{
			newItem = generator.generate(which);
			
			// holt die Koordinaten des Touch-Punktes
			RelativeLayout.LayoutParams params = setPosition(event);

			designArea.addView(newItem, params);
			
			return newItem;
			
		} catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			Log.d(LOGTAG, "id ist " + String.valueOf(which));
			return null;
		}
	}
	
	/**
	 * called to re-generate objects from database
	 * @param objectList
	 * @return
	 */
	public void getElements(ArrayList<Bundle> objectList)
	{
		try
		{
			reGenerator = new ReGenerator(generator);
			reGenerator.execute(objectList);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void requestStyle(int which, View activeItem)
	{
		manipulator.setStyle(which, activeItem);
	}
	
	public void requestResize(int which, MotionEvent start, MotionEvent end, View activeItem, ImageButton dragHandle)
	{
		manipulator.setParams(which, start, end, activeItem, dragHandle);
	}
	
	/**
	 * @param event
	 * @return
	 */
	private RelativeLayout.LayoutParams setPosition(MotionEvent event)
	{
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		int targetX = checker.collisionX(clickPosX, newItem);
		int targetY = checker.collisionY(clickPosY, newItem);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newItem.getLayoutParams();

		params.leftMargin = GridSnapper.snapToGrid(targetX);
		params.topMargin = GridSnapper.snapToGrid(targetY);
		return params;
	}
	
	/**
	 * get the coordinates of the drop event
	 * repositions:
	 * the active item and the overlay
	 * @param event the drop event
	 */
	public void performDrop(DragEvent event, View activeItem, ImageButton drag)
	{
		int dropTargetX = checker.collisionX(event.getX(), activeItem);
		int dropTargetY = checker.collisionY(event.getY(), activeItem);

		setDragParams(activeItem, drag, dropTargetX, dropTargetY);
		setActiveItemParams(activeItem, dropTargetX, dropTargetY);
	}

	/**
	 * @param activeItem
	 * @param dropTargetX
	 * @param dropTargetY
	 */
	private void setActiveItemParams(View activeItem, int dropTargetX,
			int dropTargetY)
	{
		RelativeLayout.LayoutParams activeParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
		
		activeParams.leftMargin = GridSnapper.snapToGrid(dropTargetX);
		activeParams.topMargin = GridSnapper.snapToGrid(dropTargetY);
		activeItem.setLayoutParams(activeParams);
	}

	/**
	 * @param activeItem
	 * @param drag
	 * @param dropTargetX
	 * @param dropTargetY
	 * @return
	 */
	private void setDragParams(View activeItem,
			ImageButton drag, int dropTargetX, int dropTargetY)
	{
		RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();

		dragParams.leftMargin = GridSnapper.snapToGrid(dropTargetX) + designArea.getLeft();
		dragParams.topMargin = GridSnapper.snapToGrid(dropTargetY) + designArea.getTop();
		dragParams.width = activeItem.getMeasuredWidth();
		dragParams.height = activeItem.getMeasuredHeight();
		drag.setLayoutParams(dragParams);
	}

}
