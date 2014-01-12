package com.swe.prototype.models.server;

import android.util.Log;

import com.google.gson.Gson;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;

public class EncryptedData {
	private static String TAG = "EncryptedData";
	
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
		Log.i(TAG,"Decrypting Contact id:" + data_server_id);
		ServerContact c = ServerContact.fromJson(decryptData(key));
		c.setId(data_server_id);
		c.setAccount(account);
		return c;
	}

	public ServerCalendarEntry toCalendarEntry(String key) {
		return ServerCalendarEntry.fromJson(decryptData(key));
	}
	
	public ServerNote toNote(String key, int data_server_id, AccountBase account){
		Log.i(TAG,"Decrypting Note id:" + data_server_id);
		ServerNote n = ServerNote.fromJson(decryptData(key));
		Log.i(TAG,"text is : " + n.getNote());
		n.setId(data_server_id);
		n.setAccount(account);
		return n;
	}
	
	public ServerCalendarEntry toCalendarEntry(String key, int data_server_id, AccountBase account){
		Log.i(TAG,"Decrypting CalendarEntry id:" + data_server_id);
		ServerCalendarEntry e = ServerCalendarEntry.fromJson(decryptData(key));
		Log.i(TAG,"end time is : " + e.getEndTime());
		e.setId(data_server_id);
		e.setAccount(account);
		return e;
	}
}
