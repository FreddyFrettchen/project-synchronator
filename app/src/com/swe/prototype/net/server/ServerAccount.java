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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swe.prototype.activities.CalendarAddEventActivity;
import com.swe.prototype.activities.ChangeNoteActivity;
import com.swe.prototype.activities.CreateContactActivity;
import com.swe.prototype.activities.ListContactsActivity;
import com.swe.prototype.database.DBTools;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import com.swe.prototype.models.server.EncryptedData;
import com.swe.prototype.models.server.ServerCalendarEntry;
import com.swe.prototype.models.server.ServerNote;
import com.swe.prototype.models.server.ServerContact;

public class ServerAccount extends AccountBase {
	private final static String TAG = "ServerAccount";

	protected String server_url = null;
	protected Security sec = null;
	protected Gson gson = null;

	Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
			"server_data");

	public ServerAccount(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		super(context, account_id, refresh_time_sec, username, password);
		this.server_url = Settings.getServer();
		this.sec = new Security(this.password);
		this.gson = new Gson();
	}

	/**
	 * @param data_type
	 *            -> possible values: calendar, contacts, notes
	 */
	private void synchronizeByType(final String data_type) {
		new SyncDataTask(data_type) {
			protected void onPostExecute(
					java.util.ArrayList<EncryptedData> result) {
				synchronizeDatabase(data_type, result);
			};
		}.execute();
	}

	/**
	 * checks if an entry for this data is already in the database.
	 * 
	 * @param contentUri
	 * @param where
	 * @param args
	 * @return
	 */
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
		checkForNewlyCreated(data_type, data);
		checkForDeletions(data_type, data);
		checkForUpdates(data_type, data);
	}

	/*
	 * make links for all new data that is already locally stored because it was
	 * created on the device.
	 */
	private void checkForNewlyCreated(String data_type,
			ArrayList<EncryptedData> data) {
		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ? AND "
				+ ServerDataTable.COLUMN_ID + " = ?";
		String[] args;
		ContentValues values = new ContentValues();

		for (EncryptedData encryptedData : data) {
			args = new String[] { "-1", data_type, "CREATE",
					encryptedData.getIdData() + "" };
			values.put("data_id", encryptedData.getId());
			values.put("status", "INSYNC");
			values.put("resend", "false");
			// check if entry exists and update the data_id of server and set
			// status to INSYC
			if (entryExists(contentUri, where, args)) {
				// update entry
				this.context.getContentResolver().update(contentUri, values,
						where, args);
			}
		}
	}

	/**
	 * delete all entries that are marked as deleted
	 */
	private void checkForDeletions(String data_type,
			ArrayList<EncryptedData> data) {
		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args;
		ContentValues values = new ContentValues();

		for (EncryptedData encryptedData : data) {
			args = new String[] { encryptedData.getId() + "", data_type,
					"DELETE" };
			// check if entry exists and update the data_id of server and set
			// status to INSYC
			if (entryExists(contentUri, where, args)) {
				// delete entry
				this.context.getContentResolver().delete(contentUri, where,
						args);
			}
		}
	}

	/**
	 * checks for data that has changed and replaces it
	 */
	private void checkForUpdates(String data_type, ArrayList<EncryptedData> data) {

		String where;
		String[] args;
		ContentValues values = new ContentValues();
		Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
				"server_data");

		// first try to find entry and update it, or save if new
		for (EncryptedData encryptedData : data) {
			if(encryptedData.deleted) continue;
			where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
					+ ServerDataTable.COLUMN_TAG + " = ?";
			args = new String[] { encryptedData.getId() + "", data_type };
			values.put("data", encryptedData.getData());
			values.put("data_id", encryptedData.getIdData());
			values.put("tag", data_type);
			values.put("STATUS", "INSYNC");
			values.put("resend", "false");
			
			if (entryExists(contentUri, where, args)) { // update entry
				this.context.getContentResolver().update(contentUri, values,
						where, args);
			} else { // create new entry
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
	 * encrypted and json encoded separately and have to be processed further.
	 */
	private class SyncDataTask extends AsyncDataTask<ArrayList<EncryptedData>> {
		private String data_type = null;

		public SyncDataTask(String data_type) {
			this.data_type = data_type;
		}

		protected ArrayList<EncryptedData> doInBackground(String... params) {
			String response_sync = null;
			int timestamp = getLastSynchronisationTimestamp();
			Type listType = new TypeToken<ArrayList<EncryptedData>>() {
			}.getType();

			try {
				response_sync = sync(server_url, username, password,
						this.data_type, timestamp);

				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) gson
						.fromJson(response_sync, listType);
				Log.i(TAG, list.size() + " datasets for sync.");
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i(TAG, "Error occured while syncing. Returning empty resultset");
			return new ArrayList<EncryptedData>();
		}
	}

	/**
	 * deletes user from the server. cant be undone when called!!!
	 */
	private class DeleteDataTask extends
			AsyncDataTask<ArrayList<EncryptedData>> {
		private String data_type = null;
		private int data_id = 0;

		public DeleteDataTask(String data_type, int data_id) {
			this.data_type = data_type;
			this.data_id = data_id;
		}

		// TODO wrong type
		protected ArrayList<EncryptedData> doInBackground(String... params) {
			Boolean response = null;
			int timestamp = getLastSynchronisationTimestamp();
			Type listType = new TypeToken<ArrayList<EncryptedData>>() {
			}.getType();

			try {
				response = delete(server_url, username, password,
						this.data_type, this.data_id);
				Log.i(TAG, "response of deleting " + this.data_type + ": "
						+ (response ? "True" : "False"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.i(TAG, "Error occured while syncing. Returning empty resultset");
			return new ArrayList<EncryptedData>();
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
				Log.i(TAG, "Decrypting Notes");
				notes.add(new EncryptedData(cursor.getInt(0), cursor
						.getString(2)).toNote(this.password, cursor.getInt(1),
						this));
			} while (cursor.moveToNext());
		}
		return notes;
	}

	public ArrayList<CalendarEntry> getCalendarEntries() {
		ArrayList<CalendarEntry> entries = new ArrayList<CalendarEntry>();
		Cursor cursor = getData("calendar");
		if (cursor.moveToFirst()) {
			do {
				Log.i(TAG, "Decrypting CalendarEntries");
				entries.add(new EncryptedData(cursor.getInt(0), cursor
						.getString(2)).toCalendarEntry(this.password,
						cursor.getInt(1), this));
			} while (cursor.moveToNext());
		}
		return entries;
	}

	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = getData("contacts");
		if (cursor.moveToFirst()) {
			Log.i(TAG, "Decrypting Contacts");
			do {
				contacts.add(new EncryptedData(cursor.getInt(0), cursor
						.getString(2)).toContact(this.password,
						cursor.getInt(1), this));
			} while (cursor.moveToNext());
		}
		return contacts;
	}

	/**
	 * read data from database, decrypt it and return a list of objects that can
	 * be castet to contact/calendarEntry/note
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

	
	/* 
	 * return: Der zur�ckgegebene String muss als erstes Zeichen 'S',havben!!!
	 *  
	 */
	@Override
	public String toString() {
		return "Server (" + this.username + ")";
	}

	@Override
	public void createContact(String lastname, String firstname,
			String phonenumber, String email) {

		ServerContact contact = new ServerContact(this, -1, lastname,
				firstname, phonenumber, email);

		// create pending add in database
		ContentValues values = new ContentValues();
		Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
				"server_data");

		values.put("data_id", -1);
		values.put("data", (new EncryptedData(-1, contact.toJson()))
				.encryptData(this.password));
		values.put("tag", "contacts");
		values.put("status", "CREATE");
		values.put("resend", "false");
		Uri result = this.context.getContentResolver().insert(contentUri,
				values);

		// send data to server
		new AddDataTask() {
		}.execute("contact", contact.toJson(), result.getLastPathSegment());
	}

	@Override
	public void createNote(String title, String text) {
		// create pending add in database
		ContentValues values = new ContentValues();
		Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
				"server_data");

		ServerNote note = new ServerNote(this, -1, title, text);

		values.put("data_id", -1);
		values.put("data", (new EncryptedData(-1, note.toJson()))
				.encryptData(this.password));
		values.put("tag", "notes");
		values.put("status", "CREATE");
		values.put("resend", "false");
		Uri result = this.context.getContentResolver().insert(contentUri,
				values);

		// send data to server
		new AddDataTask() {
		}.execute("note", note.toJson(), result.getLastPathSegment());

		Log.i(TAG, "erstellte note: " + note.toJson());
	}

	@Override
	public void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat) {

		// send data to server
		ServerCalendarEntry centry = new ServerCalendarEntry(this, -1,
				startDate, endDate, startTime, endTime, description, repeat);
		ContentValues values = new ContentValues();
		Uri contentUri = Uri.withAppendedPath(SQLiteDataProvider.CONTENT_URI,
				"server_data");

		values.put("data_id", -1);
		Log.i(TAG,centry.toJson());
		values.put("data", (new EncryptedData(-1, centry.toJson()))
				.encryptData(this.password));
		values.put("tag", "calendar");
		values.put("status", "CREATE");
		values.put("resend", "false");
		Uri result = this.context.getContentResolver().insert(contentUri,
				values);
		new AddDataTask() {
		}.execute("calendar", centry.toJson(), result.getLastPathSegment());
	}

	public void deleteAccount() {
		Log.i(TAG, "Deleting user account.");
		new DeleteUserTask() {
			protected void onPostExecute(Boolean result) {
				// if deletion successful, clean database
				new DBTools(context).purgeDatabase();
			};
		}.execute();
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
	 * Task for User account deletion
	 */
	public class DeleteUserTask extends AsyncUserTask {
		protected Boolean doInBackground(String... params) {
			try {
				return delete(server_url, username, password);
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
		 * @params[1] -> possible values: calendar, contact, note
		 */
		protected Boolean doInBackground(String... params) {
			String type = params[0];
			String data = sec.encrypt(params[1]);
			String id_data = params[2];

			try {
				return add(server_url, username, password, type, data, id_data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * update data from the useraccount on the server. Task returns true if
	 * entry was updated on the server.
	 */
	public class UpdateDataTask extends AsyncDataTask<Boolean> {
		/**
		 * @params[1] -> possible values: calendar, contact, note
		 */
		protected Boolean doInBackground(String... params) {
			String type = params[0];
			String data = sec.encrypt(params[1]);
			String id_data = params[2];

			try {
				return update(server_url, username, password, type, id_data,
						data);
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
		ArrayAdapter<CalendarEntry> adapter = new ArrayAdapter<CalendarEntry>(
				context, layout_id);
		adapter.addAll(getCalendarEntries());
		return adapter;
	}

	@Override
	public void editContact(Contact c, String lastname, String firstname,
			String phonenumber, String email) {
		ServerContact contact = (ServerContact) c;

		Log.i(TAG, "Edit contact id:" + contact.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { contact.getId() + "", "contacts", "INSYNC" };
		ContentValues values = new ContentValues();

		contact.setLastName(lastname);
		contact.setFirstName(firstname);
		contact.setPhoneumber(phonenumber);
		contact.setEmail(email);

		values.put("data", sec.encrypt(contact.toJson()));
		values.put("status", "UPDATE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The Contact with id " + contact.getId()
					+ " was not found in the db and cant be edited.");
		}

		// send update to server
		new UpdateDataTask() {
		}.execute("contacts", contact.toJson(), contact.getId() + "");
	}

	@Override
	public void editNote(Note n, String title, String text) {
		ServerNote sn = (ServerNote) n;

		Log.i(TAG, "Edit servernote id:" + sn.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { sn.getId() + "", "notes", "INSYNC" };
		ContentValues values = new ContentValues();

		sn.setTitle(title);
		sn.setText(text);

		values.put("data", sec.encrypt(sn.toJson()));
		values.put("status", "UPDATE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The NBote with id " + sn.getId()
					+ " was not found in the db and cant be edited.");
		}

		// send update to server
		new UpdateDataTask() {
		}.execute("notes", sn.toJson(), sn.getId() + "");
	}

	@Override
	public void editCalendarEntry(CalendarEntry ce, String startDate,
			String endDate, String startTime, String endTime,
			String description, int repeat) {
		ServerCalendarEntry entry = (ServerCalendarEntry) ce;
		Log.i(TAG, "Edit servercalendar id:" + entry.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { entry.getId() + "", "calendar", "INSYNC" };
		ContentValues values = new ContentValues();

		entry.setStartDate(startDate);
		entry.setEndDate(endDate);
		entry.setStartTime(startTime);
		entry.setEndTime(endTime);
		entry.setDescription(description);
		entry.setRepeat(repeat);

		values.put("data", sec.encrypt(entry.toJson()));
		values.put("status", "UPDATE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The CalendarEntry with id " + entry.getId()
					+ " was not found in the db and cant be edited.");
		}

		// send update to server
		new UpdateDataTask() {
		}.execute("calendar", entry.toJson(), entry.getId() + "");
	}

	@Override
	public void deleteContact(Contact c) {
		ServerContact contact = (ServerContact) c;
		Log.i(TAG, "ich lösche den contact mit server_id:" + contact.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { contact.getId() + "", "contacts",
				"INSYNC" };
		ContentValues values = new ContentValues();

		values.put("status", "DELETE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The Contact with id " + contact.getId()
					+ " was not found in the db and cant be deleted.");
		}

		// send delete to server
		new DeleteDataTask("contacts", contact.getId()) {
		}.execute();
	}

	@Override
	public void deleteNote(Note n) {
		ServerNote note = (ServerNote) n;
		Log.i(TAG, "ich lösche den note mit server_id:" + note.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { note.getId() + "", "notes", "INSYNC" };
		ContentValues values = new ContentValues();

		values.put("status", "DELETE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The Note with id " + note.getId()
					+ " was not found in the db and cant be deleted.");
		}

		// send delete to server
		new DeleteDataTask("notes", note.getId()) {
		}.execute();
	}

	@Override
	public void deleteCalendarEntry(CalendarEntry ce) {
		ServerCalendarEntry centry = (ServerCalendarEntry) ce;
		Log.i(TAG, "Deleting Calendarentry with server_id:" + centry.getId());

		String where = ServerDataTable.COLUMN_ID_DATA + " = ? AND "
				+ ServerDataTable.COLUMN_TAG + " = ? AND "
				+ ServerDataTable.COLUMN_STATUS + " = ?";
		String[] args = new String[] { centry.getId() + "", "calendar",
				"INSYNC" };
		ContentValues values = new ContentValues();

		values.put("status", "DELETE");
		if (entryExists(contentUri, where, args)) {
			// update entry
			this.context.getContentResolver().update(contentUri, values, where,
					args);
		} else {
			Log.i(TAG, "The Calendarentry with id " + centry.getId()
					+ " was not found in the db and cant be deleted.");
		}

		// send delete to server
		new DeleteDataTask("calendar", centry.getId()) {
		}.execute();
	}

	@Override
	public void synchronizeContacts() {
		synchronizeByType("contacts");
	}

	@Override
	public void synchronizeNotes() {
		synchronizeByType("notes");
	}

	@Override
	public void synchronizeCalendarEntries() {
		synchronizeByType("calendar");
	}
}
