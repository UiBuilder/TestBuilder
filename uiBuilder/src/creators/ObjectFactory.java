package creators;

import helpers.CollisionChecker;
import helpers.GridSnapper;
import helpers.ImageTools;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import creators.ReGenerator.OnObjectGeneratedListener;
import data.FromDatabaseObjectLoader;
import data.FromDatabaseObjectLoader.OnObjectLoadedFromDatabaseListener;
import data.ObjectValues;
import data.SampleAdapter;
import de.ur.rk.uibuilder.R;

public class ObjectFactory implements OnObjectLoadedFromDatabaseListener, OnObjectGeneratedListener
{
	public static final int SNAP_GRID_INTERVAL = 15;

	private Generator generator;
	private ReGenerator reGenerator;
	
	private CollisionChecker checker;
	private ObjectManipulator manipulator;
	private SampleAdapter samples;
	private Animation showUpAnimation;
	
	private View newItem;
	
	private RelativeLayout designArea;

	private static final String LOGTAG = "OBJECTFACTORY says:";

	/**
	 * KONSTRUKTOR
	 * 
	 * @param c the context in which the factory resides
	 * @param listener the event listener the factory provides for new objects. this is passed to the generator to generate touchable objects
	 * @param designArea 
	 */
	public ObjectFactory(Context context, OnTouchListener listener, RelativeLayout designArea)
	{
		this.designArea = designArea;
		
		generator = new Generator(context, listener);
		
		checker = new CollisionChecker(designArea);
		manipulator = new ObjectManipulator(context, designArea);
		samples = new SampleAdapter(context);
		
		showUpAnimation = AnimationUtils.loadAnimation(context, R.anim.design_loaded_scale_in);
		
		FromDatabaseObjectLoader.setOnObjectCreatedFromDatabaseListener(this);
		ReGenerator.setOnObjectGeneratedListener(this);
	}
	
	/**
	 * OnObjectLoadedFromDatabaseListener interface implementation
	 * 
	 * Notified by the FromDatabaseObjectLoader that a list of objects was loaded from the ScreenProviders database.
	 * Calls getElements to instantiate the loaded object properties as new items.
	 */
	@Override
	public void objectsLoaded(Bundle[] objectList)
	{
		Log.d("ObjectFactory", "OnObjectLoadedFromDatabaseListerner method called, calling GetElements");
		getElements(objectList);
	}

	/**
	 * OnObjectGeneratedListener interface implementation
	 * Called when an object is returned from async ReGenerators publish progress method.
	 * Calls setDataSources to set associated data sources.
	 * @author funklos
	 */
	@Override
	public void objectGenerated(View newItem)
	{
		designArea.addView(newItem, newItem.getLayoutParams());
		
		newItem.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		Log.d("object generated", "get bundle");
		setDataSources(newItem);	
		
		newItem.startAnimation(showUpAnimation);
	}
	
	
	/**
	 * called to create new objects
	 * @param which the requested object type       
	 * @return the generated object
	 */
	public View getElement(int which, MotionEvent event)
	{
		try
		{
			newItem = generator.generate(which);
			// holt die Koordinaten des Touch-Punktes
			RelativeLayout.LayoutParams params = setPosition(event);
			designArea.addView(newItem, params);

			return newItem;
			
		} catch (Exception e)
		{
			Log.d(LOGTAG, "Übergebene ID existiert nicht.");
			Log.d(LOGTAG, "id ist " + String.valueOf(which));
			return null;
		}
	}
	
	/**
	 * Instantiates a new async task 
	 * called to re-generate objects from database
	 * @param objectList
	 * @return
	 */
	public void getElements(Bundle[] objectList)
	{
		try
		{
			reGenerator = new ReGenerator(generator);
			Log.d("ObjectFactory", "getElements called and created Regenerator, about to execute");
			reGenerator.execute(objectList);
			
		}
		catch (Exception e) {
			Log.d("ObjectFactory", "getElements called but threw an Exception");
			// TODO: handle exception
		}
	}
	
	public void requestStyle(int which, View activeItem)
	{
		manipulator.setStyle(which, activeItem);
	}
	
	public void requestResize(int which, MotionEvent start, MotionEvent end, View activeItem, ImageButton dragHandle)
	{
		manipulator.setParams(which, start, end, activeItem, dragHandle);
	}
	
	/**
	 * @param event
	 * @return
	 */
	private RelativeLayout.LayoutParams setPosition(MotionEvent event)
	{
		Bundle tag = (Bundle) newItem.getTag();
		int defaultWidth = tag.getInt(ObjectValues.DEFAULT_WIDTH);
		int defaultHeight = tag.getInt(ObjectValues.DEFAULT_HEIGHT);
		
		float clickPosX = event.getAxisValue(MotionEvent.AXIS_X);
		float clickPosY = event.getAxisValue(MotionEvent.AXIS_Y);

		int targetX = checker.collisionX(clickPosX, defaultWidth);
		int targetY = checker.collisionY(clickPosY, defaultHeight);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newItem.getLayoutParams();

		params.leftMargin = GridSnapper.snapToGrid(targetX);
		params.topMargin = GridSnapper.snapToGrid(targetY);
		return params;
	}
	
	/**
	 * get the coordinates of the drop event
	 * repositions:
	 * the active item and the overlay
	 * @param event the drop event
	 */
	public void performDrop(DragEvent event, View activeItem, ImageButton drag)
	{
		int dropTargetX = checker.collisionX(event.getX(), activeItem.getMeasuredWidth());
		int dropTargetY = checker.collisionY(event.getY(), activeItem.getMeasuredHeight());

		setDragParams(activeItem, drag, dropTargetX, dropTargetY);
		setActiveItemParams(activeItem, dropTargetX, dropTargetY);
	}

	/**
	 * @param activeItem
	 * @param dropTargetX
	 * @param dropTargetY
	 */
	private void setActiveItemParams(View activeItem, int dropTargetX,
			int dropTargetY)
	{
		RelativeLayout.LayoutParams activeParams = (RelativeLayout.LayoutParams) activeItem.getLayoutParams();
		
		activeParams.leftMargin = GridSnapper.snapToGrid(dropTargetX);
		activeParams.topMargin = GridSnapper.snapToGrid(dropTargetY);
		activeItem.setLayoutParams(activeParams);
	}

	/**
	 * @param activeItem
	 * @param drag
	 * @param dropTargetX
	 * @param dropTargetY
	 * @return
	 */
	private void setDragParams(View activeItem,
			ImageButton drag, int dropTargetX, int dropTargetY)
	{
		RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams) drag.getLayoutParams();

		dragParams.leftMargin = GridSnapper.snapToGrid(dropTargetX) + designArea.getLeft();
		dragParams.topMargin = GridSnapper.snapToGrid(dropTargetY) + designArea.getTop();
		dragParams.width = activeItem.getMeasuredWidth();
		dragParams.height = activeItem.getMeasuredHeight();
		drag.setLayoutParams(dragParams);
	}
	

	/**
	 * @param newItem
	 * @param bundle
	 */
	private void setDataSources(View newItem)
	{
		Bundle bundle = (Bundle) newItem.getTag();
		
		switch (bundle.getInt(ObjectValues.TYPE))
		{
		case R.id.element_imageview:

			setImageResource(newItem, bundle);
			
			break;

		case R.id.element_grid : case R.id.element_list:
			
			samples.setSampleAdapter(newItem);
			break;
		default:
			break;
		}
	}

	/**
	 * @param newItem
	 * @param bundle
	 */
	private void setImageResource(View newItem, Bundle bundle)
	{
		if(bundle.getInt(ObjectValues.ICN_SRC) == 0)
		{
			((ImageView) newItem).setScaleType(ScaleType.CENTER_CROP);
			String source = bundle.getString(ObjectValues.IMG_SRC);
			ImageTools.setPic(newItem, source);
		}
		else
		{
			((ImageView) newItem).setScaleType(ScaleType.FIT_CENTER);
			((ImageView) newItem).setImageResource(bundle.getInt(ObjectValues.ICN_SRC));
		}
	}

}
