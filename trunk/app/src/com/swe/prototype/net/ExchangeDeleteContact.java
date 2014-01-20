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
		
		try 
		{
			Service service = new Service(
					"https://mail.fh-aachen.de/EWS/exchange.asmx",
					params[0], params[1]);

			FindItemResponse contactItems = service.findItem(StandardFolder.CONTACTS);

			for (int i = 0; i < contactItems.getItems().size(); i++) {
				if (params[2].equals(contactItems.getItems().get(i).getItemId()))
				{
					System.out.println(contactItems.getItems().get(i)
							.getSubject());
					Response response = service.deleteItem(contactItems
							.getItems().get(i).getItemId(),
							DeleteType.HARD_DELETE);
					return true;
				} 
				else
				{
					System.out.println("Loeschen nicht Erfolgreich: "
							+ contactItems.getItems().get(i));
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
