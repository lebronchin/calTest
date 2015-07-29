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
	public static final String DATA_COLOR = "dataColor";
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
			+ " TEXT, " + DATE_TIMESTAMP_END + " TEXT, " + DATA_COLOR+" TEXT, "+DATA_NOTE
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
		ContentValues values2 = new ContentValues();
		ContentValues values3 = new ContentValues();
			
		//7.15-7.18
		values.put("dataTitle", "Coding");
		values.put("dataColor", "#E6CAFF");
		values.put("dateYear", "2015");
		values.put("dateMonth", "6");
		values.put("dateDay", "15");
		values.put("dateTimeStamp", "1436889900134");
		values.put("dateTimeStampEnd", "1437149100134");
		//7.16-7.17
		values2.put("dataTitle", "Meeting");
		values2.put("dataColor", "#FFBFFF");
		values2.put("dateYear", "2015");
		values2.put("dateMonth", "6");
		values2.put("dateDay", "16");
		values2.put("dateTimeStamp", "1436976500134");
		values2.put("dateTimeStampEnd", "1437064400999");
		//7.17-7.20
		values3.put("dataTitle", "Hiking");
		values3.put("dataColor", "#FF9797");
		values3.put("dateYear", "2015");
		values3.put("dateMonth", "6");
		values3.put("dateDay", "17");
		values3.put("dateTimeStamp", "1437062900134");
		values3.put("dateTimeStampEnd", "1437321601134");
		
		db.insert("plannerDB", null, values);
		db.insert("plannerDB", null, values2);
		db.insert("plannerDB", null, values3);
		
		
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
