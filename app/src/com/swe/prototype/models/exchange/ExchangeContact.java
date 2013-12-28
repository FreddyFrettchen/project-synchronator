package com.swe.prototype.models.exchange;

//import com.independentsoft.exchange.Contact;
import com.independentsoft.exchange.FileAsMapping;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.IsEqualTo;
import com.independentsoft.exchange.StandardFolder;
import com.swe.prototype.models.Contact;


public class ExchangeContact extends Contact {
	public ExchangeContact(Contact c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNumber() {
		// TODO Auto-generated method stub
		return null;
	}

    public boolean createContact(){
        try
        {
            Service service = new Service("https://myserver/ews/Exchange.asmx", "username", "password");

            com.independentsoft.exchange.Contact contact = new com.independentsoft.exchange.Contact();
            contact.setGivenName("Bahos");
            contact.setSurname("Derakie");
            contact.setFileAsMapping(FileAsMapping.LAST_SPACE_FIRST);
            contact.setCompanyName("FH Aachen");
            contact.setBusinessPhone("123456789");
            contact.setEmail1Address("bahos.derakie@alumni.fh-aachen.de");
            contact.setEmail1DisplayName("Bahos Derakie");
            contact.setEmail1DisplayAs("Bahos Derakie");
            contact.setEmail1Type("SMTP");

            ItemId itemId = service.createItem(contact);
        }
        catch (ServiceException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getXmlMessage());

            e.printStackTrace();
        }
        return false;
    }

    public boolean findContact(){
        try
        {
            Service service = new Service("https://myserver/ews/Exchange.asmx", "username", "password");

            IsEqualTo restriction = new IsEqualTo(ContactPropertyPath.GIVEN_NAME, "Bahos");

            FindItemResponse response = service.findItem(StandardFolder.CONTACTS, ContactPropertyPath.getAllPropertyPaths(), restriction);

            for (int i = 0; i < response.getItems().size(); i++)
            {
                if (response.getItems().get(i) instanceof com.independentsoft.exchange.Contact)
                {
                    com.independentsoft.exchange.Contact contact = (com.independentsoft.exchange.Contact) response.getItems().get(i);

                    System.out.println("GivenName = " + contact.getGivenName());
                    System.out.println("Surname = " + contact.getSurname());
                    System.out.println("CompanyName = " + contact.getCompanyName());
                    System.out.println("BusinessAddress = " + contact.getBusinessAddress());
                    System.out.println("BusinessPhone = " + contact.getBusinessPhone());
                    System.out.println("Email1DisplayName = " + contact.getEmail1DisplayName());
                    System.out.println("Email1Address = " + contact.getEmail1Address());
                    System.out.println("----------------------------------------------------------------");
                }
            }
        }
        catch (ServiceException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getXmlMessage());

            e.printStackTrace();
        }
        return false;
    }
}
