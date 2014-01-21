package com.swe.prototype.models;

import java.util.ArrayList;
import java.util.Iterator;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class AccountManager {
	private String TAG = "AccountManager";
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);
	private String[] projection = { AccountTable.COLUMN_ID,
			AccountTable.COLUMN_PROVIDER, AccountTable.COLUMN_USERNAME,
			AccountTable.COLUMN_PASSWORD };
	protected ArrayList<AccountBase> accounts;
	protected ServerAccount server_account = null;
	protected Context context = null;

	public AccountManager(Context context, ArrayList<AccountBase> accounts,
			ServerAccount saccount) {
		this.accounts = accounts;
		this.server_account = saccount;
		this.context = context;
	}

	// default constructor loads from database
	public AccountManager(Context context) {
		this.context = context;
		this.accounts = new ArrayList<AccountBase>();
		loadDynamicAccounts(context);
		loadServerAccount(context);
		this.accounts.add(this.server_account);
		Log.i(TAG, this.accounts.size() + " Accounts loaded.");
	}

	public void loadDynamicAccounts(Context context) {
		Cursor data = context.getContentResolver().query(CONTENT_URI,
				projection, null, null, null);

		// add other accounts
		if (data.moveToFirst()) {
			do {
				Log.i(TAG, "Account type: " + data.getString(0));
				// check account type
				if (data.getString(1).equals("Google")) {
					this.accounts.add(new GoogleAccount(context,
							data.getInt(0), Settings.getRefreshTimeAsInt(),
							data.getString(2), data.getString(3)));
				} else if (data.getString(1).equals("Exchange")) {
					this.accounts.add(new ExchangeAccount(context, data
							.getInt(0), Settings.getRefreshTimeAsInt(), data
							.getString(2), data.getString(3)));
				}
			} while (data.moveToNext());
		}
		data.close();
	}

	public void loadServerAccount(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				Settings.getPrefs_name(), 0);
		String email = settings.getString("email", null);
		String password = settings.getString("password", null);
		this.server_account = new ServerAccount(context, 0,
				Settings.getRefreshTimeAsInt(), email, password);
	}

	public void refreshAllData() {
		context.startService(new Intent(context, SynchronatorService.class));
	}

	public void synchronizeAll() {
		for (int i = 0; i < this.accounts.size(); i++) {
			this.accounts.get(i).synchronizeAll();
		}
	}

	public void synchronizeContacts() {
		for (int i = 0; i < this.accounts.size(); i++) {
			this.accounts.get(i).synchronizeContacts();
		}
	}

	public void synchronizeNotes() {
		for (int i = 0; i < this.accounts.size(); i++) {
			this.accounts.get(i).synchronizeNotes();
		}
	}

	public void synchronizeCalendarEntries() {
		for (int i = 0; i < this.accounts.size(); i++) {
			this.accounts.get(i).synchronizeCalendarEntries();
		}
	}

	public ArrayList<AccountBase> getAccounts() {
		return this.accounts;
	}

	public AccountBase getServerAccount() {
		return this.server_account;
	}

	public ArrayAdapter<AccountBase> getAccountsAdapter(int resource) {
		return new ArrayAdapter<AccountBase>(this.context,
				android.R.layout.simple_list_item_checked, this.accounts);
	}

	//should throw
	public AccountBase getAccountByTag(String tag, String username,
			String password) {
		if (tag.equals("Google")) {
			return new GoogleAccount(context, -1, Settings.getRefreshTimeAsInt(),
					username, password);
		} else if (tag.equals("Exchange")) {
			return new ExchangeAccount(context, -1, Settings.getRefreshTimeAsInt(),
					username, password);
		}
		return null;
	}
}
