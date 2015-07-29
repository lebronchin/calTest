package com.example.test;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{
	public static final String TABLE_NAME = "plannerDB";

	public static final String KEY_ID = "_id";
	public static final String DATA_TITLE = "dataTitle";
	public static final String DATE_TIMESTAMP = "dateTimeStamp";
	public static final String DATE_TIMESTAMP_END = "dateTimeStampEnd";
	public static final String DATE_YEAR = "dateYear";
	public static final String DATE_MONTH = "dateMonth";
	public static final String DATE_DAY = "dateDay";
	public static final String DATE_HOUR = "dateHour";
	public static final String DATE_MIN = "dateMin";
	public static final String DATA_NOTE = "dataNote";
	public static final String CNT_NAME = "cntName";
	public static final String CNT_PHONE = "cntPhone";
	public static final String CNT_EMAIL = "cntEmail";
	public static final String MAP_NAME = "mapName";
	public static final String MAP_LAT = "mapLat";
	public static final String MAP_LNG = "mapLng";
	public static final String RMD_A_TIMESTAMP = "rmdA";
	public static final String RMD_B_TIMESTAMP = "rmdB";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

			DATA_TITLE + " TEXT NOT NULL, " + DATE_YEAR + " INTEGER, "
			+ DATE_MONTH + " INTEGER, " + DATE_DAY + " INTEGER, " + DATE_HOUR
			+ " INTEGER, " + DATE_MIN + " INTEGER, " + DATE_TIMESTAMP
			+ " TEXT, " + DATE_TIMESTAMP_END + " TEXT, " + DATA_NOTE
			+ " TEXT, " + CNT_NAME + " TEXT, " + CNT_PHONE + " TEXT, "
			+ CNT_EMAIL + " TEXT, " + MAP_NAME + " TEXT, " + MAP_LAT
			+ " TEXT, " + MAP_LNG + " TEXT, " + RMD_A_TIMESTAMP + " TEXT, "
			+ RMD_B_TIMESTAMP + " TEXT)";
	
	
	
	
	
	
	
    
    
	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
		ContentValues values = new ContentValues();
		values.put("dataTitle", "");
		values.put("dateTimeStamp", "");
		values.put("dateTimeStampEnd", "");
		
		
		
		
		db.insert("cust", null, values);
		//query(null);
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
