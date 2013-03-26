package uibuilder;

import helpers.ChildGrabber;
import helpers.FromDatabaseObjectCreator;
import helpers.ImageTools;
import helpers.ObjectValueCollector;
import helpers.ToDatabaseObjectWriter;

import java.util.ArrayList;

import uibuilder.DeleteFragment.onDeleteRequestListener;
import uibuilder.DesignFragment.onObjectSelectedListener;
import uibuilder.ItemboxFragment.onUiElementSelectedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import creators.Generator;
import data.DataBase;
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

	public static final int ITEMBOX = 0;
	public static final int EDITBOX = 1;
	public static final int DELETEBOX = 2;
	public static final int NOTHING = 3;

	private ItemboxFragment itembox;
	private EditmodeFragment editbox;
	private DesignFragment designbox;
	private FragmentManager fManager;
	private ViewGroup container;
	private ImageTools exporter;
	private DeleteFragment deletebox;
	private ChildGrabber grabber;
	private Button previewButton;

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
		grabber = new ChildGrabber();

		LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		container = (ViewGroup) inf.inflate(R.layout.layout_fragment_container, null);
		fManager = getFragmentManager();
		exporter = new ImageTools(getApplicationContext());
		// previewIcon =
		// getResources().getDrawable(android.R.drawable.ic_menu_view);
		// previewIcon.setColorFilter(R.color.text_light,
		// PorterDuff.Mode.MULTIPLY);
		// editIcon =
		// getResources().getDrawable(android.R.drawable.ic_menu_view);

		performInitTransaction();
		checkIntent();
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
		getLoaderManager().initLoader(DataBase.OBJECTS_LOADER, null, this);
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
		// bar.setCustomView(R.layout.menu_item_preview);

		// previewButton = (Button)
		// bar.getCustomView().findViewById(R.id.action_preview_mode);
		// previewIcon = (ImageView)
		// bar.getCustomView().findViewById(R.id.preview_icon);

		// previewButton.setOnTouchListener(this);
		// previewIcon.setOnTouchListener(this);

		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		bar.setBackgroundDrawable(getResources().getDrawable(R.color.designfragment_background));
	}


	@Override
	public void onBackPressed()
	{
		
		//super.onBackPressed();
		
		Intent returnIntent = new Intent();
		
		//as a preview
		changeDisplayMode(designbox.getView(), Generator.PRESENTATION_STYLE);

		Uri imageUri = exporter.requestBitmap(designbox.getView(), getContentResolver(), false);

		changeDisplayMode(designbox.getView(), Generator.CREATION_STYLE);
		
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
		
		ToDatabaseObjectWriter objectWriter = new ToDatabaseObjectWriter(designArea, screenId, getApplicationContext());
		
		
//		for (View view : content)
//		{
//			ContentValues tempValues = ObjectValueCollector.getValuePack(view);
//			
//			int databaseID = tempValues.getAsInteger(ObjectValueCollector.ID);
//			
//			Log.d("value of databaseID", String.valueOf(databaseID));
//			
//			int xpos = tempValues.getAsInteger(ObjectValueCollector.X_POS);
//			int ypos = tempValues.getAsInteger(ObjectValueCollector.Y_POS);
//			int id = tempValues.getAsInteger(ObjectValueCollector.TYPE);
//			int width = tempValues.getAsInteger(ObjectValueCollector.WIDTH);
//			int height = tempValues.getAsInteger(ObjectValueCollector.HEIGHT);
//			
//			
//			Log.d("xpos of item about to put in database", String.valueOf(xpos));
//			
//			ContentValues data = new ContentValues();
//			data.put(DataBase.KEY_OBJECTS_VIEW_TYPE, id);
//			data.put(DataBase.KEY_OBJECTS_VIEW_XPOS, xpos);
//			data.put(DataBase.KEY_OBJECTS_VIEW_YPOS, ypos);
//			data.put(DataBase.KEY_OBJECTS_SCREEN, screenId);
//			data.put(DataBase.KEY_OBJECTS_VIEW_WIDTH, width);
//			data.put(DataBase.KEY_OBJECTS_VIEW_HEIGHT, height);
//			
//		
//		}
		
		super.onStop();
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
			changeDisplayMode(designbox.getView(), Generator.PRESENTATION_STYLE);

			exporter.requestBitmap(designbox.getView(), getContentResolver(), false);

			Toast.makeText(getApplicationContext(), getString(R.string.confirmation_save_to_gallery), Toast.LENGTH_SHORT).show();
			changeDisplayMode(designbox.getView(), Generator.CREATION_STYLE);

			break;

		case R.id.action_attach_mail:
			changeDisplayMode(designbox.getView(), Generator.PRESENTATION_STYLE);

			Intent mail = exporter.getIntent(ImageTools.SHARE);
			mail.putExtra(Intent.EXTRA_STREAM, exporter.requestBitmap(designbox.getView(), getContentResolver(), false));

			startActivityForResult(Intent.createChooser(mail, getString(R.string.intent_title_share)), ImageTools.SHARE);
			changeDisplayMode(designbox.getView(), Generator.CREATION_STYLE);

			break;

		case R.id.action_preview:
			togglePreview(item);

		default:
			break;
		}
		// displaySidebar(ITEMBOX);
		return true;
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
			changeDisplayMode(designbox.getView(), Generator.CREATION_STYLE);
			displaySidebar(ITEMBOX);

		} else
		{
			designbox.disableTouch(true);
			// item.setIcon(editIcon);

			item.setTitle(R.string.menu_action_create_mode);

			isPreview = true;
			changeDisplayMode(designbox.getView(), Generator.PRESENTATION_STYLE);
			displaySidebar(NOTHING);
			designbox.disableTouch(true);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
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
		String selection = DataBase.KEY_OBJECTS_SCREEN + "=" + "'" + String.valueOf(screenId) + "'";

		Log.d("loader", "created");
		return new CursorLoader(getApplicationContext(), DataBase.CONTENT_URI_OBJECTS, null, selection, null, null);
	}

	/**
	 * @author funklos
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor)
	{
		
		FromDatabaseObjectCreator creator = new FromDatabaseObjectCreator(arg0, cursor);

//		Log.d("loader", "finished loading");
//		while (cursor.moveToNext())
//		{
//			Log.d("database contains", "item");
//			int idx_xpos = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_XPOS);
//			
//			int idx_screenId = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_SCREEN);
//			
//			Log.d("database item ScreenId", String.valueOf(cursor.getInt(idx_screenId)));
//			
//			int xpos = cursor.getInt(idx_xpos);
//			Log.d("xpos of item in database", String.valueOf(xpos));
//		}
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
		viewList = grabber.getChildren(designbox);

		for (View view : viewList)
		{
			Bundle tagBundle = (Bundle) view.getTag();
			int style = tagBundle.getInt(displayStyle);
			view.setBackgroundResource(style);

		}

	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event)
	// {
	// switch (event.getAction())
	// {
	// case MotionEvent.ACTION_DOWN:
	// designbox.deleteOverlay();
	// displaySidebar(ITEMBOX);
	// DisplayModeChanger.setDisplayMode(designbox.getView(),
	// Generator.PRESENTATION_STYLE);
	// break;
	// case MotionEvent.ACTION_UP:
	// DisplayModeChanger.setDisplayMode(designbox.getView(),
	// Generator.CREATION_STYLE);
	// return true;
	// }
	// return false;
	// }

}
