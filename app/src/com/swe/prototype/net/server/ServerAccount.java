package com.swe.prototype.net.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swe.prototype.database.SQLiteDataProvider;
import com.swe.prototype.database.tables.ServerDataTable;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.Security;
import com.swe.prototype.models.AccountBase;
import com.swe.prototype.models.server.EncryptedData;

public class ServerAccount extends AccountBase {
	private final static String TAG = "ServerAccount";

	protected String server_url = null;
	protected Security sec = null;
	protected Gson gson = null;

	public ServerAccount(Context context, int refresh_time_sec,
			String username, String password) {
		super(context, refresh_time_sec, username, password);
		this.server_url = Settings.getServer();
		this.sec = new Security(this.password);
		this.gson = new Gson();
	}

	@Override
	public void synchronize() {
		syncronizeByType("contacts");
		//syncronizeByType("calendar");
		//syncronizeByType("notes");
		//setLastSynchronisationTimestamp
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
	
	public int getLastSynchronisationTimestamp(){
		return (int)(System.currentTimeMillis()/1000L); 
	}
	
	public void setLastSynchronisationTimestamp(){
		
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
			try {
				response = sync(server_url, username, password, this.data_type, timestamp);
				Type listType = new TypeToken<ArrayList<EncryptedData>>() {
				}.getType();
				ArrayList<EncryptedData> list = (ArrayList<EncryptedData>) gson
						.fromJson(response, listType);

				/*
				 * String data = null; for (int i = 0; i < list.size(); i++) {
				 * data = list.get(i).data.replace("\n", "").replace("\r", "");
				 * list.get(i).data = sec.decrypt(data); Log.i(TAG,
				 * list.get(i).id + " = " + list.get(i).data); }
				 */
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
}
