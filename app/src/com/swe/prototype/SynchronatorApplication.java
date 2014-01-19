package com.swe.prototype;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.models.AccountManager;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Extends Basic functionality of the standard 
 * application class.
 * @author batman
 *
 */
public class SynchronatorApplication extends Application {

	private static String TAG = "SynchronatorApplication";

	// holds all accounts
	private AccountManager accountManager = null;
	
	// service that runs the synchronisation
	private ScheduledFuture<?> synchronizeThread = null;
	private ScheduledExecutorService scheduleTaskExecutor = null;

	private Note current_note = null;
	private Contact current_contact = null;
	private CalendarEntry current_calendar_entry = null;
	private ArrayList<CalendarEntry> current_calendar_entry_list = null;

	// asyc task that synchronizes all accounts
	private Runnable SYNCHRONIZE_RUNNER = new Runnable() {
		public void run() {
			accountManager.synchronizeAll();
		}
	};

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
		SharedPreferences settings = getPreferences();
		return settings.getString("email", null) != null
				&& settings.getString("password", null) != null;
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
				SYNCHRONIZE_RUNNER, 0, Settings.getRefreshTimeAsInt(),
				TimeUnit.SECONDS);
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

	public void setCurrentContact(Contact c) {
		this.current_contact = c;
	}

	public void setCurrentNote(com.swe.prototype.models.Note c) {
		this.current_note = c;
	}

	public void setCurrentCalendarEntry(CalendarEntry ce) {
		this.current_calendar_entry = ce;
	}

	public ArrayList<CalendarEntry> getCurrentCalendarEntryList() {
		return current_calendar_entry_list;
	}

	public void setCurrentCalendarEntryList(ArrayList<CalendarEntry> ccel) {
		this.current_calendar_entry_list = ccel;
	}

	public Contact getCurrentContact() {
		return this.current_contact;
	}

	public Note getCurrentNote() {
		return this.current_note;
	}

	public CalendarEntry getCurrentCalendarEntry() {
		return this.current_calendar_entry;
	}
}
