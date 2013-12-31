package com.swe.prototype.services;

import java.util.ArrayList;

import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.AccountManager;
import com.swe.prototype.net.ExchangeAccount;
import com.swe.prototype.net.GoogleAccount;
import com.swe.prototype.net.server.ServerAccount;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SynchronatorService extends Service {
	
	private final static String TAG = "SynchronatorService";
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);
	private String[] projection = { AccountTable.COLUMN_ID,
			AccountTable.COLUMN_USERNAME, AccountTable.COLUMN_PASSWORD,
			AccountTable.COLUMN_PROVIDER };
	private AccountManager accounts = null;

	@Override
	public void onCreate() {
		super.onCreate();
		this.accounts = new AccountManager(this);
		Log.i(TAG, "Starting syncthreads on " + this.accounts.getAccounts().size() + " accounts.");
		ArrayList<AccountBase> accounts = this.accounts.getAccounts();
		for (int i = 0; i < accounts.size(); ++i) {
			accounts.get(i).start();
		}
	}

	// load all accounts and start their synchronisation threads
	public int onStartCommand(Intent intent, int flags, int startId) {
		return startId;
	}

	public ArrayList<AccountBase> getAccounts() {
		ArrayList<AccountBase> accounts = new ArrayList<AccountBase>();
		// server is added manually. the other accounts are loaded
		accounts.add(new ServerAccount(this, 100, "a@a.de", Security
				.sha1("123")));

		// load external accounts
		SQLiteDataProvider provider = new SQLiteDataProvider();
		Cursor cursor = provider.query(CONTENT_URI, projection, null, null,
				null);
		if (cursor.moveToFirst()) {
			String username = null;
			String password = null;
			String prov = null;
			do {
				username = cursor.getString(1);
				password = cursor.getString(2);
				prov = cursor.getString(3);
				if (prov.equals("google")) {
					accounts.add(new GoogleAccount(this, Settings
							.getRefreshTime(), username, password));
				} else if (prov.equals("exchange")) {
					accounts.add(new ExchangeAccount(this, Settings
							.getRefreshTime(), username, password));
				}
			} while (cursor.moveToNext());
		}

		return accounts;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
