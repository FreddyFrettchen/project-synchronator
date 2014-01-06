package com.swe.prototype.net;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

public class GoogleAccount extends AccountBase {

	public GoogleAccount(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
	}

	@Override
	public void synchronize() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Google (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
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

	@Override
	public void editContact(Context context, Contact c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editNote(Context context, Note n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editCalendarEntry(Context context, CalendarEntry ce) {
		// TODO Auto-generated method stub
    }
		
	@Override
	public void deleteContact(Contact c) {
		// TODO Auto-generated method stub
    }		

	@Override
	public void deleteNote(Note n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createNote(String title, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.swe.prototype.models.AccountBase#createCalendarEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 * parameter:
	 * startDate,endDate Format: dd/mm/yyyy
	 * satrTime,endTime Format: hh:mm AM or hh:mm PM
	 * repeat: 0 == no repeat; 1 == every day; 2 == every month; 3 == every year
	 * description: String from description edit text;
	 */
	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		System.out
				.println("Google: Add CalenderEntry: noch nicht implementiert");

	}
}
