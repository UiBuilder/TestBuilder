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


	private RelativeLayout root;

	private ObjectFactory factory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_design);

		linkElements();
		setListeners();
		initHelpers();
	}

	private void initHelpers() {
		factory = new ObjectFactory(getApplicationContext());
	}

	private void linkElements() {
		root = (RelativeLayout) findViewById(R.id.design_area);


	}

	private void setListeners() {
		root.setOnTouchListener(new OnTouchListener() {


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

					View v = factory.getManipulator().getActiveItem();

					v.setX(event.getX() - (v.getWidth() / 2));
					v.setY(event.getY() - (v.getHeight() / 2));
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
