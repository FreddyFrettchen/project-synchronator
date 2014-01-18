package com.swe.prototype.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.calendar.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;
import android.util.Log;




public class SaveEntries {

	public static void saveNotes(List<Note> note, String platform) {
		try {
			FileOutputStream fs = new FileOutputStream("SyncronatorNotes_" + platform + ".not");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(note);
			os.close();
			fs.close();
		} catch (Exception e) {

		}
	}

	public static void saveContacts(List<Contact> contacts, String platform) {
		try {
			FileOutputStream fs = new FileOutputStream("SyncronatorContacts_"
					+ platform + ".not");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(contacts);
			os.close();
			fs.close();
		} catch (Exception e) {
			Log.i("GoogleKalender", e.getMessage());
		}
	}

	public static void saveCalendarentrys(List<CalendarEntry> calendar,
			String platform) {
		try {
			FileOutputStream fs = new FileOutputStream("SyncronatorCalendar_"
					+ platform + ".not");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(calendar);
			os.close();
			fs.close();
		} catch (Exception e) {

		}
	}

	public static ArrayList<Note> loadNotes(String platform) throws Exception {
		try {
			FileInputStream fs = new FileInputStream("SyncronatorNotes_" + platform + ".not");
			ObjectInputStream os = new ObjectInputStream(fs);
			ArrayList<Note> liste = (ArrayList<Note>) os.readObject();
			os.close();
			fs.close();
			return liste;
		} catch (Exception e) {

		}
		Exception e = new Exception();
		throw e;
	}

	public static List<Contact> loadContacts(String platform) throws Exception {
		try {
			FileInputStream fs = new FileInputStream("SyncronatorContacts_"
					+ platform + ".not");
			ObjectInputStream os = new ObjectInputStream(fs);
			List<Contact> liste = (List<Contact>) os.readObject();
			os.close();
			fs.close();
			return liste;
		} catch (Exception e) {

		}
		Exception e = new Exception();
		throw e;
	}

	public static List<CalendarEntry> loadCalendarentrys(String platform)
			throws Exception {
		try {
			FileInputStream fs = new FileInputStream("SyncronatorCalendar_"
					+ platform + ".not");
			ObjectInputStream os = new ObjectInputStream(fs);
			List<CalendarEntry> liste = (List<CalendarEntry>) os.readObject();
			os.close();
			fs.close();
			return liste;
		} catch (Exception e) {

		}
		Exception e = new Exception();
		throw e;
	}

}