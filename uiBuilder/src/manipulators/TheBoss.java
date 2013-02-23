package manipulators;

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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import creators.ObjectFactory;

/**
 * Die Boss-Klasse implementiert alle Listener und koordiniert die Erstellung
 * und Bearbeitung von Objekten auf der Arbeisfläche.
 * 
 * 
 */
public class TheBoss implements OnDragListener, OnGestureListener,
		OnTouchListener
{

	private Context context;
	private View activeItem;
	private ObjectFactory factory;
	private RelativeLayout root;

	private GestureDetector detector;
	private final String OVERLAYTAG = "Overlay";

	private boolean isDragging;
	boolean overlayActive = false;
	private View currentTouch;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param context reference to activity
	 * @param root reference for the design area
	 */
	public TheBoss(Context context, RelativeLayout root)
	{
		super();
		this.context = context;
		factory = new ObjectFactory(context, this);
		this.root = root;
		this.root.setTag("PLAYGROUND");

		detector = new GestureDetector(context, this);
		isDragging = false;

		activeItem = null;
	}

	private View dragIndicator; /**@param dragIndicator the active overlay element. Either ID_LEFT, ID_TOP, ID_BOTTOM, ID_RIGHT*/

	/**
	 * Erfasst alle Touch-Events, setzt ggf. Flags die den Zustand der Applikation abbilden.
	 * return calls the GestureDetector instance for a matching event
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		currentTouch = v;
		
		switch (currentTouch.getId())
		{
			case de.ur.rk.uibuilder.R.id.design_area:
				Log.d("DesignArea", "called");
				detector.setIsLongpressEnabled(false);
				dragIndicator = null;
				
				
				if(overlayActive)
				{
					deleteOverlay();
					return true;
	
				}
				activeItem = null;
				Log.d("layout forward", "called");
				break;
			
				
			case ID_TOP: case ID_RIGHT: case ID_BOTTOM: case ID_LEFT: case ID_CENTER:
				detector.setIsLongpressEnabled(false);
				dragIndicator = currentTouch;
				Log.d("wäre jetzt super", "fuck");
				break;
				
			default:
				
				if(overlayActive && currentTouch != activeItem)
				{
					deleteOverlay();
					return true;
				}
				activeItem = currentTouch;
				detector.setIsLongpressEnabled(true);
				break;
		}
 
		Log.d("forward", "is called");
		
		 // MUSS SO AUFGERUFEN WERDEN 
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event)//Wird aufgerufen, wenn das zum ersten mal Display berührt wird.
	{
		// holt die Koordinaten des Touch-Punktes
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);
		
		Log.d("Ondown", "is called");
		return createObject(clickPosX, clickPosY); /** If the Object was created the event is consumed. */
	}

	/**
	 * Creates a Button on the specified position.
	 * 
	 * @param clickPosX The coordinate on the X-axis
	 * @param clickPosY The coordinate on the Y-axis
	 * @return <b>true</b> if the conditions for creation are met, else <b>false</b>
	 * 
	 */
	private boolean createObject(float clickPosX, float clickPosY)
	{
		if (activeItem == null && !overlayActive)
		{
			Button newOne = (Button) factory
					.getElement(ObjectFactory.ID_BUTTON);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne
					.getLayoutParams();

			params.leftMargin = (int) clickPosX;
			params.topMargin = (int) clickPosY;
			root.addView(newOne, params);

			params.leftMargin = (int) clickPosX - newOne.getMeasuredWidth() / 2;
			params.topMargin = (int) clickPosY - newOne.getMeasuredHeight() / 2;
			newOne.setLayoutParams(params);
			Log.d("onDown",
					String.valueOf(params.leftMargin) + " "
							+ String.valueOf(params.topMargin));
			Log.d("onDown", String.valueOf(newOne.getWidth() / 2) + " "
					+ String.valueOf(newOne.getMeasuredHeight() / 2));
			
			return true;
		}
		return false;
	}

	private void invalidate()
	{
		root.requestLayout();
	}

	//Funktioniert aktuell noch nicht wegen der OnDrag Methode.
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{

		Toast.makeText(context.getApplicationContext(), "fling",
				Toast.LENGTH_SHORT).show();

		return true;
	}
	//Erstellt das Overlay wenn lange gedrückt wurde.
	@Override
	public void onLongPress(MotionEvent e)
	{

		Log.d("OnLongpress", "is called");

		isDragging = true;
		Toast.makeText(context.getApplicationContext(),
				"Button " + activeItem.getId() + " is longclicked",
				Toast.LENGTH_SHORT).show();
		
		setOverlay();
		Log.d("measure left", String.valueOf(left.getMeasuredWidth()));

	}

	// TEMPORÄR-BÄR
	private ImageButton drag;
	private ImageButton left;
	private ImageButton right;
	private ImageButton bottom;
	private ImageButton top;

	private static final int ID_CENTER = (int) 78.75; // hihi
	private static final int ID_TOP = 83;
	private static final int ID_RIGHT = 73;
	private static final int ID_BOTTOM = 90;
	private static final int ID_LEFT = 69;
	
	
	/**
	 * Erstellt ein Overlay. Position des Overlays wird von dem derzeit aktiven
	 * Element bestimmt. Die Flag <i>overlayActive</i> wird hier auf <b>true</b>
	 * gesetzt. Touchlistener werden gesetzt und das Overlay wird sofort angezeigt.
	 */
	private void setOverlay()
	{
		activeItem.setAlpha(0.5f);

		overlayActive = true;
		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(
				activeItem.getLayoutParams());
		
		Log.d("params right", String.valueOf(activeItem.getRight()));

		// DRAG
		drag = new ImageButton(context);
		modified.leftMargin = activeItem.getLeft();
		modified.topMargin = activeItem.getTop();
		modified.width = activeItem.getMeasuredWidth();
		modified.height = activeItem.getMeasuredHeight();
		drag.setBackgroundResource(android.R.color.background_dark);
		drag.setAlpha(0.5f);
		drag.setId(ID_CENTER);
		drag.setTag(OVERLAYTAG);
		drag.setOnTouchListener(this);
		root.addView(drag, modified);
		invalidate();

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// RIGHT
		right = new ImageButton(context);
		right.setBackgroundResource(android.R.color.holo_orange_light);
		right.setAlpha(0.5f);
		
		modified.addRule(RelativeLayout.ALIGN_TOP, ID_CENTER);
		modified.addRule(RelativeLayout.RIGHT_OF, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_BOTTOM, ID_CENTER);
		
		right.setId(ID_RIGHT);
		right.setTag(OVERLAYTAG);
		right.setOnTouchListener(this);
		root.addView(right, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// BOTTOM
		bottom = new ImageButton(context);
		bottom.setBackgroundResource(android.R.color.holo_orange_light);
		bottom.setAlpha(0.5f);
		
		modified.addRule(RelativeLayout.BELOW, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_LEFT, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_RIGHT, ID_CENTER);
		
		bottom.setId(ID_BOTTOM);
		bottom.setTag(OVERLAYTAG);
		bottom.setOnTouchListener(this);
		root.addView(bottom, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// LEFT
		left = new ImageButton(context);
		left.setBackgroundResource(android.R.color.holo_orange_light);
		left.setAlpha(0.5f);
		modified.addRule(RelativeLayout.LEFT_OF, bottom.getId());
		modified.addRule(RelativeLayout.ALIGN_TOP, right.getId());
		modified.addRule(RelativeLayout.ABOVE, bottom.getId());
		left.setId(ID_LEFT);
		left.setTag(OVERLAYTAG);
		left.setOnTouchListener(this);
		root.addView(left, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// TOP
		top = new ImageButton(context);
		top.setBackgroundResource(android.R.color.holo_orange_light);
		top.setAlpha(0.5f);
		
		modified.addRule(RelativeLayout.ABOVE, right.getId());
		modified.addRule(RelativeLayout.LEFT_OF, right.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, left.getId());
		
		top.setId(ID_TOP);
		top.setTag(OVERLAYTAG);
		top.setOnTouchListener(this);
		root.addView(top, modified);

		invalidate();
	}

	/**
	 * Perform motion of the overlay resize-handles and resize the corresponding element based on the new position of the handle
	 * movement in progress.
	 * <p>
	 * <b>The distance values passed by the framework are are not predictable.</b> 
	 * They alternate between positive and negative values.
	 * </p>
	 * The solution is to compute our own distance values to get a smooth movement and a nice experience.
	 * @Override 
	 */
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		invalidate();
		if (dragIndicator != null) //Startet ein DragEvent wenn ein Overlay existiert.
		{
			
			switch (dragIndicator.getId())
			{
			case ID_CENTER:
				Log.d("onScroll", "wurde Aufgerufen");
				ClipData.Item item = new ClipData.Item((String) activeItem.getTag());
				ClipData clipData = new ClipData((CharSequence) activeItem.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
				
				activeItem.startDrag(clipData, new View.DragShadowBuilder(activeItem), null, 0);
				break;
			

			case ID_RIGHT:
				Log.d("right indicator", "is moving");

				setParams(ID_RIGHT, e1, e2);
				
				break;
				
			case ID_BOTTOM:
				Log.d("bottom indicator", "is moving");
				
				setParams(ID_BOTTOM, e1, e2);
				
				break;
				
			case ID_TOP:
				setParams(ID_TOP, e1, e2);
				break;
				
			case ID_LEFT:
				Log.d("left indicator", "is moving");
				setParams(ID_LEFT, e1, e2);
				break;
				
			default:
				// FUCK OFF
				break;
			}
		}
		return false;
	}
	/**
	 * This method resizes the item and repositions it appropriately.
	 * 
	 * @param handleId the handle which started the scaling
	 * @param start the starting point of the scaling process
	 * @param now the actual position of the scale movement
	 */
	private void setParams(int handleId, MotionEvent start, MotionEvent now)
	{
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
		float distance;
		
		switch (handleId)
		{
		case ID_RIGHT:
			distance = now.getX() - start.getX();
			params.width = activeItem.getMeasuredWidth() + Math.round(distance);
			params.height = activeItem.getMeasuredHeight();
			break;

		case ID_BOTTOM:
			distance = now.getY() - start.getY();
			params.width = activeItem.getMeasuredWidth();
			params.height = activeItem.getMeasuredHeight() + Math.round(distance);
			break;
			
		case ID_TOP:
			distance = start.getY() - now.getY();
			params.topMargin = activeItem.getTop() - Math.round(distance);
			params.width = activeItem.getMeasuredWidth();
			params.height = activeItem.getMeasuredHeight() + Math.round(distance);
			break;
			
		case ID_LEFT:
			distance = start.getX() - now.getX();
			params.leftMargin = activeItem.getLeft() - Math.round(distance);
			params.width = activeItem.getMeasuredWidth() + Math.round(distance);
			params.height = activeItem.getMeasuredHeight();
			break;
			
		default:
			break;
		}	
		drag.setLayoutParams(params);
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

		switch (event.getAction()) 
		{
		case DragEvent.ACTION_DRAG_STARTED:
			setOverlayVisibility(false);	//Während des Drags ist kein Overlay sichtbar.
			return true;
			
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
			
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
			
		case DragEvent.ACTION_DRAG_ENDED:
			break;
			
		case DragEvent.ACTION_DRAG_EXITED:
			break;
			
		case DragEvent.ACTION_DROP:
			
			int dropTargetX = checkCollisionX(event);
			int dropTargetY = checkCollisionY(event);
			//Positionen werden ausgelesen und zugewiesen. Objekte werden an ihre Zielposition verschoben und das Overlay bekommt neue Koordinaten. 
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
					.getLayoutParams();

			params.leftMargin = dropTargetX;
			params.topMargin = dropTargetY;
			Log.d("getx gety", String.valueOf(params.leftMargin) + " " + String.valueOf(params.topMargin));
			activeItem.setLayoutParams(params);

			invalidate();
			
			params.width = activeItem.getMeasuredWidth();
			params.height = activeItem.getMeasuredHeight();
			drag.setLayoutParams(params);

			invalidate();

			setOverlayVisibility(true);//das Overlay wird wieder angezeigt, da der Drag vorbei ist. 
			isDragging = false;

			return true;
		}
		return true; //EVTL FEHLERQUELLE: RETURNS ALWAYS TRUE
	}

	private int checkCollisionY(DragEvent event)
	{
		int dropTargetY;
		int minPosY = top.getMeasuredHeight();
		int maxPosY = root.getMeasuredHeight() - bottom.getMeasuredHeight() - activeItem.getMeasuredHeight();
		
		int dropPosY = (int) event.getY() - activeItem.getMeasuredHeight()/2;
		
		if (dropPosY > minPosY && dropPosY < maxPosY)
		{
			dropTargetY = dropPosY;
		}
		else
		{
			dropTargetY = (dropPosY < minPosY) ? minPosY : maxPosY;
		}
		return dropTargetY;
	}

	private int checkCollisionX(DragEvent event)
	{
		int dropTargetX;
		int minPosX = left.getMeasuredWidth();
		int maxPosX = root.getMeasuredWidth() - right.getMeasuredWidth() - activeItem.getMeasuredWidth();
		
		int dropPosX = (int) event.getX() - activeItem.getMeasuredWidth() / 2;
		
		if (dropPosX > minPosX && dropPosX < maxPosX)
		{
			dropTargetX = dropPosX;
		}
		else
		{
			dropTargetX = (dropPosX < minPosX) ? minPosX : maxPosX;
		}
		return dropTargetX;
	}
	/** 
	 * Bestimmt die Sichtbarkeit des Overlays. Das Overlay wird <b>Versteckt </b>, jedoch <b>nicht Entfernt</b>.
	 * @param visible legt die Sichtbarkeit des Overlays fest.
	 */
	private void setOverlayVisibility(boolean visible)
	{
		if (visible)
		{
			drag.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.VISIBLE);
			left.setVisibility(View.VISIBLE);
			right.setVisibility(View.VISIBLE);
		} 
		else
		{

			drag.setVisibility(View.INVISIBLE);
			top.setVisibility(View.INVISIBLE);
			bottom.setVisibility(View.INVISIBLE);
			left.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * Entfernt das Overlay komplett. 
	 * <b>Keine</b> Überprüfung ob Overlay vorhanden!
	 * TODO increase robustness!
	 */
	private void deleteOverlay()
	{
		root.removeView(drag);
		root.removeView(left);
		root.removeView(right);
		root.removeView(top);
		root.removeView(bottom);
		activeItem.setAlpha(1.0f);

		dragIndicator = null;
		overlayActive = false;
		isDragging = false;
	}

}
