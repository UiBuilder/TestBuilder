package helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import de.ur.rk.uibuilder.R;

public class Grid extends View
{
	private Paint color;
	private int interval;
	private boolean showGrid;
	
	private int displayWidth;
	private int displayHeight;
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		//if (showGrid)
		{
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
    }
	
	public void showGrid(boolean on)
	{
		showGrid = on;
		Log.d("GRID", String.valueOf(showGrid));
	}
	
	public boolean getGridState()
	{
		return showGrid;
	}
	

	public Grid(Context context, int interval)
	{
		super(context);
		this.interval = interval;
		color = new Paint();
		color.setColor(context.getResources().getColor(R.color.designarea_grid));
		
		showGrid = false;
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
