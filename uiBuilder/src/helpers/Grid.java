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
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		int xpos = width/interval;
		int ypos = height/interval;
		
		for (int x = 0; x < width; x += interval)
		{
            canvas.drawLine(x, 0, x, height, color);
            canvas.drawLine(0, x, width, x, color);
        }
		
		int x = 0;
		/*
		while (x <= width)
		{
			canvas.drawLine(xpos +(xpos*x), 0, xpos +(xpos*x), height, color);
            canvas.drawLine(0, (xpos*x), width, (xpos*x), color);
		}*/
    }
	
		
	

	public Grid(Context context, int interval)
	{
		super(context);
		this.interval = interval;
		color = new Paint();
		color.setColor(context.getResources().getColor(R.color.designfragment_background));
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
