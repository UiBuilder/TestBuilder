package uibuilder;

import helpers.IconAdapter;
import helpers.IconHelper;
import helpers.ImageTools;

import java.util.ArrayList;

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

	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	private View currentView;
	private EditText editText, editSize;
	private NumberPicker picker;
	private ImageTools imageHandler;
	private IconAdapter adapter;
	private IconHelper iconHelper;
	private SeekBar starBar;

	private View active;

	private LinearLayout moduleAlign, modulePicture, moduleEditText, moduleItemCount, moduleChangeSize, moduleZorder, moduleNothing, moduleIcons,
			moduleStarCount;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");

		this.inflater = inflater;
		if (layoutView == null) {
			layoutView = inflater.inflate(R.layout.layout_editmode_fragment, container, false);

			getModules();

			return layoutView;
		}

		return layoutView;
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
		moduleAlign = (LinearLayout) layoutView.findViewById(R.id.editmode_included_align_content);
		moduleEditText = (LinearLayout) layoutView.findViewById(R.id.editmode_included_text);
		modulePicture = (LinearLayout) layoutView.findViewById(R.id.editmode_included_choose_picture);
		moduleItemCount = (LinearLayout) layoutView.findViewById(R.id.editmode_included_item_count);
		moduleChangeSize = (LinearLayout) layoutView.findViewById(R.id.editmode_included_changesize);
		moduleZorder = (LinearLayout) layoutView.findViewById(R.id.editmode_included_order);
		moduleNothing = (LinearLayout) layoutView.findViewById(R.id.editmode_included_nothing);
		moduleIcons = (LinearLayout) layoutView.findViewById(R.id.editmode_included_choose_icon);
		moduleStarCount = (LinearLayout) layoutView.findViewById(R.id.editmode_included_star_count);

		setupPictureModule();
		setupEdittextModule();
		setupChangesizeModule();
		setupAlignModule();
		setupZorderModule();
		setupIconModule();
		setupStarCountModule();

		// and so on..
	}

	private void setupStarCountModule()
	{
		starBar = (SeekBar) layoutView.findViewById(R.id.star_count_seekbar);
		starBar.setOnSeekBarChangeListener(new StarCountModuleListener());
	}

	private void setupIconModule()
	{
		GridView grid = (GridView) layoutView.findViewById(R.id.editmode_icon_grid);

		iconHelper = new IconHelper();
		IconAdapter adapter = new IconAdapter(getActivity(), iconHelper.getLowRes());

		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new IconModuleListener());
		adapter.notifyDataSetChanged();
	}

	private void setupZorderModule()
	{
		Button pullToFront = (Button) layoutView.findViewById(R.id.editmode_z_order_front);
		Button pushToBack = (Button) layoutView.findViewById(R.id.editmode_z_order_back);

		pullToFront.setOnClickListener(new AlignModuleListener());
		pushToBack.setOnClickListener(new ImageModuleListener());
	}

	private void setupChangesizeModule()
	{

		picker = (NumberPicker) layoutView.findViewById(R.id.item_edit_editsize_picker);
		picker.setOnValueChangedListener(new ChangesizeModuleListener());
		picker.setMinValue(5);
		picker.setMaxValue(100);

	}

	private void setupEdittextModule()
	{

		editText = (EditText) layoutView.findViewById(R.id.item_edit_edittext);
		editText.addTextChangedListener(new EditTextModuleListener());

	}

	private void setupAlignModule()
	{

		Button topLeft = (Button) layoutView.findViewById(R.id.editmode_align_top_left);
		Button topRight = (Button) layoutView.findViewById(R.id.editmode_align_top_center);
		Button topCenter = (Button) layoutView.findViewById(R.id.editmode_align_top_right);
		Button centerLeft = (Button) layoutView.findViewById(R.id.editmode_align_center_left);
		Button centerCenter = (Button) layoutView.findViewById(R.id.editmode_align_center_center);
		Button centerRight = (Button) layoutView.findViewById(R.id.editmode_align_center_right);
		Button bottomLeft = (Button) layoutView.findViewById(R.id.editmode_align_bottom_left);
		Button bottomCenter = (Button) layoutView.findViewById(R.id.editmode_align_bottom_center);
		Button bottomRight = (Button) layoutView.findViewById(R.id.editmode_align_bottom_right);

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
		Button takePic = (Button) layoutView.findViewById(R.id.image_choose_camera);
		takePic.setOnClickListener(new ImageModuleListener());

		Button picFromGallery = (Button) layoutView.findViewById(R.id.image_choose_gallery);
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

			// moduleItemCount.setVisibility(View.VISIBLE);
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			break;

		case R.id.element_edittext:

			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(((TextView) currentView).getText());

			moduleAlign.setVisibility(View.VISIBLE);

			// moduleTextSize.setVisibility(View.VISIBLE);
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

			// moduleItemCount.setVisibility(View.VISIBLE);
			break;
		case R.id.element_ratingbar:

			moduleStarCount.setVisibility(View.VISIBLE);

			starBar.setProgress((int) ((RatingBar) ((ViewGroup) currentView).getChildAt(0)).getRating());

			break;
		case R.id.element_search:
			moduleNothing.setVisibility(View.VISIBLE);
			// moduleSearch.setVisibility(View.VISIBLE); collapsed etc
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

			moduleAlign.setVisibility(View.VISIBLE);

			break;
		case R.id.element_timepicker:
			moduleNothing.setVisibility(View.VISIBLE);
			break;

		case R.id.element_seekbar:
			moduleNothing.setVisibility(View.VISIBLE);
		default:

			break;
		}
		// moduleZorder.setVisibility(View.VISIBLE);

		layoutView.invalidate();
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

	private void resetModules()
	{
		moduleAlign.setVisibility(View.GONE);
		moduleEditText.setVisibility(View.GONE);
		moduleItemCount.setVisibility(View.GONE);
		modulePicture.setVisibility(View.GONE);
		moduleChangeSize.setVisibility(View.GONE);
		moduleZorder.setVisibility(View.GONE);
		moduleNothing.setVisibility(View.GONE);
		moduleIcons.setVisibility(View.GONE);
		moduleStarCount.setVisibility(View.GONE);

		moduleAlign.invalidate();
		moduleEditText.invalidate();
		moduleItemCount.invalidate();
		modulePicture.invalidate();
		moduleChangeSize.invalidate();
		moduleZorder.invalidate();
		moduleNothing.invalidate();
		moduleIcons.invalidate();
		moduleStarCount.invalidate();
	}

	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class IconModuleListener implements OnItemClickListener
	{
		ArrayList<Integer> highresRes;

		public IconModuleListener()
		{
			highresRes = iconHelper.getFullRes();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3)
		{
			// TODO Auto-generated method stub
			Log.d("pos", String.valueOf(pos));

			int resourceId = (highresRes.get(pos)).intValue();

			Log.d("pos", String.valueOf(resourceId));

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
			switch (v.getId()) {
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
			((RatingBar) ((ViewGroup) currentView).getChildAt(0)).setNumStars(progress);

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

		@Override
		public void onClick(View v)
		{
			if (v != active) {
				if (active != null) {
					active.setActivated(false);
				}

				active = v;
				active.setActivated(true);
			}

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
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}

}
