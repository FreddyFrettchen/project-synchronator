package com.swe.prototype.net;

import android.content.Context;

import com.swe.prototype.models.AccountBase;

public class GoogleAccount extends AccountBase {

	public GoogleAccount(Context context, int refresh_time_sec,
			String username, String password) {
		super(context, refresh_time_sec, username, password);
	}

	@Override
	public void synchronize() {
		// TODO Auto-generated method stub

	}

}