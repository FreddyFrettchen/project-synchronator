package com.swe.prototype.activities;

import java.util.ArrayList;

import com.swe.prototype.R;
import com.swe.prototype.globalsettings.DrawView;
import com.swe.prototype.globalsettings.Tools;
import com.swe.prototype.models.CalendarEntry;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
	
/**
 * @author Dany Brossel
 *
 */
public class CalendarDayViewActivity extends BaseActivity {
	
	private final int TEXTVIEW_PADDING_LEFT = 2;
	private final int TEXTVIEW_PADDING_TOP = 2;
	private final int PADDING_TOP = 20;
	
	DrawView drawView;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		// display abmessung in pixel
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int height = display.getHeight();
		float oneHoure = (height-200)/24;
		final TypedArray styledAttributes = getBaseContext().getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		
		// ausgewählte datum in der dayView anzeigen
		String date = getIntent().getStringExtra("date");
		getActionBar().setTitle(Tools.convertDate(date));
		/*
		 * TextView headline = new TextView(this);
		headline.setTextSize(30);
		//headline.setX((width/2)-200);
		SpannableString spanStringDate = new SpannableString(date);
		spanStringDate.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringDate.length(), 0); // fettschrift
		headline.setText(spanStringDate);
		layout.addView(headline);
		*/
		

		String dates = "Entrys: ";
		ArrayList<CalendarEntry> calendarEntrys = getSynchronatorApplication().getCurrentCalendarEntryList();
		if(calendarEntrys!=null){
			for (CalendarEntry e : calendarEntrys) {
				dates+="("+e.getStartTime()+"-"+e.getEndTime()+") ,";
			}
		}
		else{
			dates = "Keine Termine übergeben!";
		}
		TextView t = new TextView(this);
		t.setTextSize(8);
		t.setX(104);
		t.setY(136);
		t.setText("Kochen mit gertrude");
		drawView = new DrawView(this,width,height,PADDING_TOP,oneHoure,mActionBarSize,calendarEntrys);
        
		//drawView.setBackground(getResources().getDrawable(R.drawable.dayview_background));
        layout.addView(drawView);
		layout.addView(t);
		
		for (int i = 0; i <= 24; i++) {
			
			TextView tmp = new TextView(this);
			tmp.setTextColor(Color.GRAY);
			tmp.setTextSize(20);
			tmp.setX(0+TEXTVIEW_PADDING_LEFT);
			tmp.setY((i*oneHoure)+TEXTVIEW_PADDING_TOP);
			String hourBack = "";
			if(i<10){
				hourBack = "0"+i+":00";
			}else{
				hourBack = ""+i+":00";
			}
			tmp.setText(""+hourBack);
			layout.addView(tmp);
		}
		
		setContentView(layout);
//		dateTextView = (TextView) findViewById(R.id.textview_date);
//		String date = getIntent().getStringExtra("date");
//		
//		dateTextView.setText("datum");
        ////        TextView t = new TextView(this);
//        t.setX(100);
//        t.setY(100);
//        t.setText("Kochen mit gertrude");
//        drawView.addView(t);
//        setContentView(drawView);
		
	}
	
	public static int LONG_PRESS_TIME = 500; // Time in miliseconds 
	boolean longClickFlag = false;
	final Handler _handler = new Handler(); 
	Runnable _longPressed = new Runnable() { 
	    public void run() {
	        Log.i("info","LongPress");
	        longClickFlag = true;
	    }   
	};
	
	
	public boolean onTouchEvent(MotionEvent event) {
       
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
            _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
            break;
        case MotionEvent.ACTION_MOVE:
            _handler.removeCallbacks(_longPressed);
            longClickFlag = false;
            break;
        case MotionEvent.ACTION_UP:
            _handler.removeCallbacks(_longPressed);
            if(!longClickFlag){
            	int x = (int) event.getX();
            	int y = (int) event.getY();
            	System.out.println("x: "+x+" y: "+y);
            }
            longClickFlag = false;
            break;
        }
                
        
    return super.onTouchEvent(event);
    
    
	}
	
	
	//um nen long click zu handeln, problem, der normale click ist dann nicht mehr so gut handlebar
/*
	public boolean onTouchEvent(MotionEvent event) {
	       
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
            _handler.postDelayed(_longPressed, LONG_PRESS_TIME);
            break;
        case MotionEvent.ACTION_MOVE:
            _handler.removeCallbacks(_longPressed);
            break;
        case MotionEvent.ACTION_UP:
            _handler.removeCallbacks(_longPressed);
            break;
        }
        
        int x = (int) event.getX();
        int y = (int) event.getY();
        System.out.println("x: "+x+" y: "+y);        
        
    return super.onTouchEvent(event);
	}
*/
	
	// mein erster versuch für die dayview, klappt nicht, weil imageview nur gleichmässig skalierbar...
	
/*		private static final int CELL_MARGIN = 10;
	ImageView i;
	TextView dateDisplay;
 * public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dateDisplay = new TextView(this);
		String date = getIntent().getStringExtra("date");
		dateDisplay.setText(date);
		dateDisplay.setTextSize(50);
		dateDisplay.setX(2500);
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int height = display.getHeight();
		System.out.println("height: "+height);
		System.out.println("width: "+width);
		ExtendedView eventI = new ExtendedView(this, 0, true, true, true);
		RelativeLayout mainLayout = new RelativeLayout(this);
		mainLayout.addView(dateDisplay);
		mainLayout.setBackground(getResources().getDrawable(R.drawable.dayview_background));
		for (int j = 0; j < 10; j++) {
			ImageView i = new ImageView(this);
			i.setImageResource(R.drawable.calendar_cel_event);
			i.setAdjustViewBounds(true);
			i.setX(j*10+70);
			i.setY(j*10+300);
			//i.getLayoutParams().height=j*5;
			//i.getLayoutParams().width=j*5;
			i.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("auf view geklickt");
				}
			});
			
			
			
			
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
					  LayoutParams.WRAP_CONTENT));
			mainLayout.addView(i);
		}
		
		ImageView i = new ImageView(this);
		i.setImageResource(R.drawable.calendar_day_event_synchro);
		//i.setAdjustViewBounds(true);
		i.setX(105);
		i.setY(155);

		i.setLayoutParams(new Gallery.LayoutParams(50,
				  400));
		
		//		i.getLayoutParams().height=50;
//		i.getLayoutParams().width=200;
		i.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("auf synchroview geklickt");
				v.getLayoutParams().height = 50;
				v.getLayoutParams().width = 400;
				v.requestLayout();
				
			}
		});

		mainLayout.addView(i);
		
		
		setContentView(mainLayout);
//		

//		
	}*/
	
}