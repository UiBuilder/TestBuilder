package manipulators;

import helpers.Log;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import de.ur.rk.uibuilder.R;

/**
 * 
 * The Grid is a visual helper class, instantiated be the @see DesignFragment to support the user while he 
 * repositions items via drag and drop operations.
 * The Grid is only showing when a drag operation is in progress, else hidden.
 * 
 * The interval is defined by a constant passed in and also used by the @see GridSnapper class
 * 
 * @author funklos
 *
 */
public class Grid extends View
{
	private Paint color;
	private int interval;

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Log.d("grid is drawing", "cool");
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		for (int x = 0; x < width; x += interval)
		{
			canvas.drawLine(x, 0, x, height, color);
		}

		for (int y = 0; y < height; y += interval)
		{
			canvas.drawLine(0, y, width, y, color);
		}
	}

	public Grid(Context context, int interval)
	{
		super(context);
		this.interval = interval;

		color = new Paint();
		color.setColor(context.getResources().getColor(R.color.designarea_grid));
	}

	public Grid(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Grid(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
