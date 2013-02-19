package manipulators;

import java.sql.Timestamp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import creators.ObjectFactory;

public class TheBoss implements OnLongClickListener, OnDragListener,
		OnTouchListener
{

	private Context context;
	private View activeItem;
	private ObjectFactory factory;
	private RelativeLayout root;

	private DragEvent start;

	private Timestamp timeStart;
	private Timestamp timeEnd;
	private float objectCurrentPosX = -1;
	private float objectCurrentPosY = -1;

	private static final float DRAG_THRESHOLD = 10f;
	private static final float FLING_DISTANCE = 50.00f;
	private static final int MAX_TIME = 400;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param context
	 */
	public TheBoss(Context context, RelativeLayout root)
	{
		super();
		this.context = context;
		factory = new ObjectFactory(context, this);
		this.root = root;
		timeStart = new Timestamp(0);
		timeEnd = new Timestamp(0);
		activeItem = null;
	}

	@Override
	public boolean onDrag(View root, DragEvent event)
	{
		boolean isDragging = false;
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			start = event;
			timeStart.setTime(System.currentTimeMillis());
			
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			isDragging = isDrag(event);
			break;
		case DragEvent.ACTION_DRAG_ENDED:

			break;
		case DragEvent.ACTION_DRAG_EXITED:
			timeEnd.setTime(System.currentTimeMillis());
			if (isFling(start, event, timeStart, timeEnd)) {
				activeItem.setVisibility(View.GONE);
			}
			break;
		case DragEvent.ACTION_DROP:
			timeEnd.setTime(System.currentTimeMillis());

			if (isFling(start, event, timeStart, timeEnd)) {
				// What to do if fling-gesture was identified
				activeItem.setVisibility(View.GONE);
			} else if (isDragging) {
				
				// Drop-Action
				activeItem.setX(event.getX() - (activeItem.getWidth() / 2));
				activeItem.setY(event.getY() - (activeItem.getHeight() / 2));
			}else
			{Toast.makeText(context.getApplicationContext(),
					"Button " + activeItem.getId() + " is not dragged",
					Toast.LENGTH_SHORT).show();
			}
			objectCurrentPosX = -1;
			objectCurrentPosY = -1;
			activeItem = null;
			break;
		}
		return true;
	}

	private boolean isDrag(DragEvent event)
	{
		return (Math.abs(start.getX() - event.getX()) > DRAG_THRESHOLD)
				&& (Math.abs(start.getY() - event.getY()) > DRAG_THRESHOLD);
	}

	private boolean isFling(DragEvent start, DragEvent end,
			Timestamp timeStart2, Timestamp timeEnd2)
	{

		if (Math.abs(start.getX() - end.getX()) >= FLING_DISTANCE
				|| Math.abs(start.getY() - end.getY()) >= FLING_DISTANCE) {
			if (timeEnd2.getTime() - timeStart2.getTime() <= MAX_TIME) {
				Toast.makeText(context.getApplicationContext(),
						"Button " + activeItem.getId() + " is now in Orbit",
						Toast.LENGTH_SHORT).show();
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean onLongClick(View v)
	{
		activeItem = v;

		// Toast.makeText(context.getApplicationContext(), "Button " + v.getId()
		// + " is clicked", Toast.LENGTH_SHORT).show();

		
		ClipData.Item item = new ClipData.Item((String) v.getTag());
		ClipData clipData = new ClipData((CharSequence) v.getTag(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

		v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);
	

		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
	
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// holt die Koordinaten des Touch-Punktes
			float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
			float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

			if (v instanceof RelativeLayout) {
				// erstellt den Button an den zuvor ermittelten Koordinaten
				Button newOne = (Button) factory
						.getElement(ObjectFactory.ID_BUTTON);

				root.addView(newOne);

				// DOES NOT WORK
				newOne.setX(clickPosX - (newOne.getWidth() / 2));
				newOne.setY(clickPosY - (newOne.getHeight() / 2));
				return true;
			}

		}
		return true;
	}

}
