package com.swe.prototype.models;

import java.io.Serializable;
import android.content.Context;

public abstract class BaseData implements Serializable {
	
	protected AccountBase account = null;
	
	public BaseData(AccountBase account) {
		this.account = account;
	}
	
	public void setAccount(AccountBase account){
		this.account = account;
	}
	
	public AccountBase getAccount(){
		return this.account;
	}
	
}
