package uibuilder;


import creators.ObjectFactory;
import helpers.Grid;
import helpers.Log;
import manipulators.TheBoss;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import de.ur.rk.uibuilder.R;

public class DesignFragment extends Fragment implements OnDragListener, OnGestureListener,
OnTouchListener
{

	private RelativeLayout designArea, parent;
	private Grid grid;

	private TheBoss manipulator;
	
	private Context context;
	private View activeItem;
	private ObjectFactory factory;
	

	private GestureDetector detector;
	private final String OVERLAYTAG = "Overlay";

	private boolean isDragging;
	boolean overlayActive = false;
	private View currentTouch;
	
	public static final int SNAP_GRID_INTERVAL = 25;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		context = getActivity().getApplicationContext();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_design_fragment,
		        container, false);
		designArea = (RelativeLayout) root.findViewById(R.id.design_area);
		

		//manipulator = new TheBoss(getActivity().getApplicationContext(), designArea);
		

		designArea.post(new Runnable()
		{
			

			@Override
			public void run()
			{
				
				int rootWidth = designArea.getMeasuredWidth();
				int rootHeight = rootWidth/16*9;
				
				Log.d("pre measured", String.valueOf(rootWidth));
				Log.d("pre measured", String.valueOf(rootHeight));
				
				int handleSize = getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
				int maxWidth = rootWidth - 2*handleSize;
				int maxHeight = rootHeight - 2*handleSize;
				
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
				params.width = matchWithGrid(maxWidth);
				params.height = matchWithGrid(maxHeight);
				
				designArea.setLayoutParams(params);
				designArea.forceLayout();
				
				Log.d("root width post", String.valueOf(params.width));
				Log.d("root height post", String.valueOf(params.height)); 
			}

			private int matchWithGrid(int size)
			{ 
				return (size / TheBoss.SNAP_GRID_INTERVAL) * TheBoss.SNAP_GRID_INTERVAL;
			}
		});
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		factory = new ObjectFactory(getActivity().getApplicationContext(), this);
		
		designArea.setTag("PLAYGROUND");

		parent = (RelativeLayout) designArea.getParent();
		detector = new GestureDetector(getActivity().getApplicationContext(), this);
		isDragging = false;
		activeItem = null;
		snapMode = true;
		
		grid = new Grid(getActivity().getApplicationContext(), SNAP_GRID_INTERVAL);
		grid.setLayoutParams(designArea.getLayoutParams());
		parent.addView(grid);
		
		toggleGrid();
		


		designArea.setOnTouchListener(this);
		designArea.setOnDragListener(this);
		
		super.onActivityCreated(savedInstanceState);
	}

	public void setSelection(int id)
	{
		nextObjectId = id;
	}


	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	private View dragIndicator;
	private int nextObjectId;
	private boolean snapMode;

	/**
	 * @param dragIndicator
	 *            the active overlay element. Either ID_LEFT, ID_TOP, ID_BOTTOM,
	 *            ID_RIGHT
	 */

	/**
	 * Erfasst alle Touch-Events, setzt ggf. Flags die den Zustand der
	 * Applikation abbilden. return calls the GestureDetector instance for a
	 * matching event
	 */
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		currentTouch = v;

		switch (currentTouch.getId())
		{
		case R.id.design_area:
			Log.d("DesignArea", "called");
			detector.setIsLongpressEnabled(false);
			activeItem = null;
			if (overlayActive)
			{
				Log.d("Case Design Area", "overlay active and therefore deleted");

				deleteOverlay();
				return true;
			}

			Log.d("layout forward", "called");
			break;

		case ID_TOP:
		case ID_RIGHT:
		case ID_BOTTOM:
		case ID_LEFT:
		case ID_CENTER:

			detector.setIsLongpressEnabled(false);
			dragIndicator = currentTouch;
			dragIndicator.setActivated(true);

			Log.d("dragOverlay", "drag handle selected");
			break;

		default:
			Log.d("Default case in ontouch", "called");

			if (overlayActive && currentTouch != activeItem)
			{
				Log.d("Default case in ontouch", "deleting overlay");

				deleteOverlay();
				return true;
			}
			activeItem = currentTouch;
			detector.setIsLongpressEnabled(true);

			Log.d("active Item is currentTouch", "ID:"
					+ String.valueOf(activeItem.getId()));
			break;
		}

		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:

			if (dragIndicator != null)
			{
				Log.d("drag indikator", "drag handle disabled");
				dragIndicator.setActivated(false);
			}
			break;

		default:
			break;
		}

		// MUSS SO AUFGERUFEN WERDEN
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event)
	{
		// holt die Koordinaten des Touch-Punktes
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		Log.d("Ondown", "is called");
		return createObject(clickPosX, clickPosY);
		/** If the Object was created the event is consumed. */
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		if (activeItem != null && overlayActive == false)
		{
			isDragging = true;
			Toast.makeText(getActivity().getApplicationContext(), "Button "
					+ activeItem.getId() + " selected", Toast.LENGTH_SHORT).show();

			setOverlay();
			detector.setIsLongpressEnabled(false);
			return true;
		}
		return false;
	}
	

	/**
	 * Creates a Button on the specified position.
	 * 
	 * @param clickPosX
	 *            The coordinate on the X-axis
	 * @param clickPosY
	 *            The coordinate on the Y-axis
	 * @return <b>true</b> if the conditions for creation are met, else
	 *         <b>false</b>
	 * 
	 */
	private boolean createObject(float clickPosX, float clickPosY)
	{
		if (activeItem == null && !overlayActive && nextObjectId != 0)
		{
			View newOne = (View) factory.getElement(nextObjectId);
			
			activeItem = newOne;
			
			int targetX = checkCollisionX(clickPosX);
			int targetY	= checkCollisionY(clickPosY);
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne.getLayoutParams();

			params.leftMargin = snapToGrid(targetX);
			params.topMargin = snapToGrid(targetY);

			designArea.addView(newOne, params);
			return true;
		}
		return false;
	}

	private void invalidate()
	{
		designArea.requestLayout();
	}

	
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3)
	{
		Log.d("onfling", "fling detected");

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0)
	{
		Log.d("OnLongpress deleted item with id", String.valueOf(activeItem.getId()));
		designArea.removeView(activeItem);
		activeItem = null;

		/**
		 * activeItem.createContextMenue should be called here in the future
		 * from there we could call delete, edit caption and such
		 */
		
	}

	/**
	 * Perform motion of the overlay resize-handles and resize the corresponding
	 * element based on the new position of the handle movement in progress.
	 * <p>
	 * <b>The distance values passed by the framework are are not
	 * predictable.</b> They alternate between positive and negative values.
	 * </p>
	 * The solution is to compute our own distance values to get a smooth
	 * movement and a nice experience.
	 * 
	 * @Override
	 */
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		invalidate();
		if (dragIndicator != null && activeItem != null)
		{

			switch (dragIndicator.getId())
			{
			case ID_CENTER:
				ClipData.Item item = new ClipData.Item((String) activeItem.getTag());
				ClipData clipData = new ClipData((CharSequence) activeItem.getTag(), new String[]
				{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

				activeItem.startDrag(clipData, new View.DragShadowBuilder(activeItem), null, 0);
				break;

			case ID_RIGHT:

				setParams(ID_RIGHT, e1, e2);
				break;

			case ID_BOTTOM:

				setParams(ID_BOTTOM, e1, e2);
				break;

			case ID_TOP:

				setParams(ID_TOP, e1, e2);
				break;

			case ID_LEFT:

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
	 * @param handleId
	 *            the handle which started the scaling
	 * @param start
	 *            the starting point of the scaling process
	 * @param now
	 *            the actual position of the scale movement
	 */

	private void setParams(int handleId, MotionEvent start, MotionEvent now)
	{
		RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();
		RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
		
		float distance;
		int roundedDist;

		switch (handleId)
		{
		case ID_RIGHT:
			distance = now.getX() - start.getX();
			
			roundedDist = checkCollision(distance, ID_RIGHT);
			roundedDist = snapToGrid(roundedDist);

			itemParams.width = dragParams.width = activeItem.getMeasuredWidth() + roundedDist;
			itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			
			break;

		case ID_LEFT:
			distance = start.getX() - now.getX();
			
			roundedDist = checkCollision(distance, ID_LEFT);
			roundedDist = snapToGrid(roundedDist);
			
			dragParams.leftMargin = drag.getLeft() - roundedDist;
			itemParams.leftMargin = activeItem.getLeft() - roundedDist;
			
			itemParams.width = dragParams.width = activeItem.getMeasuredWidth() + roundedDist;
			itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			
			break;

		case ID_BOTTOM:
			distance = now.getY() - start.getY();
			
			roundedDist = checkCollision(distance, ID_BOTTOM);
			roundedDist = snapToGrid(roundedDist);

			itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
			itemParams.height = dragParams.height = activeItem.getMeasuredHeight() + roundedDist;
			
			break;

		case ID_TOP:
			distance = start.getY() - now.getY();
			
			roundedDist = checkCollision(distance, ID_TOP);
			roundedDist = snapToGrid(roundedDist);

			dragParams.topMargin = drag.getTop() - roundedDist;
			itemParams.topMargin = activeItem.getTop() - roundedDist;
			
			itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
			itemParams.height = dragParams.height = activeItem.getMeasuredHeight() + roundedDist;
			
			break;

		default:
			break;
		}
		drag.setLayoutParams(dragParams);
		activeItem.setLayoutParams(itemParams);
	}

	/**
	 * Checks whether the current resize in progress will be larger than the
	 * workspace.
	 * 
	 * @param distance
	 *            the current distance moved from the origin
	 * @param which
	 *            is the id of the overlay element that initiated the scale
	 *            action
	 * @return either the rounded distance when the scaling was in the workspace
	 *         area, or a <b>restricted to workspace</b> distance.
	 */
	private int checkCollision(float distance, int which)
	{
		switch (which)
		{
		case ID_RIGHT:

			if (activeItem.getRight() + distance >= designArea.getWidth())
			{
				float overHead = (activeItem.getRight() + distance - designArea.getWidth());

				return Math.round(distance - overHead);
			}
			break;

		case ID_LEFT:

			if (activeItem.getLeft() - distance <= 0)
			{
				float overHead = (activeItem.getLeft() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case ID_TOP:

			if (activeItem.getTop() - distance <= 0)
			{
				float overHead = (activeItem.getTop() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case ID_BOTTOM:

			if (activeItem.getBottom() + distance >= designArea.getHeight())
			{
				float overHead = (activeItem.getBottom() - designArea.getHeight() + distance);

				return Math.round(distance - overHead);
			}

		default:
			break;
		}
		return Math.round(distance);
	}


	@Override
	public void onShowPress(MotionEvent event)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onDrag(View root, DragEvent event)
	{
		synchronized (activeItem)
		{

		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			
			setOverlayVisibility(false); // Während des Drags ist kein Overlay
			toggleGrid();// sichtbar.
			return true;

		case DragEvent.ACTION_DRAG_ENTERED:
			
			setStyle(DragEvent.ACTION_DRAG_ENTERED);
			
			break;

		case DragEvent.ACTION_DRAG_LOCATION:
			break;

		case DragEvent.ACTION_DRAG_ENDED:
			
			setStyle(DragEvent.ACTION_DRAG_ENDED);
			setOverlayVisibility(true); // das Overlay wird wieder angezeigt, da der Drag vorbei ist.
			
			isDragging = false;
			toggleGrid();
			
			break;

		case DragEvent.ACTION_DRAG_EXITED:
			setStyle(DragEvent.ACTION_DRAG_EXITED);
			break;

		case DragEvent.ACTION_DROP:

			int dropTargetX = checkCollisionX(event.getX());
			int dropTargetY = checkCollisionY(event.getY());

			// Positionen werden ausgelesen und zugewiesen. Objekte werden an
			// ihre Zielposition verschoben und das Overlay bekommt neue
			// Koordinaten.
			RelativeLayout.LayoutParams activeParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
			RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();
			
			dragParams.leftMargin = snapToGrid(dropTargetX) + root.getLeft();
			dragParams.topMargin = snapToGrid(dropTargetY) + root.getTop();
			dragParams.width = activeItem.getMeasuredWidth();
			dragParams.height = activeItem.getMeasuredHeight();
			drag.setLayoutParams(dragParams);
			
			
			activeParams.leftMargin = snapToGrid(dropTargetX);
			activeParams.topMargin = snapToGrid(dropTargetY);
			activeItem.setLayoutParams(activeParams);

			return true;
			}
			
		
		return true; // EVTL FEHLERQUELLE: RETURNS ALWAYS TRUE
		}
	}
	
	
	private int snapToGrid(int pos)
	{
		return Math.round((float) pos / SNAP_GRID_INTERVAL) * SNAP_GRID_INTERVAL;
	}

	private void setStyle(int event)
	{
		synchronized (activeItem)
		{
			
			switch (event)
			{
			case DragEvent.ACTION_DRAG_ENTERED:
			case DragEvent.ACTION_DRAG_ENDED:

				activeItem.setBackgroundResource(R.drawable.default_button_border);
				break;

			case DragEvent.ACTION_DRAG_EXITED:

				activeItem.setBackgroundResource(R.drawable.element_out_of_dropzone);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Checks if the target of the drop action is within view bounds
	 * 
	 * @param float y of event
	 * @return calculated Y-position of the performed drop
	 */
	private int checkCollisionY(float dropPosY)
	{
		int offsetPos = Math.round(dropPosY - activeItem.getMeasuredHeight()/2);

		int maxPos = Math.round(designArea.getMeasuredHeight() - activeItem.getMeasuredHeight());
		int minPos = 0;

		if (offsetPos <= minPos)
		{
			return minPos;
		}
		if (offsetPos >= maxPos)
		{
			return maxPos;
		}
		return offsetPos;
	}

	/**
	 * Checks if the target of the drop action is within view bounds
	 * 
	 * @param float x of event
	 * @return calculated X-position of the performed drop
	 */
	private int checkCollisionX(float dropPosX)
	{
		int offsetPos = Math.round(dropPosX - activeItem.getMeasuredWidth() / 2);

		int maxPos = Math.round(designArea.getMeasuredWidth() - activeItem.getMeasuredWidth());
		int minPos = 0;

		if (offsetPos <= minPos)
		{
			return minPos;
		}
		if (offsetPos >= maxPos)
		{
			return maxPos;
		}
		return offsetPos;
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
	 * gesetzt. Touchlistener werden gesetzt und das Overlay wird sofort
	 * angezeigt.
	 */
	private void setOverlay()
	{

		overlayActive = true;
		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(activeItem.getLayoutParams());

		Log.d("params right", String.valueOf(activeItem.getRight()));
		
		// DRAG
		drag = new ImageButton(context);
		modified.leftMargin = activeItem.getLeft() + designArea.getLeft();
		modified.topMargin = activeItem.getTop() + designArea.getTop();
		modified.width = activeItem.getMeasuredWidth();
		modified.height = activeItem.getMeasuredHeight();
		drag.setBackgroundResource(R.drawable.overlay_center_border);
		drag.setId(ID_CENTER);
		drag.setTag(OVERLAYTAG);
		drag.setOnTouchListener(this);
		parent.addView(drag, modified);

		invalidate();

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// RIGHT
		right = new ImageButton(context);
		right.setBackgroundResource(R.drawable.overlay_states_right);
		right.setAlpha(0.8f);

		right.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.ALIGN_TOP, ID_CENTER);
		modified.addRule(RelativeLayout.RIGHT_OF, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_BOTTOM, ID_CENTER);

		right.setId(ID_RIGHT);
		right.setTag(OVERLAYTAG);
		right.setOnTouchListener(this);
		parent.addView(right, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// BOTTOM
		bottom = new ImageButton(context);
		bottom.setBackgroundResource(R.drawable.overlay_states_bottom);
		bottom.setAlpha(0.8f);

		bottom.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.BELOW, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_LEFT, ID_CENTER);
		modified.addRule(RelativeLayout.ALIGN_RIGHT, ID_CENTER);

		bottom.setId(ID_BOTTOM);
		bottom.setTag(OVERLAYTAG);
		bottom.setOnTouchListener(this);
		parent.addView(bottom, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// LEFT
		left = new ImageButton(context);
		left.setAlpha(0.8f);
		left.setBackgroundResource(R.drawable.overlay_states_left);
		left.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.LEFT_OF, bottom.getId());
		modified.addRule(RelativeLayout.ALIGN_TOP, right.getId());
		modified.addRule(RelativeLayout.ABOVE, bottom.getId());
		left.setId(ID_LEFT);
		left.setTag(OVERLAYTAG);
		left.setOnTouchListener(this);
		parent.addView(left, modified);

		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// TOP
		top = new ImageButton(context);
		top.setBackgroundResource(R.drawable.overlay_states_top);
		top.setAlpha(0.8f);

		top.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.ABOVE, right.getId());
		modified.addRule(RelativeLayout.LEFT_OF, right.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, left.getId());

		top.setId(ID_TOP);
		top.setTag(OVERLAYTAG);
		top.setOnTouchListener(this);
		parent.addView(top, modified);

		invalidate();
	}

	/**
	 * Bestimmt die Sichtbarkeit des Overlays. Das Overlay wird <b>Versteckt
	 * </b>, jedoch <b>nicht Entfernt</b>.
	 * 
	 * @param visible
	 *            legt die Sichtbarkeit des Overlays fest.
	 */
	private void setOverlayVisibility(boolean visible)
	{
		synchronized (designArea)
		{
				if (drag!=null)
				{
					setItemVisibility(drag, visible);
					setItemVisibility(top, visible);
					setItemVisibility(right, visible);
					setItemVisibility(bottom, visible);
					setItemVisibility(left, visible);
				} 
		}
		
	}

	private void setItemVisibility(final View v, final boolean on)
	{
		v.post(new Runnable()
		{
			
			@Override
			public void run()
			{
				if (on)
				{
					v.setVisibility(View.VISIBLE);
				}
				else
				{
					v.setVisibility(View.INVISIBLE);
				}
				
			}
		});
	}
	
	/**
	 * Entfernt das Overlay komplett.
	 * 
	 */
	private void deleteOverlay()
	{
		synchronized (parent)
		{
			if (overlayActive)
			{
				parent.removeView(drag);
				parent.removeView(left);
				parent.removeView(right);
				parent.removeView(top);
				parent.removeView(bottom);
				
				drag = null;
				left = null;
				right = null;
				top = null;
				bottom = null;

				dragIndicator = null;
				overlayActive = false;
				isDragging = false;
			}
		}
	}
	
	private void toggleGrid()
	{
		synchronized (grid)
		{
			if (grid.getVisibility() == View.INVISIBLE)
			{
				grid.setVisibility(View.VISIBLE);
			}
			else
			{
				grid.setVisibility(View.INVISIBLE);
			}
		}
		
	}
}
