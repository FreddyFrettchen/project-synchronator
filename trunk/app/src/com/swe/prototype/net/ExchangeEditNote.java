package com.swe.prototype.net;

import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.NotePropertyPath;
import com.independentsoft.exchange.Property;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeEditNote extends AsyncTask<String, Void, Boolean> {

	private static String TAG = "ExchangeAccount";
	
	@Override
	protected Boolean doInBackground(String... params) {
		try {
			Log.i(TAG, params[0]+ " " +params[1]+
					" " + params[2]+" "+ params[3]+ " " +params[4]);
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx", params[0],
					params[1]);

			Log.i(TAG, "EditNote service");
			FindItemResponse response = service.findItem(StandardFolder.NOTES);

			Log.i(TAG, "response EditNotes");
			for (int i = 0; i < response.getItems().size(); i++) {
				ItemId itemId = response.getItems().get(i).getItemId();
				Log.i(TAG, "response EditNotes itemId");
				if (itemId.toString().equals(params[2])) {
					Property notePropertySubject = new Property(
							NotePropertyPath.SUBJECT, params[3]);
					Property notePropertyBody = new Property(
							NotePropertyPath.BODY, params[4]);

					itemId = service.updateItem(itemId, notePropertySubject);
					itemId = service.updateItem(itemId, notePropertyBody);
					return true;
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}
		return false;
	}
}
