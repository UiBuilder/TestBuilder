/**
 * 
 */
package editmodules;

import helpers.ImageTools;
import uibuilder.EditmodeFragment;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

/**
 * @author funklos
 *
 */
public class ImageModule extends Module
{
	private LinearLayout box;
	private View requesting;
	
	private Button takePic, picFromGallery;
	
	private ImageTools imageHandler;
	private EditmodeFragment context;
	
	/**
	 * @param context
	 */
	public ImageModule(EditmodeFragment context)
	{
		super(context);
		this.context = context;
		imageHandler = new ImageTools(super.context);
	}

	/* (non-Javadoc)
	 * @see editmodules.ExpansionListener.onToggleExpansionListener#getValues()
	 */
	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * will be called when the user has successfully chosen a picture from an android content provider,
	 * or taken a picture with the device camera and the result was delivered to EditmodeFragment.
	 * @param requestCode
	 * @param data
	 */
	public void setImageResource(int requestCode, Intent data)
	{
		listener.imageImported();
		
		switch (requestCode)
		{
		case ImageTools.CAMERA:

			imageHandler.handleCameraPhoto(requesting);
			break;

		case ImageTools.GALLERY:

			imageHandler.handleGalleryImport(requesting, data);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see editmodules.Module#setupUi()
	 */
	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_picture, null);
		
		takePic = (Button) box.findViewById(R.id.image_choose_camera);
		picFromGallery = (Button) box.findViewById(R.id.image_choose_gallery);
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#setListeners()
	 */
	@Override
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box));
		
		takePic.setOnClickListener(new ImageModuleListener());
		picFromGallery.setOnClickListener(new ImageModuleListener());
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#getInstance(android.view.View)
	 */
	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		
		adaptToContext();
		return box;
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#adaptToContext()
	 */
	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

	}
	

	/**
	 * deliver intent to framework to pick a picture.
	 * the result will be passed to editmode fragment, which is the context of the intents.
	 * onactivityresult will call
	 * @see ImageModule.setImageResource
	 * @author funklos
	 * 
	 */
	private class ImageModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			listener.prepareForImport();
			
			switch (v.getId())
			{
			case R.id.image_choose_camera:

				Intent cameraIntent = imageHandler.getIntent(ImageTools.CAMERA);
				context.startActivityForResult(cameraIntent, ImageTools.CAMERA);
				break;

			case R.id.image_choose_gallery:
				
				Intent galleryIntent = imageHandler.getIntent(ImageTools.GALLERY);
				context.startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), ImageTools.GALLERY);
				break;
			}
		}
	}
	
	public interface onImageImportListener
	{
		void prepareForImport();
		
		void imageImported();
	}

	private static onImageImportListener listener;

	public static void setOnImageImportListener(
			onImageImportListener listener)
	{
		ImageModule.listener = listener;
	}
}
