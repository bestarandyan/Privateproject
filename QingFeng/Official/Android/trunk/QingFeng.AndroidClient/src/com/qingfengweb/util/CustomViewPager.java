/**
 * 
 */
package com.qingfengweb.util;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.client.fragmengs.AboutUsFragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author ¡ı–«–«
 *
 */
public class CustomViewPager extends ViewPager{
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	private int oldX = 0;
	private int newX = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("pager onTouchEvent");
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			oldX = (int) event.getX();
////			CustomViewAbove.mEnabled = false;
//		}else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			newX = (int) event.getX();
//			if(newX < oldX){
//					CustomViewAbove.mEnabled = false;
//			}else{
////				if(AboutUsFragment.viewPager.getCurrentItem() == 0){
//////					CustomViewAbove.mEnabled = true;
////				}
//			}
//		}else if (event.getAction() == MotionEvent.ACTION_UP) {
//		}
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		System.out.println("pager onInterceptTouchEvent");
		return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		System.out.println("pager dispatchTouchEvent");
		return super.dispatchTouchEvent(ev);
	}
	@Override
	protected void onAnimationStart() {
		// TODO Auto-generated method stub
		super.onAnimationStart();
	}

}
