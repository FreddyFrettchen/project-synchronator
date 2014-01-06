package com.swe.prototype.database;

import java.util.ArrayList;

import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.models.server.EncryptedData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {

	public static String SERVERDATA_TABLE = "server_data";
	public static int SERVERDATA_TABLE_ID = 1;

	public DBTools(Context context) {
		super(context, "synchronator.db", null, 1);
	}

	@Override
	// create all tables here
	public void onCreate(SQLiteDatabase database) {
		ServerDataTable.onCreate(database);
		AccountTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldv, int newv) {
		ServerDataTable.onUpgrade(database, oldv, newv);
		AccountTable.onUpgrade(database, oldv, newv);
	}

	/**
	 * delete all data from the database
	 */
	public void purgeDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		onUpgrade(db, 1, 1);
	}
}
