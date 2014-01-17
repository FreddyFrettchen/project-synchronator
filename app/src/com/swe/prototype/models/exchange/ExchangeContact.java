package com.swe.prototype.models.exchange;

import android.content.Context;
import android.util.Log;

import com.independentsoft.exchange.FileAsMapping;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.IsEqualTo;
import com.independentsoft.exchange.StandardFolder;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;

public class ExchangeContact extends Contact {
	
	public static String TAG = "ExchangeContact";
	public String lastname = null;
	public String firstname = null;
	public String id = null;
	public String phoneumber = null;
	public String email = null;
	
	
	public ExchangeContact(AccountBase account){
		super(account);
	}
	
	public String getID()
	{
		return null;
	}
	
	public ExchangeContact(AccountBase account, Contact c) {
		super(account);
	}
/*
		public boolean findContact() {
		try {
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					"bd8299s@ad.fh-aachen.de", "password");

			IsEqualTo restriction = new IsEqualTo(
					ContactPropertyPath.GIVEN_NAME, "Bahos");

			FindItemResponse response = service.findItem(
					StandardFolder.CONTACTS,
					ContactPropertyPath.getAllPropertyPaths(), restriction);

			for (int i = 0; i < response.getItems().size(); i++) {
				if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact) {
					com.independentsoft.exchange.Contact contact = (com.independentsoft.exchange.Contact) response
							.getItems().get(i);

					System.out.println("GivenName = " + contact.getGivenName());
					System.out.println("Surname = " + contact.getSurname());
					System.out.println("CompanyName = "
							+ contact.getCompanyName());
					System.out.println("BusinessAddress = "
							+ contact.getBusinessAddress());
					System.out.println("BusinessPhone = "
							+ contact.getBusinessPhone());
					System.out.println("Email1DisplayName = "
							+ contact.getEmail1DisplayName());
					System.out.println("Email1Address = "
							+ contact.getEmail1Address());
					System.out
							.println("----------------------------------------------------------------");
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
		return false;
	}
*/
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPhoneumber(String phoneumber) {
		this.phoneumber = phoneumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getLastName() {
		return this.lastname;
	}

	@Override
	public String getFirstName() {
		return this.firstname;
	}

	@Override
	public String getPhoneumber() {
		return this.phoneumber;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getAccountTag() {
		return "Exchange";
	}

	@Override
	public void delete() {
		Log.i(TAG, "delete:"+this.toString());
	}
}