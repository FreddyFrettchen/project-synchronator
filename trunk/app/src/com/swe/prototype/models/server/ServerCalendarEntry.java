package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;

public class ServerCalendarEntry extends CalendarEntry {

	public ServerCalendarEntry(AccountBase account) {
		super(account);
	}

	public static ServerCalendarEntry fromJson(String json) {
		return new Gson().fromJson(json, ServerCalendarEntry.class);
	}

}
