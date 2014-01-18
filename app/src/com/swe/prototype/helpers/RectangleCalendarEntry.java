package com.swe.prototype.helpers;

import com.swe.prototype.models.CalendarEntry;

/**
 * @author Dany Brossel
 *
 * Wird in der DayView zur Koordinatenspeicherung der einzelnen CalendarEntys benutzt
 */
public class RectangleCalendarEntry {
	
	public CalendarEntry calEntry;
	//pixel values
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public int h;
	public int w;
	public int account=0;//0=Synchro,1=Google,2= Exchange , also standardmaessig Synchronator
	public RectangleCalendarEntry(CalendarEntry e, int x1, int y1, int x2, int y2) {
		this.calEntry = e;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.h = y2-y1;
		this.w=x2-x1;
	}
	public void setGoogle(){
		this.account =1;
	}
	public void setExchnage(){
		this.account =2;
	}
	
}
