package com.swe.prototype.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.swe.prototype.R;

public class AddCalendarEventActivity extends BaseActivity{
	private static final String TAG = "AddCalendarEventActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcalendarevent);
		
	}
	
	/*
	Die muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar ist.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	public void onClickSave(View v){
//		System.out.println("Save wurde gedrueckt!");
//		System.out.println("Text: "+noteText.getText());
//		
//		if(this.createNewNote){
//			// neue anlegen
//			
//		}
//		else{
//			// geänderte notiz speichern
//			
//		}
//		// hier muss dann der string aus dem edit text geholt werden und an changeNotePos in die datenbank geschrieben werden
//		if(synchronatorCheckbox.isChecked()){
//			System.out.println("Synchronator is checked!");
//		}
//		if(googleCheckbox.isChecked()){
//			System.out.println("google is checked!");
//		}
//		if(exchangeCheckbox.isChecked()){
//			System.out.println("Exchange is checked!");
//		}

		//this.onBackPressed(); und this.finish() funktioniert nicht um zurueck zur notizliste zu kommen
		Intent intent = new Intent(AddCalendarEventActivity.this,CalendarActivity.class);
		startActivity(intent);
		finish();
	}
	public void onClickCancel(View v){
		this.finish();
	}

}
