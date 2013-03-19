

package data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


//NOT IN USE


public class TheData extends SQLiteOpenHelper
{	
	
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_DATE = "date";
	public static final String KEY_DONE = "done";
	public static final String KEY_IMPORTANT = "important";
	
	public static final String[] all = {KEY_ID, KEY_CONTENT, KEY_DATE, KEY_DONE, KEY_IMPORTANT};
	
		public static final String DB_NAME = "toDoItems.db";
		public static final String DB_TABLE = "items";
		public static final int DB_VERSION = 3;
		
		private static final String CREATE = 
						"CREATE TABLE IF NOT EXISTS " 
						+ DB_TABLE + " ("
						+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
						+ KEY_CONTENT + " TEXT NOT NULL, " 
						+ KEY_DATE + " TEXT NOT NULL, "
						+ KEY_DONE + " TEXT NOT NULL, "
						+ KEY_IMPORTANT + " INTEGER)";
		
		private static final String DROP =
						"DROP TABLE " + DB_TABLE;
		
		public TheData(Context context, String name, CursorFactory factory,
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

