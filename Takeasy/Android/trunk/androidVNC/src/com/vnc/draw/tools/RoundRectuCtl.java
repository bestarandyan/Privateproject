package com.vnc.draw.tools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;
/*function:
 * @author:
 * Date:
 */
public class RoundRectuCtl implements ISketchPadTool{
  
    private Paint mPaint=new Paint();
    private boolean m_hasDrawn = false;
    private float left=0.0f,top=0.0f,right = 0.0f,bottom=0.0f;
    private float startx = 0;  
    private float starty = 0;  
    private float endx = 0;  
    private float endy = 0;  
    private float id = 0.0f;
//    SendToServiceToDraw sendService = null;
    public RoundRectuCtl(int penSize, int penColor)
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
			RectF r = new RectF(left, top, right, bottom);
    		canvas.drawRoundRect(r, 15, 15, mPaint);
            Log.i("sada022", "Rect实例");
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
		id = x;
		int[] dataInt0 = {1144,1,42,0,241,(int) id,15,15,(int) startx,(int) starty,(int) endx,(int) endy};
		MyApplication.getInstance().getSendToService().sDraw(dataInt0);
	}

	
	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		endx=x;
		endy=y;
		m_hasDrawn=true;//表示已经操作了
		drawRoundRect(x,y);
		int[] dataInt0 = {1145,1,42,0,241,(int) id,15,15,(int) startx,(int) starty,(int) endx,(int) endy};
		MyApplication.getInstance().getSendToService().sDraw(dataInt0);
	}

	
	public void touchUp(float x, float y) {
		endx=x;
		endy=y;
		drawRoundRect(x,y);
		MyApplication.getInstance().getSendToService().sendToReal();
	}
	private void drawRoundRect(float x,float y){//画圆角矩形
	   	 m_hasDrawn = true;
	   	if(x>startx){
	   		left = startx;
	   		right = x;
	   	}else{
	   		left = x;
	   		right = startx;
	   	}
	   	if(y>starty){
	   		top = starty;
	   		bottom = y;
	   	}else{
	   		top = y;
	   		bottom = starty;
	   	}     
	   	
   }
}
