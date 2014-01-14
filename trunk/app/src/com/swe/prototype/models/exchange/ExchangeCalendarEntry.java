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

    public ExchangeCalendarEntry(AccountBase account) {
        super(account);
        // TODO Auto-generated constructor stub
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
