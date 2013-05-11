package uibuilder;

import helpers.Log;

import helpers.ScreenRatioChanger;
import manipulators.Grid;
import manipulators.Overlay;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import creators.ObjectFactory;
import creators.ObjectManipulator;
import data.ObjectValues;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;


/**
 * 
 * @author funklos and jonesses
 *
 */

public class DesignFragment extends Fragment implements OnDragListener,
		OnGestureListener, OnTouchListener, OnScaleGestureListener
{
	private RelativeLayout designArea, parent;
	private Grid grid;
	private Overlay overlay;

	private View activeItem;
	//private ObjectFactory factory;
	private ObjectManipulator manipulator;

	private GestureDetector detector;
	private ScaleGestureDetector scaleDetector;
	private Boolean isPreviewing = false;
	
	private View dragIndicator;
	
	//private int nextObjectId;

	private View currentTouch;
	public static final float dings = 160f;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View root = inflater.inflate(R.layout.layout_design_fragment, container, false);
		designArea = (RelativeLayout) root.findViewById(R.id.design_area);
		parent = (RelativeLayout) designArea.getParent();
		
		designArea.post(new ScreenRatioChanger(designArea, this.getActivity()));
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		activeItem = null;

		initHelpers();
		setListeners();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStop()
	{
		Log.d("onstop designfragment", "called");
		super.onStop();
	}
	
	
	private void setListeners()
	{	
		designArea.setOnTouchListener(this);
		designArea.setOnDragListener(this);
		
		parent.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (!isPreviewing)
				{
					switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						activeItem = null;

						listener.objectSelected(false);

						if (overlay.isActive())
						{
							Log.d("Case Design Area", "overlay active and therefore deleted");

							deleteOverlay();
							return true;
						}
						break;

					default:
						break;
					}
				}
				return false;
			}
		});
	}

	/**
	 * Init helper classes
	 * factory: generates items, organizes object manipulations.
	 * detector: detects gestures, such as swipes, longpresses, drag and drop events.
	 * grid: visual helper for item repositioning and resize operations.
	 * overlay: visual helper for indication for selected elements, provides handles for resize operations.
	 * 
	 */
	private void initHelpers()
	{
		//factory = new ObjectFactory(getActivity().getApplicationContext(), this, designArea);
		
		manipulator = new ObjectManipulator(designArea);
		detector = new GestureDetector(getActivity().getApplicationContext(), this);
		scaleDetector = new ScaleGestureDetector(getActivity().getApplicationContext(), this);
		grid = new Grid(getActivity().getApplicationContext(), ObjectFactory.SNAP_GRID_INTERVAL);
		overlay = new Overlay(designArea, this);

		grid.setLayoutParams(designArea.getLayoutParams());
		parent.addView(grid);

		toggleGrid();
	}

	/**
	 * called from the uibuilderactivity to set the user selection from the
	 * itembox selection. on touch, objectrequest, this id is passed to the
	 * factory to generate the desired objecttype
	 * 
	 * @param id of the user selection in itembox
	 */
	public void setSelection(int id)
	{
		//nextObjectId = id;
	}


	/**
	 * Erfasst alle Touch-Events, setzt ggf. Flags die den Zustand der
	 * Applikation abbilden. return calls the GestureDetector instance for a
	 * matching event
	 */

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (isPreviewing)
		{
			return true;
		}
		
		int pointer = event.getPointerId(getIndex(event));

		Log.d("first down", String.valueOf(event.getPointerCount()));
		
		//catch Multitouch events
		if(pointer > 0)
		{
			return scaleDetector.onTouchEvent(event);
		}
		
		int action = event.getAction() & MotionEvent.ACTION_MASK;

		switch (action)
		{
		
			case MotionEvent.ACTION_POINTER_DOWN:
				
				
				break;
		
			case MotionEvent.ACTION_DOWN:
	
				currentTouch = v;
				listener.objectChanged(currentTouch);

	
				switch (currentTouch.getId())
				{
					/*
					 * touch on designarea
					 */
					case R.id.design_area:
						Log.d("DesignArea", "called");
						
						if (deselect())
						{
							return true;
						}
						Log.d("layout forward", "called");
						break;
	
					/*
					 * touch on overlay
					 */
					case R.id.overlay_top:
					case R.id.overlay_right:
					case R.id.overlay_bottom:
					case R.id.overlay_left:
					case R.id.overlay_drag:
		
						selectOverlay();
						break;
		
					/*
					 * touch on object
					 */
					default:
						Log.d("Default case in ontouch", "called");
		
						if (selectItem())
						{
							return true;
						}
						break;
				}
				break;
				
				//This part of code works but is flickering, provides multitouch
				//resizing when scalelistener disabled. we still have to work on this
/*
		case MotionEvent.ACTION_MOVE:
			currentTouch = v;
			designArea.invalidate();
			
			switch (currentTouch.getId())
			{
				
				 * touch on overlay
				 
				case R.id.overlay_top:
				case R.id.overlay_right:
				case R.id.overlay_bottom:
				case R.id.overlay_left:
				case R.id.overlay_drag:
	
					selectOverlay();
					break;
	
			}
			break;
*/
		case MotionEvent.ACTION_UP:

			if (dragIndicator != null)
			{
				Log.d("drag indikator", "drag handle disabled");
				dragIndicator.setActivated(false);
				isresizing = false;
			}
			break;

		default:
			break;
		}
	
		/**
		 * @return forward the touch event to the gesturedetector for further
		 *         processing
		 */
		return detector.onTouchEvent(event);
	}

	/**
	 * A motionevent was performed on an item.
	 * Check if there was an element active before, if true set its state to
	 * disabled, set the current item as active and generate an overlay to indicate the selection.
	 */
	private boolean selectItem()
	{
		listener.objectSelected(true);

		/*
		 * switch the overlay if another item was active
		 */
		if (overlay.isActive() && currentTouch != activeItem)
		{
			Log.d("Default case in ontouch", "deleting overlay");

			deleteOverlay();
		}
		activeItem = currentTouch;
		detector.setIsLongpressEnabled(true);

		if (!overlay.isActive())
		{
			overlay.generate(activeItem);

			detector.setIsLongpressEnabled(false);
			return true;
		}
		else return false;
	}

	/**
	 * A motionevent on one of the overlay elements was performed.
	 * Set its state to active.
	 */
	private void selectOverlay()
	{
		detector.setIsLongpressEnabled(false);
		dragIndicator = currentTouch;
		dragIndicator.setActivated(true);

		Log.d("dragOverlay", "drag handle selected");
	}

	/**
	 * A motion event on the designArea was performed.
	 * Check if there is an overlay active.
	 * If <b>true</b>, disable the overlay and <b>null</b> the activeItem
	 * else
	 * no operation is necessary.
	 */
	private boolean deselect()
	{
		detector.setIsLongpressEnabled(false);
		activeItem = null;

		listener.objectSelected(false);

		if (overlay.isActive())
		{
			Log.d("Case Design Area", "overlay active and therefore deleted");

			deleteOverlay();
			return true;
		}
		else return false;
	}

	boolean secondPointer = false;

	private int getIndex(MotionEvent event)
	{
		return (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	}

	@Override
	public boolean onDown(MotionEvent event)
	{
		Log.d("Ondown", "is called");
		return false;//createObject(event);
		/** If the Object was created the event is consumed. */
	}

	/**
	 * No method calls here because the discriminative power of this method is
	 * too low
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		return false;
	}

	/**
	 * Creates a Button on the specified position.
	 * 
	 * @param clickPosX The coordinate on the X-axis
	 * @param clickPosY The coordinate on the Y-axis
	 * @return <b>true</b> if the conditions for creation are met, else <b>false</b>
	 *//*
	private boolean createObject(MotionEvent event)
	{
		if (activeItem == null && !overlay.isActive() && nextObjectId != 0)
		{
			View newOne = (View) factory.getElement(nextObjectId, event);

			activeItem = newOne;
			
			return true;
		}
		else return false;
	}*/

	/**
	 * force redraw
	 */
	private void invalidate()
	{
		designArea.requestLayout();
	}

	
	@Override
	public boolean onDrag(View root, DragEvent event)
	{
		if (!isPreviewing)
		{
			switch (event.getAction())
			{
			case DragEvent.ACTION_DRAG_STARTED: 
				
				adaptToStarted();
				break;
				
			case DragEvent.ACTION_DRAG_ENTERED:
				
				adaptToEnter();
				break;

			case DragEvent.ACTION_DRAG_LOCATION:
				break;

			case DragEvent.ACTION_DRAG_ENDED:

				adaptToNormal();
				break;

			case DragEvent.ACTION_DRAG_EXITED: 
				
				adaptToExit();
				break;

			case DragEvent.ACTION_DROP: 
				// check minpositions, hide grid, display overlay at new position and reposition the element at droptarget
				
				View v = (View) event.getLocalState();
				//ViewGroup parent = (ViewGroup) v.getParent();
				//parent.removeView(v);
				ClipData.Item item = event.getClipData().getItemAt(0);
				if (item.getText().equals("itembox"))
				{
					designArea.addView(v);
				}
				
				overlay.generate(v);
				overlay.setVisibility(false);
				
				manipulator.performDrop(event, v, overlay.getDrag());
				//factory.performDrop(event, activeItem, overlay.getDrag());
				Log.d("action drop", "registered");
				v.setVisibility(View.VISIBLE);
				v.setBackgroundResource(R.drawable.object_background_default);
				
				activeItem = v;
				break;
			}
		}
		return true;
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
	 */ 
 	@Override
	public boolean onScroll(MotionEvent start, MotionEvent end, float distanceX,
			float distanceY)
	{
		if (!isPreviewing)
		{
			invalidate();
			if (dragIndicator != null && activeItem != null)
			{
				int which = dragIndicator.getId();

				switch (which)
				{
				case R.id.overlay_drag:

					requestDrag();
					break;
					
				default:
					requestResize(which, start, end);
				}
			}
		}
		return false;
	}
	boolean isresizing = false;
	/**
	 * Request a resize of the currently active item.
	 * @param which the overlay handle that triggered the resize
	 * @param start of the movement
	 * @param end actual values of the event
	 */
	private void requestResize(int which, MotionEvent start, MotionEvent end)
	{
		isresizing = true;
		manipulator.setParams(which, start, end, activeItem, overlay.getDrag());
	}

	/**
	 * Start the requested drag event
	 */
	private void requestDrag()
	{
		Bundle tagBundle = (Bundle) activeItem.getTag();
		int id = tagBundle.getInt(ObjectValues.TYPE);
		
		// Generate clipdata to provide to the dragshadowbuilder
		 
		ClipData.Item item = new ClipData.Item(String.valueOf(id));
		ClipData clipData = new ClipData((CharSequence) String.valueOf(id), new String[]
		{ ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

		activeItem.startDrag(clipData, new View.DragShadowBuilder(activeItem), activeItem, 0);
	}


	/**
	 * Indicate that the drag has left the droppable area.
	 */
	private void adaptToExit()
	{
		manipulator.setStyle(DragEvent.ACTION_DRAG_EXITED, activeItem);
	}

	/**
	 * Indicate  that the droppable area was entered again.
	 */
	private void adaptToEnter()
	{
		manipulator.setStyle(DragEvent.ACTION_DRAG_ENTERED, activeItem);
	}

	/**
	 * Reset the item style to default, show the overlay and hide the grid.
	 * Notify listeners of the ended drag event
	 */
	private void adaptToNormal()
	{
		manipulator.setStyle(DragEvent.ACTION_DRAG_ENDED, activeItem);
		overlay.setVisibility(true);
		toggleGrid();

		if (activeItem == null)
		{
			listener.objectSelected(false);
		} else
		{
			listener.objectChanged(activeItem);
			listener.objectSelected(true);
		}
		Log.d("dragging", "ended");
	}

	/**
	 * hide the overlay, show grid for positioning, 
	 * set style of active item to indicate old position
	 * notify the listeners that a drag is in progress
	 */
	private void adaptToStarted()
	{
		listener.objectDragging();

		overlay.setVisibility(false);
		toggleGrid();
		manipulator.setStyle(DragEvent.ACTION_DRAG_STARTED, activeItem);
	}


	/**
	 * Entfernt das Overlay komplett.
	 * 
	 * @author funklos
	 */
	protected boolean deleteOverlay()
	{
		synchronized (parent)
		{
			if (overlay.isActive())
			{
				overlay.delete();

				dragIndicator = null;
				return true;
			}
			else return false;
		}
	}

	/**
	 * Called before and after dragging to show and hide the grid.
	 * 
	 * @author funklos
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

	/**
	 * Call the implementing listener to notify of selection changes on the
	 * designArea.
	 * objectchanged: another object has focus, pass it to adapt to new conditions @see EditmodeFragment and @see Module
	 * objectselected: used to trigger the @see EditmodeFragment
	 * objectdragging: a drag is in progress, show the @see DeleteFragment
	 * @author funklos
	 * 
	 */
	public interface onObjectSelectedListener
	{
		void objectChanged(View view);

		void objectSelected(boolean selected);

		void objectDragging();
	}

	private static onObjectSelectedListener listener;

	protected static void setOnObjectSelectedListener(
			onObjectSelectedListener listener)
	{
		DesignFragment.listener = listener;
	}

	/**
	 * called from the parent activity @see UiBuilderActivity
	 * to perform a requested delete operation
	 */
	protected void performDelete()
	{
		designArea.removeView(activeItem);
		
		removeFromDb();
		activeItem = null;
		dragIndicator = null;
		
		overlay.delete();
	}

	/**
	 * removes the object from the database
	 */
	private void removeFromDb()
	{
		ContentResolver cres = getActivity().getContentResolver();
		
		Bundle b = (Bundle) activeItem.getTag();
		
		int id = b.getInt(ObjectValues.DATABASE_ID);
		Uri uri = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_OBJECTS, id);
		
		if (id != 0)
		{	
			cres.delete(uri, null, null);
		}
	}

	/**
	 * called from @see UiBuilderActivity when the user has selected the preview option
	 * @param disable
	 */
	protected void disableTouch(boolean disable)
	{
		isPreviewing = disable;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector)
	{
		// TODO Auto-generated method stub
		Log.d("scale", "begin");
		return false;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector)
	{
		// TODO Auto-generated method stub
		
	}
}
