package uibuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class AboutActivity extends Activity
{
	RelativeLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_about_layout);
		
		root = (RelativeLayout) findViewById(R.id.activity_about_root);
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
