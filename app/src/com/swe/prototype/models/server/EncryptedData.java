package com.swe.prototype.models.server;

import android.util.Log;

import com.google.gson.Gson;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;

public class EncryptedData {
	private static String TAG = "EncryptedData";
	
	public int id;
	public String data;
	public int id_data;
	public boolean deleted;

	public EncryptedData(int id, String data) {
		this.id = id;
		this.data = data;
	}
	
	public EncryptedData(int id, String data, int id_data, boolean deleted) {
		this.id = id;
		this.data = data;
		this.id_data = id_data;
		this.deleted = deleted;
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
	
	public int getIdData() {
		return this.id_data;
	}
	
	public boolean isDeleted() {
		return this.deleted;
	}
	
	
	public String encryptData(String key) {
		Security sec = new Security(key);
		return sec.encrypt(this.data);
	}

	public String decryptData(String key) {
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
	
	public ServerCalendarEntry toCalendarEntry(String key, int data_server_id, AccountBase account){
		ServerCalendarEntry e = ServerCalendarEntry.fromJson(decryptData(key));
		e.setId(data_server_id);
		e.setAccount(account);
		return e;
	}
}
