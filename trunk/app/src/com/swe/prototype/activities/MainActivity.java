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
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.globalsettings.Tools;
import com.swe.prototype.net.server.AsyncUserTask;
import com.swe.prototype.net.server.Server;

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
				switchToRegister();
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
	
	/* 
	 * muss am ende gel�scht werden
	 * */
	public void onClickTmpAmLoginVorbei(View v){
		show(CalendarActivity.class);
		
	}
	
	// wird aufgerufen, wenn der user auf Registrieren-Button klickt
	private void switchToRegister() {
		startActivity(new Intent(this, RegisterActivity.class));
	}
	
	/*
	Die m�ssen wir �berschreiben, damit das optionsmen� nicht sichtbar ist und der user nicht am login vorbei kommt.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	/*
	 * wird aufgerufen sobald user auf das imageview Serversettings clickt*/
	public void onClickServerSettings(View v){
		AlertDialog.Builder ssAlert = new AlertDialog.Builder(this);
		LayoutInflater inflator = getLayoutInflater();
		View optionDialogView = inflator.inflate(R.layout.dialog_serversettings,
				null);
		final EditText ip = (EditText)optionDialogView.findViewById(R.id.editText_ip);
		final EditText port = (EditText) optionDialogView.findViewById(R.id.editText_port);
		ip.setText(Settings.getIp());
		port.setText(Settings.getPort());
		ssAlert.setView(optionDialogView);
		//final EditText ip = (EditText) optionDialogView.findViewById(R.id.editText_ip);
		
		ssAlert.setCancelable(true);
		ssAlert.setTitle("Server Settings");
		ssAlert.setIcon(getResources().getDrawable(R.drawable.server_settings));
		ssAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String newIP = ip.getText().toString();
				String newPort = port.getText().toString();
				if(Tools.isValidPort(newPort)&&Tools.isValidIP(newIP)){
					Settings.setServer(newIP, newPort);
				}
				else{
					System.out.println("Server Settings IP oder Port nicht valide!");
				}
				

			}
		});
		ssAlert.setNegativeButton("Cancel", null);
		ssAlert.setCancelable(true);
		ssAlert.create().show();

	}
	

	// wird bei login-versuch aufgerufen
	private void initializeDialog(String message) {
		dialog = ProgressDialog.show(MainActivity.this, "",
				message, true);
		dialog.show();
	}
	
	// wird aufgerufen, wenn der user auf Login-Button klickt
	private void doAuthentication() {
		
		String email = ((EditText) findViewById(R.id.input_email)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.input_password))
				.getText().toString();

		if (hasInternetConnection()) {
			initializeDialog(getString(R.string.wait));
			server.new AuthenticateUserTask() {
				@Override
				protected void onPostExecute(Boolean success) {
					super.onPostExecute(success);
					dialog.dismiss();
					if (success)
						postLogin();
					else
						loginFailed();
				}
			}.execute();
		} else {
			Log.v(TAG, "no internet");
			Toast.makeText(getApplicationContext(), 
                    R.string.no_internet, Toast.LENGTH_LONG).show();
			//initializeDialog("no internet connection"+R.string.no_internet,true);
		}
	}
	
	private void loginFailed() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

		dlgAlert.setMessage("Username/Password wrong or not activated yet.");
		dlgAlert.setTitle(getString(R.string.login_failed));
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	/* Speichert logindaten */
	private void postLogin() {
		String email = ((EditText) findViewById(R.id.input_email)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.input_password))
				.getText().toString();

		SharedPreferences.Editor editor = getSharedPreferences(Settings.getPrefs_name(), 0)
				.edit();
		editor.putString("email", email);
		editor.putString("password", password);
		editor.commit();

		startActivity(new Intent(this, CalendarActivity.class));
		finish();
	}
}