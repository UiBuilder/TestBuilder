package editmodules;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

/**
 * each sublayout module has an expansion selector button with the same id.
 * get this button for each module and set the corresponding listener. a
 * reference to the parent layout is passed to the listener to avoid final
 * instances of references, which were not reliable enough when performing
 * expansions
 * 
 * @author funklos
 * @param box
 *            the editmode module containing the button
 */
public class ExpansionListener implements OnClickListener
{
		private static LinearLayout expandedBox;
		private LinearLayout module;
		private ImageButton indicator;
		private LinearLayout clickableArea;
		private View expandableView;
		private Boolean isExpanded = false;

		private int indicatorExpanded = R.raw.ico_small_0037;
		private int indicatorCollapsed = R.raw.ico_small_0035;
		
		public ExpansionListener(View box)
		{
			this.module = (LinearLayout) box;
			this.indicator = (ImageButton) box.findViewById(R.id.expand_selector);
			this.clickableArea = (LinearLayout) indicator.getParent();
			this.expandableView = this.module.findViewById(R.id.expandable);
			
			setIndikatorListener();
		}

		private void setIndikatorListener()
		{
			this.indicator.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Log.d("exp", "ind");
					module.performClick();
				}
			});	
		}

		@Override
		public void onClick(View clickedModule)
		{
			if (this.isExpanded)
			{
				collapse();

			} else
			{
		
				if (expandedBox != null)
				{
					LinearLayout expandedClickArea = (LinearLayout) expandedBox.findViewById(R.id.expand_selector).getParent();
					expandedClickArea.performClick();
				}
				expand();
			}
		}

		private void expand()
		{
			expandableView.setVisibility(View.VISIBLE);
			listener.getValues();
			
			indicator.setImageResource(indicatorCollapsed);
			//module.setActivated(true);

			expandedBox = module;
			this.isExpanded = true;
			refresh();
		}

		private void collapse()
		{
			expandableView.setVisibility(View.GONE);
			indicator.setImageResource(indicatorExpanded);
			//module.setActivated(false);

			expandedBox = null;
			this.isExpanded = false;
			refresh();
		}

		private void refresh()
		{
			expandableView.invalidate();
			module.invalidate();
			module.requestLayout();
			expandableView.forceLayout();
		}
		
		
		public abstract interface onToggleExpansionListener
		{
			void getValues();
		}

		private static onToggleExpansionListener listener;

		public static void setOnToggleExpansionListener(
				onToggleExpansionListener listener)
		{
			ExpansionListener.listener = listener;
		}
}
