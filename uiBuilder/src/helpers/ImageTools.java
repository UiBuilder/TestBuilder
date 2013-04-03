package helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import data.AlbumDirFactory;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

/**
 * taken from @autor android developers photobyintent example, class extracted from activity
 * and modified to include gallery intent to save images and mail intent to share via email
 * @author funklos
 *
 */
public class ImageTools
{
	private Context c;
	
	private Uri path;
	private int screenId;
	private String photoPath;
	private static final String JPEG_FILE_PREFIX = "UI_";
	private static final String JPEG_FILE_SUFFIX = ".png";
	private AlbumDirFactory storageFactory = null;
	
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;
	public static final int CROP = 3; //NOT IN USE BECAUSE THIS FUNCTION IS DEVICE SPECIFIC
	public static final int SHARE = 4;

	
	public ImageTools(Context c)
	{
		this.c = c;
		storageFactory = new AlbumDirFactory();
	}
	
	/**
	 * Used as a factory method to return new intents associated with media imports.
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param which
	 * @return
	 */
	public Intent getIntent(int which)
	{
		switch (which)
		{
		case CAMERA:
			
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			File f = null;

			try
			{
				f = setUpPhotoFile(ALBUM);
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
			
		case SHARE:
			
			Intent mailIntent = new Intent(Intent.ACTION_SEND);
			mailIntent.putExtra(Intent.EXTRA_SUBJECT, c.getString(R.string.intent_share_extra_subject));
			mailIntent.putExtra(Intent.EXTRA_TEXT, c.getString(R.string.intent_share_extra_text));
			mailIntent.setType("image/png");
			
			return mailIntent;
		default:
			break;
		}
		return null;
		
		

	}
	/**
	 * Called when the user has taken a picture to insert into his project.
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param destination
	 */
	public void handleCameraPhoto(View destination)
	{
		if (photoPath != null)
		{
			Log.d("path", "not null");
			setPic(destination);
			Log.d("set pic", "passed");
			galleryAddPic();
			photoPath = null;
		}

	}

	/**
	 * Fetch the path of the image chosen from the gallery and set the related picture 
	 * as a background resource of the destination view.
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param destination
	 * @param data
	 */
	public void handleGalleryImport(View destination, Intent data)
	{
		Log.d("handle", "called");
		path = data.getData();
		photoPath = getPath(path);
		
		if (photoPath != null)
		{
			setPic(destination);
		}
	}
	/**
	 * Generates a screenshot of the content of the designArea, to be used as a preview image in the manager
	 * grid or to be passed on to an share intent.
	 * @author funklos
	 * @param root
	 * @param cres
	 * @return the uri pointing to the image file
	 */
	public Uri requestBitmap(View root, ContentResolver cres, boolean insert, boolean intern, int screenId)
	{
		RelativeLayout content = (RelativeLayout) root.findViewById(R.id.design_area);

		Bitmap image = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
		
		content.draw(new Canvas(image));
		
		File f;
		String photoUri = null;
		
		if (intern == false)
		{
			
			try
			{
				f = setUpPhotoFile(ALBUM);
				photoPath = f.getAbsolutePath();
				
				FileOutputStream out = new FileOutputStream(f);
				image.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				
				//HUNDLING
				photoUri = MediaStore.Images.Media.insertImage(
				         cres, photoPath, null, null);
				
				
			} catch (IOException e)
			{
				e.printStackTrace();
				f = null;
				photoPath = null;
			}
		}
		else
		{			
			try
			{
				this.screenId = screenId;
				
				f = setUpPhotoFile(PREVIEW);
				
				FileOutputStream fos = new FileOutputStream(f);
				fos = new FileOutputStream(f);
				photoPath = f.getAbsolutePath();

				image.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				
				
				Uri screenUri = Uri.parse(photoPath);
				photoPath = null;
				
				return screenUri;
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		if (insert)
			galleryAddPic();
		
		photoPath = null;
		return Uri.parse(photoUri);
	}

	/**
	 * Calls the public static implementation which uses the async data loading approach.
	 * Set the path of the image in the tag bundle of the passed view.
	 * 
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param v the target to receive the picture
	 */
	private void setPic(View v)
	{
		setPic(v, photoPath);
		/* Add the imagepath to the tagBundle */
		
		Bundle tagBundle = (Bundle) v.getTag();
		tagBundle.putString(ObjectValues.IMG_SRC, photoPath);
		tagBundle.putInt(ObjectValues.ICN_SRC, 0);
	}

	/**
	 * use an async worker task to load the image in background and set it to the destination few.
	 * @param v
	 * @param path
	 */
	public static void setPic(View v, String path)
	{
		BitmapWorkerTask task = new BitmapWorkerTask((ImageView) v);

	    task.execute(path);
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
	
	/**
	 * Adds the new picture to the gallery
	 * @autor android developers photobyintent example
	 */
	private void galleryAddPic()
	{
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		c.getApplicationContext().sendBroadcast(mediaScanIntent);
	}

	/**
	 * Fetches the default paths
	 * @autor android developers photobyintent example
	 */
	private String getAlbumName()
	{
		return c.getString(R.string.album_name);
	}
	
	private String getPreviewFolder()
	{
		return c.getString(R.string.preview_directory);
	}

	/**
	 * @autor android developers photobyintent example
	 */
	private static final int ALBUM = 0x00, PREVIEW = 0x01;
	
	
	private File getAlbumDir(int which)
	{
		File storageDir = null;
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			switch (which)
			{
			case ALBUM:
				storageDir = storageFactory.getAlbumStorageDir(getAlbumName());
				break;

			case PREVIEW:
				storageDir = storageFactory.getPreviewStorageDir(getPreviewFolder());
			}	

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
	
	/**
	 * 
	 * @author funklos modified to handle multiple paths
	 * @autor android developers photobyintent example
	 */
	private File setUpPhotoFile(int which) throws IOException
	{

		File f = createImageFile(which);
		photoPath = f.getAbsolutePath();

		return f;
	}

	/**
	 * @author funklos modified to handle multiple paths
	 * @autor android developers photobyintent example
	 */
	private File createImageFile(int which) throws IOException
	{
		String imageFileName = "";
		
		switch (which)
		{
		case ALBUM:
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMAN).format(new Date());
			imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
			break;

		case PREVIEW:
			imageFileName = JPEG_FILE_PREFIX + screenId;

		}
		File albumF = getAlbumDir(which);
		File f = new File(albumF, imageFileName + JPEG_FILE_SUFFIX);

		return f;
	}


}
