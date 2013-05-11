package creators;

import de.ur.rk.uibuilder.R;

/**
 * used to identify the object type. switching on the button ids of the @see ItemboxFragment is
 * not reliable, because recompilation can change the ids.
 * @author funklos
 *
 */
public final class ObjectIdMapper
{
	public static final int
	
	OBJECT_ID_BUTTON = 0x001,
	OBJECT_ID_TEXTVIEW = 0X002,
	OBJECT_ID_IMAGEVIEW = 0X003,
	OBJECT_ID_EDITTEXT = 0x004,
	
	OBJECT_ID_RADIOGROUP = 0xA01,
	OBJECT_ID_SWITCH = 0xA02,
	OBJECT_ID_CHECKBOX = 0xA03,
	OBJECT_ID_LISTVIEW = 0xA04,
	OBJECT_ID_NUMBERPICKER = 0xA05,
	OBJECT_ID_RATINGBAR = 0xA06,
	OBJECT_ID_SEEKBAR = 0xA07,
	OBJECT_ID_TIMEPICKER = 0xA08,
	OBJECT_ID_GRIDVIEW  = 0xA09,
	OBJECT_ID_SPINNER  = 0xA0A;
	
	
	
	public static int mapType (int id)
	{
		switch (id)
		{
		case R.id.element_button:
			return OBJECT_ID_BUTTON;

		case R.id.element_textview:
			return OBJECT_ID_TEXTVIEW;

		case R.id.element_imageview:
			return OBJECT_ID_IMAGEVIEW;

		case R.id.element_edittext:
			return OBJECT_ID_EDITTEXT;

		case R.id.element_radiogroup:
			return OBJECT_ID_RADIOGROUP;

		case R.id.element_switch:
			return OBJECT_ID_SWITCH;
			
		case R.id.element_checkbox:
			return OBJECT_ID_CHECKBOX;

		case R.id.element_list:
			return OBJECT_ID_LISTVIEW;

		case R.id.element_numberpick:
			return OBJECT_ID_NUMBERPICKER;

		case R.id.element_ratingbar:
			return OBJECT_ID_RATINGBAR;

		case R.id.element_seekbar:
			return OBJECT_ID_SEEKBAR;

		case R.id.element_timepicker:
			return OBJECT_ID_TIMEPICKER;
	
		case R.id.element_grid:
			return OBJECT_ID_GRIDVIEW;
			
		case R.id.element_spinner:
			return OBJECT_ID_SPINNER;
		}
		return 0;
	}
}
