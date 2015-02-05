package com.vnc.draw.tools;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;
/**function:绘制椭圆
 * @author:

 */
public class OvaluCtl implements ISketchPadTool{
	private Paint mPaint=new Paint();
    private boolean m_hasDrawn = false; 
    private RectF rectf=new RectF();  
    private int id = 0;
//    private SendToServiceToDraw sendService = null;
    public OvaluCtl(int penSize, int penColor)
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
			canvas.drawOval(rectf, mPaint); 
			//canvas.drawText("adfadf", rectf.left, rectf.top, mPaint);
            Log.i("sada022", "Oval实例！");
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
		rectf.left=x;
		rectf.top=y;
		rectf.right=x;
		rectf.bottom=y;
		id = (int) x;
		 int[] dataInt0 = {1154,1,34,0,241,id,(int) rectf.left,(int) rectf.top,(int) rectf.right,(int) rectf.bottom};
		 MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
	}
	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		rectf.right=x;
		rectf.bottom=y;
		m_hasDrawn=true;//表示已经操作了
		int[] dataInt0 = {1155,1,34,0,241,id,(int) rectf.left,(int) rectf.top,(int) rectf.right,(int) rectf.bottom};
		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
	}	
	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		rectf.right=x;
		rectf.bottom=y;
		MyApplication.getInstance().getSendToService().sendToReal();
	}

}
