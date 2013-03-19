package data;

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

public class ToDoContent extends ContentProvider
{
	public static final Uri CONTENT_URI = Uri.parse("content://sascha.krause.todoprovider/items");
	private TheDataHelper dataHelper;
	
	//prio constants
	final static int LOW = 1;
	final static int HIGH = 2;
	
	//table column constants
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_DATE = "date";
	public static final String KEY_DONE = "done";
	public static final String KEY_IMPORTANT = "important";
	
	//uri match constants
	private static final int ONE = 1;
	private static final int ALL = 2;
	private static final UriMatcher match;
	static 
	{
		match = new UriMatcher(UriMatcher.NO_MATCH);
		match.addURI("sascha.krause.todoprovider", "items", ALL);
		match.addURI("sascha.krause.todoprovider", "items/#", ONE);
	}


	@Override
	public boolean onCreate()
	{
		dataHelper = new TheDataHelper (getContext(), TheDataHelper.DB_NAME, null, TheDataHelper.DB_VERSION);
		return true;
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selArgs)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();
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
		
		int deleteCount = db.delete(TheDataHelper.DB_TABLE, selection, selArgs);
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

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		
		String nullColumnHack = null;
		
		long id = db.insert(TheDataHelper.DB_TABLE, nullColumnHack, values);
		
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
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		
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
		query.setTables(TheDataHelper.DB_TABLE);
		
		Cursor cursor = query.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectArgs)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();

		switch (match.match(uri))
		{
		case ONE:
			String row = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + row + (!TextUtils.isEmpty(selection) ? " AND (" + selection +')' : "");
		default: break;
		}
		
		int updateCount = db.update(TheDataHelper.DB_TABLE, values, selection, selectArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

	private static class TheDataHelper extends SQLiteOpenHelper
	{	
		private static final String DB_NAME = "toDoItems.db";
		public static final String DB_TABLE = "items";
		private static final int DB_VERSION = 3;
		
		private static final String CREATE = 
						"create table " 
						+ DB_TABLE + " ("
						+ KEY_ID + " integer primary key autoincrement, " 
						+ KEY_CONTENT + " text not null, " 
						+ KEY_DATE + " text not null, "
						+ KEY_DONE + " text not null, "
						+ KEY_IMPORTANT + " integer not null);";
		
		private static final String DROP =
						"DROP TABLE " + DB_TABLE;
		
		public TheDataHelper(Context context, String name, CursorFactory factory,
				int version)
		{
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL(DROP);
			onCreate(db);
		}

	}
}
