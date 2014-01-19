package com.swe.prototype.services;

import java.util.ArrayList;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.AccountManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Service to Sync data from all accounts
 * without the need for the app to be in foreground.
 * 
 * @author batman
 *
 */
public class SynchronatorService extends Service {

	private final static String TAG = "SynchronatorService";
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

	// load all accounts and start their synchronization threads
	public int onStartCommand(Intent intent, int flags, int startId) {
		return startId;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
