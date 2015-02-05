package com.zhihuigu.sosoOffice.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ScrollLayoutView extends HorizontalScrollView{
	private float currentX = 0;
	private boolean boo = true;
	public static boolean MOVE_ORIENTATION = true;//true为向右  false为向左
	public static int FLAG_CENTER = 50;//终点
	public ScrollLayoutView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public ScrollLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ScrollLayoutView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				currentX = event.getX();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (event.getX() > currentX) {// 向右滑动
					if(this.getScrollX()<FLAG_CENTER){// 向右滚动
						boo = true;
					}else{// 向左滚动
						boo = false;
					}
				} else if (event.getX() < currentX) {// 向左滑动
					if(this.getScrollX()<FLAG_CENTER){// 向右滚动
						boo = true;
					}else{// 向左滚动
						boo = false;
					}
				}
				currentX = event.getX();
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				if(boo){// 向右滚动
					this.scrollTo(0, 0);
					MOVE_ORIENTATION = true;
				}else{// 向左滚动
					this.scrollTo(FLAG_CENTER*2, 0);
					MOVE_ORIENTATION = false;
				}
				currentX = event.getX();
			}
		return super.onTouchEvent(event);
	}*/
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		super.addView(child, index, params);
	}
	@Override
	public void addView(View child, int index) {
		// TODO Auto-generated method stub
		super.addView(child, index);
	}
	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		super.addView(child, params);
	}
	@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		super.addView(child);
	}
	@Override
	public void fling(int velocityX) {
		// TODO Auto-generated method stub
		super.fling(velocityX/60);
	}
	
	
	
}

