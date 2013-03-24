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

public class DataBase extends ContentProvider
{
	private static final String AUTHORITY = "de.ur.rk.uibuilder";
	private static final String BASE = "/screens";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + BASE);
	private DataManager data;
	
	//prio constants
	final static int LOW = 1;
	final static int HIGH = 2;
	
	//table column constants
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DATE = "date";
	
	//uri match constants
	private static final int ONE = 1;
	private static final int ALL = 2;
	private static final UriMatcher match;
	
	static 
	{
		match = new UriMatcher(UriMatcher.NO_MATCH);
		match.addURI(AUTHORITY, BASE, ALL);
		match.addURI(AUTHORITY, BASE + "/#", ONE);
	}


	@Override
	public boolean onCreate()
	{
		data = new DataManager (getContext(), DataManager.DB_NAME, null, DataManager.DB_VERSION);
		return true;
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selArgs)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		switch (match.match(uri))
		{
		case ONE:
			String row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
		default: break;
		}
		if (selection == null)
		{
			selection = "1";
		}
		
		int deleteCount = db.delete(DataManager.DB_TABLE, selection, selArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (match.match(uri))
		{
		case ALL:
			return "vnd.android.cursor.dir/vnd.sascha.krause.toDo";
		
		case ONE:
			return "vnd.android.cursor.item/vnd.sascha.krause.toDo";
		default: throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
	}
	
	public void createNewScreen(String name)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		data.createNewScreen(db, name);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		String nullColumnHack = null;
		
		long id = db.insert(DataManager.DB_TABLE, nullColumnHack, values);
		
		if (id > -1)
		{
			Uri inserted = ContentUris.withAppendedId(CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(inserted, null);
			
			return inserted;
		}
		else
		return null;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		
		String groupBy = null;
		String having = null;
		sortOrder = KEY_DATE + " DESC";
		
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		
		switch (match.match(uri))
		{
			case ONE:
				String row = uri.getPathSegments().get(1);
				query.appendWhere(KEY_ID + "=" + row);
			default: break;
		}
		query.setTables(DataManager.DB_TABLE);
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectArgs)
	{
		SQLiteDatabase db = data.getWritableDatabase();

		switch (match.match(uri))
		{
		case ONE:
			String row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
		default: break;
		}
		
		int updateCount = db.update(DataManager.DB_TABLE, values, selection, selectArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

	private static class DataManager extends SQLiteOpenHelper
	{	
		private static final String DB_NAME = "uibuilder.db";
		public static final String DB_TABLE = "screenManager";
		private static final int DB_VERSION = 5;
		
		private static final String CREATE = "create table if not exists ";
		
		private static final String CREATE_SCREENS_TABLE = 
						CREATE 
						+ DB_TABLE + " ("
						+ KEY_ID + " integer primary key autoincrement, " 
						+ KEY_NAME + " text not null, " 
						+ KEY_DATE + " text not null);";
		
		private static final String OBJECT_PROPERTIES =
						ObjectValueCollector.ID + " integer not null"
						+ ObjectValueCollector.X_POS + " integer not null"
						+ ObjectValueCollector.Y_POS + " integer not null);"
						;
		
		private static final String DROP =
						"DROP TABLE " + DB_TABLE;
		
		public DataManager(Context context, String name, CursorFactory factory,
				int version)
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE_SCREENS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL(DROP);
			onCreate(db);
		}

		public void createNewScreen(SQLiteDatabase db, String screenName)
		{
			db.execSQL(CREATE + screenName + " (" + KEY_ID + " integer primary key autoincrement" + OBJECT_PROPERTIES);
		}
	}
}
