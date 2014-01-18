package com.swe.prototype.helpers;

import java.util.ArrayList;
import java.util.Calendar;

import com.swe.prototype.models.CalendarEntry;

import com.swe.prototype.helpers.RectangleCalendarEntry;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * @author Dany Brossel
 *
 * Kasse wird in der DayView zum Zeichnen der Übergebenen CalendarEntys benutzt und zum Background zeichnen.
 */
public class DrawView extends View {
    Paint paint = new Paint();

    final int COLOR_SYNCHRONATOR = Color.rgb(121,121,255);
    final int COLOR_GOOGLE = Color.rgb(241,73,82);
    final int COLOR_EXCHANGE = Color.rgb(0,230,115);
    final int PADDING_TOP;
    final int PADDING_LEFT;
    final int BORDER;
    final int PADDING_LEFT_LEFT;
    final float ONE_HOUR;
    final int WIDTH;
	private ArrayList<RectangleCalendarEntry> rectangles;
	
    public DrawView(Context context, int b,int width,int pt,int pl,int pll,float oneHour,ArrayList<RectangleCalendarEntry> rect) {
        super(context);
        BORDER = b;
        PADDING_TOP = pt;
        PADDING_LEFT =pl;
        PADDING_LEFT_LEFT =pll;
        this.rectangles = rect;
        this.ONE_HOUR = oneHour;
        this.WIDTH=width;
    }

    

	@Override
    public void onDraw(Canvas canvas) {
    	drawBackground(canvas);
    	
    	if(rectangles==null){
    		return;
    	}
    	for (RectangleCalendarEntry recPos : rectangles) {
    		drawSynchronatorRectangle(canvas, recPos);
		}

        
    }
    
    private void drawExchangeRectangle(Canvas canvas, RectangleCalendarEntry rectPos){
    	paint.setColor(Color.BLACK);
        canvas.drawRect(rectPos.x1, rectPos.y1,rectPos.x2,rectPos.y2, paint );
        paint.setColor(COLOR_EXCHANGE);
        canvas.drawRect(rectPos.x1+BORDER, rectPos.y1+BORDER,rectPos.x2-BORDER,rectPos.y2-BORDER, paint );
    }
    
    private void drawSynchronatorRectangle(Canvas canvas, RectangleCalendarEntry rectPos){
    	paint.setColor(Color.BLACK);
        canvas.drawRect(rectPos.x1, rectPos.y1,rectPos.x2,rectPos.y2, paint );
        paint.setColor(COLOR_SYNCHRONATOR);
        canvas.drawRect(rectPos.x1+BORDER, rectPos.y1+BORDER,rectPos.x2-BORDER,rectPos.y2-BORDER, paint );
        
    }
    private void drawGoogleRectangle(Canvas canvas, RectangleCalendarEntry rectPos){
    	paint.setColor(Color.BLACK);
        canvas.drawRect(rectPos.x1, rectPos.y1,rectPos.x2,rectPos.y2, paint );
        paint.setColor(COLOR_GOOGLE);
        canvas.drawRect(rectPos.x1+BORDER, rectPos.y1+BORDER,rectPos.x2-BORDER,rectPos.y2-BORDER, paint );
        
    }

	private void drawBackground(Canvas canvas) {
		paint.setColor(Color.GRAY);
        canvas.drawRect(PADDING_LEFT-PADDING_LEFT_LEFT, PADDING_TOP,PADDING_LEFT -PADDING_LEFT_LEFT +BORDER,(ONE_HOUR*24)+PADDING_TOP, paint );
        canvas.drawRect(WIDTH-PADDING_LEFT+PADDING_LEFT_LEFT-BORDER, PADDING_TOP,WIDTH-PADDING_LEFT+PADDING_LEFT_LEFT,(ONE_HOUR*24)+PADDING_TOP, paint );
        for (int i = 0; i <= 24; i++) {
        	canvas.drawRect(PADDING_LEFT-PADDING_LEFT_LEFT, 0+i*ONE_HOUR +PADDING_TOP,WIDTH-PADDING_LEFT+PADDING_LEFT_LEFT,BORDER+i*ONE_HOUR+PADDING_TOP, paint );
		}
        
	}

}