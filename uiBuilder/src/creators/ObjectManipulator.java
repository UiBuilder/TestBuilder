package creators;

import helpers.GridSnapper;
import helpers.InBoundsChecker;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ObjectManipulator
{
	private Context context;
	private RelativeLayout designArea;
	private InBoundsChecker boundsCheck;
	
	public ObjectManipulator(Context context, RelativeLayout designArea)
	{
		this.context = context;
		this.designArea = designArea;
		this.boundsCheck = new InBoundsChecker(designArea);
	}
	
	/**
	 * This method resizes the item and repositions it appropriately.
	 * 
	 * @author funklos
	 * @param handleId
	 *            the handle which started the scaling
	 * @param start
	 *            the starting point of the scaling process
	 * @param now
	 *            the actual position of the scale movement
	 */
	protected void setParams(int handleId, MotionEvent start, MotionEvent now, View activeItem, View drag)
	{
		// these params are essentially the same regarding size and position but are handledseparate
		
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
	protected void setStyle(int event, final View activeItem)
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
						activeItem.setBackgroundResource(((Bundle) activeItem.getTag()).getInt(ObjectValues.BACKGROUND_EDIT));
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
	}
}
