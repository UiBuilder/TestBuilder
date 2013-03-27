package editmodules;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class ExpansionListener implements OnClickListener
{
		private LinearLayout expandedBox;
		private LinearLayout module;
		private ImageButton indicator;
		private LinearLayout clickableArea;
		private View expandableView;
		private LinearLayout root;

		private int indicatorExpanded = R.raw.ico_small_0037;
		private int indicatorCollapsed = R.raw.ico_small_0035;
		
		public ExpansionListener(View box)
		{
			this.module = (LinearLayout) box;
			this.indicator = (ImageButton) box.findViewById(R.id.expand_selector);
			this.clickableArea = (LinearLayout) indicator.getParent();
			this.expandableView = this.module.findViewById(R.id.expandable);
			this.root = (LinearLayout) module.getParent();
			
			setIndikatorListener();
		}
		
		public ExpansionListener(LinearLayout module, ImageButton indicator)
		{
			this.module = module;
			this.indicator = indicator;
			this.clickableArea = (LinearLayout) indicator.getParent();
			this.expandableView = this.module.findViewById(R.id.expandable);
			this.root = (LinearLayout) module.getParent();
			setIndikatorListener();
		}

		private void setIndikatorListener()
		{
			this.indicator.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					clickableArea.performClick();
				}
			});	
		}

		@Override
		public void onClick(View clickedModule)
		{
			if (!clickedModule.isActivated())
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
			listener.getValues();
			expandableView.setVisibility(View.VISIBLE);
			indicator.setImageResource(indicatorCollapsed);
			module.setActivated(false);

			expandedBox = module;
			//refresh();
		}

		private void collapse()
		{
			expandableView.setVisibility(View.GONE);
			indicator.setImageResource(indicatorExpanded);
			module.setActivated(true);

			expandedBox = null;
			//refresh();
		}

		private void refresh()
		{
			expandableView.invalidate();
			module.invalidate();
			module.requestLayout();
			expandableView.forceLayout();
			//root.forceLayout();
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
