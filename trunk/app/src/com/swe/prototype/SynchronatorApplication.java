package com.swe.prototype;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.models.AccountManager;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class SynchronatorApplication extends Application {

	private static String TAG = "SynchronatorApplication";

	private AccountManager accountManager = null;
	private ScheduledExecutorService scheduleTaskExecutor = null;
	private ScheduledFuture<?> synchronizeThread = null;

	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Booting Synchronator Application");

		// create threadpool for account sync
		scheduleTaskExecutor = Executors.newScheduledThreadPool(2);

		// load account if logged in
		if (isLoggedIn()) {
			Log.i(TAG, "Found login data.");
			onApplicationLogin();
		} else {
			Log.i(TAG, "No login data found.");
		}
	}

	public boolean isLoggedIn() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(
				Settings.getPrefs_name(), 0);

		String email = settings.getString("email", null);
		String password = settings.getString("password", null);

		return email != null && password != null;
	}

	public AccountManager getAccountManager() {
		return this.accountManager;
	}

	private void stopScheduler() {
		synchronizeThread.cancel(true);
	}

	private void startScheduler() {
		if (isSchedulerRunning())
			stopScheduler();
		synchronizeThread = scheduleTaskExecutor.scheduleAtFixedRate(
				new Runnable() {
					public void run() {
						accountManager.synchronizeAll();
					}
				}, 0, Settings.getRefreshTimeAsInt(), TimeUnit.SECONDS);
	}

	private boolean isSchedulerRunning() {
		return synchronizeThread != null;
	}

	public SharedPreferences getPreferences() {
		return getSharedPreferences(Settings.getPrefs_name(), 0);
	}

	public void onApplicationLogin() {
		accountManager = new AccountManager(this);
		startScheduler();
	}
}
