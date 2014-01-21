package com.swe.prototype.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountTable {
	// Database table
	public static final String TABLE_ACCOUNT = "accounts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SYNCHRONATOR_USERNAME = "synchronator_username";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_PROVIDER = "provider";
	public static final String COLUMN_LAST_SYNC = "last_sync";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ACCOUNT + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_USERNAME
			+ " text not null, " + COLUMN_PASSWORD + " text not null,"
			+ COLUMN_SYNCHRONATOR_USERNAME + " text not null,"
			+ COLUMN_PROVIDER + " text not null," + COLUMN_LAST_SYNC
			+ " text not null" + ")";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(ServerDataTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
		onCreate(database);
	}
}
