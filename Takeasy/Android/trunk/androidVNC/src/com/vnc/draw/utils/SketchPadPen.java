package com.vnc.draw.utils;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.GradientDrawable;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;

public class SketchPadPen implements ISketchPadTool
{
    private static final float TOUCH_TOLERANCE = 4.0f;
    private int shape_flag = 1;
    private float m_curX = 0.0f;
    private float m_curY = 0.0f;
    private float m_minX = 0.0f;
    private float m_maxX = 0.0f;
    private float m_minY = 0.0f;
    private float m_maxY = 0.0f;
    private float m_minXy = 0.0f;
    private float m_maxXy = 0.0f;
    private float m_minYx = 0.0f;
    private float m_maxYx = 0.0f;
    private float secondX = 0.0f;
    private float secondY = 0.0f;
    private boolean m_hasDrawn = false;
    private Path m_penPath = new Path();
    private Paint m_penPaint = new Paint();
    private float PenWidth = 2.0f;
    private int PenColor = Color.BLACK;
    private int a = 10;
    private float dx;
    private float dy;
    private float left=0.0f,top=0.0f,right = 0.0f,bottom=0.0f;
    private float radiusx = 0.0f,radiusy = 0.0f;
    private boolean moveBoo = false;
//    public SendToServiceToDraw sendService;
    public boolean point = false;
    private int id = 0;
    public ArrayList<HashMap<String,String>> pointList = new ArrayList<HashMap<String, String>>();
    public SketchPadPen(int penSize, int penColor)
    {
        m_penPaint.setAntiAlias(true);
        m_penPaint.setDither(true);
        m_penPaint.setColor(penColor);
        m_penPaint.setStrokeWidth(penSize);
        m_penPaint.setStyle(Paint.Style.STROKE);
        m_penPaint.setStrokeCap(Paint.Cap.ROUND);
        PenWidth =  penSize;
        PenColor = penColor;
//        sendService = new SendToServiceToDraw();
    }
    
    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
        	/*if(point){
        		canvas.drawPoint(m_curX, m_curY, m_penPaint);
        	}*/
                canvas.drawPath(m_penPath, m_penPaint);
        }
    }
    
    public boolean hasDraw()
    {
        return m_hasDrawn;
    }

    public void cleanAll()
    {
        m_penPath.reset();
    }

    public void touchDown(float x, float y)
    {
    	MyApplication.getInstance().setLineId(SendToServiceToDraw.getId());//为各种用画曲线的方式画图的图形设置ID
    	shape_flag = MyApplication.getInstance().getTypeShape();
        m_penPath.reset();
        m_penPath.moveTo(x, y);
        m_curX = x;
        m_curY = y;
        id = (int) MyApplication.getInstance().getLineId();
        addFirstList(x,y);
        moveBoo = false;
        if(shape_flag == 0){
        	int[] dataInt = {1124,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt);
        }else if(shape_flag == 1){
            drawQuLine(x,y,1126);
        }else if(shape_flag == 2){
    		int[] dataInt0 = {1134,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
    	}else if(shape_flag == 6){
    		int[] dataInt0 = {1134,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
    	}else if(shape_flag ==7){
            drawFiveShape(x,y,1128);
    	}else if(shape_flag == 8){
            drawDiamond(x,y,1128);
    	}else if(shape_flag == 9){
    		drawTrigon(x,y,1128);
    	}else if(shape_flag == 10){
    		drawRightArrows(x,y,1128);
    	}else if(shape_flag == 11){
    		drawLeftArrows(x,y,1128);
    	}else if(shape_flag == 12){
    		drawTopArrows(x,y,1128);
    	}else if(shape_flag == 13){
    		drawBottomArrows(x,y,1128);
    	}else if(shape_flag == 14){
    		drawAngleTrigon(x,y,1128);
    	}else if(shape_flag == 15){
    		drawSixShape(x,y,1128);
    	}else if(shape_flag == 16){
    		draw16Shape(x,y,1128);
    	}else if(shape_flag == 17){
    		draw17Shape(x,y,1128);
    	}else if(shape_flag == 18){
    		draw18Shape(x,y,1128);
    	}else if(shape_flag == 19){
    		draw19Shape(x,y,1128);
    	}
    }
    public void addFirstList(float x,float y){//为list添加第一点  当刚触摸的一瞬间
    	pointList.clear();
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("x", String.valueOf((int)x));
        map.put("y", String.valueOf((int)y));
        pointList.add(map);
    }
    public void touchMove(float x, float y)
    {	
    	if(m_minX == 0){
    		m_minX = x;
    	}
    	if(m_maxX == 0){
    		m_maxX = x;
    	}
    	if(m_minY == 0){
    		m_minY = y;
    	}
    	if(m_maxY == 0){
    		m_maxY = y;
    	}
    	if(m_minX>x){
    		m_minX = x;
    		m_minXy = y;
    	}
    	if(m_maxX < x){
    		m_maxX = x;
    		m_maxXy = y;
    	}
    	if(m_minY>y){
    		m_minY = y;
    		m_minYx = x;
    	}
    	if(m_maxY < y){
    		m_maxY = y;
    		m_maxYx = x;
    	}
    	shape_flag = MyApplication.getInstance().getTypeShape();
    
    	if(shape_flag == 0){
    		String ux = pointList.get(pointList.size()-1).get("x");
    		String uy = pointList.get(pointList.size()-1).get("y");
    		if(!ux.equals(String.valueOf(x)) && !uy.equals(String.valueOf(y))){
    			moveBoo = true;
    		drawZhiLine(x,y);
    		int[] dataInt = {1125,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt);
    		}
    	}else if(shape_flag == 1){
    		String ux = pointList.get(pointList.size()-1).get("x");
    		String uy = pointList.get(pointList.size()-1).get("y");
    		if(!ux.equals(String.valueOf((int)x)) && !uy.equals(String.valueOf((int)y))){
    			moveBoo = true;
    			HashMap<String, String> map = new HashMap<String,String>();
                map.put("x", String.valueOf((int)x));
                map.put("y", String.valueOf((int)y));
                pointList.add(map);
        		drawQuLine(x,y,1127);
    		}
    	}else if(shape_flag == 2){
    		String ux = pointList.get(pointList.size()-1).get("x");
    		String uy = pointList.get(pointList.size()-1).get("y");
    		if(!ux.equals(String.valueOf(x)) && !uy.equals(String.valueOf(y))){
    			moveBoo = true;
    		drawRect(x,y);
    		int[] dataInt0 = {1135,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
    		}
    	}else if(shape_flag == 6){
    		String ux = pointList.get(pointList.size()-1).get("x");
    		String uy = pointList.get(pointList.size()-1).get("y");
    		if(!ux.equals(String.valueOf(x)) && !uy.equals(String.valueOf(y))){
    			moveBoo = true;
    		drawSquare(x, y);
    		int[] dataInt0 = {1135,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) squareRight,(int) squareBottom};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	
    		}
    	}else if(shape_flag == 7){
    		drawFiveShape(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 8){
    		drawDiamond(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 9){
    		drawTrigon(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 10){
    		drawRightArrows(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 11){
    		drawLeftArrows(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 12){
    		drawTopArrows(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 13){
    		drawBottomArrows(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 14){
    		drawAngleTrigon(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 15){
    		drawSixShape(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 16){
    		draw16Shape(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 17){
    		draw17Shape(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 18){
    		draw18Shape(x,y,1129);
    		moveBoo = true;
    	}else if(shape_flag == 19){
    		draw19Shape(x,y,1129);
    		moveBoo = true;
    	}else{
    		String ux = pointList.get(pointList.size()-1).get("x");
    		String uy = pointList.get(pointList.size()-1).get("y");
    		if(!ux.equals(String.valueOf(x)) && !uy.equals(String.valueOf(y))){
    			moveBoo = true;
    		drawQuLine(x,y,1127);
    		}
    	}
		
    }

    public void touchUp(float x, float y)
    {
    	shape_flag = MyApplication.getInstance().getTypeShape();
    	
    	if(shape_flag == 9){
//    		drawTrigon(x, y);
    	}else if(shape_flag == 0){
    		m_penPaint.setStrokeWidth(PenWidth);
            m_penPaint.setColor(PenColor);
            m_penPaint.setPathEffect(null);
    		m_penPath.lineTo(x, y);
    		/*int[] dataInt0 = {1124,1,34,0,241,id,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);*/
//    		MyApplication.getInstance().getSendToService().sendToReal();
    	}else if(shape_flag == 1){
//    		drawQuLine(x,y,1127,(int) m_curX);
//    		MyApplication.getInstance().getSendToService().sendToReal();
    	}else if(shape_flag == 2){
    		/*drawRect(x,y);
    		int[] dataInt0 = {1134,1,34,0,241,(int) m_curX,(int) m_curX,(int) m_curY,(int) x,(int) y};
    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);	*/
    	}else if(shape_flag == 3){
//    		drawRoundRect(x,y);
    	}else if(shape_flag == 6){
//    		drawSquare(x, y);
//    		int[] dataInt0 = {1134,1,34,0,241,(int) m_curX,(int) m_curX,(int) m_curY,(int) squareRight,(int) squareBottom};
//    		MyApplication.getInstance().getSendToService().sDraw(dataInt0);
    	}else if(shape_flag == 7){
    		//drawFiveShape(x,y);
    	}else if(shape_flag == 8){
//    		drawDiamond(x,y);
    	}else if(shape_flag == 10){
//    		drawRightArrows(x,y);
    	}else if(shape_flag == 11){
//    		drawLeftArrows(x,y);
    	}else if(shape_flag == 12){
//    		drawTopArrows(x,y);
    	}else if(shape_flag == 13){
//    		drawBottomArrows(x,y);
    	}else if(shape_flag == 14){
//    		drawAngleTrigon(x,y,);
    	}else if(shape_flag == 15){
//    		drawSixShape(x,y);
    	}else if(shape_flag == 16){
//    		draw16Shape(x,y);
    	}else if(shape_flag == 17){
//    		draw17Shape(x,y);
    	}else if(shape_flag == 18){
//    		draw18Shape(x,y);
    	}else if(shape_flag == 19){
//    		draw19Shape(x,y);
    	}
    	if(moveBoo){
    		MyApplication.getInstance().getSendToService().sendToReal();
    	}
    	
    }
    
    
    
    
    public void draw19Shape(float x, float y,int cmd){//画8边形
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        float w = dx/3;
        float h = dy/3;
        float[][] dot = null;
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX+w, m_curY);
            m_penPath.lineTo(m_curX, m_curY+h);
            m_penPath.lineTo(m_curX, y-h);
            m_penPath.lineTo(m_curX+w, y);
            m_penPath.lineTo(x-w, y);
            m_penPath.lineTo(x, y-h);
            m_penPath.lineTo(x, y-(h*2));
            m_penPath.lineTo(x-w, m_curY);
            m_penPath.lineTo(m_curX+w, m_curY);
            dot = new float[][]{{m_curX+w, m_curY},{m_curX, m_curY+h},{m_curX, y-h},{m_curX+w, y},{x-w, y},
            		{x, y-h},{x, y-(h*2)},{x-w, m_curY},{m_curX+w, m_curY}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX+w, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX, y+h);
           m_penPath.lineTo(m_curX+w, y);
           m_penPath.lineTo(x-w, y);
           m_penPath.lineTo(x, y+h);
           m_penPath.lineTo(x, y+(h*2));
           m_penPath.lineTo(x-w, m_curY);
           m_penPath.lineTo(m_curX+w, m_curY);
           dot = new float[][]{{m_curX+w, m_curY},{m_curX, m_curY-h},{m_curX, y+h},{m_curX+w, y},{x-w, y},
           		{x, y+h},{x, y+(h*2)},{x-w, m_curY},{m_curX+w, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX-w, m_curY);
           m_penPath.lineTo(m_curX, m_curY+h);
           m_penPath.lineTo(m_curX, y-h);
           m_penPath.lineTo(m_curX-w, y);
           m_penPath.lineTo(x+w, y);
           m_penPath.lineTo(x, y-h);
           m_penPath.lineTo(x, y-(h*2));
           m_penPath.lineTo(x+w, m_curY);
           m_penPath.lineTo(m_curX-w, m_curY);
           dot = new float[][]{{m_curX-w, m_curY},{m_curX, m_curY+h},{m_curX, y-h},{m_curX-w, y},{x+w, y},
           		{x, y-h},{x, y-(h*2)},{x+w, m_curY},{m_curX-w, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX-w, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX, y+h);
           m_penPath.lineTo(m_curX-w, y);
           m_penPath.lineTo(x+w, y);
           m_penPath.lineTo(x, y+h);
           m_penPath.lineTo(x, y+(h*2));
           m_penPath.lineTo(x+w, m_curY);
           m_penPath.lineTo(m_curX-w, m_curY);
           dot = new float[][]{{m_curX-w, m_curY},{m_curX, m_curY-h},{m_curX, y+h},{m_curX-w, y},{x+w, y},
           		{x, y+h},{x, y+(h*2)},{x+w, m_curY},{m_curX-w, m_curY}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void draw18Shape(float x, float y,int cmd){//画两个正方形重叠的图行
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        if(dx>dy){
        	dy = dx;
        }else{
        	dx = dy;
        }
        float[][] dot = null;
        m_penPath.reset();
        float bc =(dx/3.414f);
        float h = bc*0.707f;
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, m_curY);
            m_penPath.lineTo(m_curX, m_curY+bc);
            m_penPath.lineTo(m_curX-h, m_curY+bc+h);
            m_penPath.lineTo(m_curX, m_curY+bc+h+h);
            m_penPath.lineTo(m_curX, m_curY+bc+h+h+bc);
            m_penPath.lineTo(m_curX+bc, m_curY+bc+h+h+bc);
            m_penPath.lineTo(m_curX+h+bc, m_curY+bc+h+h+bc+h);
            m_penPath.lineTo(m_curX+h+bc+h, m_curY+bc+h+h+bc);
            m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY+bc+h+h+bc);
            m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY+bc+h+h);
            m_penPath.lineTo(m_curX+h+bc+h+bc+h, m_curY+bc+h);
            m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY+bc);
            m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY);
            m_penPath.lineTo(m_curX+h+bc+h, m_curY);
            m_penPath.lineTo(m_curX+h+bc, m_curY-h);
            m_penPath.lineTo(m_curX+bc, m_curY);
            m_penPath.lineTo(m_curX, m_curY);
            dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY+bc},{m_curX-h, m_curY+bc+h},{m_curX, m_curY+bc+h+h},{m_curX, m_curY+bc+h+h+bc},
            		{m_curX+bc, m_curY+bc+h+h+bc},{m_curX+h+bc, m_curY+bc+h+h+bc+h},{m_curX+h+bc+h, m_curY+bc+h+h+bc},{m_curX+h+bc+h+bc, m_curY+bc+h+h+bc}
            		,{m_curX+h+bc+h+bc, m_curY+bc+h+h},{m_curX+h+bc+h+bc+h, m_curY+bc+h},{m_curX+h+bc+h+bc, m_curY+bc},{m_curX+h+bc+h+bc, m_curY},
            		{m_curX+h+bc+h, m_curY},{m_curX+h+bc, m_curY-h},{m_curX+bc, m_curY},{m_curX, m_curY}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(m_curX, m_curY-bc);
           m_penPath.lineTo(m_curX-h, m_curY-bc-h);
           m_penPath.lineTo(m_curX, m_curY-bc-h-h);
           m_penPath.lineTo(m_curX, m_curY-bc-h-h-bc);
           
           m_penPath.lineTo(m_curX+bc, m_curY-bc-h-h-bc);
           m_penPath.lineTo(m_curX+h+bc, m_curY-bc-h-h-bc-h);
           m_penPath.lineTo(m_curX+h+bc+h, m_curY-bc-h-h-bc);
           m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY-bc-h-h-bc);
           
           m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY-bc-h-h);
           m_penPath.lineTo(m_curX+h+bc+h+bc+h, m_curY-bc-h);
           m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY-bc);
           m_penPath.lineTo(m_curX+h+bc+h+bc, m_curY);
           
           m_penPath.lineTo(m_curX+h+bc+h, m_curY);
           m_penPath.lineTo(m_curX+h+bc, m_curY+h);
           m_penPath.lineTo(m_curX+bc, m_curY);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY-bc},{m_curX-h, m_curY-bc-h},{m_curX, m_curY-bc-h-h},{m_curX, m_curY-bc-h-h-bc},
           		{m_curX+bc, m_curY-bc-h-h-bc},{m_curX+h+bc, m_curY-bc-h-h-bc-h},{m_curX+h+bc+h, m_curY-bc-h-h-bc},{m_curX+h+bc+h+bc, m_curY-bc-h-h-bc}
           		,{m_curX+h+bc+h+bc, m_curY-bc-h-h},{m_curX+h+bc+h+bc+h, m_curY-bc-h},{m_curX+h+bc+h+bc, m_curY-bc},{m_curX+h+bc+h+bc, m_curY},
           		{m_curX+h+bc+h, m_curY},{m_curX+h+bc, m_curY+h},{m_curX+bc, m_curY},{m_curX, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(m_curX, m_curY+bc);
           m_penPath.lineTo(m_curX+h, m_curY+bc+h);
           m_penPath.lineTo(m_curX, m_curY+bc+h+h);
           m_penPath.lineTo(m_curX, m_curY+bc+h+h+bc);
           
           m_penPath.lineTo(m_curX-bc, m_curY+bc+h+h+bc);
           m_penPath.lineTo(m_curX-h-bc, m_curY+bc+h+h+bc+h);
           m_penPath.lineTo(m_curX-h-bc-h, m_curY+bc+h+h+bc);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY+bc+h+h+bc);
           
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY+bc+h+h);
           m_penPath.lineTo(m_curX-h-bc-h-bc-h, m_curY+bc+h);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY+bc);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY);
           
           m_penPath.lineTo(m_curX-h-bc-h, m_curY);
           m_penPath.lineTo(m_curX-h-bc, m_curY-h);
           m_penPath.lineTo(m_curX-bc, m_curY);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY+bc},{m_curX+h, m_curY+bc+h},{m_curX, m_curY+bc+h+h},{m_curX, m_curY+bc+h+h+bc},
              		{m_curX-bc, m_curY+bc+h+h+bc},{m_curX-h-bc, m_curY+bc+h+h+bc+h},{m_curX-h-bc-h, m_curY+bc+h+h+bc},{m_curX+h-bc-h-bc, m_curY+bc+h+h+bc}
              		,{m_curX-h-bc-h-bc, m_curY+bc+h+h},{m_curX-h-bc-h-bc-h, m_curY+bc+h},{m_curX-h-bc-h-bc, m_curY+bc},{m_curX+h-bc-h-bc, m_curY},
              		{m_curX-h-bc-h, m_curY},{m_curX-h-bc, m_curY-h},{m_curX-bc, m_curY},{m_curX, m_curY}};
              drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(m_curX, m_curY-bc);
           m_penPath.lineTo(m_curX+h, m_curY-bc-h);
           m_penPath.lineTo(m_curX, m_curY-bc-h-h);
           m_penPath.lineTo(m_curX, m_curY-bc-h-h-bc);
           
           m_penPath.lineTo(m_curX-bc, m_curY-bc-h-h-bc);
           m_penPath.lineTo(m_curX-h-bc, m_curY-bc-h-h-bc-h);
           m_penPath.lineTo(m_curX-h-bc-h, m_curY-bc-h-h-bc);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY-bc-h-h-bc);
           
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY-bc-h-h);
           m_penPath.lineTo(m_curX-h-bc-h-bc-h, m_curY-bc-h);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY-bc);
           m_penPath.lineTo(m_curX-h-bc-h-bc, m_curY);
           
           m_penPath.lineTo(m_curX-h-bc-h, m_curY);
           m_penPath.lineTo(m_curX-h-bc, m_curY+h);
           m_penPath.lineTo(m_curX-bc, m_curY);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY-bc},{m_curX+h, m_curY-bc-h},{m_curX, m_curY-bc-h-h},{m_curX, m_curY-bc-h-h-bc},
           		{m_curX-bc, m_curY-bc-h-h-bc},{m_curX-h-bc, m_curY-bc-h-h-bc-h},{m_curX-h-bc-h, m_curY-bc-h-h-bc},{m_curX-h-bc-h-bc, m_curY-bc-h-h-bc}
           		,{m_curX-h-bc-h-bc, m_curY-bc-h-h},{m_curX-h-bc-h-bc-h, m_curY-bc-h},{m_curX-h-bc-h-bc, m_curY-bc},{m_curX-h-bc-h-bc, m_curY},
           		{m_curX-h-bc-h, m_curY},{m_curX-h-bc, m_curY+h},{m_curX-bc, m_curY},{m_curX, m_curY}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void draw17Shape(float x, float y,int cmd){//画一半矩形一半三角形的图行
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        float w = dx/2;
        float h = dy/2;
        float[][] dot = null;
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX+w, m_curY);
            m_penPath.lineTo(m_curX, m_curY+h);
            m_penPath.lineTo(m_curX, y);
            m_penPath.lineTo(x, y);
            m_penPath.lineTo(x, y-h);
            m_penPath.lineTo(m_curX+w, m_curY);
            dot = new float[][]{{m_curX+w, m_curY},{m_curX, m_curY+h},{m_curX, y},{x, y},{x, y-h},{m_curX+w, m_curY}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX+w, y);
           m_penPath.lineTo(x, y+h);
           m_penPath.lineTo(x, m_curY);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY-h},{m_curX+w, y},{x, y+h},{x, m_curY},{m_curX, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX-w, m_curY);
           m_penPath.lineTo(m_curX, m_curY+h);
           m_penPath.lineTo(m_curX, y);
           m_penPath.lineTo(x, y);
           m_penPath.lineTo(x, y-h);
           m_penPath.lineTo(m_curX-w, m_curY);
           dot = new float[][]{{m_curX-w, m_curY},{m_curX, m_curY+h},{m_curX, y},{x, y},{x, y-h},{m_curX-w, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX-w, y);
           m_penPath.lineTo(x, y+h);
           m_penPath.lineTo(x, m_curY);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{m_curX, m_curY-h},{m_curX-w, y},{x, y+h},{x, m_curY},{m_curX, m_curY}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void draw16Shape(float x, float y,int cmd){//画飞机形状
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        float w = dx/4;
        float h = dy/2;
        float[][] dot = null;
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, m_curY);
            m_penPath.lineTo(x, y-h);
            m_penPath.lineTo(m_curX, y);
            m_penPath.lineTo(m_curX+w, y-h);
            m_penPath.lineTo(m_curX, m_curY);
            dot = new float[][]{{m_curX, m_curY},{x, y-h},{m_curX, y},{m_curX+w, y-h},{m_curX, m_curY}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY);
           m_penPath.lineTo(x, y+h);
           m_penPath.lineTo(m_curX, y);
           m_penPath.lineTo(m_curX+w, y+h);
           m_penPath.lineTo(m_curX, m_curY);
           dot = new float[][]{{m_curX, m_curY},{x, y-h},{m_curX, y},{m_curX+w, y-h},{m_curX, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(x, y);
           m_penPath.lineTo(x+w, y-h);
           m_penPath.lineTo(x, m_curY);
           m_penPath.lineTo(m_curX, y-h);
           m_penPath.lineTo(x, y);
           dot = new float[][]{{m_curX, m_curY},{x, y-h},{m_curX, y},{m_curX+w, y-h},{m_curX, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(x, y);
           m_penPath.lineTo(x+w, y+h);
           m_penPath.lineTo(x, m_curY);
           m_penPath.lineTo(m_curX, y+h);
           m_penPath.lineTo(x, y);
           dot = new float[][]{{x, y},{x+w, y+h},{x, m_curY},{m_curX, y+h},{x, y}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawSixShape(float x, float y,int cmd){//画六边行
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        float w = dx/4;
        float h = dy/2;
        float[][] dot = null;
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, m_curY+h);
            m_penPath.lineTo(m_curX+w, y);
            m_penPath.lineTo(x-w, y);
            m_penPath.lineTo(x, m_curY+h);
            m_penPath.lineTo(x-w, m_curY);
            m_penPath.lineTo(m_curX+w, m_curY);
            m_penPath.lineTo(m_curX, m_curY+h);
             dot = new float[][]{{m_curX, m_curY+h},{m_curX+w, y},{x-w, y},{x, m_curY+h},{x-w, m_curY},
            		 {m_curX+w, m_curY},{m_curX, m_curY+h}};
             drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX+w, y);
           m_penPath.lineTo(x-w, y);
           m_penPath.lineTo(x, m_curY-h);
           m_penPath.lineTo(x-w, m_curY);
           m_penPath.lineTo(m_curX+w, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           dot = new float[][]{{m_curX, m_curY-h},{m_curX+w, y},{x-w, y},{x, m_curY-h},{x-w, m_curY},
          		 {m_curX+w, m_curY},{m_curX, m_curY-h}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX, m_curY+h);
           m_penPath.lineTo(m_curX-w, y);
           m_penPath.lineTo(x+w, y);
           m_penPath.lineTo(x, m_curY+h);
           m_penPath.lineTo(x+w, m_curY);
           m_penPath.lineTo(m_curX-w, m_curY);
           m_penPath.lineTo(m_curX, m_curY+h);
           dot = new float[][]{{m_curX, m_curY+h},{m_curX-w, y},{x+w, y},{x, m_curY+h},{x+w, m_curY},
          		 {m_curX-w, m_curY},{m_curX, m_curY+h}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-h);
           m_penPath.lineTo(m_curX-w, y);
           m_penPath.lineTo(x+w, y);
           m_penPath.lineTo(x, m_curY-h);
           m_penPath.lineTo(x+w, m_curY);
           m_penPath.lineTo(m_curX-w, m_curY);
           m_penPath.lineTo(m_curX, m_curY-h);
           dot = new float[][]{{m_curX, m_curY-h},{m_curX-w, y},{x+w, y},{x, m_curY-h},{x+w, m_curY},
          		 {m_curX-w, m_curY},{m_curX, m_curY-h}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawAngleTrigon(float x, float y,int cmd){//画直角三角形
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
	   	 	m_penPath.moveTo(m_curX, m_curY);
	       	m_penPath.lineTo(m_curX, y);
	       	m_penPath.lineTo(x, y);
	   		m_penPath.lineTo(m_curX, m_curY);
	   		float[][] dot = {{m_curX, m_curY},{m_curX, y},{x, y},{m_curX, m_curY}};
            drawShape(dot);
            commSendToService(cmd);
            pointList.clear();
    }
    public void drawBottomArrows(float x, float y,int cmd){//画向下的箭头
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        float a = dx/4;//横坐标移动的差值的1/4
        float b = dy/2;//正坐标移动的差值的1/2;
        m_penPath.reset();
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX+dx/2, y);
       	    m_penPath.lineTo(x,y-b); 
            m_penPath.lineTo(x-a, y-b);
            m_penPath.lineTo(x-a,m_curY);
            m_penPath.lineTo(m_curX+a, m_curY);
            m_penPath.lineTo(m_curX+a, m_curY+b);
            m_penPath.lineTo(m_curX, m_curY+b);
            m_penPath.lineTo(m_curX+dx/2, y);
            float[][] dot = {{m_curX+dx/2, y},{x,y-b},{x-a, y-b},{x-a,m_curY},{m_curX+a, m_curY},
            		{m_curX+a, m_curY+b},{m_curX, m_curY+b},{m_curX+dx/2, y}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX+dx/2, m_curY);
      	   m_penPath.lineTo(x,y+b); 
           m_penPath.lineTo(x-a, y+b);
           m_penPath.lineTo(x-a,y);
           m_penPath.lineTo(m_curX+a, y);
           m_penPath.lineTo(m_curX+a, m_curY-b);
           m_penPath.lineTo(m_curX, m_curY-b);
           m_penPath.lineTo(m_curX+dx/2, m_curY);
           float[][] dot = {{m_curX+dx/2, m_curY},{x,y+b},{x-a, y+b},{x-a,y},{m_curX+a, y},
           		{m_curX+a, m_curY-b},{m_curX, m_curY-b},{m_curX+dx/2, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX-dx/2, y);
      	   m_penPath.lineTo(x,y-b); 
           m_penPath.lineTo(x+a, y-b);
           m_penPath.lineTo(x+a,m_curY);
           m_penPath.lineTo(m_curX-a, m_curY);
           m_penPath.lineTo(m_curX-a, m_curY+b);
           m_penPath.lineTo(m_curX, m_curY+b);
           m_penPath.lineTo(m_curX-dx/2, y);
           float[][] dot = {{m_curX-dx/2, y},{x,y-b},{x+a, y-b},{x+a,m_curY},{m_curX-a, m_curY},
           		{m_curX-a, m_curY+b},{m_curX, m_curY+b},{m_curX-dx/2, y}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX-dx/2, m_curY);
      	   m_penPath.lineTo(x,y+b); 
           m_penPath.lineTo(x+a, y+b);
           m_penPath.lineTo(x+a,y);
           m_penPath.lineTo(m_curX-a, y);
           m_penPath.lineTo(m_curX-a, m_curY-b);
           m_penPath.lineTo(m_curX, m_curY-b);
           m_penPath.lineTo(m_curX-dx/2, m_curY);
           float[][] dot = {{m_curX-dx/2, m_curY},{x,y+b},{x+a, y+b},{x+a,y},{m_curX-a, y},
           		{m_curX-a, m_curY-b},{m_curX, m_curY-b},{m_curX-dx/2, m_curY}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawTopArrows(float x, float y,int cmd){//画向上的箭头
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        float a = dx/4;//横坐标移动的差值的1/4
        float b = dy/2;//正坐标移动的差值的1/2;
        m_penPath.reset();
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX+dx/2, m_curY);
       	    m_penPath.lineTo(x,y-b); 
            m_penPath.lineTo(x-a, y-b);
            m_penPath.lineTo(x-a,y);
            m_penPath.lineTo(m_curX+a, y);
            m_penPath.lineTo(m_curX+a, m_curY+b);
            m_penPath.lineTo(m_curX, m_curY+b);
            m_penPath.lineTo(m_curX+dx/2, m_curY);
            float[][] dot = {{m_curX+dx/2, m_curY},{x,y-b},{x-a, y-b},{x-a,y},{m_curX+a, y},
            		{m_curX+a, m_curY+b},{m_curX, m_curY+b},{m_curX+dx/2, m_curY}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX+dx/2, y);
      	   m_penPath.lineTo(x,y+b); 
           m_penPath.lineTo(x-a, y+b);
           m_penPath.lineTo(x-a,m_curY);
           m_penPath.lineTo(m_curX+a, m_curY);
           m_penPath.lineTo(m_curX+a, m_curY-b);
           m_penPath.lineTo(m_curX, m_curY-b);
           m_penPath.lineTo(m_curX+dx/2, y);
           float[][] dot = {{m_curX+dx/2, y},{x,y+b},{x-a, y+b},{x-a,m_curY},{m_curX+a, m_curY},
           		{m_curX+a, m_curY-b},{m_curX, m_curY-b},{m_curX+dx/2, y}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX-dx/2, m_curY);
      	   m_penPath.lineTo(x,y-b); 
           m_penPath.lineTo(x+a, y-b);
           m_penPath.lineTo(x+a,y);
           m_penPath.lineTo(m_curX-a, y);
           m_penPath.lineTo(m_curX-a, m_curY+b);
           m_penPath.lineTo(m_curX, m_curY+b);
           m_penPath.lineTo(m_curX-dx/2, m_curY);
           float[][] dot = {{m_curX-dx/2, m_curY},{x,y-b},{x+a, y-b},{x+a,y},{m_curX-a, y},
           		{m_curX-a, m_curY+b},{m_curX, m_curY+b},{m_curX-dx/2, m_curY}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX-dx/2, y);
      	   m_penPath.lineTo(x,y+b); 
           m_penPath.lineTo(x+a, y+b);
           m_penPath.lineTo(x+a,m_curY);
           m_penPath.lineTo(m_curX-a, m_curY);
           m_penPath.lineTo(m_curX-a, m_curY-b);
           m_penPath.lineTo(m_curX, m_curY-b);
           m_penPath.lineTo(m_curX-dx/2, y);
           float[][] dot = {{m_curX-dx/2, y},{x,y+b},{x+a, y+b},{x+a,m_curY},{m_curX-a, m_curY},
           		{m_curX-a, m_curY-b},{m_curX, m_curY-b},{m_curX-dx/2, y}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawLeftArrows(float x, float y,int cmd){//画向左的箭头
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        float a = dy/4;//正坐标移动的差值的1/4
        float b = dx/2;//横坐标移动的差值的1/2;
        m_penPath.reset();
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, m_curY+(a*2));
       	    m_penPath.lineTo(m_curX+b,y); 
            m_penPath.lineTo(m_curX+b, y-a);
            m_penPath.lineTo(x,y-a);
            m_penPath.lineTo(x, m_curY+a);
            m_penPath.lineTo(m_curX+b, m_curY+a);
            m_penPath.lineTo(m_curX+b, m_curY);
            m_penPath.lineTo(m_curX, m_curY+(a*2));
            float[][] dot = {{m_curX, m_curY+(a*2)},{m_curX+b,y},{m_curX+b, y-a},{x,y-a},{x, m_curY+a},
            		{m_curX+b, m_curY+a},{m_curX+b, m_curY},{m_curX, m_curY+(a*2)}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-(a*2));
      	   m_penPath.lineTo(m_curX+b,y);
           m_penPath.lineTo(m_curX+b, y+a);
           m_penPath.lineTo(x,y+a);
           m_penPath.lineTo(x, m_curY-a);
           m_penPath.lineTo(m_curX+b, m_curY-a);
           m_penPath.lineTo(m_curX+b, m_curY);
           m_penPath.lineTo(m_curX, m_curY-(a*2));
           float[][] dot = {{m_curX, m_curY-(a*2)},{m_curX+b,y},{m_curX+b, y+a},{x,y+a},{x, m_curY-a},
           		{m_curX+b, m_curY-a},{m_curX+b, m_curY},{m_curX, m_curY-(a*2)}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX, m_curY+a);
      	   m_penPath.lineTo(m_curX-b,m_curY+a);
           m_penPath.lineTo(m_curX-b, m_curY);
           m_penPath.lineTo(x,y-(a*2));
           m_penPath.lineTo(m_curX-b, y);
           m_penPath.lineTo(m_curX-b, y-a);
           m_penPath.lineTo(m_curX, y-a);
           m_penPath.lineTo(m_curX, m_curY+a);
           float[][] dot = {{m_curX, m_curY+a},{m_curX-b,m_curY+a},{m_curX-b, m_curY},{x,y-(a*2)},{m_curX-b, y},
           		{m_curX-b, y-a},{m_curX, y-a},{m_curX, m_curY+a}};
           drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-a);
      	   m_penPath.lineTo(m_curX-b,m_curY-a);
           m_penPath.lineTo(m_curX-b, m_curY);
           m_penPath.lineTo(x,y+(a*2));
           m_penPath.lineTo(m_curX-b, y);
           m_penPath.lineTo(m_curX-b, y+a);
           m_penPath.lineTo(m_curX, y+a);
           m_penPath.lineTo(m_curX, m_curY-a);
           float[][] dot = {{m_curX, m_curY-a},{m_curX-b,m_curY-a},{m_curX-b, m_curY},{x,y+(a*2)},{m_curX-b, y},
           		{m_curX-b, y+a},{m_curX, y+a},{m_curX, m_curY-a}};
           drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawRightArrows(float x, float y,int cmd){//画向右的箭头
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        float a = dy/4;//正坐标移动的差值的1/4
        float b = dx/2;//横坐标移动的差值的1/2;
        m_penPath.reset();
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, m_curY+a);
       	    m_penPath.lineTo(m_curX+b,m_curY+a);
            m_penPath.lineTo(m_curX+b, m_curY);
            m_penPath.lineTo(x,m_curY+(a*2));
            m_penPath.lineTo(m_curX+b, y);
            m_penPath.lineTo(m_curX+b, y-a);
            m_penPath.lineTo(m_curX, y-a);
            m_penPath.lineTo(m_curX, m_curY+a);
            float[][] dot = {{m_curX, m_curY+a},{m_curX+b,m_curY+a},{m_curX+b, m_curY},{x,m_curY+(a*2)},{m_curX+b, y},
            		{m_curX+b, y-a},{m_curX, y-a},{m_curX, m_curY+a}};
            drawShape(dot);
       }else if(x>m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-a);
      	   m_penPath.lineTo(m_curX+b,m_curY-a);
           m_penPath.lineTo(m_curX+b, m_curY);
           m_penPath.lineTo(x,m_curY-(a*2));
           m_penPath.lineTo(m_curX+b, y);
           m_penPath.lineTo(m_curX+b, y+a);
           m_penPath.lineTo(m_curX, y+a);
           m_penPath.lineTo(m_curX, m_curY-a);
           float[][] dot = {{m_curX, m_curY-a},{m_curX+b,m_curY-a},{m_curX+b, m_curY},{x,m_curY-(a*2)},{m_curX+b, y},
           		{m_curX+b, y+a},{m_curX, y+a},{m_curX, m_curY-a}};
           drawShape(dot);
       }else if(x<m_curX && y>m_curY){
    	   m_penPath.moveTo(m_curX, m_curY+(a*2));
      	   m_penPath.lineTo(m_curX-b,y);
           m_penPath.lineTo(m_curX-b, y-a);
           m_penPath.lineTo(x,y-a);
           m_penPath.lineTo(x, m_curY+a);
           m_penPath.lineTo(m_curX-b, m_curY+a);
           m_penPath.lineTo(m_curX-b, m_curY);
           m_penPath.lineTo(m_curX, m_curY+(a*2));
           float[][] dot = {{m_curX, m_curY+(a*2)},{m_curX-b,y},{m_curX-b, y-a},{x,y-a},{x, m_curY+a},
              		{m_curX-b, m_curY+a},{m_curX-b, m_curY},{m_curX, m_curY+(a*2)}};
              drawShape(dot);
       }else if(x<m_curX && y<m_curY){
    	   m_penPath.moveTo(m_curX, m_curY-(a*2));
      	   m_penPath.lineTo(m_curX-b,y);
           m_penPath.lineTo(m_curX-b, y+a);
           m_penPath.lineTo(x,y+a);
           m_penPath.lineTo(x, m_curY-a);
           m_penPath.lineTo(m_curX-b, m_curY-a);
           m_penPath.lineTo(m_curX-b, m_curY);
           m_penPath.lineTo(m_curX, m_curY-(a*2));
           float[][] dot = {{m_curX, m_curY-(a*2)},{m_curX-b,y},{m_curX-b, y+a},{x,y+a},{x, m_curY-a},
             		{m_curX-b, m_curY-a},{m_curX-b, m_curY},{m_curX, m_curY-(a*2)}};
             drawShape(dot);
       }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawDiamond(float x, float y,int cmd){//画菱形
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        m_penPath.moveTo(m_curX, m_curY);
        if(x>m_curX && y>m_curY){
        	 m_penPath.lineTo(m_curX+dx/2,y);
             m_penPath.lineTo(x, m_curY);
             m_penPath.lineTo(m_curX+(dx/2), m_curY-dy);
             m_penPath.lineTo(m_curX, m_curY);
             float[][] dot = {{m_curX+dx/2,y},{x, m_curY},{m_curX+(dx/2), m_curY-dy},{m_curX, m_curY},{m_curX+dx/2,y}};
             drawShape(dot);
        }else if(x>m_curX && y<m_curY){
        	 m_penPath.lineTo(m_curX+dx/2,y);
             m_penPath.lineTo(x, m_curY);
             m_penPath.lineTo(m_curX+(dx/2), m_curY+dy);
             m_penPath.lineTo(m_curX, m_curY);
             float[][] dot = {{m_curX+dx/2,y},{x, m_curY},{m_curX+(dx/2), m_curY+dy},{m_curX, m_curY},{m_curX+dx/2,y}};
             drawShape(dot);
        }else if(x<m_curX && y>m_curY){
        	 m_penPath.lineTo(m_curX-dx/2,y);
             m_penPath.lineTo(x, m_curY);
             m_penPath.lineTo(m_curX-(dx/2), m_curY-dy);
             m_penPath.lineTo(m_curX, m_curY);
             float[][] dot = {{m_curX-dx/2,y},{x, m_curY},{m_curX-(dx/2), m_curY-dy},{m_curX, m_curY},{m_curX-dx/2,y}};
             drawShape(dot);
        }else if(x<m_curX && y<m_curY){
        	 m_penPath.lineTo(m_curX-dx/2,y);
             m_penPath.lineTo(x, m_curY);
             m_penPath.lineTo(m_curX-(dx/2), m_curY+dy);
             m_penPath.lineTo(m_curX, m_curY);
             float[][] dot = {{m_curX-dx/2,y},{x, m_curY},{m_curX-(dx/2), m_curY+dy},{m_curX, m_curY},{m_curX-dx/2,y}};
             drawShape(dot);
        }
        commSendToService(cmd);
        pointList.clear();
    }
    public void drawZhiLine(float x, float y){//画直线
    	 m_hasDrawn = true;
    	shape_flag = 0;
    	m_penPath.reset();
    	PathEffect effects = new DashPathEffect(new float[]{15,15,15,15},1);  
    	m_penPaint.setPathEffect(effects);
        m_penPaint.setStrokeWidth(3);
        m_penPaint.setColor(Color.GRAY);
    	m_penPath.moveTo(m_curX, m_curY);
    	m_penPath.lineTo(x, y);
    	
    }
    public void drawQuLine(float x,float y,int cmd){//画曲线

		float dx = Math.abs(x - m_curX);
        float dy = Math.abs(y - m_curY);
        
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_penPath.quadTo(m_curX, m_curY, (x + m_curX) / 2, (y + m_curY) / 2);
            m_hasDrawn = true;
            m_curX = x;
            m_curY = y;
        }
        commSendToService(cmd);
    }
    public void drawTrigon(float x,float y,int cmd){//画三角形
    	 m_hasDrawn = true;
    	 dx = Math.abs(x - m_curX);
         dy = Math.abs(y - m_curY);
        m_penPath.reset();
        if(x<m_curX){
        	m_penPath.moveTo(m_curX-(dx/2), m_curY);
        	m_penPath.lineTo(x, y);
        	m_penPath.lineTo(m_curX, y);
        	m_penPath.lineTo(m_curX-(dx/2), m_curY);
        	float[][] dot = {{m_curX-(dx/2), m_curY},{x, y},{m_curX, y},{m_curX-(dx/2), m_curY}};
            drawShape(dot);
        }else{
        	m_penPath.moveTo(m_curX+(dx/2), m_curY);
        	m_penPath.lineTo(x, y);
        	m_penPath.lineTo(m_curX, y);
        	m_penPath.lineTo(m_curX+(dx/2), m_curY);
        	float[][] dot = {{m_curX+(dx/2), m_curY},{x, y},{m_curX, y},{m_curX+(dx/2), m_curY}};
            drawShape(dot);
        }
    	commSendToService(cmd);
    	pointList.clear();
    }
    private void drawRect(float x,float y){//画矩形
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        m_penPath.moveTo(m_curX, m_curY);
        m_penPath.lineTo(m_curX, y);
        m_penPath.lineTo(x, y);
        m_penPath.lineTo(x, m_curY);
        m_penPath.lineTo(m_curX, m_curY);
       
    }
    private float squareRight=0.0f,squareBottom=0.0f;
    private void drawSquare(float x,float y){//画正方形
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        m_penPath.moveTo(m_curX, m_curY);
        if(dx>dy && y>m_curY){
            m_penPath.lineTo(m_curX, y+(dx-dy));
            m_penPath.lineTo(x, y+(dx-dy));
            m_penPath.lineTo(x, m_curY);
            m_penPath.lineTo(m_curX, m_curY);
            squareRight = x; squareBottom = y+(dx-dy);
        }else if(dx>dy && y<m_curY){
        	m_penPath.lineTo(m_curX, y-(dx-dy));
            m_penPath.lineTo(x, y-(dx-dy));   
            m_penPath.lineTo(x, m_curY);
            m_penPath.lineTo(m_curX, m_curY);
            squareRight = x; squareBottom = y-(dx-dy);
        }else if(dx<dy && y>m_curY){
        	m_penPath.lineTo(m_curX, y+(dx-dy));
            m_penPath.lineTo(x, y+(dx-dy));
            m_penPath.lineTo(x, m_curY);
            m_penPath.lineTo(m_curX, m_curY);
            squareRight = x; squareBottom = y+(dx-dy);
        }else if(dx<dy && y<m_curY){
        	m_penPath.lineTo(m_curX, y-(dx-dy));
            m_penPath.lineTo(x, y-(dx-dy));
            m_penPath.lineTo(x, m_curY);
            m_penPath.lineTo(m_curX, m_curY);
            squareRight = x; squareBottom = y-(dx-dy);
        }
    }
    private void drawShape(float[][] dot){
    	for(int i=0;i<dot.length;i++){
    		HashMap<String, String> map = new HashMap<String,String>();
    		map.put("x", String.valueOf(dot[i][0]));
            map.put("y", String.valueOf(dot[i][1]));
            pointList.add(map);
    	}
    }
    private void drawFiveShape(float x,float y,int cmd){//画五边行
    	 m_hasDrawn = true;
    	dx = Math.abs(x - m_curX);
        dy = Math.abs(y - m_curY);
        m_penPath.reset();
        if(x>m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, (dy/2)+m_curY);
            m_penPath.lineTo(m_curX+(dx/4), y);
            m_penPath.lineTo(m_curX+((dx/4)*3), y);
            m_penPath.lineTo(x, m_curY+(dy/2));
            m_penPath.lineTo(m_curX+(dx/2), m_curY);
            m_penPath.lineTo(m_curX, (dy/2)+m_curY);
            float[][] dot = {{m_curX,(dy/2)+m_curY},{m_curX+(dx/4),y},{m_curX+((dx/4)*3),y},{x,m_curY+(dy/2)},{m_curX+(dx/2),m_curY},{m_curX,(dy/2)+m_curY}};
            drawShape(dot);
        }else if(x>m_curX && y<m_curY){
        	m_penPath.moveTo(m_curX, m_curY-(dy/2));
            m_penPath.lineTo(m_curX+(dx/4), m_curY);
            m_penPath.lineTo(m_curX+((dx/4)*3), m_curY);
            m_penPath.lineTo(x, m_curY-(dy/2));
            m_penPath.lineTo(m_curX+(dx/2), y);
            m_penPath.lineTo(m_curX, m_curY-(dy/2));
            float[][] dot = {{m_curX, m_curY-(dy/2)},{m_curX+(dx/4), m_curY},{m_curX+((dx/4)*3), m_curY},
            		{x, m_curY-(dy/2)},{m_curX+(dx/2), y},{m_curX, m_curY-(dy/2)}};
            drawShape(dot);
        }else if(x<m_curX && y>m_curY){
        	m_penPath.moveTo(m_curX, (dy/2)+m_curY);
            m_penPath.lineTo(m_curX-(dx/4), y);
            m_penPath.lineTo(m_curX-((dx/4)*3), y);
            m_penPath.lineTo(x, m_curY+(dy/2));
            m_penPath.lineTo(m_curX-(dx/2), m_curY);
            m_penPath.lineTo(m_curX, (dy/2)+m_curY);
            float[][] dot = {{m_curX, (dy/2)+m_curY},{m_curX-(dx/4), y},{m_curX-((dx/4)*3), y},
            		{x, m_curY+(dy/2)},{m_curX-(dx/2), m_curY},{m_curX, (dy/2)+m_curY}};
            drawShape(dot);
        }else if(x<m_curX && y<m_curY){
        	m_penPath.moveTo(m_curX, m_curY-(dy/2));
            m_penPath.lineTo(m_curX-(dx/4), m_curY);
            m_penPath.lineTo(m_curX-((dx/4)*3), m_curY);
            m_penPath.lineTo(x, m_curY-(dy/2));
            m_penPath.lineTo(m_curX-(dx/2), y);
            m_penPath.lineTo(m_curX, m_curY-(dy/2));
            float[][] dot = {{m_curX, m_curY-(dy/2)},{m_curX-(dx/4), m_curY},{m_curX-((dx/4)*3), m_curY},
            		{x, m_curY-(dy/2)},{m_curX-(dx/2), y},{m_curX, m_curY-(dy/2)}};
            drawShape(dot);
        }
        commSendToService(cmd);
        pointList.clear();
    }
    private void commSendToService(int cmd){//向服务器发送画曲线的命令，这里的曲线包括多边形
    	int[] dataInt1 = new int[pointList.size()*2+7];
		dataInt1[0] = cmd;
		dataInt1[1] = 4;
		dataInt1[2] = 22+(pointList.size()*2*4);
		dataInt1[3] = 0;
		dataInt1[4] = 241;
		dataInt1[5] = MyApplication.getInstance().getLineId();
		dataInt1[6] = pointList.size();
		int add = 7;
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
    private void drawRoundRect(float x,float y){//画圆角矩形
    	 m_hasDrawn = true;
    	if(x>m_curX){
    		left = m_curX;
    		right = x;
    	}else{
    		left = x;
    		right = m_curX;
    	}
    	if(y>m_curY){
    		top = m_curY;
    		bottom = y;
    	}else{
    		top = y;
    		bottom = m_curY;
    	}      
    }
}
