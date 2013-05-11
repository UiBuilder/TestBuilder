package creators;

import java.util.ArrayList;

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


/**
 * The ObjectFactory Class coordinates the generation of new views,
 * the regeneration from database and
 * the manipulation of object properties by the user.
 * 
 * It requests new views from an instance of the generator class by passing in the desired type.
 * The Regenerator instance is responsible for instantiating views from the supplied Bundle[] which is passed 
 * in by the OnObjectLoadedFromDatabaseListener interface callback method.
 * 
 * Those classes return their results and are added to the view tree by the factory.
 * Finally the factory checks if additional datasources, like adapters, or images have to be added to the object.
 * 
 * The Factory is also notified when a user interacts with an object and manipulates its properties.
 * Those events are passed on to the ObjectManipultor instance to handle the request appropriately.
 * 
 * @author funklos
 *
 */
public class ObjectFactory implements OnObjectLoadedFromDatabaseListener, OnObjectGeneratedListener
{
	public static final int SNAP_GRID_INTERVAL = 15;

	private Generator generator;
	private ReGenerator reGenerator;

	private ObjectManipulator manipulator;
	private SampleAdapter samples;
	private Animation showUpAnimation;
	
	private View newItem;
	
	private RelativeLayout designArea;

	private static final String LOGTAG = "OBJECTFACTORY says:";
	
	private ArrayList<View> imageViews;

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
		
		manipulator = new ObjectManipulator(designArea);
		samples = new SampleAdapter(context);
		
		showUpAnimation = AnimationUtils.loadAnimation(context, R.anim.design_loaded_scale_in);
		
		FromDatabaseObjectLoader.setOnObjectCreatedFromDatabaseListener(this);
		ReGenerator.setOnObjectGeneratedListener(this);
		
		imageViews = new ArrayList<View>();
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
			RelativeLayout.LayoutParams params = setInitialPosition(event);
			
			Log.d("pos", String.valueOf(params.height) + " " + String.valueOf(params.width));
			
			designArea.addView(newItem, params);
			setDataSources(newItem);

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
/*	
	*//**
	 * The views state has to be changed, because it is involved in a drag process.
	 * Forward the call to the manipulator to set the appropriate optical
	 * Representation of the state.
	 * 
	 * @param which
	 * @param activeItem
	 *//*
	public void requestStyle(int which, View activeItem)
	{
		manipulator.setStyle(which, activeItem);
	}
	
	*//**
	 * the user is interaction with the overlays scale handles.
	 * resizes the item correspondingly.
	 * 
	 * @param which the id of the overlay handle which is touched by the user
	 * @param start the start event
	 * @param end the event which represents the actual state
	 * @param activeItem the item which is manipulated
	 * @param dragHandle the center element of the overlay, which has to adapt its properties to the item being resized
	 *//*
	public void requestResize(int which, MotionEvent start, MotionEvent end, View activeItem, ImageButton dragHandle)
	{
		manipulator.setParams(which, start, end, activeItem, dragHandle);
		designArea.invalidate();
	}
	
	*//**
	 * The method is called after a view has been generated for the first time.
	 * The supplied event represents the users target for instantiation. This target
	 * position has to be checked for collisions with the designArea, to keep the
	 * generated view in bounds.
	 * 
	 * This method calculates an appropriate position, which has to be in 
	 * bounds of the designArea, and rounded to the nearest grid column.
	 * 
	 * @param event
	 * @return the calculated params, which keep the view in bounds and follow the grid columns.
	 */
	private RelativeLayout.LayoutParams setInitialPosition(MotionEvent event)
	{
		return manipulator.setInitialPosition(event, newItem);
	}
	
	/**
	 * A drop operation has returned true, but the position has to be checked
	 * for collisions and rounded to the nearest grid value.
	 * Repositions the drag overlay accordingly.
	 * 
	 * @param event the drop event
	 * @param activeItem the item in progress
	 * @param drag the drag element, representing the overlay
	 *//*
	public void performDrop(DragEvent event, View activeItem, ImageButton drag)
	{
		manipulator.performDrop(event, activeItem, drag);
	}
*/
	/**
	 * Check if the generated item needs additional datasources.
	 * Images need a source, which must be added after adding them to the view tree, else 
	 * their size is 0 and no appropriate scaling can be applied by the ImageTools class.
	 * If the new item is an instance of gridView or listView, they need an adapter
	 * to display sample content. The SampleAdapter instance is called to return a new adapter.
	 * 
	 * @param newItem
	 * @param bundle
	 */
	private void setDataSources(View newItem)
	{
		Bundle bundle = (Bundle) newItem.getTag();
		
		switch (bundle.getInt(ObjectValues.TYPE))
		{
		case ObjectIdMapper.OBJECT_ID_IMAGEVIEW:

			imageViews.add(newItem);
			setImageResource(newItem, bundle);
			
			break;

		case ObjectIdMapper.OBJECT_ID_GRIDVIEW : case ObjectIdMapper.OBJECT_ID_LISTVIEW:
			
			samples.setSampleAdapter(newItem);
			break;
			
		default:
			break;
		}
	}
	
	

	/**
	 * Check if the supplied supplied bundle contains a reference to either an icon
	 * or an image, set the image accordingly.
	 * @param newItem
	 * @param bundle
	 */
	private void setImageResource(final View newItem, Bundle bundle)
	{
		if(bundle.getInt(ObjectValues.ICN_SRC) == 0)
		{
			((ImageView) newItem).setScaleType(ScaleType.CENTER_CROP);
			final String source = bundle.getString(ObjectValues.IMG_SRC);
			newItem.post(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					ImageTools.setPic(newItem, source);
				}
			});
			
		}
		else
		{
			((ImageView) newItem).setScaleType(ScaleType.FIT_CENTER);
			((ImageView) newItem).setImageResource(bundle.getInt(ObjectValues.ICN_SRC));
		}
	}

}
