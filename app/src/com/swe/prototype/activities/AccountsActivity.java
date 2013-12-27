package com.swe.prototype.activities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
/*import android.support.v4.app.LoaderManager;
 import android.support.v4.app.LoaderManager.LoaderCallbacks;
 import android.support.v4.content.CursorLoader;
 import android.support.v4.content.Loader;
 import android.support.v4.widget.CursorAdapter;*/
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.swe.prototype.R;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.net.server.Server;
import com.swe.prototype.net.server.Server.AuthenticateUserTask;

public class AccountsActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private static final String TAG = "AccountsActivity";
	private final static int ADD_ACCOUNT_BUTTON = 0x122;
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);

	private ListView listView = null;
	SimpleCursorAdapter mAdapter;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		loadermanager = getLoaderManager();

		String[] uiBindFrom = { AccountTable.COLUMN_USERNAME,
				AccountTable.COLUMN_PROVIDER };
		int[] uiBindTo = { R.id.text_username, R.id.text_provider };

		/* Empty adapter that is used to display the loaded data */
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_account,
				null, uiBindFrom, uiBindTo);

		listView = (ListView) findViewById(R.id.list_view_accounts);
		registerForContextMenu(listView);
		listView.setAdapter(mAdapter);
		loadermanager.initLoader(1, null, (LoaderCallbacks<Cursor>) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ADD_ACCOUNT_BUTTON, 0, "Add Account");
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
		case ADD_ACCOUNT_BUTTON:
			show(CreateAccountActivity.class); 
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { AccountTable.COLUMN_ID,
				AccountTable.COLUMN_USERNAME, AccountTable.COLUMN_PROVIDER };
		CursorLoader loader = new CursorLoader(this, CONTENT_URI, projection,
				null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		((SimpleCursorAdapter) listView.getAdapter()).swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		((SimpleCursorAdapter) listView.getAdapter()).swapCursor(null);
	}
}
