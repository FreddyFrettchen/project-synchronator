package com.swe.prototype.activities;

import java.util.ArrayList;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.swe.prototype.R;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.EncryptedData;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.net.server.Server.GetDataTask;
import com.swe.prototype.services.SynchronatorService;

public class ListContactsActivity extends BaseActivity{

	private static final String TAG = "ListContactsActivity";

	private ListView listView = null;
	private ContactAdapter adapter;
	private final static int ADD_CONTACT_BUTTON = 0x123;
	private final static int REFRESH_CONTACT_BUTTON = 0x124;
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, "server_data");
	private final String[] PROJECTION = { ServerDataTable.COLUMN_ID,
			ServerDataTable.COLUMN_ID_DATA, ServerDataTable.COLUMN_DATA,
			ServerDataTable.COLUMN_TAG };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontacts);
		invalidateOptionsMenu();
		/*
		 * add_button = (MenuItem)findViewById(R.id.action_add_object);
		 * add_button.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		 * public boolean onMenuItemClick(MenuItem item) { // create new contact
		 * Log.i(TAG,"new contact"); return false; } });
		 */

		/*new DBTools(this).purgeDatabase();
		Intent in = new Intent(this, SynchronatorService.class);
		startService(in);*/

		// Construct the data source
		ArrayList<Contact> arrayOfUsers = new ArrayList<Contact>();

		arrayOfUsers.add(new ServerContact("mark", "123"));
		arrayOfUsers.add(new ServerContact("matthias", "423"));
		arrayOfUsers.add(new ServerContact("friedrich", "777"));

		// Create the adapter to convert the array to views
		adapter = new ContactAdapter(this);
		for (int i = 0; i < this.accounts.size(); i++) {
			adapter.addAll(this.accounts.get(i).getContacts());
		}

		// Attach the adapter to a ListView
		listView = (ListView) findViewById(R.id.contacts_list_view);
		registerForContextMenu(listView);
		listView.setAdapter(adapter);

		/*
		 * server.new GetDataTask() { protected void
		 * onPostExecute(ArrayList<EncryptedData> list) {
		 * super.onPostExecute(list);
		 * 
		 * if(list == null) return;
		 * 
		 * Gson gson = new Gson(); ArrayList<ServerContact> contacts = new
		 * ArrayList<ServerContact>();
		 * 
		 * for (int i = 0; i < list.size(); i++) {
		 * contacts.add(gson.fromJson(list.get(i).data, ServerContact.class)); }
		 * 
		 * adapter.addAll(contacts); adapter.notifyDataSetChanged(); }
		 * }.execute("contacts");
		 */

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ServerContact o = (ServerContact) listView
						.getItemAtPosition(position);
				Log.i(TAG, "clickidi:" + o.toJson());
			}
		});
	}

	// benachrichtigt jeden account seine kontakte zu erneuern
	private void updateContacts() {
		for (int i = 0; i < this.accounts.size(); i++) {
			this.accounts.get(i).updateContacts();
		}
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
			updateContacts();
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
		switch (item.getItemId()) {
		case R.id.edit:
			Log.i(TAG, "edit:");
			return true;
		case R.id.move:
			Log.i(TAG, "move:");
			return true;
		case R.id.delete:
			Log.i(TAG, "delete:");
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
