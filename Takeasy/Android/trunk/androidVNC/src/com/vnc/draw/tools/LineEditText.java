package com.vnc.draw.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class LineEditText  extends EditText {   
  
    private Paint mPaint;   
    /**  
     * @param context  
     * @param attrs  
     */  
    public LineEditText(Context context) {   
        super(context);   
        // TODO Auto-generated constructor stub   
        mPaint = new Paint();   
           
        mPaint.setStyle(Paint.Style.STROKE);   
        mPaint.setColor(Color.BLUE);   
    }   
       
    @Override  
    public void onDraw(Canvas canvas)   
    {   
        super.onDraw(canvas);   
           
//      »­µ×Ïß   
        canvas.drawLine(0,this.getHeight()-1,  this.getWidth()-1, this.getHeight()-1, mPaint);   
    }   
}  
