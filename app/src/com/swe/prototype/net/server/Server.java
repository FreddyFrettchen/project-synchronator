package com.swe.prototype.net.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.Account;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import com.swe.prototype.models.server.EncryptedData;
import com.swe.prototype.models.server.ServerCalendarEntry;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.models.server.ServerNote;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

/**
 * This class handles all connections between app and storage server.
 * 
 * @author batman
 * 
 */

public class Server extends Account {

	protected static final String TAG = "Server";

	protected String server_url = null;
	protected Security sec = null;
	protected Gson gson = null;

	public Server(String username, String password) {
		super("server", username, password);
		this.server_url = Settings.getServer();
		this.sec = new Security(this.password);
		this.gson = new Gson();
	}

	/**
	 * Task for User authentification
	 */
	public class AuthenticateUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return authenticate(server_url, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Task for user registration
	 */
	public class RegisterUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return register(server_url, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * add data to the useraccount on the server. Task returns true if a new
	 * entry was created on the server.
	 */
	public class AddDataTask extends AsyncDataTask<Boolean> {
		/**
		 * @params[2] -> possible values: calendar, contact, note
		 */
		protected Boolean doInBackground(String... params) {
			String type = params[0];
			String data = sec.encrypt(params[1]);

			try {
				return add(server_url, username, password, type, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * get data from useraccount on the server to the app. Task returns a list
	 * of entries of specified Type given in param[2] the entries are still
	 * encrypted and json encoded seperatly and have to be processed further.
	 */
	public class GetDataTask extends AsyncDataTask<ArrayList<EncryptedData>> {
		/**
		 * @params[2] -> possible values: calendar, contacts, notes
		 */
		protected ArrayList<EncryptedData> doInBackground(String... params) {
			String type = params[0];

			String response = null;
			try {
				response = get(server_url, username, password, type);
				Type listType = new TypeToken<ArrayList<EncryptedData>>() {
				}.getType();
				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) gson
						.fromJson(response, listType);
				String data = null;
				for (int i = 0; i < list.size(); i++) {
					data = list.get(i).data.replace("\n", "").replace("\r", "");
					list.get(i).data = sec.decrypt(data);
					Log.i(TAG, list.get(i).id + " = " + list.get(i).data);
				}
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void updateContacts() {
		new GetDataTask() {
			protected void onPostExecute(ArrayList<EncryptedData> list) {
				super.onPostExecute(list);

				if (list == null)
					return;

				contacts.clear();
				for (int i = 0; i < list.size(); i++) {
					contacts.add(gson.fromJson(list.get(i).data,
							ServerContact.class));
				}

			}
		}.execute("contacts");
	}
}
