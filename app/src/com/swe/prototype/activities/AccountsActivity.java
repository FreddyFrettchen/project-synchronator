package com.swe.prototype.activities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
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

// muss in register umbenannt werden
public class AccountsActivity extends BaseActivity {
	
	private static final String TAG = "AccountsActivity";
	
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
	
	private void alertPasswordsNotMatching() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setTitle("Registrierung fehlgeschlagen");
		dlgAlert.setMessage("Die Passwörter stimmen leider nicht überein.");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	private void doRegistration() {
		String url = "http://10.0.2.2:45678";
		
		String email = ((EditText) findViewById(R.id.register_input_email)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.register_input_password))
				.getText().toString();
		String password2 = ((EditText) findViewById(R.id.register_input_password_rep))
				.getText().toString();
		
		if( !password.equals(password2) ){
			alertPasswordsNotMatching();
			return;
		}
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new RegisterUserTask().execute(url, email, password);
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
	
	private void openLogin(){
		Intent myIntent = new Intent(this, MainActivity.class);
		startActivity(myIntent);		
	}
	
	private void postRegister(){
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("Nachdem Ihr Account vom Admin freigeschaltet wurde, können Sie sich einloggen.");
		dlgAlert.setTitle("Registrierung erfolgreich");
		dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	openLogin();
            }
        });
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
		
	}
	
	private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = params[2];
			//
			try {
				return register(serverurl, username, password);
			} catch (IOException e) {
				//turn "IO EXCEPTION: " + e.getMessage();
			}
			return false;
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean result) {
			Log.v(TAG, "Result authentication = " + result);
			if( result )
				postRegister();
			else
				registerFailed();
		}

		private boolean register(String server, String email,
				String password) throws IOException {
			String authentification_url = server + "/user/register";

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
