package com.swe.prototype.net;

import com.independentsoft.exchange.FileAsMapping;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeCreateContact extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) {

		// new ExchangeCreateContact().execute();
		Log.i(TAG, "CreateContact Start");
		try {
			
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					params[0], params[1]);
			Log.i(TAG, "CreateContact nach serviceanfrage");

			FindItemResponse respone = service
					.findItem(StandardFolder.CONTACTS);

			com.independentsoft.exchange.Contact contact = new com.independentsoft.exchange.Contact();
			Log.i(TAG, params[2]);
			contact.setGivenName(params[2]);
			contact.setSurname(params[3]);
			contact.setFileAsMapping(FileAsMapping.LAST_SPACE_FIRST);
			contact.setBusinessPhone(params[5]);
			contact.setEmail1Address(params[4]);

			service.createItem(contact);
			return true;
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
		return false;
	}

}
