package manipulators;

import helpers.Log;
import uibuilder.DesignFragment;
import uibuilder.EditmodeFragment;
import uibuilder.EditmodeFragment.onObjectEditedListener;
import android.content.Context;
import android.graphics.Matrix.ScaleToFit;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;


/**
 * 
 * @author funklos
 *
 */
public class Overlay
{
	public static final int BOTH = 0;
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	
	
	private ImageButton drag;
	private ImageButton right;
	private ImageButton bottom;
	private ImageButton left;
	private ImageButton top;

	private RelativeLayout designArea, parent;
	private Context context;
	private LayoutInflater inflater;
	private DesignFragment designFragment;

	private final String OVERLAYTAG = "Overlay";
	int dragId;
	int typeActive;
	
	private boolean overlayActive;

	public Overlay(RelativeLayout root, DesignFragment designFragment)
	{
		designArea = root;
		parent = (RelativeLayout) root.getParent();
		context = designFragment.getActivity().getApplicationContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.designFragment = designFragment;
		
	}
	
	/**Generates a new overlay object.
	 * 
	 * @param activeItem the item requesting the overlay
	 * @param type the scaletype of the item
	 */
	public void generate(View activeItem, int type)
	{
		typeActive = type;
		
		RelativeLayout.LayoutParams modified = new RelativeLayout.LayoutParams(activeItem.getLayoutParams());

		buildDrag(activeItem, modified);

		invalidate();

		dragId = drag.getId();

		buildRight(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		buildBottom(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		buildLeft(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		buildTop(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		invalidate();
		
		switch (type)
		{
		case HORIZONTAL:
			bottom.setVisibility(View.INVISIBLE);
			top.setVisibility(View.INVISIBLE);
			
			break;

		case VERTICAL:
			left.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
			break;
			
		default:
			break;
		}
		overlayActive = true;
	}
	/**
	 * Bestimmt die Sichtbarkeit des Overlays. Das Overlay wird <b>Versteckt
	 * </b>, jedoch <b>nicht Entfernt</b>.
	 * 
	 * @param visibility
	 *            legt die Sichtbarkeit des Overlays fest.
	 */
	public void setVisibility(boolean visibility)
	{
		synchronized (designArea)
		{
			if (drag != null)
			{
				switch (typeActive)
				{
				case BOTH:
					setItemVisibility(drag, visibility);
					setItemVisibility(top, visibility);
					setItemVisibility(right, visibility);
					setItemVisibility(bottom, visibility);
					setItemVisibility(left, visibility);
				
					break;
				
				case VERTICAL:
					setItemVisibility(top, visibility);
					setItemVisibility(bottom, visibility);
					
					break;
					
				case HORIZONTAL:
					setItemVisibility(left, visibility);
					setItemVisibility(right, visibility);
					
					break;
				}	
			}
		}
	}

	/**
	 * This method performs his method calls via the .post a runnable approach,
	 * to avoid a "concurrentModificationException" caused by the frameworks
	 * inner implementation of the viewtree as a hashmap. At the very moment
	 * when this method is called, there is already an iterator iterating over
	 * the collection causing the exception.
	 * 
	 * @param v
	 *            the view which should be set
	 * @param on
	 *            the visibility that v should be set to
	 */
	private void setItemVisibility(final View v, final boolean on)
	{
		v.post(new Runnable()
		{

			@Override
			public void run()
			{
				if (on)
				{
					v.setVisibility(View.VISIBLE);
				} else
				{
					v.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/** 
	 * Entfernt das Overlay komplett.
	 * 
	 */
	public void delete()
	{
		synchronized (parent)
		{
			if (overlayActive)
			{
				parent.removeView(drag);
				parent.removeView(left);
				parent.removeView(right);
				parent.removeView(top);
				parent.removeView(bottom);

				drag = null;
				left = null;
				right = null;
				top = null;
				bottom = null;

				overlayActive = false;
			}
		}
	}

	private void invalidate()
	{
		designArea.forceLayout();

	}

	private void buildTop(RelativeLayout.LayoutParams modified)
	{
		top = (ImageButton) inflater.inflate(R.layout.overlay_handle_top, null);

		top.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		
		modified.addRule(RelativeLayout.ABOVE, right.getId());
		modified.addRule(RelativeLayout.LEFT_OF, right.getId());
		modified.addRule(RelativeLayout.RIGHT_OF, left.getId());
		modified.height = context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);

		top.setTag(OVERLAYTAG);
		top.setOnTouchListener(designFragment);
		parent.addView(top, modified);
	}

	private void buildLeft(RelativeLayout.LayoutParams modified)
	{
		left = (ImageButton) inflater.inflate(R.layout.overlay_handle_left, null);

		left.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.LEFT_OF, bottom.getId());
		modified.addRule(RelativeLayout.ALIGN_TOP, right.getId());
		modified.addRule(RelativeLayout.ABOVE, bottom.getId());
		modified.width = context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
		
		left.setTag(OVERLAYTAG);
		left.setOnTouchListener(designFragment);
		parent.addView(left, modified);
	}

	private void buildBottom(RelativeLayout.LayoutParams modified)
	{
		bottom = (ImageButton) inflater.inflate(R.layout.overlay_handle_bottom, null);

		bottom.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.BELOW, dragId);
		modified.addRule(RelativeLayout.ALIGN_LEFT, dragId);
		modified.addRule(RelativeLayout.ALIGN_RIGHT, dragId);
		modified.height = context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);
		
		bottom.setTag(OVERLAYTAG);
		bottom.setOnTouchListener(designFragment);
		parent.addView(bottom, modified);
	}

	private void buildRight(RelativeLayout.LayoutParams modified)
	{
		right = (ImageButton) inflater.inflate(R.layout.overlay_handle_right, null);
		right.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension));
		modified.addRule(RelativeLayout.ALIGN_TOP, dragId);
		modified.addRule(RelativeLayout.RIGHT_OF, dragId);
		modified.addRule(RelativeLayout.ALIGN_BOTTOM, dragId);
		modified.width = context.getResources().getDimensionPixelSize(R.dimen.default_overlay_handle_dimension);

		right.setTag(OVERLAYTAG);
		right.setOnTouchListener(designFragment);
		parent.addView(right, modified);
	}

	private void buildDrag(View activeItem, RelativeLayout.LayoutParams modified)
	{
		Log.d("params right", String.valueOf(activeItem.getRight()));

		drag = (ImageButton) inflater.inflate(R.layout.overlay_drag, null);
		modified.leftMargin = activeItem.getLeft() + designArea.getLeft();
		modified.topMargin = activeItem.getTop() + designArea.getTop();
		modified.width = activeItem.getMeasuredWidth();
		modified.height = activeItem.getMeasuredHeight();

		drag.setTag(OVERLAYTAG);
		drag.setOnTouchListener(designFragment);
		parent.addView(drag, modified);
	}

	public boolean isActive()
	{
		return overlayActive;
	}

	public ImageButton getDrag()
	{
		return drag;
	}
}
