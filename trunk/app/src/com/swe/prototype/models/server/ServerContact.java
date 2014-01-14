package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;

public class ServerContact extends Contact {
	
	public static String TAG = "ServerContact";

	private int id = -1;
	private String lastname = null;
	private String firstname = null;
	private String phonenumber = null;
	private String email = null;

	public String getID()
	{
		return null;
	}
	
	public ServerContact(AccountBase account, int id, String lastname, String firstname, String phonenumber,
			String email) {
		super(account);
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.phonenumber = phonenumber;
		this.email = email;
	}

	public ServerContact(AccountBase account, Contact c) {
		super(account);
	}

	public String toJson() {
		return getGsonInstance().toJson(this);
	}

	public static ServerContact fromJson(String json) {
		return getGsonInstance().fromJson(json, ServerContact.class);
	}
	
	public int getId(){
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
		return this.phonenumber;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	public String toString() {
		return getLastName() + ", " + getFirstName();
	}

	@Override
	public String getAccountTag() {
		return "Server";
	}

	public void setId(int data_server_id) {
		this.id = data_server_id;
	}
}