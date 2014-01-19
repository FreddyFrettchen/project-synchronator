package com.swe.prototype.models.exchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.independentsoft.exchange.And;
import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.AppointmentPropertyPath;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.IsGreaterThanOrEqualTo;
import com.independentsoft.exchange.IsLessThanOrEqualTo;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.AccountBase;

public class ExchangeCalendarEntry extends CalendarEntry {

	public String id;
	public String StartDate;
	public String EndDate;
	public String Subject;
	public String Description;
	public int Repeat;
	
	public void setId(String _id){
		this.id = _id;
	}
	
	public String getId(){
		return this.id;
	}
	
    public String getSubject() {
		return Subject;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setRepeat(int repeat) {
		Repeat = repeat;
	}

	public ExchangeCalendarEntry(AccountBase account) {
        super(account);
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
		return null;
	}

	@Override
	public String getEndTime() {
		return null;
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
