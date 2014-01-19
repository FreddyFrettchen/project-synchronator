package com.swe.prototype.activities;

import java.util.Currency;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.prototype.R;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.net.server.AsyncDataTask;

public class CreateContactActivity extends BaseActivity {

	private static final String TAG = "CreateContactActivity";
	ProgressDialog dialog = null;
	ListView list_accounts = null;

	Contact edit_contact = null;
	Boolean edit_mode = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);

		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		list_accounts = (ListView) findViewById(R.id.list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		edit_contact = getSynchronatorApplication().getCurrentContact();
		Button save_button = (Button) findViewById(R.id.done_button);

		if (edit_contact != null) {
			edit_mode = true;
			prefill_fields();
			save_button.setText("Edit");
			getActionBar().setTitle("Edit Contact");  
		} else {
			save_button.setText("Save");
		}

		save_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (correctInputChoise()) {
					if (edit_mode) {
						initializeDialog("Editing contact...");
						editContact(v);
					} else {
						initializeDialog("Creating contact...");
						saveContact(v);
					}
				}
			}

			private boolean correctInputChoise() {
				if ((getEditText(R.id.edit_text_last_name).isEmpty() && getEditText(R.id.edit_text_first_name).isEmpty()) || (getEditText(R.id.edit_text_phonenumber).isEmpty()	&& getEditText(R.id.edit_text_email).isEmpty())) {
					this.showShortToast("Mindestens Vorname oder Nachname und Telefonnummer oder Email Adresse m�ssen angegeben werden!");
					return false;
				}

				if (edit_mode) {
					return true;
				} else {

					int cntChoice = list_accounts.getCount();
					SparseBooleanArray selected_accounts = list_accounts
							.getCheckedItemPositions();

					for (int i = 0; i < cntChoice; i++) {
						if (selected_accounts.get(i) == true) {
							return true;
						}
					}
					this.showShortToast("At least one server has to be selected!");
				}
				return false;
			}

			private void showShortToast(String message) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		});

		Button cancel_button = (Button) findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				getSynchronatorApplication().setCurrentContact(null);
				toContacts();
			}
		});
	}
	
	private void toContacts() {
		startActivity(new Intent(this, ListContactsActivity.class));
		finish();
	}

	private void prefill_fields() {
		setEditText(R.id.edit_text_last_name, edit_contact.getLastName());
		setEditText(R.id.edit_text_first_name, edit_contact.getFirstName());
		setEditText(R.id.edit_text_phonenumber, edit_contact.getPhoneumber());
		setEditText(R.id.edit_text_email, edit_contact.getEmail());
		((TextView) findViewById(R.id.text_save_to)).setVisibility(4);
		list_accounts.setVisibility(4);
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
				accounts.getAccounts().get(i)
						.createContact(lastname, firstname, phonenumber, email);
			}
		}

		dialog.dismiss();
		showAndFinish(ListContactsActivity.class);
	}

	private void editContact(View v) {
		String lastname = getEditText(R.id.edit_text_last_name);
		String firstname = getEditText(R.id.edit_text_first_name);
		String phonenumber = getEditText(R.id.edit_text_phonenumber);
		String email = getEditText(R.id.edit_text_email);

		edit_contact.getAccount().editContact(edit_contact, lastname,
				firstname, phonenumber, email);

		dialog.dismiss();
		getSynchronatorApplication().setCurrentContact(null);
		showAndFinish(ListContactsActivity.class);
	}

	private void initializeDialog(String message) {
		dialog = ProgressDialog.show(CreateContactActivity.this, "", message,
				true);
		dialog.show();
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}
}
