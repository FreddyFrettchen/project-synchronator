package com.swe.prototype.activities;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.swe.prototype.R;

public class AddCalendarEventActivity extends BaseActivity{
	private static final String TAG = "AddCalendarEventActivity";
	
	private int mYear;
	private int mMonth;
	private int mDay;

	private TextView mDateDisplay;
	private TextView mTimeDisplay;

	static final int DATE_DIALOG_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcalendarevent);
		mDateDisplay = (TextView) findViewById(R.id.textView_datepicker);
		mDateDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		mTimeDisplay = (TextView) findViewById(R.id.textView_timepicker);
		mTimeDisplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("timepicker clicked");
			}
		});
	    final Calendar c = Calendar.getInstance();
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH);
	    mDay = c.get(Calendar.DAY_OF_MONTH);
	    updateDisplay();
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
				@Override
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            updateDisplay();
		        }
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_ID:
	      return new DatePickerDialog(this,
	                mDateSetListener,
	                mYear, mMonth, mDay);
	   }
	   return null;
	}
	private void updateDisplay() {
	    this.mDateDisplay.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
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
		Intent intent = new Intent(AddCalendarEventActivity.this,CalendarActivity.class);
		startActivity(intent);
		this.finish();
	}
	

	
}
