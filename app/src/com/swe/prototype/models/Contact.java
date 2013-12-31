package com.swe.prototype.models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class Contact extends BaseData {
	
	public Contact(Contact c) {

	}

	public abstract String getLastName();

	public abstract String getFirstName();

	public abstract String getPhoneumber();

	public abstract String getEmail();
	
	public abstract String getAccountTag();
}
