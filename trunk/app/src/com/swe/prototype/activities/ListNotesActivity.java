package com.swe.prototype.activities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.swe.prototype.R;
import com.swe.prototype.adapter.NoteAdapter;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.models.server.ServerNote;

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
		listView.setOnItemClickListener(new ViewListItem());
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

		Note o = (Note) listView.getAdapter().getItem(info.position);
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

	public void editNote(Note c) {
		getSynchronatorApplication().setCurrentNote(c);
		Intent in = new Intent(this, ChangeNoteActivity.class);
		startActivity(in);
	}

	public void deleteNote(Note c) {
		c.delete();
		Toast.makeText(this, "Note marked for deletion", Toast.LENGTH_SHORT).show();
	}

	class ViewListItem implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent in = new Intent(parent.getContext(), NoteActivity.class);
			ServerNote sn = (ServerNote) listView.getItemAtPosition(position);
			in.putExtra("title", sn.getTitle());
			in.putExtra("text", sn.getNote());
			startActivityForResult(in, 0);
		}
	}
	
	public void moveNote(Note c) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_move);
		dialog.setTitle("Move Note to:");
		
		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		ListView list_accounts = (ListView) dialog.findViewById(R.id.dialog_list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		Button btn_move = (Button) dialog.findViewById(R.id.dialog_button_move);
		Button btn_cancel = (Button) dialog
				.findViewById(R.id.dialog_button_cancel);
		// if button is clicked, close the custom dialog
		btn_move.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "moving notes ...");
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

	/*protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		Log.i(TAG,"onActivityResult called");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	Log.i(TAG,"setting 5 second timer to update");
            	// refresh 5 secs from now
    			final ScheduledExecutorService worker = Executors
    					.newSingleThreadScheduledExecutor();
    			Runnable task = new Runnable() {
    				public void run() {
    					adapter.notifyDataSetChanged();
    				}
    			};
    			worker.schedule(task, 5, TimeUnit.SECONDS);
            }
        }
    }*/
}
