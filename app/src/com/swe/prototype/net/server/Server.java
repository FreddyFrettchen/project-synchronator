package com.swe.prototype.net.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

/**
 * This class handles all connections between app and storage server.
 * 
 * @author batman
 * 
 */

public class Server {

	protected static final String TAG = "Server";
	protected static final String SERVER = "http://10.0.2.2:45678";

	/**
	 * Task for User authentification
	 * 
	 * @author batman
	 */
	public class AuthenticateUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = sha1(params[2]);

			try {
				return authenticate(serverurl, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Task for user registration
	 * 
	 * @author batman
	 * 
	 */
	public class RegisterUserTask extends AsyncUserTask{
		protected Boolean doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = sha1(params[2]);

			try {
				return register(serverurl, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Add data to useraccount on the server
	 * 
	 * @author batman
	 */
	public class AddDataTask extends AsyncDataTask<Boolean> {
		/**
		 * @params[2] -> possible values: calendar, contact, note
		 */
		protected Boolean doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = sha1(params[2]);
			String type = params[2];
			String data = params[3];

			try {
				return add(serverurl, username, password, type, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * get data from useraccount on the server
	 * to the app.
	 * Task returns a list of entries of specified Type
	 * given in param[2]
	 * 
	 * @author batman
	 */
	public class GetDataTask<Type> extends AsyncDataTask<List<Type>> {
		/**
		 * @params[2] -> possible values: calendar, contacts, notes
		 */
		protected List<Type> doInBackground(String... params) {
			String serverurl = params[0];
			String username = params[1];
			String password = sha1(params[2]);
			String type = params[3];

			String response = null;
			try {
				response = get(serverurl, username, password, type);
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
