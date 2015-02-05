package com.vnc.draw.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;


public class SketchPadEraser implements ISketchPadTool
{
    private static final float TOUCH_TOLERANCE = 0.0f;

    private float m_curX = 0.0f;
    private float m_curY = 0.0f;
    private boolean m_hasDrawn = false;
    private Path m_eraserPath = new Path();
    private Paint m_eraserPaint = new Paint();
    public ArrayList<HashMap<String,String>> pointList = new ArrayList<HashMap<String, String>>();
//    public SendToServiceToDraw sendService;
    public int size = 2;
    public SketchPadEraser(int eraserSize)
    {
        m_eraserPaint.setAntiAlias(true);
        m_eraserPaint.setDither(true);
        m_eraserPaint.setColor(0xFF000000);
        m_eraserPaint.setStrokeWidth(eraserSize);
        m_eraserPaint.setStyle(Paint.Style.STROKE);
        m_eraserPaint.setStrokeJoin(Paint.Join.ROUND);
        m_eraserPaint.setStrokeCap(Paint.Cap.SQUARE);
        m_eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        sendService = new SendToServiceToDraw();
        size = eraserSize;
    }

    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
            canvas.drawPath(m_eraserPath, m_eraserPaint);
        }
    }

    public boolean hasDraw()
    {
        return m_hasDrawn;
    }

    public void cleanAll()
    {
        m_eraserPath.reset();
    }

    public void touchDown(float x, float y)
    {
    	MyApplication.getInstance().setLineId(SendToServiceToDraw.getId());//为各种用画曲线的方式画图的图形设置ID
    	addFirstList(x,y);
        m_eraserPath.reset();
        m_eraserPath.moveTo(x, y);
        m_curX = x;
        m_curY = y;
        commSendToService(1164);
    }

    public void touchMove(float x, float y)
    {
        float dx = Math.abs(x - m_curX);
        float dy = Math.abs(y - m_curY);
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        pointList.add(map);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_eraserPath.quadTo(m_curX, m_curY, (x + m_curX) / 2, (y + m_curY) / 2);
            
            m_hasDrawn = true;
            m_curX = x;
            m_curY = y;
        }
        commSendToService(1165);
    }

    public void touchUp(float x, float y)
    {
        m_eraserPath.lineTo(x, y);
        MyApplication.getInstance().getSendToService().sendToReal();//结束命令
    }
    public void addFirstList(float x,float y){//为list添加第一点  当刚触摸的一瞬间
    	pointList.clear();
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        pointList.add(map);
    }
    private void commSendToService(int cmd){//向服务器发送画曲线的命令，这里的曲线包括多边形
    	int[] dataInt1 = new int[pointList.size()*2+8];
		dataInt1[0] = cmd;
		dataInt1[1] = 4;
		dataInt1[2] = 26+(pointList.size()*2*4);
		dataInt1[3] = 0;
		dataInt1[4] = 241;
		dataInt1[5] = MyApplication.getInstance().getLineId();
		dataInt1[6] = size;
		dataInt1[7] = pointList.size();
		int add = 8;
		for(int i = 0 ;i<pointList.size();i++){
			HashMap<String,String> map = pointList.get(i);
			dataInt1[i+add] = (int)Float.parseFloat(map.get("x"));
			dataInt1[i+add+1] = (int)Float.parseFloat(map.get("y"));
			add++;
		}
		//int[] dataInt1 = {1126,4,46,0,241,(int) m_curX+1,3,(int) m_curX,(int) m_curY,(int) x,(int) y,100,100};
		MyApplication.getInstance().getSendToService().sDrawQuXian(dataInt1);
		/*ArrayList<HashMap<String,String>> arr = new ArrayList<HashMap<String,String>>();
		for(int a=0;a<pointList.size()-1;a++){
			HashMap<String, String> map = pointList.get(a);
			arr.add(map);
		}*/
		
    }
}
