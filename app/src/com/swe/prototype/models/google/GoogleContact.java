package com.swe.prototype.models.google;

import android.content.Context;
import android.util.Log;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;

public class GoogleContact extends Contact {
	
	public static String TAG = "GoogleContact";
	private String lastname;
	private String firstname;
	private String id;
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPhoneumber(String phoneumber) {
		this.phoneumber = phoneumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String phoneumber;
	private String email;
	
	
	protected GoogleContact(AccountBase account, Contact c) {
		super(account);
	}

	public String getID()
	{
		return this.id;
	}
	
	@Override
	public String getLastName() {
		return this.lastname;
	}

	@Override
	public String getFirstName() {
		return this.firstname;
	}

	@Override
	public String getPhoneumber() {
		return this.phoneumber;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getAccountTag() {
		return "Google";
	}

	@Override
	public void delete() {
		Log.i(TAG, "delete:"+this.toString());
	}

	@Override
	public void edit(Context context) {
		Log.i(TAG, "edit:"+this.toString());
	}

}
