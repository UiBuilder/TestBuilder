package uibuilder;

import helpers.IconAdapter;
import helpers.ImageTools;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import data.ObjectValues;
import data.ResArrayImporter;
import de.ur.rk.uibuilder.R;
import editmodules.AlignModule;
import editmodules.UserTextModule;

public class EditmodeFragment extends Fragment
{

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private View root;
	private View currentView;

	private ImageTools imageHandler;

	private NumberPicker picker;

	private LinearLayout modulePicture, moduleChangeSize, 
			moduleNothing, moduleIcons,  moduleListConfig,
			moduleGridConfig, moduleGridColumns, moduleContent,
			moduleBackgroundColor;

	private AlignModule alignModule;
	private UserTextModule userTextModule;

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreate called");

		imageHandler = new ImageTools(getActivity());

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		getModules();
		setupModules();


		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");

		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_editmode_fragment, container, false);

			return root;
		}
		return root;
	}

	/**
	 * @author funklos
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
		case ImageTools.CAMERA:

			if (resultCode == Activity.RESULT_OK)
			{
				imageHandler.handleBigCameraPhoto(currentView);
			}
			break;

		case ImageTools.GALLERY:

			if (resultCode == Activity.RESULT_OK)
			{
				imageHandler.handleGalleryImport(currentView, data);
			}
		}
	}

	private void getModules()
	{

		modulePicture = (LinearLayout) root.findViewById(R.id.editmode_included_choose_picture);


		moduleNothing = (LinearLayout) root.findViewById(R.id.editmode_included_nothing);
		moduleIcons = (LinearLayout) root.findViewById(R.id.editmode_included_choose_icon);
		moduleListConfig = (LinearLayout) root.findViewById(R.id.editmode_included_list_config);
		moduleGridConfig = (LinearLayout) root.findViewById(R.id.editmode_included_grid_config);
		moduleGridColumns = (LinearLayout) root.findViewById(R.id.editmode_included_grid_columns);
		moduleContent = (LinearLayout) root.findViewById(R.id.editmode_included_grid_content);
		moduleBackgroundColor = (LinearLayout) root.findViewById(R.id.editmode_included_background_color);
		// and so on..
	}

	private void setupModules()
	{
		alignModule = new AlignModule(getActivity().getApplicationContext());
		userTextModule = new UserTextModule(getActivity().getApplicationContext());

		setupPictureModule();

		setupIconModule();

		setupGridColumnModule();
		setupContentModule();

		setupListConfigModule();
		setupGridConfigModule();
		setupBackgroundColorModule();
	}

	private void setupBackgroundColorModule()
	{
		Button backgroundRed = (Button) root.findViewById(R.id.editmode_background_red);
		Button backgroundOrange = (Button) root.findViewById(R.id.editmode_background_orange);
		Button backgroundYellow = (Button) root.findViewById(R.id.editmode_background_yellow);
		Button backgroundGreenLight = (Button) root.findViewById(R.id.editmode_background_green_light);
		Button backgroundGreen = (Button) root.findViewById(R.id.editmode_background_green);
		Button backgroundAqua = (Button) root.findViewById(R.id.editmode_background_aqua);
		Button backgroundBlue = (Button) root.findViewById(R.id.editmode_background_blue);
		Button backgroundGreyLight = (Button) root.findViewById(R.id.editmode_background_grey_light);
		Button backgroundGrey = (Button) root.findViewById(R.id.editmode_background_grey);
		Button backgroundReset = (Button) root.findViewById(R.id.editmode_background_reset);

		backgroundRed.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundOrange.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundYellow.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundGreenLight.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundGreen.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundAqua.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundBlue.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundGreyLight.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundGrey.setOnClickListener(new SetBackgroundColorModuleListener());
		backgroundReset.setOnClickListener(new SetBackgroundColorModuleListener());

	}


	/**
	 * each sublayout module has an expansion selector button with the same id.
	 * get this button for each module and set the corresponding listener. a
	 * reference to the parent layout is passed to the listener to avoid final
	 * instances of references, which were not reliable enough when performing
	 * expansions
	 * 
	 * @author funklos
	 * @param module
	 *            the editmode module containing the button
	 */


	/**
	 * @author funklos
	 */
	private void setupContentModule()
	{
		Button hipster = (Button) moduleContent.findViewById(R.id.content_choose_hipster);
		Button bacon = (Button) moduleContent.findViewById(R.id.content_choose_bacon);

		hipster.setOnClickListener(new ContentSelectedListener());
		bacon.setOnClickListener(new ContentSelectedListener());

	}

	/**
	 * @author funklos
	 */
	private void setupGridConfigModule()
	{
		LinearLayout layoutTypeOne = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_1);
		LinearLayout layoutTypeTwo = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_2);
		LinearLayout layoutTypeThree = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_3);
		LinearLayout layoutTypeFour = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_4);

		GridLayoutModuleListener gridLayoutListener = new GridLayoutModuleListener();

		layoutTypeOne.setOnClickListener(gridLayoutListener);
		layoutTypeTwo.setOnClickListener(gridLayoutListener);
		layoutTypeThree.setOnClickListener(gridLayoutListener);
		layoutTypeFour.setOnClickListener(gridLayoutListener);
	}

	/**
	 * @author funklos
	 */
	private void setupGridColumnModule()
	{
		SeekBar columnNumber = (SeekBar) root.findViewById(R.id.editmode_grid_choose_number);

		columnNumber.setOnSeekBarChangeListener(new ColumnNumberListener());

	}

	/**
	 * @author funklos
	 */
	private void setupListConfigModule()
	{
		LinearLayout layoutTypeOne = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_1);
		LinearLayout layoutTypeTwo = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_2);
		LinearLayout layoutTypeThree = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_3);
		LinearLayout layoutTypeFour = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_4);
		LinearLayout layoutTypeFive = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_5);
		LinearLayout layoutTypeSix = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_6);

		ListLayoutModuleListener listLayoutListener = new ListLayoutModuleListener();

		layoutTypeOne.setOnClickListener(listLayoutListener);
		layoutTypeTwo.setOnClickListener(listLayoutListener);
		layoutTypeThree.setOnClickListener(listLayoutListener);
		layoutTypeFour.setOnClickListener(listLayoutListener);
		layoutTypeFive.setOnClickListener(listLayoutListener);
		layoutTypeSix.setOnClickListener(listLayoutListener);
	}

	

	private void setupIconModule()
	{
		GridView grid = (GridView) root.findViewById(R.id.editmode_icon_grid);

		int[] lowResIcns = ResArrayImporter.getRefArray(getActivity(), R.array.icons_small);

		IconAdapter adapter = new IconAdapter(getActivity(), lowResIcns);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new IconModuleListener());
		adapter.notifyDataSetChanged();
	}


	private void setupPictureModule()
	{
		Button takePic = (Button) root.findViewById(R.id.image_choose_camera);
		takePic.setOnClickListener(new ImageModuleListener());

		Button picFromGallery = (Button) root.findViewById(R.id.image_choose_gallery);
		picFromGallery.setOnClickListener(new ImageModuleListener());
	}

	protected void adaptLayoutToContext(View view)
	{
		currentView = view;

		Bundle tagBundle = (Bundle) currentView.getTag();
		int id = tagBundle.getInt(ObjectValues.TYPE);

		((LinearLayout) root).removeAllViews();

		switch (id)
		{
		case R.id.element_button:
			((LinearLayout) root).addView(userTextModule.getInstance(view));

			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			moduleBackgroundColor.setVisibility(View.VISIBLE);

			break;

		case R.id.element_checkbox:

			((LinearLayout) root).addView(userTextModule.getInstance(view));


			break;

		case R.id.element_edittext:

			((LinearLayout) root).addView(userTextModule.getInstance(view));


			
			((LinearLayout) root).addView(alignModule.getInstance(view));

			root.requestLayout();

			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());
			break;

		case R.id.element_imageview:
			moduleIcons.setVisibility(View.VISIBLE);
			modulePicture.setVisibility(View.VISIBLE);
			moduleBackgroundColor.setVisibility(View.VISIBLE);
			break;

		case R.id.element_radiogroup:
			((LinearLayout) root).addView(userTextModule.getInstance(view));


			break;
		case R.id.element_ratingbar:

			moduleBackgroundColor.setVisibility(View.VISIBLE);

			break;

		case R.id.element_switch:
			((LinearLayout) root).addView(userTextModule.getInstance(view));


			break;
		case R.id.element_textview:
			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			((LinearLayout) root).addView(userTextModule.getInstance(view));


			moduleBackgroundColor.setVisibility(View.VISIBLE);

			break;

		case R.id.element_list:
			moduleListConfig.setVisibility(View.VISIBLE);
			moduleContent.setVisibility(View.VISIBLE);
			break;

		case R.id.element_grid:
			moduleGridColumns.setVisibility(View.VISIBLE);
			moduleGridConfig.setVisibility(View.VISIBLE);
			moduleContent.setVisibility(View.VISIBLE);

			ViewGroup container = (ViewGroup) currentView;

			GridView grid = (GridView) container.getChildAt(0);
			SeekBar bar = (SeekBar) moduleGridColumns.findViewById(R.id.editmode_grid_choose_number);
			TextView display = (TextView) moduleGridColumns.findViewById(R.id.editmode_grid_display);

			bar.setProgress(grid.getNumColumns() - 2);
			display.setText(String.valueOf(bar.getProgress() + 2));
			break;

		default:
			moduleNothing.setVisibility(View.VISIBLE);
			break;
		}


		root.invalidate();
	}


	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ContentSelectedListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();

			switch (id)
			{
			case R.id.content_choose_hipster:
			case R.id.content_choose_bacon:
				editListener.setSampleContent(currentView, id);
			}

		}
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ColumnNumberListener implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar bar, int val, boolean arg2)
		{
			ViewGroup parent = (ViewGroup) bar.getParent();
			TextView feedback = (TextView) parent.findViewById(R.id.editmode_grid_display);
			feedback.setText(String.valueOf(val + 2));

			editListener.gridColumnsChanged(currentView, val + 2);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0)
		{
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class GridLayoutModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View gridLayout)
		{
			int id = gridLayout.getId();

			switch (id)
			{
			case R.id.editmode_grid_included_layout_1:
			case R.id.editmode_grid_included_layout_2:
			case R.id.editmode_grid_included_layout_3:
			case R.id.editmode_grid_included_layout_4:
				editListener.refreshAdapter(currentView, id);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ListLayoutModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View listLayout)
		{
			int id = listLayout.getId();

			switch (id)
			{
			case R.id.editmode_list_included_layout_1:
			case R.id.editmode_list_included_layout_2:
			case R.id.editmode_list_included_layout_3:
			case R.id.editmode_list_included_layout_4:
			case R.id.editmode_list_included_layout_5:
			case R.id.editmode_list_included_layout_6:
				editListener.refreshAdapter(currentView, id);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class IconModuleListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos,
				long arg3)
		{
			editListener.setIconResource(currentView, pos);
		}
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ImageModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.image_choose_camera:

				Intent cameraIntent = imageHandler.getIntent(ImageTools.CAMERA);
				startActivityForResult(cameraIntent, ImageTools.CAMERA);

				break;

			case R.id.image_choose_gallery:

				Intent galleryIntent = imageHandler.getIntent(ImageTools.GALLERY);
				startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), ImageTools.GALLERY);
				break;
			}
		}
	}

	private class SetBackgroundColorModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			Bundle bundle = (Bundle) currentView.getTag();

			switch (v.getId())
			{
			case R.id.editmode_background_red:
				currentView.setBackgroundResource(R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_red);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_red);

				break;

			case R.id.editmode_background_yellow:
				currentView.setBackgroundResource(R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_dark);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_dark);

				break;

			case R.id.editmode_background_orange:
				currentView.setBackgroundResource(R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_orange);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_orange);

				break;

			case R.id.editmode_background_green_light:
				currentView.setBackgroundResource(R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green_light);

				break;

			case R.id.editmode_background_green:
				currentView.setBackgroundResource(R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_green);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_green);

				break;

			case R.id.editmode_background_aqua:
				currentView.setBackgroundResource(R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_aqua);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_aqua);

				break;

			case R.id.editmode_background_blue:
				currentView.setBackgroundResource(R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_blue);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_blue);

				break;

			case R.id.editmode_background_grey_light:
				currentView.setBackgroundResource(R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey_light);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey_light);

				break;

			case R.id.editmode_background_grey:
				currentView.setBackgroundResource(R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_grey);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_object_background_grey);

				break;

			case R.id.editmode_background_reset:
				currentView.setBackgroundResource(R.drawable.object_background_default);

				resetBackgroundToDefault(bundle);

			}

		}

		private void resetBackgroundToDefault(Bundle bundle)
		{
			bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default);

			switch (bundle.getInt(ObjectValues.TYPE))
			{
			case R.id.element_button:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default_button);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_button_default);
				break;

			case R.id.element_edittext:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default_edittext);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_border_medium);
				break;

			default:
				bundle.putInt(ObjectValues.BACKGROUND_EDIT, R.drawable.object_background_default);
				bundle.putInt(ObjectValues.BACKGROUND_PRES, R.drawable.presentation_default_object);
				break;

			}
		}
	}





	public interface onObjectEditedListener
	{
		void setSampleContent(View active, int id);

		void refreshAdapter(View active, int id);

		void gridColumnsChanged(View active, int col);

		void setIconResource(View active, int pos);
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}

}
