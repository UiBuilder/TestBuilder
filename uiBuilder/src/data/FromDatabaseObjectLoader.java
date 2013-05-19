package data;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

/**
 * After an async database query for objects belonging to a specific screen, an instance of this class
 * processes the result cursor to generate a bundle, representing a former object.
 * The bundles are collected in an arraylist and passed as an array, for type safeties sake.
 * A listener is notified via callback when all cursor entries are collected for further processing of the bundle array.
 * 
 * @author jonesses  edited by funklos
 *
 */
public class FromDatabaseObjectLoader
{

	public void loadObjects(Cursor cursor)
	{
		if (cursor.getCount() != 0)
		{
			ArrayList<Bundle> dataBaseObjects = new ArrayList<Bundle>();
			
			while (cursor.moveToNext())
			{
	
				android.util.Log.d("cursor size", String.valueOf(cursor.getCount()));
				
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
				//int idxBackgroundColorEdit = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUNDCLR_EDIT);
				//int idxBackgroundColorPres = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUNDCLR_PRESENTATION);
				int idxBackgroundColor = cursor.getColumnIndexOrThrow(ScreenProvider.KEY_OBJECTS_VIEW_BACKGROUND);
				
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
				//valuesBundle.putInt(ObjectValues.BACKGROUND_EDIT, cursor.getInt(idxBackgroundColorEdit));
				//valuesBundle.putInt(ObjectValues.BACKGROUND_PRES, cursor.getInt(idxBackgroundColorPres ));
				valuesBundle.putInt(ObjectValues.BACKGROUNDCOLOR, cursor.getInt(idxBackgroundColor));
				
				dataBaseObjects.add(valuesBundle);
			}
			
			cursor.close();
			
			Bundle[] objectList = new Bundle[dataBaseObjects.size()];
			dataBaseObjects.toArray(objectList);
			
			listener.objectsLoaded(objectList);
			Log.d("FromDatabaseObjectCreator", "size of dataBaseObjects: "+ String.valueOf(dataBaseObjects.size()));
			Log.d("FromDatabaseObjectCreator", "done loading Objects, called listener");
		}
	}

	/**
	 * Interface definition, implemented by the class responsible for further processing. 
	 * @author jonesses, edited by funklos
	 *
	 */
	public interface OnObjectLoadedFromDatabaseListener
	{
		void objectsLoaded(Bundle[] objectList);
	}

	private static OnObjectLoadedFromDatabaseListener listener;

	public static void setOnObjectCreatedFromDatabaseListener(
			OnObjectLoadedFromDatabaseListener listener)
	{
		FromDatabaseObjectLoader.listener = listener;
	}

}
