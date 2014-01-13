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
	
	private int id_account = 0;
	private int data_id = 0;
	private boolean edit_mode = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
		
		if (getIntent().hasExtra("id_account"))
			id_account = getIntent().getExtras().getInt("id_account");
		
		if (getIntent().hasExtra("data_id"))
			data_id = getIntent().getExtras().getInt("edit_mode");
		
		if (getIntent().hasExtra("edit_mode"))
			edit_mode = getIntent().getExtras().getBoolean("edit_mode");

		if (edit_mode && id_account != 0)
			prefill_fields();

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
				if(correctInputChoise()) {
					initializeDialog("Creating contact...");
					saveContact(v);
				}
			}

			private boolean correctInputChoise() {
				// TODO Auto-generated method stub
				
				if(getEditText(R.id.edit_text_last_name).isEmpty() || getEditText(R.id.edit_text_first_name).isEmpty() || getEditText(R.id.edit_text_phonenumber).isEmpty() || getEditText(R.id.edit_text_email).isEmpty()) {
					this.showShortToast("Alle Felder müssen ausgefüllt sein!");
					return false;
				}
				int cntChoice = list_accounts.getCount();
				SparseBooleanArray selected_accounts = list_accounts
						.getCheckedItemPositions();
				
				for (int i = 0; i < cntChoice; i++) {
					if (selected_accounts.get(i) == true) {
						return true;
					}
				}
				this.showShortToast("Mindestens ein Server muss gewählt sein!");
				return false;
			}
			
			private void showShortToast(String message) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
		
		Button cancel_button = (Button) findViewById(R.id.cancel_button);
		cancel_button.setText("Cancel");
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void prefill_fields(){
		
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

	private void initializeDialog(String message) {
		dialog = ProgressDialog.show(CreateContactActivity.this, "", message,
				true);
		dialog.show();
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}
}
