package uibuilder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.ur.rk.uibuilder.R;

public class DeleteFragment extends Fragment implements OnDragListener
{
	private Button delete;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.layout_deletebox_fragment, container, false);
		
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
			delete.setBackgroundResource(R.drawable.ui_deletebox_active_border);
			delete.setShadowLayer(2f, 1f, 1f, R.color.element_out_of_dropzone);
			return true;
					
		case DragEvent.ACTION_DRAG_LOCATION:
			return true;
			
		case DragEvent.ACTION_DRAG_EXITED:
			delete.setBackgroundResource(R.drawable.default_button_border);
			
			return false;
			
		case DragEvent.ACTION_DROP:
			delete.setBackgroundResource(R.drawable.default_button_border);
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
