package com.swe.prototype.net;

import java.net.URL;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.extensions.Country;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.FormattedAddress;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostCode;
import com.google.gdata.data.extensions.Region;
import com.google.gdata.data.extensions.Street;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

public class GoogleAccount extends AccountBase {
	private final static String TAG = "GoogleKalender";

	public GoogleAccount(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
	}

	@Override
	public void synchronizeNotes() {
		Log.i(TAG, "Synchronize wurde aufgerufen!");
	}

	@Override
	public void synchronizeContacts() {
		Log.i(TAG, "Synchronize wurde aufgerufen!");
	}
	
	@Override
	public void synchronizeCalendarEntries() {
		Log.i(TAG, "Synchronize wurde aufgerufen!");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Google (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
		// TODO Auto-generated method stub

		Log.i(TAG, "CreateContact!!!!!!!!!!!!!!");
		System.out.println("CreateContact!!!!!!!!!!!!");
		try
    	{
    		ContactsService myService = new ContactsService("<var>YOUR_APPLICATION_NAME</var>");
    		myService.setUserCredentials(this.username, this.password);
    		
	    	// Create the entry to insert.
	    	ContactEntry contact = new ContactEntry();
	    	// Set the contact's name.
	    	Name name = new Name();
	    	final String NO_YOMI = null;
	    	name.setFullName(new FullName(lastname +" "+ firstname, NO_YOMI));
	    	name.setGivenName(new GivenName(firstname, NO_YOMI));
	    	name.setFamilyName(new FamilyName(lastname, NO_YOMI));
	    	contact.setName(name);
	    	//contact.setContent(new PlainTextConstruct("Notes"));
	    	// Set contact's e-mail addresses.
	    	Email primaryMail = new Email();
	    	primaryMail.setAddress(email);
	    	primaryMail.setDisplayName(lastname+" "+firstname);
	    	primaryMail.setRel("http://schemas.google.com/g/2005#home");
	    	primaryMail.setPrimary(true);
	    	contact.addEmailAddress(primaryMail);
	    	// Set contact's phone numbers.
	    	PhoneNumber primaryPhoneNumber = new PhoneNumber();
	    	primaryPhoneNumber.setPhoneNumber("(206)555-1212");
	    	primaryPhoneNumber.setRel("http://schemas.google.com/g/2005#work");
	    	primaryPhoneNumber.setPrimary(true);
	    	contact.addPhoneNumber(primaryPhoneNumber);
	    	// Set contact's postal address.
	    	/*StructuredPostalAddress postalAddress = new StructuredPostalAddress();
	    	postalAddress.setStreet(new Street("1600 Amphitheatre Pkwy"));
	    	postalAddress.setCity(new City("Mountain View"));
	    	postalAddress.setRegion(new Region("CA"));
	    	postalAddress.setPostcode(new PostCode("94043"));
	    	postalAddress.setCountry(new Country("US", "United States"));
	    	postalAddress.setFormattedAddress(new FormattedAddress("1600 Amphitheatre Pkwy Mountain View"));
	    	postalAddress.setRel("http://schemas.google.com/g/2005#work");
	    	postalAddress.setPrimary(true);
	    	contact.addStructuredPostalAddress(postalAddress);
	    	*/// Ask the service to insert the new entry
	    	URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
	    	ContactEntry createdContact = myService.insert(postUrl, contact);
	    	System.out.println("Contact's ID: " + createdContact.getId());
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context,layout_id);
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context,layout_id);
		return adapter;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editContact(Context context, Contact c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editNote(Context context, Note n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editCalendarEntry(Context context, CalendarEntry ce) {
		// TODO Auto-generated method stub
    }
		
	@Override
	public void deleteContact(Contact c) {
		// TODO Auto-generated method stub
    }		

	@Override
	public void deleteNote(Note n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createNote(String title, String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.swe.prototype.models.AccountBase#createCalendarEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 * parameter:
	 * startDate,endDate Format: dd/mm/yyyy
	 * satrTime,endTime Format: hh:mm AM or hh:mm PM
	 * repeat: 0 == no repeat; 1 == every day; 2 == every month; 3 == every year
	 * description: String from description edit text;
	 */
	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
		System.out
				.println("Google: Add CalenderEntry: noch nicht implementiert");

	}
}
