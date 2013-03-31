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
import android.widget.Toast;
import data.FromDatabaseObjectCreator;
import data.ObjectValues;
import data.ScreenProvider;
import data.ToDatabaseObjectWriter;
import de.ur.rk.uibuilder.R;

public class UiBuilderActivity extends Activity implements
		onUiElementSelectedListener, onObjectSelectedListener,
		onDeleteRequestListener, LoaderCallbacks<Cursor>
{

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// /// TODO Auto-generated method stub
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
	private FromDatabaseObjectCreator objectCreator;

	// private Drawable previewIcon;
	// private Drawable editIcon;

	private int screenId;
	private Boolean isPreview = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_fragment_container);
		setupUi();
		checkIntent();
		
		grabber = new ChildGrabber();
		objectCreator = new FromDatabaseObjectCreator();

		//container = (ViewGroup) inf.inflate(R.layout.layout_fragment_container, null);
		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());
		// previewIcon =
		// getResources().getDrawable(android.R.drawable.ic_menu_view);
		// previewIcon.setColorFilter(R.color.text_light,
		// PorterDuff.Mode.MULTIPLY);
		// editIcon =
		// getResources().getDrawable(android.R.drawable.ic_menu_view);

		performInitTransaction();
	}

	private void checkIntent()
	{
		Intent intent = getIntent();
		Bundle intentBundle = intent.getExtras();

		if (intentBundle != null)
		{
			screenId = intentBundle.getInt(ManagerActivity.DATABASE_SCREEN_ID);
			Log.d("screen id is", String.valueOf(screenId));
		}

		checkDb();
	}

	private void checkDb()
	{
		manager = getLoaderManager();
		manager.initLoader(ScreenProvider.OBJECTS_LOADER, null, this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Create UI-Fragment instances and set the activity as listener for changes
	 */
	private void setupUi()
	{

		itembox = new ItemboxFragment();
		editbox = new EditmodeFragment();
		designbox = new DesignFragment();
		deletebox = new DeleteFragment();

		ItemboxFragment.setOnUiElementSelectedListener(this);
		DesignFragment.setOnObjectSelectedListener(this);
		DeleteFragment.onDeleteRequestListener(this);
		setActionBarStyle();

	}

	/**
	 * @author funklos
	 */
	private void setActionBarStyle()
	{
		ActionBar bar = getActionBar();

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}


	@Override
	public void onBackPressed()
	{	
		//as a preview
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);

		Uri imageUri = exporter.requestBitmap(designbox.getView(), getContentResolver(), false, true, screenId);

		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra(ManagerActivity.RESULT_SCREEN_ID, screenId);
		returnIntent.putExtra(ManagerActivity.RESULT_IMAGE_PATH,imageUri.toString());
		setResult(RESULT_OK, returnIntent);     
		finish();
	}
	
	@Override
	protected void onStop()
	{
		
		Log.d("UIBuilderactivity", "onStop called");
		
		View rootDesignBox = designbox.getView();
		
		View designArea = rootDesignBox.findViewById(R.id.design_area);
		
		objectWriter.execute(designArea);
		super.onStop();
	}
	

	@Override
	protected void onResume()
	{
		objectWriter = new ToDatabaseObjectWriter(screenId, getApplicationContext());
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
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
			togglePreview(item);

		default:
			break;
		}
		// displaySidebar(ITEMBOX);
		return true;
	}

	/**
	 * 
	 */
	private void startSharing()
	{
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);

		Intent mail = exporter.getIntent(ImageTools.SHARE);
		mail.putExtra(Intent.EXTRA_STREAM, exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0));

		startActivityForResult(Intent.createChooser(mail, getString(R.string.intent_title_share)), ImageTools.SHARE);
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
	}

	/**
	 * 
	 */
	private void exportToGallery()
	{
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);

		exporter.requestBitmap(designbox.getView(), getContentResolver(), false, false, 0);

		Toast.makeText(getApplicationContext(), getString(R.string.confirmation_save_to_gallery), Toast.LENGTH_SHORT).show();
		changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
	}

	private void togglePreview(MenuItem item)
	{
		designbox.deleteOverlay();

		if (isPreview)
		{
			designbox.disableTouch(false);
			// item.setIcon(previewIcon);
			item.setTitle(R.string.menu_action_preview_mode);
			isPreview = false;
			changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_EDIT);
			displaySidebar(ITEMBOX);

		} else
		{
			designbox.disableTouch(true);
			// item.setIcon(editIcon);

			item.setTitle(R.string.menu_action_create_mode);

			isPreview = true;
			changeDisplayMode(designbox.getView(), ObjectValues.BACKGROUND_PRES);
			displaySidebar(NOTHING);
			designbox.disableTouch(true);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK)
			switch (requestCode)
			{

			case ImageTools.SHARE:
				Toast.makeText(getApplicationContext(), getString(R.string.confirmation_share_via_mail), Toast.LENGTH_SHORT).show();
				break;
			}
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
		String selection = ScreenProvider.KEY_OBJECTS_SCREEN + "=" + "'" + String.valueOf(screenId) + "'";

		Log.d("loader", "created");
		return new CursorLoader(getApplicationContext(), ScreenProvider.CONTENT_URI_OBJECTS, null, selection, null, null);
	}

	/**
	 * @author funklos
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{	
		int loaderId = loader.getId();
		
		switch (loaderId)
		{
		case ScreenProvider.OBJECTS_LOADER:
			
			objectCreator.loadObjects(cursor);
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
		// TODO Auto-generated method stub

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
}
