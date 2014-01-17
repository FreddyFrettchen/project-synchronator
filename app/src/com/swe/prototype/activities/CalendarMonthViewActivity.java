package com.swe.prototype.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import com.commonsware.cwac.merge.MergeAdapter;
import com.swe.prototype.R;
import com.swe.prototype.adapter.CalendarAdapter;
import com.swe.prototype.adapter.ContactAdapter;
import com.swe.prototype.globalsettings.DateOnSaveLocation;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.server.ServerCalendarEntry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarMonthViewActivity extends BaseActivity {

	protected static final String TAG = "CalendarMonthViewActivity";
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<DateOnSaveLocation> items; // container to store calendar
												// items which

	public HashMap<String, DateOnSaveLocation> itemHashMap = null;
	public HashMap<String, ArrayList<CalendarEntry>> eventsOnDate = null;

	// needs showing the event marker

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_month_view);
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<DateOnSaveLocation>();
		adapter = new CalendarAdapter(this, month);

		getSynchronatorApplication().setCurrentCalendarEntryList(null);
		// Methode zieht die daten aus dem Adapter jedes Accounts heraus
		initCalendarEvents();
		// itemHashMap +eventsOnDate initialisiert

		


		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
					return; // hier wird dann erstmal der vorherige monat
							// geladen
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
					return; // hier nï¿½chste monat laden
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				getSynchronatorApplication().setCurrentCalendarEntryList(
						eventsOnDate.get(selectedGridDate));
				// System.out.println("selectedGridDate "+selectedGridDate);
				showDayView(selectedGridDate);
				// showToast(selectedGridDate);

			}
		});
	}

	private void showDayView(String date) {
		Intent intent = new Intent(CalendarMonthViewActivity.this,
				CalendarDayViewActivity.class);
		intent.putExtra("date", date);
		startActivity(intent);
	}

	// falls der user auf den Rechts Pfeil klickt
	// danach wird auch immer refreshCalendar() aufgerufen
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	// falls der user auf den Links Pfeil klickt
	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print current date
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			itemvalue = df.format(itemmonth.getTime());
			itemmonth.add(GregorianCalendar.DATE, 1);
			
			for (Object e : itemHashMap.values()) {
				items.add((DateOnSaveLocation)e);
			}
			
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void addClicked() {
		Intent intent = new Intent(CalendarMonthViewActivity.this,
				CalendarAddEventActivity.class);
		startActivity(intent);
		finish();

	}

	private void initCalendarEvents() {
		itemHashMap = new HashMap<String, DateOnSaveLocation>();
		eventsOnDate = new HashMap<String, ArrayList<CalendarEntry>>();
		for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<CalendarEntry> adapterI = (ArrayAdapter<CalendarEntry>) this.accounts
					.getAccounts()
					.get(i)
					.getCalendarAdapter(this, R.layout.item_calendar_month_view);
			//Log.i(TAG, "der CalenderAdapter des " + i + ". Accounts hat "
			//		+ adapterI.getCount() + " EintrÃ¤ge");
			for (int j = 0; j < adapterI.getCount(); j++) {
				CalendarEntry e = (CalendarEntry) adapterI.getItem(j);
				if (e != null) {
					//Log.i(TAG,
					//		"CalenderAdapter: Account (" + i
					//				+ ") CalenderEvent:" + e + "\nDate:"
					//				+ e.getStartDate());

					putIntoDataStructures(e);

				}
			}
		}
	}

	private void putIntoDataStructures(CalendarEntry e) {
		String startDate = e.getStartDate();
		String endDate = e.getEndDate();
		// Log.i(TAG, "startDate:"+startDate+" endDate: "+endDate);
		// kann später entfernt werden; Fall darf nicht auftreten
		if (startDate == null || endDate == null) {
			Log.i(TAG, "CalendarEntry e: startDate oder endDate == null");
			return;
		}
		// erster Fall: eintagesEvent
		if (startDate.equals(endDate)) {
			putIntoDataStructures(e, startDate);
		} else {
			// 2.Fall mehrere Tage event
		}

	}

	private void putIntoDataStructures(CalendarEntry e, String date) {

		// erstmal bauen wir uns ne Liste von CalendarEntrys für jeden möglichen
		// Tag, um später der dayview zu übergeben
		if (eventsOnDate.containsKey(date)) {
			eventsOnDate.get(date).add(e);
		} else {
			ArrayList<CalendarEntry> tmpList = new ArrayList<CalendarEntry>();
			tmpList.add(e);
			eventsOnDate.put(date, tmpList);
		}

		char acc = e.getAccount().toString().charAt(0);
		// hier bauen wir für den Adapter ne Hashmap die Weiss, an welchem datum
		// welcher Account nen eintrag hat
		// diese hashmap wir dann später zu ner Liste und dem Calendar Adapter
		// übergeben
		if (itemHashMap.containsKey(date)) {

			switch (acc) {
			case 'G': {
				itemHashMap.get(date).setG();
				break;
			}
			case 'S': {
				itemHashMap.get(date).setS();
				break;
			}
			case 'E': {
				itemHashMap.get(date).setE();
				break;
			}
			default: {
				Log.i(TAG,
						"Konvention gebrochen: Account.toString() fängt an mit char: "
								+ acc);
			}
			}

		} else {
			switch (acc) {
			case 'G': {
				itemHashMap.put(date, new DateOnSaveLocation(date, false, true,
						false));
				break;
			}
			case 'S': {
				itemHashMap.put(date, new DateOnSaveLocation(date, true, false,
						false));
				break;
			}
			case 'E': {
				itemHashMap.put(date, new DateOnSaveLocation(date, false,
						false, true));
				break;
			}
			default: {
				Log.i(TAG,
						"Konvention gebrochen: Account.toString() fängt an mit char: "
								+ acc);
			}
			}

		}

	}
}