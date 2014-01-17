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
	private String lastname;
	private String firstname;
	private String id;
	private String phoneumber;
	private String email;
	
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPhoneumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
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
