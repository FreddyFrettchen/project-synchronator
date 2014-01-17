package com.swe.prototype.globalsettings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    int height = 991-136;
    int width = 701 -100;
    int pTop = 136;
    int pLeft=100;
    public DrawView(Context context) {
        super(context);            
    }

    @Override
    public void onDraw(Canvas canvas) {

    	drawBackground();
    	
        //kochen mit gertrude
        paint.setColor(Color.BLACK);
        canvas.drawRect(pLeft+2, pTop+2, pLeft+width-2, pTop+(height/(24*2)) , paint );
        
        paint.setColor(Color.YELLOW);
        canvas.drawRect(pLeft+4, pTop+4,  pLeft+width-4, pTop+(height/(24*2)) -2, paint );

        //halbe studne
        for (int i = 3; i < 12; i++) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(102, 138+(34*i)+(1*i), 699, 172+(34*i)+(1*i), paint );
            paint.setColor(Color.RED);
            if(i==10){
                paint.setColor(Color.GREEN);
            }

            canvas.drawRect(104, 138+(34*i)+(1*i)+2, 697,172+(34*i)+(1*i)-2, paint );
		}
        paint.setColor(Color.GREEN);
        int yy= 991;
        canvas.drawRect(102, yy, 699, yy+34,paint );
        
      
        
    }

	private void drawBackground() {
		
		
	}

}