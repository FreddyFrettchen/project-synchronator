package com.swe.prototype.models;

import java.io.Serializable;

public abstract class Contact extends BaseData {

	/**
	 * 
	 */

	public Contact(AccountBase account) {
		super(account);
	}
	
	public abstract String getLastName();

	public abstract String getFirstName();

	public abstract String getPhoneumber();

	public abstract String getEmail();

	public abstract String getAccountTag();
	
	public void delete(){
		this.account.deleteContact(this);
	}
	
	public String toString() {
		return getLastName() + ", " + getFirstName();
	}
}
