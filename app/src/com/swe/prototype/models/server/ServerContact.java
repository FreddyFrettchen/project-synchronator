package com.swe.prototype.models.server;

import java.io.IOException;

import com.google.gson.Gson;
import com.swe.prototype.models.Contact;
import com.swe.prototype.net.server.Server;
import com.swe.prototype.net.server.Server.AddDataTask;

public class ServerContact extends Contact {

	private int id = -1;
	private String lastname = null;
	private String firstname = null;
	private String phonenumber = null;
	private String email = null;

	public ServerContact(String lastname, String firstname, String phonenumber,
			String email) {
		super(null);
		this.lastname = lastname;
		this.firstname = firstname;
		this.phonenumber = phonenumber;
		this.email = email;
	}

	public ServerContact(Contact c) {
		super(c);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public static ServerContact fromJson(String json) {
		return new Gson().fromJson(json, ServerContact.class);
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
	
	public String toString(){
		return getLastName() + ", " + getFirstName();
	}

	@Override
	public String getAccountTag() {
		return "Server";
	}
}
