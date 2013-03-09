package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uibuilder.EditmodeFragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

public class ImageTools
{
	private Context c;
	
	private Uri path;
	private String photoPath;
	private static final String JPEG_FILE_PREFIX = "UI_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private BaseAlbumDirFactory storageFactory = null;
	
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;
	public static final int CROP = 3;
	
	public ImageTools(Context c)
	{
		this.c = c;
		storageFactory = new BaseAlbumDirFactory();
	}
	
	public Intent getIntent(int which)
	{
		switch (which)
		{
		case CAMERA:
			
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
			}
			
			return cameraIntent;
			
		case GALLERY:
			
			Intent galleryIntent = new Intent();
			galleryIntent.setType("image/*");
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			
			return galleryIntent;
			
		default:
			break;
		}
		return null;
		
		

	}
	
	public void handleBigCameraPhoto(View destination)
	{

		if (photoPath != null)
		{
			setPic(destination);
			galleryAddPic();
			photoPath = null;
		}

	}

	public void handleGalleryImport(View destination, Intent data)
	{
		path = data.getData();
		photoPath = getPath(path);
		
		if (photoPath != null)
		{
			setPic(destination);
		}
	}
	
	public void requestBitmap(View root, ContentResolver cres)
	{
		RelativeLayout content = (RelativeLayout) root.findViewById(R.id.design_area);

		Bitmap image = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
		content.draw(new Canvas(image));
		
		File f;
		try
		{
			f = setUpPhotoFile();
			photoPath = f.getAbsolutePath();
			
			FileOutputStream out = new FileOutputStream(f);

			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
			f = null;
			photoPath = null;
		}
	
		galleryAddPic();
		photoPath = null;
	}

	/*
	 * 
	 * private void handleSmallCameraPhoto(Intent intent) { Bundle extras =
	 * intent.getExtras(); mImageBitmap = (Bitmap) extras.get("data");
	 * mImageView.setImageBitmap(mImageBitmap); //mVideoUri = null;
	 * mImageView.setVisibility(View.VISIBLE);
	 * mVideoView.setVisibility(View.INVISIBLE); }
	 */
	


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

	private void setPic(View v)
	{

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = ((ImageView) v).getWidth();
		int targetH = ((ImageView) v).getHeight();

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
		((ImageView) v).setImageBitmap(bitmap);
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

	private String getPath(Uri uri)
	{
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = c.getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
	
	private void galleryAddPic()
	{
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		c.getApplicationContext().sendBroadcast(mediaScanIntent);
	}


	private String getAlbumName()
	{
		return c.getString(R.string.album_name);
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
			Log.v(c.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
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
