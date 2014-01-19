package com.swe.prototype.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.swe.prototype.R;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;

public class ListContactsActivity extends BaseActivity {

	private static final String TAG = "ListContactsActivity";

	private ListView listView = null;
	private ContactAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontacts);
		invalidateOptionsMenu();

		// Create the adapter to convert the array to views
		adapter = new ContactAdapter(this);
		for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
			adapter.addAdapter(this.accounts
					.getAccounts()
					.get(i)
					.getContactAdapter(this,
							android.R.layout.simple_list_item_1));
		}

		// Attach the adapter to a ListView
		listView = (ListView) findViewById(R.id.contacts_list_view);
		registerForContextMenu(listView);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new ViewListItem());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_contacts, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			showAndFinish(CreateContactActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onContextItemSelected(final MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Contact o = (Contact) listView.getAdapter().getItem(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editContact(o);
			return true;
		case R.id.move:
		//	moveContact(o);
			return true;
		case R.id.delete:
			deleteContact(o);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void editContact(Contact c) {
		getSynchronatorApplication().setCurrentContact(c);
		// c.edit(this);
		Intent in = new Intent(this, CreateContactActivity.class);
		startActivity(in);
	}

	public void moveContact(final Contact c) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_move);
		dialog.setTitle("Move Contact to:");

		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		final ListView list_accounts = (ListView) dialog
				.findViewById(R.id.dialog_list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		Button btn_move = (Button) dialog.findViewById(R.id.dialog_button_move);
		Button btn_cancel = (Button) dialog
				.findViewById(R.id.dialog_button_cancel);
		// if button is clicked, close the custom dialog
		btn_move.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i(TAG, "moving contacts ...");
				showToast("Moving contacts...");


				int cntChoice = list_accounts.getCount();
				SparseBooleanArray selected_accounts = list_accounts
						.getCheckedItemPositions();

				if(list_accounts.getCheckedItemCount() != 0) {
					// create new accounts
					for (int i = 0; i < cntChoice; i++) {
						if (selected_accounts.get(i) == true) {
							accounts.getAccounts()
									.get(i)
									.createContact(c.getLastName(), c.getFirstName(),
											c.getPhoneumber(), c.getEmail());
						}
					}
					Log.i(TAG, "moving contacts ...");
					showToast("Moving contacts...");
					
					//delete original 
					c.getAccount().deleteContact(c);
					
					dialog.cancel();
				}

				
				//delete original 
				c.getAccount().deleteContact(c);
				showToast("No server selected.");

			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();
	}

	public void deleteContact(Contact c) {
		c.delete();
	}

	class ViewListItem implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent in = new Intent(parent.getContext(), ContactActivity.class);
			ServerContact o = (ServerContact) listView
					.getItemAtPosition(position);
			in.putExtra("name", o.toString());
			in.putExtra("phone", o.getPhoneumber());
			in.putExtra("email", o.getEmail());
			startActivity(in);
		}
	}
}
