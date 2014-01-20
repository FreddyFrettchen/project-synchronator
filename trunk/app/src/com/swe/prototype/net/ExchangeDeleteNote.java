package com.swe.prototype.net;

import com.independentsoft.exchange.DeleteType;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeDeleteNote extends AsyncTask<String, Void, Boolean>{
	private static String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) {
		try {
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",
					params[0], params[1]);
			FindItemResponse notesItems = service.findItem(StandardFolder.NOTES); 
			for (int i = 0; i < notesItems.getItems().size(); i++) {
				if(notesItems.getItems().get(i).getItemId().toString().equals(params[2])){
					service.deleteItem(notesItems.getItems()
						.get(i).getItemId(), DeleteType.HARD_DELETE);
				}
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getXmlMessage());

			e.printStackTrace();
		}		return false;
	}
}