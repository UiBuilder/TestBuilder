package helpers;

import creators.ObjectFactory;
import android.view.View;
import android.widget.RelativeLayout;

public class CollisionChecker
{
	private RelativeLayout designArea;
	
	public CollisionChecker(RelativeLayout designArea)
	{
		this.designArea = designArea;
	}
	
	/**
	 * Checks if the target of the drop action is within view bounds
	 * 
	 * @author funklos
	 * @param float x of event
	 * @return calculated X-position of the performed drop
	 */
	public int collisionX(float dropPosX, View item)
	{
		int offsetPos = Math.round(dropPosX - item.getMeasuredWidth() / 2);

		int maxPos = Math.round(designArea.getMeasuredWidth() - item.getMeasuredWidth());
		int minPos = 0;

		if (offsetPos <= minPos)
		{
			return minPos;
		}
		if (offsetPos >= maxPos)
		{
			return maxPos;
		}
		return offsetPos;
	}
	
	/**
	 * Checks if the target of the drop action is within view bounds
	 * 
	 * @author funklos
	 * @param float y of event
	 * @return calculated Y-position of the performed drop
	 */
	public int collisionY(float dropPosY, View item)
	{
		int offsetPos = Math.round(dropPosY - item.getMeasuredHeight()
				/ 2);

		int maxPos = Math.round(designArea.getMeasuredHeight()
				- item.getMeasuredHeight());
		int minPos = 0;

		if (offsetPos <= minPos)
		{
			return minPos;
		}
		if (offsetPos >= maxPos)
		{
			return maxPos;
		}
		return offsetPos;
	}
	
	
	
	/**
	 * round the provided value to meet the next gridvalue
	 * 
	 * @param value
	 * @return
	 */
	public static int snapToGrid(int value)
	{
		return Math.round((float) value / ObjectFactory.SNAP_GRID_INTERVAL)
				* ObjectFactory.SNAP_GRID_INTERVAL;
	}
}
