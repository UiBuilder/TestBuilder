package uibuilder;

import helpers.IconAdapter;
import helpers.ImageTools;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import data.ObjectValues;
import data.ResArrayImporter;
import de.ur.rk.uibuilder.R;
import editmodules.AlignModule;

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

	private EditText editText;
	private NumberPicker picker;

	private SeekBar starBar, ratingSlider;
	private Button topLeft, topCenter, topRight, centerLeft, centerCenter,
			centerRight, bottomLeft, bottomCenter, bottomRight;

	private LinearLayout /*moduleAlign, */modulePicture, moduleEditText,
			moduleChangeSize, moduleZorder, moduleNothing, moduleIcons,
			moduleStarCount, moduleListConfig, moduleGridConfig,
			moduleGridColumns, moduleContent, moduleBackgroundColor;

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
		setExpansionSelectos();

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
		//moduleAlign = (LinearLayout) root.findViewById(R.id.editmode_included_align_content);
		moduleEditText = (LinearLayout) root.findViewById(R.id.editmode_included_text);
		modulePicture = (LinearLayout) root.findViewById(R.id.editmode_included_choose_picture);
		moduleChangeSize = (LinearLayout) root.findViewById(R.id.editmode_included_changesize);
		moduleZorder = (LinearLayout) root.findViewById(R.id.editmode_included_order);
		moduleNothing = (LinearLayout) root.findViewById(R.id.editmode_included_nothing);
		moduleIcons = (LinearLayout) root.findViewById(R.id.editmode_included_choose_icon);
		moduleStarCount = (LinearLayout) root.findViewById(R.id.editmode_included_star_count);
		moduleListConfig = (LinearLayout) root.findViewById(R.id.editmode_included_list_config);
		moduleGridConfig = (LinearLayout) root.findViewById(R.id.editmode_included_grid_config);
		moduleGridColumns = (LinearLayout) root.findViewById(R.id.editmode_included_grid_columns);
		moduleContent = (LinearLayout) root.findViewById(R.id.editmode_included_grid_content);
		moduleBackgroundColor = (LinearLayout) root.findViewById(R.id.editmode_included_background_color);
		// and so on..
	}
AlignModule align;
	private void setupModules()
	{
		align = new AlignModule(getActivity().getApplicationContext());
		
		setupPictureModule();
		setupEdittextModule();
		setupChangesizeModule();
		//setupAlignModule();
		setupZorderModule();
		setupIconModule();
		setupStarCountModule();
		
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

	private void setExpansionSelectos()
	{
		setExpansionSelector(moduleGridConfig);
		//setExpansionSelector(moduleAlign);
		setExpansionSelector(moduleIcons);
		setExpansionSelector(modulePicture);
		setExpansionSelector(moduleEditText);
		setExpansionSelector(moduleListConfig);
		setExpansionSelector(moduleChangeSize);
		setExpansionSelector(moduleGridColumns);
		setExpansionSelector(moduleContent);
		setExpansionSelector(moduleBackgroundColor);
		setExpansionSelector(moduleStarCount);
		setExpansionSelector(moduleZorder);

		root.invalidate();
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
	private void setExpansionSelector(View module)
	{

		module.invalidate();

		ImageButton expandIndicator = (ImageButton) module.findViewById(R.id.expand_selector);

		View toggleArea = (View) expandIndicator.getParent();
		toggleArea.setOnClickListener(new ExpansionListener((LinearLayout) module, expandIndicator));

		/**
		 * onclick has to be called once for the button to react. this was the
		 * only workaround which did the job.
		 */
		toggleArea.performClick();
		root.requestLayout();
	}

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

	private void setupStarCountModule()
	{
		starBar = (SeekBar) root.findViewById(R.id.star_count_seekbar);
		ratingSlider = (SeekBar) root.findViewById(R.id.star_rating_seekbar);
		starBar.setMax(9);
		starBar.setOnSeekBarChangeListener(new StarCountModuleListener());
		ratingSlider.setMax(10);

		ratingSlider.setOnSeekBarChangeListener(new StarCountModuleListener());
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

	private void setupZorderModule()
	{
		Button pullToFront = (Button) root.findViewById(R.id.editmode_z_order_front);
		Button pushToBack = (Button) root.findViewById(R.id.editmode_z_order_back);

		pullToFront.setOnClickListener(new ZorderModuleListener());
		pushToBack.setOnClickListener(new ZorderModuleListener());
	}

	private void setupChangesizeModule()
	{
		picker = (NumberPicker) root.findViewById(R.id.item_edit_editsize_picker);
		picker.setOnValueChangedListener(new FontsizeModuleListener());
		picker.setMinValue(5);
		picker.setMaxValue(130);

	}

	private void setupEdittextModule()
	{
		editText = (EditText) root.findViewById(R.id.item_edit_edittext);
		editText.addTextChangedListener(new EditTextModuleListener());
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
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			moduleBackgroundColor.setVisibility(View.VISIBLE);

			break;

		case R.id.element_checkbox:

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;

		case R.id.element_edittext:

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(((TextView) currentView).getHint());

			//moduleAlign.setVisibility(View.VISIBLE);
			//adaptAlignButtons(currentView);
			
			((LinearLayout)root).addView(align.getInstance(view));
			
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
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;
		case R.id.element_ratingbar:

			moduleBackgroundColor.setVisibility(View.VISIBLE);

			moduleStarCount.setVisibility(View.VISIBLE);
			ratingSlider.setProgress((int)(((RatingBar) ((ViewGroup) currentView).getChildAt(0)).getRating()));

			starBar.setProgress(((RatingBar) ((ViewGroup) currentView).getChildAt(0)).getNumStars()-1);

//			ratingSlider.setProgress(5);
			break;

		case R.id.element_switch:
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;
		case R.id.element_textview:
			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			//moduleAlign.setVisibility(View.VISIBLE);
			//adaptAlignButtons(currentView);

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
		moduleZorder.setVisibility(View.VISIBLE);

		root.invalidate();
	}

	private CharSequence getViewText(View view)
	{
		if (view instanceof LinearLayout)
		{
			TextView textView = (TextView) ((LinearLayout) view).getChildAt(0);

			return textView.getText();

		}
		return ((TextView) currentView).getText();

	}

	public void setViewText(String string)
	{
		if (currentView instanceof LinearLayout && currentView !=null)
		{
			TextView textView = (TextView) ((LinearLayout) currentView).getChildAt(0);

			textView.setText(string);

		} else if (currentView instanceof EditText&& currentView !=null)
		{
			((EditText) currentView).setHint(string);

		} else if(currentView !=null)
		{
			((TextView) currentView).setText(string);
		}
	}

	/**
	 * @author funklos
	 */
	private void resetModules()
	{
		//moduleAlign.setVisibility(View.GONE);
		moduleEditText.setVisibility(View.GONE);
		modulePicture.setVisibility(View.GONE);
		moduleChangeSize.setVisibility(View.GONE);
		moduleZorder.setVisibility(View.GONE);
		moduleNothing.setVisibility(View.GONE);
		moduleIcons.setVisibility(View.GONE);
		moduleStarCount.setVisibility(View.GONE);
		moduleListConfig.setVisibility(View.GONE);
		moduleGridConfig.setVisibility(View.GONE);
		moduleGridColumns.setVisibility(View.GONE);
		moduleContent.setVisibility(View.GONE);
		moduleBackgroundColor.setVisibility(View.GONE);

		//moduleAlign.invalidate();
		moduleEditText.invalidate();
		modulePicture.invalidate();
		moduleChangeSize.invalidate();
		moduleZorder.invalidate();
		moduleNothing.invalidate();
		moduleIcons.invalidate();
		moduleStarCount.invalidate();
		moduleListConfig.invalidate();
		moduleGridConfig.invalidate();
		moduleGridColumns.invalidate();
		moduleContent.invalidate();
		moduleBackgroundColor.invalidate();
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

	private class ZorderModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			ViewGroup parent = (ViewGroup) currentView.getParent();
			
			
			switch (v.getId())
			{
			case R.id.editmode_z_order_back:
				parent.removeView(currentView);
				ArrayList<View> allItems = new ArrayList<View>();
				
				allItems.add(currentView);
				
				int number = parent.getChildCount();
				for (int i=0; i<number; i++)
				{
					allItems.add(parent.getChildAt(i));
				}
				parent.removeAllViews();
				
				for (View child : allItems)
				{
					parent.addView(child);
				}
				parent.invalidate();
				
				break;

			case R.id.editmode_z_order_front:
				currentView.bringToFront();
				parent.invalidate();
				break;

			default:
				break;
			}
		}
	}

	private class StarCountModuleListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{
			switch (seekBar.getId())
			{
			case R.id.star_count_seekbar:
				((RatingBar) ((ViewGroup) currentView).getChildAt(0)).setNumStars(progress +1);
				ratingSlider.setMax(progress +1);
				
				Log.d("StarcountSeekbar", "gotValue for progress: " + progress +1);
				break;
				
			case R.id.star_rating_seekbar:
				Log.d("RatingSeekbar", "gotValue for progress: " + progress);
				((RatingBar) ((ViewGroup) currentView).getChildAt(0)).setRating(progress);
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 
	 * @author pattern: funklos
	 * 
	 */
	private class EditTextModuleListener implements TextWatcher
	{

		@Override
		public void afterTextChanged(Editable s)
		{
			setViewText(s.toString());

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{
			// TODO Auto-generated method stub

		}
	}

	private class FontsizeModuleListener implements OnValueChangeListener
	{

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal)
		{
			((TextView) currentView).setTextSize(newVal);
			currentView.invalidate();
		}
	}

	/**
	 * in each editmode module a button
	 * 
	 * @author funklos
	 * 
	 */
	private LinearLayout expandedBox;
	
	private class ExpansionListener implements OnClickListener
	{
		private LinearLayout module;
		private ImageButton indicator;
		private LinearLayout clickableArea;
		private View expandableView;

		private int indicatorExpanded = R.raw.ico_small_0037;
		private int indicatorCollapsed = R.raw.ico_small_0035;

		public ExpansionListener(LinearLayout module, ImageButton indicator)
		{
			this.module = module;
			this.indicator = indicator;
			this.clickableArea = (LinearLayout) indicator.getParent();
			this.expandableView = this.module.findViewById(R.id.expandable);
			
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
			expandableView.setVisibility(View.VISIBLE);
			indicator.setImageResource(indicatorCollapsed);
			module.setActivated(false);

			expandedBox = module;
			refresh();
		}

		private void collapse()
		{
			expandableView.setVisibility(View.GONE);
			indicator.setImageResource(indicatorExpanded);
			module.setActivated(true);

			expandedBox = null;
			refresh();
		}

		private void refresh()
		{
			expandableView.invalidate();
			module.invalidate();
			module.requestLayout();
			expandableView.forceLayout();
			root.forceLayout();
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
