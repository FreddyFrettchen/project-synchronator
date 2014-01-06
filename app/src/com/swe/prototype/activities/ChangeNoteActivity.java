package com.swe.prototype.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.swe.prototype.models.AccountBase;

public class ChangeNoteActivity extends BaseActivity {

	private static final String TAG = "NoteActivity";
	EditText title = null;
	EditText text = null;
	Button cancel = null;
	Button done = null;
	ListView list_accounts = null;
	ProgressDialog dialog = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changenote);
		
        title = (EditText) findViewById(R.id.text_note_title);
		text = (EditText) findViewById(R.id.text_note_note);
		cancel = (Button) findViewById(R.id.button_note_cancel);
		done = (Button) findViewById(R.id.button_note_done);

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
				initializeDialog("Creating note...");
				saveNote(v);
			}
		});
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
}
