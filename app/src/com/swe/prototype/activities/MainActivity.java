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

import android.R.string;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity";
	ProgressDialog dialog;

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

	private void initializeDialog() {
		dialog = ProgressDialog.show(MainActivity.this, "", "Bitte einen moment geduld...", true);
		dialog.show();
	}

	private void doAuthentication() {
		initializeDialog();
		String email = ((EditText) findViewById(R.id.input_email)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.input_password))
				.getText().toString();

		if (hasInternetConnection()) {
			new AuthenticateUserTask().execute(SERVER, email, password);
		} else {
			Log.v(TAG, "no internet");
			Log.v(TAG, "email: " + email);
			Log.v(TAG, "password: " + password);
		}
	}

	private void loginFailed() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("Username/Passwort falsch.");
		dlgAlert.setTitle("Login fehlgeschlagen");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	private void postLogin() {
		startActivity(new Intent(this, CalendarActivity.class));
		finish();
	}

	private class AuthenticateUserTask extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = params[2];
			//
			try {
				return authenticate(serverurl, username, password);
			} catch (IOException e) {
				// turn "IO EXCEPTION: " + e.getMessage();
			}
			return false;
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			Log.v(TAG, "Result authentication = " + result);
			if (result) {
				postLogin();
			} else {
				loginFailed();
			}
		}

		private boolean authenticate(String server, String email,
				String password) throws IOException {
			String authentification_url = server + "/user/authenticate";

			URL url = new URL(authentification_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("password", password));

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write(getQuery(params));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			// response code of 200 is accepted and 403 is failed.
			int response = conn.getResponseCode();
			Log.d(TAG, "The response is: " + response);

			return response == 200 ? true : false;
		}
	}
}