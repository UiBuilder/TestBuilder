package editmodules;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * @param box the editmode module containing the button
 */
public class ExpansionListener implements OnClickListener
{
		private static LinearLayout expandedBox;
		private LinearLayout module;
		private ImageButton indicator;
		private View expandableView;
		private Boolean isExpanded = false;

		private int indicatorExpanded = R.raw.ico_small_0037;
		private int indicatorCollapsed = R.raw.ico_small_0035;
		
		private static float alphaActive = 1.0f, alphaInactive = 0.7f; 
		
		private Animation fadeOut, fadeIn;
		
		public ExpansionListener(View box, Context context)
		{
			this.module = (LinearLayout) box;
			this.indicator = (ImageButton) box.findViewById(R.id.expand_selector);
			this.expandableView = this.module.findViewById(R.id.expandable);
			
			fadeOut = AnimationUtils.loadAnimation(context, R.anim.modules_fade_out);
			fadeIn = AnimationUtils.loadAnimation(context, R.anim.modules_fade_in);
			
			//this.module.startAnimation(fadeOut);
			module.setAlpha(alphaInactive);
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
				expand();
			}
		}
		
		/**
		 * expand the box
		 */
		private void expand()
		{
			collapseOther();
			
			expandableView.setVisibility(View.VISIBLE);
			listener.getValues();
			
			indicator.setImageResource(indicatorCollapsed);
			module.startAnimation(fadeIn);
			module.setAlpha(alphaActive);

			expandedBox = module;
			this.isExpanded = true;
			refresh();
		}

		/**
		 * called before expansion to close the box which is in "expanded" state
		 */
		private void collapseOther()
		{
			if (expandedBox != null)
			{
				ImageButton expandedClickArea = (ImageButton) expandedBox.findViewById(R.id.expand_selector);
				expandedClickArea.performClick();
			}
		}

		/**
		 * collapse the box
		 */
		private void collapse()
		{
			expandableView.setVisibility(View.GONE);
			indicator.setImageResource(indicatorExpanded);
			
			//module.startAnimation(fadeOut);
			module.setAlpha(alphaInactive);

			expandedBox = null;
			this.isExpanded = false;
			refresh();
		}

		/**
		 * force ui redraw
		 */
		private void refresh()
		{
			expandableView.invalidate();
			module.invalidate();
			module.requestLayout();
			expandableView.forceLayout();
		}
		
		/**NOT IN USE
		 * Interface implemented by all subclasses of Module.
		 * Implementation is realized in the superclass implementation @see Module
		 * @author funklos
		 *	
		 */
		public abstract interface onToggleExpansionListener
		{
			void getValues();
		}

		private static onToggleExpansionListener listener;

		public static void setOnToggleExpansionListener(onToggleExpansionListener listener, Context context)
		{
			ExpansionListener.listener = listener;
		}
}
