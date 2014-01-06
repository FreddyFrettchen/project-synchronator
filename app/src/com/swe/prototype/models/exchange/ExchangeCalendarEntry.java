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

public class ExchangeCalendarEntry extends CalendarEntry {

	public ExchangeCalendarEntry(CalendarEntry ce) {
		super(ce);
		// TODO Auto-generated constructor stub
	}

	public boolean getExchangeCalendar() {
		try {
			Service service = new Service("https://myserver/ews/Exchange.asmx",
					"username", "password");
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date startTime = dateFormat.parse("2014-04-15 00:00:00");
			Date endTime = dateFormat.parse("2014-04-16 00:00:00");

			IsGreaterThanOrEqualTo restriction1 = new IsGreaterThanOrEqualTo(
					AppointmentPropertyPath.START_TIME, startTime);
			IsLessThanOrEqualTo restriction2 = new IsLessThanOrEqualTo(
					AppointmentPropertyPath.END_TIME, endTime);
			And restriction3 = new And(restriction1, restriction2);

			FindItemResponse response = service
					.findItem(StandardFolder.CALENDAR,
							AppointmentPropertyPath.getAllPropertyPaths(),
							restriction3);

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof Appointment) {
					Appointment appointment = (Appointment) response.getItems()
							.get(i);

					System.out.println("Subject = " + appointment.getSubject());
					System.out.println("StartTime = "
							+ appointment.getStartTime());
					System.out.println("EndTime = " + appointment.getEndTime());
					System.out.println("Body Preview = "
							+ appointment.getBodyPlainText());
					System.out
							.println("----------------------------------------------------------------");
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean CreateAppointment() {
		try {
			Service service = new Service("https://myserver/ews/Exchange.asmx",
					"username", "password");
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date startTime = dateFormat.parse("2014-02-25 16:00:00");
			Date endTime = dateFormat.parse("2014-02-25 18:00:00");

			Appointment appointment = new Appointment();
			appointment.setSubject("Test");
			appointment.setBody(new Body("Body text"));
			appointment.setStartTime(startTime);
			appointment.setEndTime(endTime);
			appointment.setLocation("My Office");
			appointment.setReminderIsSet(true);
			appointment.setReminderMinutesBeforeStart(30);

			ItemId itemId = service.createItem(appointment);
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
