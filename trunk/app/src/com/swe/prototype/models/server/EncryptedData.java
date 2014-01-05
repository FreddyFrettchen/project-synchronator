package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.helpers.Security;

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
	
	private String decryptData(String key){
		// TODO pw ersetzen
		Security sec = new Security(key);
		return sec.decrypt(this.data);
	}
	
	public ServerContact toContact(String key){
		return ServerContact.fromJson(decryptData(key));
	}
	
	public ServerCalendarEntry toCalendarEntry(String key){
		return ServerCalendarEntry.fromJson(decryptData(key));
	}
	
	public ServerNote toNote(String key){
		return ServerNote.fromJson(decryptData(key));
	}
}