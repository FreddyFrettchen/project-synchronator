package com.swe.prototype.activities;

import android.app.ProgressDialog;
import android.location.Address;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.swe.prototype.R;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.net.server.AsyncDataTask;
import com.swe.prototype.net.server.Server;

public class CreateContactActivity extends BaseActivity {

	private static final String TAG = "CreateContactActivity";
	ProgressDialog dialog = null;
	ListView list_accounts = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);

		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		list_accounts = (ListView) findViewById(R.id.list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		Button save_button = (Button) findViewById(R.id.done_button);
		save_button.setText("Save");
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

		int cntChoice = list_accounts.getCount();
		SparseBooleanArray selected_accounts = list_accounts
				.getCheckedItemPositions();

		for (int i = 0; i < cntChoice; i++) {
			if (selected_accounts.get(i) == true) {
				accounts.getAccounts().get(i).createContact(lastname, firstname, phonenumber, email);
			}
		}

		dialog.dismiss(); 
		finish();
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
