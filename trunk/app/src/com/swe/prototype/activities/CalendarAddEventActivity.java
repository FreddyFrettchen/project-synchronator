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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.swe.prototype.R;
import com.swe.prototype.models.AccountBase;

public class CalendarAddEventActivity extends BaseActivity {
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

	ListView list_accounts = null;

	EditText description;

	RadioGroup radioGroupEvery;

	private TimePickerDialog.OnTimeSetListener TimePickerListenerFrom = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			hourFrom = hour;
			minuteFrom = minute;
			timeDisplayFrom.setText(convertTime(hour, minute));
			// Toast.makeText(getApplicationContext(),
			// "hour:"+hour+" min: "+minute, 1).show();
		}

	};

	private TimePickerDialog.OnTimeSetListener TimePickerListenerTo = new TimePickerDialog.OnTimeSetListener() {

		// while dialog box is closed, below method is called.
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			hourTo = hour;
			minuteTo = minute;
			timeDisplayTo.setText(convertTime(hour, minute));
		}

	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			yearFrom = year;
			monthFrom = monthOfYear;
			dayFrom = dayOfMonth;
			updateDisplayFrom();
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
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
		// find description edit text
		description = (EditText) findViewById(R.id.editText_description);
		// find checkboxes

		ArrayAdapter<AccountBase> adapter = new ArrayAdapter<AccountBase>(this,
				android.R.layout.simple_list_item_checked,
				accounts.getAccounts());
		list_accounts = (ListView) findViewById(R.id.list_accounts);
		list_accounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_accounts.setAdapter(adapter);

		// hier muss ich dann noch die checkboxen hiden, falls der user keinen
		// account hinzugef�gt hat.

		// find radio button group
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

		String timeResult = "";
		if(hours<10){
			timeResult+="0";
		}
		timeResult+=hours+":";
		if(mins<10){
			timeResult+="0";
		}
		timeResult+=mins;
		return timeResult;
	}
	
	private String convertDate(int year, int month,int day) {

		String dateResult = ""+year+"-";
		//hier wird halt das datum januar = 01 maessig gebaut
		if((month+1)<10){
			dateResult+="0";
		}
		dateResult+=(month+1)+"-";
		if(day<10){
			dateResult+="0";
		}
		dateResult+=day;
		return dateResult;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_FROM:
			return new DatePickerDialog(this, mDateSetListenerFrom, yearFrom,
					monthFrom, dayFrom);
		case DATE_DIALOG_TO:
			return new DatePickerDialog(this, mDateSetListenerTo, yearTo,
					monthTo, dayTo);
		case TIME_DIALOG_FROM:
			return new TimePickerDialog(this, TimePickerListenerFrom, hourFrom,
					minuteFrom, true);
		case TIME_DIALOG_TO:
			return new TimePickerDialog(this, TimePickerListenerTo, hourTo,
					minuteTo, true);
		default:
			System.out.println("Dialog mit falscher ID!");

		}
		return null;
	}

	private void updateDisplayFrom() {
		String date = "";
		if (dayFrom < 10) {
			date += "0";
		}
		date += dayFrom + "/";
		if (monthFrom < 9) {
			date += "0";
		}
		date += monthFrom + 1;
		date += "/";
		date += yearFrom;
		this.dateDisplayFrom.setText(date);
	}

	private void updateDisplayTo() {
		String date = "";
		if (dayTo < 10) {
			date += "0";
		}
		date += dayTo + "/";
		if (monthTo < 9) {
			date += "0";
		}
		date += monthTo + 1;
		date += "/";
		date += yearTo;
		this.dateDisplayTo.setText(date);

	}

	/*
	 * Die muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar
	 * ist.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void onClickSave(View v) {
		//"2014-02-25 16:00:00" so h�tten wir das gerne!!!
		// checken ob man �berhaut speichern kann oder ob es inkonsistenzen gibt
		// falls inkonsistenz gibt correctInputChoise() ein Toast mit
		// Fehlermeldung aus!
		if (correctInputChoise()) {
			String startDate = this.convertDate(yearFrom, monthFrom, dayFrom);//format= yyyy-mm-dd wobei january=01
			String endDate = this.convertDate(yearTo, monthTo, dayTo);
			String startTime = this.convertTime(hourFrom, minuteFrom);//format= hh:mm
			String endTime = this.convertTime(hourTo, minuteTo);
			String descr = description.getText().toString();
			int every = 0;
			// radio button: every==0 keine wiederholung,every=1:
			// every_Dayevery=2 : every_month every=3: every_year
			int i = radioGroupEvery.getCheckedRadioButtonId();
			switch (i) {
			case R.id.radioButton_everyday:
				every = 1;
				//System.out.println("every day");
				break;
			case R.id.radioButton_everymonth:
				every = 2;
				//System.out.println("every month");
				break;
			case R.id.radioButton_everyyear:
				every = 3;
				//System.out.println("every year");
				break;
			default:
			}

			// jetzt auf allen hinzugef�gten Accounts speichern.
			int cntChoice = list_accounts.getCount();
			SparseBooleanArray selected_accounts = list_accounts
					.getCheckedItemPositions();
			boolean saveLocationChecked=false;
			for (int k = 0; k < cntChoice; k++) {
				if (selected_accounts.get(k) == true) {
					saveLocationChecked=true;
					accounts.getAccounts()
							.get(k)
							.createCalendarEntry(startDate, endDate, startTime+":00",
									endTime+":00", descr, every); // +":00" ist weil bahos da i.wie noch die sekunden dran haben wollte
				}
			}
			
			// der user muss mindestens einen account als speicherlocation ausw�hlen
			if(saveLocationChecked){
				Intent intent = new Intent(CalendarAddEventActivity.this,
						CalendarMonthViewActivity.class);
				startActivity(intent);
				finish();
			}
			else{
				showShortToast("Please select a save location!");
			}

		}

	}

	private void showShortToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * Methode Pr�ft, ob: a) date und zeit von FROM < date und zeit von TO b) ob
	 * die ausgew�hlte radio button option g�ltig ist (bsp: Every Day:
	 * zeitspannne muss kleiner als ein Tag sein)
	 * 
	 * @return
	 */
	private boolean correctInputChoise() {
		this.getMinutesBetweenSelectedDates();
		/*
		 * //so k�nnte man s auch l�sen, hat dann aber keine sch�nen
		 * Fehlermeldungen if(this.getMinutesBetweenSelectedDates()<0){
		 * showShortToast("From Time < To Time"); };
		 */

		if (yearFrom > yearTo) {
			this.showShortToast("From Date > To Date");
			return false;
		}
		if (yearFrom == yearTo) {
			if (monthFrom > monthTo) {
				this.showShortToast("From Date > To Date");
				return false;
			}

			if (monthFrom == monthTo) {
				if (dayFrom > dayTo) {
					this.showShortToast("From Date > To Date");
					return false;
				}
				if (dayFrom == dayTo) {
					if (hourFrom > hourTo) {
						this.showShortToast("From Time > To Time");
						return false;
					}
					if (hourFrom == hourTo) {
						// �ber diese bedingung kann man die k�zest m�gliche
						// minuten zeit eines events einstellen.
						if (minuteFrom >= minuteTo) {
							this.showShortToast("From Time >= To Time");
							return false;
						}
					}
				}
			}
		}

		// every day kann man nur machen, wenn das event weniger als 24 h
		// dauert.
		// every month ...
		// every jear ...
		long minutesBetweenSelectedDates = getMinutesBetweenSelectedDates();
		switch (radioGroupEvery.getCheckedRadioButtonId()) {
		case R.id.radioButton_everyday:
			// 60* 24 = 1440
			if (minutesBetweenSelectedDates > 1440) {
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one day!");
				return false;
			}
			break;
		case R.id.radioButton_everymonth:
			// 60*24*30 = 43200
			if (minutesBetweenSelectedDates > 43200) {
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one month!");
				return false;
			}
			break;
		case R.id.radioButton_everyyear:
			// 60*24*365 = 525600
			if (minutesBetweenSelectedDates > 525600) {
				this.showShortToast("You can't select the Every Day option if the time between your selected dates is bigger than one year!");
				return false;
			}
			break;
		default:
			// keine option ausgew�hlt
		}
		return true;
	}

	private long getMinutesBetweenSelectedDates() {
		// muss noch getestet werden
		long res = 0;
		GregorianCalendar from = new GregorianCalendar(yearFrom, monthFrom,
				dayFrom);
		GregorianCalendar to = new GregorianCalendar(yearTo, monthTo, dayTo);
		long difference = to.getTimeInMillis() - from.getTimeInMillis();
		res += (difference / (1000 * 60));
		res += ((hourTo - hourFrom) * 60);
		res += (minuteTo - minuteFrom);
		return res;
	}

	public void onClickCancel(View v) {
		Intent intent = new Intent(CalendarAddEventActivity.this,
				CalendarMonthViewActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void onClickChangeDateFrom(View v) {
		showDialog(DATE_DIALOG_FROM);

	}

	public void onClickChangeDateTo(View v) {
		showDialog(DATE_DIALOG_TO);
	}

	public void onClickChangeTimeFrom(View v) {
		showDialog(TIME_DIALOG_FROM);

	}

	public void onClickChangeTimeTo(View v) {
		showDialog(TIME_DIALOG_TO);

	}

}
