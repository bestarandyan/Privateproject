package com.chinaLife.claimAssistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class sc_DrawTextStudy  extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    public sc_DrawTextStudy(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        new Thread(new MyThread()).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        
    }
    
    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y); 
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y); 
        }
    }
    
    class MyThread implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Canvas canvas = null;    
            try{                        
                canvas = holder.lockCanvas();
                Paint paint = new Paint();                
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);                
                canvas.drawLine(100, 100, 100, 400, paint);
                canvas.save();
                canvas.rotate(45); 
                paint.setColor(Color.BLUE);
                canvas.drawLine(100, 100, 400, 100, paint);
                canvas.rotate(45); 
            } catch(Exception e){
                
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }        
            
        }        
    }

}
