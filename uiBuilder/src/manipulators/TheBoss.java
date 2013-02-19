package manipulators;

import java.sql.Timestamp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
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

	private static final float DRAG_THRESHOLD = 50;
	private static final float FLING_DISTANCE = 300f;
	private static final int MAX_TIME = 200;

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

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			start = event;
			timeStart.setTime(System.currentTimeMillis());

			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			timeEnd.setTime(System.currentTimeMillis());
			if (isFling(start, event, timeStart, timeEnd))
			{
				this.root.removeView(activeItem);
			}
			break;
		case DragEvent.ACTION_DROP:

			timeEnd.setTime(System.currentTimeMillis());

			if (!isDrag(event))
			{
				Toast.makeText(context.getApplicationContext(),
						"Button " + activeItem.getId() + " is not dragged",
						Toast.LENGTH_SHORT).show();
			} else if (isFling(start, event, timeStart, timeEnd))
			{
				// What to do if fling-gesture was identified
				this.root.removeView(activeItem);

			} else
			{
				// Drop-Action

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
						.getLayoutParams();

				params.leftMargin = (int) event.getX()
						- (activeItem.getWidth() / 2);
				params.topMargin = (int) event.getY()
						- (activeItem.getHeight() / 2);

				root.requestLayout();

				// this.root.invalidate();

			}

			activeItem = null;
			break;
		}
		return true;
	}

	private boolean isDrag(DragEvent event)
	{
		return (Math.abs(start.getX() - event.getX()) > DRAG_THRESHOLD)
				|| (Math.abs(start.getY() - event.getY()) > DRAG_THRESHOLD);
	}

	private boolean isFling(DragEvent start, DragEvent end,
			Timestamp timeStart2, Timestamp timeEnd2)
	{

		if (Math.abs(start.getX() - end.getX()) >= FLING_DISTANCE
				|| Math.abs(start.getY() - end.getY()) >= FLING_DISTANCE)
		{
			if (timeEnd2.getTime() - timeStart2.getTime() <= MAX_TIME)
			{
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

		Button companion = new Button(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_TOP, v.getId());
		params.addRule(RelativeLayout.RIGHT_OF, v.getId());

		companion.setId(4444);
		companion.setLayoutParams(params);

		companion.setText("Companion");
		root.addView(companion, params);
		// companion.setX(100);
		// companion.setY(100);
		Log.d("OnLongclick",
				"Wurde Ausgeführt und nach Compantion aufruf geschafft");
		v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);

		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{

			// holt die Koordinaten des Touch-Punktes
			float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
			float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

			if (v instanceof RelativeLayout)
			{
				// erstellt den Button an den zuvor ermittelten Koordinaten
				Button newOne = (Button) factory
						.getElement(ObjectFactory.ID_BUTTON);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne
						.getLayoutParams();

				params.leftMargin = (int) clickPosX;
				params.topMargin = (int) clickPosY;
				root.addView(newOne, params);

				/*
				 * // DOES NOT WORK newOne.setX(clickPosX - (newOne.getWidth() /
				 * 2)); newOne.setY(clickPosY - (newOne.getHeight() / 2));
				 * return true;
				 */
			}

		}
		return true;
	}

}
