package creators;

import helpers.CollisionChecker;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ObjectFactory
{
	public static final int SNAP_GRID_INTERVAL = 15;
	
	private Context ref;
	private Generator generator;
	private CollisionChecker checker;
	
	private View newItem;
	
	private RelativeLayout designArea;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c
	 *            Referenz auf die Activity
	 */
	public ObjectFactory(Context c, OnTouchListener l, RelativeLayout designArea)
	{
		ref = c;
		this.designArea = designArea;
		
		generator = new Generator(ref, l, this);
		checker = new CollisionChecker(designArea);
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
	 * @param bundle
	 * @return
	 */
	public View getElement(Bundle bundle)
	{
		newItem = generator.generate(bundle);
		
		return newItem;
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

		params.leftMargin = CollisionChecker.snapToGrid(targetX);
		params.topMargin = CollisionChecker.snapToGrid(targetY);
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
		
		activeParams.leftMargin = CollisionChecker.snapToGrid(dropTargetX);
		activeParams.topMargin = CollisionChecker.snapToGrid(dropTargetY);
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

		dragParams.leftMargin = CollisionChecker.snapToGrid(dropTargetX) + designArea.getLeft();
		dragParams.topMargin = CollisionChecker.snapToGrid(dropTargetY) + designArea.getTop();
		dragParams.width = activeItem.getMeasuredWidth();
		dragParams.height = activeItem.getMeasuredHeight();
		drag.setLayoutParams(dragParams);
	}

}
