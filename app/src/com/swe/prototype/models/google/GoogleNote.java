package com.swe.prototype.models.google;

import android.R.string;

import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Note;

public class GoogleNote extends Note {

	public GoogleNote(AccountBase account) {
		super(account);
	}

	@Override
	public String getNote() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getAccountTag() {
		return "Google";
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean isUpToDate() {
		return true;
	}

}
