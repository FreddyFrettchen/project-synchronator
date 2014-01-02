package com.swe.prototype.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.swe.prototype.R;

public class AddCalendarEventActivity extends BaseActivity{
	private static final String TAG = "AddCalendarEventActivity";
	
	// diese variabeln halten konsistent das zuletzt ausgewählte datum/zeit
	private int yearFrom;
	private int monthFrom;
	private int dayFrom;

	private int yearTo;
	private int monthTo;
	private int dayTo;
	
	
    private int hourFrom;
    private int minuteFrom;
    
    private int hourTo;
    private int minuteTo;
	
	private TextView dateDisplayFrom;
	private TextView dateDisplayTo;
	private TextView timeDisplayFrom;
	private TextView timeDisplayTo;
	
	
	static final int DATE_DIALOG_FROM = 0;
	static final int DATE_DIALOG_TO = 1;
	static final int TIME_DIALOG_FROM = 2;
	static final int TIME_DIALOG_TO = 3;
	
	CheckBox synchronatorCheckbox;
	CheckBox googleCheckbox;
	CheckBox exchangeCheckbox;
	
	EditText description;
	
	RadioGroup radioGroupEvery;
	
    private TimePickerDialog.OnTimeSetListener TimePickerListenerFrom =
            new TimePickerDialog.OnTimeSetListener() {
    			@Override
                public void onTimeSet(TimePicker view, int hour, int minute) {
                    hourFrom = hour;
                    minuteFrom = minute;
                    timeDisplayFrom.setText(convertTime(hour, minute));
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
                            timeDisplayTo.setText(convertTime(hour, minute));
                        }

      };
      

	
	private DatePickerDialog.OnDateSetListener mDateSetListenerFrom =
		    new DatePickerDialog.OnDateSetListener() {
				@Override
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            yearFrom = year;
		            monthFrom = monthOfYear;
		            dayFrom = dayOfMonth;
		            updateDisplayFrom();
		        }
	};
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerTo =
		    new DatePickerDialog.OnDateSetListener() {
				@Override
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            yearTo = year;
		            monthTo = monthOfYear;
		            dayTo = dayOfMonth;
		            updateDisplayTo();
		        }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcalendarevent);
		// find date and time elements
		dateDisplayFrom = (TextView) findViewById(R.id.textView_datepicker);
		dateDisplayTo = (TextView) findViewById(R.id.textView_datepicker_to);
		timeDisplayFrom = (TextView) findViewById(R.id.textView_timepicker_from);
		timeDisplayTo = (TextView) findViewById(R.id.textView_timepicker_to);
		//find description edit text
		description = (EditText) findViewById(R.id.editText_description);
		//find checkboxes
		synchronatorCheckbox = (CheckBox) findViewById(R.id.checkBox_synchronator_addcontact);
		googleCheckbox = (CheckBox) findViewById(R.id.checkBox_google_addcontact);
		exchangeCheckbox = (CheckBox) findViewById(R.id.checkBox_exchange_addcontact);
		// hier muss ich dann noch die checkboxen hiden, falls der user keinen account hinzugefügt hat.
		
		//find radio button group
		radioGroupEvery = (RadioGroup) findViewById(R.id.radiogroup_every);
		
		
		this.setDatePickerDisplayOnCurrentDate();
		this.setTimePickerDispalyOnCurrentTime();
		


	}

    private void setTimePickerDispalyOnCurrentTime() {
	    final Calendar c = Calendar.getInstance();
	    yearFrom = c.get(Calendar.YEAR);
	    monthFrom = c.get(Calendar.MONTH);
	    dayFrom = c.get(Calendar.DAY_OF_MONTH);
	    updateDisplayFrom();
	    yearTo = c.get(Calendar.YEAR);
	    monthTo = c.get(Calendar.MONTH);
	    dayTo = c.get(Calendar.DAY_OF_MONTH);
        updateDisplayTo();
        
	}

	private void setDatePickerDisplayOnCurrentDate() {
		final Calendar c = Calendar.getInstance();
	    hourFrom = c.get(Calendar.HOUR_OF_DAY);
	    hourTo = c.get(Calendar.HOUR_OF_DAY);
        minuteFrom = c.get(Calendar.MINUTE);
        minuteTo = c.get(Calendar.MINUTE);
        timeDisplayFrom.setText(convertTime(hourFrom, minuteFrom));
        timeDisplayTo.setText(convertTime(hourFrom, minuteFrom));
    }

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
 
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_FROM:
	      return new DatePickerDialog(this,
	                mDateSetListenerFrom,
	                yearFrom, monthFrom, dayFrom);
	      case DATE_DIALOG_TO:
	      return new DatePickerDialog(this,
	                mDateSetListenerTo,
	                yearTo, monthTo, dayTo);
	      case TIME_DIALOG_FROM:
              return new TimePickerDialog(this, TimePickerListenerFrom,
                      hourFrom, minuteFrom, false);
	      case TIME_DIALOG_TO:
              return new TimePickerDialog(this, TimePickerListenerTo,
                      hourTo, minuteTo, false);
       default:
    	   System.out.println("Dialog mit falscher ID!");
	      
	   }
	   return null;
	}
	private void updateDisplayFrom() {
	    this.dateDisplayFrom.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(monthFrom + 1).append("-")
	                .append(dayFrom).append("-")
	                .append(yearFrom).append(" "));
	}
	
	private void updateDisplayTo() {
	    this.dateDisplayTo.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(monthTo + 1).append("-")
	                .append(dayTo).append("-")
	                .append(yearTo).append(" "));
	}
	
	
	/*
	Die muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar ist.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	public void onClickSave(View v){
		//checken ob man überhaut speichern kann oder ob es inkonsistenzen gibt
		//falls inkonsistenz gibt correctInputChoise() ein Toast mit Fehlermeldung aus!
		if(correctInputChoise()){

			// achtung die speicheung ist ein bisschen komisch zb month wird ab 0 gezählt, und bei zeit: siehe convertTime
			System.out.println("fromDate:"+dayFrom+"/"+monthFrom+"/"+yearFrom+" fromTime: "+hourFrom+":"+minuteFrom);
			// eingegebene Beschreibung
			String descriptionString = description.getText().toString();
			System.out.println(descriptionString);
			int i =radioGroupEvery.getCheckedRadioButtonId();
			switch(i){
				case R.id.radioButton_everyday: 
					System.out.println("every day");
					break;
				case R.id.radioButton_everymonth: 
					System.out.println("every month");
					break;
				case R.id.radioButton_everyyear: 
					System.out.println("every year");
					break;
				default:
					System.out.println("Fehler bei RadioButtonGroup; id i="+i);
			
			}
			
			int checkboxCounter=0;
			if(synchronatorCheckbox.isChecked()){
				checkboxCounter++;
				System.out.println("Synchronator is checked!");
			}
			if(googleCheckbox.isChecked()){
				checkboxCounter++;
				System.out.println("google is checked!");
			}
			if(exchangeCheckbox.isChecked()){
				checkboxCounter++;
				System.out.println("Exchange is checked!");
			}
			if(checkboxCounter==0){
				showShortToast("You have to check at least one checkbox!");
				return;
			}
			
			Intent intent = new Intent(AddCalendarEventActivity.this,CalendarActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
	private void showShortToast(String message){
		Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Methode Prüft, ob:
	 * a) date und zeit von FROM < date und zeit von TO
	 * b) ob die ausgewählte radio button option gültig ist (bsp: Every Day: zeitspannne muss kleiner als ein Tag sein)
	 * @return
	 */
	private boolean correctInputChoise() {
		this.getMinutesBetweenSelectedDates();
/*		//so könnte man s auch lösen, hat dann aber keine schönen Fehlermeldungen
		if(this.getMinutesBetweenSelectedDates()<0){
			showShortToast("From Time < To Time");
		};*/
		
		if(yearFrom>yearTo){
			this.showShortToast("From Date > To Date");
			return false;
		}
		if(yearFrom==yearTo){
			if(monthFrom>monthTo){
				this.showShortToast("From Date > To Date");
				return false;
			}
			
			if(monthFrom == monthTo){
				if(dayFrom > dayTo){
					this.showShortToast("From Date > To Date");
					return false;
				}
				if(dayFrom == dayTo){
					if(hourFrom>hourTo){
						this.showShortToast("From Time > To Time");
						return false;
					}
					if(hourFrom==hourTo){
						//über diese bedingung kann man die küzest mögliche minuten zeit eines events einstellen.
						if(minuteFrom>=minuteTo){
							this.showShortToast("From Time >= To Time");
							return false;
						}
					}
				}
			}
		}
		
		
		// every day kann man nur machen, wenn das event weniger als 24 h dauert.
		// every month ...
		//every jear ...
		long minutesBetweenSelectedDates = getMinutesBetweenSelectedDates();
		switch(radioGroupEvery.getCheckedRadioButtonId()){
		case R.id.radioButton_everyday:
			// 60* 24 = 1440
			if(minutesBetweenSelectedDates>1440){
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one day!");
				return false;
			}
			break;
		case R.id.radioButton_everymonth:
			// 60*24*30 = 43200
			if(minutesBetweenSelectedDates>43200){
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one month!");
				return false;
			}
			break;
		case R.id.radioButton_everyyear:
			// 60*24*365 = 525600
			if(minutesBetweenSelectedDates>525600){
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one year!");
				return false;
			}
			break;
		default:
			// keine option ausgewählt
		}
		return true;
	}

	private long getMinutesBetweenSelectedDates() {
		// muss noch getestet werden
		long res = 0;
		GregorianCalendar from = new GregorianCalendar(yearFrom,monthFrom,dayFrom);
		GregorianCalendar to = new GregorianCalendar(yearTo, monthTo, dayTo);
		long difference = to.getTimeInMillis() - from.getTimeInMillis();
		res+= (difference/(1000*60)); 
		res+=((hourTo - hourFrom) * 60);
		res+= (minuteTo - minuteFrom); 
		return res;
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
