package uibuilder;

import helpers.ChildGrabber;
import helpers.ImageTools;

import java.util.ArrayList;

import uibuilder.DeleteFragment.onDeleteRequestListener;
import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import data.FromDatabaseObjectLoader;
import data.ObjectValues;
import data.ScreenProvider;
import data.ToDatabaseObjectWriter;
import de.ur.rk.uibuilder.R;
import editmodules.ImageModule;
import editmodules.ImageModule.onImageImportListener;

public class UiBuilderActivity extends Activity implements
		onUiElementSelectedListener, onObjectSelectedListener,
		onDeleteRequestListener, LoaderCallbacks<Cursor>, onImageImportListener
{

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}

	public static final int ITEMBOX = 0x00;
	public static final int EDITBOX = 0x01;
	public static final int DELETEBOX = 0x02;
	public static final int NOTHING = 0x03;

	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private FragmentManager fManager;

	private ImageTools exporter;
	private DeleteFragment deletebox;
	private ChildGrabber grabber;
	private LoaderManager manager;
	
	private ToDatabaseObjectWriter objectWriter;
	private FromDatabaseObjectLoader objectBundleLoader;


	private int screenId;
	private boolean isPreview = false;
	private boolean intentStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_fragment_container);
		setupUi();
		checkIntent();
		
		grabber = new ChildGrabber();
		objectBundleLoader = new FromDatabaseObjectLoader();

		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());

		performInitTransaction();
		checkDb();
	}

	@Override
	protected void onResume()
	{	
		objectWriter = new ToDatabaseObjectWriter(screenId, getApplicationContext());
		
		Log.d("onresume", "called");
		if (!intentStarted)
		{
			checkDb();
		}
		else
		{
			intentStarted = false;
		}
		super.onResume();
	}
	

	@Override
	protected void onStop()
	{		
		Log.d("stoppingsaving state to database", "saving state to database");
		
		if(!intentStarted)
		{
			saveStateToDatabase();
		}
		
		super.onStop();
	}

	/**
	 * Fetch a reference to the designArea and pass it as a parameter to the ToDatabaseObjectWriter
	 * instance, which fetches a childviews and inserts them into the ScreenProvider in an async task.
	 */
	private void saveStateToDatabase()
	{
		View rootDesignBox = designbox.getView();
		
		ViewGroup designArea = (ViewGroup) rootDesignBox.findViewById(R.id.design_area);
		
		objectWriter.execute(designArea);
	}

	/**
	 * Get the passed intent from the Manager activity and fetch the content id of the screen which should be
	 * edited. The id specifies which database entries to load and to which screen new generated objects
	 * should be associated.
	 */
	private void checkIntent()
	{
		Intent intent = getIntent();
		Bundle intentBundle = intent.getExtras();

		if (intentBundle != null)
		{
			screenId = intentBundle.getInt(ManagerActivity.DATABASE_SCREEN_ID);
			Log.d("screen id is", String.valueOf(screenId));
		}
	}

	/**
	 * Fetch a reference to the loader manager to generate a new loader instance, which is responsible
	 * for loading the associated objects data from the ScreenProvider's objects table.
	 */
	private void checkDb()
	{
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.DATABASE_OBJECTS_LOADER, null, this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onBackPressed()
	{	
		
		/**
		 * USABILITY TEST REQUIREMENT:
		 * if an overlay is active, use the back button to disable it
		 */
		if (designbox.deleteOverlay())
		{
			displaySidebar(ITEMBOX);
		}
		
		/**
		 * USABILITY TEST REQUIREMENT:
		 * if the activity is in preview mode, use the back button to disable it
		 */
		else if (isPreview)
		{
			togglePreview();
		}
		
		/**
		 * default case, use back button to leave the design activity and navigate back to the manager
		 */
		else
		{
			//export screen as a preview to display it in the screen manager
			returnToManager();
		}
	}

	/**
	 * Make a screenshot in presentation mode to display as preview in the manager grid.
	 * Put the information about the loaction of the picture as extra
	 * and return to manager activity with an overridden transition.
	 */
	private void returnToManager()
	{
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);

		Uri imageUri = exporter.requestBitmap(designbox.getView(), getContentResolver(), false, true, screenId);

		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra(ManagerActivity.RESULT_SCREEN_ID, screenId);
		returnIntent.putExtra(ManagerActivity.RESULT_IMAGE_PATH,imageUri.toString());
		setResult(RESULT_OK, returnIntent); 
		
		finish();
		overridePendingTransition(R.anim.activity_transition_from_left_in, R.anim.activity_transition_to_right_out);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		intentStarted = false;
		
		if (resultCode == Activity.RESULT_OK)
			switch (requestCode)
			{
				case ImageTools.SHARE:
					Toast.makeText(getApplicationContext(), getString(R.string.confirmation_share_via_mail), Toast.LENGTH_LONG).show();
					break;
			}
	}

	/**
	 * Create UI-Fragment instances and set the activity as listener for changes
	 * @see onUiElementSelectedListener
	 * @see onObjectSelectedListener
	 * @see onDeleteRequestListener
	 * @see onImageImportListener
	 */
	private void setupUi()
	{
		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();
		deletebox = new DeleteFragment();

		ItemboxFragment.setOnUiElementSelectedListener(this);
		DesignFragment.setOnObjectSelectedListener(this);
		DeleteFragment.setOnDeleteRequestListener(this);
		ImageModule.setOnImageImportListener(this);
		
		setActionBarStyle();
	}

	/**
	 * Configure the action bar to match the ui-style.
	 * @author funklos
	 */
	private void setActionBarStyle()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
		
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayOptions(ActionBar.NAVIGATION_MODE_STANDARD|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
	}


	/**
	 * Initial fragmenttransaction to display the fragments. Editbox is added
	 * but hidden to guarantee access
	 */
	private void performInitTransaction()
	{
		FragmentTransaction init = fManager.beginTransaction();

		init.add(R.id.fragment_sidebar, editbox);
		init.add(R.id.fragment_sidebar, itembox);
		init.add(R.id.fragment_sidebar, deletebox);
		init.add(R.id.fragment_design, designbox);

		init.hide(editbox);
		init.hide(itembox);
		init.hide(deletebox);

		init.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		init.commit();

		objectSelected(false);
	}

	/**
	 * Adapt the sidebar to create or edit mode. Is called from the interface
	 * implementation.
	 * 
	 * @param sidebarType
	 *            specifies which of the sidebars to display
	 */

	public boolean displaySidebar(int sidebarType)
	{
		Log.d("DisplaySidebar", "is Called");
		FragmentTransaction outSwapper = fManager.beginTransaction();
		outSwapper.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);

		switch (sidebarType)
		{
		case NOTHING:
			outSwapper.hide(itembox);
			outSwapper.hide(editbox);
			outSwapper.hide(deletebox);
			break;

		case ITEMBOX:

			outSwapper.hide(editbox);
			outSwapper.show(itembox);
			break;

		case EDITBOX:

			Log.d("switched sideBarType", "result Editbox, replacing");

			outSwapper.show(editbox);
			outSwapper.hide(itembox);

			break;

		case DELETEBOX:

			outSwapper.hide(editbox);
			outSwapper.show(deletebox);
			break;

		}
		outSwapper.commit();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.design, menu);

		return true;
	}

	/**
	 * switch on the selected item action export: call imagetools to process the
	 * exporting request
	 * 
	 * @author funklos
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{

		case R.id.action_export_jpeg:

			exportToGallery();
			break;

		case R.id.action_attach_mail:
			
			startSharing();
			break;

		case R.id.action_preview:
			
			togglePreview();
			break;
			
		case android.R.id.home:
			
			returnToManager();
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * Calls the imagetools instance to fetch a share intent.
	 * An extra containing the uri of a new screenshot is set.
	 * The intent is passed to the framework to determine which activity can handle it.
	 * Intended results are:
	 * share via email,
	 * share via bluetooth,
	 * share via g+,
	 * share via google keep
	 * 
	 * these are working and maybe many more, depending on the users device.
	 */
	private void startSharing()
	{
		intentStarted = true;
		
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);
		Uri screenShotPath = exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0);

		Intent shareIntent = exporter.getIntent(ImageTools.SHARE);
		shareIntent.putExtra(Intent.EXTRA_STREAM, screenShotPath);

		startActivityForResult(Intent.createChooser(shareIntent, getString(R.string.intent_title_share)), ImageTools.SHARE);
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
	}

	/**
	 * Calls the ImageTools instance to request a screenshot and save it in the uiBuilder gallery folder
	 */
	private void exportToGallery()
	{
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);

		exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0);

		Toast.makeText(getApplicationContext(), getString(R.string.confirmation_save_to_gallery), Toast.LENGTH_LONG).show();
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
	}

	
	private void togglePreview()
	{
		designbox.deleteOverlay();

		if (isPreview)
		{
			disablePreviewMode();

		} else
		{
			enablePreviewMode();
		}

	}

	/**
	 * @param item
	 */
	private void enablePreviewMode()
	{
		designbox.disableTouch(true);
		
		isPreview = true;
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);
		displaySidebar(NOTHING);
		designbox.disableTouch(true);
	}

	/**
	 * @param item
	 */
	private void disablePreviewMode()
	{
		designbox.disableTouch(false);
		
		isPreview = false;
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
		displaySidebar(ITEMBOX);
	}


	/**
	 * Interface onUiElementSelected method
	 * 
	 * @author funklos implemented to notify the designbox of the chosen type of
	 *         interface element.
	 */
	@Override
	public void typeChanged(int id)
	{
		designbox.setSelection(id);
	}

	/**
	 * Interface onObjectSelected method
	 * called when an item is selected on the designarea
	 * 
	 * @author funklos sets a reference to the object in progress
	 * @param view
	 *            the selected view
	 */
	@Override
	public void objectChanged(View view)
	{
		lastTouch = view;
	}

	private View lastTouch;

	/**
	 * Interface callback method to switch between itembox, when no item is
	 * selected, and editbox when an item is selected
	 */
	@Override
	public void objectSelected(boolean selected)
	{

		if (!selected)
		{
			if (!itembox.isVisible())
			{
				displaySidebar(ITEMBOX);
			}
		} else
		{
			displaySidebar(EDITBOX);
			editbox.adaptLayoutToContext(lastTouch);
		}
	}

	/**
	 * Interface callback to notify a drag in process.
	 * The deletebox is showing up.
	 */
	@Override
	public void objectDragging()
	{
		displaySidebar(DELETEBOX);
	}

	/**
	 * deletebox interface callback
	 * 
	 * @author funklos
	 */
	@Override
	public void requestDelete()
	{

		designbox.performDelete();
	}

	/**
	 * @author funklos
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	{
		String selection = ScreenProvider.KEY_OBJECTS_SCREEN + " = " + "'" + String.valueOf(screenId) + "'";

		Log.d("loader", "created");
		return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_OBJECTS, null, selection, null, null);
	}

	/**
	 * The loader has finished loading from database.
	 * The cursor containing the loaded object definitions is passed to the 
	 * FromDatabaseObjectLoader instance to convert the cursor entries to bundles.
	 * @author funklos
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{	
		int loaderId = loader.getId();

		switch (loaderId)
		{
		case ScreenProvider.DATABASE_OBJECTS_LOADER:
			
			objectBundleLoader.loadObjects(cursor);
			manager.destroyLoader(loaderId);
			manager = null;
			break;
		}
	}

	/**
	 * @author funklos
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{

	}

	private void changeDisplayMode(View designbox, String displayStyle)
	{
		ArrayList<View> viewList;
		viewList = grabber.getChildren(designbox.findViewById(R.id.design_area));

		for (View view : viewList)
		{
			Bundle tagBundle = (Bundle) view.getTag();
			int style = tagBundle.getInt(displayStyle);
			view.setBackgroundResource(style);
		}
	}


	@Override
	public void prepareForBackground()
	{
		intentStarted = true;
	}
	
}
