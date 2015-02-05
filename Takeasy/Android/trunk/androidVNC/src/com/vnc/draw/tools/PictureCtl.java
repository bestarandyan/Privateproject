package com.vnc.draw.tools;

import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.content.ISketchPadTool;

public class PictureCtl  implements ISketchPadTool{
	public static  Paint m_penPaint = new Paint();
	public static Bitmap b = null;
	public static float bmLeft = 0.0f,bmTop = 0.0f;
	public PictureCtl(int penSize, int penColor) {
		// TODO Auto-generated constructor stub
		    m_penPaint.setAntiAlias(true);
	        m_penPaint.setDither(true);
	        m_penPaint.setColor(penColor);
	        m_penPaint.setStrokeWidth(penSize);
	        m_penPaint.setStyle(Paint.Style.STROKE);
	        m_penPaint.setStrokeCap(Paint.Cap.ROUND);
	}
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(null != canvas){
			Bitmap b = null;
			b = MyApplication.getInstance().getBitmap();
			int l = MyApplication.getInstance().getBitmapLeft();
			int t = MyApplication.getInstance().getBitmapTop();
			//SketchPadView.m_canvas.drawBitmap(b, l, t,null);
			int a = MyApplication.getInstance().getPic_back_flag();
			/*Path m_penPath = new Path();
			m_penPath.moveTo(100, 100);
            m_penPath.lineTo(101, 101);
			canvas.drawPath(m_penPath, m_penPaint);*/
			if(a == 0){
				canvas.drawBitmap(b, l, t,null);
				
//				int[] dataInt0 = {1224,1,34,0,241,l,l,t,b.getWidth(),b.getHeight()};
				/*SendToServiceToDraw sendService = new SendToServiceToDraw();
				sendService.sUpdatePic();*/
			}else if(a == 1){
				//if(MyApplication.getInstance().back_pic!=SketchPadView.m_undoBm.size()-1){
					Map<String,Object> map = SketchPadView.m_undoBm.get(MyApplication.getInstance().back_pic);
					Bitmap bm = (Bitmap) map.get("bm");    
					int left = (Integer) map.get("left");
					int top = (Integer) map.get("top");
					canvas.drawBitmap(bm, left,top, m_penPaint);
					MyApplication.getInstance().back_pic++;
					//SketchPadView.m_undoBm.remove(SketchPadView.m_undoBm.size()-1);
			//	}
				
			}else if(a == 2){
				//if(MyApplication.getInstance().back_pic!=SketchPadView.m_undoBm.size()-1){
					Map<String,Object> map = SketchPadView.m_undoBm.get(MyApplication.getInstance().back_pic);
					Bitmap bm = (Bitmap) map.get("bm");    
					int left = (Integer) map.get("left");
					int top = (Integer) map.get("top");
					canvas.drawBitmap(bm, left,top, m_penPaint);
					MyApplication.getInstance().back_pic++;
					//SketchPadView.m_undoBm.remove(SketchPadView.m_undoBm.size()-1);
				//}
				
			}
		}
	}

	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return true;
	}

	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		
	}

}
