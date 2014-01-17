package com.swe.prototype.models;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

public abstract class AccountBase extends Thread {
	private final static String TAG = "AccountBase";

	protected int refresh_time_sec = 6 * 60 * 60; // default 6 hours
	protected int account_id = 0;
	protected String username = null;
	protected String password = null;
	protected Context context = null;

	public AccountBase(Context context, int account_id, int refresh_time_sec,
			String username, String password) {
		this.setRefreshTime(refresh_time_sec);
		this.account_id = account_id;
		this.username = username;
		this.password = password;
		this.context = context;
	}

	public void run() {
		synchronizeAll();
	}

	public int getRefreshTime() {
		return refresh_time_sec;
	}

	public void setRefreshTime(int refresh_time_sec) {
		this.refresh_time_sec = refresh_time_sec;
	}

	public int getAccountId() {
		return this.account_id;
	}

	public void synchronizeAll() {
		synchronizeContacts();
		synchronizeNotes();
		synchronizeCalendarEntries();
	}

	/*
	 * Synchronisiert alle kontate vom account. D.h die daten holen und
	 * abspeichern.
	 */
	public abstract void synchronizeContacts();

	/*
	 * Synchronisiert alle Notizen vom account. D.h die daten holen und
	 * abspeichern.
	 */
	public abstract void synchronizeNotes();

	/*
	 * Synchronisiert alle Kalendereinträge vom account. D.h die daten holen und
	 * abspeichern.
	 */
	public abstract void synchronizeCalendarEntries();

	/**
	 * Lokale Kontakte auslesen und zurückgeben
	 * 
	 * @param context
	 * @param layout_id
	 * @return
	 */
	public abstract BaseAdapter getContactAdapter(Context context, int layout_id);

	/**
	 * Lokale Notizen auslesen und zurückgeben
	 * 
	 * @param context
	 * @param layout_id
	 * @return
	 */
	public abstract BaseAdapter getNotesAdapter(Context context, int layout_id);

	/**
	 * Lokale Kalendereinträge auslesen und zurückgeben
	 * @param context
	 * @param layout_id
	 * @return
	 */
	public abstract BaseAdapter getCalendarAdapter(Context context,
			int layout_id);

	/**
	 * Neuen Kontakt anlegen. Speichern und zum account schicken
	 * 
	 * @param lastname
	 * @param firstname
	 * @param phonenumber
	 * @param email
	 */
	public abstract void createContact(String lastname, String firstname,
			String phonenumber, String email);

	/**
	 * Neue Notiz anlegen. Speichern und zum account schicken
	 * 
	 * @param title
	 * @param text
	 */
	public abstract void createNote(String title, String text);

	/**
	 * Neue Notiz anlegen. Speichern und zum account schicken
	 * 
	 * @param startDate
	 *            Sting: Format: dd/mm/yyyy
	 * @param endDate
	 *            Sting: Format: dd/mm/yyyy
	 * @param startTime
	 *            String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param endTime
	 *            String hh:mm AM or hh:mm PM | hh:00 ->11 mm:00->59
	 * @param description
	 *            Sting from EditText
	 * @param repeat
	 *            : int | 0 == no_repeat; 1 == every_day; 2 == every_month; 3 ==
	 *            every_year
	 */
	public abstract void createCalendarEntry(String startDate, String endDate,
			String startTime, String endTime, String description, int repeat);

	/**
	 * Einen vorhandenen Kontakt editieren Lokal editieren und zum account
	 * schicken.
	 * 
	 * @param c
	 *            Zu editierender Kontakt ( um z.b die id auszulesen (muss zum
	 *            jeweiligen AccountContact gecastet werden))
	 * @param lastname
	 *            neuer name
	 * @param firstname
	 *            neuer vorname
	 * @param phonenumber
	 *            neue nummer
	 * @param email
	 *            neue email
	 */
	public abstract void editContact(Contact c, String lastname,
			String firstname, String phonenumber, String email);

	/**
	 * Eine vorhandene Notiz editieren Lokal editieren und zum account schicken.
	 * 
	 * @param n
	 *            Zu editierende Notiz ( um z.b die id auszulesen (muss zum
	 *            jeweiligen AccountContact gecastet werden))
	 * @param title
	 *            neuer titel
	 * @param text
	 *            neuer text
	 */
	public abstract void editNote(Note n, String title, String text);

	/**
	 * 
	 * @param ce
	 *            Zu editierender Kalenereintrag ( um z.b die id auszulesen
	 *            (muss zum jeweiligen AccountContact gecastet werden))
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @param description
	 * @param repeat
	 */
	public abstract void editCalendarEntry(CalendarEntry ce, String startDate,
			String endDate, String startTime, String endTime,
			String description, int repeat);

	/**
	 * Lösche den Kontakt lokal und vom Account. Am besten erst auf dem account
	 * löschen und wenn erfolgreich erst lokal.
	 * 
	 * @param c
	 */
	public abstract void deleteContact(Contact c);

	/**
	 * Lösche den Notiz lokal und vom Account. Am besten erst auf dem account
	 * löschen und wenn erfolgreich erst lokal.
	 * 
	 * @param n
	 */
	public abstract void deleteNote(Note n);

	/**
	 * Lösche den Kalendereintrag lokal und vom Account. Am besten erst auf dem
	 * account löschen und wenn erfolgreich erst lokal.
	 * 
	 * @param ce
	 */
	public abstract void deleteCalendarEntry(CalendarEntry ce);

	/*
	 * return: Der zurückgegebene String muss als erstes Zeichen 'S','G' oder
	 * 'E' havben!!! Je nach dem was das für ein Account ist.
	 */
	public abstract String toString();

	/**
	 * Überprüft ob die logindaten für den Account stimmen.
	 * 
	 * @return true wenn accountdaten valide
	 */
	public abstract boolean validateAccountData();
}
