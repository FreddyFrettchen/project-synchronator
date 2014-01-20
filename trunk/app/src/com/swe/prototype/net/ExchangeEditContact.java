package com.swe.prototype.net;


import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Property;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeEditContact extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) 
	{
		
		// TODO Auto-generated method stub
		try {
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", params[0], params[1]);

			FindItemResponse response = service.findItem(StandardFolder.CONTACTS);

			for (int i = 0; i < response.getItems().size(); i++) {
				ItemId itemId = response.getItems().get(i).getItemId();
				if(itemId.toString().equals(params[2]))
				{
					Property businessPhonePropertyFN = new Property(ContactPropertyPath.GIVEN_NAME, params[3]);
					Property businessPhonePropertyLN = new Property(ContactPropertyPath.SURNAME, params[4]);
					Property businessPhonePropertyBP = new Property(ContactPropertyPath.BUSINESS_PHONE, params[5]);
					Property businessPhonePropertyEA = new Property(ContactPropertyPath.EMAIL1_ADDRESS, params[6]);
	
					
					itemId = service.updateItem(itemId, businessPhonePropertyFN);
					itemId = service.updateItem(itemId, businessPhonePropertyLN);
					itemId = service.updateItem(itemId, businessPhonePropertyBP);
					itemId = service.updateItem(itemId, businessPhonePropertyEA);
					return true;
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
