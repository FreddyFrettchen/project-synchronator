package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.independentsoft.exchange.AbsoluteYearlyRecurrencePattern;
import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.DailyRecurrencePattern;
import com.independentsoft.exchange.DayOfWeek;
import com.independentsoft.exchange.EndDateRecurrenceRange;
import com.independentsoft.exchange.Month;
import com.independentsoft.exchange.NumberedRecurrenceRange;
import com.independentsoft.exchange.Recurrence;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.TimeZoneDefinition;
import com.independentsoft.exchange.WeeklyRecurrencePattern;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeCreateCalendar extends AsyncTask<String, Void, Boolean> {

	private static String TAG = "ExchangeAccount";
	
	@Override
	protected Boolean doInBackground(String... params) {
		Log.i(TAG, "doInBackground Calendar start");
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					params[0], params[1]);

		
			Log.i(TAG, params[0] + " " + params[1] + " " + params[2] + " " +
			params[3] + " " + params[4] + " " + params[5] + " " + params[6] + " " + params[7]);
			
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date _startTime = dateFormat.parse(params[2] + " " + params[4]);//(startDate + " " + startTime);// "2014-02-25 16:00:00");
			Date _endTime = dateFormat.parse(params[3] + " " + params[5]);//(endDate + " " + endTime);// "2014-02-25 18:00:00");

			Appointment appointment = new Appointment();
			appointment.setSubject(params[6]);
			appointment.setBody(new Body(params[6]));
			appointment.setStartTime(_startTime);
			appointment.setEndTime(_endTime);
			appointment.setReminderIsSet(true);
			appointment.setReminderMinutesBeforeStart(30);
		
			
			int repeat = Integer.parseInt(params[7]);
			Log.i(TAG, repeat + "");
			if(repeat != 0){
				
				Date patternStartTime = dateFormat.parse(params[2] + " 00:00:00");//"2014-03-01 00:00:00");
	            Date patternEndTime = dateFormat.parse("2016-01-31 00:00:00");
	            Recurrence recurrence = new Recurrence();
	            
				if(repeat == 1){
					DailyRecurrencePattern pattern = new DailyRecurrencePattern(1);
					NumberedRecurrenceRange range = new NumberedRecurrenceRange(patternStartTime, 500);
					recurrence.setPattern(pattern);
		            recurrence.setRange(range);
				}
				else if(repeat == 2){
					List<DayOfWeek> days = new ArrayList<DayOfWeek>();
		            days.add(DayOfWeek.TUESDAY);
					WeeklyRecurrencePattern pattern = new WeeklyRecurrencePattern(1, days);
					EndDateRecurrenceRange range = new EndDateRecurrenceRange(patternStartTime, patternEndTime);
					recurrence.setPattern(pattern);
		            recurrence.setRange(range);
				}
				else if(repeat == 3){
					AbsoluteYearlyRecurrencePattern pattern = new AbsoluteYearlyRecurrencePattern(Month.JANUARY, _startTime.getDay());
					EndDateRecurrenceRange range = new EndDateRecurrenceRange(patternStartTime, patternEndTime);
					recurrence.setPattern(pattern);
		            recurrence.setRange(range);
				}
				
	            appointment.setRecurrence(recurrence);
			}
			
			
			service.createItem(appointment);
			return true;
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
