package com.swe.prototype.activities;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.swe.prototype.R;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;

public class CreateAccountActivity extends BaseActivity {

	private static final String TAG = "CreateAccountActivity";
	private static final Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);

	private int id_account = 0;
	private boolean edit_mode = false;
	ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account);

		if(getIntent().hasExtra("id_account"))
			id_account = getIntent().getExtras().getInt("id_account");
		
		if(getIntent().hasExtra("edit_mode"))
			edit_mode = getIntent().getExtras().getBoolean("edit_mode");

		if (edit_mode && id_account != 0)
			prefill_fields();

		Button save_button = (Button) findViewById(R.id.button_edit_save);
		save_button.setText(edit_mode ? "EditAccount" : "Save Account");
		save_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_mode) {
					updateAccount(v);
				} else {
					saveAccount(v);
				}
				finish();
			}
		});
	}

	// load account from db
	protected void prefill_fields() {
		String[] projection = { AccountTable.COLUMN_PROVIDER,
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
		}
	}

	protected int getAccountPos(String account_name) {
		String[] accs = getResources().getStringArray(R.array.accounts_array);
		int pos = 0;
		for (int i = 0; i < accs.length; i++) {
			if (accs[i].equals(account_name)) {
				pos = i;
			}
		}
		return pos;
	}

	protected void updateAccount(View v) {
		ContentValues values = new ContentValues();
		values.put(AccountTable.COLUMN_PROVIDER,
				((Spinner) findViewById(R.id.spinner_account))
						.getSelectedItem().toString());
		values.put(AccountTable.COLUMN_USERNAME,
				getEditText(R.id.edit_text_username));
		values.put(AccountTable.COLUMN_PASSWORD,
				getEditText(R.id.edit_text_password));
		getContentResolver().update(CONTENT_URI, values, "_id = ?",
				new String[] { id_account + "" });
	}

	protected void saveAccount(View v) {
		ContentValues values = new ContentValues();
		values.put(AccountTable.COLUMN_PROVIDER,
				((Spinner) findViewById(R.id.spinner_account))
						.getSelectedItem().toString());
		values.put(AccountTable.COLUMN_USERNAME,
				getEditText(R.id.edit_text_username));
		values.put(AccountTable.COLUMN_PASSWORD,
				getEditText(R.id.edit_text_password));
		values.put("last_sync", "0");
		getContentResolver().insert(CONTENT_URI, values);
	}

	private void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}

	private void setSpinner(int id, int pos) {
		((Spinner) findViewById(id)).setSelection(pos);
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
