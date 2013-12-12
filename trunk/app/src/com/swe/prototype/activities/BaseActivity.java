package com.swe.prototype.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import com.swe.prototype.R;
import com.swe.prototype.R.id;
import com.swe.prototype.R.menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class BaseActivity extends Activity {

	protected static final String SERVER = "http://10.0.2.2:45678";

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
			show(AccountsActivity.class);
			return true;
		case R.id.action_settings:
			show(SettingsActivity.class);
			return true;
		case R.id.action_home:
			show(MainActivity.class);
			return true;
		case R.id.action_contacts:
			show(ListContactsActivity.class);
			return true;
		case R.id.action_calendar:
			show(CalendarActivity.class);
			return true;
		case R.id.action_notes:
			show(ListNotesActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void show(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}
	
	/**
	 * checks if the device is connected to the Internet
	 * @return
	 */
	protected boolean hasInternetConnection(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**
	 * urlencodes list of NameValuePairs
	 * 
	 * @param params
	 * @return urlencoded querystring
	 * @throws UnsupportedEncodingException
	 */
	String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
