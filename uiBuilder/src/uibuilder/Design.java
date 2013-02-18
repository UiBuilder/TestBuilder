package uibuilder;

import helpers.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import creators.ObjectFactory;
import de.ur.rk.uibuilder.R;

public class Design extends Activity {
	private float dragx;
	private float dragy;

	private RelativeLayout root;
	// private Button addView;

	private ObjectFactory factory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_design);

		linkElements();
		initHelpers();
	}

	private void initHelpers() {
		factory = new ObjectFactory(getApplicationContext());
	}

	private void linkElements() {
		root = (RelativeLayout) findViewById(R.id.design_area);
		// addView = (Button) findViewById(R.id.design_button_add);

		setListeners();
	}

	private void setListeners() {
		root.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// holt die Koordinaten des Touch-Punktes
					float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
					float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);
					// erstellt den Button an den zuvor ermittelten Koordinaten
					Button newOne = (Button) factory
							.getElement(ObjectFactory.ID_BUTTON);
					root.addView(newOne);
					
					Log.d("Button Width", String.valueOf(newOne.getWidth()));
					Log.d("Button Height",String.valueOf(newOne.getHeight()) );
					newOne.setX(clickPosX - (newOne.getWidth() / 2));
					newOne.setY(clickPosY- (newOne.getHeight() / 2));
					
				}
				return true;

			}
		});

		root.setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View parent, DragEvent event) {

				switch (event.getAction()) {
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
					// v.setX(event.getX());
					// v.setY(event.getY());

					// MarginLayoutParams marginParams = new
					// MarginLayoutParams(v.getLayoutParams());

					// marginParams.topMargin = (int)event.getY();//
					// -(v.getHeight());
					// marginParams.leftMargin = (int)event.getX();// -
					// (v.getWidth()/2);
					Log.d("try get item", "trying!");
					View v = factory.getManipulator().getActiveItem();
					Log.d("get item", "got!");
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							v.getLayoutParams());
					Log.d("searching bug", "params!");
					// RelativeLayout.LayoutParams params = new
					// RelativeLayout.LayoutParams(marginParams);

					// params.setMargins((int)event.getX(), (int)event.getY(),
					// 0, 0);
					Log.d("searching bug", "margins!");
					// v.setLayoutParams(params);

					Log.d("event", "top" + String.valueOf(event.getY())
							+ "left" + String.valueOf(event.getX()));
					// Log.d("params","top"+String.valueOf(params.topMargin)+"left"+String.valueOf(params.leftMargin));
					// Log.d("pos margins","top"+v.getTop()+"left"+v.getLeft());
					// Log.d("pos get","top"+v.getY()+"left"+v.getX());
					v.setX(event.getX() - (v.getWidth() / 2));
					v.setY(event.getY() - (v.getHeight() / 2));
					// v.setLayoutParams(params);
					Log.d("searching bug", "pos set!");
				}
				return true;
			}
		});
	}

	protected Builder createItemChooseDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.design, menu);
		return false;
	}
}
