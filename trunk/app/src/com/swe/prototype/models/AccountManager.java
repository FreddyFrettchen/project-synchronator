package com.swe.prototype.models;

import java.util.ArrayList;

import com.swe.prototype.R;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.net.ExchangeAccount;
import com.swe.prototype.net.GoogleAccount;
import com.swe.prototype.net.server.ServerAccount;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class AccountManager {
	private String TAG = "AccountManager";
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);
	private String[] projection = { AccountTable.COLUMN_PROVIDER,
			AccountTable.COLUMN_USERNAME, AccountTable.COLUMN_PASSWORD };
	protected ArrayList<AccountBase> accounts;

	public AccountManager(ArrayList<AccountBase> accounts) {
		this.accounts = accounts;
	}
	
	public ArrayList<AccountBase> getAccounts(){
		return this.accounts;
	}

	// default constructor loads from database
	public AccountManager(Context context) {
		Cursor data = context.getContentResolver().query(CONTENT_URI, projection, null, null, null);
		this.accounts = new ArrayList<AccountBase>();

		// server account always exists
		this.accounts.add(new ServerAccount(context, Settings.getRefreshTimeAsInt(),
				"a@a.de", Security.sha1("123")));

		// add other accounts
		if (data.moveToFirst()) {
			do {
				Log.i(TAG,"Account type: "+data.getString(0));
				// check account type
				if (data.getString(0).equals("Google")) {
					this.accounts.add(new GoogleAccount(context, Settings
							.getRefreshTimeAsInt(), data.getString(1), data
							.getString(2)));
				} else if (data.getString(0).equals("Exchange")) {
					this.accounts.add(new ExchangeAccount(context, Settings
							.getRefreshTimeAsInt(), data.getString(1), data
							.getString(2)));
				}
			} while (data.moveToNext());
		}
		
		Log.i(TAG,this.accounts.size() + " accounts loaded.");
	}
}
