package com.swe.prototype.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.calendar.CalendarEntry;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.Note;

import android.content.Context;
import android.util.Log;




public class SaveEntries {

	public static void saveNotes(List<Note> note, String platform) {
		try
        {
           ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/" + "SyncronatorNotes_" + platform + ".not"))); //Select where you wish to save the file...
           oos.writeObject(note); // write the class as an 'object'
           oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
           oos.close();// close the stream
        }
        catch(Exception ex)
        {
           Log.v("Serialization Save Error : ",ex.getMessage());
           ex.printStackTrace();
        }
	}

	public static void saveContacts(List<Contact> contacts, String platform) {
		try
        {
           ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/" + "SyncronatorContacts_" + platform + ".not"))); //Select where you wish to save the file...
           oos.writeObject(contacts); // write the class as an 'object'
           oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
           oos.close();// close the stream
        }
        catch(Exception ex)
        {
           Log.v("Serialization Save Error : ",ex.getMessage());
           ex.printStackTrace();
        }
	}

	public static void saveCalendarentrys(List<CalendarEntry> calendar,
			String platform) {
		try
        {
           ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/" + "SyncronatorCalendar_" + platform + ".not"))); //Select where you wish to save the file...
           oos.writeObject(calendar); // write the class as an 'object'
           oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
           oos.close();// close the stream
        }
        catch(Exception ex)
        {
           Log.v("Serialization Save Error : ",ex.getMessage());
           ex.printStackTrace();
        }
	}

	public static ArrayList<Note> loadNotes(String platform) throws Exception {
		try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/sdcard/" + "SyncronatorNotes_" + platform + ".not"));
            ArrayList<Note> o =(ArrayList<Note>) ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
        Log.v("Serialization Read Error : ",ex.getMessage());
            ex.printStackTrace();
        }
		Exception e = new Exception();
		throw e;
	}

	public static ArrayList<Contact> loadContacts(String platform) throws Exception {
		try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/sdcard/" + "SyncronatorContacts_" + platform + ".not"));
            ArrayList<Contact> o =(ArrayList<Contact>) ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
        Log.v("Serialization Read Error : ",ex.getMessage());
            ex.printStackTrace();
        }
		Exception e = new Exception();
		throw e;
	}

	public static ArrayList<com.swe.prototype.models.CalendarEntry> loadCalendarentrys(String platform) throws Exception {
		try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/sdcard/" + "SyncronatorCalendar_" + platform + ".not"));
            ArrayList<com.swe.prototype.models.CalendarEntry> o =(ArrayList<com.swe.prototype.models.CalendarEntry>) ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
        Log.v("Serialization Read Error : ",ex.getMessage());
            ex.printStackTrace();
        }
		Exception e = new Exception();
		throw e;
	}

}