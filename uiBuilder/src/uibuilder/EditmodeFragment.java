package uibuilder;

import helpers.ImageTools;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;
import editmodules.AlignModule;
import editmodules.BackgroundColorModule;
import editmodules.ContentModule;
import editmodules.FontSizeModule;
import editmodules.GridColumnModule;
import editmodules.GridLayoutModule;
import editmodules.IconModule;
import editmodules.ImageModule;
import editmodules.ListLayoutModule;
import editmodules.Module;
import editmodules.StarCountModule;
import editmodules.UserTextModule;
import editmodules.ZOrderModule;

public class EditmodeFragment extends Fragment
{

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private View root;
	private LinearLayout linearRoot;
	private View currentView;

	private ImageTools imageHandler;

	private Module 
			alignModule,
			userTextModule,
			backgroundColorModule,
			contentModule,
			fontSizeModule,
			gridColumnModule,
			gridLayoutModule,
			iconModule,
			imageModule,
			listLayoutModule,
			starCountModule,
			zOrderModule;
	
	private Context context;

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

		context = getActivity();
		
		BroadcastReceiver receiver = new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context context, Intent intent)
			{
				
				int code = intent.getIntExtra(ImageModule.IMAGEREQUEST, 0);
				switch (code)
				{
				case ImageTools.CAMERA:

					Intent cameraIntent = imageHandler.getIntent(ImageTools.CAMERA);
					startActivityForResult(cameraIntent, ImageTools.CAMERA);

					break;

				case ImageTools.GALLERY:

					Intent galleryIntent = imageHandler.getIntent(ImageTools.GALLERY);
					startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), ImageTools.GALLERY);
					break;
				}
			}
		};
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		getModules();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("Editmode Fragment", "onCreateView called");

		if (root == null)
		{
			root = inflater.inflate(R.layout.layout_editmode_fragment, container, false);
			linearRoot = (LinearLayout) root;
			
			return root;
		}
		return root;
	}

	/**
	 * @author funklos
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
		case ImageTools.CAMERA:

			if (resultCode == Activity.RESULT_OK)
			{
				imageHandler.handleBigCameraPhoto(currentView);
			}
			break;

		case ImageTools.GALLERY:

			if (resultCode == Activity.RESULT_OK)
			{
				imageHandler.handleGalleryImport(currentView, data);
			}
		}
	}

	private void getModules()
	{
		alignModule = new AlignModule(context);
		backgroundColorModule = new BackgroundColorModule(context);
		contentModule = new ContentModule(context);
		fontSizeModule = new FontSizeModule(context);
		gridColumnModule = new GridColumnModule(context);
		gridLayoutModule = new GridLayoutModule(context);
		iconModule = new IconModule(context);
		imageModule = new ImageModule(context);
		listLayoutModule = new ListLayoutModule(context);
		starCountModule = new StarCountModule(context);
		userTextModule = new UserTextModule(context);
		zOrderModule = new ZOrderModule(context);
	}


	protected void adaptLayoutToContext(View view)
	{
		currentView = view;

		Bundle tagBundle = (Bundle) currentView.getTag();
		int id = tagBundle.getInt(ObjectValues.TYPE);

		linearRoot.removeAllViews();
		
		configDefault();

		switch (id)
		{
		case R.id.element_button:
			
			configButton();
			break;

		case R.id.element_checkbox:

			configCheckBox();
			break;

		case R.id.element_edittext:

			configEditText();
			break;

		case R.id.element_imageview:

			configImageView();
			break;

		case R.id.element_radiogroup:

			configRadioGroup();
			break;
		case R.id.element_ratingbar:
			configRatingBar();
			break;

		case R.id.element_switch:
			
			configSwitch();
			break;
		case R.id.element_textview:

			configTextView();
			break;

		case R.id.element_list:
			
			configListView();
			break;

		case R.id.element_grid:

			configGrid();
			break;

		default:
			break;
		}

		root.invalidate();
	}

	private void configGrid()
	{
		linearRoot.addView(contentModule.getInstance(currentView));
		linearRoot.addView(gridColumnModule.getInstance(currentView));
		linearRoot.addView(gridLayoutModule.getInstance(currentView));
	}

	private void configListView()
	{
		linearRoot.addView(contentModule.getInstance(currentView));
		linearRoot.addView(listLayoutModule.getInstance(currentView));
	}

	private void configTextView()
	{
		configButton();
	}

	private void configSwitch()
	{
		configCheckBox();	
	}

	private void configRatingBar()
	{
		linearRoot.addView(starCountModule.getInstance(currentView));
	}

	private void configRadioGroup()
	{
		configCheckBox();	
	}

	private void configImageView()
	{
		linearRoot.addView(iconModule.getInstance(currentView));
		linearRoot.addView(imageModule.getInstance(currentView));
	}

	private void configEditText()
	{
		configButton();	
	}

	/**
	 * 
	 */
	private void configDefault()
	{
		linearRoot.addView(zOrderModule.getInstance(currentView));
	}

	private void configCheckBox()
	{
		//linearRoot.addView(userTextModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}

	private void configButton()
	{
		//linearRoot.addView(userTextModule.getInstance(currentView));
		linearRoot.addView(fontSizeModule.getInstance(currentView));
		linearRoot.addView(alignModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}



	public interface onObjectEditedListener
	{
		void setSampleContent(View active, int id);

		void refreshAdapter(View active, int id);

		void gridColumnsChanged(View active, int col);

		void setIconResource(View active, int pos);
	}

	private static onObjectEditedListener editListener;

	public static void setOnObjectEditedListener(onObjectEditedListener listener)
	{
		EditmodeFragment.editListener = listener;
	}

}
