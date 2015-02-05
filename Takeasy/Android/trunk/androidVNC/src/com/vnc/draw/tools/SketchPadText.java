package com.vnc.draw.tools;


import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadTool;
/**function:ªÊ÷∆Õ÷‘≤
 * @author:

 */
public class SketchPadText implements ISketchPadTool{
	private Paint mPaint=new Paint();
    private boolean m_hasDrawn = true; 
    private float left =0.0f;
    private float top = 0.0f;
    public SketchPadText(int penSize, int penColor)
    {
    	mPaint.setAntiAlias(true);
    	mPaint.setDither(true);
    	mPaint.setColor(penColor);
    	mPaint.setStrokeWidth(penSize);
    //	mPaint.setStyle(Paint.Style.STROKE);
    	mPaint.setStrokeCap(Paint.Cap.ROUND);
        //mPaint.setStrokeWidth(1);//…Ë÷√ª≠± ¥÷œ∏
        mPaint.setTextSize(20);
    }
	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (null != canvas)
        {
			int a = MyApplication.getInstance().getText_back_flag();
			if(a == 0){
				if(MyApplication.getInstance().getMsg()!=null && !MyApplication.getInstance().getMsg().equals("")){
					canvas.drawText(
							MyApplication.getInstance().getMsg(), 
							MyApplication.getInstance().getMsgLeft(), 
							MyApplication.getInstance().getMsgTop(), mPaint);
					int[] dataInt0 = {1214,1,26,0,241,(int) MyApplication.getInstance().getMsgLeft(),
							MyApplication.getInstance().getMsgLeft(),
							MyApplication.getInstance().getMsgTop()};
//					SendToServiceToDraw sendService = new SendToServiceToDraw();
					MyApplication.getInstance().getSendToService().sDrawText(1215,MyApplication.getInstance().getTextId());
					//MyApplication.getInstance().setMsg("");
					}
			}else if(a == 1){
				//if(MyApplication.getInstance().back_text!=SketchPadView.m_undoText.size()-1){
					Map<String,Object> map = SketchPadView.m_undoText.get(MyApplication.getInstance().back_text);
					String str = (String) map.get("msg");    
					int left = (Integer) map.get("left");
					int top = (Integer) map.get("top");
					int color = (Integer) map.get("color");
					canvas.drawText(str , left , top, mPaint);
					MyApplication.getInstance().back_text++;
					//SketchPadView.m_undoBm.remove(SketchPadView.m_undoBm.size()-1);
			//	}
			}else if(a == 2){
					Map<String,Object> map = SketchPadView.m_undoText.get(MyApplication.getInstance().back_text);
					String str = (String) map.get("msg");    
					int left = (Integer) map.get("left");
					int top = (Integer) map.get("top");
					int color = (Integer) map.get("color");
					canvas.drawText(str , left , top, mPaint);
					MyApplication.getInstance().back_text++;
			}
				
        }
	}
	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return m_hasDrawn;
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
