package data;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
import helpers.Log;

/**
 * ContentProvider SKELETON from
 * @see WROX - Professional Android 4 Application Development, Reto Meier
 * pimped by @author funklos to support and manage multiple tables and respective queries, inserts and delete operations.
 * 
 * The provider also supports bulkInsert methods for efficient object insertion processing.
 * The provider is running in his own thread and supports async access.
 *
 * grants access to the database tables via specific URI's, which are mapped to related tables.
 */
public class ScreenProvider extends ContentProvider
{
	//accesing loaders should use these constants as identifiers.
	public static final int 
					LOADER_ID_SCREENS = 0x101ff,	
					LOADER_ID_OBJECTS = 0x102ff,
					LOADER_ID_PROJECTS = 0x103ff,
					LOADER_ID_SECTIONS = 0x104ff
					;
	
	private static final String 
					AUTHORITY = "de.ur.rk.uibuilder",
					PREFIX = "content://",
					SCREENS_URI = "screen",
					OBJECTS_URI = "object",
					PROJECTS_URI = "project",
					SECTIONS_URI = "section"
					;
	
	//URI definition for access
	public static final Uri 
					CONTENT_URI_SCREENS = Uri.parse(PREFIX + AUTHORITY + "/" + SCREENS_URI),
					CONTENT_URI_OBJECTS = Uri.parse(PREFIX + AUTHORITY + "/" + OBJECTS_URI),
					CONTENT_URI_PROJECTS = Uri.parse(PREFIX + AUTHORITY + "/" + PROJECTS_URI),
					CONTENT_URI_SECTIONS = Uri.parse(PREFIX + AUTHORITY + "/" + SECTIONS_URI)
					;
	
	private DataManager data;
	private ContentResolver innerRes;
	
	
	//uri match constants
	private static final int 
					SCREENS_SINGLE = 0x01,
					SCREENS_ALL = 0x02,
					
					OBJECTS_SINGLE = 0x03,
					OBJECTS_ALL = 0x04,
					
					PROJECTS_SINGLE = 0x05,
					PROJECTS_ALL = 0x06,
					
					SECTIONS_SINGLE = 0x07,
					SECTIONS_ALL = 0x08
					;
	
	
	private static final UriMatcher matcher;
	static 
	{
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		matcher.addURI(AUTHORITY, SCREENS_URI, SCREENS_ALL);
		matcher.addURI(AUTHORITY, SCREENS_URI + "/#", SCREENS_SINGLE);
		
		matcher.addURI(AUTHORITY, OBJECTS_URI, OBJECTS_ALL);
		matcher.addURI(AUTHORITY, OBJECTS_URI + "/#", OBJECTS_SINGLE);
		
		matcher.addURI(AUTHORITY, PROJECTS_URI, PROJECTS_ALL);
		matcher.addURI(AUTHORITY, PROJECTS_URI + "/#", PROJECTS_SINGLE);
		
		matcher.addURI(AUTHORITY, SECTIONS_URI, SECTIONS_ALL);
		matcher.addURI(AUTHORITY, SECTIONS_URI + "/#", SECTIONS_SINGLE);
	}


	@Override
	public boolean onCreate()
	{
		innerRes = getContext().getContentResolver();
		data = new DataManager(getContext(), DataManager.DB_NAME, null, DataManager.DB_VERSION);
		return true;
	}
	


	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		String nullColumnHack = null;
		
		Log.d("insert as", String.valueOf(matcher.match(uri)));	
		Log.d("insert", uri.toString());
		
		long id;
		Uri inserted = null;
		
		switch (matcher.match(uri))
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
			Log.d("inserted screenId",String.valueOf(values.getAsLong(ScreenProvider.KEY_OBJECTS_SCREEN)));
			
			id = db.insert(DataManager.TABLE_OBJECTS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_OBJECTS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
			
		case PROJECTS_ALL:
			
			id = db.insert(DataManager.TABLE_PROJECTS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_PROJECTS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;
			
		case SECTIONS_ALL:
			
			id = db.insert(DataManager.TABLE_SECTIONS, nullColumnHack, values);
			
			if (id > -1)
			{
				inserted = ContentUris.withAppendedId(CONTENT_URI_SECTIONS, id);
				getContext().getContentResolver().notifyChange(inserted, null);
			}
			break;

		}
		//breakpoint, remove when done
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
		
		switch (matcher.match(uri))
		{
			case SCREENS_SINGLE:
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_SCREENS);
				break;
				
			case SCREENS_ALL:
				
				sortOrder = KEY_ID + " DESC";
				//query.appendWhere(KEY_SCREEN_ASSOCIATED_PROJECT + "=" + selection);
				Log.d("selection is", selection);
				query.setTables(DataManager.TABLE_SCREENS);
				break;
				
			case OBJECTS_SINGLE:
				
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_OBJECTS);
				break;
				
			case OBJECTS_ALL:
				
				sortOrder = KEY_OBJECTS_VIEW_ZORDER + " ASC";
				query.setTables(DataManager.TABLE_OBJECTS);
				break;
				
			case PROJECTS_SINGLE:
				
				row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
				
				query.setTables(DataManager.TABLE_PROJECTS);
				break;
				
			case PROJECTS_ALL:
				
				sortOrder = KEY_ID + " DESC";
				
				//String tables = DataManager.TABLE_PROJECTS + " LEFT OUTER JOIN " + DataManager.TABLE_SECTIONS + " ON (" + DataManager.TABLE_SECTIONS + "." + KEY_SECTION_ASSOCIATED_PROJECT + " = " + DataManager.TABLE_PROJECTS + "." + KEY_ID + ")";
				//query.setTables(tables);
				
				query.setTables(DataManager.TABLE_PROJECTS);
				break;
				
			case SECTIONS_ALL:
				
				sortOrder = KEY_ID + " DESC";
				 
				query.setTables(DataManager.TABLE_SECTIONS);
				break;

		}
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		Log.d("provider cursor", String.valueOf(cursor.getCount()));
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectArgs)
	{
		SQLiteDatabase db = data.getWritableDatabase();

		Log.d("provider update", "called");
		
		String row;
		int updateCount = 0;
		
		row = uri.getPathSegments().get(1);
		selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
		
		switch (matcher.match(uri))
		{
		case SCREENS_SINGLE:
			
			updateCount = db.update(DataManager.TABLE_SCREENS, values, selection, selectArgs);
			break;
			
		case OBJECTS_SINGLE:
			
			updateCount = db.update(DataManager.TABLE_OBJECTS, values, selection, selectArgs);
			break;
		
		case PROJECTS_SINGLE:
			
			updateCount = db.update(DataManager.TABLE_PROJECTS, values, selection, selectArgs);
			break;
			
		case SECTIONS_SINGLE:
			
			updateCount = db.update(DataManager.TABLE_SECTIONS, values, selection, selectArgs);
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
		
		switch (matcher.match(uri))
		{
		case SCREENS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			Log.d("database screens single delete row", row);
			
			deletePreviewImage(uri, row, innerRes);
			deleteObjects(row, innerRes);
			
			deleteCount = db.delete(DataManager.TABLE_SCREENS, selection, selArgs);
			break;
			
		case SCREENS_ALL:
			
			innerRes.delete(CONTENT_URI_OBJECTS, null, selArgs);
			deleteCount = db.delete(DataManager.TABLE_SCREENS, selection, selArgs);
			Log.d("database screens all deletecount", String.valueOf(deleteCount));
			break;
			
		case OBJECTS_ALL:

			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			Log.d("objects all deletecount:", String.valueOf(deleteCount));
			break;
			
		case OBJECTS_SINGLE:
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");

			Log.d("database delete objects single was called with", row);
			deleteCount = db.delete(DataManager.TABLE_OBJECTS, selection, selArgs);
			break;
			
		case PROJECTS_SINGLE:
			
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");

			Log.d("database delete projects single was called with row", row);
			
			innerRes.delete(CONTENT_URI_SECTIONS, KEY_SECTION_ASSOCIATED_PROJECT + "=" + row, null);
			deleteCount = db.delete(DataManager.TABLE_PROJECTS, selection, selArgs);		
			break;
			
		case SECTIONS_ALL:
			
			Cursor sections = innerRes.query(CONTENT_URI_SECTIONS, null, selection, null, null);
			
			int sectionIdIdx = sections.getColumnIndexOrThrow(KEY_ID);
			while(sections.moveToNext())
			{
				Log.d("sections all deleting single row", String.valueOf(sections.getInt(sectionIdIdx)));
				Uri sectionUriSingle = ContentUris.withAppendedId(CONTENT_URI_SECTIONS, sections.getInt(sectionIdIdx));
				innerRes.delete(sectionUriSingle, null, null);
			}
			
			deleteCount = db.delete(DataManager.TABLE_SECTIONS, selection, null);
			Log.d("sections all delete count", String.valueOf(deleteCount));
			break;
			
		case SECTIONS_SINGLE:
			
			row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
			
			Log.d("database delete section single was called with row", row);	
			
			innerRes.delete(CONTENT_URI_SCREENS, KEY_SCREEN_ASSOCIATED_SECTION + " = " + row, null);
			
			deleteCount = db.delete(DataManager.TABLE_SECTIONS, selection, selArgs);
			break;
		
		default: break;
		}
		
		if (selection == null)
		{
			selection = "1";
		}
		
		innerRes.notifyChange(uri, null);
		return deleteCount;
	}
	

	/**
	 * efficient insertion of contentvalues[]'s
	 */

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		String nullColumnHack = null;

		Log.d("bulk insert", uri.toString());
		
		long id = 0;
		int count = 0;
		Uri inserted = null;
		
		switch (matcher.match(uri))
		{
		case OBJECTS_ALL:
			
			Log.d("bulk ", String.valueOf(values.length));
			
			for (ContentValues contentValues : values)
			{
				Log.d("bulk", "for each");
				
				id = db.insert(DataManager.TABLE_OBJECTS, nullColumnHack, contentValues);
				
				if (id > -1)
				{
					inserted = ContentUris.withAppendedId(CONTENT_URI_OBJECTS, id);
					getContext().getContentResolver().notifyChange(inserted, null);
				}
				count++;
			}
			break;
			
		case SECTIONS_ALL:
			
			Log.d("bulk ", String.valueOf(values.length));
			
			for (ContentValues contentValues : values)
			{
				Log.d("bulk", "for each");
				
				id = db.insert(DataManager.TABLE_SECTIONS, nullColumnHack, contentValues);
				
				if (id > -1)
				{
					inserted = ContentUris.withAppendedId(CONTENT_URI_SECTIONS, id);
					getContext().getContentResolver().notifyChange(inserted, null);
				}
				count++;
			}
			break;
		}
		
		
		return count;
	}


	/**
	 * deletes all corresponding objects when delete was called for a screen
	 * @param row
	 * @param innerRes
	 */
	private void deleteObjects(String row, ContentResolver innerRes)
	{
		String selection = KEY_OBJECTS_SCREEN + "=" + "'" + String.valueOf(row) + "'";
		
		innerRes.delete(CONTENT_URI_OBJECTS, selection, null);
	}



	/**
	 * deletes the preview image from the /previews folder
	 * @param selection
	 * @param db
	 */
	private void deletePreviewImage(Uri uri, String row, ContentResolver innerRes)
	{
		String selection = KEY_ID + "=" + row;
		
		Cursor c = innerRes.query(uri, new String[]{ KEY_SCREEN_PREVIEW }, selection, null, null);
		
		c.moveToFirst();
		
		int imageIdx = c.getColumnIndexOrThrow(KEY_SCREEN_PREVIEW);	
		String imagePath = c.getString(imageIdx);
		
		if (!imagePath.equalsIgnoreCase("0"))
		{
			File temp = new File(imagePath);
			temp.delete();
			Log.d("image", "DELETED!");
		}
	}

	@Override
	public String getType(Uri uri)
	{
		switch (matcher.match(uri))
		{
		case SCREENS_ALL:
			return "vnd.android.cursor.dir/vnd.uibuilder.screens";
		
		case SCREENS_SINGLE:
			return "vnd.android.cursor.item/vnd.uibuilder.screens";
			
		case OBJECTS_ALL:
			return "vnd.android.cursor.dir/vnd.uibuilder.objects";
		
		default: throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
	}
	
	
	
	
	
	
	
	
	
	//table column constants
		public static final String KEY_ID = "_id";
		
		//PROJECTS TABLE
		public static final String
						KEY_PROJECTS_NAME = "projectname",
						KEY_PROJECTS_DATE = "projectcreation",
						KEY_PROJECTS_DESCRIPTION = "description",
						KEY_PROJECTS_PARSE_ID = "parseId",
						KEY_PROJECTS_SHARED = "shared"
						;
		
		//SCREENS TABLE
		public static final String 
						KEY_SCREEN_NAME = "name", 
						KEY_SCREEN_DATE = "date",
						KEY_SCREEN_PREVIEW = "preview",
						KEY_SCREEN_ASSOCIATED_SECTION ="associatedsection"
						;
		
		//SECTION TAGBLE
		public static final String 
						KEY_SECTION_NAME = "section",
						KEY_SECTION_DESCRIPTION = "desc",
						KEY_SECTION_ASSOCIATED_PROJECT = "associatedproject"
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
						//KEY_OBJECTS_VIEW_BACKGROUNDCLR_EDIT = ObjectValues.BACKGROUND_EDIT,
						//KEY_OBJECTS_VIEW_BACKGROUNDCLR_PRESENTATION = ObjectValues.BACKGROUND_PRES,
						KEY_OBJECTS_VIEW_ZORDER = ObjectValues.ZORDER,
						KEY_OBJECTS_VIEW_BACKGROUND = ObjectValues.BACKGROUNDCOLOR
						;
		
		
		

	/**
	 * the database uses two tables, one to manage the user-generated screens and the other to manage the corresponding
	 * objects.
	 * database transactions are provided by the contentprovider.
	 * access to a specific table is granted via the related URI.
	 * @author funklos
	 *
	 */
	private static class DataManager extends SQLiteOpenHelper
	{	
		/*
		 * Database properties
		 */
		public static final String 
						DB_NAME = "uibuilder.db",
						TABLE_SCREENS = "screenManager",
						TABLE_OBJECTS = "objects",
						TABLE_PROJECTS = "projects",
						TABLE_SECTIONS = "sections"
						;
		
		
		private static final int DB_VERSION = 43;
		
		private static final String CREATE = "create table if not exists ";
		private static final String DROP = "DROP TABLE if exists ";	
		
		private static final String 
						TEXT_NNULL = " text not null", 
						INT_NNULL = " integer not null",
						TEXT = " text",
						INT = " integer",
						KOMMA = ", "
						;
		
		private static final String ID = KEY_ID + " integer primary key autoincrement, ";
		
		
		private static final String 
						OBJECT_PROPERTIES 
						= KEY_OBJECTS_VIEW_TYPE + INT_NNULL + KOMMA 
						+ KEY_OBJECTS_VIEW_XPOS + INT_NNULL + KOMMA
						+ KEY_OBJECTS_VIEW_YPOS + INT_NNULL + KOMMA
						+ KEY_OBJECTS_VIEW_WIDTH + INT_NNULL + KOMMA
						+ KEY_OBJECTS_VIEW_HEIGHT + INT_NNULL + KOMMA
						
						+ KEY_OBJECTS_VIEW_ALIGNMENT + INT + KOMMA
						//+ KEY_OBJECTS_VIEW_BACKGROUNDCLR_EDIT + INT + KOMMA
						//+ KEY_OBJECTS_VIEW_BACKGROUNDCLR_PRESENTATION + INT + KOMMA
						+ KEY_OBJECTS_VIEW_COLUMNS_NUM + INT + KOMMA
						+ KEY_OBJECTS_VIEW_CONTENT + INT + KOMMA
						+ KEY_OBJECTS_VIEW_FONTSIZE + INT + KOMMA
						+ KEY_OBJECTS_VIEW_ICNSRC + INT + KOMMA
						+ KEY_OBJECTS_VIEW_IMGSRC + TEXT + KOMMA
						+ KEY_OBJECTS_VIEW_LAYOUT + INT + KOMMA
						+ KEY_OBJECTS_VIEW_RATING + INT + KOMMA
						+ KEY_OBJECTS_VIEW_STARSNUM + INT + KOMMA
						+ KEY_OBJECTS_VIEW_USERTEXT + TEXT + KOMMA
						+ KEY_OBJECTS_VIEW_ZORDER + INT + KOMMA
						+ KEY_OBJECTS_VIEW_BACKGROUND + INT
						,
						
						SCREEN_PROPERTIES
						= KEY_SCREEN_NAME + TEXT_NNULL + KOMMA
						+ KEY_SCREEN_DATE + TEXT_NNULL + KOMMA
						+ KEY_SCREEN_PREVIEW + TEXT + KOMMA
						+ KEY_SCREEN_ASSOCIATED_SECTION + INT_NNULL
						,
						
						PROJECTS_PROPERTIES
						= KEY_PROJECTS_NAME + TEXT_NNULL + KOMMA
						+ KEY_PROJECTS_DATE + TEXT_NNULL + KOMMA
						+ KEY_PROJECTS_DESCRIPTION + TEXT + KOMMA
						+ KEY_PROJECTS_PARSE_ID + INT + KOMMA
						+ KEY_PROJECTS_SHARED + INT
						,
						
						SECTION_PROPERTIES
						= KEY_SECTION_NAME + TEXT_NNULL + KOMMA
						+ KEY_SECTION_DESCRIPTION + TEXT + KOMMA
						+ KEY_SECTION_ASSOCIATED_PROJECT + INT_NNULL 
						
						;

		
		//CREATE COMMANDS
		private static final String 
						CREATE_SCREENS_TABLE 
						= CREATE 
						+ TABLE_SCREENS + " ("
						+ ID 
						+ SCREEN_PROPERTIES
						+ ");"
						,
		
						CREATE_OBJECTS_TABLE 
						= CREATE
						+ TABLE_OBJECTS + " ("
						+ ID
						+ KEY_OBJECTS_SCREEN + INT_NNULL + KOMMA
						+ OBJECT_PROPERTIES
						+ ");"
						,
						
						CREATE_PROJECTS_TABLE
						= CREATE
						+ TABLE_PROJECTS + " ("
						+ ID
						+ PROJECTS_PROPERTIES
						+ ");"
						,
						
						CREATE_SECTION_TABLE
						= CREATE
						+ TABLE_SECTIONS + " ("
						+ ID
						+ SECTION_PROPERTIES
						+ ");"
						
						;
						
		//DROP COMMANDS
		private static final String 
						DROP_SCREENS 
						= DROP 
						+ TABLE_SCREENS
						,
						
						DROP_OBJECTS 
						= DROP 
						+ TABLE_OBJECTS
						,
						
						DROP_PROJECTS
						= DROP
						+ TABLE_PROJECTS
						,
						
						DROP_SECTIONS
						= DROP
						+ TABLE_SECTIONS
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
			db.execSQL(CREATE_PROJECTS_TABLE);
			db.execSQL(CREATE_SECTION_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.d("database upgraded to v.", String.valueOf(DB_VERSION));
			
			db.execSQL(DROP_SCREENS);
			db.execSQL(DROP_OBJECTS);
			db.execSQL(DROP_PROJECTS);
			db.execSQL(DROP_SECTIONS);
			
			onCreate(db);
		}
	}
}
