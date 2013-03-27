package editmodules;


import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class AlignModule extends Module
{
	public AlignModule(Context context)
	{
		super(context);
	}

	private View box;
	
	private View requesting;
	
	private Button topLeft, topCenter, topRight, centerLeft, centerCenter,
	centerRight, bottomLeft, bottomCenter, bottomRight;
	


	/**
	 * 
	 */
	protected void setupUi()
	{
		box = super.inflater.inflate(R.layout.editmode_entry_align_content, null);
		box.setOnClickListener(new ExpansionListener(box));
		
		topLeft = (Button) box.findViewById(R.id.editmode_align_top_left);
		topCenter = (Button) box.findViewById(R.id.editmode_align_top_center);
		topRight = (Button) box.findViewById(R.id.editmode_align_top_right);
		centerLeft = (Button) box.findViewById(R.id.editmode_align_center_left);
		centerCenter = (Button) box.findViewById(R.id.editmode_align_center_center);
		centerRight = (Button) box.findViewById(R.id.editmode_align_center_right);
		bottomLeft = (Button) box.findViewById(R.id.editmode_align_bottom_left);
		bottomCenter = (Button) box.findViewById(R.id.editmode_align_bottom_center);
		bottomRight = (Button) box.findViewById(R.id.editmode_align_bottom_right);

		AlignModuleListener alignListener = new AlignModuleListener();
		
		topLeft.setOnClickListener(alignListener);
		topCenter.setOnClickListener(alignListener);
		topRight.setOnClickListener(alignListener);
		centerLeft.setOnClickListener(alignListener);
		centerCenter.setOnClickListener(alignListener);
		centerRight.setOnClickListener(alignListener);
		bottomLeft.setOnClickListener(alignListener);
		bottomCenter.setOnClickListener(alignListener);
		bottomRight.setOnClickListener(alignListener);
	}
	
	public View getInstance(View inProgress)
	{	
		requesting = inProgress;
		adaptAlignButtons();
		
		return box;
	}
	
	private void adaptAlignButtons()
	{
		clearAlignSelection();
		Log.d("adapt", "called");
		Log.d("value", String.valueOf(((TextView) requesting).getGravity()));
		switch (((TextView) requesting).getGravity())
		{
			case Gravity.TOP | Gravity.LEFT:
			topLeft.setActivated(true);
			break;
			
			case Gravity.TOP | Gravity.CENTER_HORIZONTAL:
			topCenter.setActivated(true);
			break;
			
			case Gravity.TOP | Gravity.RIGHT:
			topRight.setActivated(true);
			break;
			
			case Gravity.CENTER_VERTICAL | Gravity.LEFT:
			centerLeft.setActivated(true);
			break;
			
			case Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL:
			centerCenter.setActivated(true);
			break;
			
			case Gravity.CENTER_VERTICAL | Gravity.RIGHT:
			centerRight.setActivated(true);
			break;
			
			case Gravity.BOTTOM | Gravity.LEFT:
			bottomLeft.setActivated(true);
			break;
			
			case Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL:
			bottomCenter.setActivated(true);
			break;
			
			case Gravity.BOTTOM | Gravity.RIGHT:
			bottomRight.setActivated(true);
			break;
		}
		box.invalidate();
	}
	
	protected void clearAlignSelection()
	{
		topLeft.setActivated(false);
		topCenter.setActivated(false);
		topRight.setActivated(false);
		centerLeft.setActivated(false);
		centerCenter.setActivated(false);
		centerRight.setActivated(false);
		bottomLeft.setActivated(false);
		bottomCenter.setActivated(false);
		bottomRight.setActivated(false);
	}
	
	private class AlignModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			clearAlignSelection();
			v.setActivated(true);

			switch (v.getId())
			{

			case R.id.editmode_align_top_left:
				((TextView) requesting).setGravity(Gravity.LEFT);
				break;
			case R.id.editmode_align_top_center:
				((TextView) requesting).setGravity(Gravity.CENTER_HORIZONTAL);
				break;
			case R.id.editmode_align_top_right:
				((TextView) requesting).setGravity(Gravity.RIGHT);
				break;
			case R.id.editmode_align_center_left:
				((TextView) requesting).setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_center_center:
				((TextView) requesting).setGravity(Gravity.CENTER);

				break;
			case R.id.editmode_align_center_right:
				((TextView) requesting).setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_bottom_left:
				((TextView) requesting).setGravity(Gravity.LEFT
						| Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_center:
				((TextView) requesting).setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_right:
				((TextView) requesting).setGravity(Gravity.RIGHT
						| Gravity.BOTTOM);

				break;

			}
		}
	}

	@Override
	public void getValues()
	{
		adaptAlignButtons();		
	}

	
}
