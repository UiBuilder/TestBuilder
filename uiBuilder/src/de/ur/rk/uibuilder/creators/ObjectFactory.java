package de.ur.rk.uibuilder.creators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;

public class ObjectFactory {

	public static final int ID_BUTTON = 1;
	public static final int ID_TEXTVIEW = 2;
	
	private Context ref;
	private int idCount;
	
	public ObjectFactory(Context c) {
		super();
		// TODO Auto-generated constructor stub
		ref = c;
		idCount = 1;
	}

	public View getElement (int which)
	{
		switch (which)
		{
			case ID_BUTTON: return newButton();
				
			case ID_TEXTVIEW: return newTextview();
				
		}
		
		return null;
	}

	private View newTextview() {
		// TODO Auto-generated method stub
		return null;
	}

	private View newButton() {
		// TODO Auto-generated method stub
		Button newB = new Button(ref);
		
		newB.setText("Button");
		newB.setId(idCount);
		idCount++;
		
		@SuppressWarnings("deprecation")
		ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		
		newB.setLayoutParams(params);
		newB.setX(0);
		newB.setY(0);
		
		return newB;
	}
}
