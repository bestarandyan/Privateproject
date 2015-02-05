/**
 * 
 */
package com.qingfengweb.util;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * @author ¡ı–«–«
 * @createDate 2013°¢6°¢21
 *
 */
public class CustomListView extends ListView{
	
	 private float xDistance, yDistance, xLast, yLast;
	 private GestureDetector mGestureDetector;
	    View.OnTouchListener mGestureListener;

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		  setFadingEdgeLength(0);

		// TODO Auto-generated constructor stub
	}
//	private int oldX = 0;
//	private int newX = 0;
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			oldX = (int) event.getX();
////			CustomViewAbove.mEnabled = false;
//		}else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			newX = (int) event.getX();
//			if(newX < oldX){
//					CustomViewAbove.mEnabled = false;
//			}else{
////				if(viewPager.getCurrentItem() == 0){
//					CustomViewAbove.mEnabled = true;
////				}
//			}
//		}else if (event.getAction() == MotionEvent.ACTION_UP) {
//		}
//		return super.onTouchEvent(event);
//	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	  return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}
	class YScrollDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	   if(distanceY!=0&&distanceX!=0){
	           
	   }
	            if(Math.abs(distanceY) >= Math.abs(distanceX)) {
	                return true;
	            }
	            return false;
	        }
	}

}
