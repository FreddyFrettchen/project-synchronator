package com.swe.prototype.database;

import java.util.ArrayList;

import com.swe.prototype.models.server.EncryptedData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class SQLiteDataProvider extends ContentProvider {

	private static final String AUTHORITY = "com.swe.prototype.database.SQLiteDataProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	DBTools tools;

	private final static UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, DBTools.SERVERDATA_TABLE,
				DBTools.SERVERDATA_TABLE_ID);
	}

	@Override
	public int delete(Uri uri, String where, String[] args) {
		String table = getTableName(uri);
		SQLiteDatabase database = tools.getWritableDatabase();
		return database.delete(table, where, args);
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = getTableName(uri);
		SQLiteDatabase database = tools.getWritableDatabase();
		long value = database.insert(table, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
	}

	@Override
	public boolean onCreate() {
		tools = new DBTools(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = getTableName(uri);
		SQLiteDatabase database = tools.getReadableDatabase();

		Cursor cursor = database.query(table, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		String table = getTableName(uri);
		SQLiteDatabase database = tools.getWritableDatabase();
		getContext().getContentResolver().notifyChange(uri, null);
		return database.update(table, values, whereClause, whereArgs);
	}

	public static String getTableName(Uri uri) {
		String value = uri.getPath();
		value = value.replace("/", "");// we need to remove '/'
		return value;
	}
}
