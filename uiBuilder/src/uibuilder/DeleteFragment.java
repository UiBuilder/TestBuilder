package uibuilder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class DeleteFragment extends Fragment implements OnDragListener
{
	private Button delete;
	private LinearLayout container;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_deletebox_fragment, container, false);
		
		this.container = (LinearLayout) root.findViewById(R.id.deletebox_container);
		delete = (Button) root.findViewById(R.id.deletebox_delete_area);
		root.setOnDragListener(this);
		
		return root;
	}

	@Override
	public boolean onDrag(View draggingView, DragEvent event)
	{
		switch (event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			
			return true;
			
		case DragEvent.ACTION_DRAG_ENTERED:
			container.setBackgroundResource(R.drawable.ui_deletebox_active_border);

			return true;
					
		case DragEvent.ACTION_DRAG_LOCATION:
			return true;
			
		case DragEvent.ACTION_DRAG_EXITED:
			container.setBackgroundResource(R.drawable.default_object_border);
			
			return false;
			
		case DragEvent.ACTION_DROP:
			container.setBackgroundResource(R.drawable.default_object_border);
			listener.requestDelete();
			return true;

		default:
			break;
		}
		
		return false;
	}

	/**
	 * 
	 * @author funklos
	 *
	 */
	public interface onDeleteRequestListener
	{
		void requestDelete();
	}

	private static onDeleteRequestListener listener;

	public static void onDeleteRequestListener(
			onDeleteRequestListener listener)
	{
		DeleteFragment.listener = listener;
	}
}
