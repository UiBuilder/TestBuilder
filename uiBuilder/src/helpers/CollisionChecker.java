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
	public int collisionX(float dropPosX, View item)
	{
		Bundle b = (Bundle) item.getTag();
		int defaultWidth = b.getInt(ObjectValues.DEFAULT_WIDTH);
		
		
		int offsetPos = Math.round(dropPosX - defaultWidth / 2);

		int maxPos = Math.round(designArea.getMeasuredWidth() - defaultWidth);
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
		Bundle b = (Bundle) item.getTag();
		int defaultHeight = b.getInt(ObjectValues.DEFAULT_HEIGHT);
		
		int offsetPos = Math.round(dropPosY - defaultHeight / 2);

		int maxPos = Math.round(designArea.getMeasuredHeight() - defaultHeight);
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
