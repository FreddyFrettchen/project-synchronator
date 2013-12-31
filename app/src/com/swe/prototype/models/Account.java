package com.swe.prototype.models;

import java.util.ArrayList;

import com.swe.prototype.adapter.ContactAdapter;

import android.os.Environment;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public abstract class Account {
	protected String account_type = null;
	protected String username = null;
	protected String password = null;
	
	protected ArrayList<Contact> contacts = null;
	protected ArrayList<CalendarEntry> calendar_entries = null;
	protected ArrayList<Note> notes = null;
	
	public Account(String account_type, String username, String password){
		this.account_type = account_type;
		this.username = username;
		this.password = password;
		
		this.contacts = new ArrayList<Contact>();
		this.calendar_entries = new ArrayList<CalendarEntry>();
		this.notes = new ArrayList<Note>();
	}
	
	public abstract void updateContacts();
	
	public String getIdentifier(){
		return this.account_type;
	}
	
	public ArrayList<Contact> getContacts() {
		return this.contacts;
	}

	public ArrayList<CalendarEntry> getCalendarEntries() {
		return this.calendar_entries;
	}

	public ArrayList<Note> getNotes() {
		return this.notes;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public abstract BaseAdapter getContactAdapter();
	public abstract BaseAdapter getNotesAdapter();
	public abstract BaseAdapter getCalendarAdapter();
}
