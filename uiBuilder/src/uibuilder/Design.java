package uibuilder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import creators.ObjectFactory;
import de.ur.rk.uibuilder.R;


public class Design extends Activity 
{
	private float dragx;
	private float dragy;
	
	private RelativeLayout root;
	private Button addView;
	
	private ObjectFactory factory; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_design);
		
		linkElements();
		initHelpers();
	}

	private void initHelpers() 
	{
		factory = new ObjectFactory(getApplicationContext());
	}

	private void linkElements() 
	{
		root = (RelativeLayout) findViewById(R.id.design_area);
		addView = (Button) findViewById(R.id.design_button_add);
		
		setListeners();
	}

	private void setListeners() 
	{
		addView.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				newView();
			}

			private void newView() 
			{
				Button newOne = (Button) factory.getElement(ObjectFactory.ID_BUTTON);
				root.addView(newOne);
				root.invalidate();
			}
		});
		
		root.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View v, DragEvent event)
			{
				
				switch(event.getAction())
				{
			    case DragEvent.ACTION_DRAG_STARTED:
			    	
			        break;
			    case DragEvent.ACTION_DRAG_ENTERED:
			        break;
			    case DragEvent.ACTION_DRAG_LOCATION:
			        break;
			    case DragEvent.ACTION_DRAG_ENDED:
			        break;
			    case DragEvent.ACTION_DRAG_EXITED:
			        break;
			    case DragEvent.ACTION_DROP:
			    	MarginLayoutParams marginParams = new MarginLayoutParams(v.getLayoutParams());
			    	
			    	marginParams.topMargin = (int)event.getY();// - (v.getHeight()); 
			    	marginParams.leftMargin = (int)event.getX();// - (v.getWidth()/2);
			    	
			    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(marginParams);
			    	Log.d("paramstop", String.valueOf(marginParams.topMargin));
			    	v.setLayoutParams(params);
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.design, menu);
		return false;
	}
}
