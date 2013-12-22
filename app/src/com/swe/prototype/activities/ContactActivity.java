package com.swe.prototype.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.swe.prototype.R;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;

public class ContactActivity extends BaseActivity {

	private static final String TAG = "ContactActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

	}
}
