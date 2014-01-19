package com.swe.prototype.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.swe.prototype.R;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.net.server.ServerAccount;

public class AccountsActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private static final String TAG = "AccountsActivity";
	// private final static int ADD_ACCOUNT_BUTTON = 0x122;
	private final static Uri CONTENT_URI = Uri.withAppendedPath(
			SQLiteDataProvider.CONTENT_URI, AccountTable.TABLE_ACCOUNT);

	private ListView listView = null;
	private SimpleCursorAdapter mAdapter = null;
	private LoaderManager loadermanager = null;
	private Context context = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		this.context = this;
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
		
		final Button delete_synchronator = (Button) findViewById(R.id.button_delete_synchronator);
		delete_synchronator.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				 
				// set title
				alertDialogBuilder.setTitle("Do you want to delete your Synchronator account?");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Warning! You will lose all your Data!")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							deleteSynchronator();
						}
					})
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
				
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
	 
				// show it
				alertDialog.show();
			}
		});
	}
	

	private void deleteSynchronator() {
		((ServerAccount)accounts.getServerAccount()).deleteAccount();
		SharedPreferences pref = getPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("email", null);
		editor.putString("password", null);
		editor.commit();
		showAndFinish(MainActivity.class);
	}

	public void deleteAccount(int id_account) {
		Log.i(TAG,"Deleting account " + id_account);
		getContentResolver().delete(CONTENT_URI, "_id = ?",
				new String[] { id_account + "" });
		showToast("Account deleted");
		showAndFinish(AccountsActivity.class);
	}

	public void editAccount(int id_account) {
		Intent in = new Intent(this, CreateAccountActivity.class);
		in.putExtra("id_account", id_account);
		in.putExtra("edit_mode", true);
		startActivity(in);
		// loadermanager.restartLoader(1, null, (LoaderCallbacks<Cursor>) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0, ADD_ACCOUNT_BUTTON, 0, "Add Account");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			show(CreateAccountActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_accounts, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		// Object a = listView.getAdapter().getItem(item.getItemId());
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.edit:
			editAccount((int) info.id);
			break;
		case R.id.delete:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			 
			// set title
			alertDialogBuilder.setTitle("Do you want to delete the account?");
 
			// set dialog message
			alertDialogBuilder
				//.setMessage("Do you want to delete the account?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						deleteAccount((int) info.id);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
 
			// show it
			alertDialog.show();
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
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
