package com.qingfengweb.baoqi.insuranceShow;   
import android.content.Context;   
import android.graphics.Bitmap;   
import android.graphics.Canvas;   
import android.graphics.Color;
import android.graphics.Paint;   
import android.graphics.Path;   
import android.util.AttributeSet;
import android.view.MotionEvent;   
import android.view.View;   
import android.widget.Toast;
public class WritePath extends View {   
    private float mX , mY;   
    public Path mPath;   
    private Paint mPaint;   
    private static final float TOUCH_TOLERANCE = 4;
    private static Bitmap  mBitmap;   
    private static Canvas  mCanvas;   
    private Paint   mBitmapPaint; 
    
    
    private int windowsWidth;
    private int windowsHeight;
    
    
       
    public WritePath(Context c,AttributeSet attrs) {   
        super(c,attrs);   
//        windowsWidth = c.getWindowManager().getDefaultDisplay().getWidth();
//        windowsHeight = c.getWindowManager().getDefaultDisplay().getHeight(); 
        mPaint = new Paint();//创建画笔渲染对象   
        mPaint.setAntiAlias(true);//设置抗锯齿，让绘画比较平滑   
        mPaint.setDither(true);//设置递色   
        mPaint.setColor(Color.BLACK);//设置画笔的颜色   
        mPaint.setStyle(Paint.Style.STROKE);//画笔的类型有三种（1.FILL 2.FILL_AND_STROKE 3.STROKE ）   
        mPaint.setStrokeJoin(Paint.Join.ROUND);//默认类型是MITER（1.BEVEL 2.MITER 3.ROUND ）   
        mPaint.setStrokeCap(Paint.Cap.ROUND);//默认类型是BUTT（1.BUTT 2.ROUND 3.SQUARE ）   
        mPaint.setStrokeWidth(8);//设置描边的宽度，如果设置的值为0那么边是一条极细的线   
           
        mBitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);//绘制固定大小的bitmap对象   
        mCanvas = new Canvas(mBitmap);//将固定的bitmap对象嵌入到canvas对象中   
        mPath = new Path();//创建画笔路径   
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);   
    }   
    
    public WritePath(InsureActivity c) {   
        super(c);   
//        windowsWidth = c.getWindowManager().getDefaultDisplay().getWidth();
//        windowsHeight = c.getWindowManager().getDefaultDisplay().getHeight(); 
        mPaint = new Paint();//创建画笔渲染对象   
        mPaint.setAntiAlias(true);//设置抗锯齿，让绘画比较平滑   
        mPaint.setDither(true);//设置递色   
        mPaint.setColor(Color.BLACK);//设置画笔的颜色   
        mPaint.setStyle(Paint.Style.STROKE);//画笔的类型有三种（1.FILL 2.FILL_AND_STROKE 3.STROKE ）   
        mPaint.setStrokeJoin(Paint.Join.ROUND);//默认类型是MITER（1.BEVEL 2.MITER 3.ROUND ）   
        mPaint.setStrokeCap(Paint.Cap.ROUND);//默认类型是BUTT（1.BUTT 2.ROUND 3.SQUARE ）   
        mPaint.setStrokeWidth(8);//设置描边的宽度，如果设置的值为0那么边是一条极细的线   
//        System.out.println("image width:"+c.guo_tab5.getWidth()+"image height:"+c.guo_tab5.getWidth());
//        mBitmap = Bitmap.createBitmap(c.guo_tab5.getWidth(), c.guo_tab5.getHeight(), Bitmap.Config.ARGB_8888);//绘制固定大小的bitmap对象   
        mBitmap = Bitmap.createBitmap(1060, 1060, Bitmap.Config.ARGB_8888);//绘制固定大小的bitmap对象   
        mCanvas = new Canvas(mBitmap);//将固定的bitmap对象嵌入到canvas对象中   
        mPath = new Path();//创建画笔路径   
        mBitmapPaint = new Paint(Paint.DITHER_FLAG); 
    } 
    @Override  
    protected void onDraw(Canvas canvas) { 
    	
    		canvas.drawColor(Color.WHITE);   
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);   
            canvas.drawPath(mPath, mPaint);
            super.onDraw(canvas);

    	
           
    }   
    private void onTouchDown(float x , float y){   
        mPath.reset();//将上次的路径保存起来，并重置新的路径。   
        mPath.moveTo(x, y);//设置新的路径“轮廓”的开始   
        mX = x;   
        mY = y;   
    }   
    private void onTouchMove(float x , float y){   
        float dx = Math.abs(x - mX);   
        float dy = Math.abs(y - mY);   
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {   
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);   
            mX = x;   
            mY = y;   
        }   
    }   
    private void onTouchUp(float x , float y){   
        mPath.lineTo(mX, mY);//从最后一个指定的xy点绘制一条线，如果没有用moveTo方法，那么起始点表示（0，0）点。   
        // commit the path to our offscreen   
        mCanvas.drawPath(mPath, mPaint);//手指离开屏幕后，绘制创建的“所有”路径。
        // kill this so we don't double draw   
        mPath.reset();   
    }   
    @Override  
    public boolean onTouchEvent(MotionEvent event) {   
        float x = event.getX();   
        float y = event.getY();   
        switch (event.getAction()) {   
        case MotionEvent.ACTION_DOWN://手指开始按压屏幕，这个动作包含了初始化位置   
            onTouchDown(x , y);   
            invalidate();//刷新画布，重新运行onDraw（）方法   
            break;   
        case MotionEvent.ACTION_MOVE://手指按压屏幕时，位置的改变触发，这个方法在ACTION_DOWN和ACTION_UP之间。   
           onTouchMove(x , y);   
            invalidate();             
 break;  
        case MotionEvent.ACTION_UP://手指离开屏幕，不再按压屏幕   
            onTouchUp(x , y);   
            invalidate();   
            break;   
        default:   
            break;   
       }   
        return true;   
    } 
    
//    public static void storeWrite(){
//    	mCanvas.save(Canvas.ALL_SAVE_FLAG);
//    	mCanvas.restore();
//    	MyApplication.getInstance().setBitmap1(mBitmap);
//    }
}  