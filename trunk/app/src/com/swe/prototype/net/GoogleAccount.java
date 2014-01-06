package com.swe.prototype.net;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.swe.prototype.R;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

public class GoogleAccount extends AccountBase {

	public GoogleAccount(Context context, int refresh_time_sec,
			String username, String password) {
		super(context, refresh_time_sec, username, password);
	}

	@Override
	public void synchronize() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Google ("+this.username+")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createNote() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createCalendarEntry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context,layout_id);
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context,layout_id);
		return adapter;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.swe.prototype.models.AccountBase#createCalendarEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 * parameter:
	 * startDate,endDate Format: dd/mm/yyyy
	 * satrTime,endTime Format: hh:mm AM or hh:mm PM
	 * repeat: 0 == no repeat; 1 == every day; 2 == every month; 3 == every year
	 * description: String from description edit text;
	 * 
	 */
	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		System.out.println("Google: Add CalenderEntry: noch nicht implementiert");
		
		
	}

}
