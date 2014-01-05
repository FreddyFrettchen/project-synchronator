
package com.swe.prototype.activities;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost.Settings;
import com.swe.prototype.globalsettings.*;
import com.swe.prototype.net.server.Server.AuthenticateUserTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swe.prototype.R;

public class SettingsActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "SettingsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final EditText refreshTime = (EditText)findViewById(R.id.edittext_refreshingtime);
		refreshTime.setText(""+com.swe.prototype.globalsettings.Settings.getRefreshTimeAsFloat());
		Button save = (Button) findViewById(R.id.save_button);
		Button cancle = (Button) findViewById(R.id.cancel_button);
		Button refresh = (Button) findViewById(R.id.button_refresh);
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				returnToLastActivity();
				
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					float f = Float.parseFloat(refreshTime.getText().toString());
					com.swe.prototype.globalsettings.Settings.setRefreshTimeAsFloat(f);
					saveRefreshTimePerm(f);
					returnToLastActivity(); 
				}
				catch(Exception e){
					System.out.println("Refreshing time falsches format");
				}
				
			}

			private void saveRefreshTimePerm(float f) {
				SharedPreferences pref = getSharedPreferences(com.swe.prototype.globalsettings.Settings.getPrefs_name(), 0);
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
					onClickRefresh(getCurrentFocus());
				} else {
					Log.v(TAG, "no internet");
					Toast.makeText(getApplicationContext(), 
		                    R.string.no_internet, Toast.LENGTH_LONG).show();
					//initializeDialog("no internet connection"+R.string.no_internet,true);
				}				
			}
		});

		
	}

	
	private void returnToLastActivity(){
		finish();
	}
	
	public void onClickRefresh(View v){
		accounts.refreshAllData();
	}
	
	
}
