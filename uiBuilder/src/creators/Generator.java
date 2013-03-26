package creators;

import helpers.Log;
import helpers.ObjectValueCollector;
import manipulators.Overlay;
import uibuilder.DesignFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import data.DataBase;
import de.ur.rk.uibuilder.R;

/**
 * 
 * @author funklos
 *
 */
public class Generator
{

	public static final String OBJECT_TAG = "isObject";

	private int idCount;
	/** Variable zur dynamischen Vergabe laufender IDs */

	private Context context;
	private OnTouchListener manipulator;
	private LayoutInflater inflater;
	private ObjectFactory factory;
	
	public static final String MINWIDTH = "mWidth", MINHEIGHT = "mHeight", TYPE = "type", ID = "id", SCALETYPE = "type", CREATION_STYLE = "create", PRESENTATION_STYLE = "pres", EXAMPLE_CONTENT = "exampleContent", EXAMPLE_LAYOUT = "exampleLayout", IMG_SRC = "imageSource", ICN_SRC = "iconSource";


	
	
	private Resources res;
	private int gridFactor;

	/**
	 * Konstruktor
	 */
	public Generator(Context ref, OnTouchListener mp, ObjectFactory fucktory)
	{
		idCount = 1;
		context = ref;
		manipulator = mp;
		factory = fucktory;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		gridFactor = DesignFragment.SNAP_GRID_INTERVAL;
		res = ref.getApplicationContext().getResources();
	}

	/**
	 * Use this method to create a View by passing an identifier of the type of view to be created
	 * 
	 * @param id specifies the View to be created.
	 * @return the created View
	 */
	protected View generate(int id)
	{
		View xmlView;
		RelativeLayout.LayoutParams params = null;
		Bundle properties = getBundle(id);
		params = new RelativeLayout.LayoutParams((int) (properties.getInt(MINWIDTH)* 2), (int) (properties.getInt(MINHEIGHT) * 2));

		switch (id)
		{
		case R.id.element_button:
			xmlView = buildButton();
			
			break;

		case R.id.element_textview:
			xmlView = buildTextview();
			
			break;

		case R.id.element_imageview:
			xmlView = buildImageView();
			
			break;

		case R.id.element_edittext:
			xmlView = buildEditText();
			
			break;

		case R.id.element_radiogroup:
			xmlView = buildRadioButtons();
			
			break;

		case R.id.element_switch:
			xmlView = buildSwitch();
			params = new RelativeLayout.LayoutParams((properties.getInt(MINWIDTH)), (int) (properties.getInt(MINHEIGHT) * 2));

			
			break;

		case R.id.element_checkbox:
			xmlView = buildCheckBox();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(MINWIDTH)* 2), (int) (properties.getInt(MINHEIGHT)));

			
			break;

		case R.id.element_list:
			xmlView = buildListView();
			
			break;

		case R.id.element_numberpick:
			xmlView = buildNumberPicker();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(MINWIDTH)), (int) (properties.getInt(MINHEIGHT)));

			
			break;

		case R.id.element_ratingbar:
			xmlView = buildRatingBar();
			params = new RelativeLayout.LayoutParams(properties.getInt(MINWIDTH) * 6, properties.getInt(MINHEIGHT));

			
			break;

		case R.id.element_seekbar:
			xmlView = buildSeekBar();
			params = new RelativeLayout.LayoutParams(properties.getInt(MINWIDTH), properties.getInt(MINHEIGHT) * 2);

			
			break;

		case R.id.element_timepicker:
			xmlView = buildTimePicker();
			params = new RelativeLayout.LayoutParams((int) (properties.getInt(MINWIDTH)), (int) (properties.getInt(MINHEIGHT)));

			
			break;
/*
		case R.id.element_container:
			xmlView = buildRelativeContainer();
			
			break;
			*/
		case R.id.element_grid:
			xmlView = buildGrid();
			
			break;
		
		default:
			throw new NoClassDefFoundError();
		}
		
		xmlView.setLayoutParams(params);
		
		xmlView.setBackgroundResource(properties.getInt(CREATION_STYLE));
		xmlView.setId(idCount++);
		xmlView.setTag(properties);
		xmlView.setOnTouchListener(manipulator);
		
		xmlView.measure(properties.getInt(MINWIDTH), properties.getInt(MINHEIGHT));

		return xmlView;
	}
	
	/**
	 * Use this method to generate Views from a Bundle object.
	 * @author jonesses
	 * @param databaseBundle: a Bundle containing every piece of data to describe a View in the layout
	 * @return the View that the databaseBundle described
	 */
	protected View generate(Bundle databaseBundle)
	{
		int tagID = databaseBundle.getInt(ObjectValueCollector.TYPE);
		
		Bundle properties = getBundle(tagID);
		View xmlView;
		RelativeLayout.LayoutParams params = null;
		params = new RelativeLayout.LayoutParams(databaseBundle.getInt(ObjectValueCollector.WIDTH), databaseBundle.getInt(ObjectValueCollector.HEIGHT));
		

		switch (tagID)
		{
		case R.id.element_button:
			xmlView = buildButton();
			((Button)xmlView).setText(databaseBundle.getString(ObjectValueCollector.USER_TEXT));
			((Button)xmlView).setTextSize(databaseBundle.getInt(ObjectValueCollector.FONTSIZE));
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));
			
			properties.putInt(Generator.CREATION_STYLE, databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));

			
			break;

		case R.id.element_textview:
			xmlView = buildTextview();
			((TextView)xmlView).setText(databaseBundle.getString(ObjectValueCollector.USER_TEXT));
			((TextView)xmlView).setTextSize(databaseBundle.getInt(ObjectValueCollector.FONTSIZE));
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));
			((TextView)xmlView).setGravity(databaseBundle.getInt(ObjectValueCollector.ALIGNMENT));
			
			properties.putInt(Generator.CREATION_STYLE, databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));


			
			break;

		case R.id.element_imageview:
			xmlView = buildImageView();
			xmlView.setBackgroundResource(R.drawable.object_background_default);
			
			properties.putInt(Generator.CREATION_STYLE, databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));

			
			break;

		case R.id.element_edittext:
			xmlView = buildEditText();
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));
			((EditText)xmlView).setGravity(databaseBundle.getInt(ObjectValueCollector.ALIGNMENT));
			((EditText)xmlView).setTextSize(databaseBundle.getInt(ObjectValueCollector.FONTSIZE));

			
			break;

		case R.id.element_radiogroup:
			xmlView = buildRadioButtons();
			((TextView) xmlView).setText(databaseBundle.getString(ObjectValueCollector.USER_TEXT));

			xmlView.setBackgroundResource(R.drawable.object_background_default);

			
			break;

		case R.id.element_switch:
			xmlView = buildSwitch();
			((TextView) xmlView).setText(databaseBundle.getString(ObjectValueCollector.USER_TEXT));
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_checkbox:
			xmlView = buildCheckBox();
			((TextView) ((LinearLayout)xmlView).getChildAt(0)).setText(databaseBundle.getString(ObjectValueCollector.USER_TEXT));

			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_list:
			xmlView = buildListView();
			
			xmlView.setBackgroundResource(R.drawable.object_background_default);
			factory.setAdapter(xmlView, databaseBundle.getInt(ObjectValueCollector.LAYOUT));
			//Content is missing here
			

			
			break;

		case R.id.element_numberpick:
			xmlView = buildNumberPicker();
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_ratingbar:
			xmlView = buildRatingBar();
			xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));
			((RatingBar)xmlView).setRating(databaseBundle.getInt(ObjectValueCollector.RATING));
			((RatingBar)xmlView).setNumStars(databaseBundle.getInt(ObjectValueCollector.STARS_NUM));
			properties.putInt(Generator.CREATION_STYLE, databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));

			
			break;

		case R.id.element_seekbar:
			xmlView = buildSeekBar();
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_timepicker:
			xmlView = buildTimePicker();
			xmlView.setBackgroundResource(R.drawable.object_background_default);


			
			break;

		case R.id.element_grid:
			xmlView = buildGrid();
			factory.setAdapter(xmlView, databaseBundle.getInt(ObjectValueCollector.LAYOUT));
			((GridView)((RelativeLayout)xmlView).getChildAt(0)).setNumColumns(databaseBundle.getInt(ObjectValueCollector.COLUMNS_NUM));
			xmlView.setBackgroundResource(R.drawable.object_background_default);

			
			break;
		
		default:
			throw new NoClassDefFoundError();
		}
		params.leftMargin = databaseBundle.getInt(ObjectValueCollector.X_POS);
		params.topMargin = databaseBundle.getInt(ObjectValueCollector.Y_POS);
		xmlView.setBackgroundResource(databaseBundle.getInt(ObjectValueCollector.BACKGROUND_COLOR));

		xmlView.setLayoutParams(params);
		
		xmlView.setId(idCount++);
		
		properties.putInt(ID, databaseBundle.getInt(DataBase.KEY_ID));
		
		xmlView.setTag(properties);
		
		//xmlView.(databaseBundle.getInt(ObjectValueCollector.X_POS));
		//xmlView.setY(databaseBundle.getInt(ObjectValueCollector.Y_POS));

		
		
		xmlView.setOnTouchListener(manipulator);
		
		xmlView.measure(databaseBundle.getInt(ObjectValueCollector.WIDTH), databaseBundle.getInt(ObjectValueCollector.HEIGHT));

		return xmlView;
	
		
	}
	


	private Bundle getBundle(int which)
	{
		Bundle tagBundle = new Bundle();
		int width = 0;
		int height = 0;
		int scaleType = 0;
		int createMode = 0;
		int presMode = 0;

		presMode = R.drawable.presentation_default_object;

		switch (which)
		{
		case R.id.element_button:

			width = res.getInteger(R.integer.button_factor_width);
			height = res.getInteger(R.integer.button_factor_height);
			scaleType = Overlay.BOTH;
			presMode = R.drawable.presentation_button_default;
			
			break;

		case R.id.element_textview:
			
			width = res.getInteger(R.integer.textview_factor_width);
			height = res.getInteger(R.integer.textview_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_imageview:
			
			width = res.getInteger(R.integer.image_factor_width);
			height = res.getInteger(R.integer.image_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(IMG_SRC, 0);
			tagBundle.putInt(ICN_SRC, 0);
			
			break;

		case R.id.element_edittext:

			width = res.getInteger(R.integer.edittext_factor_width);
			height = res.getInteger(R.integer.edittext_factor_height);
			scaleType = Overlay.BOTH;
			presMode = R.drawable.presentation_border_medium;
			
			break;

		case R.id.element_radiogroup:

			width = res.getInteger(R.integer.radio_factor_width);
			height = res.getInteger(R.integer.radio_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_switch:
			
			width = res.getInteger(R.integer.switch_factor_width);
			height = res.getInteger(R.integer.switch_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_checkbox:
			
			width = res.getInteger(R.integer.checkbox_factor_width);
			height = res.getInteger(R.integer.checkbox_factor_height);
			scaleType = Overlay.BOTH;

			break;

		case R.id.element_list:
			
			width = res.getInteger(R.integer.list_factor_width);
			height = res.getInteger(R.integer.list_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(EXAMPLE_CONTENT, R.id.content_choose_hipster);
			tagBundle.putInt(EXAMPLE_LAYOUT, R.layout.item_listview_example_layout_1);

			break;

		case R.id.element_numberpick:
			
			width = res.getInteger(R.integer.numberpicker_factor_width);
			height = res.getInteger(R.integer.numberpicker_factor_height);
			scaleType = Overlay.VERTICAL;

			break;

		case R.id.element_ratingbar:
			
			width = res.getInteger(R.integer.ratingbar_factor_width);
			height = res.getInteger(R.integer.ratingbar_factor_height);
			scaleType = Overlay.BOTH;
			
			break;

		case R.id.element_seekbar:
			
			width = res.getInteger(R.integer.seekbar_factor_width);
			height = res.getInteger(R.integer.seekbar_factor_height);
			scaleType = Overlay.HORIZONTAL;

			break;

		case R.id.element_timepicker:
			
			width = res.getInteger(R.integer.timepicker_factor_width);
			height = res.getInteger(R.integer.timepicker_factor_height);
			scaleType = Overlay.VERTICAL;

			break;
/*
		case R.id.element_container:
			
			width = res.getInteger(R.integer.edittext_factor_width);
			height = res.getInteger(R.integer.edittext_factor_height);
			scaleType = Overlay.BOTH;
			presMode = R.drawable.presentation_border_light;

			break;
			*/
		case R.id.element_grid:
			
			width = res.getInteger(R.integer.grid_factor_width);
			height = res.getInteger(R.integer.grid_factor_height);
			scaleType = Overlay.BOTH;
			tagBundle.putInt(EXAMPLE_CONTENT, R.id.content_choose_bacon);
			tagBundle.putInt(EXAMPLE_LAYOUT, R.layout.item_gridview_example_layout_3);
			
			
			break;
			
		default:
			Log.d("bundle ", "not built");
			throw new NoClassDefFoundError();
		}
		
		width *= gridFactor;
		height *= gridFactor;
		createMode = R.drawable.object_background_default;
		
		tagBundle.putInt(PRESENTATION_STYLE, presMode);
		tagBundle.putInt(CREATION_STYLE, createMode);
		tagBundle.putInt(SCALETYPE, scaleType);
		tagBundle.putInt(MINHEIGHT, height);
		tagBundle.putInt(MINWIDTH, width);
		tagBundle.putInt(TYPE, which);
		

		return tagBundle;
	}

	private View buildGrid()
	{
		RelativeLayout xmlGridContainer = createContainer();
		
		GridView xmlGrid = (GridView) inflater.inflate(R.layout.item_gridview_layout, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlGrid.setLayoutParams(params);
		xmlGridContainer.addView(xmlGrid);
		factory.setAdapter(xmlGrid, R.layout.item_gridview_example_layout_3);
		
		return xmlGridContainer;
	}
	
	private View buildTimePicker()
	{
		RelativeLayout xmlTimePickerContainer = createContainer();

		TimePicker xmlTimePicker = (TimePicker) inflater.inflate(R.layout.item_timepicker_layout, null);
		xmlTimePicker.setIs24HourView(true);
		xmlTimePicker.setEnabled(false);

		RelativeLayout.LayoutParams pickerparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pickerparams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);

		xmlTimePicker.setLayoutParams(pickerparams);
		xmlTimePickerContainer.addView(xmlTimePicker);

		return xmlTimePickerContainer;
	}

	private View buildSeekBar()
	{
		RelativeLayout xmlSeekBarContainer = createContainer();

		SeekBar xmlSeekBar = (SeekBar) inflater.inflate(R.layout.item_seekbar_layout, null);
		xmlSeekBar.setEnabled(false);
		xmlSeekBar.setActivated(true);

		RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		seekBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlSeekBar.setLayoutParams(seekBarParams);
		
		xmlSeekBarContainer.addView(xmlSeekBar);

		return xmlSeekBarContainer;
	}

	private View buildRatingBar()
	{
		RelativeLayout xmlRatingBarContainer = createContainer();

		RatingBar xmlRatingBar = (RatingBar) inflater.inflate(R.layout.item_ratingbar_layout, null);
		xmlRatingBar.setEnabled(true);
		xmlRatingBar.setActivated(true);
		
		RelativeLayout.LayoutParams ratingBarParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ratingBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlRatingBar.setLayoutParams(ratingBarParams);

		xmlRatingBarContainer.addView(xmlRatingBar);
		
		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		xmlRatingBarContainer.setLayoutParams(containerParams);
		
		
		return xmlRatingBarContainer;
	}
	
	private View buildListView()
	{
		RelativeLayout container = createContainer();
		
		ListView xmlList = (ListView) inflater.inflate(R.layout.item_istview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlList.setLayoutParams(params);
		container.addView(xmlList);
		
		factory.setAdapter(xmlList, R.layout.item_listview_example_layout_1);
		
		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		container.setLayoutParams(containerParams);
		
		return container;
	}

	private View buildNumberPicker()
	{
		RelativeLayout xmlPickerLayout = createContainer();

		NumberPicker xmlPicker = (NumberPicker) inflater.inflate(R.layout.item_numberpicker_layout, null);

		xmlPicker.setEnabled(false);
		xmlPicker.setMaxValue(5);
		xmlPicker.setMinValue(1);
		xmlPicker.setWrapSelectorWheel(false);
		xmlPicker.setValue(3);

		RelativeLayout.LayoutParams pickerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pickerParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlPicker.setLayoutParams(pickerParams);
		xmlPickerLayout.addView(xmlPicker);

		return xmlPickerLayout;
	}

	private LinearLayout buildRelativeContainer()
	{
		LinearLayout relativeLayout = new LinearLayout(context);
		relativeLayout.setBackgroundResource(R.drawable.object_background_default);
		relativeLayout.setOrientation(LinearLayout.HORIZONTAL);

		relativeLayout.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View v, DragEvent event)
			{
				switch (event.getAction())
				{
				case DragEvent.ACTION_DRAG_STARTED:
					Bundle tagBundle = (Bundle) v.getTag();

					int id = tagBundle.getInt(Generator.TYPE);
					/*
					if (id == R.id.element_container)
					{
						return false;
					}
					return true;
*/
				case DragEvent.ACTION_DRAG_ENTERED:
					
					return true;
				case DragEvent.ACTION_DRAG_EXITED:

					return true;

				case DragEvent.ACTION_DROP:
					// Dropped, reassign View to ViewGroup
					View view = (View) event.getLocalState();
				      ViewGroup owner = (ViewGroup) view.getParent();
				      owner.removeView(view);
				      LinearLayout container = (LinearLayout) v;
				      container.addView(view);
					
					
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					container.addView(view);

					return true;
					
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
				case DragEvent.ACTION_DRAG_ENDED:

				default:
					break;
				}
				return true;
			}
		});
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);

		relativeLayout.setLayoutParams(params);
		return relativeLayout;

	}

	private RelativeLayout createContainer()
	{
		RelativeLayout xmlPickerLayout = new RelativeLayout(context)
		{

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev)
			{
				Log.d("CONTAINER", "INTERCEPTING MOTION EVENT");
				return true;
			}

		};
		xmlPickerLayout.setBackgroundResource(R.drawable.object_background_default);
		xmlPickerLayout.setClickable(true);
		xmlPickerLayout.setFocusable(true);
		xmlPickerLayout.setFocusableInTouchMode(true);
		xmlPickerLayout.setEnabled(true);
		xmlPickerLayout.setMotionEventSplittingEnabled(false);
		xmlPickerLayout.setFilterTouchesWhenObscured(false);
		return xmlPickerLayout;
	}

	private View buildSearchView()
	{
		RelativeLayout xmlSearchViewContainer = createContainer();

		SearchView xmlSearchView = (SearchView) inflater.inflate(R.layout.item_searchview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlSearchView.setLayoutParams(params);

		//xmlSearchView.setEnabled(false);
		xmlSearchViewContainer.addView(xmlSearchView);

		return xmlSearchViewContainer;
	}

	private View buildCheckBox() //
	{
		LinearLayout xmlCheckBox = (LinearLayout) inflater.inflate(R.layout.item_checkbox_layout, null);

		return xmlCheckBox;
	}

	private View buildSwitch()
	{
		Switch xmlSwitch = (Switch) inflater.inflate(R.layout.item_switch_layout, null);
		xmlSwitch.setBackgroundResource(R.drawable.object_background_default);

		return xmlSwitch;
	}

	private View buildRadioButtons()
	{

		RadioButton xmlRadioButton = (RadioButton) inflater.inflate(R.layout.item_radiobutton_layout, null);
		xmlRadioButton.setBackgroundResource(R.drawable.object_background_default);
		//xmlRadioGroup.addView(xmlRadioButton);

		return xmlRadioButton;
	}

	/**
	 * Generate new TextView from xml resource
	 * 
	 * @return new TextView
	 */
	private TextView buildTextview()
	{
		TextView xmlTextView = (TextView) inflater.inflate(R.layout.item_textview_layout, null);

		return xmlTextView;
	}

	/**
	 * Generate new ImageView from xml resource
	 * 
	 * @return the newly generated ImageView
	 */
	private ImageView buildImageView()
	{
		ImageView xmlImageView = (ImageView) inflater.inflate(R.layout.item_imageview_layout, null);
		return xmlImageView;
	}

	/**
	 * Generate new EditText from xml resource
	 * 
	 * @return new Edittext
	 */
	private EditText buildEditText()
	{
		EditText xmlEditText = (EditText) inflater.inflate(R.layout.item_edittext_layout, null);

		return xmlEditText;
	}

	/**
	 * Generate new Button from xml resource
	 * 
	 * @return new Button
	 */
	private Button buildButton()
	{
		Button xmlButton = (Button) inflater.inflate(R.layout.item_button_layout, null);
		
		return xmlButton;
	}
}
