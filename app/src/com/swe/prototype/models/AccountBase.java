package com.swe.prototype.models;

import android.content.Context;
import android.util.Log;

public abstract class AccountBase extends Thread {
	private final static String TAG = "AccountBase";
	
	protected int refresh_time_sec = 6*60*60; // default 6 hours
	protected String username = null;
	protected String password = null;
	protected Context context = null;

	public AccountBase(Context context, int refresh_time_sec, String username, String password) {
		this.setRefreshTime(refresh_time_sec);
		this.username = username;
		this.password = password;
		this.context = context;
	}

	public void run() {
		while (true) {
			synchronize();
			try {
				Thread.sleep(this.refresh_time_sec*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getRefreshTime() {
		return refresh_time_sec;
	}

	public void setRefreshTime(int refresh_time_sec) {
		this.refresh_time_sec = refresh_time_sec;
	}
	
	public abstract void synchronize();
}