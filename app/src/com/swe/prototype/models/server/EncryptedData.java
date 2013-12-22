package com.swe.prototype.models.server;

import com.google.gson.Gson;

public class EncryptedData {
	public int id;
	public String data;

	public String toJson() {
		return new Gson().toJson(this);
	}
}