package manipulators;

import java.sql.Timestamp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import creators.ObjectFactory;

public class TheBoss implements OnDragListener, OnGestureListener,
		OnTouchListener
{

	private Context context;
	private View activeItem;
	private ObjectFactory factory;
	private RelativeLayout root;

	private GestureDetector detector;
	private final String OVERLAYTAG = "Overlay";
	private final String OBJECTTAG = "Object";

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

		detector = new GestureDetector(context, this);

		activeItem = null;
	}

	private View dragIndicator;

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (v instanceof RelativeLayout) {
			detector.setIsLongpressEnabled(false);
			activeItem = null;
			removeOverlay();

		}
		if (v.getTag() == OBJECTTAG) {
			detector.setIsLongpressEnabled(true);
			dragIndicator = null;
			removeOverlay();
			activeItem = v;
		}
		if (v.getTag() == OVERLAYTAG) {
			dragIndicator = v;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			break;

		}
		return detector.onTouchEvent(event); // MUSS SO AUFGERUFEN WERDEN
	}

	@Override
	public boolean onDown(MotionEvent event)
	{
		// holt die Koordinaten des Touch-Punktes
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		if (activeItem == null && dragIndicator == null) {
			// erstellt den Button an den zuvor ermittelten Koordinaten
			Button newOne = (Button) factory
					.getElement(ObjectFactory.ID_BUTTON);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne
					.getLayoutParams();

			params.leftMargin = (int) clickPosX;
			params.topMargin = (int) clickPosY;
			root.addView(newOne, params);
			root.requestLayout();
		}
		if (activeItem == null) {
			removeOverlay();
			dragIndicator = null;
		}

		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		if (dragIndicator != null) {
			this.root.removeView(activeItem);
			removeOverlay();

		}
		Toast.makeText(context.getApplicationContext(), "fling",
				Toast.LENGTH_SHORT).show();

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

		Log.d("OnLongpress", "is called");

		if (activeItem.getTag() == OBJECTTAG) {
			Toast.makeText(context.getApplicationContext(),
					"Button " + activeItem.getId() + " is longclicked",
					Toast.LENGTH_SHORT).show();
			setOverlay();

			activeItem.setAlpha(0.5f);
			// ClipData.Item item = new ClipData.Item((String)
			// activeItem.getTag());
			// ClipData clipData = new ClipData(
			// (CharSequence) activeItem.getTag(),
			// new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

			// RelativeLayout overlay = factory.getOverlay(activeItem);

			// root.addView(overlay);
			root.requestLayout();

			// Log.d("OnLongclick", String.valueOf(overlay.getWidth()));

			// activeItem.startDrag(clipData, new View.DragShadowBuilder(
			// activeItem), null, 0);
		}

	}

	// TEMPORÄR
	private Button drag;
	private Button left;
	private Button right;
	private Button bottom;
	private Button top;

	private void setOverlay()
	{

		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(
				activeItem.getLayoutParams());
		int dragId = 1111;
		Log.d("params left", String.valueOf(modified.leftMargin));

		// DRAG
		drag = new Button(context);
		modified.leftMargin = activeItem.getLeft();
		modified.topMargin = activeItem.getTop();
		modified.width = activeItem.getMeasuredWidth();
		drag.setBackgroundResource(android.R.color.background_dark);
		drag.setAlpha(0.5f);
		drag.setId(dragId);
		drag.setTag(OVERLAYTAG);
		drag.setOnTouchListener(this);
		root.addView(drag, modified);
		// root.requestLayout();

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// RIGHT
		right = new Button(context);
		right.setBackgroundResource(android.R.color.holo_orange_light);
		right.setAlpha(0.5f);
		modified.addRule(RelativeLayout.ALIGN_TOP, dragId);
		modified.addRule(RelativeLayout.RIGHT_OF, dragId);
		modified.addRule(RelativeLayout.ALIGN_BOTTOM, dragId);
		right.setId(1112);
		right.setTag(OVERLAYTAG);
		root.addView(right, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// BOTTOM
		bottom = new Button(context);
		bottom.setBackgroundResource(android.R.color.holo_orange_light);
		bottom.setAlpha(0.5f);
		modified.addRule(RelativeLayout.BELOW, dragId);
		modified.addRule(RelativeLayout.ALIGN_LEFT, dragId);
		modified.addRule(RelativeLayout.ALIGN_RIGHT, dragId);
		bottom.setId(1113);
		bottom.setTag(OVERLAYTAG);
		root.addView(bottom, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// LEFT
		left = new Button(context);
		left.setBackgroundResource(android.R.color.holo_orange_light);
		left.setAlpha(0.5f);
		modified.addRule(RelativeLayout.LEFT_OF, bottom.getId());
		modified.addRule(RelativeLayout.ALIGN_TOP, right.getId());
		modified.addRule(RelativeLayout.ABOVE, bottom.getId());
		left.setId(1114);
		left.setTag(OVERLAYTAG);
		root.addView(left, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// TOP
		top = new Button(context);
		top.setBackgroundResource(android.R.color.holo_orange_light);
		top.setAlpha(0.5f);
		modified.addRule(RelativeLayout.ABOVE, right.getId());
		modified.addRule(RelativeLayout.LEFT_OF, right.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, left.getId());
		top.setTag(OVERLAYTAG);
		root.addView(top, modified);

		root.requestLayout();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		if (activeItem != null && dragIndicator != null) {
			Log.d("onScroll", "wurde Aufgerufen");
			ClipData.Item item = new ClipData.Item((String) activeItem.getTag());
			ClipData clipData = new ClipData(
					(CharSequence) activeItem.getTag(),
					new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
			activeItem.startDrag(clipData, new View.DragShadowBuilder(
					activeItem), null, 0);
		}
		/*
		 * RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
		 * activeItem .getLayoutParams();
		 * 
		 * params.leftMargin += (int) e2.getX(); params.topMargin += (int)
		 * e2.getY();
		 * 
		 * activeItem.setLayoutParams(params); root.requestLayout();
		 * 
		 * Log.d("leftmargin", String.valueOf(params.leftMargin));
		 */
		return false;
	}

	// UNUSED
	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDrag(View root, DragEvent event)
	{

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			setVisibility(false);
			root.requestLayout();
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;

		case DragEvent.ACTION_DRAG_EXITED:

			break;
		case DragEvent.ACTION_DROP:

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
					.getLayoutParams();

			params.leftMargin = (int) event.getX();
			params.topMargin = (int) event.getY();
			activeItem.setLayoutParams(params);
			dragIndicator.setLayoutParams(params);

			break;
		case DragEvent.ACTION_DRAG_ENDED:
			root.requestLayout();
			setVisibility(true);

			break;
		}
		return true;
	}

	private void setVisibility(boolean visible)
	{

		if (visible && drag != null) {
			drag.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.VISIBLE);
			left.setVisibility(View.VISIBLE);
			right.setVisibility(View.VISIBLE);
			root.requestLayout();
		} else if (drag != null) {

			drag.setVisibility(View.INVISIBLE);
			top.setVisibility(View.INVISIBLE);
			bottom.setVisibility(View.INVISIBLE);
			left.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
			root.requestLayout();
		}
	}

	private void removeOverlay()
	{
		

			int itemCount = root.getChildCount();
			for (int i = 0; i < itemCount; i++) {
				if (root.getChildAt(i) != null
						&& root.getChildAt(i).getTag() == OVERLAYTAG) {
					root.removeView(root.getChildAt(i));
				
			}
		}
	}

}
