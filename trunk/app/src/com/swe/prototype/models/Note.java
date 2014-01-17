package com.swe.prototype.models;

import java.io.Serializable;

import android.R.string;
import android.content.Context;

public abstract class Note extends BaseData implements Serializable{
	public Note(AccountBase account){
		super(account);
	}

	public abstract String getNote();

	public abstract String getTitle();

	public abstract String getAccountTag();
	
	public abstract String toString();
	
	public void delete(){
		this.account.deleteNote(this);
	}
}
