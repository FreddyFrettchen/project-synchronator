package com.swe.prototype.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost.Settings;
import com.swe.prototype.globalsettings.*;
import com.swe.prototype.models.AccountBase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.swe.prototype.R;

public class SettingsActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "SettingsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final EditText refreshTime = (EditText) findViewById(R.id.edittext_refreshingtime);
		refreshTime.setText(""
				+ com.swe.prototype.globalsettings.Settings
						.getRefreshTimeAsFloat());
		Button save = (Button) findViewById(R.id.save_button);
		Button cancel = (Button) findViewById(R.id.cancel_button);
		Button refresh = (Button) findViewById(R.id.button_refresh);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				returnToLastActivity();

			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					float f = Float.parseFloat(refreshTime.getText().toString());
					if(f <= 0.0) {
						Toast.makeText(getApplicationContext(),"Refreshtime <= 0.0 not allowed!", Toast.LENGTH_LONG).show();
						refreshTime.setText("" + com.swe.prototype.globalsettings.Settings.getRefreshTimeAsFloat());
					}else {
						com.swe.prototype.globalsettings.Settings.setRefreshTimeAsFloat(f);
						saveRefreshTimePerm(f);
						returnToLastActivity();
					}
				} catch (Exception e) {
					System.out.println("Refreshing time wrong format");
				}

			}

			private void saveRefreshTimePerm(float f) {
				SharedPreferences pref = getSharedPreferences(
						com.swe.prototype.globalsettings.Settings
								.getPrefs_name(), 0);
				SharedPreferences.Editor editor = pref.edit();
				String email = pref.getString("email", "");
				editor.putFloat("refreshTime-" + email, f);
				editor.commit();
			}
		});
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hasInternetConnection()) {
					final ProgressDialog dialog;
					dialog = ProgressDialog.show(SettingsActivity.this, "",getString(R.string.atsync), true);
					dialog.show();
					new MyRefresher() {
						protected void onPostExecute(Boolean result) {
							dialog.dismiss();
							//showToast("Refreshing all accounts...");
						}
					}.execute();
				} else {
					Log.v(TAG, "no internet");
					Toast.makeText(getApplicationContext(),
							R.string.no_internet, Toast.LENGTH_LONG).show();
					// initializeDialog("no internet connection"+R.string.no_internet,true);
				}
			}
		});

	}

	private void returnToLastActivity() {
		finish();
	}

	protected void createAccount() {
		Intent intent = new Intent(SettingsActivity.this,
				CreateAccountActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void addClicked() {
		this.createAccount();
	}
	
	class MyRefresher extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			//onClickRefresh(getCurrentFocus());
			accounts.synchronizeAll();
			return true;
		}
	}
}
