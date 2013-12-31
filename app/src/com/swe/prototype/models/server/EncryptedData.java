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
	
	private String decryptData(){
		// TODO pw ersetzen
		Security sec = new Security(Security.sha1("123"));
		return sec.decrypt(this.data);
	}
	
	public ServerContact toContact(){
		return ServerContact.fromJson(decryptData());
	}
	
	public ServerCalendarEntry toCalendarEntry(){
		return ServerCalendarEntry.fromJson(decryptData());
	}
	
	public ServerNote toNote(){
		return ServerNote.fromJson(decryptData());
	}
}