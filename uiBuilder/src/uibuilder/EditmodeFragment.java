package uibuilder;

import helpers.BaseAlbumDirFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment
{
	private Uri path;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK
				&& requestCode == ImageModuleListener.CAMERA)
		{
			path = data.getData();

			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

			((ImageView) currentView).setImageBitmap(thumbnail);

		}

		if (resultCode == Activity.RESULT_OK
				&& requestCode == ImageModuleListener.GALLERY)
		{

		}

		super.onActivityResult(requestCode, resultCode, data);

		/*
		 * case ACTION_TAKE_PHOTO_B: { if (resultCode == RESULT_OK) {
		 * handleBigCameraPhoto(); } break; } // ACTION_TAKE_PHOTO_B
		 * 
		 * case ACTION_TAKE_PHOTO_S: { if (resultCode == RESULT_OK) {
		 * handleSmallCameraPhoto(data); } break; } // ACTION_TAKE_PHOTO_S
		 */}

	/*
	 * private void handleSmallCameraPhoto(Intent intent) { Bundle extras =
	 * intent.getExtras(); mImageBitmap = (Bitmap) extras.get("data");
	 * mImageView.setImageBitmap(mImageBitmap); mVideoUri = null;
	 * mImageView.setVisibility(View.VISIBLE);
	 * mVideoView.setVisibility(View.INVISIBLE); }
	 * 
	 * private void handleBigCameraPhoto() {
	 * 
	 * if (mCurrentPhotoPath != null) { setPic(); galleryAddPic();
	 * mCurrentPhotoPath = null; }
	 * 
	 * }
	 */

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

	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	private View currentView;
	private EditText editText, editSize;

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
		setupAlignModule();
		setupEdittextModule();
		setupChangesizeModule();

		// and so on..
	}

	private void setupChangesizeModule()
	{
		Button larger = (Button) layoutView.findViewById(R.id.item_edit_editsize_bigger);
		Button smaller = (Button) layoutView.findViewById(R.id.item_edit_editsize_smaller);
		editSize = (EditText) layoutView.findViewById(R.id.item_edit_editsize_field);
		larger.setOnClickListener(new ChangesizeModuleListener());
		smaller.setOnClickListener(new ChangesizeModuleListener());

	}

	private void setupEdittextModule()
	{
		Button submit = (Button) layoutView.findViewById(R.id.item_edit_edittext_submitbutton);

		editText = (EditText) layoutView.findViewById(R.id.item_edit_edittext);

		submit.setOnClickListener(new EditTextModuleListener());

	}

	private void setupAlignModule()
	{

		Button alignLeft = (Button) layoutView.findViewById(R.id.item_edit_align_left_button);
		Button alignRight = (Button) layoutView.findViewById(R.id.item_edit_align_right_button);
		Button alignCenter = (Button) layoutView.findViewById(R.id.item_edit_align_center_button);
		alignLeft.setOnClickListener(new AlignModuleListener());
		alignRight.setOnClickListener(new AlignModuleListener());
		alignCenter.setOnClickListener(new AlignModuleListener());
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

		int tag = (Integer.valueOf(view.getTag().toString()));

		resetModules();

		currentView = view;
		switch (tag)
		{
		case R.id.element_button:

			moduleEditText.setVisibility(View.VISIBLE);
			moduleAlign.setVisibility(View.VISIBLE);
			moduleChangeSize.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));
			editSize.setText(String.valueOf((int)((TextView)currentView).getTextSize()));


			break;

		case R.id.element_checkbox:

			// moduleItemCount.setVisibility(View.VISIBLE);
			moduleEditText.setVisibility(View.VISIBLE);

			editText.setText(getViewText(currentView));

			break;

		case R.id.element_edittext:

			moduleEditText.setVisibility(View.VISIBLE);
			moduleAlign.setVisibility(View.VISIBLE);
			editText.setText(((TextView) currentView).getText());
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
			moduleEditText.setVisibility(View.VISIBLE);
			moduleAlign.setVisibility(View.VISIBLE);
			editText.setText(getViewText(currentView));
			editSize.setText(String.valueOf((int)((TextView)currentView).getTextSize()));

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

				// startActivityForResult(cameraIntent, CAMERA);

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

			/*
			 * } catch (ActivityNotFoundException e) =======
			 * 
			 * startActivityForResult(cameraIntent, CAMERA); } catch
			 * (ActivityNotFoundException e) >>>>>>> branch 'master' of
			 * https://github.com/UiBuilder/TestBuilder.git { String
			 * errorMessage =
			 * "Whoops - your device doesn't support capturing images!"; Toast
			 * toast = Toast.makeText(getActivity().getApplicationContext(),
			 * errorMessage, Toast.LENGTH_SHORT); toast.show(); } <<<<<<< HEAD
			 * break;
			 */

			case R.id.image_choose_gallery:
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
				break;

			}
		}
	}

	private class EditTextModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.item_edit_edittext_submitbutton:
				setViewText((editText.getText().toString()));
				break;
			}
		}
	}

	private class AlignModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.item_edit_align_left_button:
				((TextView) currentView).setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);
				break;
			case R.id.item_edit_align_center_button:
				((TextView) currentView).setGravity(Gravity.CENTER);
				break;
			case R.id.item_edit_align_right_button:
				((TextView) currentView).setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
				break;

			}

		}
	}


	private class ChangesizeModuleListener implements OnClickListener
	{
		
		@Override
		public void onClick(View v)
		{
			
			switch (v.getId())
			{
			case R.id.item_edit_editsize_bigger:
				changeSize((TextView) currentView, 1);
				break;
			case R.id.item_edit_editsize_smaller:
				changeSize((TextView) currentView, -1);
				break;

			}

			//editListener.refreshOverlay(currentView);
			


		}
		
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
	}

	
	public interface onObjectEditedListener
	{
		void refreshOverlay(View active);
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(
			onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}
}
