package helpers;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Class to load image resources off of the ui thread.
 * Uses the approach from @see http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 * to load and set them async.
 * uses weak references which can be easily freed when the imageView which requested the resource is no longer displayed on the screen.
 * @author funklos
 *
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> 
{
    @Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	private final WeakReference<ImageView> imageViewReference;
    private String data = null;
    
    private int measuredWidth, measuredHeight;

    public BitmapWorkerTask(ImageView imageView) 
    {
        // Use a WeakReference to ensure the ImageView can be garbage collected
    	measuredHeight = imageView.getMeasuredHeight();
    	measuredWidth = imageView.getMeasuredWidth();
    	Log.d("image size", String.valueOf(measuredHeight));
    	Log.d("image width", String.valueOf(measuredWidth));
        imageViewReference = new WeakReference<ImageView>(imageView);
 
        Log.d("image worker", "instantiated");
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) 
    {
        data = params[0];
        
        return decodeSampledBitmap(data);
    }

    private Bitmap decodeSampledBitmap(String photoPath)
	{
    	if (photoPath != null)
		{
    		Log.d("image worker", " path not null ");
			/* Get the size of the image */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(photoPath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;
	
			/* Figure out which way needs to be reduced less */
			int scaleFactor = 1;
			if ((measuredWidth > 0) && (measuredHeight > 0))
			{
				scaleFactor = Math.min(photoW / measuredWidth, photoH / measuredHeight);
			}
			
			Log.d("scalefactor of image", String.valueOf(scaleFactor));
			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inDither = true;
			bmOptions.inPreferQualityOverSpeed = true;
			bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bmOptions.inPurgeable = true;
	
			/* Decode the JPEG file into a Bitmap */
			return BitmapFactory.decodeFile(photoPath, bmOptions);
		}
		return null;
	}

	// Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) 
    {
        if (imageViewReference != null && bitmap != null) 
        {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) 
            {
                imageView.setImageBitmap(bitmap);
                Log.d("image worker", "image set");
                imageView.postInvalidate();
            }
        }
    }
}
