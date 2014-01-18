package com.swe.prototype.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.swe.prototype.R;
import com.swe.prototype.activities.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

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
	 * Sets up a post request and returns it. connect(); function has to be
	 * called to make the actual network call then.
	 * 
	 * @param server
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection postRequest(String _url, List<NameValuePair> params)
			throws IOException {

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

	public HttpsURLConnection postRequestSSL(Context context, String _url,
			List<NameValuePair> params) throws IOException {

		HttpsURLConnection conn = null;
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("BKS");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in = context.getResources().openRawResource(R.raw.certs);

		try {
			keyStore.load(in, "swe1314".toCharArray());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TrustManagerFactory tmf = null;
		try {
			tmf = TrustManagerFactory.getInstance("X509");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			tmf.init(keyStore);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SSLContext ssl_context = null;
		try {
			ssl_context = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ssl_context.init(null, tmf.getTrustManagers(), null);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.setProperty("http.keepAlive", "false");

		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		URL url = new URL(_url);
		conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(ssl_context.getSocketFactory());
		conn.setHostnameVerifier(hostnameVerifier);

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

	/**
	 * only returns return code of a post request
	 * 
	 * @return
	 */
	public int getPostRequestSSLReturnCode(Context context, String _url,
			List<NameValuePair> params) {

		HttpsURLConnection request = null;
		try {
			request = postRequestSSL(context, _url, params);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int response = 0;
		try {
			request.connect();
			response = request.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public Pair<Integer, String> getPostRequestSSLResponse(Context context,
			String _url, List<NameValuePair> params) {

		HttpsURLConnection request = null;
		try {
			request = postRequestSSL(context, _url, params);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int response_code = 0;
		String response_body = "";
		try {
			request.connect();
			response_code = request.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Scanner s;
			if (response_code != 200) {
				s = new Scanner(request.getErrorStream());
			} else {

				s = new Scanner(request.getInputStream());

			}
			s.useDelimiter("\\Z");

			response_body = s.next();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Pair<Integer, String>(response_code, response_body);
	}
}
