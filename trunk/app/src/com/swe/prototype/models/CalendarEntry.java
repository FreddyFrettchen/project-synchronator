package com.swe.prototype.models;

import android.content.Context;

public abstract class CalendarEntry extends BaseData 
{
	//
	private String id;
	private String description;
	private int repeat;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	

	public void setId(String id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public CalendarEntry(AccountBase account) {
		super(account);
	}

	public void delete() {
		this.account.deleteCalendarEntry(this);
	}

	public void edit(Context context) {
		this.account.editCalendarEntry(context, this);
	}

	public abstract String getStartDate();

	public abstract String getEndDate();

	public abstract String getStartTime();

	public abstract String getEndTime();

	public abstract String getDescription();

	public abstract int getRepeat();

}
