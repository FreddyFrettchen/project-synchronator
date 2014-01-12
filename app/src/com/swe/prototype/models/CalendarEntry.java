package com.swe.prototype.models;

import android.content.Context;

public abstract class CalendarEntry extends BaseData {
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
