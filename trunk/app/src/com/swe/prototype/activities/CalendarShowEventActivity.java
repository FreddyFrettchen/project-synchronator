package com.swe.prototype.activities;

import com.swe.prototype.R;
import com.swe.prototype.helpers.Tools;
import com.swe.prototype.models.CalendarEntry;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CalendarShowEventActivity extends BaseActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_show_event);
		CalendarEntry calE = getSynchronatorApplication()
				.getCurrentCalendarEntry();
		if (calE != null) {

			TextView dateFrom = (TextView) findViewById(R.id.textView_date_from_show_event);
			TextView dateTo = (TextView) findViewById(R.id.textView_date_to_show_event);
			TextView timeFrom = (TextView) findViewById(R.id.textView_time_from_show_event);
			TextView timeTo = (TextView) findViewById(R.id.textView_time_to_show_event);


			TextView desc = (TextView) findViewById(R.id.textView6);
			TextView repeatOption = (TextView) findViewById(R.id.textView_repeat_show_event);
			TextView saveLoc = (TextView) findViewById(R.id.textView_saveloc_show_entry);

			desc.setText(calE.getDescription());
			dateFrom.setText(Tools.convertDate(calE.getStartDate()));
			dateTo.setText(Tools.convertDate(calE.getEndDate()));
			timeFrom.setText(calE.getStartTime());
			timeTo.setText(calE.getEndTime());
			switch (calE.getRepeat()) {
			case 0:
				repeatOption.setText("No Repeat");
				break;
			case 1:
				repeatOption.setText("Every Day");
				break;
			case 2:

				repeatOption.setText("Every Month");
				break;
			case 3:
				repeatOption.setText("Every Year");
				break;

			default:
				repeatOption.setText("No Repeat");

			}
			saveLoc.setText(calE.getAccount().toString());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	public void onClickCancel(View v){
		finish();
	}
	
	
}
