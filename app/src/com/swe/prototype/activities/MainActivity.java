package com.swe.prototype.activities;

import com.swe.prototype.R;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity";
	
	// test
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// buttons and listeners for login register
		Button btn_register = (Button) findViewById(R.id.button_register);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchToAccounts();
			}
		});

		Button btn_login = (Button) findViewById(R.id.button_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doAuthentication();
			}
		});
	}

	private void switchToAccounts() {
		Intent myIntent = new Intent(this, AccountsActivity.class);
		startActivity(myIntent);
	}

	private void doAuthentication() {
		String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
		String password = ((EditText)findViewById(R.id.input_password)).getText().toString();
		/*Log.v(TAG, "email: " + email);
		Log.v(TAG, "password: " + password);*/
		
		// login hier
	}

}