package com.swe.prototype.net;

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
import java.util.List;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public abstract class AsyncTaskBase<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	/**
	 * urlencodes list of NameValuePairs
	 * 
	 * @param params
	 * @return urlencoded querystring
	 * @throws UnsupportedEncodingException
	 */
	public static String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	/**
	 * calculate sha1 for given String s
	 * 
	 * @param s
	 * @return sha1 hashed string
	 */
	public String sha1(String s) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.reset();
		byte[] data = digest.digest(s.getBytes());
		return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,
				data));
	}

	/**
	 * Sets up a post request and returns it. connect(); function has to be
	 * called to make the actual network call then.
	 * 
	 * @param server
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection postRequest(String _url,
			List<NameValuePair> params) throws IOException {

		URL url = new URL(_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		// write parameters to request
		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,
				"UTF-8"));
		writer.write(getQuery(params));
		writer.flush();
		writer.close();
		os.close();

		return conn;
	}
}
