/**
 * 
 */
package editmodules;

import uibuilder.EditmodeFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import data.IconAdapter;
import data.ObjectValues;
import data.ResArrayImporter;
import de.ur.rk.uibuilder.R;

/**
 * @author funklos
 *
 */
public class IconModule extends Module
{
	private LinearLayout box;
	private View requesting;
	GridView iconGrid;
	
	int[] lowResIcns;
	int[] highResIcns;
	IconAdapter adapter;

	/**
	 * @param context
	 */
	public IconModule(EditmodeFragment context)
	{
		super(context);
		setupData(super.context);
	}

	/**
	 * @param context
	 */
	private void setupData(Context context)
	{
		highResIcns = ResArrayImporter.getRefArray(context, R.array.icons_big);
		lowResIcns = ResArrayImporter.getRefArray(context, R.array.icons_small);
		
		adapter = new IconAdapter(context, lowResIcns);
		iconGrid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/* (non-Javadoc)
	 * @see editmodules.ExpansionListener.onToggleExpansionListener#getValues()
	 */
	@Override
	public void getValues()
	{
		adaptToContext();
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#setupUi()
	 */
	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_icon, null);
		iconGrid = (GridView) box.findViewById(R.id.editmode_icon_grid);
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#setListeners()
	 */
	@Override
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box));
		iconGrid.setOnItemClickListener(new IconModuleListener());
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#getInstance(android.view.View)
	 */
	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		
		adaptToContext();
		return box;
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#adaptToContext()
	 */
	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub
		box.invalidate();
		Log.d("adapt called", "refresh");
		iconGrid.setAdapter(adapter);
	}
	
	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class IconModuleListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos,
				long arg3)
		{
			setIconResource(pos);
		}
		
		private void setIconResource(int pos)
		{
			int resourceId = (highResIcns[pos]);
			Bundle bundle = (Bundle) requesting.getTag();
			bundle.putInt(ObjectValues.ICN_SRC, resourceId);

			((ImageView) requesting).setScaleType(ScaleType.FIT_CENTER);
			
			((ImageView) requesting).setImageResource(resourceId);
		}
	}

}
