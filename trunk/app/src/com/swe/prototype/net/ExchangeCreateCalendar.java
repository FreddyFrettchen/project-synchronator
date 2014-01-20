package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;

import android.os.AsyncTask;

public class ExchangeCreateCalendar extends AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					params[0], params[1]);

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date _startTime = dateFormat.parse(params[2] + " " + params[4]);//(startDate + " " + startTime);// "2014-02-25 16:00:00");
			Date _endTime = dateFormat.parse(params[3] + " " + params[5]);//(endDate + " " + endTime);// "2014-02-25 18:00:00");

			Appointment appointment = new Appointment();
			appointment.setSubject(params[6]);
			appointment.setBody(new Body(params[6]));
			appointment.setStartTime(_startTime);
			appointment.setEndTime(_endTime);
			//appointment.setLocation(description);
			appointment.setReminderIsSet(true);
			appointment.setReminderMinutesBeforeStart(30);

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
