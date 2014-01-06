package com.swe.prototype.net.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swe.prototype.activities.CreateContactActivity;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import com.swe.prototype.models.server.EncryptedData;
import com.swe.prototype.models.server.ServerContact;
import com.swe.prototype.models.server.ServerNote;

public class ServerAccount extends AccountBase {
	private final static String TAG = "ServerAccount";

	protected String server_url = null;
	protected Security sec = null;
	protected Gson gson = null;

	public ServerAccount(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
		this.server_url = Settings.getServer();
		this.sec = new Security(this.password);
		this.gson = new Gson();
	}

	@Override
	public void synchronize() {
		syncronizeByType("contacts");
		syncronizeByType("calendar");
		syncronizeByType("notes");
		// setLastSynchronisationTimestamp
	}

	/**
	 * @param data_type
	 *            -> possible values: calendar, contacts, notes
	 */
	private void syncronizeByType(String data_type) {
		new SyncDataTask(data_type).execute();
	}

	private boolean entryExists(Uri contentUri, String where, String[] args) {
		final ContentResolver resolver = this.context.getContentResolver();
		final String[] projection = { ServerDataTable.COLUMN_ID,
				ServerDataTable.COLUMN_ID_DATA, ServerDataTable.COLUMN_DATA,
				ServerDataTable.COLUMN_TAG };
		Cursor cursor = resolver.query(contentUri, projection, where, args,
				null);
		return cursor.moveToFirst();
	}

	private void synchronizeDatabase(String data_type,
			ArrayList<EncryptedData> data) {
		Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
				"server_data");

		// first try to find entry and update it, or save if new
		ContentValues values = new ContentValues();
		String where;
		String[] args;
		for (EncryptedData encryptedData : data) {
			where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
					+ ServerDataTable.COLUMN_TAG + " = ?";
			args = new String[] { encryptedData.getId() + "", data_type };
			values.put("data_id", encryptedData.getId());
			values.put("data", encryptedData.getData());
			values.put("tag", data_type);
			// check if entry exists
			if (entryExists(contentUri, where, args)) {
				// update entry
				this.context.getContentResolver().update(contentUri, values,
						where, args);
			} else {
				// create new entry
				this.context.getContentResolver().insert(contentUri, values);
			}
		}

	}

	public int getLastSynchronisationTimestamp() {
		return 0;// (int) (System.currentTimeMillis() / 1000L);
	}

	public void setLastSynchronisationTimestamp() {

	}

	/**
	 * get data from useraccount on the server to the app. Task returns a list
	 * of entries of specified Type given in param[2] the entries are still
	 * encrypted and json encoded seperatly and have to be processed further.
	 */
	private class SyncDataTask extends AsyncDataTask<ArrayList<EncryptedData>> {
		private String data_type = null;

		public SyncDataTask(String data_type) {
			this.data_type = data_type;
		}

		protected ArrayList<EncryptedData> doInBackground(String... params) {
			String response = null;
			int timestamp = getLastSynchronisationTimestamp();
			Type listType = new TypeToken<ArrayList<EncryptedData>>() {
			}.getType();

			try {
				response = sync(server_url, username, password, this.data_type,
						timestamp);
				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) gson
						.fromJson(response, listType);
				Log.i(TAG, list.size() + " datasets for sync.");
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(ArrayList<EncryptedData> list) {
			super.onPostExecute(list);
			if (list == null)
				return;
			synchronizeDatabase(this.data_type, list);
		}
	}
	
	public ArrayList<Note> getNotes() {
		ArrayList<Note> notes = new ArrayList<Note>();
		Cursor cursor = getData("notes");
		if (cursor.moveToFirst()) {
			do {
				notes.add(new EncryptedData(cursor.getInt(0), cursor
						.getString(2)).toNote(this.password,
						cursor.getInt(1), this));
			} while (cursor.moveToNext());
		}
		Log.i(TAG, "i return " + notes.size() + " notes!!");
		return notes;
	}

	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = getData("contacts");
		if (cursor.moveToFirst()) {
			do {
				contacts.add(new EncryptedData(cursor.getInt(0), cursor
						.getString(2)).toContact(this.password,
						cursor.getInt(1), this));
			} while (cursor.moveToNext());
		}
		Log.i(TAG, "i return " + contacts.size() + " contacts!!");
		return contacts;
	}
	
	/**
	 * read data from database, decrypt it and return a list of contact objects.
	 * 
	 * @return
	 */
	public Cursor getData(String tag) {
		final ContentResolver resolver = this.context.getContentResolver();
		final Uri dataUri = Uri.withAppendedPath(
				SQLiteDataProvider.CONTENT_URI,
				ServerDataTable.TABLE_SERVERDATA);
		final String[] projection = { ServerDataTable.COLUMN_ID,
				ServerDataTable.COLUMN_ID_DATA, ServerDataTable.COLUMN_DATA,
				ServerDataTable.COLUMN_TAG };
		Cursor cursor = resolver.query(dataUri, projection, "tag = ?",
				new String[] { tag }, null);
		return cursor;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Server (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {
		Log.i(TAG, "ich erstelle den contact:" + lastname);

		// send data to server
		ServerContact contact = new ServerContact(this, -1, lastname,
				firstname, phonenumber, email);
		new AddDataTask() {

		}.execute("contact", contact.toJson());
		Log.i(TAG, "erstellter kontakt: " + contact.toJson());
	}

	@Override
	public void createNote(String title, String text) {
		Log.i(TAG, "ich erstelle den note:" + title);

		// send data to server
		ServerNote note = new ServerNote(this, -1, title, text);
		/*new AddDataTask() {

		}.execute("note", note.toJson());*/
		Log.i(TAG, "erstellte note: " + note.toJson());
	}

	/**
	 * Task for User authentification
	 */
	public class AuthenticateUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return authenticate(server_url, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Task for user registration
	 */
	public class RegisterUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return register(server_url, username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * add data to the useraccount on the server. Task returns true if a new
	 * entry was created on the server.
	 */
	public class AddDataTask extends AsyncDataTask<Boolean> {
		/**
		 * @params[2] -> possible values: calendar, contact, note
		 */
		protected Boolean doInBackground(String... params) {
			String type = params[0];
			String data = sec.encrypt(params[1]);

			try {
				return add(server_url, username, password, type, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * get data from useraccount on the server to the app. Task returns a list
	 * of entries of specified Type given in param[2] the entries are still
	 * encrypted and json encoded seperatly and have to be processed further.
	 */
	public class GetDataTask extends AsyncDataTask<ArrayList<EncryptedData>> {
		/**
		 * @params[2] -> possible values: calendar, contacts, notes
		 */
		protected ArrayList<EncryptedData> doInBackground(String... params) {
			String type = params[0];

			String response = null;
			try {
				response = get(server_url, username, password, type);
				Type listType = new TypeToken<ArrayList<EncryptedData>>() {
				}.getType();
				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) gson
						.fromJson(response, listType);
				String data = null;
				for (int i = 0; i < list.size(); i++) {
					data = list.get(i).data.replace("\n", "").replace("\r", "");
					list.get(i).data = sec.decrypt(data);
					Log.i(TAG, list.get(i).id + " = " + list.get(i).data);
				}
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public BaseAdapter getContactAdapter(Context context, int layout_id) {
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(context,
				layout_id);
		adapter.addAll(getContacts());
		return adapter;
	}

	@Override
	public BaseAdapter getNotesAdapter(Context context, int layout_id) {
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(context, layout_id);
		adapter.addAll(getNotes());
		return adapter;
	}

	@Override
	public BaseAdapter getCalendarAdapter(Context context, int layout_id) {
		return null;
	}

	@Override
	public void editContact(Context context, Contact c) {
		Log.i(TAG, "edit:" + this.toString());
		Intent in = new Intent(context, CreateContactActivity.class);
		in.putExtra("edit_mode", true);
		in.putExtra("account_id", c.getAccount().getAccountId());
		context.startActivity(in);
	}

	@Override
	public void editNote(Context context, Note n) {

	}

	@Override
	public void editCalendarEntry(Context context, CalendarEntry ce) {

	}

	@Override
	public void deleteContact(Contact c) {
		ServerContact contact = (ServerContact) c;
		Log.i(TAG, "ich lösche den contact mit server_id:" + contact.getId());

		/*
		 * new AddDataTask() {
		 * 
		 * }.execute("contact", contact.toJson()); Log.i(TAG,
		 * "erstellter kontakt: " + contact.toJson());
		 */
	}

	@Override
	public void deleteNote(Note n) {

	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {

	}

	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {
	}
}
