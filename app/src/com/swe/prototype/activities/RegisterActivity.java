package com.swe.prototype.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swe.prototype.R;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.net.server.ServerAccount;

public class RegisterActivity extends BaseActivity {

	private static final String TAG = "RegisterActivity";
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// buttons and listeners for register button
		Button btn_register = (Button) findViewById(R.id.register_button_register);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (hasInternetConnection()) {
					doRegistration();
				} else {
					Log.v(TAG, "no internet");
					Toast.makeText(getApplicationContext(),
							R.string.no_internet, Toast.LENGTH_LONG).show();
					// initializeDialog("no internet connection"+R.string.no_internet,true);
				}
			}
		});
	}

	/*
	 * muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar ist und
	 * der user nicht am registrieren vorbei kommt.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private void initializeDialog() {
		dialog = ProgressDialog.show(RegisterActivity.this, "",
				"Please wait a moment ...", true);
		dialog.show();
	}

	private void alertPasswordsNotMatching() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setTitle("Registration Failed");
		dlgAlert.setMessage("Unfortunately, the passwords do not match.");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dialog.dismiss();
		dlgAlert.create().show();
	}

	/**
	 * returns the content of a editText by id.
	 * 
	 * @param id
	 * @return
	 */
	private String getField(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void doRegistration() {
		initializeDialog();
		String email = getField(R.id.register_input_email);
		String password = getField(R.id.register_input_password);
		String password2 = getField(R.id.register_input_password_rep);

		if (!password.equals(password2)) {
			alertPasswordsNotMatching();
			return;
		}

		if (hasInternetConnection()) {
			new ServerAccount(this, 0, Settings.getRefreshTimeAsInt(), email,
					Security.sha1(password)).new RegisterUserTask(this) {
				@Override
				protected void onPostExecute(Boolean result) {
					dialog.dismiss();
					if (result)
						postRegister();
					else
						registerFailed();
				}
			}.execute();
		} else {
			Log.i(TAG, "No internet");
		}
	}

	private void registerFailed() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("E-mail address is not valid or already in use.");
		dlgAlert.setTitle("Registration Failed.");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	private void openLogin() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	private void postRegister() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("Congratulations! You can log in as soon as the Administrator approved your account.");
		dlgAlert.setTitle("Registration successful");
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				openLogin();
			}
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}
}
