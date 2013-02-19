package uibuilder;

import manipulators.TheBoss;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class Design extends Activity
{

	private RelativeLayout root;

	private TheBoss manipulator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_design);

		linkElements();
		initHelpers();
		setListeners();
	}

	private void initHelpers()
	{
		manipulator = new TheBoss(this, root);
	}

	private void linkElements()
	{
		root = (RelativeLayout) findViewById(R.id.design_area);

	}

	private void setListeners()
	{
		root.setOnTouchListener(manipulator);

		root.setOnDragListener(manipulator);
	}

	protected Builder createItemChooseDialog()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.design, menu);
		return false;
	}
}
