package com.swe.prototype.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;
import com.swe.prototype.R;
import com.swe.prototype.R.id;
import com.swe.prototype.R.menu;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.Account;
import com.swe.prototype.models.AccountManager;
import com.swe.prototype.net.server.Server;
import com.swe.prototype.services.SynchronatorService;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.globalsettings.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public abstract class BaseActivity extends Activity {
	protected static final String TAG = "BaseActivity";

	// protected ArrayList<Account> accounts = null;
	protected AccountManager accounts = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isLoggedIn())
			accounts = new AccountManager(this);
		// startSyncService();
	}

	/*
	 * public void checkLogin(){ if( getClassTag().equals("MainActivity") )
	 * return; if (!(isLoggedIn() && isPublic())) { startActivity(new
	 * Intent(this, MainActivity.class)); finish(); } }
	 */

	/**
	 * Checks if we have logincreds saved for a user
	 * 
	 * @return logged in true/false
	 */
	protected boolean isLoggedIn() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(
				Settings.getPrefs_name(), 0);
		String email = settings.getString("email", null);
		String password = settings.getString("password", null);

		return email != null && password != null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_accounts:
			if (!(this instanceof AccountsActivity)) {
				show(AccountsActivity.class);
			}
			return true;
		case R.id.action_settings:
			if (!(this instanceof SettingsActivity)) {
				show(SettingsActivity.class);
			}
			return true;
		case R.id.action_contacts:
			if (!(this instanceof ListContactsActivity)) {
				show(ListContactsActivity.class);
			}
			return true;
		case R.id.action_calendar:
			if (!(this instanceof CalendarMonthViewActivity)) {
				show(CalendarMonthViewActivity.class);
			}
			return true;
		case R.id.action_notes:
			if (!(this instanceof ListNotesActivity)) {
				show(ListNotesActivity.class);
			}
			return true;
		case R.id.action_logout:
			if (!(this instanceof MainActivity)) {
				SharedPreferences pref = getSharedPreferences(
						Settings.getPrefs_name(), 0);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("email", null);
				editor.putString("password", null);
				editor.commit();
				show(MainActivity.class);
			}
			return true;
		case R.id.action_add: {
			this.addClicked();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void show(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}

	public void showAndFinish(Class<?> cls) {
		show(cls);
		finish();
	}

	/**
	 * checks if the device is connected to the Internet
	 * 
	 * @return
	 */
	protected boolean hasInternetConnection() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	protected void startSyncService() {
		// new DBTools(this).purgeDatabase();
		// startService(new Intent(this, SynchronatorService.class));
	}

	protected void stopSyncService() {

	}

	/*
	 * Diese Methode sollte von der jeweiligen Activity �berschrieben werden,
	 * um die Add funktion nutzen zu k�nnen.
	 */
	protected void addClicked() {

	}

}
