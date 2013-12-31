package com.swe.prototype.models.google;

import com.swe.prototype.models.Contact;

public class GoogleContact extends Contact {

	protected GoogleContact(Contact c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPhoneumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccountTag() {
		return "Google";
	}

}
