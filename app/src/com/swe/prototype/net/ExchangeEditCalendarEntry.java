package com.swe.prototype.net;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.independentsoft.exchange.AppointmentPropertyPath;
import com.independentsoft.exchange.ContactPropertyPath;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Property;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;
import com.swe.prototype.models.exchange.ExchangeCalendarEntry;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeEditCalendarEntry extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) 
	{		
		try
		{
			Log.i(TAG, "ExchangeEditCalendar start!!!!");
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx",params[0], params[1]);

		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		    Date newStartTime = dateFormat.parse(params[3]+" "+params[4]);
		    Date newEndTime = dateFormat.parse(params[5]+" "+params[6]);
		    Log.i(TAG, "ExchangeEditCalendar FOR Response!!!!");
		    FindItemResponse response = service.findItem(StandardFolder.CALENDAR, AppointmentPropertyPath.getAllPropertyPaths());
		    Log.i(TAG, "ExchangeEditCalendar FOR FORSCHLEIFE!!!!");
		    for (int i = 0; i < response.getItems().size(); i++)
		    {
		    	Log.i(TAG, "ExchangeEditCalendar IN DER FORSCHLEIFE!!!!");
		    	if (response.getItems().get(i).toString().contains(params[2]))
		        {
		    		ItemId itemId = response.getItems().get(i).getItemId();

		            Property startTimeProperty = new Property(AppointmentPropertyPath.START_TIME, newStartTime);
		            Property endTimeProperty = new Property(AppointmentPropertyPath.END_TIME, newEndTime);
		            Property textProperty = new Property(AppointmentPropertyPath.SUBJECT,params[7]);
		            Property titleProperty = new Property(AppointmentPropertyPath.BODY_PLAIN_TEXT,params[7]);
		            
		            
		            List<Property> properties = new ArrayList<Property>();
		            properties.add(startTimeProperty);
		            properties.add(endTimeProperty);
		            properties.add(textProperty);
		            properties.add(titleProperty);

		            itemId = service.updateItem(itemId, properties);
		            return true;
		        }
		    }
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return false;
		
	}
}
