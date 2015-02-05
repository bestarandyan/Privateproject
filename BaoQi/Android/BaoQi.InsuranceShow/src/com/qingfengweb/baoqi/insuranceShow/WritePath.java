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
        mPaint = new Paint();//����������Ⱦ����   
        mPaint.setAntiAlias(true);//���ÿ���ݣ��û滭�Ƚ�ƽ��   
        mPaint.setDither(true);//���õ�ɫ   
        mPaint.setColor(Color.BLACK);//���û��ʵ���ɫ   
        mPaint.setStyle(Paint.Style.STROKE);//���ʵ����������֣�1.FILL 2.FILL_AND_STROKE 3.STROKE ��   
        mPaint.setStrokeJoin(Paint.Join.ROUND);//Ĭ��������MITER��1.BEVEL 2.MITER 3.ROUND ��   
        mPaint.setStrokeCap(Paint.Cap.ROUND);//Ĭ��������BUTT��1.BUTT 2.ROUND 3.SQUARE ��   
        mPaint.setStrokeWidth(8);//������ߵĿ�ȣ�������õ�ֵΪ0��ô����һ����ϸ����   
           
        mBitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);//���ƹ̶���С��bitmap����   
        mCanvas = new Canvas(mBitmap);//���̶���bitmap����Ƕ�뵽canvas������   
        mPath = new Path();//��������·��   
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);   
    }   
    
    public WritePath(InsureActivity c) {   
        super(c);   
//        windowsWidth = c.getWindowManager().getDefaultDisplay().getWidth();
//        windowsHeight = c.getWindowManager().getDefaultDisplay().getHeight(); 
        mPaint = new Paint();//����������Ⱦ����   
        mPaint.setAntiAlias(true);//���ÿ���ݣ��û滭�Ƚ�ƽ��   
        mPaint.setDither(true);//���õ�ɫ   
        mPaint.setColor(Color.BLACK);//���û��ʵ���ɫ   
        mPaint.setStyle(Paint.Style.STROKE);//���ʵ����������֣�1.FILL 2.FILL_AND_STROKE 3.STROKE ��   
        mPaint.setStrokeJoin(Paint.Join.ROUND);//Ĭ��������MITER��1.BEVEL 2.MITER 3.ROUND ��   
        mPaint.setStrokeCap(Paint.Cap.ROUND);//Ĭ��������BUTT��1.BUTT 2.ROUND 3.SQUARE ��   
        mPaint.setStrokeWidth(8);//������ߵĿ�ȣ�������õ�ֵΪ0��ô����һ����ϸ����   
//        System.out.println("image width:"+c.guo_tab5.getWidth()+"image height:"+c.guo_tab5.getWidth());
//        mBitmap = Bitmap.createBitmap(c.guo_tab5.getWidth(), c.guo_tab5.getHeight(), Bitmap.Config.ARGB_8888);//���ƹ̶���С��bitmap����   
        mBitmap = Bitmap.createBitmap(1060, 1060, Bitmap.Config.ARGB_8888);//���ƹ̶���С��bitmap����   
        mCanvas = new Canvas(mBitmap);//���̶���bitmap����Ƕ�뵽canvas������   
        mPath = new Path();//��������·��   
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
        mPath.reset();//���ϴε�·�������������������µ�·����   
        mPath.moveTo(x, y);//�����µ�·�����������Ŀ�ʼ   
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
        mPath.lineTo(mX, mY);//�����һ��ָ����xy�����һ���ߣ����û����moveTo��������ô��ʼ���ʾ��0��0���㡣   
        // commit the path to our offscreen   
        mCanvas.drawPath(mPath, mPaint);//��ָ�뿪��Ļ�󣬻��ƴ����ġ����С�·����
        // kill this so we don't double draw   
        mPath.reset();   
    }   
    @Override  
    public boolean onTouchEvent(MotionEvent event) {   
        float x = event.getX();   
        float y = event.getY();   
        switch (event.getAction()) {   
        case MotionEvent.ACTION_DOWN://��ָ��ʼ��ѹ��Ļ��������������˳�ʼ��λ��   
            onTouchDown(x , y);   
            invalidate();//ˢ�»�������������onDraw��������   
            break;   
        case MotionEvent.ACTION_MOVE://��ָ��ѹ��Ļʱ��λ�õĸı䴥�������������ACTION_DOWN��ACTION_UP֮�䡣   
           onTouchMove(x , y);   
            invalidate();             
 break;  
        case MotionEvent.ACTION_UP://��ָ�뿪��Ļ�����ٰ�ѹ��Ļ   
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