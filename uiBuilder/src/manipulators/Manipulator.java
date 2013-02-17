package manipulators;

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
	public boolean onDrag(View arg0, DragEvent arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongClick(View arg0)
	{
		// TODO Auto-generated method stub
		Toast.makeText(context.getApplicationContext(), 
                "Button is clicked", Toast.LENGTH_LONG).show();
		
		return false;
	}

}
