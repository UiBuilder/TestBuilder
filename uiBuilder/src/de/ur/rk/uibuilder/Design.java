package de.ur.rk.uibuilder;

import de.ur.rk.uibuilder.creators.ObjectFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class Design extends Activity {

	private AbsoluteLayout root;
	private Button addView;
	
	private ObjectFactory factory; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_design);
		
		linkElements();
		initHelpers();
	}

	private void initHelpers() {
		// TODO Auto-generated method stub
		factory = new ObjectFactory(getApplicationContext());
	}

	private void linkElements() {
		// TODO Auto-generated method stub
		root = (AbsoluteLayout) findViewById(R.id.design_area);
		addView = (Button) findViewById(R.id.design_button_add);
		
		setListeners();
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		addView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newView();
			}

			private void newView() {
				// TODO Auto-generated method stub
				Button newOne = (Button) factory.getElement(ObjectFactory.ID_BUTTON);
				root.addView(newOne);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.design, menu);
		return false;
	}
//test
}
