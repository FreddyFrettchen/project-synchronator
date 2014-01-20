package com.swe.prototype.helpers;

public class RectPairs {

	public RectPairs(RectangleCalendarEntry r1, RectangleCalendarEntry r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	RectangleCalendarEntry r1;
	RectangleCalendarEntry r2;
	
	public boolean equals(Object o){
		RectPairs oo = (RectPairs) o;
		if((oo.r1==r1&&oo.r2==r2) ||(oo.r1==r2&&oo.r2==r1)){
			return true;
		}
		
		return false;
	}
	
	
}
