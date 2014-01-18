package com.swe.prototype.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.independentsoft.exchange.Appointment;
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
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.exchange.ExchangeCalendarEntry;
import com.swe.prototype.models.exchange.ExchangeContact;
import com.swe.prototype.models.exchange.ExchangeNote;

public class ExchangeAccount extends AccountBase {

	private static final String TAG = "ExchangeAccount";

	public static ArrayList<Contact> test = new ArrayList<Contact>();
	public static ArrayList<com.swe.prototype.models.Note> test2 = new ArrayList<com.swe.prototype.models.Note>();
	
	public ExchangeAccount(Context context, int account_id,
			int refresh_time_sec, String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
		Log.i(TAG,"accountdata: " + username + ", "+password );
	}

	@Override
	public void synchronizeNotes() {
		test2 = getNotes();
	}

	@Override
	public void synchronizeContacts() {
		Log.i(TAG, "Synchronize Start Contacts");
		test = getContacts();
	}

	@Override
	public void synchronizeCalendarEntries() {

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
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					this.username, this.password);
			
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
	
	
	public ArrayList<Contact> getContacts(){
	       
		Log.i(TAG, "getContact start");
		ArrayList<Contact> list = new ArrayList<Contact>();
		
			try
	        {
				ExchangeContact excon = new ExchangeContact(this);
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);
	        	Log.i(TAG, "getContacts for response.findItem");
	            FindItemResponse response = service.findItem(StandardFolder.CONTACTS, ContactPropertyPath.getAllPropertyPaths());
	            Log.i(TAG, "getContacts for FORSCHLEIFE");
	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact)
	                {
	                    com.independentsoft.exchange.Contact contact = (com.independentsoft.exchange.Contact) response.getItems().get(i);

	                    Log.i(TAG, contact.getGivenName());
	                    excon.setFirstname(contact.getGivenName());
	                    excon.setLastname(contact.getSurname());
	                    Log.i(TAG, excon.getLastName() + "ExchangeContact");
	                    Log.i(TAG, contact.getSurname() + "independent Contact");
	                    excon.setPhoneumber(contact.getBusinessPhone());
	                    excon.setEmail(contact.getEmail1Address());
	                    excon.setId(contact.getItemId().toString());
	                    list.add(excon);
	                }
	                Log.i(TAG, list.size()+"");
	                Log.i(TAG, list.get(0).getLastName());
	            }
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
		return list;
	}

	public ArrayList<com.swe.prototype.models.Note> getNotes(){
	       
		ArrayList<com.swe.prototype.models.Note> list = new ArrayList<com.swe.prototype.models.Note>();
		ExchangeNote exnote = new ExchangeNote(this);
			try
	        {
	        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",this.username, this.password);

	            FindItemResponse response = service.findItem(StandardFolder.NOTES, ContactPropertyPath.getAllPropertyPaths());

	            for (int i = 0; i < response.getItems().size(); i++)
	            {
	                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Note)
	                {
	                    com.independentsoft.exchange.Note note = (com.independentsoft.exchange.Note) response.getItems().get(i);

	                    exnote.setSubject(note.getSubject());
	                    exnote.setBody(note.getBodyPlainText());
	                    exnote.setID(note.getItemId().toString());
	                    list.add(exnote);
	                }
	            }
	        }
	        catch (ServiceException e)
	        {
	            System.out.println(e.getMessage());
	            System.out.println(e.getXmlMessage());

	            e.printStackTrace();
	        }
		return list;
	}

	public ArrayList<CalendarEntry> getCalendarEntry(){
	       
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
		return list;
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
		adapter.addAll(test2);
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
		ArrayAdapter<CalendarEntry> adapter = new ArrayAdapter<CalendarEntry>(
				context, layout_id);
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
		try {
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
		}
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
		return true;
		
		//Validierung wird vor benutzer eingabe aufgerufen, ich komme nicht dazu meinen benutzer zu testen
		/*
		try
        {
        	Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", this.username, this.password);
        
            FindItemResponse response = service.findItem(StandardFolder.INBOX);
            
         }
        catch (ServiceException e)
        {
         Log.i(TAG, "Benutzer Validierung nicht Erfolgreich");
         return false;
        }*/
	}

}
