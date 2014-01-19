package com.swe.prototype.net.server;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.swe.prototype.net.AsyncTaskBase;

public abstract class AsyncUserTask extends
		AsyncTaskBase<String, Void, Boolean> {
	
	public AsyncUserTask(Context context){
		super(context);
	}

	protected static final String TAG = "AsyncUserTask";

	protected boolean authenticate(Context context, String server,
			String email, String password) throws IOException {
		String authentification_url = server + "/user/authenticate";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		HttpURLConnection request = postRequest(authentification_url, params);
		request.connect();

		// response code of 200 is accepted and 403 is failed. int response =
		return request.getResponseCode() == 200;
		/*
		 * return getPostRequestSSLReturnCode(context, authentification_url,
		 * params) == 200;
		 */
	}

	protected boolean delete(String server, String email, String password)
			throws IOException {
		String authentification_url = server + "/user/delete";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		HttpURLConnection request = postRequest(authentification_url, params);
		request.connect();

		int response = request.getResponseCode();

		return response == 200;
	}

	protected boolean register(String server, String email, String password)
			throws IOException {
		String register_url = server + "/user/register";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		HttpURLConnection request = postRequest(register_url, params);
		request.connect();

		// response code of 200 is accepted and 403 is failed.
		int response = request.getResponseCode();

		return response == 200;
	}
}
