package com.swe.prototype.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.swe.prototype.R;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;

public class ContactActivity extends BaseActivity {

	private static final String TAG = "ContactActivity";
	
	private TextView name        	= null;
	private TextView phonenumber 	= null;
	private TextView email 		 	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		name = (TextView)findViewById(R.id.contact_name);
		phonenumber = (TextView)findViewById(R.id.contact_phone);
		email = (TextView)findViewById(R.id.contact_email);
		
		name.setText(getIntent().getExtras().getString("name"));
		phonenumber.setText(getIntent().getExtras().getString("phone"));
		email.setText(getIntent().getExtras().getString("email"));
	}
}
