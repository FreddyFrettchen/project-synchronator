package com.swe.prototype.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.prototype.R;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

public class ChangeNoteActivity extends BaseActivity {

	private static final String TAG = "NoteActivity";
	EditText title = null;
	EditText text = null;
	Button cancel = null;
	Button done = null;
	ListView list_accounts = null;
	ProgressDialog dialog = null;
	
	Note edit_note = null;
	Boolean edit_mode = null;

	private int id_account = -1;
	private boolean edit_mode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changenote);

		if (getIntent().hasExtra("edit_mode"))
			edit_mode = getIntent().getExtras().getBoolean("edit_mode");

		if (getIntent().hasExtra("id_account"))
			id_account = getIntent().getExtras().getInt("id_account");

		if (edit_mode && id_account != 0)
			prefill_fields();

		title = (EditText) findViewById(R.id.text_note_title);
		text = (EditText) findViewById(R.id.text_note_note);
		cancel = (Button) findViewById(R.id.button_note_cancel);
		done = (Button) findViewById(R.id.button_note_done);

		edit_note = getSynchronatorApplication().getCurrentNote();
		if (edit_note != null) {
			edit_mode = true;
			prefill_fields();
		}

		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		list_accounts = (ListView) findViewById(R.id.list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		done.setText("Save");
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (correctInputChoise()) {
					initializeDialog("Creating note...");
					saveNote(v);
				}
			}

			private boolean correctInputChoise() {
				// TODO Auto-generated method stub
				if (title.getText().toString().equals("")) {
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
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}

		});

		cancel.setText("Cancel");
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toNotes();
			}
		});
	}

	// load account from db
	protected void prefill_fields() {
		/*String[] projection = { AccountTable.COLUMN_PROVIDER,
				AccountTable.COLUMN_USERNAME, AccountTable.COLUMN_PASSWORD };
		Cursor data = getContentResolver().query(CONTENT_URI, projection,
				"_id = ?", new String[] { id_account + "" }, null);

		if (data.moveToFirst()) {
			setSpinner(R.id.spinner_account, getAccountPos(data.getString(0)));
			setEditText(R.id.edit_text_username, data.getString(1));
			setEditText(R.id.edit_text_password, data.getString(2));
		} else {
			Log.i(TAG, "no data to update on id: " + id_account);
			finish();
		}*/
	}

	private void toNotes() {
		startActivity(new Intent(this, ListNotesActivity.class));
		finish();
	}
	
	public void prefill_fields(){
		setEditText(R.id.text_note_title, edit_note.getTitle());
		setEditText(R.id.text_note_note, edit_note.getNote());
		((TextView)findViewById(R.id.text_save_to)).setVisibility(4);
		list_accounts.setVisibility(4);
	}

	private void initializeDialog(String message) {
		dialog = ProgressDialog
				.show(ChangeNoteActivity.this, "", message, true);
		dialog.show();
	}

	private void saveNote(View v) {
		String title = this.title.getText().toString();
		String text = this.text.getText().toString();

		int cntChoice = list_accounts.getCount();
		SparseBooleanArray selected_accounts = list_accounts
				.getCheckedItemPositions();

		for (int i = 0; i < cntChoice; i++) {
			if (selected_accounts.get(i) == true) {
				accounts.getAccounts().get(i).createNote(title, text);
			}
		}

		dialog.dismiss();
		finish();
	}
	
	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}
}
