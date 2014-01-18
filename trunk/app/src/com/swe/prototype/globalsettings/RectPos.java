package com.swe.prototype.globalsettings;

public class RectPos {
	//pixel values
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public int h;
	public int w;
	public RectPos(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.h = y2-y1;
		this.w=x2-x1;
	}
}
