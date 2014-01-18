package com.swe.prototype.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExchangeContactTable {
	// Database table
	public static final String TABLE_CONTACTS = "contacts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_GIVENNAME = "firstname";
	public static final String COLUMN_SURNAME = "lastname";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PHONE = "phonenumber";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CONTACTS + "(" + COLUMN_ID
			+ " text not null, " + COLUMN_GIVENNAME
			+ " text not null, " + COLUMN_SURNAME + " text not null,"
			+ COLUMN_EMAIL + " text not null," + COLUMN_PHONE
			+ " text not null" + ")";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(ServerDataTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		onCreate(database);
	}
}
