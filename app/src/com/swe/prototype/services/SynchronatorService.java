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
		Log.i(TAG, "Starting Syncthreads on "
				+ this.accounts.getAccounts().size() + " accounts.");
		ArrayList<AccountBase> accounts = this.accounts.getAccounts();
		for (int i = 0; i < accounts.size(); ++i) {
			accounts.get(i).start();
		}

		try {
			for (int i = 0; i < accounts.size(); ++i) {
				accounts.get(i).join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "Stopping Service");
		this.stopSelf();
	}

	// load all accounts and start their synchronisation threads
	public int onStartCommand(Intent intent, int flags, int startId) {
		return startId;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
