package com.swe.prototype.models;

import android.R.string;

public abstract class Note extends BaseData {
	public Note(Note n){
	}
	
	public abstract string getNote();
	public abstract String getTitle();
}
