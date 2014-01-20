package com.swe.prototype.models.exchange;

import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.AccountBase;

public class ExchangeCalendarEntry extends CalendarEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String StartDate;
	public String EndDate;
	public String StartTime;
	public String EndTime;
	public String Subject;
	public String Description;
	public int Repeat;
	
	public ExchangeCalendarEntry(AccountBase account) {
        super(account);
    }

	public void setId(String _id){
		this.id = _id;
	}
	
	public String getId(){
		return this.id;
	}
	
    public String getSubject() {
		return this.Subject;
	}

	public void setStartDate(String startDate) {
		this.StartDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.EndDate = endDate;
	}

	public void setStartTime(String startTime) {
		this.StartTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.EndTime = endTime;
	}

	
	public void setSubject(String subject) {
		this.Subject = subject;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public void setRepeat(int repeat) {
		this.Repeat = repeat;
	}

	@Override
	public String getStartDate() {
		return this.StartDate;
	}

	@Override
	public String getEndDate() {
		return this.EndDate;
	}

	@Override
	public String getStartTime() {
		return this.StartTime;
	}

	@Override
	public String getEndTime() {
		return this.EndTime;
	}

	@Override
	public String getDescription() {
		return this.Description;
	}

	@Override
	public int getRepeat() {
		return this.Repeat;
	}

	@Override
	public boolean isUpToDate() {
		return true;
	}
}
