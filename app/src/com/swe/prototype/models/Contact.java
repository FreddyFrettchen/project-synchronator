package com.swe.prototype.models;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class Contact extends BaseData implements Serializable{

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
}
