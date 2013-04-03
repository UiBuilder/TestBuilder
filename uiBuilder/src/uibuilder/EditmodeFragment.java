package uibuilder;

import android.app.Activity;
import android.app.Fragment;
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
	 * The only result delivered to this fragment is an imagePick intent.
	 * The requestCode is checked and differentiated inside the @see ImageModule
	 * @author funklos
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK)
		{
			((ImageModule) imageModule).setImageResource(requestCode, data);
		}
			
	}

	/**
	 * Instantiate the modules to be displayed in the sidebar.
	 * @see Module
	 */
	private void getModules()
	{
		alignModule = new AlignModule(this);
		backgroundColorModule = new BackgroundColorModule(this);
		contentModule = new ContentModule(this);
		fontSizeModule = new FontSizeModule(this);
		gridColumnModule = new GridColumnModule(this);
		gridLayoutModule = new GridLayoutModule(this);
		iconModule = new IconModule(this);
		imageModule = new ImageModule(this);
		listLayoutModule = new ListLayoutModule(this);
		starCountModule = new StarCountModule(this);
		userTextModule = new UserTextModule(this);
		zOrderModule = new ZOrderModule(this);
	}

	/**
	 * Called from @see UiBuilderActivity when the selected view on the
	 * designArea has changed.
	 * This method checks which element type has focus, resets the state of
	 * the sidebar and loads the
	 * correct sidebar option menus for editing the properties.
	 * The config methods call for an container instance by calling .getInstance on
	 * the module classes
	 * For the general concept @see Module
	 * @param view
	 */
	protected void adaptLayoutToContext(View view)
	{
		currentView = view;

		Bundle tagBundle = (Bundle) currentView.getTag();
		int id = tagBundle.getInt(ObjectValues.TYPE);

		resetState();
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
			
		case R.id.element_seekbar:
			
			configSeekBar();
			break;

		default:
			break;
		}

		root.invalidate();
	}

	/**
	 * Clear the sidebar
	 */
	private void resetState()
	{
		linearRoot.removeAllViews();
	}

	/**
	 * selected type is SeekBar
	 */
	private void configSeekBar()
	{
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}

	/**
	 * selected type is gridView
	 */
	private void configGrid()
	{
		linearRoot.addView(contentModule.getInstance(currentView));
		linearRoot.addView(gridColumnModule.getInstance(currentView));
		linearRoot.addView(gridLayoutModule.getInstance(currentView));
	}

	/**
	 * selected type is listView
	 */
	private void configListView()
	{
		linearRoot.addView(contentModule.getInstance(currentView));
		linearRoot.addView(listLayoutModule.getInstance(currentView));
	}

	/**
	 * selected type is TextView
	 */
	private void configTextView()
	{
		configButton();
	}

	/**
	 * selected type is Switch
	 */
	private void configSwitch()
	{
		configCheckBox();	
	}

	private void configRatingBar()
	{
		linearRoot.addView(starCountModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}

	/**
	 * selected type is RadioButton
	 */
	private void configRadioGroup()
	{
		configCheckBox();	
	}

	/**
	 * selected type is ImageView
	 */
	private void configImageView()
	{
		linearRoot.addView(imageModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
		linearRoot.addView(iconModule.getInstance(currentView));
	}

	/**
	 * selected type is EditText
	 */
	private void configEditText()
	{
		configButton();	
	}

	/**
	 * Load a default configuration for all objects: just z-order is in common for all
	 */
	private void configDefault()
	{
		linearRoot.addView(zOrderModule.getInstance(currentView));
	}
	
	/**
	 * selected type is CheckBox
	 */
	private void configCheckBox()
	{
		linearRoot.addView(userTextModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}

	/**
	 * selected type is Button
	 */
	private void configButton()
	{
		linearRoot.addView(userTextModule.getInstance(currentView));
		linearRoot.addView(fontSizeModule.getInstance(currentView));
		linearRoot.addView(alignModule.getInstance(currentView));
		linearRoot.addView(backgroundColorModule.getInstance(currentView));
	}

}
