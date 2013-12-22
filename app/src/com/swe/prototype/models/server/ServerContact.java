package com.swe.prototype.models.server;

import java.io.IOException;

import com.google.gson.Gson;
import com.swe.prototype.models.Contact;
import com.swe.prototype.net.server.Server;
import com.swe.prototype.net.server.Server.AddDataTask;

public class ServerContact extends Contact {

	private int id = -1;
	private String name = null;
	private String number = null;

	public ServerContact(String name, String number) {
		super(null);
		this.name = name;
		this.number = number;
	}

	public ServerContact(Contact c) {
		super(c);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNumber() {
		return this.number;
	}

	public String toJson(){
		return new Gson().toJson(this);
	}

	public static ServerContact fromJson(String json){
		return new Gson().fromJson(json, ServerContact.class);   
	}
}
