package com.vnc.draw.tools;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;

public class CircleCtl  implements ISketchPadTool{
	private Paint mPaint=new Paint();
    private boolean m_hasDrawn = false; 
    private float startx = 0;  
    private float starty = 0;  
    private float endx = 0;  
    private float endy = 0; 
    private float radius=0;  
    private int id = 0;
    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;
//    public SendToServiceToDraw sendService = null;
    public CircleCtl(int penSize, int penColor)
    {
    	mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(penColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(penSize);//设置画笔粗细
//        sendService = new SendToServiceToDraw();
    }	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (null != canvas)
        {
		/*	PorterDuffXfermode   avoid = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
			mPaint.setXfermode(avoid);*/
			//canvas.drawColor(Color.RED, PorterDuff.Mode.SRC_OUT);
//			canvas.drawCircle((startx+endx)/2, (starty+endy)/2, radius, mPaint);
			canvas.drawCircle(startx+(endx-startx)/2, starty+(endy-starty)/2, radius, mPaint);
            Log.i("sada022", "Circle实例！");
        }
	}	
	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return m_hasDrawn;
		//return false;
	}
	public void cleanAll() {
		// TODO Auto-generated method stub		
	}
	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		startx=x;
		starty=y;
		endx=x;
		endy=y;
		radius=0;
		id = (int) x;
		int[] dataInt0 = {1154,1,34,0,241,id,(int) startx,(int) starty,(int) endx,(int) endy};
		
		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
	}	
	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		endx=x;
		endy=y;
		radius=(float) ((Math.sqrt((x-startx)*(x-startx)+(y-starty)*(y-starty)))/2);
		m_hasDrawn=true;//表示已经操作了
		left = (int) (startx+(endx-startx)/2-radius);
		top = (int) (starty+(endy-starty)/2-radius);
		right = (int) (startx+(endx-startx)/2+radius);
		bottom = (int) (starty+(endy-starty)/2+radius);
		int[] dataInt0 = {1155,1,34,0,241,id,left ,top,right,bottom};
		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
	}	
	public void touchUp(float x, float y) {
		endx=x;
		endy=y;
		left = (int) (startx+(endx-startx)/2-radius);
		top = (int) (starty+(endy-starty)/2-radius);
		right = (int) (startx+(endx-startx)/2+radius);
		bottom = (int) (starty+(endy-starty)/2+radius);
		MyApplication.getInstance().getSendToService().sendToReal();
	}
}
