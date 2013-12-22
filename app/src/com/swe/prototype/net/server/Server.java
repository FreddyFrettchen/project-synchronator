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
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.server.EncryptedData;
import com.swe.prototype.models.server.ServerContact;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

/**
 * This class handles all connections between app and storage server.
 * 
 * @author batman
 * 
 */

public class Server {

	protected static final String TAG = "Server";

	protected String server_url = null;
	protected String email = null;
	protected String password_hash = null;
	protected Security sec = null;

	public Server(String server_url, String email, String password_hash) {
		this.server_url = server_url;
		this.email = email;
		this.password_hash = password_hash;
		this.sec = new Security(this.password_hash);
	}

	/**
	 * Task for User authentification
	 */
	public class AuthenticateUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return authenticate(server_url, email, password_hash);
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
				return register(server_url, email, password_hash);
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
				return add(server_url, email, password_hash, type, data);
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
				response = get(server_url, email, password_hash, type);
				Type listType = new TypeToken<ArrayList<EncryptedData>>() {
				}.getType();
				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) new Gson()
						.fromJson(response, listType);
				String data = null;
				for (int i = 0; i < list.size(); i++) {
					data = list.get(i).data.replace("\n", "").replace("\r", "");
					//Log.i(TAG,data.length()+"");
					//Log.i(TAG,data.replace("\n", "").replace("\r", "").length()+"");
					list.get(i).data = sec.decrypt(data);
					// contacts.add(gson.fromJson(list.get(i).data, DataType));
					Log.i(TAG, list.get(i).id + " = " + list.get(i).data);
				}
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
