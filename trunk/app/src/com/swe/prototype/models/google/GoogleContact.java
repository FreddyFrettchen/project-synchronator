package com.swe.prototype.models.google;

import android.content.Context;
import android.util.Log;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;

public class GoogleContact extends Contact {
	
	public static String TAG = "GoogleContact";

	protected GoogleContact(AccountBase account, Contact c) {
		super(account);
	}

	@Override
	public String getLastName() {
		return null;
	}

	@Override
	public String getFirstName() {
		return null;
	}

	@Override
	public String getPhoneumber() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
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
