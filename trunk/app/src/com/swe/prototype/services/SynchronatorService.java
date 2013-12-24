package com.swe.prototype.services;

import java.util.ArrayList;

import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.net.server.ServerAccount;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SynchronatorService extends Service {
	
	public int onStartCommand(Intent intent, int flags, int startId){
		// load all accounts and start their synchronisation threads
		ArrayList<AccountBase> accounts = getAccounts();
		for (int i = 0; i < accounts.size(); i++) {
			accounts.get(i).start();
		}
		return startId;
	}
	
	private ArrayList<AccountBase> getAccounts(){
		ArrayList<AccountBase> accounts = new ArrayList<AccountBase>();
		// server is added manually. the other accounts are loaded
		accounts.add(new ServerAccount(this,100, "a@a.de", Security.sha1("123")));
		
		return accounts;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
