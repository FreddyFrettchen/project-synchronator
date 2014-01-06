package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;
import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.FileAsMapping;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Note;
import com.independentsoft.exchange.NoteColor;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;

public class ExchangeAccount extends AccountBase {

	public ExchangeAccount(Context context, int refresh_time_sec,
			String username, String password) {
		super(context, refresh_time_sec, username, password);
	}

	@Override
	public void synchronize() {
		/* TODO Auto-generated method stub */
	}

	@Override
	public String toString() {
		return "Exchange (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);// "bd8299s@ad.fh-aachen.de",
													// "password");

			com.independentsoft.exchange.Contact contact = new com.independentsoft.exchange.Contact();
			contact.setGivenName(firstname);
			contact.setSurname(lastname);
			contact.setFileAsMapping(FileAsMapping.LAST_SPACE_FIRST);
			contact.setBusinessPhone(phonenumber);
			contact.setEmail1Address(email);
			contact.setEmail1Type("SMTP");

			ItemId itemId = service.createItem(contact);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void createNote() {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);// "bd8299s@ad.fh-aachen.de",
													// "password");

			Note note = new Note();
			note.setSubject("My test note");
			note.setBody(new Body("My test note"));
			note.setColor(NoteColor.GREEN);
			note.setIconColor(NoteColor.GREEN);
			note.setHeight(200);
			note.setWidth(300);
			note.setLeft(400);
			note.setTop(200);

			ItemId itemId = service.createItem(note, StandardFolder.NOTES);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void createCalendarEntry() {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);// "bd8299s@ad.fh-aachen.de",
													// "password");

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

	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context, layout_id);
		// adapter.addAll(Contact);
        try
        {
            Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", "bd8299s@ad.fh-aachen.de", "password");

            FindItemResponse response = service.findItem(StandardFolder.CONTACTS);

            for (int i = 0; i < response.getItems().size(); i++)
            {
                adapter.add((Contact)response.getItems());
                //System.out.println(response.getItems().get(i).getSubject());
            }
        }
        catch (ServiceException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getXmlMessage());

            e.printStackTrace();
        }
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context, layout_id);
        try
        {
            Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", "bd8299s@ad.fh-aachen.de", "password");

            FindItemResponse response = service.findItem(StandardFolder.NOTES);

            for (int i = 0; i < response.getItems().size(); i++)
            {
                adapter.add((Note)response.getItems());
                //System.out.println(response.getItems().get(i).getSubject());
            }
        }
        catch (ServiceException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getXmlMessage());

            e.printStackTrace();
        }
        return adapter;
    }


	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
        ArrayAdapter<CalendarEntry> adapter = new ArrayAdapter<CalendarEntry>(context, layout_id);
        try
        {
            Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", "bd8299s@ad.fh-aachen.de", "password");

            FindItemResponse response = service.findItem(StandardFolder.CALENDAR);

            for (int i = 0; i < response.getItems().size(); i++)
            {
                adapter.add((CalendarEntry)response.getItems());
                //System.out.println(response.getItems().get(i).getSubject());
            }
        }
        catch (ServiceException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getXmlMessage());

            e.printStackTrace();
        }
		return adapter;
	}

	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		System.out
				.println("Exchnage Add CalenderEntry: noch nicht implementiert");

	}

}
