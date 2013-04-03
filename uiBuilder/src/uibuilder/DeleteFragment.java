package uibuilder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class DeleteFragment extends Fragment implements OnDragListener
{
	private LinearLayout container;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_deletebox_fragment, container, false);
		
		this.container = (LinearLayout) root.findViewById(R.id.deletebox_container);
		container.setOnDragListener(this);
		
		return root;
	}

	/**
	 * Adjust the containers visual presentation to reflect the events.
	 */
	@Override
	public boolean onDrag(View draggingView, DragEvent event)
	{
		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			Log.d("drag", "registerd");
			return true;
			
		case DragEvent.ACTION_DRAG_ENTERED:
			
			container.setBackgroundResource(R.drawable.ui_deletebox_active_border);
			return true;
					
		case DragEvent.ACTION_DRAG_LOCATION:
			return true;
			
		case DragEvent.ACTION_DRAG_EXITED:
			
			container.setBackgroundResource(R.drawable.object_background_default);
			return false;
			
		case DragEvent.ACTION_DROP:
			
			container.setBackgroundResource(R.drawable.object_background_default);
			listener.requestDelete();
			return true;

		default:
			break;
		}
		
		return false;
	}

	/**
	 * The interface callback is called when the user successfully dropped the item
	 * on the drop area.
	 * The DeleteFragment just request a delete operation, the interface is implemented by
	 * the @see uibuilderactivity, handles the event.
	 * @author funklos
	 *
	 */
	public interface onDeleteRequestListener
	{
		void requestDelete();
	}

	private static onDeleteRequestListener listener;

	public static void setOnDeleteRequestListener(
			onDeleteRequestListener listener)
	{
		DeleteFragment.listener = listener;
	}
}
