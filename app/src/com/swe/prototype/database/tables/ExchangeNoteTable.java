package com.swe.prototype.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExchangeNoteTable {
	// Database table
		public static final String TABLE_NOTES = "note";
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_SUBJECT = "title";
		public static final String COLUMN_BODY = "description";

		// Database creation SQL statement
		private static final String DATABASE_CREATE = "create table "
				+ TABLE_NOTES + "(" + COLUMN_ID
				+ " text not null, " + COLUMN_SUBJECT
				+ " text not null, " + COLUMN_BODY + " text not null" + ")";

		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		public static void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(ServerDataTable.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
			onCreate(database);
		}
}
