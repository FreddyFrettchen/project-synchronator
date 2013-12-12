package com.swe.prototype.activities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.swe.prototype.R;
import com.swe.prototype.net.server.Server;
import com.swe.prototype.net.server.Server.AuthenticateUserTask;

public class AccountsActivity extends BaseActivity {

	private static final String TAG = "AccountsActivity";
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		// buttons and listeners for register button
		Button btn_register = (Button) findViewById(R.id.register_button_register);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doRegistration();
			}
		});
	}

	private void initializeDialog() {
		dialog = ProgressDialog.show(AccountsActivity.this, "",
				"Bitte einen moment geduld...", true);
		dialog.show();
	}

	private void alertPasswordsNotMatching() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setTitle("Registrierung fehlgeschlagen");
		dlgAlert.setMessage("Die Passwörter stimmen leider nicht überein.");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
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
			Server server = new Server();
			server.new RegisterUserTask() {
				@Override
				protected void onPostExecute(Boolean result) {
					dialog.dismiss();
					if (result)
						postRegister();
					else
						registerFailed();
				}
			}.execute(SERVER, email, password);
		} else {
			Log.i(TAG, "No internet");
		}
	}

	private void registerFailed() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("E-Mail Adresse nicht valide oder schon vergeben.");
		dlgAlert.setTitle("Registrierung fehlgeschlagen");
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

		dlgAlert.setMessage("Nachdem Ihr Account vom Admin freigeschaltet wurde, können Sie sich einloggen.");
		dlgAlert.setTitle("Registrierung erfolgreich");
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				openLogin();
			}
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}
}
