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
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
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

	private DragEvent start;
	private GestureDetector detector;

	private boolean isDragging;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	private float downX = 0;
	private float downY = 0;

	
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
		detector = new GestureDetector(context, this);
		isDragging = false;

		activeItem = null;
	}

	/*
	 * @Override public boolean onLongClick(View v) {
	 * 
	 * activeItem = v;
	 * 
	 * // Toast.makeText(context.getApplicationContext(), "Button " + v.getId()
	 * // + " is clicked", Toast.LENGTH_SHORT).show();
	 * 
	 * ClipData.Item item = new ClipData.Item((String) v.getTag()); ClipData
	 * clipData = new ClipData((CharSequence) v.getTag(), new String[] {
	 * ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
	 * 
	 * //Button companion = new Button(context); //RelativeLayout.LayoutParams
	 * params = new RelativeLayout.LayoutParams( // LayoutParams.WRAP_CONTENT,
	 * LayoutParams.WRAP_CONTENT); //params.addRule(RelativeLayout.ALIGN_TOP,
	 * v.getId()); //params.addRule(RelativeLayout.RIGHT_OF, v.getId());
	 * 
	 * //companion.setLayoutParams(params);
	 * 
	 * //companion.setText("Companion"); RelativeLayout overlay =
	 * factory.getOverlay(activeItem);
	 * 
	 * root.addView(overlay); root.requestLayout();
	 * 
	 * Log.d("OnLongclick", String.valueOf(overlay.getWidth()));
	 * v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);
	 * 
	 * return true; }
	 */

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		
			activeItem = v;
		
			if(activeItem instanceof Button){
				detector.setIsLongpressEnabled(true);
			}else detector.setIsLongpressEnabled(false);
			
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				 downX = event.getX();
				 downY = event.getY();
			case MotionEvent.ACTION_MOVE:
				float moveX = event.getX();
				float moveY = event.getY();
				
				if(Math.abs(downX - moveX) >150 || Math.abs(downY - moveY)>150 ){
					
					
				}
				
			}
			
			

		
		//detector.onTouchEvent(event);
		return detector.onTouchEvent(event); // MUSS SO AUFGERUFEN WERDEN
	}

	@Override
	public boolean onDown(MotionEvent event)
	{

		// holt die Koordinaten des Touch-Punktes
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		if (activeItem instanceof RelativeLayout)
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
			 * 2)); newOne.setY(clickPosY - (newOne.getHeight() / 2)); return
			 * true;
			 */
		}

		
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		
		
			Toast.makeText(context.getApplicationContext(), "fling",
					Toast.LENGTH_LONG).show();

		

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

		Log.d("OnLongpress", "is called");

		if (activeItem instanceof Button)
		{
			isDragging =true;
			Toast.makeText(context.getApplicationContext(),
					"Button " + activeItem.getId() + " is longclicked",
					Toast.LENGTH_SHORT).show();
			
			ClipData.Item item = new ClipData.Item((String) activeItem.getTag());
			ClipData clipData = new ClipData(
					(CharSequence) activeItem.getTag(),
					new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

			RelativeLayout overlay = factory.getOverlay(activeItem);

			root.addView(overlay);
			root.requestLayout();

			Log.d("OnLongclick", String.valueOf(overlay.getWidth()));
			

			//activeItem.startDrag(clipData, new View.DragShadowBuilder(
				//	activeItem), null, 0);
		}

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		
		if(isDragging){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
				.getLayoutParams();

		params.leftMargin += (int) e2.getX();
		params.topMargin += (int) e2.getY();
		
		activeItem.setLayoutParams(params);
		root.requestLayout();
		}
	
	
	activeItem = null;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		isDragging=false;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDrag(View root, DragEvent event)
	{

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			start = event;
			isDragging = true;
			
			

			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			timeEnd.setTime(System.currentTimeMillis());
			/*
			 * if (isFling(start, event, timeStart, timeEnd)) {
			 * this.root.removeView(activeItem); }
			 */
			break;
		case DragEvent.ACTION_DROP:

			timeEnd.setTime(System.currentTimeMillis());

			
			

			break;
			
		
		}
		return true;
	}

	

}
