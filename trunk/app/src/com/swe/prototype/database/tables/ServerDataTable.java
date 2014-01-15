package com.swe.prototype.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ServerDataTable {
	// Database table
	public static final String TABLE_SERVERDATA = "server_data";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ID_DATA = "data_id";
	public static final String COLUMN_DATA = "data";
	public static final String COLUMN_TAG = "tag";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_RESEND = "resend";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SERVERDATA + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ID_DATA
			+ " integer not null, " + COLUMN_DATA + " text not null,"
			+ COLUMN_TAG + " text not null," + COLUMN_STATUS
			+ " text not null," + COLUMN_RESEND + " text not null" + ")";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(ServerDataTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVERDATA);
		onCreate(database);
	}
}
