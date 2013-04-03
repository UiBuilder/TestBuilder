package helpers;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * Checks whether resize operations are in bounds of the designarea
 * @author funklos
 *
 */
public class InBoundsChecker
{
	private RelativeLayout designArea;
	
	public InBoundsChecker(RelativeLayout designArea)
	{
		this.designArea = designArea;
	}

	/**
	 * compares the actual size to the minsizes provided by the tagbundle. this
	 * is necessary because .getminwidth and getminheight methods are only
	 * provided by api16 and up
	 * 
	 * @author funklos
	 * @param params the params of the active item to compare against the min dimensions provided by the tag
	 * @param distance the distance of the actual move event
	 * @param itemTag tag to fetch min dimensions from
	 * @param which discriminates resizing direction
	 * @return true if further resizing is possible, false if the size restriction has been met
	 */
	public boolean checkMinSize(RelativeLayout.LayoutParams params,
			float distance, Bundle itemTag, int which)
	{

		int minWidth = itemTag.getInt(ObjectValues.MINWIDTH);
		int minHeight = itemTag.getInt(ObjectValues.MINHEIGHT);

		switch (which)
		{
		case R.id.overlay_right:
		case R.id.overlay_left:

			return params.width >= minWidth
					&& !(params.width + distance < minWidth);

		case R.id.overlay_bottom:
		case R.id.overlay_top:

			return params.height >= minHeight
					&& !(params.height + distance < minHeight);

		default:

		}
		return false;
	}

	/**
	 * Checks whether the current resize in progress will be larger than the
	 * workspace.
	 * 
	 * @author funklos
	 * @param distance
	 *            the current distance moved from the origin of the movement
	 * @param which
	 *            is the id of the overlay element that initiated the scale
	 *            action
	 * @return either the rounded distance when the scaling was in the workspace
	 *         area, or a <b>restricted to workspace</b> distance.
	 */
	public int checkMaxSize(float distance, int which, View activeItem)
	{
		switch (which)
		{
		case R.id.overlay_right:

			if (activeItem.getRight() + distance >= designArea.getWidth())
			{
				float overHead = (activeItem.getRight() + distance - designArea.getWidth());

				return Math.round(distance - overHead);
			}
			break;

		case R.id.overlay_left:

			if (activeItem.getLeft() - distance <= 0)
			{
				float overHead = (activeItem.getLeft() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case R.id.overlay_top:

			if (activeItem.getTop() - distance <= 0)
			{
				float overHead = (activeItem.getTop() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case R.id.overlay_bottom:

			if (activeItem.getBottom() + distance >= designArea.getHeight())
			{
				float overHead = (activeItem.getBottom()
						- designArea.getHeight() + distance);

				return Math.round(distance - overHead);
			}

		default:
			break;
		}
		return Math.round(distance);
	}
}
