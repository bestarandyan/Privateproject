package com.boluomi.children.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {  
	  
	  
	  
    public MyHorizontalScrollView(Context context) {  

            super(context);  

            // TODO Auto-generated constructor stub  

    }  



    public MyHorizontalScrollView(Context context, AttributeSet attrs,  

                    int defStyle) {  

            super(context, attrs, defStyle);  

            // TODO Auto-generated constructor stub  

    }  



     public MyHorizontalScrollView(Context context, AttributeSet attrs) {  

            super(context, attrs);  

            // TODO Auto-generated constructor stub  

    }  



    @Override
	public void fling(int velocityX) {
		// TODO Auto-generated method stub
		super.fling(velocityX*2);
	}



	@Override  

    public boolean arrowScroll(int direction) {  

            // TODO Auto-generated method stub  

            Log.e("boolean", super.arrowScroll(direction)+"");  

            return super.arrowScroll(direction);  

    }  



    @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}



	@Override  

    public boolean onTouchEvent(MotionEvent ev) {   

            // TODO Auto-generated method stub  

            Log.d("touchevent", "touchevent"+super.onTouchEvent(ev));  

//            return super.onTouchEvent(ev);  

            return boo;  

    }  
	public boolean boo = true;
    public void arrawScroll(){
    	boo = true;
    }
    
    public void prohibitScroll(){
    	boo = false;
    }

}
