package helpers;

import data.ObjectValues;
import android.os.Bundle;
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
	public int collisionX(float dropPosX, int width)
	{
		//Bundle b = (Bundle) item.getTag();
		//int defaultWidth = b.getInt(ObjectValues.DEFAULT_WIDTH);
		
		
		int offsetPos = Math.round(dropPosX - width / 2);

		int maxPos = Math.round(designArea.getMeasuredWidth() - width);
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
	public int collisionY(float dropPosY, int height)
	{

		
		int offsetPos = Math.round(dropPosY - height / 2);

		int maxPos = Math.round(designArea.getMeasuredHeight() - height);
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
}
