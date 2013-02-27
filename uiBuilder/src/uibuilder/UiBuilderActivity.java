package uibuilder;

import manipulators.TheBoss;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity
{

	private RelativeLayout root;

	private TheBoss manipulator;
	private ItemboxFragment itembox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fragment_container);
		itembox = (ItemboxFragment) getFragmentManager().findFragmentById(R.id.fragment_itembox);
		
/*		
		linkElements();	
		initHelpers();
		setListeners();*/
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
	}
/*	

	private void initHelpers()
	{
		manipulator = new TheBoss(getApplicationContext(), root);
		
	}
*/
	private Button createButton, createTextView, createImage;
/*	private void linkElements()
	{
		root = (RelativeLayout) findViewById(R.id.design_area);
		
		createButton = (Button) findViewById(R.id.new_element_button);
		createTextView = (Button) findViewById(R.id.new_element_textview);
		createImage = (Button) findViewById(R.id.new_element_imageview);

		
		createButton.setTag(ObjectFactory.ID_BUTTON);
		createTextView.setTag(ObjectFactory.ID_TEXTVIEW);
		createImage.setTag(ObjectFactory.ID_IMAGEVIEW);
		
		createButton.setOnClickListener(this);
		createTextView.setOnClickListener(this);
		createImage.setOnClickListener(this);
	}*/
/*
	private void setListeners()
	{
		root.setOnTouchListener(manipulator);
		root.setOnDragListener(manipulator);
	}
*/
	protected Builder createItemChooseDialog()
	{
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public void onClick(View v)
	{
		if(v.getTag() != null)
		switch (Integer.valueOf(v.getTag().toString() ))
		{
		case ObjectFactory.ID_BUTTON:
			manipulator.setObjectType(ObjectFactory.ID_BUTTON);
			break;
			
		case ObjectFactory.ID_TEXTVIEW:
			manipulator.setObjectType(ObjectFactory.ID_TEXTVIEW);
			break;
			
		case ObjectFactory.ID_IMAGEVIEW:
			manipulator.setObjectType(ObjectFactory.ID_IMAGEVIEW);
			break;

		default:
			break;
		}
		
	}
*/	
	public void setSelectedType(int id)
	{
		DesignFragment design = (DesignFragment) getFragmentManager().findFragmentById(R.id.fragment_design);
		design.setSelection(id);
	}
	
	private int selectedType;
	/*
	public int getSelectedType()
	{
		return selectedType;
	}
	*/
	
}
