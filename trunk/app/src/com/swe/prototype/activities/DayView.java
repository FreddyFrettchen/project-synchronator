package com.swe.prototype.activities;

import android.widget.CalendarView;


public class DayView extends CalendarView {
    private static final int CELL_MARGIN = 10;

    public DayView(CalendarActivity activity) {
        super(activity);
        
        
        init();
    }

    private void init() {
        boolean mDrawTextInEventRect = true;
        int mNumDays = 1;
       // mEventGeometry.setCellMargin(CELL_MARGIN);
    }
 }