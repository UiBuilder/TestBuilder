package uibuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment implements OnClickListener
{
	private Uri path;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK && requestCode == ImageModuleListener.CAMERA)
		{
			path = data.getData();
			String path2 = path.getPath();
 			
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

	        
	       ((ImageView) currentView).setImageBitmap(thumbnail);
		}
		
		if (resultCode == Activity.RESULT_OK && requestCode == ImageModuleListener.GALLERY)
		{
			
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
//IN PROGRESS
	/* Photo album for this application */
/*	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
	*/


	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	private Button submit, set;
	private EditText editText;
	private View currentView;

	private LinearLayout moduleAlign, modulePicture, moduleEditText,
			moduleItemCount;

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

		moduleAlign.setVisibility(View.VISIBLE);
		moduleEditText.setVisibility(View.VISIBLE);
		moduleItemCount.setVisibility(View.VISIBLE);
		modulePicture.setVisibility(View.VISIBLE);
		
		setupPictureModule();
		setupAlignModule();
		setupEdittextModule();
		//and so on..
	}

	private void setupEdittextModule()
	{
		// TODO Auto-generated method stub
		
	}

	private void setupAlignModule()
	{
		// TODO Auto-generated method stub
		
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
			break;

		case R.id.element_checkbox:

			//moduleItemCount.setVisibility(View.VISIBLE);
			moduleEditText.setVisibility(View.VISIBLE);
			break;

		case R.id.element_edittext:
			
			moduleEditText.setVisibility(View.VISIBLE);
			moduleAlign.setVisibility(View.VISIBLE);
			//moduleTextSize.setVisibility(View.VISIBLE);
			break;
			
		case R.id.element_imageview:
			
			modulePicture.setVisibility(View.VISIBLE);
			break;
			
		case R.id.element_numberpick:
			//moduleNothingToEdit.setVisibility(View.VISIBLE);
			
			break;
		case R.id.element_radiogroup:
			moduleEditText.setVisibility(View.VISIBLE);
			//moduleItemCount.setVisibility(View.VISIBLE);
			break;
		case R.id.element_ratingbar:
			break;
		case R.id.element_search:
			//moduleSearch.setVisibility(View.VISIBLE); collapsed etc
			break;
		case R.id.element_switch:
			moduleEditText.setVisibility(View.VISIBLE);
			break;
		case R.id.element_textview:
			moduleEditText.setVisibility(View.VISIBLE);
			moduleAlign.setVisibility(View.VISIBLE);
			//moduleTextSize.setVisibility(View.VISIBLE);
			break;
		case R.id.element_timepicker:
			break;

		default:
			break;
		}
		layoutView.invalidate();
	}

	private void resetModules()
	{
		moduleAlign.setVisibility(View.GONE);
		moduleEditText.setVisibility(View.GONE);
		moduleItemCount.setVisibility(View.GONE);
		modulePicture.setVisibility(View.GONE);

		moduleAlign.invalidate();
		moduleEditText.invalidate();
		moduleItemCount.invalidate();
		modulePicture.invalidate();
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.item_edit_edittext_submitbutton:
			((TextView) currentView).setText(editText.getText().toString());

		}

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
				try
				{
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					
			        startActivityForResult(cameraIntent, CAMERA);
				}
				catch (ActivityNotFoundException e) 
				{
					String errorMessage = "Whoops - your device doesn't support capturing images!";
				    Toast toast = Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
				    toast.show();
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
	
	private class EditTextModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.item_edit_edittext_submitbutton:
				((TextView) currentView).setText(editText.getText().toString());
				break;
			}		
		}	
	}
	
	private class AlignModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			
		}
		
	}
}
