package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;

public class EncryptedData {
	public int id;
	public String data;

	public EncryptedData(int id, String data) {
		this.id = id;
		this.data = data;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	// remove newlines
	public String getData() {
		return this.data.replace("\n", "").replace("\r", "");
	}

	public int getId() {
		return this.id;
	}

	private String decryptData(String key) {
		// TODO pw ersetzen
		Security sec = new Security(key);
		return sec.decrypt(this.data);
	}
	
	public ServerContact toContact(String key, int data_server_id, AccountBase account){
		ServerContact c = ServerContact.fromJson(decryptData(key));
		c.setId(data_server_id);
		c.setAccount(account);
		return c;
	}

	public ServerCalendarEntry toCalendarEntry(String key) {
		return ServerCalendarEntry.fromJson(decryptData(key));
	}
	
	public ServerNote toNote(String key, int data_server_id, AccountBase account){
		ServerNote n = ServerNote.fromJson(decryptData(key));
		n.setId(data_server_id);
		n.setAccount(account);
		return n;
	}
}
