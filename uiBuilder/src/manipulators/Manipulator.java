package manipulators;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;

public class Manipulator implements OnLongClickListener, OnDragListener
{
	public View.OnLongClickListener addLongClickListener(View v)
	{
		return this;
	}
	
	@Override
	public boolean onDrag(View arg0, DragEvent arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongClick(View arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
