package helpers;

import android.database.Cursor;
import android.os.Bundle;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class FromDatabaseObjectCreator
{

	public FromDatabaseObjectCreator(Cursor cursor)
	{

		while (cursor.moveToNext())
		{
			Bundle valuesBundle = new Bundle();

			int idxKEYID = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_ID);
			int idxID = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_TYPE);
			int idxXPos = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_XPOS);
			int idxYPos = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_YPOS);
			int idxWidth = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_WIDTH);
			int idxHeight = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_HEIGHT);
			int idxUserText = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_USERTEXT);
			int idxRating = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_RATING);
			int idxContent = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_CONTENT);
			int idxColumnsNum = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_COLUMNS_NUM);
			int idxLayout = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_LAYOUT);
			int idxStarsNum = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_STARSNUM);
			int idxAlignment = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_ALIGNMENT);
			int idxFontsize = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_FONTSIZE);
			int idxImageSource = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_IMGSRC);
			int idxIconSource = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_ICNSRC);
			int idxBackgroundColor = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUNDCLR);
			Log.d("KEY", String.valueOf(cursor.getInt(idxKEYID)));
			valuesBundle.putInt(ObjectValues.DATABASE_ID, cursor.getInt(idxKEYID));
			valuesBundle.putInt(ObjectValues.TYPE, cursor.getInt(idxID));
			valuesBundle.putInt(ObjectValues.X_POS, cursor.getInt(idxXPos));
			valuesBundle.putInt(ObjectValues.Y_POS, cursor.getInt(idxYPos));
			valuesBundle.putInt(ObjectValues.WIDTH, cursor.getInt(idxWidth));
			valuesBundle.putInt(ObjectValues.HEIGHT, cursor.getInt(idxHeight));
			valuesBundle.putString(ObjectValues.USER_TEXT, cursor.getString(idxUserText));
			valuesBundle.putInt(ObjectValues.RATING, cursor.getInt(idxRating));
			valuesBundle.putInt(ObjectValues.EXAMPLE_CONTENT, cursor.getInt(idxContent));
			valuesBundle.putInt(ObjectValues.COLUMNS_NUM, cursor.getInt(idxColumnsNum));
			valuesBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, cursor.getInt(idxLayout));
			valuesBundle.putInt(ObjectValues.STARS_NUM, cursor.getInt(idxStarsNum));
			valuesBundle.putInt(ObjectValues.ALIGNMENT, cursor.getInt(idxAlignment));
			valuesBundle.putInt(ObjectValues.FONTSIZE, cursor.getInt(idxFontsize));
			valuesBundle.putString(ObjectValues.IMG_SRC, cursor.getString(idxImageSource));
			valuesBundle.putInt(ObjectValues.ICN_SRC, cursor.getInt(idxIconSource));
			valuesBundle.putInt(ObjectValues.BACKGROUND_EDIT, cursor.getInt(idxBackgroundColor));

			// This is independent form Database, just assigning the right
			// colours to presentationMode tag.
			valuesBundle.putInt(ObjectValues.BACKGROUND_PRES, getPresColour(cursor.getInt(idxBackgroundColor)));

			listener.objectLoaded(valuesBundle);

		}
	}
/**
 * @deprecated
 * @param int1
 * @return
 */
	private int getPresColour(int int1)
	{
		int presColor;

		switch (int1)
		{
		case R.drawable.object_background_aqua:
			presColor = R.drawable.presentation_object_background_aqua;
			break;
		case R.drawable.object_background_blue:
			presColor = R.drawable.presentation_object_background_blue;
			break;
		case R.drawable.object_background_green:
			presColor = R.drawable.presentation_object_background_green;
			break;
		case R.drawable.object_background_green_light:
			presColor = R.drawable.presentation_object_background_green_light;
			break;
		case R.drawable.object_background_grey:
			presColor = R.drawable.presentation_object_background_grey;
			break;
		case R.drawable.object_background_grey_dark:
			presColor = R.drawable.presentation_object_background_grey_dark;
			break;
		case R.drawable.object_background_grey_light:
			presColor = R.drawable.presentation_object_background_grey_light;
			break;
		case R.drawable.object_background_orange:
			presColor = R.drawable.presentation_object_background_orange;
			break;
		case R.drawable.object_background_red:
			presColor = R.drawable.presentation_object_background_red;
			break;
		case R.drawable.object_background_default_button:
			presColor = R.drawable.presentation_button_default;
			break;
		case R.drawable.object_background_default_edittext:
			presColor = R.drawable.presentation_border_medium;
		default:
			presColor = R.drawable.presentation_default_object;
		}
		return presColor;
	}

	public interface OnObjectLoadedFromDatabaseListener
	{
		void objectLoaded(Bundle objectBundle);

	}

	private static OnObjectLoadedFromDatabaseListener listener;

	public static void setOnObjectCreatedFromDatabaseListener(
			OnObjectLoadedFromDatabaseListener listener)
	{
		FromDatabaseObjectCreator.listener = listener;
	}

}
