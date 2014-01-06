package com.swe.prototype.net.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.nfc.Tag;
import android.util.Log;

import com.swe.prototype.net.AsyncTaskBase;

/**
 * All functions relating to adding/updating/getting/syncing with the data
 * server.
 * 
 * @author batman
 * @param <Result>
 */
public abstract class AsyncDataTask<Result> extends
		AsyncTaskBase<String, Void, Result> {

	protected static final String TAG = "AsyncDataTask";

	protected boolean add(String server, String email, String password,
			String type, String data) throws IOException {
		String add_url = server + "/data/add/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("data", data));

		HttpURLConnection request = postRequest(add_url, params);
		request.connect();

		// response code of 200 is accepted and 304 is failed.
		int response = request.getResponseCode();

		// Log.i(TAG, "add request returned: "+ response +
		// ". params: "+email+"/"+password+"/"+type+"/"+data);

		return response == 200;
	}

	protected String get(String server, String email, String password,
			String type) throws IOException {
		String add_url = server + "/data/get/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		HttpURLConnection request = postRequest(add_url, params);
		request.connect();

		Scanner s;
		if (request.getResponseCode() != 200) {
			s = new Scanner(request.getErrorStream());
		} else {
			s = new Scanner(request.getInputStream());
		}
		s.useDelimiter("\\Z");

		String response = s.next();
		Log.i(TAG, response);

		return response;
	}

	protected Boolean update(String server, String email, String password,
			String type, String id, String data) throws IOException {
		String add_url = server + "/data/update/" + type + "/" + id;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("data", data));

		HttpURLConnection request = postRequest(add_url, params);
		request.connect();

		return request.getResponseCode() == 200;
	}

	protected String sync(String server, String email, String password,
			String type, int timestamp) throws IOException {
		String add_url = server + "/data/sync/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("last_sync", Integer
				.toString(timestamp)));

		HttpURLConnection request = postRequest(add_url, params);
		request.connect();
		Scanner s;
		if (request.getResponseCode() != 200) {
			s = new Scanner(request.getErrorStream());
		} else {
			s = new Scanner(request.getInputStream());
		}
		s.useDelimiter("\\Z");

		String response = s.next();
		return response;
	}
}
