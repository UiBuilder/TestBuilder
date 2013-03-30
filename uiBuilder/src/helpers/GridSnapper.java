package helpers;

import creators.ObjectFactory;

public class GridSnapper
{
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