package com.qingfengweb.weddingideas.customview;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public class MyVerticalScrollView  extends ScrollView{  
	  
	  
	  
    public MyVerticalScrollView(Context context) {  
            super(context);  
    }  
    public MyVerticalScrollView(Context context, AttributeSet attrs,  
                    int defStyle) {  
            super(context, attrs, defStyle);  
    }  
     public MyVerticalScrollView(Context context, AttributeSet attrs) {  
            super(context, attrs);  
    }  
    @Override  
    public boolean arrowScroll(int direction) {  
            Log.e("boolean", super.arrowScroll(direction)+"");  
            return super.arrowScroll(direction);  
    }  
    @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}



	@Override
	public void fling(int velocityY) {
		// TODO Auto-generated method stub
		super.fling(velocityY);
	}

	int x1 = 0;
	int x2 = 0;
	int y1 = 0;
	int y2 = 0;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x1 = (int) event.getX();
			y1 = (int) event.getY();
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			x2 = (int) event.getX();
			y2 = (int) event.getY();
			int x = Math.abs(x1-x2);
			int y = Math.abs(y1-y2);
			if(x > y){
				return false;
			}
		}
		return super.onInterceptHoverEvent(event);
	}
	@Override  
    public boolean onTouchEvent(MotionEvent ev) {   
            Log.d("touchevent", "touchevent"+super.onTouchEvent(ev));  
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
