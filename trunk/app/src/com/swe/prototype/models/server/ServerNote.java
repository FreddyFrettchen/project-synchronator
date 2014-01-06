package com.swe.prototype.models.server;

import android.R.string;

import com.google.gson.Gson;
import com.swe.prototype.models.Note;

public class ServerNote extends Note {

	public ServerNote(Note n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getNote() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ServerNote fromJson(String json) {
		return new Gson().fromJson(json, ServerNote.class);
	}

	@Override
	public String getAccountTag() {
		return "Server";
	}

}
