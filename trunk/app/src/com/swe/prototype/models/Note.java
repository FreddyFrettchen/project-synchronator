package com.swe.prototype.models;

import android.R.string;
import android.content.Context;

public abstract class Note extends BaseData {
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

	public void edit(Context context){
		this.account.editNote(context, this);
	}
}
