package uibuilder;

import android.app.Activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

/**
 * Displays an explanation of the functionality of the app
 * @author jonesses and funklos
 *
 */

public class HelpActivity extends Activity
{
	RelativeLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_help_layout);
		
		root = (RelativeLayout) findViewById(R.id.activity_help_root);
		root.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
				
			}
		});

	}
	
}
