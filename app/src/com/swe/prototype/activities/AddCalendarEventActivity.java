package com.swe.prototype.activities;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.swe.prototype.R;

public class AddCalendarEventActivity extends BaseActivity{
	private static final String TAG = "AddCalendarEventActivity";
	
	private int mYearFrom;
	private int mMonthFrom;
	private int mDayFrom;

	private int mYearTo;
	private int mMonthTo;
	private int mDayTo;
	
	
    private int hourFrom;
    private int minuteFrom;
    
    private int hourTo;
    private int minuteTo;
    //private int ampmFrom;
	
	private TextView mDateDisplayFrom;
	private TextView mDateDisplayTo;
	private TextView mTimeDisplayFrom;
	private TextView mTimeDisplayTo;
	
	
	static final int DATE_DIALOG_FROM = 0;
	static final int DATE_DIALOG_TO = 1;
	static final int TIME_DIALOG_FROM = 2;
	static final int TIME_DIALOG_TO = 3;
	
    private TimePickerDialog.OnTimeSetListener TimePickerListenerFrom =
            new TimePickerDialog.OnTimeSetListener() {

                // while dialog box is closed, below method is called.
    			@Override
                public void onTimeSet(TimePicker view, int hour, int minute) {
                    hourFrom = hour;
                    minuteFrom = minute;
                    mTimeDisplayFrom.setText(convertTime(hour, minute));
                    //Toast.makeText(getApplicationContext(), "hour:"+hour+" min: "+minute, 1).show();
                }

            };
            
     private TimePickerDialog.OnTimeSetListener TimePickerListenerTo =
                    new TimePickerDialog.OnTimeSetListener() {

                        // while dialog box is closed, below method is called.
            			@Override
                        public void onTimeSet(TimePicker view, int hour, int minute) {
                            hourTo = hour;
                            minuteTo = minute;
                            mTimeDisplayTo.setText(convertTime(hour, minute));
                        }

      };
      
      private String convertTime(int hours, int mins) {
          
          String timeSet = "";
          if (hours > 12) {
              hours -= 12;
              timeSet = "PM";
          } else if (hours == 0) {
              hours += 12;
              timeSet = "AM";
          } else if (hours == 12)
              timeSet = "PM";
          else
              timeSet = "AM";
   
           
          String minutes = "";
          if (mins < 10)
              minutes = "0" + mins;
          else
              minutes = String.valueOf(mins);
   
          // Append in a StringBuilder
           return new StringBuilder().append(hours).append(':')
                  .append(minutes).append(" ").append(timeSet).toString();
      }
   
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerFrom =
		    new DatePickerDialog.OnDateSetListener() {
				@Override
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYearFrom = year;
		            mMonthFrom = monthOfYear;
		            mDayFrom = dayOfMonth;
		            updateDisplayFrom();
		        }
	};
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerTo =
		    new DatePickerDialog.OnDateSetListener() {
				@Override
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYearTo = year;
		            mMonthTo = monthOfYear;
		            mDayTo = dayOfMonth;
		            updateDisplayTo();
		        }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcalendarevent);
		mDateDisplayFrom = (TextView) findViewById(R.id.textView_datepicker);
		mDateDisplayTo = (TextView) findViewById(R.id.textView_datepicker_to);
		mTimeDisplayFrom = (TextView) findViewById(R.id.textView_timepicker_from);
		mTimeDisplayTo = (TextView) findViewById(R.id.textView_timepicker_to);

	    final Calendar c = Calendar.getInstance();
	    mYearFrom = c.get(Calendar.YEAR);
	    mMonthFrom = c.get(Calendar.MONTH);
	    mDayFrom = c.get(Calendar.DAY_OF_MONTH);
	    updateDisplayFrom();
	    mYearTo = c.get(Calendar.YEAR);
	    mMonthTo = c.get(Calendar.MONTH);
	    mDayTo = c.get(Calendar.DAY_OF_MONTH);
        updateDisplayTo();
        
        // hier setzen wir die Zeit auf die aktuelle zeit.
	    hourFrom = c.get(Calendar.HOUR_OF_DAY);
	    hourTo = c.get(Calendar.HOUR_OF_DAY);
        minuteFrom = c.get(Calendar.MINUTE);
        minuteTo = c.get(Calendar.MINUTE);
        mTimeDisplayFrom.setText(convertTime(hourFrom, minuteFrom));
        mTimeDisplayTo.setText(convertTime(hourFrom, minuteFrom));
        // müssen wir uns noch entscheiden wei das gemacht werden soll
        mTimeDisplayFrom.setText(convertTime(6, 30));
        mTimeDisplayTo.setText(convertTime(18, 30));

	}

	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_FROM:
	      return new DatePickerDialog(this,
	                mDateSetListenerFrom,
	                mYearFrom, mMonthFrom, mDayFrom);
	      case DATE_DIALOG_TO:
	      return new DatePickerDialog(this,
	                mDateSetListenerTo,
	                mYearTo, mMonthTo, mDayTo);
	      case TIME_DIALOG_FROM:
              return new TimePickerDialog(this, TimePickerListenerFrom,
                      hourFrom, minuteFrom, false);
	      case TIME_DIALOG_TO:
              return new TimePickerDialog(this, TimePickerListenerTo,
                      hourTo, minuteTo, false);
	      
	   }
	   return null;
	}
	private void updateDisplayFrom() {
	    this.mDateDisplayFrom.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonthFrom + 1).append("-")
	                .append(mDayFrom).append("-")
	                .append(mYearFrom).append(" "));
	}
	
	private void updateDisplayTo() {
	    this.mDateDisplayTo.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonthTo + 1).append("-")
	                .append(mDayTo).append("-")
	                .append(mYearTo).append(" "));
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
	
	
	public void onClickChangeDateFrom(View v){
		showDialog(DATE_DIALOG_FROM);
		
	}
	
	public void onClickChangeDateTo(View v){
		showDialog(DATE_DIALOG_TO);
	}
	
	public void onClickChangeTimeFrom(View v){
		showDialog(TIME_DIALOG_FROM);
		
	}
	
	public void onClickChangeTimeTo(View v){
		showDialog(TIME_DIALOG_TO);
		
	}

	
}
