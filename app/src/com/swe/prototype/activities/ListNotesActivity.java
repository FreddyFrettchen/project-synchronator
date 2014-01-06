package com.swe.prototype.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.swe.prototype.R;
import com.swe.prototype.adapter.NoteAdapter;
import com.swe.prototype.models.Contact;

public class ListNotesActivity extends BaseActivity {

	private static final String TAG = "ListNotesActivity";

	private ListView listView = null;
	private NoteAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listnotes);
		invalidateOptionsMenu();
		// Create the adapter to convert the array to views
		adapter = new NoteAdapter(this);
		for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
			adapter.addAdapter(this.accounts.getAccounts().get(i)
					.getNotesAdapter(this, android.R.layout.simple_list_item_1));
		}

		// Attach the adapter to a ListView
		listView = (ListView) findViewById(R.id.note_list_view);
		registerForContextMenu(listView);
		listView.setAdapter(adapter);
	}

	protected void createNote() {
		Intent intent = new Intent(ListNotesActivity.this,
				ChangeNoteActivity.class);
		Bundle b = new Bundle();
		b.putBoolean("createNewNote", true);
		intent.putExtras(b);
		startActivity(intent);
		finish();
	}

	@Override
	protected void addClicked() {
		this.createNote();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_contacts, menu);
	}

	public boolean onContextItemSelected(final MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Contact o = (Contact) listView.getAdapter().getItem(info.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editNote(o);
			return true;
		case R.id.move:
			moveNote(o);
			return true;
		case R.id.delete:
			deleteNote(o);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void editNote(Contact c) {
		Log.i(TAG, "edit:" + c.toString());
	}

	public void moveNote(Contact c) {
		Log.i(TAG, "move:" + c.toString());
	}

	public void deleteNote(Contact c) {
		Log.i(TAG, "delete:" + c.toString());
	}

}
