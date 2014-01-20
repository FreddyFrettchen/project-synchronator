package com.swe.prototype.activities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.swe.prototype.R;
import com.swe.prototype.SynchronatorApplication;
import com.swe.prototype.models.AccountManager;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.globalsettings.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	private ScheduledExecutorService scheduleTaskExecutor;

	protected static final String TAG = "BaseActivity";
	protected AccountManager accounts = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scheduleTaskExecutor = Executors.newScheduledThreadPool(2);

		if (isLoggedIn()) {
			accounts = new AccountManager(this);
		}
	}
	
	protected boolean isLoggedIn() {
		return getSynchronatorApplication().isLoggedIn();
	}

	protected AccountManager getAccountManager() {
		return getSynchronatorApplication().getAccountManager();
	}

	public SynchronatorApplication getSynchronatorApplication() {
		return ((SynchronatorApplication) getApplication());
	}

	public SharedPreferences getPreferences(){
		return getSynchronatorApplication().getPreferences();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.purge_db_btn:
			new DBTools(this).purgeDatabase();
			return true;
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
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);

				// set title
				alertDialogBuilder.setTitle("Do you really want to Logout?");

				// set dialog message
				alertDialogBuilder
						// .setMessage("Do you really want to Logout?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										SharedPreferences pref = getSharedPreferences(
												Settings.getPrefs_name(), 0);
										SharedPreferences.Editor editor = pref
												.edit();
										editor.putString("email", null);
										editor.putString("password", null);
										editor.putInt("last_sync", 0);
										editor.commit();
										new DBTools(getApplicationContext()).purgeData();
										show(MainActivity.class);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
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
	
	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Diese Methode sollte von der jeweiligen Activity überschrieben werden, um
	 * die Add funktion nutzen zu können.
	 */
	protected void addClicked() {

	}

}
