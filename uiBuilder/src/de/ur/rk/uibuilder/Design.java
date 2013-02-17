package de.ur.rk.uibuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.creators.ObjectFactory;


public class Design extends Activity 
{

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
