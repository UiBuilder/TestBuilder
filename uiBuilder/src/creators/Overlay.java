package creators;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

class Overlay
{
	public final static String OVERLAYTAG = "Overlay";
	
	public static void getOverlay(Context context, RelativeLayout root, View activeItem, OnTouchListener listener)
	{
		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(activeItem.getLayoutParams());
		int dragId = 1111;
		Log.d("params left", String.valueOf(modified.leftMargin));
		
		//DRAG
		Button drag = new Button(context);
		modified.leftMargin = activeItem.getLeft();
		modified.topMargin = activeItem.getTop();
		modified.width = activeItem.getMeasuredWidth();
		drag.setBackgroundResource(android.R.color.background_dark);
		drag.setAlpha(0.5f);
		drag.setId(dragId);
		drag.setTag(OVERLAYTAG);
		drag.setOnTouchListener(listener);
		root.addView(drag, modified);
		root.requestLayout();
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//RIGHT
		Button right = new Button(context);
		right.setBackgroundResource(android.R.color.holo_orange_light);
		right.setAlpha(0.5f);
		modified.addRule(RelativeLayout.ALIGN_TOP, dragId);
		modified.addRule(RelativeLayout.RIGHT_OF, dragId);
		modified.addRule(RelativeLayout.ALIGN_BOTTOM, dragId);
		right.setId(1112);
		right.setTag(OVERLAYTAG);
		root.addView(right, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//BOTTOM
		Button bottom = new Button(context);
		bottom.setBackgroundResource(android.R.color.holo_orange_light);
		bottom.setAlpha(0.5f);
		modified.addRule(RelativeLayout.BELOW, dragId);
		modified.addRule(RelativeLayout.ALIGN_LEFT, dragId);
		modified.addRule(RelativeLayout.ALIGN_RIGHT, dragId);
		bottom.setId(1113);
		bottom.setTag(OVERLAYTAG);
		root.addView(bottom, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//LEFT
		Button left = new Button(context);
		left.setBackgroundResource(android.R.color.holo_orange_light);
		left.setAlpha(0.5f);
		modified.addRule(RelativeLayout.LEFT_OF, bottom.getId());
		modified.addRule(RelativeLayout.ALIGN_TOP, right.getId());
		modified.addRule(RelativeLayout.ABOVE, bottom.getId());
		left.setId(1114);
		left.setTag(OVERLAYTAG);
		root.addView(left, modified);
		
		modified = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//TOP
		Button top = new Button(context);
		top.setBackgroundResource(android.R.color.holo_orange_light);
		top.setAlpha(0.5f);
		modified.addRule(RelativeLayout.ABOVE, right.getId());
		modified.addRule(RelativeLayout.LEFT_OF, right.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, left.getId());
		top.setTag(OVERLAYTAG);
		root.addView(top, modified);
		
		root.requestLayout(); 
	}

}
