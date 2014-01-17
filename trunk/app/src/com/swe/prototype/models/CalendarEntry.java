package com.swe.prototype.models;

import java.io.Serializable;

import android.content.Context;

public abstract class CalendarEntry extends BaseData implements Serializable
{
	public CalendarEntry(AccountBase account) {
		super(account);
	}

	public void delete() {
		this.account.deleteCalendarEntry(this);
	}

	public abstract String getStartDate();

	public abstract String getEndDate();

	public abstract String getStartTime();

	public abstract String getEndTime();

	public abstract String getDescription();

	public abstract int getRepeat();

}
