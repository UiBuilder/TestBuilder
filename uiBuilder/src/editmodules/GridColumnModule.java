package editmodules;

import uibuilder.EditmodeFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class GridColumnModule extends Module
{
	private LinearLayout box;
	private GridView requesting;
	SeekBar columnNumber;
	TextView display;
	private int offset = 2;

	public GridColumnModule(EditmodeFragment context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_grid_columns, null);
		
		columnNumber = (SeekBar) box.findViewById(R.id.editmode_grid_choose_number);
		display = (TextView) box.findViewById(R.id.editmode_grid_display);
	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box));
		
		columnNumber.setOnSeekBarChangeListener(new ColumnNumberListener());
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		ViewGroup container = (ViewGroup) inProgress;

		requesting = (GridView) container.getChildAt(0);
		adaptToContext();
		
		return box;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

		columnNumber.setProgress(requesting.getNumColumns() - offset);
		display.setText(String.valueOf(columnNumber.getProgress() + offset));
	}
	
	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ColumnNumberListener implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar bar, int val, boolean arg2)
		{
			display.setText(String.valueOf(val + offset));

			gridColumnsChanged(val + offset);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0)
		{
			// TODO Auto-generated method stub

		}
	}
	
	private void gridColumnsChanged(int col)
	{
		requesting.setNumColumns(col);
	}

}
