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

	public Date StartDate;
	public Date EndDate;
	public String Subject;
	public String Description;
	public int Repeat;
	
    public String getSubject() {
		return Subject;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public void setEndDate(Date endDate) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEndDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRepeat() {
		// TODO Auto-generated method stub
		return 0;
	}
}
