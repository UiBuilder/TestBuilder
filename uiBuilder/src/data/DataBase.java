package data;

import helpers.ObjectValueCollector;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * ContentProvider SKELETON from
 * @see WROX - Professional Android 4 Application Development, Reto Meier
 * @author funklos
 *
 */
public class DataBase extends ContentProvider
{
	public static final int SCREENS_LOADER = 1;
	public static final int OBJECTS_LOADER = 2;
	
	private static final String AUTHORITY = "de.ur.rk.uibuilder";
	private static final String SCREENS_URI = "screens";
	private static final String OBJECTS_URI = "objects";
	
	public static final Uri CONTENT_URI_SCREENS = Uri.parse("content://" + AUTHORITY + "/" + SCREENS_URI);
	public static final Uri CONTENT_URI_OBJECTS = Uri.parse("content://" + AUTHORITY + "/" + OBJECTS_URI);
	
	private DataManager data;
	
	//table column constants
	public static final String KEY_ID = "_id";
	
	//SCREENS TABLE
	public static final String 
					KEY_SCREEN_NAME = "name", 
					KEY_SCREEN_DATE = "date",
					KEY_SCREEN_PREVIEW = "preview"
					;
	
	//OBJECTS TABLE
	public static final String
					KEY_OBJECTS_SCREEN = "screen",
					KEY_OBJECTS_VIEW_TYPE = ObjectValueCollector.TYPE,
					KEY_OBJECTS_VIEW_XPOS = ObjectValueCollector.X_POS,
					KEY_OBJECTS_VIEW_YPOS = ObjectValueCollector.Y_POS,
					KEY_OBJECTS_VIEW_WIDTH = ObjectValueCollector.WIDTH,
					KEY_OBJECTS_VIEW_HEIGHT = ObjectValueCollector.HEIGHT,
					KEY_OBJECTS_VIEW_USERTEXT = ObjectValueCollector.USER_TEXT,
					KEY_OBJECTS_VIEW_RATING = ObjectValueCollector.RATING,
					KEY_OBJECTS_VIEW_CONTENT = ObjectValueCollector.CONTENT,
					KEY_OBJECTS_VIEW_COLUMNS_NUM = ObjectValueCollector.COLUMNS_NUM,
					KEY_OBJECTS_VIEW_LAYOUT = ObjectValueCollector.LAYOUT,
					KEY_OBJECTS_VIEW_STARSNUM = ObjectValueCollector.STARS_NUM,
					KEY_OBJECTS_VIEW_ALIGNMENT = ObjectValueCollector.ALIGNMENT,
					KEY_OBJECTS_VIEW_FONTSIZE = ObjectValueCollector.FONTSIZE,
					KEY_OBJECTS_VIEW_IMGSRC = ObjectValueCollector.IMG_SRC,
					KEY_OBJECTS_VIEW_ICNSRC = ObjectValueCollector.ICN_SRC,
					KEY_OBJECTS_VIEW_BACKGROUNDCLR = ObjectValueCollector.BACKGROUND_COLOR
					;
					
					
	
	//uri match constants
	private static final int SCREENS_SINGLE = 1;
	private static final int SCREENS_ALL = 2;
	
	private static final int OBJECTS_SINGLE = 3;
	private static final int OBJECTS_ALL = 4;
	
	
	private static final UriMatcher match;
	static 
	{
		match = new UriMatcher(UriMatcher.NO_MATCH);
		
		match.addURI(AUTHORITY, SCREENS_URI, SCREENS_ALL);
		match.addURI(AUTHORITY, SCREENS_URI + "/#", SCREENS_SINGLE);
		
		match.addURI(AUTHORITY, OBJECTS_URI, OBJECTS_ALL);
		match.addURI(AUTHORITY, OBJECTS_URI + "/#", OBJECTS_SINGLE);
	}


	@Override
	public boolean onCreate()
	{
		data = new DataManager (getContext(), DataManager.DB_NAME, null, DataManager.DB_VERSION);
		return true;
	}
	


	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		String nullColumnHack = null;
		
		Log.d("insert", String.valueOf(match.match(uri)));
		Log.d("inserted screenId",String.valueOf(values.getAsLong(DataBase.KEY_OBJECTS_SCREEN)));
		Log.d("insert", uri.toString());
		long id;
		Uri inserted = null;
		
		switch (match.match(uri))
		{
		case SCREENS_ALL:
			
			id = db.insert(DataManager.TABLE_SCREENS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_SCREENS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
			
		case OBJECTS_ALL:
			Log.d("objects all", "about to insert");
			id = db.insert(DataManager.TABLE_OBJECTS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_OBJECTS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
		}
		Log.d("inserted", inserted.toString());
		return inserted;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		String groupBy = null;
		String having = null;
		String row = null;
		
		sortOrder = null;
		
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		
		switch (match.match(uri))
		{
			case SCREENS_SINGLE:
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_SCREENS);
				break;
				
			case SCREENS_ALL:
				
				sortOrder = KEY_SCREEN_DATE + " DESC";
				query.setTables(DataManager.TABLE_SCREENS);
				break;
				
			case OBJECTS_SINGLE:
				
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_OBJECTS);
				break;
				
			case OBJECTS_ALL:
				
				//query.appendWhere(selection);
				query.setTables(DataManager.TABLE_OBJECTS);
				break;
		}
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectArgs)
	{
		SQLiteDatabase db = data.getWritableDatabase();

		String row;
		int updateCount = 0;
		
		switch (match.match(uri))
		{
		case SCREENS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			updateCount = db.update(DataManager.TABLE_SCREENS, values, selection, selectArgs);
			break;
			
		case OBJECTS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			updateCount = db.update(DataManager.TABLE_OBJECTS, values, selection, selectArgs);
			break;
			
		default: break;
		}
		
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selArgs)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		String row;
		int deleteCount = 0;
		
		switch (match.match(uri))
		{
		case SCREENS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			

			deleteCount = db.delete(DataManager.TABLE_SCREENS, selection, selArgs);
			break;
			
		case OBJECTS_ALL:

			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			Log.d("objects deleted:", String.valueOf(deleteCount));
			break;
			
		case OBJECTS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");

			Log.d("deleting", String.valueOf(row));
			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			break;
			
		default: break;
		}
		if (selection == null)
		{
			selection = "1";
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (match.match(uri))
		{
		case SCREENS_ALL:
			return "vnd.android.cursor.dir/vnd.uibuilder.screens";
		
		case SCREENS_SINGLE:
			return "vnd.android.cursor.item/vnd.uibuilder.screens";
			
		case OBJECTS_ALL:
			return "vnd.android.cursor.dir/vnd.uibuilder.objects";
			
		case OBJECTS_SINGLE:
			return "vnd.android.cursor.item/vnd.uibuilder.objects";
			
		default: throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
	}


	private static class DataManager extends SQLiteOpenHelper
	{	
		
		public static final String 
						DB_NAME = "uibuilder.db",
						TABLE_SCREENS = "screenManager",
						TABLE_OBJECTS = "objects";
		
		private static final int DB_VERSION = 18;
		
		private static final String CREATE = "create table if not exists ";
		private static final String DROP = "DROP TABLE if exists ";	
		
		private static final String 
						TEXT_NULL = " text not null", 
						INT_NULL = " integer not null",
						TEXT = " text",
						INT = " integer",
						KOMMA = ", "
						;
		
		private static final String ID = KEY_ID + " integer primary key autoincrement, ";
		
		
		private static final String OBJECT_PROPERTIES 
						= KEY_OBJECTS_VIEW_TYPE + INT_NULL + KOMMA 
						+ KEY_OBJECTS_VIEW_XPOS + INT_NULL + KOMMA
						+ KEY_OBJECTS_VIEW_YPOS + INT_NULL + KOMMA
						+ KEY_OBJECTS_VIEW_WIDTH + INT_NULL + KOMMA
						+ KEY_OBJECTS_VIEW_HEIGHT + INT_NULL + KOMMA
						
						+ KEY_OBJECTS_VIEW_ALIGNMENT + INT + KOMMA
						+ KEY_OBJECTS_VIEW_BACKGROUNDCLR + INT + KOMMA
						+ KEY_OBJECTS_VIEW_COLUMNS_NUM + INT + KOMMA
						+ KEY_OBJECTS_VIEW_CONTENT + INT + KOMMA
						+ KEY_OBJECTS_VIEW_FONTSIZE + INT + KOMMA
						+ KEY_OBJECTS_VIEW_ICNSRC + INT + KOMMA
						+ KEY_OBJECTS_VIEW_IMGSRC + TEXT + KOMMA
						+ KEY_OBJECTS_VIEW_LAYOUT + INT + KOMMA
						+ KEY_OBJECTS_VIEW_RATING + INT + KOMMA
						+ KEY_OBJECTS_VIEW_STARSNUM + INT + KOMMA
						+ KEY_OBJECTS_VIEW_USERTEXT + TEXT
						
						+ ");"
						;
		
		private static final String CREATE_SCREENS_TABLE 
						= CREATE 
						+ TABLE_SCREENS + " ("
						+ ID 
						+ KEY_SCREEN_NAME + TEXT_NULL + KOMMA
						+ KEY_SCREEN_DATE + TEXT_NULL + KOMMA
						+ KEY_SCREEN_PREVIEW + TEXT
						
						+ ");";
		
		private static final String CREATE_OBJECTS_TABLE 
						= CREATE
						+ TABLE_OBJECTS + " ("
						+ ID
						+ KEY_OBJECTS_SCREEN + INT_NULL + KOMMA
						+ OBJECT_PROPERTIES;

		
		private static final String DROP_MAIN 
						= DROP 
						+ TABLE_SCREENS;
		
		private static final String DROP_OBJECTS 
						= DROP 
						+ TABLE_OBJECTS; 
		
		public DataManager(Context context, String name, CursorFactory factory,
				int version)
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE_SCREENS_TABLE);
			db.execSQL(CREATE_OBJECTS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL(DROP_MAIN);
			db.execSQL(DROP_OBJECTS);
			
			onCreate(db);
		}
	}
}
