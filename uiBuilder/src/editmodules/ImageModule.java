/**
 * 
 */
package editmodules;

import helpers.ImageTools;
import helpers.OnGoingInBackground;
import uibuilder.EditmodeFragment;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to set an image as the background resource of an imageView.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author funklos
 *
 */
public class ImageModule extends Module
{
	private LinearLayout box;
	
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
		
		switch (requestCode)
		{
		case ImageTools.CAMERA:

			imageHandler.handleCameraPhoto(container);
			break;

		case ImageTools.GALLERY:

			imageHandler.handleGalleryImport(container, data);
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
		box.setOnClickListener(new ExpansionListener(box, super.context));
		
		takePic.setOnClickListener(new ImageModuleListener());
		picFromGallery.setOnClickListener(new ImageModuleListener());
	}

	/* (non-Javadoc)
	 * @see editmodules.Module#getInstance(android.view.View)
	 */
	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
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
			//listener.prepareForImport();
			listener.prepareForBackground();
			
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
	
	/**
	 * @see OnGoingInBackground
	 * @author funklos
	 *
	 */
	public interface onImageImportListener extends OnGoingInBackground
	{
		
	}

	private static onImageImportListener listener;

	public static void setOnImageImportListener(
			onImageImportListener listener)
	{
		ImageModule.listener = listener;
	}
}
