package com.swe.prototype.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;
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
	private static final Uri contentUri = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);
	ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account);

		Button save_button = (Button) findViewById(R.id.button_edit_save);
		save_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveAccount(v);
				finish();
			}
		});
	}

	protected void saveAccount(View v) {
		ContentValues values = new ContentValues();
		values.put(AccountTable.COLUMN_PROVIDER,
				((Spinner) findViewById(R.id.spinner_account)).getSelectedItem().toString());
		values.put(AccountTable.COLUMN_USERNAME,
				getEditText(R.id.edit_text_username));
		values.put(AccountTable.COLUMN_PASSWORD,
				getEditText(R.id.edit_text_password));
		getContentResolver().insert(contentUri, values);
	}

	private String getEditText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
