package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.independentsoft.exchange.And;
import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.AppointmentPropertyPath;
import com.independentsoft.exchange.CalendarView;
import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.InstanceType;
import com.independentsoft.exchange.RecurringMasterItemId;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ExchangeCalendarTable;
import com.swe.prototype.database.tables.ExchangeContactTable;
import com.swe.prototype.database.tables.ExchangeNoteTable;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.exchange.ExchangeCalendarEntry;
import com.swe.prototype.models.exchange.ExchangeContact;
import com.swe.prototype.models.exchange.ExchangeNote;

public class ExchangeAccount extends AccountBase {

	/**
	 * 
	 */
	private static final String TAG = "ExchangeAccount";

	// Database fields
	// private SQLiteDatabase database;
	// private MySQLiteHelper dbHelper;

	public ExchangeAccount(Context context, int account_id,
			int refresh_time_sec, String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
		Log.i(TAG, "accountdata: " + username + ", " + password);
	}

	@Override
	public void synchronizeNotes() {
		Log.i(TAG, "Synchronize Note start");
		try {
			ContentValues values = new ContentValues();
			Uri contentUri = Uri.withAppendedPath(
					SQLiteDataProvider.CONTENT_URI, "note");

			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);

			FindItemResponse response = service.findItem(StandardFolder.NOTES,
					ContactPropertyPath.getAllPropertyPaths());

			Log.i(TAG, "delete all notes before Synchronize");
			new DBTools(context).purgeNotesTable();

			Log.i(TAG, "Synchronize Note for FORSCHLEIFE");
			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Note) {
					com.independentsoft.exchange.Note note = (com.independentsoft.exchange.Note) response
							.getItems().get(i);
					Log.i(TAG, note.getSubject());
					Log.i(TAG, note.getBodyPlainText());

					values.put("_id", note.getItemId().toString());
					values.put("title", note.getSubject());
					values.put("description", note.getBodyPlainText());

					this.context.getContentResolver()
							.insert(contentUri, values);
				}
			}

			// SaveEntries.saveNotes(list, TAG);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void synchronizeContacts() {
		Log.i(TAG, "Synchronize Start Contacts");
		ArrayList<Contact> list = new ArrayList<Contact>();

		try {
			ContentValues values = new ContentValues();
			Uri contentUri = Uri.withAppendedPath(
					SQLiteDataProvider.CONTENT_URI, "contacts");
			ExchangeContact excon = new ExchangeContact(this);
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
			Log.i(TAG, "SnchronizeContacts for response.findItem");
			FindItemResponse response = service.findItem(
					StandardFolder.CONTACTS,
					ContactPropertyPath.getAllPropertyPaths());
			Log.i(TAG, "Synchronize for FORSCHLEIFE");

			Log.i(TAG, "Delete All COntacts before Synchronize");
			new DBTools(context).purgeContactTable();

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact) {
					com.independentsoft.exchange.Contact contact = (com.independentsoft.exchange.Contact) response
							.getItems().get(i);

					values.put("_id", contact.getItemId().toString());
					values.put("firstname", contact.getGivenName());
					values.put("lastname", contact.getSurname());
					values.put("email", contact.getEmail1Address());
					values.put("phonenumber", contact.getBusinessPhone());
					// excon.setFirstname(contact.getGivenName());
					// excon.setLastname(contact.getSurname());
					Log.i(TAG, excon.getLastName() + "ExchangeContact");
					Log.i(TAG, contact.getSurname() + "independent Contact");
					// excon.setPhoneumber(contact.getBusinessPhone());
					// excon.setEmail(contact.getEmail1Address());
					// excon.setId(contact.getItemId().toString());
					// list.add(excon);

					this.context.getContentResolver()
							.insert(contentUri, values);
				}
			}
			// SaveEntries.saveContacts(list, TAG);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}
	

	private String formatTime(String date)
	{
		String time="";
		boolean AM = true;
		
		if(date.length()>20)
		{
			if(date.charAt(6) == ',')
			{
				if(date.charAt(15)==':')
				{
					for(int i = 0;i<8;i++)
						time = time + date.charAt(i+13);
					if(date.charAt(22) == 'P')
					{
						AM = false;
					}
				}
				else
				{
					time="0";
					for(int i = 0;i<7;i++)
						time = time + date.charAt(i+13);					
					if(date.charAt(21) == 'P')
					{
						AM = false;
					}
				}
			}
			else
			{
				if(date.charAt(14)==':')
				{
					for(int i = 0;i<8;i++)
						time = time + date.charAt(i+12);
					if(date.charAt(21) == 'P')
					{
						AM = false;
					}
				}
				else
				{
					time="0";
					for(int i = 0;i<7;i++)
						time = time + date.charAt(i+12);					
					if(date.charAt(20) == 'P')
					{
						AM = false;
					}
				}
			}	
		}
		if(AM == true)
			return time;
		
		String[] stuecke = time.split(":");
		try
		{
			int h = Integer.parseInt(stuecke[0]);
			h = h +12;
			time=h+":"+stuecke[1]+":"+stuecke[2];
		}
		catch(Exception e)
		{
			
		}
		return time;
	}

	private String formatDate(String date)
	{
		String datum=date;
		if(date.length()>12)
		{
			String monat="";
			if(date.contains("Jan")){monat = "01";}
			if(date.contains("Feb")){monat = "02";}
			if(date.contains("Mar")){monat = "03";}
			if(date.contains("Apr")){monat = "04";}
			if(date.contains("May")){monat = "05";}
			if(date.contains("Jun")){monat = "06";}
			if(date.contains("Jul")){monat = "07";}
			if(date.contains("Aug")){monat = "08";}
			if(date.contains("Sep")){monat = "09";}
			if(date.contains("Oct")){monat = "10";}
			if(date.contains("Nov")){monat = "11";}
			if(date.contains("Dec")){monat = "12";}
			String tag="";
			String jahr="";
			if(date.charAt(6) == ',')
			{
				tag = datum.charAt(4)+""+datum.charAt(5);
				jahr = datum.charAt(8)+""+datum.charAt(9)+""+datum.charAt(10)+""+datum.charAt(11);
			}
			else
			{
				tag = "0"+datum.charAt(4);
				jahr = datum.charAt(7)+""+datum.charAt(8)+""+datum.charAt(9)+""+datum.charAt(10);	
			}
			
			datum = jahr+"-"+monat+"-"+tag;
		}
		return datum;
	}

	@Override
	public void synchronizeCalendarEntries() {
		Log.i(TAG, "Synchronize Start CalendarEntry");
		ArrayList<CalendarEntry> list = new ArrayList<CalendarEntry>();
		try {
			ContentValues values = new ContentValues();
			Uri contentUri = Uri.withAppendedPath(
					SQLiteDataProvider.CONTENT_URI, "calendar");
			ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this);
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
		//muss noch besser gemacht werden!!!!
		/*	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null, endTime = null;
			try {
				startTime = dateFormat.parse("2014-01-01 00:00:00");
				endTime = dateFormat.parse("2016-02-01 00:00:00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			CalendarView view = new CalendarView(startTime, endTime);
			*/
			Log.i(TAG, "SnchronizeCalendar for response.findItem");
			FindItemResponse response = service.findItem(
					StandardFolder.CALENDAR,
					AppointmentPropertyPath.getAllPropertyPaths());
			
			Log.i(TAG, "Synchronice Calendar drop all");
			new DBTools(context).purgeCalendarTable();
			
			Log.i(TAG, "Synchronize for FORSCHLEIFE");
			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Appointment) {
					Appointment appointment = (Appointment) response.getItems()
							.get(i);

					Log.i(TAG, appointment.getSubject());
					values.put("_id", appointment.getItemId().toString());
					values.put("title", appointment.getSubject());
					values.put("body", appointment.getBodyPlainText());
					
					values.put("startTime", this.formatTime(appointment.getStartTime()
							.toLocaleString()));
					values.put("endTime", this.formatTime(appointment.getEndTime()
							.toLocaleString()));
					values.put("startDate", this.formatDate(appointment.getStartTime()
							.toLocaleString()));
					values.put("endDate", this.formatDate(appointment.getEndTime()
							.toLocaleString()));
					
			        if (appointment.getInstanceType() == InstanceType.OCCURRENCE)
                    {
                        RecurringMasterItemId masterId = new RecurringMasterItemId(appointment.getItemId().getId(), appointment.getItemId().getChangeKey());

                        Appointment master = service.getAppointment(masterId);
                    }

					this.context.getContentResolver().insert(contentUri, values);
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	/*
	 * return: Der zur�ckgegebene String muss als erstes Zeichen 'E',haben!!!
	 */
	@Override
	public String toString() {
		return "Exchange (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {

		new ExchangeCreateContact().execute(this.username, this.password,
				firstname, lastname, email, phonenumber);
	}

	/**
	 * read data from database and return a list of objects that can be castet
	 * to contact/calendarEntry/note
	 * 
	 * @return
	 */
	public Cursor getContactData(String tag) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(
				SQLiteDataProvider.CONTENT_URI,
				ExchangeContactTable.TABLE_CONTACTS);
		final String[] projection = { ExchangeContactTable.COLUMN_ID,
				ExchangeContactTable.COLUMN_GIVENNAME,
				ExchangeContactTable.COLUMN_SURNAME,
				ExchangeContactTable.COLUMN_EMAIL,
				ExchangeContactTable.COLUMN_PHONE };
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null,
				selectionArgs, null);
		return cursor;
	}

	/**
	 * read data from database and return a list of objects that can be castet
	 * to contact/calendarEntry/note
	 * 
	 * @return
	 */
	public Cursor getNoteData(String tag) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(
				SQLiteDataProvider.CONTENT_URI, ExchangeNoteTable.TABLE_NOTES);
		final String[] projection = { ExchangeNoteTable.COLUMN_ID,
				ExchangeNoteTable.COLUMN_SUBJECT, ExchangeNoteTable.COLUMN_BODY };
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null,
				selectionArgs, null);
		return cursor;
	}

	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> contactlist = new ArrayList<Contact>();
		Cursor cursor = getContactData("contacts");
		if (cursor.moveToFirst()) {
			do {
				ExchangeContact excon = new ExchangeContact(this);
				excon.setId(cursor.getString(0));
				excon.setFirstname(cursor.getString(1));
				excon.setLastname(cursor.getString(2));
				excon.setEmail(cursor.getString(3));
				excon.setPhoneumber(cursor.getString(4));
				contactlist.add(excon);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return contactlist;
	}

	public ArrayList<com.swe.prototype.models.Note> getNotes() {
		ArrayList<com.swe.prototype.models.Note> notelist = new ArrayList<com.swe.prototype.models.Note>();
		Cursor cursor = getNoteData("note");
		if (cursor.moveToFirst()) {
			do {
				ExchangeNote exnote = new ExchangeNote(this);
				exnote.setID(cursor.getString(0));
				exnote.setTitle(cursor.getString(1));
				exnote.setBody(cursor.getString(2));
				notelist.add(exnote);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return notelist;
	}

	public ArrayList<CalendarEntry> getCalendarEntry() {

		ArrayList<CalendarEntry> calendarlist = new ArrayList<CalendarEntry>();
		Cursor cursor = getCalendarData("calendar");
		if (cursor.moveToFirst()) {
			do {
				ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this);
				excal.setId(cursor.getString(0));
				excal.setSubject(cursor.getString(1));
				excal.setDescription(cursor.getString(2));
				Log.i(TAG,"Aenderung");
				
				Log.i(TAG,"Datumcheck1:"+cursor.getString(3));
				Log.i(TAG,"Datumcheck2:"+cursor.getString(4));
				
				excal.setStartDate(cursor.getString(3).toString());
				excal.setEndDate(cursor.getString(4).toString());
				excal.setStartTime(cursor.getString(5).toString());
				excal.setEndTime(cursor.getString(6).toString());
				
				calendarlist.add(excal);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return calendarlist;

		/*
		 * ArrayList<CalendarEntry> list = new ArrayList<CalendarEntry>();
		 * ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this); try {
		 * Service service = new
		 * Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username,
		 * this.password);
		 * 
		 * FindItemResponse response = service.findItem(StandardFolder.CALENDAR,
		 * ContactPropertyPath.getAllPropertyPaths());
		 * 
		 * for (int i = 0; i < response.getItems().size(); i++) { if
		 * (response.getItems().get(i) instanceof
		 * com.independentsoft.exchange.Appointment) { Appointment appointment =
		 * (Appointment) response.getItems().get(i);
		 * 
		 * excal.setSubject(appointment.getSubject());
		 * excal.setStartDate(appointment.getStartTime());
		 * excal.setEndDate(appointment.getEndTime());
		 * excal.setDescription(appointment.getBodyPlainText());
		 * 
		 * list.add(excal); } } } catch (ServiceException e) {
		 * System.out.println(e.getMessage());
		 * System.out.println(e.getXmlMessage());
		 * 
		 * e.printStackTrace(); } return list;
		 */
	}

	public Cursor getCalendarData(String string) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(
				SQLiteDataProvider.CONTENT_URI,
				ExchangeCalendarTable.TABLE_CALENDAR);
		final String[] projection = { ExchangeCalendarTable.COLUMN_ID,
				ExchangeCalendarTable.COLUMN_SUBJECT,
				ExchangeCalendarTable.COLUMN_BODY,
				ExchangeCalendarTable.COLUMN_STARTDATE,
				ExchangeCalendarTable.COLUMN_ENDDATE, 
				ExchangeCalendarTable.COLUMN_STARTTIME,
				ExchangeCalendarTable.COLUMN_ENDTIME};
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null,
				selectionArgs, null);
		return cursor;
	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context,
				layout_id);
		adapter.addAll(getContacts());
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<com.swe.prototype.models.Note> adapter = new ArrayAdapter<com.swe.prototype.models.Note>(
				context, layout_id);
		adapter.addAll(getNotes());
		/*
		 * try { Service service = new Service(
		 * "https://mail.fh-aachen.de/EWS/exchange.asmx", this.username,
		 * this.password); FindItemResponse response =
		 * service.findItem(StandardFolder.NOTES); for (int i = 0; i <
		 * response.getItems().size(); i++) { adapter.add((Note)
		 * response.getItems()); //
		 * System.out.println(response.getItems().get(i).getSubject()); } }
		 * catch (ServiceException e) { System.out.println(e.getMessage());
		 * System.out.println(e.getXmlMessage());
		 * 
		 * e.printStackTrace(); }
		 */return adapter;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		ArrayAdapter<CalendarEntry> adapter = new ArrayAdapter<CalendarEntry>(
				context, layout_id);
		adapter.addAll(getCalendarEntry());
		return adapter;
	}

	@Override
	public void deleteContact(Contact c) 
	{
		Log.i(TAG,"DeleteContact!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		String id = ((ExchangeContact)c).getID();
		Log.i(TAG,"ID = "+id);
		new ExchangeDeleteContact().execute(this.username, this.password,id);	
	}

	@Override
	public void deleteNote(com.swe.prototype.models.Note n) {
		Log.i(TAG, "Delete Note start");
		ExchangeNote exnote = (ExchangeNote) n;
		Log.i(TAG, "ID = " + exnote.getID());
		new ExchangeDeleteNote().execute(this.username, this.password, exnote.getID().toString());	
	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {
		ExchangeCalendarEntry e = (ExchangeCalendarEntry)ce;
		Log.i(TAG,"delete got called");
		
		new ExchangeDeleteCalendarEntry().execute(this.username,this.password,e.getId());
		// TODO Auto-generated method stub

	}

	@Override
	public void createNote(String title, String text)
	{
		new ExchangeCreateNote().execute(this.username, this.password,title, text);	
	}

	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		new ExchangeCreateCalendar().execute(this.username, this.password,
				startDate, endDate,startTime, endTime,description, repeat + "");
	}

	@Override
	public void editContact(Contact c, String lastname, String firstname,
			String phonenumber, String email)
	{
		
		Log.i(TAG,"EditContact start");
		String id = ((ExchangeContact)c).getID();
		Log.i(TAG,"EditContact id="+id);
			
		//EDITCONTACTS!!!!
		new ExchangeEditContact().execute(this.username,this.password, id, firstname,lastname,phonenumber,email);		
	}

	@Override
	public void editNote(com.swe.prototype.models.Note n, String title,
			String text) {
		ExchangeNote exnote = (ExchangeNote) n;
		new ExchangeEditNote().execute(this.username, this.password, exnote.getID(), title, text);
	}

	@Override
	public void editCalendarEntry(CalendarEntry ce, String startDate,
			String endDate, String startTime, String endTime,
			String description, int repeat) 
	{
		// TODO Auto-generated method stub
		String id = ((ExchangeCalendarEntry)ce).getId();
		new ExchangeEditCalendarEntry().execute(this.username, this.password, 
				id, startDate, startTime,endDate,endTime,description,repeat+"");
		
	}

	@Override
	public boolean validateAccountData() {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
			FindItemResponse response = service.findItem(StandardFolder.INBOX);
			Log.i(TAG, "Benutzer Validierung Erfolgreich");
			return true;
		} catch (ServiceException e) {
			Log.i(TAG, "Benutzer Validierung nicht Erfolgreich");
			return false;
		}
	}
}
