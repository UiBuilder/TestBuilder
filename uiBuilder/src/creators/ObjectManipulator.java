package creators;

import helpers.CollisionChecker;
import helpers.GridSnapper;
import helpers.InBoundsChecker;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;


/**
 * Used to manipulate object properties such as position and size.
 * To keep view sizes and positions in bounds of the designarea and aligned with the grid,
 * which is displayed on the designarea to simplify repositions, the ObjectManipulator 
 * uses instances of the inBoundsChecker and CollisionChecker and static accesses to the
 * GridSnapper's snapToGrid method.
 * @author funklos
 *
 */
public class ObjectManipulator
{

	private RelativeLayout designArea;
	private InBoundsChecker boundsCheck;
	private CollisionChecker collChecker;
	
	public ObjectManipulator(RelativeLayout designArea)
	{
		this.designArea = designArea;
		this.boundsCheck = new InBoundsChecker(designArea);
		this.collChecker = new CollisionChecker(designArea);
	}
	
	/**
	 * The method is called after a view has been generated for the first time.
	 * The supplied event represents the users target for instantiation. This target
	 * position has to be checked for collisions with the designArea, to keep the
	 * generated view in bounds.
	 * 
	 * This method calculates an appropriate position, which has to be in 
	 * bounds of the designArea, and rounded to the nearest grid column.
	 * 
	 * @param event
	 * @return the calculated params, which keep the view in bounds and follow the grid columns.
	 */
	protected RelativeLayout.LayoutParams setInitialPosition(MotionEvent event, View newItem)
	{
		Bundle tag = (Bundle) newItem.getTag();
		int defaultWidth = tag.getInt(ObjectValues.DEFAULT_WIDTH);
		int defaultHeight = tag.getInt(ObjectValues.DEFAULT_HEIGHT);
		
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		int targetX = collChecker.collisionX(clickPosX, defaultWidth);
		int targetY = collChecker.collisionY(clickPosY, defaultHeight);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newItem.getLayoutParams();

		params.leftMargin = GridSnapper.snapToGrid(targetX);
		params.topMargin = GridSnapper.snapToGrid(targetY);
		return params;
	}
	
	/**
	 * This method resizes the item and repositions it appropriately.
	 * 
	 * @author funklos
	 * @param handleId the handle which started the scaling
	 * @param start the starting point of the scaling process
	 * @param now the actual position of the scale movement
	 */
	public void setParams(int handleId, MotionEvent start, MotionEvent now, View activeItem, View drag)
	{	
		RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams(); 
		RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams(); 

		float distance;
		int roundedDist;
		Bundle itemTag = (Bundle) activeItem.getTag();

		switch (handleId)
		{
		case R.id.overlay_right:
			distance = now.getX() - start.getX();

			if (boundsCheck.checkMinSize(dragParams, distance, itemTag, R.id.overlay_right))
			{
				roundedDist = boundsCheck.checkMaxSize(distance, R.id.overlay_right, activeItem);
				roundedDist = GridSnapper.snapToGrid(roundedDist);

				itemParams.width = dragParams.width = activeItem.getMeasuredWidth()
						+ roundedDist;
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			}
			break;

		case R.id.overlay_left:
			distance = start.getX() - now.getX();

			if (boundsCheck.checkMinSize(dragParams, distance, itemTag, R.id.overlay_left))
			{
				roundedDist = boundsCheck.checkMaxSize(distance, R.id.overlay_left, activeItem);
				roundedDist = GridSnapper.snapToGrid(roundedDist);

				dragParams.leftMargin = drag.getLeft() - roundedDist;
				itemParams.leftMargin = activeItem.getLeft() - roundedDist;

				itemParams.width = dragParams.width = activeItem.getMeasuredWidth()
						+ roundedDist;
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			}
			break;

		case R.id.overlay_bottom:
			distance = now.getY() - start.getY();

			if (boundsCheck.checkMinSize(dragParams, distance, itemTag, R.id.overlay_bottom))
			{
				roundedDist = boundsCheck.checkMaxSize(distance, R.id.overlay_bottom, activeItem);
				roundedDist = GridSnapper.snapToGrid(roundedDist);

				itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight()
						+ roundedDist;
			}
			break;

		case R.id.overlay_top:
			distance = start.getY() - now.getY();

			if (boundsCheck.checkMinSize(dragParams, distance, itemTag, R.id.overlay_top))
			{
				roundedDist = boundsCheck.checkMaxSize(distance, R.id.overlay_top, activeItem);
				roundedDist = GridSnapper.snapToGrid(roundedDist);

				dragParams.topMargin = drag.getTop() - roundedDist;
				itemParams.topMargin = activeItem.getTop() - roundedDist;

				itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight()
						+ roundedDist;
			}
			break;

		default:
			break;
		}
		drag.setLayoutParams(dragParams);
		activeItem.setLayoutParams(itemParams);
	}

	/**
	 * sets the appropriate style to the item being dragged. check if activeitem
	 * == null because a drop can result in a delete operation. without
	 * syncronization the drag started and drag ended styles are sometimes not
	 * set.
	 * 
	 * @author funklos
	 * @param event
	 */
	public void setStyle(int event, final View activeItem)
	{
		if (activeItem != null)
		// synchronized (activeItem)
		{
			switch (event)
			{
			case DragEvent.ACTION_DRAG_STARTED:

				activeItem.post(new Runnable()
				{

					@Override
					public void run()
					{
						activeItem.setBackgroundResource(R.drawable.element_dragging);
					}
				});

				break;
			case DragEvent.ACTION_DRAG_ENTERED:

				activeItem.setBackgroundResource(R.drawable.element_dragging);
				break;

			case DragEvent.ACTION_DRAG_ENDED:
			case DragEvent.ACTION_DROP:
				activeItem.post(new Runnable()
				{

					@Override
					public void run()
					{
						activeItem.setBackgroundResource(R.drawable.object_background_default);
					}
				});
				break;

			case DragEvent.ACTION_DRAG_EXITED:

				activeItem.setBackgroundResource(R.drawable.element_out_of_dropzone);
				break;

			default:
				break;
			}
		}
		designArea.invalidate();
	}
	
	/**
	 * A drop operation has returned true, but the position has to be checked
	 * for collisions and rounded to the nearest grid value.
	 * Repositions the drag overlay accordingly.
	 * 
	 * @param event the drop event
	 * @param activeItem the item in progress
	 * @param drag the drag element, representing the overlay
	 */
	public void performDrop(DragEvent event, View activeItem, ImageButton drag)
	{
		int itemWidth = activeItem.getMeasuredWidth();
		int itemHeight = activeItem.getMeasuredHeight();
		
		int dropTargetX = collChecker.collisionX(event.getX(), itemWidth);
		int dropTargetY = collChecker.collisionY(event.getY(), itemHeight);

		setDragParams(activeItem, drag, dropTargetX, dropTargetY);
		setActiveItemParams(activeItem, dropTargetX, dropTargetY);
	}
	
	/**
	 * Repositions the active item with rounded values.
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
	 * Repositions the drag element according to the active items position.
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
