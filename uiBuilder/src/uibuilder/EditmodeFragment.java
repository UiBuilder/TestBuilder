package uibuilder;

import helpers.BaseAlbumDirFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import creators.Generator;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment
{
	private Uri path;

	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	private View currentView;
	private EditText editText, editSize;
	private NumberPicker picker;

	private LinearLayout moduleAlign, modulePicture, moduleEditText,
			moduleItemCount, moduleChangeSize;

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
		storageFactory = new BaseAlbumDirFactory();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");

		this.inflater = inflater;
		if (layoutView == null)
		{
			layoutView = inflater.inflate(R.layout.layout_editmode_fragment, container, false);

			getModules();

			return layoutView;
		}

		return layoutView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{

		case ImageModuleListener.CAMERA:
		{
			if (resultCode == Activity.RESULT_OK)
			{
				handleBigCameraPhoto();
			}
			break;
		}

		case ImageModuleListener.GALLERY:
		{
			if (resultCode == Activity.RESULT_OK)
			{
				path = data.getData();
				photoPath = getPath(path);
				handleGalleryImport();
			}

		}
		}
	}

	/**
	 * Approach from stackoverflow.com, but slightly modified to use the
	 * contentResolver instead of the deprecated managedQuery.
	 * 
	 * @author Zelimir from "stackoverflow" src
	 *         http://stackoverflow.com/questions
	 *         /4859011/not-able-to-pick-photo-from-gallery?rq=1
	 * @param uri
	 * @return
	 */

	public String getPath(Uri uri)
	{
		String[] projection =
		{ MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	/*
	 * not in use private static Bitmap Image = null; private static Bitmap
	 * rotateImage = null; int rotation; public int getOrientation(Context
	 * context, Uri photoUri) { Cursor cursor =
	 * getActivity().getApplicationContext
	 * ().getContentResolver().query(photoUri, new String[] {
	 * MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);
	 * 
	 * if (cursor.getCount() != 1) { return -1; } cursor.moveToFirst(); return
	 * cursor.getInt(0); }
	 */

	private void getModules()
	{
		moduleAlign = (LinearLayout) layoutView.findViewById(R.id.editmode_included_align_content);
		moduleEditText = (LinearLayout) layoutView.findViewById(R.id.editmode_included_text);
		modulePicture = (LinearLayout) layoutView.findViewById(R.id.editmode_included_choose_picture);
		moduleItemCount = (LinearLayout) layoutView.findViewById(R.id.editmode_included_item_count);
		moduleChangeSize = (LinearLayout) layoutView.findViewById(R.id.editmode_included_changesize);

		moduleAlign.setVisibility(View.VISIBLE);
		moduleEditText.setVisibility(View.VISIBLE);
		moduleItemCount.setVisibility(View.VISIBLE);
		modulePicture.setVisibility(View.VISIBLE);
		moduleChangeSize.setVisibility(View.VISIBLE);

		setupPictureModule();
		setupEdittextModule();
		setupChangesizeModule();
		setupAlignModule();

		// and so on..
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
		switch (id)
		{
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

			modulePicture.setVisibility(View.VISIBLE);
			break;

		case R.id.element_numberpick:
			// moduleNothingToEdit.setVisibility(View.VISIBLE);

			break;
		case R.id.element_radiogroup:
			moduleEditText.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));

			// moduleItemCount.setVisibility(View.VISIBLE);
			break;
		case R.id.element_ratingbar:
			break;
		case R.id.element_search:
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
			break;

		default:
			break;
		}
		layoutView.invalidate();
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
		if (currentView instanceof LinearLayout)
		{
			TextView textView = (TextView) ((LinearLayout) currentView).getChildAt(0);

			textView.setText(string);

		} else if (currentView instanceof EditText)
		{
			((EditText) currentView).setHint(string);

		} else
		{
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

		moduleAlign.invalidate();
		moduleEditText.invalidate();
		moduleItemCount.invalidate();
		modulePicture.invalidate();
		moduleChangeSize.invalidate();
	}

	private class ImageModuleListener implements OnClickListener
	{
		static final int CAMERA = 1;
		static final int GALLERY = 2;
		static final int CROP = 3;

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.image_choose_camera:

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

				File f = null;

				try
				{
					f = setUpPhotoFile();
					photoPath = f.getAbsolutePath();
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				} catch (IOException e)
				{
					e.printStackTrace();
					f = null;
					photoPath = null;
				} finally
				{
					startActivityForResult(cameraIntent, CAMERA);
				}

				break;

			case R.id.image_choose_gallery:
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
				break;
			}
		}
	}

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

	private class AlignModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{

			switch (v.getId())
			{
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
				((TextView) currentView).setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_center_center:
				((TextView) currentView).setGravity(Gravity.CENTER);

				break;
			case R.id.editmode_align_center_right:
				((TextView) currentView).setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);

				break;
			case R.id.editmode_align_bottom_left:
				((TextView) currentView).setGravity(Gravity.LEFT
						| Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_center:
				((TextView) currentView).setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.BOTTOM);

				break;
			case R.id.editmode_align_bottom_right:
				((TextView) currentView).setGravity(Gravity.RIGHT
						| Gravity.BOTTOM);

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
		void refreshOverlay(View active);
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}

	// Photo import stuff

	private void handleBigCameraPhoto()
	{

		if (photoPath != null)
		{
			setPic();
			galleryAddPic();
			photoPath = null;
		}

	}

	private void handleGalleryImport()
	{
		if (photoPath != null)
		{
			setPic();
		}
	}

	/*
	 * 
	 * private void handleSmallCameraPhoto(Intent intent) { Bundle extras =
	 * intent.getExtras(); mImageBitmap = (Bitmap) extras.get("data");
	 * mImageView.setImageBitmap(mImageBitmap); //mVideoUri = null;
	 * mImageView.setVisibility(View.VISIBLE);
	 * mVideoView.setVisibility(View.INVISIBLE); }
	 */

	private void setPic()
	{

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = ((ImageView) currentView).getWidth();
		int targetH = ((ImageView) currentView).getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0))
		{
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		((ImageView) currentView).setImageBitmap(bitmap);
	}

	private void galleryAddPic()
	{
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	private String photoPath;
	private static final String JPEG_FILE_PREFIX = "UI_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private BaseAlbumDirFactory storageFactory = null;

	private String getAlbumName()
	{
		return getString(R.string.album_name);
	}

	private File getAlbumDir()
	{
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{

			storageDir = storageFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null)
			{
				if (!storageDir.mkdirs())
				{
					if (!storageDir.exists())
					{
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else
		{
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private File setUpPhotoFile() throws IOException
	{

		File f = createImageFile();
		photoPath = f.getAbsolutePath();

		return f;
	}

	private File createImageFile() throws IOException
	{
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

}
