package helpers;

import android.content.Context;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class ScreenRatioChanger implements Runnable
{
	//protected final static int SCREENRATIO_DEFAULT = 160;
	private RelativeLayout designArea;
	private Context context;
	
	public ScreenRatioChanger(RelativeLayout designArea, Context context)
	{
		this.designArea = designArea;
		this.context = context;
	}
	
	@Override
	public void run()
	{
		resizeDrawingArea();
	}

	/**
	 * @author funklos Resizes the designArea according to the screen
	 *         size after the layouting process has finished, to have
	 *         access to measured dimensions
	 */
	private void resizeDrawingArea()
	{
		//float ratio = SCREENRATIO_DEFAULT;
		int rootHeight = designArea.getHeight();
		int rootWidth = Math.round(rootHeight / 16*10f); // does not work with another accessing method

		int handleSize = context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
		int maxWidth = rootWidth - 2 * handleSize;
		int maxHeight = rootHeight - 2 * handleSize;

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) designArea.getLayoutParams();
		params.width = GridSnapper.snapToGrid(maxWidth);
		params.height = GridSnapper.snapToGrid(maxHeight);

		designArea.setLayoutParams(params);
		designArea.forceLayout();
	}

}
