package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.google.gdata.data.spreadsheet.Column;
import com.independentsoft.exchange.Appointment;
import com.independentsoft.exchange.AppointmentPropertyPath;
import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.DeleteType;
import com.independentsoft.exchange.FileAsMapping;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.IsEqualTo;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Note;
import com.independentsoft.exchange.NoteColor;
import com.independentsoft.exchange.NotePropertyPath;
import com.independentsoft.exchange.Property;
import com.independentsoft.exchange.Response;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ExchangeCalendarTable;
import com.swe.prototype.database.tables.ExchangeContactTable;
import com.swe.prototype.database.tables.ExchangeNoteTable;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.exchange.ExchangeCalendarEntry;
import com.swe.prototype.models.exchange.ExchangeContact;
import com.swe.prototype.models.exchange.ExchangeNote;
import com.swe.prototype.models.server.ServerCalendarEntry;

public class ExchangeAccount extends AccountBase{

	/**
	 * 
	 */
	private static final String TAG = "ExchangeAccount";
	  
	// Database fields
	//	private SQLiteDatabase database;
	//	private MySQLiteHelper dbHelper;
	  
	public ExchangeAccount(Context context, int account_id,
			int refresh_time_sec, String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
		Log.i(TAG,"accountdata: " + username + ", "+password );
	}

	@Override
	public void synchronizeNotes() {
		Log.i(TAG, "Synchronize Note start");
			try
	        {
				ContentValues values = new ContentValues();
				Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
						"note");
				
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);

	            FindItemResponse response = service.findItem(StandardFolder.NOTES, ContactPropertyPath.getAllPropertyPaths());
	            Log.i(TAG, "Synchronize Note for FORSCHLEIFE");
	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Note)
	                {
	                    com.independentsoft.exchange.Note note = (com.independentsoft.exchange.Note) response.getItems().get(i);
	                    Log.i(TAG, note.getSubject());
	                    Log.i(TAG, note.getBodyPlainText());
	                 
	                    values.put("_id", note.getItemId().toString());
	                    values.put("title", note.getSubject());
	                    values.put("description", note.getBodyPlainText());
	                    /*exnote.setSubject(note.getSubject());
	                    exnote.setBody(note.getBodyPlainText());
	                    exnote.setID(note.getItemId().toString());
	                    list.add(exnote);*/
	                    this.context.getContentResolver().insert(contentUri,values);
	                }
	            }
	            
	            //SaveEntries.saveNotes(list, TAG);
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
	}

	@Override
	public void synchronizeContacts() {
		Log.i(TAG, "Synchronize Start Contacts");
		ArrayList<Contact> list = new ArrayList<Contact>();

			try
	        {
				ContentValues values = new ContentValues();
				Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
						"contacts");
				ExchangeContact excon = new ExchangeContact(this);
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);
	        	Log.i(TAG, "SnchronizeContacts for response.findItem");
	            FindItemResponse response = service.findItem(StandardFolder.CONTACTS, ContactPropertyPath.getAllPropertyPaths());
	            Log.i(TAG, "Synchronize for FORSCHLEIFE");
	      
	            new DBTools(context).purgeContactTable();
	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact)
	                {
	                    com.independentsoft.exchange.Contact contact = (com.independentsoft.exchange.Contact) response.getItems().get(i);

	                    values.put("_id", contact.getItemId().toString());
	                    values.put("firstname", contact.getGivenName());
	                    values.put("lastname", contact.getSurname());
	                    values.put("email", contact.getEmail1Address());
	                    values.put("phonenumber", contact.getBusinessPhone());
	                    //excon.setFirstname(contact.getGivenName());
	                    //excon.setLastname(contact.getSurname());
	                    Log.i(TAG, excon.getLastName() + "ExchangeContact");
	                    Log.i(TAG, contact.getSurname() + "independent Contact");
	                    //excon.setPhoneumber(contact.getBusinessPhone());
	                    //excon.setEmail(contact.getEmail1Address());
	                    //excon.setId(contact.getItemId().toString());
	                    //list.add(excon);
	                    
	                    this.context.getContentResolver().insert(contentUri,values);
	                }
	            }
	            //SaveEntries.saveContacts(list, TAG);
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
	}

	@Override
	public void synchronizeCalendarEntries() {
		Log.i(TAG, "Synchronize Start CalendarEntry");
		ArrayList<CalendarEntry> list = new ArrayList<CalendarEntry>();
			try
	        {
				ContentValues values = new ContentValues();
				Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
						"calendar");
				ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this);
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);
	        	Log.i(TAG, "SnchronizeCalendar for response.findItem");
	            FindItemResponse response = service.findItem(StandardFolder.CALENDAR, AppointmentPropertyPath.getAllPropertyPaths());
	            Log.i(TAG, "Synchronize for FORSCHLEIFE");
	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Appointment)
	                {
	                    Appointment appointment = (Appointment) response.getItems().get(i);

	                    values.put("_id", appointment.getItemId().toString());
	                    values.put("title", appointment.getSubject());
	                    values.put("body", appointment.getBodyPlainText());
	                    values.put("startTime", appointment.getStartTime().toString());
	                    values.put("endTime", appointment.getEndTime().toString());
	                    values.put("repeat", appointment.getRecurrencePattern());
	                    
	                    this.context.getContentResolver().insert(contentUri,values);
	                }
	            }
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
	}

	/* 
	 * return: Der zur�ckgegebene String muss als erstes Zeichen 'E',haben!!!
	 *  
	 */
	@Override
	public String toString() {
		return "Exchange (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
	/*	try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
			
			com.independentsoft.exchange.Contact contact = new com.independentsoft.exchange.Contact();
			contact.setGivenName(firstname);
			contact.setSurname(lastname);
			//contact.setFileAsMapping(FileAsMapping.LAST_SPACE_FIRST);
			contact.setBusinessPhone(phonenumber);
			contact.setEmail1Address(email);
		//	contact.setEmail1Type("SMTP");

			ItemId itemId = service.createItem(contact);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}*/
	}
	
	/**
	 * read data from database and return a list of objects that can
	 * be castet to contact/calendarEntry/note
	 * 
	 * @return
	 */
	public Cursor getContactData(String tag) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,ExchangeContactTable.TABLE_CONTACTS);
		final String[] projection = { ExchangeContactTable.COLUMN_ID,ExchangeContactTable.COLUMN_GIVENNAME, 
				ExchangeContactTable.COLUMN_SURNAME, ExchangeContactTable.COLUMN_EMAIL, ExchangeContactTable.COLUMN_PHONE};
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null ,selectionArgs, null);
		return cursor;
	}

	/**
	 * read data from database and return a list of objects that can
	 * be castet to contact/calendarEntry/note
	 * 
	 * @return
	 */
	public Cursor getNoteData(String tag) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,ExchangeNoteTable.TABLE_NOTES);
		final String[] projection = { ExchangeNoteTable.COLUMN_ID,ExchangeNoteTable.COLUMN_SUBJECT, 
				ExchangeNoteTable.COLUMN_BODY};
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null ,selectionArgs, null);
		return cursor;
	}
	
	public ArrayList<Contact> getContacts(){
	    ArrayList<Contact> contactlist = new ArrayList<Contact>();
		Cursor cursor = getContactData("contacts");
		if(cursor.moveToFirst()){
		do{
			ExchangeContact excon = new ExchangeContact(this);
			excon.setId(cursor.getString(0));
			excon.setFirstname(cursor.getString(1));
			excon.setLastname(cursor.getString(2));
			excon.setEmail(cursor.getString(3));
			excon.setPhoneumber(cursor.getString(4));
			contactlist.add(excon);
		}while(cursor.moveToNext());
		}
		cursor.close();
		return contactlist;
	}

	public ArrayList<com.swe.prototype.models.Note> getNotes(){
		ArrayList<com.swe.prototype.models.Note> notelist = new ArrayList<com.swe.prototype.models.Note>();		
		Cursor cursor = getNoteData("note");
		if(cursor.moveToFirst()){
		do{
			ExchangeNote exnote = new ExchangeNote(this);
			exnote.setID(cursor.getString(0));
			exnote.setTitle(cursor.getString(1));
			exnote.setBody(cursor.getString(2));
			notelist.add(exnote);  
		}while(cursor.moveToNext());
		}
		cursor.close();
		return notelist;
	}

	public ArrayList<CalendarEntry> getCalendarEntry(){
	   
	    ArrayList<CalendarEntry> calendarlist = new ArrayList<CalendarEntry>();
		Cursor cursor = getCalendarData("calendar");
		if(cursor.moveToFirst()){
		do{
			ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this);
			excal.setId(cursor.getString(0));
			excal.setSubject(cursor.getString(1));
			excal.setDescription(cursor.getString(2));
			excal.setStartDate(cursor.getString(3).toString());
			excal.setEndDate(cursor.getString(4).toString());
			excal.setRepeat(cursor.getInt(5));
			calendarlist.add(excal);
		}while(cursor.moveToNext());
		}
		return calendarlist;
		
		/*   
		ArrayList<CalendarEntry> list = new ArrayList<CalendarEntry>();
		ExchangeCalendarEntry excal = new ExchangeCalendarEntry(this);
			try
	        {
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);

	            FindItemResponse response = service.findItem(StandardFolder.CALENDAR, ContactPropertyPath.getAllPropertyPaths());

	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Appointment)
	                {
	                	Appointment appointment = (Appointment) response.getItems().get(i);
	                
	                	excal.setSubject(appointment.getSubject());
	                	excal.setStartDate(appointment.getStartTime());
	                	excal.setEndDate(appointment.getEndTime());
	                	excal.setDescription(appointment.getBodyPlainText());
	                	
	                	list.add(excal);
	                }
	            }
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
		return list;*/
	}

	
	private Cursor getCalendarData(String string) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,ExchangeCalendarTable.TABLE_CALENDAR);
		final String[] projection = { ExchangeCalendarTable.COLUMN_ID,ExchangeCalendarTable.COLUMN_SUBJECT, 
				ExchangeCalendarTable.COLUMN_BODY, ExchangeCalendarTable.COLUMN_STARTTIME, ExchangeCalendarTable.COLUMN_ENDTIME,
				ExchangeCalendarTable.COLUMN_REPEAT};
		String[] selectionArgs = null;
		Cursor cursor = resolver.query(dataUri, projection, null ,selectionArgs, null);
		return cursor;
	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context, layout_id);
		adapter.addAll(getContacts());
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<com.swe.prototype.models.Note> adapter = new ArrayAdapter<com.swe.prototype.models.Note>(context, layout_id);
		adapter.addAll(getNotes());
		/*try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
			FindItemResponse response = service.findItem(StandardFolder.NOTES);
			for (int i = 0; i < response.getItems().size(); i++) {
				adapter.add((Note) response.getItems());
				// System.out.println(response.getItems().get(i).getSubject());
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
*/		return adapter;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		ArrayAdapter<CalendarEntry> adapter = new ArrayAdapter<CalendarEntry>(context, layout_id);
		adapter.addAll(getCalendarEntry());
		/*	try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);

			FindItemResponse response = service
					.findItem(StandardFolder.CALENDAR);

			for (int i = 0; i < response.getItems().size(); i++) {
				adapter.add((CalendarEntry) response.getItems());
				// System.out.println(response.getItems().get(i).getSubject());
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
*/		return adapter;
	}

	@Override
	public void deleteContact(Contact c) {
		try {
			String vergleichsString = "";
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", this.username, this.password);

			FindItemResponse contactItems = service.findItem(StandardFolder.CONTACTS);

			// falls vorname oder nachname nicht angegeben ist, wird das
			// leerzeichen nicht mit abgefragt
			if (!(c.getFirstName().isEmpty() || c.getLastName().isEmpty())) {
				vergleichsString = c.getFirstName() + c.getLastName();
			}

			for (int i = 0; i < contactItems.getItems().size(); i++) {
				if (contactItems.getItems().get(i).getSubject()
						.equals(vergleichsString)) {
					System.out.println(contactItems.getItems().get(i)
							.getSubject());
					Response response = service.deleteItem(contactItems
							.getItems().get(i).getItemId(),
							DeleteType.HARD_DELETE);
				} else {
					System.out.println("Loeschen nicht Erfolgreich: "
							+ contactItems.getItems().get(i));
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void deleteNote(com.swe.prototype.models.Note n) {
		// TODO Auto-generated method stub
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);

			IsEqualTo restriction = new IsEqualTo(NotePropertyPath.SUBJECT,
					n.getTitle());

			FindItemResponse notesItems = service.findItem(
					StandardFolder.NOTES, restriction);

			for (int i = 0; i < notesItems.getItems().size(); i++) {
				Response response = service.deleteItem(notesItems.getItems()
						.get(i).getItemId(), DeleteType.HARD_DELETE);
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}

	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createNote(String title, String text) {
	/*	try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);// "bd8299s@ad.fh-aachen.de",
													// "password");

			Note note = new Note();
			note.setSubject(title);
			note.setBody(new Body(text));
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
		}*/
	}

	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);// "bd8299s@ad.fh-aachen.de",
													// "password");

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date _startTime = dateFormat.parse(startDate + " " + startTime);// "2014-02-25 16:00:00");
			Date _endTime = dateFormat.parse(endDate + " " + endTime);// "2014-02-25 18:00:00");

			Appointment appointment = new Appointment();
			appointment.setSubject("");
			appointment.setBody(new Body(description));
			appointment.setStartTime(_startTime);
			appointment.setEndTime(_endTime);
			appointment.setLocation(description);
			appointment.setReminderIsSet(true);
			appointment.setReminderMinutesBeforeStart(30);
			// appointment.setReminderNextTime(repeat); Reminder noch finden und
			// repeat??? was uebergibt es mir

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
	public void editContact(Contact c, String lastname, String firstname,
			String phonenumber, String email) {
		// TODO Auto-generated method stub
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);

			IsEqualTo restriction = new IsEqualTo(
					ContactPropertyPath.EMAIL1_ADDRESS, c.getEmail());

			FindItemResponse response = service.findItem(
					StandardFolder.CONTACTS, restriction);

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact) {
					ItemId itemId = response.getItems().get(i).getItemId();

					Property businessPhonePropertyFN = new Property(
							ContactPropertyPath.GIVEN_NAME, c.getFirstName());
					Property businessPhonePropertyLN = new Property(
							ContactPropertyPath.SURNAME, c.getLastName());
					Property businessPhonePropertyBP = new Property(
							ContactPropertyPath.BUSINESS_PHONE,
							c.getPhoneumber());

					itemId = service
							.updateItem(itemId, businessPhonePropertyFN);
					itemId = service
							.updateItem(itemId, businessPhonePropertyLN);
					itemId = service
							.updateItem(itemId, businessPhonePropertyBP);
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
	}

	@Override
	public void editNote(com.swe.prototype.models.Note n, String title,
			String text) {
		// TODO Auto-generated method stub
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);

			IsEqualTo restriction = new IsEqualTo(NotePropertyPath.SUBJECT,
					n.getTitle());

			FindItemResponse response = service.findItem(StandardFolder.NOTES,
					restriction);

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Note) {
					ItemId itemId = response.getItems().get(i).getItemId();

					Property noteProperty = new Property(NotePropertyPath.BODY,
							n.getNote());

					itemId = service.updateItem(itemId, noteProperty);
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}

	}

	@Override
	public void editCalendarEntry(CalendarEntry ce, String startDate,
			String endDate, String startTime, String endTime,
			String description, int repeat) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateAccountData() {
		
		//Validierung wird vor benutzer eingabe aufgerufen, ich komme nicht dazu meinen benutzer zu testen
		
		try
        {
        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", this.username, this.password);
            FindItemResponse response = service.findItem(StandardFolder.INBOX);
            Log.i(TAG, "Benutzer Validierung Erfolgreich");
            return true;
         }
        catch (ServiceException e)
        {
        	Log.i(TAG, "Benutzer Validierung nicht Erfolgreich");
        	return false;
        }
	}

}
