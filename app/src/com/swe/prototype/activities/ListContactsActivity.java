package com.swe.prototype.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;

import java.util.ArrayList;

public class ListContactsActivity extends BaseActivity {

	private static final String TAG = "ListContactsActivity";

	private ListView listView = null;
	private ContactAdapter adapter;
	private final static int ADD_CONTACT_BUTTON = 0x123;
	private final static int REFRESH_CONTACT_BUTTON = 0x124;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontacts);
		invalidateOptionsMenu();

		// Create the adapter to convert the array to views
		adapter = new ContactAdapter(this);
		for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
			adapter.addAdapter(this.accounts.getAccounts().get(i)
					.getContactAdapter(this,android.R.layout.simple_list_item_1));
		}

		// Attach the adapter to a ListView
		listView = (ListView) findViewById(R.id.contacts_list_view);
		registerForContextMenu(listView);
		listView.setAdapter(adapter);

		final Intent in = new Intent(this, ContactActivity.class);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ServerContact o = (ServerContact) listView
						.getItemAtPosition(position);
				Log.i(TAG, "clickidi:" + o.toJson());
				startActivity(in);
			}
		});
	}

	// benachrichtigt jeden account seine kontakte zu erneuern
	private void refreshContacts() {
		/*
		 * for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
		 * this.accounts.getAccounts().get(i).updateContacts(); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ADD_CONTACT_BUTTON, 0, "add");
		menu.add(0, REFRESH_CONTACT_BUTTON, 0, "refresh");
		// Inflate the menu; this adds items to the action bar if it is present.
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
		case REFRESH_CONTACT_BUTTON:
			Log.i(TAG, "syncing contacts");
			refreshContacts();
			return true;
		case ADD_CONTACT_BUTTON:
			show(CreateContactActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onContextItemSelected(final MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		
		Contact o = (Contact)listView.getAdapter().getItem(info.position);
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
	
	public void editContact(Contact c){
		Log.i(TAG, "edit:"+c.toString());
	}
	
	public void moveContact(Contact c){
		Log.i(TAG, "move:"+c.toString());
	}
	
	public void deleteContact(Contact c){
		Log.i(TAG, "delete:"+c.toString());
	}
}
