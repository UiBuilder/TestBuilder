package helpers;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import data.DataBase;

public class FromDatabaseObjectCreator
{
	
	public static void createObjects(Loader<Cursor> arg0, Cursor cursor)
	{
		
		
		while (cursor.moveToNext())
		{
			Bundle valuesBundle = new Bundle();
			
			int idxID = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_TYPE);
			int idxXPos = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_XPOS);
			int idxYPos = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_YPOS);
			int idxWidth = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_WIDTH);
			int idxHeight = cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_HEIGHT);
			int idxUserText =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_USERTEXT);
			int idxRating =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_RATING);
			int idxContent =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_CONTENT);
			int idxColumnsNum =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_COLUMNS_NUM);
			int idxLayout =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_LAYOUT);
			int idxStarsNum =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_STARSNUM);
			int idxAlignment =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_ALIGNMENT);
			int idxFontsize =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_FONTSIZE);
			int idxImageSource =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_IMGSRC);
			int idxIconSource =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_ICNSRC);
			int idxBackgroundColor =  cursor.getColumnIndexOrThrow(DataBase.KEY_OBJECTS_VIEW_BACKGROUNDCLR);
			
			valuesBundle.putInt(ObjectValueCollector.TYPE, cursor.getInt(idxID));
			valuesBundle.putInt(ObjectValueCollector.X_POS, cursor.getInt(idxXPos));
			valuesBundle.putInt(ObjectValueCollector.Y_POS, cursor.getInt(idxYPos));
			valuesBundle.putInt(ObjectValueCollector.WIDTH, cursor.getInt(idxWidth));
			valuesBundle.putInt(ObjectValueCollector.HEIGHT, cursor.getInt(idxHeight));
			valuesBundle.putInt(ObjectValueCollector.USER_TEXT, cursor.getInt(idxUserText));
			valuesBundle.putInt(ObjectValueCollector.RATING, cursor.getInt(idxRating));
			valuesBundle.putInt(ObjectValueCollector.CONTENT, cursor.getInt(idxContent));
			valuesBundle.putInt(ObjectValueCollector.COLUMNS_NUM, cursor.getInt(idxColumnsNum));
			valuesBundle.putInt(ObjectValueCollector.LAYOUT, cursor.getInt(idxLayout));
			valuesBundle.putInt(ObjectValueCollector.STARS_NUM, cursor.getInt(idxStarsNum));
			valuesBundle.putInt(ObjectValueCollector.ALIGNMENT, cursor.getInt(idxAlignment));
			valuesBundle.putInt(ObjectValueCollector.FONTSIZE, cursor.getInt(idxFontsize));
			valuesBundle.putInt(ObjectValueCollector.IMG_SRC, cursor.getInt(idxImageSource));
			valuesBundle.putInt(ObjectValueCollector.ICN_SRC, cursor.getInt(idxIconSource));
			valuesBundle.putInt(ObjectValueCollector.BACKGROUND_COLOR, cursor.getInt(idxBackgroundColor));

			listener.objectLoaded(valuesBundle);
			
			
		}
	}
	
	





	public interface OnObjectLoadedFromDatabaseListener
	{
		void objectLoaded(Bundle objectBundle);

		
	}

	private static OnObjectLoadedFromDatabaseListener listener;

	protected static void setOnObjectSelectedListener(
			OnObjectLoadedFromDatabaseListener listener)
	{
		FromDatabaseObjectCreator.listener = listener;
	}
	
}
