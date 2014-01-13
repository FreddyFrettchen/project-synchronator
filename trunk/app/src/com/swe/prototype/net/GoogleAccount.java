package com.swe.prototype.net;

import java.net.URL;
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

	private void printAllContacts()
	{
	   	try
	   	{
	   		ContactsService myService = new ContactsService("<var>YOUR_APPLICATION_NAME</var>");
	   		myService.setUserCredentials(this.username, this.password);
	   		// Request the feed
	   		URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
	   		ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
	   		// Print the results
	   		System.out.println(resultFeed.getTitle().getPlainText());
	   		for (ContactEntry entry : resultFeed.getEntries()) 
	   		{
	   		//	Contact GC = new Contact();
	   			if (entry.hasName()) 
	   			{
	   				Name name = entry.getName();
	   				if (name.hasFullName())
	   				{
	   					String fullNameToDisplay = name.getFullName().getValue();
	   					if (name.getFullName().hasYomi()) 
	   					{
	   						fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
	   					}
	   					System.out.println("\\\t\\\t" + fullNameToDisplay);
	   				}
	   				else 
	   				{
	   					System.out.println("\\\t\\\t (no full name found)");
	   				}
	   				if (name.hasNamePrefix()) 
	   				{
	   					System.out.println("\\\t\\\t" + name.getNamePrefix().getValue());
	   				} 
	   				else
	   				{
	   					System.out.println("\\\t\\\t (no name prefix found)");
	   				}
	   				if (name.hasGivenName()) 
	   				{
	   					String givenNameToDisplay = name.getGivenName().getValue();
	   					if (name.getGivenName().hasYomi()) 
	   					{
	   						givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
	   					}
	   					System.out.println("\\\t\\\t" + givenNameToDisplay);
	   				} 
	   				else
	   				{
	   					System.out.println("\\\t\\\t (no given name found)");
	   				}
	   				if (name.hasAdditionalName()) 
	   				{
	   					String additionalNameToDisplay = name.getAdditionalName().getValue();
	   					if (name.getAdditionalName().hasYomi()) 
	   					{
	   						additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
	 					}
	   					System.out.println("\\\t\\\t" + additionalNameToDisplay);
	   				} 
	   				else 
	   				{
	   					System.out.println("\\\t\\\t (no additional name found)");
	   				}
	   				if (name.hasFamilyName()) 
	   				{
	   					String familyNameToDisplay = name.getFamilyName().getValue();
	   					if (name.getFamilyName().hasYomi()) 
	   					{
	   						familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
	   					}
	   					System.out.println("\\\t\\\t" + familyNameToDisplay);
	   				}
	   				else
	   				{
	   					System.out.println("\\\t\\\t (no family name found)");
	   				}
	   				if (name.hasNameSuffix())
	   				{
	   					System.out.println("\\\t\\\t" + name.getNameSuffix().getValue());
	   				} 
	   				else
	   				{
	   					System.out.println("\\\t\\\t (no name suffix found)");
	   				}
	   			} 
	   			else
	   			{
	   				System.out.println("\t (no name found)");
	   			}
	   			System.out.println("Phone numbers:");
	   			for(PhoneNumber phone : entry.getPhoneNumbers())
	   			{
	   				System.out.println(phone.getPhoneNumber());
	   			}
	   			
	   			
	   			System.out.println("Email addresses:");
	   			for (Email email : entry.getEmailAddresses())
	   			{
	   				System.out.print(" " + email.getAddress());
	   				if (email.getRel() != null)
	   				{
	   					System.out.print(" rel:" + email.getRel());
	   				}
	   				if (email.getLabel() != null) 
	   				{
	   					System.out.print(" label:" + email.getLabel());
	   				}
	   				if (email.getPrimary()) 
	   				{
	  					System.out.print(" (primary) ");
	   				}
	   				System.out.print("\n");
	   			}
	   			System.out.println("IM addresses:");
	   			for (Im im : entry.getImAddresses()) 
	   			{
	   				System.out.print(" " + im.getAddress());
	   				if (im.getLabel() != null) 
	   				{
	   					System.out.print(" label:" + im.getLabel());
	   				}
	   				if (im.getRel() != null) 
	   				{
	   					System.out.print(" rel:" + im.getRel());
	   				}
	   				if (im.getProtocol() != null) 
	   				{
	   					System.out.print(" protocol:" + im.getProtocol());
	   				}
	   				if (im.getPrimary())
	   				{
	   					System.out.print(" (primary) ");
	   				}
	   				System.out.print("\n");
	   			}
	   			System.out.println("Groups:");
	   			for (GroupMembershipInfo group : entry.getGroupMembershipInfos())
	   			{
	   				String groupHref = group.getHref();
	   				System.out.println("  Id: " + groupHref);
	   			}
	   			System.out.println("Extended Properties:");
	   			for (ExtendedProperty property : entry.getExtendedProperties())
	   			{
	   				if (property.getValue() != null)
	   				{
	   					System.out.println("  " + property.getName() + "(value) = " +
	   							property.getValue());
	   				} 
	   				else if (property.getXmlBlob() != null) 
	   				{
	   					System.out.println("  " + property.getName() + "(xmlBlob)= " +
	   							property.getXmlBlob().getBlob());
	   				}
	   			}
	   			Link photoLink = entry.getContactPhotoLink();
	   			String photoLinkHref = photoLink.getHref();
	   			System.out.println("Photo Link: " + photoLinkHref);
	   			if (photoLink.getEtag() != null) 
	   			{
	   				System.out.println("Contact Photo's ETag: " + photoLink.getEtag());
	   			}
	   			System.out.println("Contact's ETag: " + entry.getEtag());
	   		}
	   	}
	   	catch(Exception e)
	   	{
	   		System.out.println(e);
	 	}
	}
	
	
	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context,layout_id);
		//GoogleContact GC = new GoogleContact();
		
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
