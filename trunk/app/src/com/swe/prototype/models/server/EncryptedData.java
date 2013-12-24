package com.swe.prototype.models.server;

import com.google.gson.Gson;

public class EncryptedData {
	public int id;
	public String data;
	
	public EncryptedData(int id, String data){
		this.id = id;
		this.data = data;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
	
	// remove newlines
	public String getData(){
		return this.data.replace("\n", "").replace("\r", "");
	}
	
	public int getId(){
		return this.id;
	}
}