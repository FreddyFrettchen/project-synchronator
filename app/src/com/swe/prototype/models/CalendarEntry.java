package com.swe.prototype.models;

import android.content.Context;

public abstract class CalendarEntry extends BaseData {
	public CalendarEntry(AccountBase account) {
		super(account);
	}
	
	public void delete(){
		this.account.deleteCalendarEntry(this);
	}

	public void edit(Context context){
		this.account.editCalendarEntry(context, this);
	}
}
