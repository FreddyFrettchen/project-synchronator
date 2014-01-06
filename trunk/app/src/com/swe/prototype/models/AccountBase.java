package com.swe.prototype.models;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

public abstract class AccountBase extends Thread {
	private final static String TAG = "AccountBase";

	protected int refresh_time_sec = 6 * 60 * 60; // default 6 hours
	protected String username = null;
	protected String password = null;
	protected Context context = null;

	public AccountBase(Context context, int refresh_time_sec, String username,
			String password) {
		this.setRefreshTime(refresh_time_sec);
		this.username = username;
		this.password = password;
		this.context = context;
	}

	public void run() {
		synchronize();
	}

	public int getRefreshTime() {
		return refresh_time_sec;
	}

	public void setRefreshTime(int refresh_time_sec) {
		this.refresh_time_sec = refresh_time_sec;
	}

	public abstract void synchronize();

	public abstract BaseAdapter getContactAdapter(Context context, int layout_id);

	public abstract BaseAdapter getNotesAdapter(Context context, int layout_id);

	public abstract BaseAdapter getCalendarAdapter(Context context,
			int layout_id);

	public abstract void createContact(String lastname, String firstname,
			String phonenumber, String email);
	

	/**
	 * @param startDate Sting: Format: dd/mm/yyyy
	 * @param endDate	Sting: Format: dd/mm/yyyy
	 * @param startTime	String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param endTime String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param description Sting from EditText
	 * @param repeat: int |	0 == no_repeat; 1 == every_day; 2 == every_month; 3 == every_year
	 */
	public abstract void createCalendarEntry(String startDate, String endDate,String startTime,String endTime,
			String description, int repeat);

	public abstract void createNote();

	public abstract void createCalendarEntry();

	public abstract String toString();
}
