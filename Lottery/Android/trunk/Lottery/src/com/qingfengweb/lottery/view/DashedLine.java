package com.qingfengweb.lottery.view;

import com.qingfengweb.lottery.data.MyApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class DashedLine extends View {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Rect mRect;
 
    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);         
       
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);       
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ADC0B8"));
        Path path = new Path();    
        path.moveTo(0, 0);
        path.lineTo(MyApplication.getInstance().getScreenW(),0);     
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1.5f);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}