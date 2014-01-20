package com.swe.prototype.net.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
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

	public AsyncDataTask(Context context) {
		super(context);
	}

	protected boolean add(String server, String email, String password,
			String type, String data, String id_data) throws IOException {
		String add_url = server + "/data/add/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("data", data));
		params.add(new BasicNameValuePair("id_data", id_data));

		return getPostRequestReturnCode(add_url, params) == 200;
	}

	protected String get(String server, String email, String password,
			String type) throws IOException {
		String get_url = server + "/data/get/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		return getPostRequestResponse(get_url, params).second;
	}

	protected Boolean update(String server, String email, String password,
			String type, String id, String data) throws IOException {
		String update_url = server + "/data/update/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("data", data));
		params.add(new BasicNameValuePair("id_data", id));
		
		return getPostRequestReturnCode(update_url, params) == 200;
	}

	protected String sync(String server, String email, String password,
			String type, int timestamp) throws IOException {
		String sync_url = server + "/data/sync/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("last_sync", Integer
				.toString(timestamp)));

		return getPostRequestResponse(sync_url, params).second;
	}

	protected Boolean delete(String server, String email, String password,
			String type, int data_id) throws IOException {
		String delete_url = server + "/data/delete/" + type;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("data_id", data_id + ""));

		return getPostRequestReturnCode(delete_url, params) == 200;
	}
}
