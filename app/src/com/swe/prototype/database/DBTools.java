package com.swe.prototype.database;

import com.swe.prototype.database.tables.AccountTable;
import com.swe.prototype.database.tables.ExchangeCalendarTable;
import com.swe.prototype.database.tables.ExchangeContactTable;
import com.swe.prototype.database.tables.ExchangeNoteTable;
import com.swe.prototype.database.tables.GoogleContactTable;
import com.swe.prototype.database.tables.ServerDataTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {

 public static String SERVERDATA_TABLE = "server_data";
 public static int SERVERDATA_TABLE_ID = 1;

 public DBTools(Context context) {
  super(context, "synchronator.db", null, 1);
 }

 @Override
 // create all tables here
 public void onCreate(SQLiteDatabase database) {
  ServerDataTable.onCreate(database);
  AccountTable.onCreate(database);
  ExchangeContactTable.onCreate(database);
  ExchangeNoteTable.onCreate(database);
  ExchangeCalendarTable.onCreate(database);
  GoogleContactTable.onCreate(database);
 }

 @Override
 public void onUpgrade(SQLiteDatabase database, int oldv, int newv) {
  ServerDataTable.onUpgrade(database, oldv, newv);
  AccountTable.onUpgrade(database, oldv, newv);
  ExchangeContactTable.onUpgrade(database, oldv, newv);
  ExchangeNoteTable.onUpgrade(database, oldv, newv);
  ExchangeCalendarTable.onUpgrade(database, oldv, newv);
  GoogleContactTable.onUpgrade(database, oldv, newv);
 }

 /**
  * delete all data from the database
  */
 public void purgeDatabase() {
  SQLiteDatabase db = this.getWritableDatabase();
  onUpgrade(db, 1, 1);
 }
 
 /**
  * delete all contacts/notes/calendarentries
  */
 public void purgeData() {
  SQLiteDatabase db = this.getWritableDatabase();
  ServerDataTable.onUpgrade(db, 1, 1);
 }
 
 /**
  * delete all contacts from ExchangeContactTable
  */
 public void purgeContactTable() {
  SQLiteDatabase db = this.getWritableDatabase();
  GoogleContactTable.onUpgrade(db, 1, 1);
 }
 
 /**
  * delete all from ExchangeNoteTable
  */
 public void purgeNotesTable() {
  SQLiteDatabase db = this.getWritableDatabase();
  ExchangeNoteTable.onUpgrade(db, 1, 1);
 }
 
 /**
  * delete all from ExchangeCalendarTable
  */
 public void purgeCalendarTable() {
  SQLiteDatabase db = this.getWritableDatabase();
  ExchangeCalendarTable.onUpgrade(db, 1, 1);
 }
}