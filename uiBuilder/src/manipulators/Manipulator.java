package manipulators;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class Manipulator implements OnLongClickListener, OnDragListener
{
	private Context context;
	
	public Manipulator(Context context)
	{
		super();
		this.context = context;
	}

	public View.OnLongClickListener addLongClickListener(View v)
	{
		return this;
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event)
	{
		/*switch(event.getAction())
		{
	    case DragEvent.ACTION_DRAG_STARTED:
	        break;
	    case DragEvent.ACTION_DRAG_ENTERED:
	        break;
	    case DragEvent.ACTION_DRAG_LOCATION:
	        break;
	    case DragEvent.ACTION_DRAG_ENDED:
	    	v.setX(event.getX());
	    	v.setY(event.getY());
	        break;
	    case DragEvent.ACTION_DRAG_EXITED:
	        break;
	    case DragEvent.ACTION_DROP:
	    	v.setX(event.getX());
	    	v.setY(event.getY());
	    	//return true;
	        //break;
		}
		return true;*/
		return false;
	}

	@Override
	public boolean onLongClick(View v)
	{
		Toast.makeText(context.getApplicationContext(), 
                "Button is clicked", Toast.LENGTH_LONG).show();
	
		ClipData.Item item = new ClipData.Item((String) v.getTag());
		ClipData clipData = new ClipData((CharSequence) v.getTag(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);

		
		return true;

	}

}
