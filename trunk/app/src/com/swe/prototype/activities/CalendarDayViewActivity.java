package com.swe.prototype.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.CalendarEntity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.swe.prototype.R;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.helpers.DrawView;
import com.swe.prototype.helpers.RectangleCalendarEntry;
import com.swe.prototype.helpers.Tools;
import com.swe.prototype.models.CalendarEntry;
import com.swe.prototype.models.Contact;

/**
 * @author Dany Brossel
 * 
 */
public class CalendarDayViewActivity extends BaseActivity {

	// Colors
	private static final int HOUR_TEXTVIEW_COLOR = Color.GRAY;
	private static final int TEXT_COLOR = Color.BLACK;

	// Padding
	private static final int TEXTVIEW_PADDING_LEFT = 2;
	private static final int TEXTVIEW_PADDING_TOP = 2;
	private static final int HEIGHT_OFFSET = 170;
	private static final int PADDING_TOP = 20;
	private static final int PADDING_LEFT = 150;
	private static final int PADDING_LEFT_LEFT = 20;
	public static final int BORDER = 2;
	private static final int PADDING_DESCRIPTION = 2 + BORDER;

	private int width;
	private int height;
	private float oneHour;
	private float oneMinute;
	private int actionBarSize;
	private DrawView drawView;
	private String currentDate;
	private ArrayList<CalendarEntry> calendarEntrys;
	private ArrayList<RectangleCalendarEntry> recList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		// display abmessung in pixel
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		oneHour = (height - HEIGHT_OFFSET) / 24;
		oneMinute = oneHour / 60;
		final TypedArray styledAttributes = getBaseContext().getTheme()
				.obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		actionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		// ausgewählte datum in der dayView anzeigen
		currentDate = getIntent().getStringExtra("date");
		getActionBar().setTitle(Tools.convertDate(currentDate));

		calendarEntrys = getSynchronatorApplication()
				.getCurrentCalendarEntryList();
		if (calendarEntrys != null) {
			initRectangles(calendarEntrys);
			addEventDescriptionTextViews(layout);
		}
		drawView = new DrawView(this, BORDER, width, PADDING_TOP, PADDING_LEFT,
				PADDING_LEFT_LEFT, oneHour, recList);
		layout.addView(drawView);
		this.addHourTextViews(layout);

		if (calendarEntrys != null) {
			addEventDescriptionTextViews(layout);
		}

		System.out.println("actionBarSize: " + actionBarSize);
		setContentView(layout);
	}

	/**
	 * @param e
	 * @return res[0]: minuten von 00:00 an & res[1]: länge des events in
	 *         minuten
	 * 
	 */
	private int[] getEventLenght(CalendarEntry e) {
		int[] res = new int[2];
		// Format: hh:mm:ss
		String startTime = e.getStartTime();
		String endTime = e.getEndTime();
		int sH = Integer.parseInt("" + startTime.charAt(0) + ""
				+ startTime.charAt(1));
		int sM = Integer.parseInt("" + startTime.charAt(3) + ""
				+ startTime.charAt(4));
		res[0] = sH * 60 + sM;
		if (e.getStartDate().equals(e.getEndDate())) {
			// eintages Event
			int eH = Integer.parseInt("" + endTime.charAt(0) + ""
					+ endTime.charAt(1));
			int eM = Integer.parseInt("" + endTime.charAt(3) + ""
					+ endTime.charAt(4));
			res[1] = (eH - sH) * 60 + (eM - sM);

		} else {
			// mehrtagesEvent: zähle bis mitternacht
			res[1] = (24 - sH) * 60 + sM;
		}
		return res;

	}

	private void initRectangles(ArrayList<CalendarEntry> entrys) {
		recList = new ArrayList<RectangleCalendarEntry>();
		for (CalendarEntry e : entrys) {
			int[] a = getEventLenght(e);
			// standardaccount == synchronator
			RectangleCalendarEntry rec = new RectangleCalendarEntry(e,
					PADDING_LEFT, (int) (a[0] * oneMinute) + PADDING_TOP, width
							- PADDING_LEFT,
					(int) ((a[0] + a[1]) * oneMinute + PADDING_TOP));
			if (e.getAccount().toString().charAt(0) == 'E') {
				rec.setExchnage();
			}
			if (e.getAccount().toString().charAt(0) == 'G') {
				rec.setGoogle();
			}
			recList.add(rec);

		}
	}

	/**
	 * @param layout
	 */
	private void addEventDescriptionTextViews(RelativeLayout layout) {

		for (RectangleCalendarEntry r : this.recList) {
			TextView t = new TextView(this);
			t.setX(r.x1 + BORDER * 3);
			t.setY(r.y1 + BORDER);
			t.setWidth(r.w);
			t.setHeight(r.h);
			t.setTextColor(TEXT_COLOR);
			t.setText(r.calEntry.getDescription());
			layout.addView(t);
		}

	}

	private void addHourTextViews(RelativeLayout layout) {
		for (int i = 0; i <= 24; i++) {
			TextView tmp = new TextView(this);
			tmp.setTextColor(HOUR_TEXTVIEW_COLOR);
			tmp.setTextSize(20);
			tmp.setX(0 + TEXTVIEW_PADDING_LEFT);
			tmp.setY((i * oneHour) + TEXTVIEW_PADDING_TOP);
			String hourBack = "";
			if (i < 10) {
				hourBack = "0" + i + ":00";
			} else {
				hourBack = "" + i + ":00";
			}
			tmp.setText("" + hourBack);
			layout.addView(tmp);
		}
	}

	private boolean onTouchBlocked = false;
	private int ONTOUCH_OFFSET = 120;

	public boolean onTouchEvent(MotionEvent e) {
		if (!onTouchBlocked) {

			int x = (int) e.getX();
			int y = (int) e.getY();
			y -= +ONTOUCH_OFFSET;
			// System.out.println("x: " + x + "y: " + y);
			if (x < PADDING_LEFT + PADDING_LEFT_LEFT
					|| x > (width - (PADDING_LEFT + PADDING_LEFT_LEFT))
					|| y < (PADDING_TOP)) {
				// Toast.makeText(this, "Ausserhalb geklickt",
				// Toast.LENGTH_SHORT).show();
				return true;
			}

			for (RectangleCalendarEntry rect : recList) {
				if (y > rect.y1 && y < rect.y2) {
					// System.out.println("x: " + x + "y: " + e.getY());
					// System.out.println("Treffer: "+rect.calEntry.getDescription());
					// System.out.println("ry1: "+rect.y1+" ry2: "+rect.y2);
					
					onTouchBlocked = true;
					CalendarEntry calEntry = rect.calEntry;
					clickedEntry(rect);

					return true;
				}
			}
		}
		return true;

	}

	private void clickedEntry(final RectangleCalendarEntry rec) {
		AlertDialog.Builder onclickAlert = new AlertDialog.Builder(this);
		LayoutInflater inflator = getLayoutInflater();
		View optionDialogView = inflator.inflate(
				R.layout.dialog_calendarentry, null);
		Button show = (Button)optionDialogView.findViewById(R.id.button_show_cal_entry);
		Button edit = (Button)optionDialogView.findViewById(R.id.button_change_cal_entry);
		Button delete = (Button)optionDialogView.findViewById(R.id.button_delete_cal_entry);
		show.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show entry
				getSynchronatorApplication().setCurrentCalendarEntry(rec.calEntry);
				Intent intent = new Intent(CalendarDayViewActivity.this,
						CalendarShowEventActivity.class);
				startActivity(intent);
				
			}
		});
		edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//edit entry
				getSynchronatorApplication().setCurrentCalendarEntry(rec.calEntry);
				Intent intent = new Intent(CalendarDayViewActivity.this,
						CalendarAddEventActivity.class);
				Bundle b = new Bundle();
				b.putBoolean("edit", true);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//delete
				rec.calEntry.delete();
				Intent intent = new Intent(CalendarDayViewActivity.this,
						CalendarMonthViewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		onclickAlert.setView(optionDialogView);
		onclickAlert.setCancelable(true);
		onclickAlert.setTitle("" + rec.calEntry.getDescription());
		switch (rec.account) {
		case 0:
			onclickAlert.setIcon(getResources().getDrawable(
					R.drawable.alert_synchronator));
			break;

		case 1:
			onclickAlert.setIcon(getResources().getDrawable(
					R.drawable.alert_google));
			break;

		case 2:
			onclickAlert.setIcon(getResources().getDrawable(
					R.drawable.alert_exchange));
			break;
		}

		onclickAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				onTouchBlocked = false;
			}
		});
		
		onclickAlert.setCancelable(true);
		onclickAlert.create().show();

	}

	/*
	 * public static int LONG_PRESS_TIME = 500; // Time in miliseconds boolean
	 * longClickFlag = false; final Handler _handler = new Handler(); Runnable
	 * _longPressed = new Runnable() { public void run() { Log.i("info",
	 * "LongPress"); longClickFlag = true;
	 * 
	 * 
	 * } };
	 * 
	 * public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * switch (event.getAction()) { case MotionEvent.ACTION_DOWN:
	 * _handler.postDelayed(_longPressed, LONG_PRESS_TIME); break; case
	 * MotionEvent.ACTION_MOVE: _handler.removeCallbacks(_longPressed);
	 * longClickFlag = false; break; case MotionEvent.ACTION_UP:
	 * _handler.removeCallbacks(_longPressed); if (!longClickFlag) { int x =
	 * (int) event.getX(); int y = (int) event.getY(); System.out.println("x: "
	 * + x + " y: " + y); } longClickFlag = false; break; }
	 * 
	 * return super.onTouchEvent(event);
	 * 
	 * }
	 */

	@Override
	protected void addClicked() {
		Intent intent = new Intent(CalendarDayViewActivity.this,
				CalendarAddEventActivity.class);
		startActivity(intent);
		finish();

	}

}