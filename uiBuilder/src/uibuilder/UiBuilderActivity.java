package uibuilder;

import helpers.ChildGrabber;
import helpers.ImageTools;

import java.util.ArrayList;

import uibuilder.DeleteFragment.onDeleteRequestListener;
import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onObjectRequestedListener;
import uxcomponents.ScreenCommentsFragment;
import uxcomponents.ScreenCommentsFragment.commentsUpdatedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import creators.ObjectFactory;
import data.FromDatabaseObjectLoader;
import data.ScreenProvider;
import data.ToDatabaseObjectWriter;
import de.ur.rk.uibuilder.R;
import editmodules.ImageModule;
import editmodules.ImageModule.onImageImportListener;

/**
 * This activity hosts the fragments for the design and manipulation of UIs
 * @author jonesses and funklos
 *
 */

public class UiBuilderActivity 
								extends Activity 
								implements
								onObjectSelectedListener,
								onDeleteRequestListener, 
								LoaderCallbacks<Cursor>, 
								onImageImportListener, onObjectRequestedListener, commentsUpdatedListener
{

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}

	public static final int 
			ITEMBOX = 0x00, 
			EDITBOX = 0x01, 
			DELETEBOX = 0x02, 
			NOTHING = 0x03;
	

	public static final String MODE = "mode";
	public static final int MODE_EDIT = 0xaf, MODE_VIEW = 0xbf;
	private int inMode;
	
	private static final int DISPLAY_PRESENTATION_MODE = 0x01;
	private static final int DISPLAY_EDIT_MODE = 0x02;

	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private ScreenCommentsFragment commentsBox;
	private FragmentManager fManager;
	
	private RelativeLayout rootLayout;
	private LinearLayout sideFragment;
	
	private ImageTools exporter;
	private DeleteFragment deletebox;
	private ChildGrabber grabber;
	private LoaderManager manager;
	
	private ToDatabaseObjectWriter objectWriter;
	private FromDatabaseObjectLoader objectBundleLoader;

	private int screenId;
	private Bundle intentBundle;
	private int numberOfComments;
	public static final String NUMBER_OF_COMMENTS = "comments";
	private boolean isPreview = false, intentStarted = false;
	
	private ObjectFactory factory;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_fragment_container);
		checkIntent();
		setupUi();
		
		
		grabber = new ChildGrabber();
		objectBundleLoader = new FromDatabaseObjectLoader();

		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());
		ScreenCommentsFragment.setCommentsUpdatedListener(this);

		performInitTransaction();
		
		//checkDb();
		
		
	}

	/**
	 * load the objects in onResume, for the case the activity was killed.
	 * No data reloading is necessary if the activity was stopped by an media intent,
	 * originating from this app.
	 * by the system to free up memory resources.
	 */
	@Override
	protected void onResume()
	{	
		objectWriter = new ToDatabaseObjectWriter(screenId, getApplicationContext());
		
		factory = new ObjectFactory(getApplicationContext(), designbox, rootLayout);
		
		Log.d("onresume", "called");
		if (!intentStarted)
		{
			Log.d("ONRESUME intentstarted", String.valueOf(intentStarted));
			checkDb();
		}
		else
		{
			intentStarted = false;
		}
		super.onResume();
	}
	
	
	/**
	 * The onStop method is always called by the framework.
	 * To differentiate if onStop was called because of an intent
	 * launched from this app, intentStarted is checked, which is set to true
	 * by an interface callback, in the case of an intent originating from an
	 * activity or fragment by this app.
	 * In this case, no database insertion is desired, to avoid duplicates or
	 * a complete reload of the objects displayed on the designArea.
	 */
	@Override
	protected void onStop()
	{		
		Log.d("ONSTOP", String.valueOf(intentStarted));
		if(!intentStarted)
		{
			Log.d("stoppingsaving state to database", "saving state to database");
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
		intentBundle = intent.getExtras();

		if (intentBundle != null)
		{
			screenId = intentBundle.getInt(ScreenProvider.KEY_ID);
			inMode = intentBundle.getInt(MODE);
			numberOfComments = intentBundle.getInt(NUMBER_OF_COMMENTS);
			
			Log.d("number of comments ", String.valueOf(numberOfComments));
		}
	}

	/**
	 * Fetch a reference to the loader manager to generate a new loader instance, which is responsible
	 * for loading the associated objects data from the ScreenProvider's objects table.
	 * @see onLoadFinished for the description of processing the results.
	 */
	private void checkDb()
	{
		Log.d("check db", "init loader");
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.LOADER_ID_OBJECTS, null, this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onBackPressed()
	{	
		if (inMode == UiBuilderActivity.MODE_EDIT)
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
			else if (isDisplayingComments)
			{
				displaySidebar(ITEMBOX);
				isDisplayingComments = false;
			}
			else
			{
				returnToManager();
			}
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
		
		Uri imageUri = getPreviewImage();
		ContentValues image = new ContentValues();
		image.put(ScreenProvider.KEY_SCREEN_PREVIEW, imageUri.toString());

		ContentResolver res = getContentResolver();
		Uri imageUpdate = ContentUris.withAppendedId(ScreenProvider.CONTENT_URI_SCREENS, screenId);

		res.update(imageUpdate, image, null, null);
		
		/*Intent returnIntent = new Intent();
		returnIntent.putExtra(ScreenManagerActivity.RESULT_SCREEN_ID, screenId);
		returnIntent.putExtra(ScreenManagerActivity.RESULT_IMAGE_PATH,imageUri.toString());
		setResult(RESULT_OK, returnIntent); 
		*/
		finish();
		overridePendingTransition(R.anim.activity_transition_from_left_in, R.anim.activity_transition_to_right_out);
	}

	/**
	 * @return
	 */
	private Uri getPreviewImage()
	{
		changeDisplayMode(designbox.getView(), DISPLAY_PRESENTATION_MODE);

		Uri imageUri = exporter.requestBitmap(designbox.getView(), getContentResolver(), false, true, screenId);

		changeDisplayMode(designbox.getView(), DISPLAY_EDIT_MODE);
		return imageUri;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("ONRESULT intentstarted", String.valueOf(intentStarted));
		if (resultCode == Activity.RESULT_OK)
		{
			switch (requestCode)
			{
				case ImageTools.SHARE:
					
					Toast.makeText(getApplicationContext(), getString(R.string.confirmation_share_via_mail), Toast.LENGTH_LONG).show();
					break;
			}
		}
		else
		{
			
		}
	}

	/**
	 * Create UI-Fragment instances and set the activity as listener for changes
	 * @see OnNewItemRequestedListener
	 * @see onObjectSelectedListener
	 * @see onDeleteRequestListener
	 * @see onImageImportListener
	 */
	private void setupUi()
	{

		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();
		designbox.setArguments(intentBundle);
		deletebox = new DeleteFragment();
		commentsBox = new ScreenCommentsFragment();
		commentsBox.setArguments(intentBundle);

		DesignFragment.setOnObjectSelectedListener(this);
		DeleteFragment.setOnDeleteRequestListener(this);
		ImageModule.setOnImageImportListener(this);
		ItemboxFragment.setOnObjectRequestedListener(this);
		
		rootLayout = (RelativeLayout) findViewById(R.id.uibuilder_activity_root);
		sideFragment = (LinearLayout) findViewById(R.id.fragment_sidebar);
		
		setActionBarStyle();
		setupMessageBadge();
	}

	private void setupMessageBadge()
	{
		TextView badge = (TextView) findViewById(R.id.message_badge);
		
		
		{
			badge.setVisibility(View.VISIBLE);
			badge.setText(String.valueOf(numberOfComments));
			badge.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (inMode == MODE_EDIT)
					{
						if (isDisplayingComments)
						{
							displaySidebar(ITEMBOX);
							isDisplayingComments = false;
						}
						else
							performDisplayCommentsTransaction();
					}
					
				}

			});
		}

		
	}

	/**
	 * Configure the action bar to match the ui-style.
	 * @author funklos
	 */
	private void setActionBarStyle()
	{
		ActionBar bar = getActionBar();
		bar.setDisplayShowTitleEnabled(false);

		//bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
		
		//bar.setDisplayHomeAsUpEnabled(true);
		//bar.setDisplayOptions(ActionBar.NAVIGATION_MODE_STANDARD|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_TITLE);
		
	}


	/**
	 * Initial fragmenttransaction to display the fragments. Editbox is added
	 * but hidden to guarantee access
	 * @author jonesses edited by funklos
	 */
	private void performInitTransaction()
	{
		FragmentTransaction init = fManager.beginTransaction();

		if (inMode == MODE_EDIT)
		{
			init.add(R.id.fragment_sidebar, editbox);
			init.add(R.id.fragment_sidebar, itembox);
			init.add(R.id.fragment_sidebar, deletebox);
			init.add(R.id.fragment_design, designbox);
			init.add(R.id.fragment_sidebar, commentsBox);
		
			init.hide(editbox);
			init.hide(itembox);
			init.hide(deletebox);
			init.hide(commentsBox);
		}
		if (inMode == MODE_VIEW)
		{
			//init.add(R.id.fragment_sidebar, editbox);
			//init.add(R.id.fragment_sidebar, itembox);
			//init.add(R.id.fragment_sidebar, deletebox);
			init.add(R.id.fragment_design, designbox);
			init.add(R.id.fragment_sidebar, commentsBox);
		
			//init.hide(editbox);
			//init.hide(itembox);
			//init.hide(deletebox);
			//init.hide(commentsBox);
		}

		init.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
		init.commit();

		objectSelected(false);
	}
	
	private boolean isDisplayingComments;
	
	private void performDisplayCommentsTransaction()
	{
		if (inMode == MODE_EDIT)
		{
			FragmentTransaction comments = fManager.beginTransaction();
			comments.show(commentsBox);
		
			comments.hide(editbox);
			comments.hide(itembox);
			comments.hide(deletebox);
			
			comments.setCustomAnimations(R.animator.to_left_in, R.animator.to_left_out);
			comments.commit();
			
			isDisplayingComments = true;
		}
		
	}

	/**
	 * Adapt the sidebar to create or edit mode. Is called from the interface
	 * implementation.
	 * @author jonesses slightly edited by funklos
	 * @param sidebarType specifies which of the sidebars to display
	 */

	public void displaySidebar(final int sidebarType)
	{
		//synchronized (sideFragment)
		{
			sideFragment.post(new Runnable()
			{
				
				@Override
				public void run()
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
						outSwapper.hide(deletebox);
						outSwapper.hide(editbox);
						outSwapper.show(itembox);
						break;

					case EDITBOX:

						Log.d("switched sideBarType", "result Editbox, replacing");
						outSwapper.hide(deletebox);
						outSwapper.hide(itembox);
						outSwapper.show(editbox);

						break;

					case DELETEBOX:

						outSwapper.hide(editbox);
						outSwapper.show(deletebox);
						break;

					}
					outSwapper.commit();
					//sideFragment.invalidate();
					//rootLayout.forceLayout();
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.design_menu, menu);

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
			
		case R.id.action_help:
			
			launchHelp();
			break;
			
			
		case android.R.id.home:
			
			returnToManager();
			break;

		default:
			break;
		}
		return true;
	}

	private void launchHelp()
	{
		prepareForBackground();
		
		Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
		startActivity(helpIntent);
		
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
		prepareForBackground();
		
		changeDisplayMode(designbox.getView(), DISPLAY_PRESENTATION_MODE);//ObjectValues.BACKGROUND_PRES);
		Uri screenShotPath = exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0);

		Intent shareIntent = exporter.getIntent(ImageTools.SHARE);
		shareIntent.putExtra(Intent.EXTRA_STREAM, screenShotPath);

		startActivityForResult(Intent.createChooser(shareIntent, getString(R.string.intent_title_share)), ImageTools.SHARE);
		changeDisplayMode(designbox.getView(), DISPLAY_EDIT_MODE);//ObjectValues.BACKGROUND_EDIT);
	}

	/**
	 * Calls the ImageTools instance to request a screenshot and save it in the uiBuilder gallery folder
	 */
	private void exportToGallery()
	{
		changeDisplayMode(designbox.getView(), DISPLAY_PRESENTATION_MODE);

		exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0);

		Toast.makeText(getApplicationContext(), getString(R.string.confirmation_save_to_gallery), Toast.LENGTH_LONG).show();
		changeDisplayMode(designbox.getView(), DISPLAY_EDIT_MODE);//ObjectValues.BACKGROUND_EDIT);
	}

	/**
	 * @author jonesses
	 * Called when the toggle preview menu button was clicked by the user
	 */
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
	 * Hide the itembox and disable all interactions for the designarea
	 * @param item
	 */
	private void enablePreviewMode()
	{
		//designbox.disableTouch(true);
		
		isPreview = true;
		changeDisplayMode(designbox.getView(), DISPLAY_PRESENTATION_MODE);
		displaySidebar(NOTHING);
		designbox.disableTouch(true);
	}

	/**
	 * Shows the sidebar again and reactivate the interaction listeners again. 
	 * @param item
	 */
	private void disablePreviewMode()
	{
		designbox.disableTouch(false);
		
		isPreview = false;
		changeDisplayMode(designbox.getView(), DISPLAY_EDIT_MODE);//ObjectValues.BACKGROUND_EDIT);
		displaySidebar(ITEMBOX);
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
	 * Creates a loader responsible for async ScreenProvider queries in the objects table.
	 * The selection (WHERE clause) restricts the query to objects belonging to the screen
	 * which is edited by the activity.
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
		case ScreenProvider.LOADER_ID_OBJECTS:
			
			objectBundleLoader.loadObjects(cursor);
			manager.destroyLoader(loaderId);
			manager = null;
			break;
		}
	}

	/**
	 * Do nothing on this callback. The screen should not be updated when the objects table was
	 * updated, because all elements are already in the viewtree of the designArea.
	 * @author funklos
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		
	}
	
	
	/**
	 * Switches the style of the objects in the designArea to represent the preview or
	 * edit mode.
	 * Calls the grabber for a list of references and updates their visual presentation.
	 * @author jonesses
	 * @param designbox
	 * @param displayStyle
	 */
	private void changeDisplayMode(View designbox, /*String displayStyle,*/ int style)
	{
		ArrayList<View> viewList;
		viewList = grabber.getChildren(designbox.findViewById(R.id.design_area), ChildGrabber.MODE_FLAT);

		for (View view : viewList)
		{
			if (style == DISPLAY_EDIT_MODE)
			{
				view.setBackgroundResource(R.drawable.object_background_default);
			}
			else
			{
				view.setBackgroundResource(android.R.color.transparent);
			}
			//Bundle tagBundle = (Bundle) view.getTag();
			//int style = tagBundle.getInt(displayStyle);
			//view.setBackgroundResource(style);
		}
	}


	/**
	 * interface callback to tell the activity that it is going to be covered by an
	 * activity, started by an intent, such as an mediaPickIntent or a shareIntent, which is launched
	 * by the app itself.
	 * In this case, no database insertion should be processed.
	 */
	@Override
	public void prepareForBackground()
	{
		intentStarted = true;
	}

	@Override
	public View requestObject(int id, MotionEvent event)
	{
		View v = factory.getElement(id, event);
		designbox.newObjectEvent();
		
		return v;
	}

	@Override
	public void newCommentsLoaded(int number)
	{
		TextView badge = (TextView) findViewById(R.id.message_badge);
		badge.setText(String.valueOf(number));
	}	
}
