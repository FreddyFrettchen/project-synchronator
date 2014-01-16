package com.swe.prototype.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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

	// needs showing the event marker

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_month_view);
		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<DateOnSaveLocation>();
		adapter = new CalendarAdapter(this, month);

		for (int i = 0; i < this.accounts.getAccounts().size(); i++) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<CalendarEntry> x = (ArrayAdapter<CalendarEntry>) this.accounts
					.getAccounts()
					.get(i)
					.getCalendarAdapter(this, R.layout.item_calendar_month_view);
			Log.i(TAG,
					"der CalenderAdapter des " + i + ". Accounts hat "
							+ x.getCount() + " Einträge");
			for (int j = 0; j < x.getCount(); j++) {
				CalendarEntry e = (CalendarEntry) x.getItem(j);
				if (e != null) {
					Log.i(TAG,
							"CalenderAdapter: Account (" + i
									+ ") CalenderEvent:" + e + "\nDate:"
									+ e.getStartDate());

					System.out.println(e);
					System.out.println(e.getStartDate());
				}
			}
		}

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
				System.out.println(selectedGridDate);
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
					return; // hier n�chste monat laden
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

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

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(new DateOnSaveLocation("2013-12-12", true, true, true));
				items.add(new DateOnSaveLocation("2014-01-15", true, false,
						true));
				items.add(new DateOnSaveLocation("2014-01-20", true, true,
						false));
				items.add(new DateOnSaveLocation("2014-02-24", false, false,
						false));
				items.add(new DateOnSaveLocation("2014-02-28", false, true,
						true));

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
}