package uibuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import de.ur.rk.uibuilder.R;

public class ManagerActivity extends Activity
{

	private GridView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		

		grid = (GridView)findViewById(R.id.manager_activity_project_grid);
		//grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

}
