package data;

import helpers.ObjectValueCollector;
import helpers.ObjectValues;
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
	
	private static final String 
					AUTHORITY = "de.ur.rk.uibuilder",
					PREFIX = "content://",
					SCREENS_URI = "screens",
					OBJECTS_URI = "objects",
					PREVIEWS_URI = "previews";
	
	public static final Uri 
					CONTENT_URI_SCREENS = Uri.parse(PREFIX + AUTHORITY + "/" + SCREENS_URI),
					CONTENT_URI_OBJECTS = Uri.parse(PREFIX + AUTHORITY + "/" + OBJECTS_URI),
					CONTENT_URI_PREVIEWS = Uri.parse(PREFIX + AUTHORITY + "/" + PREVIEWS_URI);
	
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
					KEY_OBJECTS_VIEW_TYPE = ObjectValues.TYPE,
					KEY_OBJECTS_VIEW_XPOS = ObjectValues.X_POS,
					KEY_OBJECTS_VIEW_YPOS = ObjectValues.Y_POS,
					KEY_OBJECTS_VIEW_WIDTH = ObjectValues.WIDTH,
					KEY_OBJECTS_VIEW_HEIGHT = ObjectValues.HEIGHT,
					KEY_OBJECTS_VIEW_USERTEXT = ObjectValues.USER_TEXT,
					KEY_OBJECTS_VIEW_RATING = ObjectValues.RATING,
					KEY_OBJECTS_VIEW_CONTENT = ObjectValues.EXAMPLE_CONTENT,
					KEY_OBJECTS_VIEW_COLUMNS_NUM = ObjectValues.COLUMNS_NUM,
					KEY_OBJECTS_VIEW_LAYOUT = ObjectValues.EXAMPLE_LAYOUT,
					KEY_OBJECTS_VIEW_STARSNUM = ObjectValues.STARS_NUM,
					KEY_OBJECTS_VIEW_ALIGNMENT = ObjectValues.ALIGNMENT,
					KEY_OBJECTS_VIEW_FONTSIZE = ObjectValues.FONTSIZE,
					KEY_OBJECTS_VIEW_IMGSRC = ObjectValues.IMG_SRC,
					KEY_OBJECTS_VIEW_ICNSRC = ObjectValues.ICN_SRC,
					KEY_OBJECTS_VIEW_BACKGROUNDCLR = ObjectValues.BACKGROUND_EDIT
					;
					
	//PREVIEWS TABLE
	public static final String
					KEY_PREVIEWS_PATH = "path",
					KEY_PREVIEWS_ASSOCIATED = "associatedscreen"
					;
	
	//uri match constants
	private static final int 
					SCREENS_SINGLE = 0x01,
					SCREENS_ALL = 0x02,
					
					OBJECTS_SINGLE = 0x03,
					OBJECTS_ALL = 0x04,
					
					PREVIEWS_SINGLE = 0x05,
					PREVIEWS_ALL = 0x06
					;
	
	
	private static final UriMatcher match;
	static 
	{
		match = new UriMatcher(UriMatcher.NO_MATCH);
		
		match.addURI(AUTHORITY, SCREENS_URI, SCREENS_ALL);
		match.addURI(AUTHORITY, SCREENS_URI + "/#", SCREENS_SINGLE);
		
		match.addURI(AUTHORITY, OBJECTS_URI, OBJECTS_ALL);
		match.addURI(AUTHORITY, OBJECTS_URI + "/#", OBJECTS_SINGLE);
		
		match.addURI(AUTHORITY, PREVIEWS_URI, PREVIEWS_ALL);
		match.addURI(AUTHORITY, PREVIEWS_URI + "/#", PREVIEWS_SINGLE);
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
		
		Log.d("insert as", String.valueOf(match.match(uri)));
		
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
			Log.d("inserted screenId",String.valueOf(values.getAsLong(DataBase.KEY_OBJECTS_SCREEN)));
			
			id = db.insert(DataManager.TABLE_OBJECTS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_OBJECTS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
			
		case PREVIEWS_ALL:
			
			Log.d("previews all", "about to insert");
			
			id = db.insert(DataManager.TABLE_PREVIEWS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_PREVIEWS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
			
		}
		Log.d("inserted uri", inserted.toString());
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
				
			case PREVIEWS_SINGLE:
				
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_PREVIEWS);
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
			
		case PREVIEWS_SINGLE:
			
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			updateCount = db.update(DataManager.TABLE_PREVIEWS, values, selection, selectArgs);
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
		Log.d("database", "delete called");
		String row;
		int deleteCount = 0;
		
		switch (match.match(uri))
		{
		case SCREENS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			Log.d("database delete was called with", row);
			deleteCount = db.delete(DataManager.TABLE_SCREENS, selection, selArgs);
			break;
			
		case OBJECTS_ALL:

			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			Log.d("objects deleted:", String.valueOf(deleteCount));
			break;
			
		case OBJECTS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");

			Log.d("database delete objects single was called with", row);
			Log.d("deleting", String.valueOf(row));
			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			break;
			
		case PREVIEWS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			Log.d("database delete was called with", row);
			deleteCount = db.delete(DataManager.TABLE_PREVIEWS, selection, selArgs);
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
						TABLE_OBJECTS = "objects",
						TABLE_PREVIEWS = "previews"
						;
		
		
		private static final int DB_VERSION = 22;
		
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
						;
		
		private static final String PREVIEWS_PROPERTIES
						= KEY_PREVIEWS_PATH + TEXT_NULL + KOMMA
						+ KEY_PREVIEWS_ASSOCIATED + INT_NULL + KOMMA
						;
		
		//CREATE COMMANDS
		private static final String 
						CREATE_SCREENS_TABLE 
						= CREATE 
						+ TABLE_SCREENS + " ("
						+ ID 
						+ KEY_SCREEN_NAME + TEXT_NULL + KOMMA
						+ KEY_SCREEN_DATE + TEXT_NULL + KOMMA
						+ KEY_SCREEN_PREVIEW + TEXT
						+ ");"
						,
		
						CREATE_OBJECTS_TABLE 
						= CREATE
						+ TABLE_OBJECTS + " ("
						+ ID
						+ KEY_OBJECTS_SCREEN + INT_NULL + KOMMA
						+ OBJECT_PROPERTIES
						+ ");"
						,
		
						CREATE_PREVIEWS_TABLE
						= CREATE
						+ TABLE_PREVIEWS + " ("
						+ ID
						+ PREVIEWS_PROPERTIES
						+ ");"
						;
						
		//DROP COMMANDS
		private static final String 
						DROP_MAIN 
						= DROP 
						+ TABLE_SCREENS
						,
						
						DROP_OBJECTS 
						= DROP 
						+ TABLE_OBJECTS
						,
						
						DROP_PREVIEWS
						= DROP
						+ TABLE_PREVIEWS
						;
		
		
		
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
			db.execSQL(CREATE_PREVIEWS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL(DROP_MAIN);
			db.execSQL(DROP_OBJECTS);
			db.execSQL(DROP_PREVIEWS);
			
			onCreate(db);
		}
	}
}
