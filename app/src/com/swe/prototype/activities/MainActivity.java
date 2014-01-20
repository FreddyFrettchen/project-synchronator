package com.swe.prototype.activities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import com.swe.prototype.R;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.activities.SettingsActivity.MyRefresher;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Tools;
import com.swe.prototype.net.server.AsyncUserTask;
import com.swe.prototype.net.server.ServerAccount;

import android.R.string;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity";
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (isLoggedIn()) {
			showAndFinish(ListContactsActivity.class);
		}

		// buttons and listeners for login register
		Button btn_register = (Button) findViewById(R.id.button_register);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				show(RegisterActivity.class);
			}
		});

		Button btn_login = (Button) findViewById(R.id.button_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doAuthentication();
			}
		});
		// Laden des Servers aus den SharedPreferences wenn sie dort schon
		// abgespeichert sind
		SharedPreferences pref = getPreferences();
		if (pref.getString("Server_IP", "") != ""
				&& pref.getString("Server_Port", "") != "") {
			Settings.setServer(pref.getString("Server_IP", ""),
					pref.getString("Server_Port", ""));
		}
	}

	/*
	 * Die muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar ist
	 * und der user nicht am login vorbei kommt.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/*
	 * wird aufgerufen sobald user auf das imageview Serversettings clickt
	 */
	public void onClickServerSettings(View v) {
		AlertDialog.Builder ssAlert = new AlertDialog.Builder(this);
		LayoutInflater inflator = getLayoutInflater();
		View optionDialogView = inflator.inflate(
				R.layout.dialog_serversettings, null);
		final EditText ip = (EditText) optionDialogView
				.findViewById(R.id.editText_ip);
		final EditText port = (EditText) optionDialogView
				.findViewById(R.id.editText_port);
		ip.setText(Settings.getIp());
		port.setText(Settings.getPort());
		ssAlert.setView(optionDialogView);
		// final EditText ip = (EditText)
		// optionDialogView.findViewById(R.id.editText_ip);

		ssAlert.setCancelable(true);
		ssAlert.setTitle("Server Settings");
		ssAlert.setIcon(getResources().getDrawable(R.drawable.server_settings));
		ssAlert.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String newIP = ip.getText().toString();
						String newPort = port.getText().toString();
						if (Tools.isValidPort(newPort)
								&& (Tools.isValidIP(newIP) || Tools
										.isValidHost(newIP))) {
							Settings.setServer(newIP, newPort);
							SharedPreferences pref = getPreferences();
							SharedPreferences.Editor editor = getPreferences()
									.edit();
							editor.putString("Server_IP", newIP);
							editor.putString("Server_Port", newPort);
							editor.commit();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"Server Settings IP oder Port nicht valide!",
									Toast.LENGTH_LONG).show();
							System.out
									.println("Server Settings IP oder Port nicht valide!");
						}

					}
				});
		ssAlert.setNegativeButton("Cancel", null);
		ssAlert.setCancelable(true);
		ssAlert.create().show();

	}

	// wird bei login-versuch aufgerufen
	private void initializeDialog(String message) {
		dialog = ProgressDialog.show(MainActivity.this, "", message, true);
		dialog.show();
	}

	// wird aufgerufen, wenn der user auf Login-Button klickt
	private void doAuthentication() {

		final String email = ((EditText) findViewById(R.id.input_email))
				.getText().toString();
		final String password = ((EditText) findViewById(R.id.input_password))
				.getText().toString();

		if (hasInternetConnection()) {
			initializeDialog(getString(R.string.wait));
			new ServerAccount(this, 0, Settings.getRefreshTimeAsInt(), email,
					Security.sha1(password)).new AuthenticateUserTask(this) {
				@Override
				protected void onPostExecute(Boolean success) {
					super.onPostExecute(success);
					dialog.dismiss();
					if (success)
						postLogin(email, password);
					else
						loginFailed();
				}
			}.execute();
		} else {
			Toast.makeText(getApplicationContext(), R.string.no_internet,
					Toast.LENGTH_LONG).show();
		}
	}

	private void loginFailed() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("Username/Password wrong or not activated yet. Or maybe wrong Server?!");
		dlgAlert.setTitle(getString(R.string.login_failed));
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	/* Speichert logindaten und liesst refreshTime des Benutzers */
	private void postLogin(String email, String password) {
		SharedPreferences pref = getPreferences();
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putString("email", email);
		editor.putString("password", password);
		if (pref.getFloat("refreshTime-" + email, 0) > 0) {
			Settings.setRefreshTimeAsFloat(pref.getFloat(
					"refreshTime-" + email, 0));
		}
		editor.commit();
		getSynchronatorApplication().onApplicationLogin();

		//final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.wait), true);
		//dialog.show();
		new MyRefresher(this) {
			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				startActivity(new Intent(context, ListContactsActivity.class));
				finish();
			}
		}.execute();
	}

	class MyRefresher extends AsyncTask<Void, Void, Boolean> {
		protected Context context = null;
		protected ProgressDialog dialog = null;

		public MyRefresher(Context context) {
			this.context = context;
			this.dialog = ProgressDialog.show(context, "", getString(R.string.atsync), true);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// onClickRefresh(getCurrentFocus());
			dialog.show();
			getSynchronatorApplication().getAccountManager().synchronizeAll();
			//accounts.synchronizeAll();
			return true;
		}
	}
}
