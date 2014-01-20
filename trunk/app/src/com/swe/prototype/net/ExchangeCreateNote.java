package com.swe.prototype.net;

import com.independentsoft.exchange.Body;
import com.independentsoft.exchange.FindItemResponse;
import com.independentsoft.exchange.ItemId;
import com.independentsoft.exchange.Note;
import com.independentsoft.exchange.Service;
import com.independentsoft.exchange.ServiceException;
import com.independentsoft.exchange.StandardFolder;

import android.os.AsyncTask;
import android.util.Log;

public class ExchangeCreateNote extends AsyncTask<String, Void, Boolean> {
	private static final String TAG = "ExchangeAccount";
	@Override
	protected Boolean doInBackground(String... params) 
	{
		
		try 
		{
			Service service = new Service("https://mail.fh-aachen.de/EWS/exchange.asmx", params[0], params[1]);
			  
			Note note = new Note();
			note.setSubject(params[2]); 
			note.setBody(new Body(params[3]));
			note.setWidth(300); 
			note.setLeft(400); 
			note.setTop(200);
			ItemId itemId = service.createItem(note, StandardFolder.NOTES);
			return true;
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
