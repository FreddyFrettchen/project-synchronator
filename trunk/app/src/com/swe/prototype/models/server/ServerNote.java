package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Note;

public class ServerNote extends Note {
	
	private int id = -1;
	private String title = null;
	private String text = null;

	public ServerNote(AccountBase account, int id, String title, String text) {
		super(account);
		this.id = id;
		this.title = title;
		this.text = text;
	}
	
	public int getId() {
		return this.id;
	}

	@Override
	public String getNote() {
		return this.text;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	public static ServerNote fromJson(String json) {
		return getGsonInstance().fromJson(json, ServerNote.class);
	}

	@Override
	public String getAccountTag() {
		return "Server";
	}
	
	public String toJson() {
		return getGsonInstance().toJson(this);
	}
	
	public String toString(){
		return getTitle();
	}

	public void setId(int data_server_id) {
		this.id = data_server_id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean isUpToDate() {
		return this.id != -1;
	}
}
