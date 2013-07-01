package helpers;

import android.content.Context;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

/**
 * Resizes the drawing area to match with the dimensions of a real screen device.
 * In this version of the app we use a default ratio of 16:10.
 * 
 * This class has to be posted as runnable and is run after the layout process is finsihed to apply the resizing.
 * Else no measurements would be possible.
 * @author funklos
 *
 */
public class ScreenRatioChanger implements Runnable
{
	//protected final static int SCREENRATIO_DEFAULT = 160;
	private RelativeLayout designArea;
	private Context context;
	
	private int handleSize;
	
	public ScreenRatioChanger(RelativeLayout designArea, Context context)
	{
		this.designArea = designArea;
		this.context = context;
		
		handleSize = this.context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
	}
	
	@Override
	public void run()
	{
		
		resizeDrawingArea();
		//resizeForPreview();
	}

	/**
	 * @author funklos Resizes the designArea according to the screen
	 *         size after the layouting process has finished, to have
	 *         access to measured dimensions
	 */
	private void resizeDrawingArea()
	{
		//float ratio = SCREENRATIO_DEFAULT;
		int rootHeight = designArea.getMeasuredHeight() - 2 * handleSize;
		
		Log.d("height of design frag", String.valueOf(designArea.getMeasuredHeight()));
		
		int rootWidth = //Math.round
				(int)((rootHeight / (330/208.0f)) + 0.5f*handleSize); // does not work with another accessing method

		Log.d("width", String.valueOf(rootWidth));
		Log.d("height", String.valueOf(rootHeight));
		
		//int maxWidth = rootWidth - 2 * handleSize;
		//int maxHeight = rootHeight - 2 * handleSize;

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
		params.width = GridSnapper.snapToGrid(rootWidth);
		params.height = GridSnapper.snapToGrid(rootHeight);

		designArea.setLayoutParams(params);
		designArea.forceLayout();
	}
	
	private void resizeForPreview()
	{
		designArea.setScaleX(0.7f);
		designArea.setScaleY(0.7f);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
		params.width = GridSnapper.snapToGrid(306);
		params.height = GridSnapper.snapToGrid(451);
		
		//designArea.setLayoutParams(params);
		designArea.forceLayout();
	}

}
