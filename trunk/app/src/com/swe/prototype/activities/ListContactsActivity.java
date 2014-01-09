package com.swe.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.swe.prototype.R;
import com.swe.prototype.adapter.ContactAdapter;
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
			show(CreateContactActivity.class);
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
			moveContact(o);
			return true;
		case R.id.delete:
			deleteContact(o);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void editContact(Contact c) {
		c.edit(this);
	}

	public void moveContact(Contact c) {
		Log.i(TAG, "move:" + c.toString());
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
