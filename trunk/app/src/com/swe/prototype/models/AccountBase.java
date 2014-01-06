package com.swe.prototype.models;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

public abstract class AccountBase extends Thread {
	private final static String TAG = "AccountBase";

	protected int refresh_time_sec = 6 * 60 * 60; // default 6 hours
	protected int account_id = 0;
	protected String username = null;
	protected String password = null;
	protected Context context = null;

	public AccountBase(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		this.setRefreshTime(refresh_time_sec);
		this.account_id = account_id;
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

	public int getAccountId() {
		return this.account_id;
	}

	public abstract void synchronize();

	public abstract BaseAdapter getContactAdapter(Context context, int layout_id);

	public abstract BaseAdapter getNotesAdapter(Context context, int layout_id);

	public abstract BaseAdapter getCalendarAdapter(Context context,
			int layout_id);

	public abstract void createContact(String lastname, String firstname,
			String phonenumber, String email);

	public abstract void createNote(String title, String text);
	
    /**
	 * @param startDate
	 *            Sting: Format: dd/mm/yyyy
	 * @param endDate
	 *            Sting: Format: dd/mm/yyyy
	 * @param startTime
	 *            String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param endTime
	 *            String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param description
	 *            Sting from EditText
	 * @param repeat
	 *            : int | 0 == no_repeat; 1 == every_day; 2 == every_month; 3 ==
	 *            every_year
	 */
	public abstract void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat);

	public abstract void editContact(Context context, Contact c);

	public abstract void editNote(Context context, Note n);

	public abstract void editCalendarEntry(Context context, CalendarEntry ce);

	public abstract void deleteContact(Contact c);

	public abstract void deleteNote(Note n);

	public abstract void deleteCalendarEntry(CalendarEntry ce);

	public abstract String toString();
}
