package com.swe.prototype.net;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.google.gdata.client.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.*;
import com.google.gdata.data.acl.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
//import com.google.gdata.data.contacts.Event;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.contacts.*;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import com.swe.prototype.models.google.GoogleContact;
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
	    	primaryPhoneNumber.setPhoneNumber(phonenumber);
	    	primaryPhoneNumber.setRel("http://schemas.google.com/g/2005#work");
	    	primaryPhoneNumber.setPrimary(true);
	    	contact.addPhoneNumber(primaryPhoneNumber);
	    	// Set contact's postal address.
	    	StructuredPostalAddress postalAddress = new StructuredPostalAddress();
	    	/*postalAddress.setStreet(new Street("1600 Amphitheatre Pkwy"));
	    	postalAddress.setCity(new City("Mountain View"));
	    	postalAddress.setRegion(new Region("CA"));
	    	postalAddress.setPostcode(new PostCode("94043"));
	    	postalAddress.setCountry(new Country("US", "United States"));
	    	postalAddress.setFormattedAddress(new FormattedAddress("1600 Amphitheatre Pkwy Mountain View"));
	    	postalAddress.setRel("http://schemas.google.com/g/2005#work");
	    	postalAddress.setPrimary(true);
	    	contact.addStructuredPostalAddress(postalAddress);
	    	*/
	    	/// Ask the service to insert the new entry
	    	URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
	    	ContactEntry createdContact = myService.insert(postUrl, contact);
	    	System.out.println("Contact's ID: " + createdContact.getId());
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    	
	}

	private ArrayList<GoogleContact> getContacts()
	{
	   	try
	   	{
	   		ContactsService myService = new ContactsService("<var>YOUR_APPLICATION_NAME</var>");
	   		myService.setUserCredentials(this.username, this.password);
	   		// Request the feed
	   		URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
	   		ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
	   		// Print the results
	   		ArrayList<GoogleContact> liste = new ArrayList<GoogleContact>();
	   		for (ContactEntry entry : resultFeed.getEntries()) 
	   		{
	   			GoogleContact GC = new GoogleContact();
	   			if (entry.hasName()) 
	   			{
	   				Name name = entry.getName();
	   				if (name.hasGivenName()) 
	   				{
	   					GC.setFirstname(name.getGivenName().getValue());
	   		   		} 
	   				if (name.hasFamilyName()) 
	   				{
	   					GC.setLastname(name.getFamilyName().getValue());
		   			}
	   			} 
	   			for(PhoneNumber phone : entry.getPhoneNumbers())
	   			{
	   				GC.setPhoneumber(phone.getPhoneNumber());
	   			}
	   			for (Email email : entry.getEmailAddresses())
	   			{
	   				GC.setEmail(email.getAddress());
	   			}
	   			GC.setId(entry.getEtag());
	   			liste.add(GC);
	   		}
	   		return liste;
	   	}
	   	catch(Exception e)
	   	{
	   		System.out.println(e);
	 	}
	   	return null;
	}
	
	
	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		Log.i(TAG, "GoogleAdapterContact!");
		System.out.println("ContactAdapterGoogle!");
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context,layout_id);
		adapter.addAll(this.getContacts());
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
		
		GoogleContact gc = (GoogleContact)c;
		try
    	{
	    	ContactsService myService = new ContactsService("<var>YOUR_APPLICATION_NAME</var>");
			myService.setUserCredentials(this.username, this.password);
			// Request the feed
			URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
			ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
			
			List<ContactEntry> list = resultFeed.getEntries();
			for(int i = 0;i< list.size();i++)
			{
				System.out.println(list.get(i).getExtensionLocalName());
				if(list.get(i).getEtag().contains(gc.getID()))
				{
					list.get(i).delete();
				}
			}
    	}
    	catch(Exception e)
    	{
    		
    	}
		
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
