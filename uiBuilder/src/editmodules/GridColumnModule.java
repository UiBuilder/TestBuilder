package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the number of columns.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author funklos
 *
 */
public class GridColumnModule extends Module
{
	private LinearLayout box;
	
	private SeekBar columnNumber;
	private TextView display;
	private int offset = 2;
	
	private int colNum;

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
		box.setOnClickListener(new ExpansionListener(box, super.context));
		
		columnNumber.setOnSeekBarChangeListener(new ColumnNumberListener());
	}

	
	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		adaptToContext();
		
		return box;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

		columnNumber.setProgress(((GridView) item).getNumColumns() - offset);
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
			putInTag();
		}
		
		
		private void gridColumnsChanged(int col)
		{
			((GridView) item).setNumColumns(col);
			colNum = col;
		}

		private void putInTag()
		{
			Bundle tag = (Bundle) container.getTag();
			tag.putInt(ObjectValues.COLUMNS_NUM, colNum);
		}
	}


}
