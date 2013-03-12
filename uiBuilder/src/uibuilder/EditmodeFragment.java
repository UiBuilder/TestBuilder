package uibuilder;

import helpers.IconAdapter;
import helpers.ResArrayImporter;
import helpers.ImageTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import creators.Generator;
import de.ur.rk.uibuilder.R;

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
	private View active;

	private LayoutInflater inflater;
	
	private ImageTools imageHandler;
	private IconAdapter adapter;
	
	private EditText editText, editSize;
	private NumberPicker picker;

	private SeekBar starBar, ratingSlider;
	private Button topLeft, topCenter, topRight, centerLeft, centerCenter, centerRight, bottomLeft, bottomCenter, bottomRight;

	private LinearLayout moduleAlign, modulePicture, moduleEditText, moduleChangeSize, moduleZorder, moduleNothing, moduleIcons, moduleStarCount, moduleListConfig, moduleGridConfig;

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
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");

		this.inflater = inflater;
		if (root == null) {
			root = inflater.inflate(R.layout.layout_editmode_fragment, container, false);

			getModules();

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

		switch (requestCode) {

		case ImageTools.CAMERA: {
			if (resultCode == Activity.RESULT_OK) {
				imageHandler.handleBigCameraPhoto(currentView);
			}
			break;
		}

		case ImageTools.GALLERY: {
			if (resultCode == Activity.RESULT_OK) {
				imageHandler.handleGalleryImport(currentView, data);
			}
		}
		}
	}

	private void getModules()
	{
		moduleAlign = (LinearLayout) root.findViewById(R.id.editmode_included_align_content);
		moduleEditText = (LinearLayout) root.findViewById(R.id.editmode_included_text);
		modulePicture = (LinearLayout) root.findViewById(R.id.editmode_included_choose_picture);
		moduleChangeSize = (LinearLayout) root.findViewById(R.id.editmode_included_changesize);
		moduleZorder = (LinearLayout) root.findViewById(R.id.editmode_included_order);
		moduleNothing = (LinearLayout) root.findViewById(R.id.editmode_included_nothing);
		moduleIcons = (LinearLayout) root.findViewById(R.id.editmode_included_choose_icon);
		moduleStarCount = (LinearLayout) root.findViewById(R.id.editmode_included_star_count);
		moduleListConfig = (LinearLayout) root.findViewById(R.id.editmode_included_list_config);
		moduleGridConfig = (LinearLayout) root.findViewById(R.id.editmode_included_grid_config);
		
		setupPictureModule();
		setupEdittextModule();
		setupChangesizeModule();
		setupAlignModule();
		setupZorderModule();
		setupIconModule();
		setupStarCountModule();
		setupListConfigModule();
		setupGridConfigModule();
		
		setExpansionSelector(moduleGridConfig);
		setExpansionSelector(moduleAlign);
		setExpansionSelector(moduleIcons);
		setExpansionSelector(modulePicture); 
		setExpansionSelector(moduleEditText);
		setExpansionSelector(moduleListConfig);

		root.invalidate();
		// and so on..
	}

	private void setExpansionSelector(View module)
	{
		module.setVisibility(View.GONE);
		module.invalidate();
		ImageButton b = (ImageButton) module.findViewById(R.id.expand_selector);
		b.setOnClickListener(new ExpansionListener((LinearLayout) module));
		b.performClick();
		root.requestLayout();
		
		//moduleGridConfig.setOnClickListener(new ExpansionListener());
		
		//ExpansionListener grid = new ExpansionListener(moduleGridConfig);
		/*setExpansionSelector(moduleIcons);
		setExpansionSelector(moduleAlign);
		setExpansionSelector(modulePicture);
		setExpansionSelector(moduleEditText);*/
		
	}

	private void setupGridConfigModule()
	{	
		LinearLayout layoutTypeOne = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_1);
		LinearLayout layoutTypeTwo = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_2);
		LinearLayout layoutTypeThree = (LinearLayout) root.findViewById(R.id.editmode_grid_included_layout_3);
		
		layoutTypeOne.setOnClickListener(new GridLayoutModuleListener());
		layoutTypeTwo.setOnClickListener(new GridLayoutModuleListener());
		layoutTypeThree.setOnClickListener(new GridLayoutModuleListener());
	}
	
	private class ExpansionListener implements OnClickListener
	{
		private LinearLayout module;
		
		public ExpansionListener(LinearLayout module)
		{
			// TODO Auto-generated constructor stub
			this.module = module;
		}

		@Override
		public void onClick(View v)
		{
			ImageButton toggle = (ImageButton) v;
			View expandable = module.findViewById(R.id.expandable);
			
			if (!v.isActivated())
			{
				expandable.setVisibility(View.GONE);
				toggle.setImageResource(R.raw.ico_small_0037);
				toggle.setActivated(true);
			}
			else
			{
				expandable.setVisibility(View.VISIBLE);
				toggle.setImageResource(R.raw.ico_small_0035); 
				toggle.setActivated(false);
			}
			Log.d("found", "button");
			expandable.invalidate();
			module.requestLayout();
		}
	}

	private void setupListConfigModule()
	{
		LinearLayout layoutTypeOne = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_1);
		LinearLayout layoutTypeTwo = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_2);
		LinearLayout layoutTypeThree = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_3);
		LinearLayout layoutTypeFour = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_4);
		LinearLayout layoutTypeFive = (LinearLayout) root.findViewById(R.id.editmode_list_included_layout_5);
		
		layoutTypeOne.setOnClickListener(new ListLayoutModuleListener());
		layoutTypeTwo.setOnClickListener(new ListLayoutModuleListener());
		layoutTypeThree.setOnClickListener(new ListLayoutModuleListener());
		layoutTypeFour.setOnClickListener(new ListLayoutModuleListener());
		layoutTypeFive.setOnClickListener(new ListLayoutModuleListener());
	}

	private void setupStarCountModule()
	{
		starBar = (SeekBar) root.findViewById(R.id.star_count_seekbar);
		ratingSlider = (SeekBar) root.findViewById(R.id.star_rating_seekbar);
		starBar.setMax(9);
		starBar.setOnSeekBarChangeListener(new StarCountModuleListener());
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

		pullToFront.setOnClickListener(new AlignModuleListener());
		pushToBack.setOnClickListener(new ImageModuleListener());
	}

	private void setupChangesizeModule()
	{
		picker = (NumberPicker) root.findViewById(R.id.item_edit_editsize_picker);
		picker.setOnValueChangedListener(new ChangesizeModuleListener());
		picker.setMinValue(5);
		picker.setMaxValue(100);

	}

	private void setupEdittextModule()
	{
		editText = (EditText) root.findViewById(R.id.item_edit_edittext);
		editText.addTextChangedListener(new EditTextModuleListener());
	}

	private void setupAlignModule()
	{
		
		topLeft = (Button) root.findViewById(R.id.editmode_align_top_left);
		topRight = (Button) root.findViewById(R.id.editmode_align_top_center);
		topCenter = (Button) root.findViewById(R.id.editmode_align_top_right);
		centerLeft = (Button) root.findViewById(R.id.editmode_align_center_left);
		centerCenter = (Button) root.findViewById(R.id.editmode_align_center_center);
		centerRight = (Button) root.findViewById(R.id.editmode_align_center_right);
		bottomLeft = (Button) root.findViewById(R.id.editmode_align_bottom_left);
		bottomCenter = (Button) root.findViewById(R.id.editmode_align_bottom_center);
		bottomRight = (Button) root.findViewById(R.id.editmode_align_bottom_right);

		topLeft.setOnClickListener(new AlignModuleListener());
		topCenter.setOnClickListener(new AlignModuleListener());
		topRight.setOnClickListener(new AlignModuleListener());
		centerLeft.setOnClickListener(new AlignModuleListener());
		centerCenter.setOnClickListener(new AlignModuleListener());
		centerRight.setOnClickListener(new AlignModuleListener());
		bottomLeft.setOnClickListener(new AlignModuleListener());
		bottomCenter.setOnClickListener(new AlignModuleListener());
		bottomRight.setOnClickListener(new AlignModuleListener());

		/*
		 * int gravity = ((TextView) currentView).getGravity(); switch (gravity)
		 * { case Gravity.TOP | Gravity.LEFT:
		 * topLeft.setBackgroundColor(getResources
		 * ().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.TOP | Gravity.CENTER_HORIZONTAL:
		 * topCenter.setBackgroundColor
		 * (getResources().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.TOP | Gravity.RIGHT:
		 * topRight.setBackgroundColor(getResources
		 * ().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.CENTER_VERTICAL | Gravity.LEFT:
		 * centerLeft.setBackgroundColor
		 * (getResources().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.CENTER:
		 * centerCenter.setBackgroundColor(getResources().getColor
		 * (R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.CENTER_VERTICAL | Gravity.RIGHT:
		 * centerRight.setBackgroundColor
		 * (getResources().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.BOTTOM | Gravity.LEFT:
		 * bottomLeft.setBackgroundColor(getResources
		 * ().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL:
		 * bottomCenter.setBackgroundColor
		 * (getResources().getColor(R.color.overlay_background_active)); break;
		 * 
		 * case Gravity.BOTTOM | Gravity.RIGHT:
		 * bottomRight.setBackgroundColor(getResources
		 * ().getColor(R.color.overlay_background_active)); break;
		 * 
		 * }
		 */

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
		Bundle tagBundle = (Bundle) view.getTag();

		int id = tagBundle.getInt(Generator.ID);

		resetModules();

		currentView = view;

		switch (id) {
		case R.id.element_button:
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			// moduleAlign.setVisibility(View.VISIBLE);

			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			break;

		case R.id.element_checkbox:
			
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;

		case R.id.element_edittext:

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(((TextView) currentView).getHint());

			moduleAlign.setVisibility(View.VISIBLE);
			adaptAlignButtons(currentView);

			//moduleTextSize.setVisibility(View.VISIBLE);
			break;

		case R.id.element_imageview:
			moduleIcons.setVisibility(View.VISIBLE);
			modulePicture.setVisibility(View.VISIBLE);
			break;

		case R.id.element_numberpick:
			moduleNothing.setVisibility(View.VISIBLE);

			break;
		case R.id.element_radiogroup:
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;
		case R.id.element_ratingbar:

			moduleStarCount.setVisibility(View.VISIBLE);

			starBar.setProgress(((RatingBar) ((ViewGroup) currentView).getChildAt(0)).getNumStars() - 1);

			ratingSlider.setProgress((int) ((RatingBar) ((ViewGroup) currentView).getChildAt(0)).getRating());

			break;
		// case R.id.element_search:
		// moduleNothing.setVisibility(View.VISIBLE);
		// // moduleSearch.setVisibility(View.VISIBLE); collapsed etc
		// break;
		case R.id.element_switch:
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;
		case R.id.element_textview:
			moduleChangeSize.setVisibility(View.VISIBLE);
			picker.setValue((int) ((TextView) currentView).getTextSize());

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			moduleAlign.setVisibility(View.VISIBLE);
			adaptAlignButtons(currentView);

			break;
		case R.id.element_timepicker:
			moduleNothing.setVisibility(View.VISIBLE);
			break;

		case R.id.element_seekbar:
			moduleNothing.setVisibility(View.VISIBLE);
			break;
			
		case R.id.element_list:
			moduleListConfig.setVisibility(View.VISIBLE);
			break;
			
		case R.id.element_grid:
			moduleGridConfig.setVisibility(View.VISIBLE);
			break;
			
		default:

			break;
		}
		// moduleZorder.setVisibility(View.VISIBLE);

		root.invalidate();
	}

	private void adaptAlignButtons(View currentView2)
	{
		clearAlignSelection();
		
		switch (((TextView) currentView).getGravity()) {
		case Gravity.TOP | Gravity.LEFT:
			topLeft.setActivated(true);
			break;
		case Gravity.TOP | Gravity.CENTER_HORIZONTAL:
			topCenter.setActivated(true);

			break;
		case Gravity.TOP | Gravity.RIGHT:
			topRight.setActivated(true);

			break;
		case Gravity.CENTER_VERTICAL | Gravity.LEFT:

			centerLeft.setActivated(true);
			break;
		case Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL:
			centerCenter.setActivated(true);

			break;
		case Gravity.CENTER_VERTICAL | Gravity.RIGHT:
			centerRight.setActivated(true);

			break;
		case Gravity.BOTTOM | Gravity.LEFT:
			bottomLeft.setActivated(true);
			break;
		case Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL:
			bottomCenter.setActivated(true);

			break;
		case Gravity.BOTTOM | Gravity.RIGHT:
			bottomRight.setActivated(true);

			break;
		}
	}

	protected void clearAlignSelection()
	{
		topLeft.setActivated(false);
		topCenter.setActivated(false);
		topRight.setActivated(false);
		centerLeft.setActivated(false);
		centerCenter.setActivated(false);
		centerRight.setActivated(false);
		bottomLeft.setActivated(false);
		bottomCenter.setActivated(false);
		bottomRight.setActivated(false);
	}

	private CharSequence getViewText(View view)
	{
		if (view instanceof LinearLayout) {
			TextView textView = (TextView) ((LinearLayout) view).getChildAt(0);

			return textView.getText();

		}
		return ((TextView) currentView).getText();

	}

	public void setViewText(String string)
	{
		if (currentView instanceof LinearLayout) {
			TextView textView = (TextView) ((LinearLayout) currentView).getChildAt(0);

			textView.setText(string);

		} else if (currentView instanceof EditText) {
			((EditText) currentView).setHint(string);

		} else {
			((TextView) currentView).setText(string);
		}
	}

	/**
	 * @author funklos
	 */
	private void resetModules()
	{
		moduleAlign.setVisibility(View.GONE);
		moduleEditText.setVisibility(View.GONE);
		modulePicture.setVisibility(View.GONE);
		moduleChangeSize.setVisibility(View.GONE);
		moduleZorder.setVisibility(View.GONE);
		moduleNothing.setVisibility(View.GONE);
		moduleIcons.setVisibility(View.GONE);
		moduleStarCount.setVisibility(View.GONE);
		moduleListConfig.setVisibility(View.GONE);
		moduleGridConfig.setVisibility(View.GONE);

		moduleAlign.invalidate();
		moduleEditText.invalidate();
		modulePicture.invalidate();
		moduleChangeSize.invalidate();
		moduleZorder.invalidate();
		moduleNothing.invalidate();
		moduleIcons.invalidate();
		moduleStarCount.invalidate();
		moduleListConfig.invalidate();
		moduleGridConfig.invalidate();
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
				editListener.prefencesChanged(currentView, id);
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
				editListener.prefencesChanged(currentView, id);
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
		int[] highResIcns;

		public IconModuleListener()
		{
			highResIcns = ResArrayImporter.getRefArray(getActivity(), R.array.icons_big);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3)
		{
			int resourceId = (highResIcns[pos]);

			((ImageView) currentView).setScaleType(ScaleType.FIT_CENTER);
			((ImageView) currentView).setImageResource(resourceId);
			
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

	private class ZorderModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId()) {
			case R.id.editmode_z_order_back:

				break;

			case R.id.editmode_z_order_front:
				currentView.bringToFront();
				break;

			default:
				break;
			}

		}

	}

	private class StarCountModuleListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			switch (seekBar.getId()) {
			case R.id.star_count_seekbar:
				((RatingBar) ((ViewGroup) currentView).getChildAt(0)).setNumStars(progress + 1);
				ratingSlider.setMax(progress + 1);
				// ratingSlider.setMax((int) ((RatingBar) ((ViewGroup)
				// currentView).getChildAt(0)).getNumStars());
				break;
			case R.id.star_rating_seekbar:
				Log.d("RatingSeekbar", "gotValue for progress: " + progress);
				((RatingBar) ((ViewGroup) currentView).getChildAt(0)).setRating(progress);
				
			;
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
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			// TODO Auto-generated method stub

		}
	}

	private class AlignModuleListener implements OnClickListener
	{
		
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v)
		{
			clearAlignSelection();
			v.setActivated(true);
			

//			if (v != active) {
//				if (active != null) {
//					active.setActivated(false);
//				}
//
//				active = v;
//				active.setActivated(true);
//			}

			switch (v.getId()) {
			
			
			case R.id.editmode_align_top_left:
				((TextView) currentView).setGravity(Gravity.LEFT);
				break;
			case R.id.editmode_align_top_center:
				((TextView) currentView).setGravity(Gravity.CENTER_HORIZONTAL);
				break;
			case R.id.editmode_align_top_right:
				((TextView) currentView).setGravity(Gravity.RIGHT);
				break;
			case R.id.editmode_align_center_left:
				((TextView) currentView).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_center_center:
				((TextView) currentView).setGravity(Gravity.CENTER);

				break;
			case R.id.editmode_align_center_right:
				((TextView) currentView).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_bottom_left:
				((TextView) currentView).setGravity(Gravity.LEFT | Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_center:
				((TextView) currentView).setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_right:
				((TextView) currentView).setGravity(Gravity.RIGHT | Gravity.BOTTOM);

				break;

			}

		}

	}

	private class ChangesizeModuleListener implements OnValueChangeListener
	{
		private void changeSize(TextView view, int sizeStep)
		{
			Paint p = new Paint();
			p = view.getPaint();

			Log.d("ChangeSize", "called with sizeStep = " + sizeStep);

			int currentSize = (int) p.getTextSize();
			Log.d("ChangeSize", "currentSize = " + currentSize);

			int newSize = currentSize + sizeStep;
			Log.d("ChangeSize", "newSize = " + newSize);

			// p.setTextSize(newSize);
			((TextView) currentView).setTextSize(newSize);
			editSize.setText(String.valueOf(newSize));

		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal)
		{
			((TextView) currentView).setTextSize(newVal);

		}
	}

	public interface onObjectEditedListener
	{
		void refreshOverlay(View active, int type);
		
		void prefencesChanged(View active, int from);
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}

}
