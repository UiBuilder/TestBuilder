package uibuilder;

import helpers.Grid;
import helpers.Log;
import manipulators.Overlay;
import manipulators.TheBoss;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import creators.Generator;
import creators.ObjectFactory;
import de.ur.rk.uibuilder.R;

public class DesignFragment extends Fragment implements OnDragListener,
		OnGestureListener, OnTouchListener
{

	private RelativeLayout designArea, parent;
	private Grid grid;
	private Overlay overlay;

	private View activeItem;
	private ObjectFactory factory;

	private GestureDetector detector;


	private boolean isDragging;
	private View currentTouch;

	public static final int SNAP_GRID_INTERVAL = 25;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View root = inflater.inflate(R.layout.layout_design_fragment, container, false);
		designArea = (RelativeLayout) root.findViewById(R.id.design_area);

		designArea.post(new Runnable()
		{
			@Override
			public void run()
			{
				resizeDrawingArea();
			}

			/**
			 * Resizes the designArea after the layouting process has finished,
			 * to have access to measured dimensions
			 */
			private void resizeDrawingArea()
			{
				int rootWidth = designArea.getMeasuredWidth();
				int rootHeight = rootWidth / 16 * 9;

				Log.d("pre measured", String.valueOf(rootWidth));
				Log.d("pre measured", String.valueOf(rootHeight));

				int handleSize = getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
				int maxWidth = rootWidth - 2 * handleSize;
				int maxHeight = rootHeight - 2 * handleSize;

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
				params.width = matchWithGrid(maxWidth);
				params.height = matchWithGrid(maxHeight);

				designArea.setLayoutParams(params);
				designArea.forceLayout();

				Log.d("root width post", String.valueOf(params.width));
				Log.d("root height post", String.valueOf(params.height));
			}

			/**
			 * Round the dimension to match with the given grid.
			 * 
			 * @param actual
			 *            dimension of the area
			 * @return dimension rounded to a multiple of the grid dimension
			 */
			private int matchWithGrid(int size)
			{
				return (size / TheBoss.SNAP_GRID_INTERVAL)
						* TheBoss.SNAP_GRID_INTERVAL;
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

		overlay = new Overlay(designArea, this);

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

		int action = event.getAction() & MotionEvent.ACTION_MASK;

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:

			currentTouch = v;

			listener.objectChanged(currentTouch);

			Log.d("first down", String.valueOf(event.getPointerCount()));

			switch (currentTouch.getId())
			{
			case R.id.design_area:
				Log.d("DesignArea", "called");
				detector.setIsLongpressEnabled(false);
				activeItem = null;

				listener.objectSelected(false);

				if (overlay.isActive())
				{
					Log.d("Case Design Area", "overlay active and therefore deleted");

					deleteOverlay();
					return true;
				}

				Log.d("layout forward", "called");
				break;

			case R.id.overlay_top:
			case R.id.overlay_right:
			case R.id.overlay_bottom:
			case R.id.overlay_left:
			case R.id.overlay_drag:

				detector.setIsLongpressEnabled(false);
				dragIndicator = currentTouch;
				dragIndicator.setActivated(true);

				Log.d("dragOverlay", "drag handle selected");
				break;

			default:
				Log.d("Default case in ontouch", "called");

				listener.objectSelected(true);

				if (overlay.isActive() && currentTouch != activeItem)
				{
					Log.d("Default case in ontouch", "deleting overlay");

					deleteOverlay();
				}
				activeItem = currentTouch;
				detector.setIsLongpressEnabled(true);

				if (!overlay.isActive())
				{
					isDragging = true;
					Toast.makeText(getActivity().getApplicationContext(), "Button "
							+ activeItem.getId() + " selected", Toast.LENGTH_SHORT).show();

					overlay.generate(activeItem);

					detector.setIsLongpressEnabled(false);
					return true;
				}

				Log.d("active Item is currentTouch", "ID:"
						+ String.valueOf(activeItem.getId()));
				break;
			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:

			int pointerId = event.getPointerId(getIndex(event));
			Log.d("pointer down", String.valueOf(event.getPointerId(getIndex(event))));

			if (pointerId == 1)
			{
				secondPointer = true;
			}

			break;

		case MotionEvent.ACTION_POINTER_UP:

			secondPointer = false;
			break;

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

	boolean secondPointer = false;

	private int getIndex(MotionEvent event)
	{
		return (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
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

	/**
	 * Considered dangerous to call methods here
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{

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
		if (activeItem == null && !overlay.isActive() && nextObjectId != 0)
		{
			View newOne = (View) factory.getElement(nextObjectId);

			activeItem = newOne;

			int targetX = checkCollisionX(clickPosX);
			int targetY = checkCollisionY(clickPosY);

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newOne.getLayoutParams();

			params.leftMargin = snapToGrid(targetX);
			params.topMargin = snapToGrid(targetY);

			designArea.addView(newOne, params);
			return true;
		}
		return false;
	}

	/**
	 * force redraw
	 */
	private void invalidate()
	{
		designArea.requestLayout();
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3)
	{

		if (activeItem != null && !overlay.isActive())
		{
			designArea.removeView(activeItem);
			activeItem = null;

			Log.d("onfling", "fling detected");
			return true;
		}
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
			case R.id.overlay_drag:
				
				Bundle tagBundle = (Bundle) activeItem.getTag();
				int id = tagBundle.getInt(Generator.ID);

				ClipData.Item item = new ClipData.Item(String.valueOf(id));
				ClipData clipData = new ClipData((CharSequence) String.valueOf(id), new String[]
				{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

				activeItem.startDrag(clipData, new View.DragShadowBuilder(activeItem), activeItem, 0);
				break;

			case R.id.overlay_right:

				setParams(R.id.overlay_right, e1, e2);
				break;

			case R.id.overlay_bottom:

				setParams(R.id.overlay_bottom, e1, e2);
				break;

			case R.id.overlay_top:

				setParams(R.id.overlay_top, e1, e2);
				break;

			case R.id.overlay_left:

				setParams(R.id.overlay_left, e1, e2);
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
		ImageButton drag = overlay.getDrag();

		RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();
		RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();

		float distance;
		int roundedDist;
		Bundle itemTag = (Bundle) activeItem.getTag();
		
		switch (handleId)
		{
		case R.id.overlay_right:
			distance = now.getX() - start.getX();
			
			if (checkMinWidth(dragParams, distance, itemTag, R.id.overlay_right))
			{
				roundedDist = checkCollision(distance, R.id.overlay_right);
				roundedDist = snapToGrid(roundedDist);
	
				itemParams.width = dragParams.width = activeItem.getMeasuredWidth()
						+ roundedDist;
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			}
			break;

		case R.id.overlay_left:
			distance = start.getX() - now.getX();
			
			if (checkMinWidth(dragParams, distance, itemTag, R.id.overlay_left))
			{
				roundedDist = checkCollision(distance, R.id.overlay_left);
				roundedDist = snapToGrid(roundedDist);
	
				dragParams.leftMargin = drag.getLeft() - roundedDist;
				itemParams.leftMargin = activeItem.getLeft() - roundedDist;
	
				itemParams.width = dragParams.width = activeItem.getMeasuredWidth() + roundedDist;
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight();
			}
			break;

		case R.id.overlay_bottom:
			distance = now.getY() - start.getY();

			if (checkMinWidth(dragParams, distance, itemTag, R.id.overlay_bottom))
			{
				roundedDist = checkCollision(distance, R.id.overlay_bottom);
				roundedDist = snapToGrid(roundedDist);
	
				itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight() + roundedDist;
			}
			break;

		case R.id.overlay_top:
			distance = start.getY() - now.getY();

			if (checkMinWidth(dragParams, distance, itemTag, R.id.overlay_top))
			{
				roundedDist = checkCollision(distance, R.id.overlay_top);
				roundedDist = snapToGrid(roundedDist);
	
				dragParams.topMargin = drag.getTop() - roundedDist;
				itemParams.topMargin = activeItem.getTop() - roundedDist;
	
				itemParams.width = dragParams.width = activeItem.getMeasuredWidth();
				itemParams.height = dragParams.height = activeItem.getMeasuredHeight() + roundedDist;
			}
			break;

		default:
			break;
		}
		drag.setLayoutParams(dragParams);
		activeItem.setLayoutParams(itemParams);
	}
	
	private boolean checkMinWidth(RelativeLayout.LayoutParams params, float distance, Bundle itemTag, int which)
	{
		
		int minWidth = itemTag.getInt(Generator.MINWIDTH);
		int minHeight = itemTag.getInt(Generator.MINHEIGHT);
		
		switch (which)
		{
		case R.id.overlay_right: 
		case R.id.overlay_left:
			
			return params.width >= minWidth && !(params.width + distance < minWidth);
			
			
		case R.id.overlay_bottom:
		case R.id.overlay_top:
			
			return params.height >= minHeight && !(params.height + distance < minHeight);

		default:
			
		}
		return false;
	}

	/**
	 * Checks whether the current resize in progress will be larger than the
	 * workspace.
	 * 
	 * @param distance
	 *            the current distance moved from the origin of the movement
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
		case R.id.overlay_right:

			if (activeItem.getRight() + distance >= designArea.getWidth())
			{
				float overHead = (activeItem.getRight() + distance - designArea.getWidth());

				return Math.round(distance - overHead);
			}
			break;

		case R.id.overlay_left:

			if (activeItem.getLeft() - distance <= 0)
			{
				float overHead = (activeItem.getLeft() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case R.id.overlay_top:

			if (activeItem.getTop() - distance <= 0)
			{
				float overHead = (activeItem.getTop() - distance);

				return Math.round(distance + overHead);
			}
			break;

		case R.id.overlay_bottom:

			if (activeItem.getBottom() + distance >= designArea.getHeight())
			{
				float overHead = (activeItem.getBottom()
						- designArea.getHeight() + distance);

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

				overlay.setVisibility(false); // Während des Drags ist kein
												// Overlay
				toggleGrid();// sichtbar.
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:

				setStyle(DragEvent.ACTION_DRAG_ENTERED);

				break;

			case DragEvent.ACTION_DRAG_LOCATION:
				break;

			case DragEvent.ACTION_DRAG_ENDED:

				setStyle(DragEvent.ACTION_DRAG_ENDED);
				overlay.setVisibility(true); // das Overlay wird wieder
												// angezeigt, da der Drag vorbei
												// ist.

				isDragging = false;
				toggleGrid();

				break;

			case DragEvent.ACTION_DRAG_EXITED:
				setStyle(DragEvent.ACTION_DRAG_EXITED);
				break;

			case DragEvent.ACTION_DROP:

				if (secondPointer)
				{

				} else
				{

				}
				ImageButton drag = overlay.getDrag();

				int dropTargetX = checkCollisionX(event.getX());
				int dropTargetY = checkCollisionY(event.getY());

				// Positionen werden ausgelesen und zugewiesen. Objekte werden
				// an
				// ihre Zielposition verschoben und das Overlay bekommt neue
				// Koordinaten.
				RelativeLayout.LayoutParams activeParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
				RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();

				dragParams.leftMargin = snapToGrid(dropTargetX)
						+ root.getLeft();
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
		return Math.round((float) pos / SNAP_GRID_INTERVAL)
				* SNAP_GRID_INTERVAL;
	}

	private void setStyle(int event)
	{
		synchronized (activeItem)
		{
			// final Drawable defaultBackground;

			switch (event)
			{
			case DragEvent.ACTION_DRAG_STARTED:

				// defaultBackground = activeItem.getBackground();
				break;
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
		int offsetPos = Math.round(dropPosY - activeItem.getMeasuredHeight()
				/ 2);

		int maxPos = Math.round(designArea.getMeasuredHeight()
				- activeItem.getMeasuredHeight());
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

		int maxPos = Math.round(designArea.getMeasuredWidth()
				- activeItem.getMeasuredWidth());
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
	 * Entfernt das Overlay komplett.
	 * 
	 */
	private void deleteOverlay()
	{
		synchronized (parent)
		{
			if (overlay.isActive())
			{
				overlay.delete();
				//listener.objectSelected(false);

				dragIndicator = null;
				isDragging = false;
			}
		}
	}

	/**
	 * Called before and after dragging to show and hide the grid.
	 * 
	 */
	private void toggleGrid()
	{
		// synchronized (grid)
		{
			if (grid.getVisibility() == View.INVISIBLE)
			{
				grid.setVisibility(View.VISIBLE);
			} else
			{
				grid.setVisibility(View.INVISIBLE);
			}
		}

	}

	public interface onObjectSelectedListener
	{
		void objectChanged(View view);

		void objectSelected(boolean selected);
	}

	private static onObjectSelectedListener listener;

	public static void setOnObjectSelectedListener(
			onObjectSelectedListener listener)
	{
		DesignFragment.listener = listener;
	}

}
