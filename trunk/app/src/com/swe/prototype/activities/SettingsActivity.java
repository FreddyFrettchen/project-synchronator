
package com.swe.prototype.activities;
import android.media.audiofx.BassBoost.Settings;
import com.swe.prototype.globalsettings.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
					returnToLastActivity(); 
				}
				catch(Exception e){
					System.out.println("Refreshing time falsches format");
				}
				
			}
		});
		

		
	}

	
	private void returnToLastActivity(){
		finish();
	}
	
	public void onClickRefresh(View v){
		System.out.println("Refresh button gedrueckt");
		//muss noch implementiert werden
	}
	
	
}
