package com.swe.prototype.models.server;

import com.google.gson.Gson;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;

public class ServerCalendarEntry extends CalendarEntry {

	private int id = -1;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String description;
	private int repeat;

	public ServerCalendarEntry(AccountBase account, int id, String startDate,
			String endDate, String startTime, String endTime,
			String description, int repeat) {
		super(account);
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.repeat = repeat;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public ServerCalendarEntry(AccountBase account) {
		super(account);
	}
	
	public static ServerCalendarEntry fromJson(String json) {
		return new Gson().fromJson(json, ServerCalendarEntry.class);
	}
	
	public String toJson() {
		return getGsonInstance().toJson(this);
	}

	@Override
	public String getStartDate() {
		return startDate;
	}

	@Override
	public String getEndDate() {
		return endDate;
	}

	@Override
	public String getStartTime() {
		return startTime;
	}

	@Override
	public String getEndTime() {
		return endTime;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getRepeat() {
		return repeat;
	}
	
	public void setStartDate(String str) {
		this.startDate = str;
	}

	public void setEndDate(String str) {
		this.endDate = str;
	}

	public void setStartTime(String str) {
		this.startTime = str;
	}

	public void setEndTime(String str) {
		this.endTime = str;
	}

	public void setDescription(String str) {
		this.description = str;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	@Override
	public boolean isUpToDate() {
		return true;
	}

}
