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
		isDragging = false;

		activeItem = null;
	}

	private View dragIndicator; //war gedacht um laufende Drags

	// festzustellen.

	/**
	 * Erfasst alle Touch-Events, setzt ggf. Flags die den Zustand der Applikation abbilden.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		if (v instanceof RelativeLayout)// Betrifft alle touch-Events die auf
										// der leeren Arbeitsfläche stattfinden.
		{
			dragIndicator = null;
			detector.setIsLongpressEnabled(false);

			if (overlayActive)// Für den Fall, dass ein Overlay existiert.
			{
				deleteOverlay();
				return true;
			}			
			activeItem = null;

		} else
		{
			if (v.getTag() == OVERLAYTAG) // Relikt
			{
				dragIndicator = v;
				Log.d("wäre jetzt super", "fuck");
				
			} 
			else
			{
				Log.d("OnLongpressenabled", "is called");

				if (overlayActive && (v != activeItem)) // Overlay wird nur dann
														// entfernt, wenn aktiv
														// und das Touch-Event
														// (auch Drags) nicht
														// über dem Aktiven
														// Element stattfinden.
				{
					deleteOverlay();
				}
				activeItem = v;
				detector.setIsLongpressEnabled(true); //Longclics sollen weitergereicht werden.
			}
		}
/*		
		 switch (event.getAction()) 
		 {
		 case MotionEvent.ACTION_MOVE:
		 
			 switch (v.getId())
			{
			case ID_TOP:
				Log.d("top indicator", "is moving");
				break;

			case ID_RIGHT:
				Log.d("right indicator", "is moving");
				Log.d("right width", String.valueOf(dragIndicator.getMeasuredWidth()));
				
				break;
				
			case ID_BOTTOM:
				Log.d("bottom indicator", "is moving");
				break;
				
			case ID_LEFT:
				Log.d("left indicator", "is moving");
				break;
				
			default:
				// FUCK OFF
				break;
			}
		 
		 break;
		 
		  }
*/		 
		Log.d("forward", "is called");
		return detector.onTouchEvent(event); // MUSS SO AUFGERUFEN WERDEN
	}

	@Override
	public boolean onDown(MotionEvent event)//Wird aufgerufen, wenn das Display berührt wird.
	{
		// holt die Koordinaten des Touch-Punktes
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		if (activeItem == null && !overlayActive)
		{

			// erstellt den Button an den zuvor ermittelten Koordinaten
			Button newOne = (Button) factory
					.getElement(ObjectFactory.ID_BUTTON);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne
					.getLayoutParams();

			params.leftMargin = (int) clickPosX;
			params.topMargin = (int) clickPosY;
			root.addView(newOne, params);

			//invalidate(); //Damit der neue Button Koordinaten und Maße bekommt.
			params.leftMargin = (int) clickPosX - newOne.getMeasuredWidth() / 2;
			params.topMargin = (int) clickPosY - newOne.getMeasuredHeight() / 2;
			newOne.setLayoutParams(params);
			Log.d("onDown",
					String.valueOf(params.leftMargin) + " "
							+ String.valueOf(params.topMargin));
			Log.d("onDown", String.valueOf(newOne.getWidth() / 2) + " "
					+ String.valueOf(newOne.getMeasuredHeight() / 2));
			//invalidate(); 

			return true;
		}

		Log.d("Ondown", "is called");
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
		/*if (overlayActive && activeItem != null)
		{
			this.root.removeView(activeItem);
		}*/
		Toast.makeText(context.getApplicationContext(), "fling",
				Toast.LENGTH_LONG).show();

		return true;
	}
	//Erstellt das Overlay wenn lange gedrückt wurde.
	@Override
	public void onLongPress(MotionEvent e)
	{

		Log.d("OnLongpress", "is called");

		if (activeItem instanceof Button && !overlayActive)//Verhindert doppelte Overlays
		{
			isDragging = true;
			Toast.makeText(context.getApplicationContext(),
					"Button " + activeItem.getId() + " is longclicked",
					Toast.LENGTH_SHORT).show();
			//invalidate();
			setOverlay();
			Log.d("measure left", String.valueOf(left.getMeasuredWidth()));

			//invalidate();
		}

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
		root.addView(top, modified);

		invalidate();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		if (dragIndicator != null)//Startet ein DragEvent wenn ein Overlay existiert.
		{
			switch (dragIndicator.getId())
			{
			case ID_CENTER:
				Log.d("onScroll", "wurde Aufgerufen");
				ClipData.Item item = new ClipData.Item((String) activeItem.getTag());
				ClipData clipData = new ClipData(
						(CharSequence) activeItem.getTag(),
						new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
				activeItem.startDrag(clipData, new View.DragShadowBuilder(
						activeItem), null, 0);

				
				break;
			
			case ID_TOP:
				Log.d("top indicator", "is moving");
				break;

			case ID_RIGHT:
				Log.d("right indicator", "is moving");
				
				Log.d("right distance", String.valueOf(distanceX));
				
				
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
						.getLayoutParams(); 
				params.width = activeItem.getMeasuredWidth();
				params.height = activeItem.getMeasuredHeight();
				params.width -= Math.round(distanceX); //FRAMEWORK INVERTS VALUES!!!
				activeItem.setLayoutParams(params);
				
				//invalidate();
				params = (RelativeLayout.LayoutParams) activeItem
						.getLayoutParams();
				//params.width -= distanceX;
				drag.setLayoutParams(params);
				invalidate();
				
				break;
				
			case ID_BOTTOM:
				Log.d("bottom indicator", "is moving");
				break;
				
			case ID_LEFT:
				Log.d("left indicator", "is moving");
				break;
				
			default:
				// FUCK OFF
				break;
			}
		}
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

		// if (overlayActive)
		{
			switch (event.getAction()) 
			{
			case DragEvent.ACTION_DRAG_STARTED:
				setOverlayVisibility(false);	//Während des Drags ist kein Overlay sichtbar.
				//root.requestLayout();
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
				
				//Positionen werden ausgelesen und zugewiesen. Objekte werden an ihre Zielposition verschoben und das Overlay bekommt neue Koordinaten. 
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activeItem
						.getLayoutParams();

				params.leftMargin = (int) event.getX()
						- activeItem.getMeasuredWidth() / 2;
				params.topMargin = (int) event.getY()
						- activeItem.getMeasuredHeight() / 2;
				Log.d("getx gety", String.valueOf(params.leftMargin) + " "
						+ String.valueOf(params.topMargin));
				activeItem.setLayoutParams(params);

				root.requestLayout();

				params.width = activeItem.getMeasuredWidth();
				params.height = activeItem.getMeasuredHeight();
				drag.setLayoutParams(params);

				root.requestLayout();

				setOverlayVisibility(true);//das Overlay wird wieder angezeigt, da der Drag vorbei ist. 
				isDragging = false;

				return true;

			}
		}
		return true;
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
		} else
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
	 */
	private void deleteOverlay()
	{
		root.removeView(drag);
		root.removeView(left);
		root.removeView(right);
		root.removeView(top);
		root.removeView(bottom);
		activeItem.setAlpha(1.0f);

		//invalidate();
		overlayActive = false;
	}

}
