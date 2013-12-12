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
import com.swe.prototype.net.server.AsyncUserTask;
import com.swe.prototype.net.server.Server;

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
		startActivity(new Intent(this, AccountsActivity.class));
	}

	private void initializeDialog() {
		dialog = ProgressDialog.show(MainActivity.this, "",
				"Bitte einen moment geduld...", true);
		dialog.show();
	}

	private void doAuthentication() {
		initializeDialog();
		String email = ((EditText) findViewById(R.id.input_email)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.input_password))
				.getText().toString();

		if (hasInternetConnection()) {
			Server server = new Server();
			server.new AuthenticateUserTask(){
				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					dialog.dismiss();
					if (result)
						postLogin();
					else
						loginFailed();
				}
			}.execute(SERVER, email, password);
		} else {
			Log.v(TAG, "no internet");
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
}