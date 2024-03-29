package com.swe.prototype.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.swe.prototype.R;

/**
 * All Server related requests that are issued extend this baseclass that
 * supplies post functions for http and https.
 * 
 * @author batman
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class AsyncTaskBase<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	
	private final static String TAG = "AsyncTaskBase";

	private Context context = null;
	private final static int readTimeout = 15000; // ms
	private final static int connectionTimeout = 15000; // ms

	public AsyncTaskBase(Context context) {
		this.context = context;
	}

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

		//Log.w(TAG, "Executing HTTP call to: " + _url);
		
		URL url = new URL(_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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

	public Pair<Integer, String> getPostRequestResponse(String _url,
			List<NameValuePair> params) {
		return (_url.startsWith("https")) ? _getPostRequestSSLResponse(_url,
				params) : _getPostRequestResponse(_url, params);
	}

	public int getPostRequestReturnCode(String _url, List<NameValuePair> params) {
		return (_url.startsWith("https")) ? _getPostRequestSSLReturnCode(_url,
				params) : _getPostRequestReturnCode(_url, params);
	}

	/**
	 * only returns return code of a post request
	 * 
	 * @return
	 */
	public int _getPostRequestReturnCode(String _url, List<NameValuePair> params) {
		HttpURLConnection request = null;
		int response_code = 0;

		try {
			request = postRequest(_url, params);
			request.connect();
			response_code = request.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response_code;
	}

	public Pair<Integer, String> _getPostRequestResponse(String _url,
			List<NameValuePair> params) {

		String response = null;
		HttpURLConnection request = null;

		try {
			request = postRequest(_url, params);
			request.connect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int response_code = 0;
		try {
			response_code = request.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner s = null;
		if (response_code != 200) {
			s = new Scanner(request.getErrorStream());
		} else {
			try {
				s = new Scanner(request.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*if(_url.contains("calendar")){
			Log.i(TAG,"calendar stuff");
		// test
		while(s.hasNext()){
			response = s.next();
			Log.i(TAG,response);
		}
		}*/

		if (s != null) {
			//s.useDelimiter("\\Z");
			response = s.next();
		}

		return new Pair<Integer, String>(response_code, response);
	}

	/**
	 * uses an https connection to make requests.
	 * 
	 * @param context
	 * @param _url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public HttpsURLConnection postRequestSSL(String _url,
			List<NameValuePair> params) throws IOException {
		
		//Log.w(TAG, "Executing HTTPS call to: " + _url);
		
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		HttpsURLConnection conn = null;
		KeyStore keyStore = null;
		TrustManagerFactory tmf = null;
		SSLContext ssl_context = null;

		try {
			keyStore = KeyStore.getInstance("BKS");
			InputStream in = context.getResources()
					.openRawResource(R.raw.certs);
			keyStore.load(in, "swe1314".toCharArray());
			tmf = TrustManagerFactory.getInstance("X509");
			tmf.init(keyStore);
			ssl_context = SSLContext.getInstance("TLS");
			ssl_context.init(null, tmf.getTrustManagers(), null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		System.setProperty("http.keepAlive", "false");

		URL url = new URL(_url);
		conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(ssl_context.getSocketFactory());
		conn.setHostnameVerifier(hostnameVerifier);

		conn.setReadTimeout(readTimeout);
		conn.setConnectTimeout(connectionTimeout);
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
	public int _getPostRequestSSLReturnCode(String _url,
			List<NameValuePair> params) {

		HttpsURLConnection request = null;
		try {
			request = postRequestSSL(_url, params);
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

	public Pair<Integer, String> _getPostRequestSSLResponse(String _url,
			List<NameValuePair> params) {

		HttpsURLConnection request = null;
		try {
			request = postRequestSSL(_url, params);
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
			//s.useDelimiter("\\Z");

			response_body = s.next();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Pair<Integer, String>(response_code, response_body);
	}
}
