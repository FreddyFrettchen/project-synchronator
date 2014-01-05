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

}
