package com.swe.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;

import com.swe.prototype.R;

public class ListContactsActivity extends BaseActivity {
	
	private static final String TAG = "ListContactsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontacts);
	}
}
