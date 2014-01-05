package com.swe.prototype.models;

import java.util.ArrayList;
import java.util.Iterator;

import com.swe.prototype.R;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.net.ExchangeAccount;
import com.swe.prototype.net.GoogleAccount;
import com.swe.prototype.net.server.ServerAccount;
import com.swe.prototype.services.SynchronatorService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	protected ServerAccount server_account = null;
	protected Context context = null;

	public AccountManager(Context context, ArrayList<AccountBase> accounts,
			ServerAccount saccount) {
		this.accounts = accounts;
		this.server_account = saccount;
		this.context = context;
	}

	public ArrayList<AccountBase> getAccounts() {
		return this.accounts;
	}

	public ServerAccount getServerAccount() {
		return server_account;
	}

	// default constructor loads from database
	public AccountManager(Context context) {
		this.context = context;
		this.accounts = new ArrayList<AccountBase>();
		loadDynamicAccounts(context);
		loadServerAccount(context);
		this.accounts.add(this.server_account);
		Log.i(TAG, this.accounts.size() + " accounts loaded.");
	}

	public void loadDynamicAccounts(Context context) {
		Cursor data = context.getContentResolver().query(CONTENT_URI,
				projection, null, null, null);

		// add other accounts
		if (data.moveToFirst()) {
			do {
				Log.i(TAG, "Account type: " + data.getString(0));
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
	}

	public void loadServerAccount(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				Settings.getPrefs_name(), 0);
		String email = settings.getString("email", null);
		String password = settings.getString("password", null);
		Log.i(TAG,"email: " + email);
		Log.i(TAG,"password: " + password);
		this.server_account = new ServerAccount(context, Settings
				.getRefreshTimeAsInt(), email, Security.sha1(password));
	}
	
	public void refreshAllData(){
		context.startService(new Intent(context, SynchronatorService.class));
	}
}
