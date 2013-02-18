package manipulators;

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
import android.widget.Toast;
import creators.ObjectFactory;

public class TheBoss implements OnLongClickListener, OnDragListener, OnTouchListener
{
	private Context context;
	private View activeItem;
	private ObjectFactory factory;
	private RelativeLayout root;
	
	private DragEvent start;
	
	/**
	 * KONSTRUKTOR
	 * @param context
	 */
	public TheBoss(Context context, RelativeLayout root)
	{
		super();
		this.context = context;
		factory = new ObjectFactory(context, this);
		this.root = root;
		
		activeItem = null;
	}

	
	
	@Override
	public boolean onDrag(View root, DragEvent event)
	{
		
			switch (event.getAction()) 
			{
			case DragEvent.ACTION_DRAG_STARTED:
				start = event;
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_LOCATION:

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				
				if(isFling(start, event))
				{
					// DELETE ACTION
				}
				break;
			case DragEvent.ACTION_DROP:
				
				if(isFling(start, event))
				{
					// DELETE ACTION
				}
				else
				{
					//Drop-Action
					activeItem.setX(event.getX() - (activeItem.getWidth() / 2));
					activeItem.setY(event.getY() - (activeItem.getHeight() / 2));
				}
				activeItem = null;
			}
			return true;
	}

	private boolean isFling(DragEvent start, DragEvent end)
	{
		float minDistance = 100.0f; //WERT noch zu ermitteln
		
		if (Math.abs(start.getX()-end.getX()) >= minDistance || Math.abs(start.getY()-end.getY()) >= minDistance)
		{
			//TODO
			//if () Endzeit - Startzeit <= maxTime
			// return true
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v)
	{		
		activeItem = v;
		
		Toast.makeText(context.getApplicationContext(), "Button " + v.getId() + " is clicked", Toast.LENGTH_SHORT).show();
	
		ClipData.Item item = new ClipData.Item((String) v.getTag());
		ClipData clipData = new ClipData((CharSequence) v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		
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

			// erstellt den Button an den zuvor ermittelten Koordinaten
			Button newOne = (Button) factory.getElement(ObjectFactory.ID_BUTTON);
			
			root.addView(newOne);

			//DOES NOT WORK
			newOne.setX(clickPosX - (newOne.getWidth() / 2));
			newOne.setY(clickPosY- (newOne.getHeight() / 2));			
		}
		return true;
	}

}
