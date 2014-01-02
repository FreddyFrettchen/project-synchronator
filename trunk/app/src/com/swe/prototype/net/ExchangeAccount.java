package com.swe.prototype.net;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.swe.prototype.R;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;

public class ExchangeAccount extends AccountBase {

	public ExchangeAccount(Context context, int refresh_time_sec,
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
		return "Exchange ("+this.username+")";
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		// TODO Auto-generated method stub
		return null;
	}

}