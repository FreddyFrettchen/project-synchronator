package com.swe.prototype.net;

import com.independentsoft.exchange.AppointmentPropertyPath;
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

public class ExchangeDeleteCalendarEntry extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) 
	{
		try 
		{
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",params[0], params[1]);
		
		    FindItemResponse response = service.findItem(StandardFolder.CALENDAR, AppointmentPropertyPath.getAllPropertyPaths());

			for (int i = 0; i < response.getItems().size(); i++) {
							
				if (params[2].contains(response.getItems().get(i).getItemId().toString()))
				{
					Response responses = service.deleteItem(response.getItems().get(i).getItemId(),DeleteType.HARD_DELETE);
					return true;
				} 
				else
				{
				}
			}
			return false;
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
