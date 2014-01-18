package com.swe.prototype.globalsettings;

import java.util.ArrayList;

import com.swe.prototype.models.CalendarEntry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    float oneHour;
    ArrayList<CalendarEntry> toPaint;
    
    final int paddingTop;
    final int PADDING_LEFT=100;
    int widht;
    int height;
	private int abHeight;
    public DrawView(Context context,int w,int h,int pt,float oneHour,int abHeight,ArrayList<CalendarEntry>toPaint) {
        super(context);
        this.oneHour=oneHour;
        this.toPaint=toPaint;
        this.height=h;
        this.widht=w;
        this.abHeight = abHeight;
        this.paddingTop = pt;
    }

    @Override
    public void onDraw(Canvas canvas) {

    	
        //halbe studne
/*        for (int i = 3; i < 12; i++) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(102, 138+(34*i)+(1*i), 699, 172+(34*i)+(1*i), paint );
            paint.setColor(Color.RED);
            if(i==10){
                paint.setColor(Color.GREEN);
            }

            canvas.drawRect(104, 138+(34*i)+(1*i)+2, 697,172+(34*i)+(1*i)-2, paint );
		}*/
        
        paint.setColor(Color.BLACK);
        canvas.drawRect(PADDING_LEFT, 5*oneHour,widht-PADDING_LEFT ,6*oneHour , paint );
        paint.setColor(Color.GREEN);
        canvas.drawRect(PADDING_LEFT, 8*oneHour,widht-PADDING_LEFT ,9*oneHour , paint );
        
        paint.setColor(Color.GREEN);
        canvas.drawRect(PADDING_LEFT, 23*oneHour,widht-PADDING_LEFT ,24*oneHour , paint );
        
        paint.setColor(Color.BLUE);
        canvas.drawRect(PADDING_LEFT, 0*oneHour,widht-PADDING_LEFT ,2*oneHour , paint );
        
    	drawBackground(canvas);
    }

	private void drawBackground(Canvas canvas) {
		paint.setColor(Color.BLACK);
        canvas.drawRect(PADDING_LEFT-20, 0+paddingTop,PADDING_LEFT -18,(oneHour*24)+paddingTop, paint );
        canvas.drawRect(widht-PADDING_LEFT+18, 0+paddingTop,widht-PADDING_LEFT+20,(oneHour*24)+paddingTop, paint );
        for (int i = 0; i <= 24; i++) {
        	canvas.drawRect(PADDING_LEFT-20, 0+i*oneHour +paddingTop,widht-PADDING_LEFT+20,2+i*oneHour+paddingTop, paint );
		}
        
	}

}