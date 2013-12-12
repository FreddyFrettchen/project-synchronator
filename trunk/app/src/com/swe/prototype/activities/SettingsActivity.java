package com.swe.prototype.activities;

import android.os.Bundle;

import com.swe.prototype.R;

public class SettingsActivity extends BaseActivity {
	
	private static final String TAG = "SettingsActivity";
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_settings);
	    }
}
