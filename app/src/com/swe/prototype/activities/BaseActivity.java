package com.swe.prototype.activities;

import com.swe.prototype.R;
import com.swe.prototype.R.id;
import com.swe.prototype.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class BaseActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accounts:
			showAccounts();
			return true;
		case R.id.action_settings:
			showSettings();
			return true;
		case R.id.action_home:
			showHome();
			return true;
		case R.id.action_contacts:
			showListContacts();
			return true;
		case R.id.action_calendar:
			showCalendar();
			return true;
		case R.id.action_notes:
			showListNotes();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void showAccounts() {
		Intent myIntent = new Intent(this, AccountsActivity.class);
		startActivity(myIntent);
	}
	
	public void showHome() {
		Intent myIntent = new Intent(this, MainActivity.class);
		startActivity(myIntent);
	}
	
	public void showSettings() {
		Intent myIntent = new Intent(this, SettingsActivity.class);
		startActivity(myIntent);
	}

	public void showListContacts() {
		Intent myIntent = new Intent(this, ListContactsActivity.class);
		startActivity(myIntent);
	}

	public void showCalendar() {
		Intent myIntent = new Intent(this, CalendarActivity.class);
		startActivity(myIntent);
	}

	public void showListNotes() {
		Intent myIntent = new Intent(this, ListNotesActivity.class);
		startActivity(myIntent);
	}
}