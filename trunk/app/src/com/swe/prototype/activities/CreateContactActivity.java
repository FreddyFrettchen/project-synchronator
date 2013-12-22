package com.swe.prototype.activities;

import android.app.ProgressDialog;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.swe.prototype.R;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.net.server.AsyncDataTask;
import com.swe.prototype.net.server.Server;

public class CreateContactActivity extends BaseActivity {

	private static final String TAG = "CreateContactActivity";
	ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);

		Button save_button = (Button) findViewById(R.id.done_button);
		save_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initializeDialog("Creating contact...");
				saveContact(v);
			}
		});
	}

	private void saveContact(View v) {
		String lastname = getEditText(R.id.edit_text_last_name);
		String firstname = getEditText(R.id.edit_text_first_name);
		String phonenumber = getEditText(R.id.edit_text_phonenumber);
		String email = getEditText(R.id.edit_text_email);

		server.new AddDataTask() {
			protected void onPostExecute(Boolean success) {
				super.onPostExecute(success);
				dialog.dismiss();
				finish();
			}
		}.execute("contact", new ServerContact(lastname, phonenumber).toJson());
	}

	private void initializeDialog(String message) {
		dialog = ProgressDialog.show(CreateContactActivity.this, "", message,
				true);
		dialog.show();
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}
}
