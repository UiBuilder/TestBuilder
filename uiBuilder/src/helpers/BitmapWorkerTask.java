package helpers;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> 
{
    private final WeakReference<ImageView> imageViewReference;
    private String data = null;

    public BitmapWorkerTask(ImageView imageView) 
    {
        // Use a WeakReference to ensure the ImageView can be garbage collected
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
    	//try
    	{
	    	if (photoPath != null && imageViewReference.get()!= null)
			{
	    		ImageView v = imageViewReference.get();
	    		
				int targetW = ((ImageView) v).getMeasuredWidth();
				int targetH = ((ImageView) v).getMeasuredHeight();
		
				/* Get the size of the image */
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(photoPath, bmOptions);
				int photoW = bmOptions.outWidth;
				int photoH = bmOptions.outHeight;
		
				/* Figure out which way needs to be reduced less */
				int scaleFactor = 8;
				if ((targetW > 0) && (targetH > 0))
				{
					Log.d("imagecontainer w", String.valueOf(photoW));
					Log.d("imagecontainer w", String.valueOf(targetW));
					scaleFactor = Math.min(photoW / targetW, photoH / targetH);
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
    	//catch (Exception e) {
			// TODO: handle exception
		//}
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
            }
        }
    }
}
