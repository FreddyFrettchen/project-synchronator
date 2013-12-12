package com.swe.prototype.net.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.swe.prototype.net.AsyncTaskBase;

public abstract class AsyncDataTask<Result> extends AsyncTaskBase<String, Void, Result> {

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
		
		return response == 200;
	}
}
