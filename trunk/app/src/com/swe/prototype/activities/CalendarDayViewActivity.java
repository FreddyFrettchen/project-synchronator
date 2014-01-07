package com.swe.prototype.activities;

import com.swe.prototype.R;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarDayViewActivity extends BaseActivity {
	private static final int CELL_MARGIN = 10;
	TextView dateDisplay;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_day_view);
		dateDisplay = (TextView)findViewById(R.id.textview_date);
		String date = getIntent().getStringExtra("date");
		dateDisplay.setText(date);
	}
	
}