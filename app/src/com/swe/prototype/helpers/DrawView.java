package com.swe.prototype.helpers;

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
    final int PADDING_LEFT=150;
    final int border=2;
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

    	drawBackground(canvas);
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
        canvas.drawRect(PADDING_LEFT, 5*oneHour+paddingTop,widht-PADDING_LEFT ,(int)(6.5*oneHour +paddingTop), paint );
        paint.setColor(Color.GREEN);
        canvas.drawRect(PADDING_LEFT+border, 5*oneHour+paddingTop+border,widht-PADDING_LEFT -border,(int)(6.5*oneHour +paddingTop)-border, paint );
        
        paint.setColor(Color.BLACK);
        canvas.drawRect(PADDING_LEFT, 0*oneHour+paddingTop,widht-PADDING_LEFT ,(int)(3*oneHour +paddingTop), paint );
        paint.setColor(Color.BLUE);
        canvas.drawRect(PADDING_LEFT+border, 0*oneHour+paddingTop+border,widht-PADDING_LEFT -border,(int)(3*oneHour +paddingTop)-border, paint );
        
        paint.setColor(Color.BLACK);
        canvas.drawRect(PADDING_LEFT, 22*oneHour+paddingTop,widht-PADDING_LEFT ,(int)(24*oneHour +paddingTop), paint );
        paint.setColor(Color.RED);
        canvas.drawRect(PADDING_LEFT+border, 22*oneHour+paddingTop+border,widht-PADDING_LEFT -border,(int)(24*oneHour +paddingTop)-border, paint );
        
        
    }

	private void drawBackground(Canvas canvas) {
		paint.setColor(Color.GRAY);
        canvas.drawRect(PADDING_LEFT-20, 0+paddingTop,PADDING_LEFT -18,(oneHour*24)+paddingTop, paint );
        canvas.drawRect(widht-PADDING_LEFT+18, 0+paddingTop,widht-PADDING_LEFT+20,(oneHour*24)+paddingTop, paint );
        for (int i = 0; i <= 24; i++) {
        	canvas.drawRect(PADDING_LEFT-20, 0+i*oneHour +paddingTop,widht-PADDING_LEFT+20,2+i*oneHour+paddingTop, paint );
		}
        
	}

}