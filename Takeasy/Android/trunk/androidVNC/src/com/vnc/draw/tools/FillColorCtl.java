package com.vnc.draw.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;

import com.vnc.draw.content.ISketchPadTool;


public class FillColorCtl  implements ISketchPadTool{
	private Paint mPaint=new Paint();
    private boolean m_hasDrawn = false; 
    private RectF rectf=new RectF();  
    public float yx = 0.0f, yy = 0.0f;
    public int penColor = 0;
    public ProgressDialog progress;
    Context context;
    public boolean fillboo = false;
    public FillColorCtl(Context context,int penSize, int penColor)
    {
    	mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(penColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(penSize);//设置画笔粗细
        this.penColor = penColor;
        this.context = context;
    }
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(progress == null){
    		progress = new ProgressDialog(context);
    		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	}
    	progress.setMessage("正在填充图形，请稍候。。。");
    	progress.show();
    	Thread thread = new Thread(myRunnable);
    	thread.start();
	}

	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return fillboo;
	}

	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		yx  = x; yy = y;//给触点赋值（填充区的起始点）
	}

	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		yx  = x; yy = y;//给触点赋值（填充区的起始点）
	}
public Runnable myRunnable = new Runnable() {
		
		public void run() {/*
			// TODO Auto-generated method stub
			seed_fill((int)yx,(int)yy,SketchPadView.m_foreBitmap.getPixel((int)yx,(int)yy),penColor);
			
			Message msg = new Message();
			msg.what = 0;
			myHandler.sendMessage(msg);
		*/}
	};
	public Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				progress.dismiss();
				fillboo = true;
				//invalidate();
			}
			super.handleMessage(msg);
		}
		
	};
	//队列实现种子递归，用于油漆桶工具   7/28
  	public void seed_fill (int x, int y, int t_color, int r_color){/*
     		int MAX_COL = MyApplication.getInstance().getScreenw();
  		int MAX_ROW = MyApplication.getInstance().getScreenh();
  		int row_size = MAX_ROW;
  		int col_size = MAX_COL;
  		if (x < 0 || x >= col_size || y < 0 || y >= row_size || SketchPadView.m_foreBitmap.getPixel(x,y) == r_color) {
  			return;
  		}   
  		//write of bestar
  		
  		//end of bestar write
  		int queue[][]=new int[MAX_ROW*MAX_COL+1][2];
  		int head = 0, end = 0;
  		int tx, ty;
  		 Add node to the end of queue.  
  		queue[end][0] = x;
  		queue[end][1] = y;
  		end++;
  		while (head < end) {
  			tx = queue[head][0]; 
  			ty = queue[head][1];
  			if (SketchPadView.m_foreBitmap.getPixel(tx,ty) == t_color) { 
  				SketchPadView.m_foreBitmap.setPixel(tx,ty,r_color);
  			}
  			 Remove the first element from queue.  
  			head++;
  			 West  
  			if (tx-1 >= 0 && SketchPadView.m_foreBitmap.getPixel(tx-1,ty) == t_color) {
  				SketchPadView.m_foreBitmap.setPixel(tx-1,ty,r_color);
  				queue[end][0] = tx-1;
  				queue[end][1] = ty;
  				end++;
  			}else if(tx-1 >= 0 && SketchPadView.m_foreBitmap.getPixel(tx-1,ty)!=t_color){
  				SketchPadView.m_foreBitmap.setPixel(tx-1,ty,r_color);
  			}
  			 East  
  			if (tx+1 < col_size && SketchPadView.m_foreBitmap.getPixel(tx+1,ty) == t_color) {
  				SketchPadView.m_foreBitmap.setPixel(tx+1,ty,r_color);
  				queue[end][0] = tx+1;
  				queue[end][1] = ty;
  				end++;
  			}else if(tx+1 <col_size && SketchPadView.m_foreBitmap.getPixel(tx+1,ty)!=t_color){
  				SketchPadView.m_foreBitmap.setPixel(tx+1,ty,r_color);
  			}
  			 North  
  			if (ty-1 >= 0 && SketchPadView.m_foreBitmap.getPixel(tx,ty-1) == t_color) {
  				SketchPadView.m_foreBitmap.setPixel(tx,ty-1,r_color);
  				queue[end][0] = tx;
  				queue[end][1] = ty-1;
  				end++;
  			}else if(ty-1 >= 0 && SketchPadView.m_foreBitmap.getPixel(tx,ty-1)!=t_color){
  				SketchPadView.m_foreBitmap.setPixel(tx,ty-1,r_color);
  			}
  			 South  
  			if (ty+1<SketchPadView.m_foreBitmap.getHeight() && ty+1 < row_size &&  SketchPadView.m_foreBitmap.getPixel(tx,ty+1) == t_color) {
  				SketchPadView.m_foreBitmap.setPixel(tx,ty+1,r_color);
  				queue[end][0] = tx;
  				queue[end][1] = ty+1;
  				end++;
  			}else if(ty+1<SketchPadView.m_foreBitmap.getHeight() && ty+1<row_size&&SketchPadView.m_foreBitmap.getPixel(tx,ty+1)!=t_color){
  				SketchPadView.m_foreBitmap.setPixel(tx,ty+1,r_color);
  			}
  		}
  		return; 
  	*/}          
}
