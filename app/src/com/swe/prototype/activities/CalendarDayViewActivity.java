package com.swe.prototype.activities;

import java.util.ArrayList;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.CalendarEntity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swe.prototype.helpers.DrawView;
import com.swe.prototype.helpers.RectangleCalendarEntry;
import com.swe.prototype.helpers.Tools;
import com.swe.prototype.models.CalendarEntry;

/**
 * @author Dany Brossel
 * 
 */
public class CalendarDayViewActivity extends BaseActivity {

	// Colors
	private static final int HOUR_TEXTVIEW_COLOR = Color.GRAY;
	private static final int TEXT_COLOR = Color.BLACK;

	//Padding
	private static final int TEXTVIEW_PADDING_LEFT = 2;
	private static final int TEXTVIEW_PADDING_TOP = 2;
	private static final int HEIGHT_OFFSET = 170;
	private static final int PADDING_TOP = 20;
	private static final int PADDING_LEFT = 150;
	private static final int PADDING_LEFT_LEFT = 20;
	public static final int BORDER = 2;
	private static final int PADDING_DESCRIPTION = 2+BORDER;
	
	
	
	
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
		oneMinute = oneHour/60;
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
		if(calendarEntrys!=null){
			initRectangles(calendarEntrys);
			addEventDescriptionTextViews(layout);
		}
		drawView = new DrawView(this, BORDER,width, PADDING_TOP, PADDING_LEFT,PADDING_LEFT_LEFT, oneHour, recList);
		layout.addView(drawView);
		this.addHourTextViews(layout);
		
		if(calendarEntrys!=null){
			addEventDescriptionTextViews(layout);
		}
		
		setContentView(layout);
	}
	
	/**
	 * @param e
	 * @return res[0]: minuten von 00:00 an & res[1]: länge des events in minuten
	 * 
	 */
	private int [] getEventLenght(CalendarEntry e){
		int [] res=new int[2];
		//Format: hh:mm:ss 
		String startTime = e.getStartTime();
		String endTime = e.getEndTime();
		int sH = Integer.parseInt(""+startTime.charAt(0)+""+startTime.charAt(1));
		int sM = Integer.parseInt(""+startTime.charAt(3)+""+startTime.charAt(4));
		res[0] = sH*60+sM;
		if(e.getStartDate().equals(e.getEndDate())){
			// eintages Event
			int eH = Integer.parseInt(""+endTime.charAt(0)+""+endTime.charAt(1));
			int eM = Integer.parseInt(""+endTime.charAt(3)+""+endTime.charAt(4));
			res[1]=(eH-sH)*60+(eM-sM);
			
		}else{
			//mehrtagesEvent: zähle bis mitternacht
			res[1]= (24-sH) *60 +sM;
		}
		return res;
		
	}
	
	private void initRectangles(ArrayList<CalendarEntry> entrys){
		recList = new ArrayList<RectangleCalendarEntry>();
		for (CalendarEntry e : entrys) {
			int []a = getEventLenght(e);
			System.out.println("a[0]"+a[0]+" a[1]"+a[1]);
			RectangleCalendarEntry rec = new RectangleCalendarEntry(e,PADDING_LEFT,(int)(a[0]*oneMinute)+PADDING_TOP,width-PADDING_LEFT,(int)((a[0]+a[1])*oneMinute +PADDING_TOP));
			recList.add(rec);
			
		}
	}

	private void addEventDescriptionTextViews(RelativeLayout layout) {
		
		for (RectangleCalendarEntry r : this.recList) {
			TextView t = new TextView(this);
			t.setX(r.x1+BORDER);
			t.setY(r.y1+BORDER);
			t.setWidth(r.w);
			t.setHeight(r.h);
			t.setTextSize(8);
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

	public static int LONG_PRESS_TIME = 500; // Time in miliseconds
	boolean longClickFlag = false;
	final Handler _handler = new Handler();
	Runnable _longPressed = new Runnable() {
		public void run() {
			Log.i("info", "LongPress");
			longClickFlag = true;
			
			
		}
	};

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			_handler.postDelayed(_longPressed, LONG_PRESS_TIME);
			break;
		case MotionEvent.ACTION_MOVE:
			_handler.removeCallbacks(_longPressed);
			longClickFlag = false;
			break;
		case MotionEvent.ACTION_UP:
			_handler.removeCallbacks(_longPressed);
			if (!longClickFlag) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				System.out.println("x: " + x + " y: " + y);
			}
			longClickFlag = false;
			break;
		}

		return super.onTouchEvent(event);

	}

	// um nen long click zu handeln, problem, der normale click ist dann nicht
	// mehr so gut handlebar
	/*
	 * public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * switch(event.getAction()){ case MotionEvent.ACTION_DOWN:
	 * _handler.postDelayed(_longPressed, LONG_PRESS_TIME); break; case
	 * MotionEvent.ACTION_MOVE: _handler.removeCallbacks(_longPressed); break;
	 * case MotionEvent.ACTION_UP: _handler.removeCallbacks(_longPressed);
	 * break; }
	 * 
	 * int x = (int) event.getX(); int y = (int) event.getY();
	 * System.out.println("x: "+x+" y: "+y);
	 * 
	 * return super.onTouchEvent(event); }
	 */

	// mein erster versuch für die dayview, klappt nicht, weil imageview nur
	// gleichmässig skalierbar...

	/*
	 * private static final int CELL_MARGIN = 10; ImageView i; TextView
	 * dateDisplay; public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); dateDisplay = new TextView(this);
	 * String date = getIntent().getStringExtra("date");
	 * dateDisplay.setText(date); dateDisplay.setTextSize(50);
	 * dateDisplay.setX(2500); Display display =
	 * getWindowManager().getDefaultDisplay(); int width = display.getWidth();
	 * int height = display.getHeight(); System.out.println("height: "+height);
	 * System.out.println("width: "+width); ExtendedView eventI = new
	 * ExtendedView(this, 0, true, true, true); RelativeLayout mainLayout = new
	 * RelativeLayout(this); mainLayout.addView(dateDisplay);
	 * mainLayout.setBackground
	 * (getResources().getDrawable(R.drawable.dayview_background)); for (int j =
	 * 0; j < 10; j++) { ImageView i = new ImageView(this);
	 * i.setImageResource(R.drawable.calendar_cel_event);
	 * i.setAdjustViewBounds(true); i.setX(j*10+70); i.setY(j*10+300);
	 * //i.getLayoutParams().height=j*5; //i.getLayoutParams().width=j*5;
	 * i.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * System.out.println("auf view geklickt"); } });
	 * 
	 * 
	 * 
	 * 
	 * i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
	 * LayoutParams.WRAP_CONTENT)); mainLayout.addView(i); }
	 * 
	 * ImageView i = new ImageView(this);
	 * i.setImageResource(R.drawable.calendar_day_event_synchro);
	 * //i.setAdjustViewBounds(true); i.setX(105); i.setY(155);
	 * 
	 * i.setLayoutParams(new Gallery.LayoutParams(50, 400));
	 * 
	 * // i.getLayoutParams().height=50; // i.getLayoutParams().width=200;
	 * i.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * System.out.println("auf synchroview geklickt");
	 * v.getLayoutParams().height = 50; v.getLayoutParams().width = 400;
	 * v.requestLayout();
	 * 
	 * } });
	 * 
	 * mainLayout.addView(i);
	 * 
	 * 
	 * setContentView(mainLayout); //
	 * 
	 * // }
	 */

}