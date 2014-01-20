package com.swe.prototype.net;

import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.DeleteType;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Note;
import com.independentsoft.exchange.Response;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeDeleteContact extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) 
	{
		Log.i(TAG,"ExchangeDeleteContact_1");
		try 
		{
			Log.i(TAG,"ExchangeDeleteContact_2");
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",params[0], params[1]);
			Log.i(TAG,"ExchangeDeleteContact_3");

			FindItemResponse contactItems = service.findItem(StandardFolder.CONTACTS);
			Log.i(TAG,"ExchangeDeleteContact_4");

			for (int i = 0; i < contactItems.getItems().size(); i++) {
				Log.i(TAG,"ExchangeDeleteContact_5 id="+params[2]);

				
				if (params[2].contains(contactItems.getItems().get(i).getItemId().toString()))
				{
					Log.i(TAG,"ExchangeDeleteContact_6");
					System.out.println(contactItems.getItems().get(i)
							.getSubject());
					Log.i(TAG,"ExchangeDeleteContact_7");
					Response response = service.deleteItem(contactItems
							.getItems().get(i).getItemId(),
							DeleteType.HARD_DELETE);
					Log.i(TAG,"ExchangeDeleteContact_8");
					return true;
				} 
				else
				{
					System.out.println("Loeschen nicht Erfolgreich: "
							+ contactItems.getItems().get(i));
					Log.i(TAG,"ExchangeDeleteContact_9");
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
