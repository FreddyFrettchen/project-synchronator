package com.swe.prototype.activities;

import android.content.Intent;
import android.os.Bundle;

import com.swe.prototype.R;

public class CalendarActivity extends BaseActivity {

	private static final String TAG = "CalendarActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
	}

	@Override
	protected void addClicked() {
		Intent intent = new Intent(CalendarActivity.this,
				AddCalendarEventActivity.class);
		startActivity(intent);
		finish();

	}
}
