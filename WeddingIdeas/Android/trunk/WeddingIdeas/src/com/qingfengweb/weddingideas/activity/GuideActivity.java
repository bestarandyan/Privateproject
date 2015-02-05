/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.util.ArrayList;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.adapter.ViewPagerAdapter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author qingfeng
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) public class GuideActivity extends BaseActivity implements OnTouchListener{
	ViewPager viewPager;
	private ArrayList<View> pageViews;  
	int currentPage = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_guide);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pageViews = new ArrayList<View>();
		getPageView();
		viewPager.setAdapter(new ViewPagerAdapter(pageViews));  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());  
		viewPager.setOnTouchListener(this);
	}
	public void getPageView(){
		View v1 = LayoutInflater.from(this).inflate(R.layout.page_guide1, null);
		pageViews.add(v1);
		View v2 = LayoutInflater.from(this).inflate(R.layout.page_guide2, null);
		pageViews.add(v2);
		View v3 = LayoutInflater.from(this).inflate(R.layout.page_guide3, null);
		pageViews.add(v3);
	}
	int sx = 0;
	int sy = 0;
	int ex = 0;
	int ey = 0;
	 // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	if(arg0 == 0){
        		currentPage = 0;
        	}else if(arg0 == 1){
        		currentPage = 1;
        	}else if(arg0 == 2){
        		currentPage = 2;
        	}
        }  
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(currentPage != 2){
			return viewPager.onTouchEvent(event);
		}
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			sx = (int) event.getX();
			sy = (int) event.getY();
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			return super.onTouchEvent(event);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			ex = (int) event.getX();
			ey = (int) event.getY();
			if(Math.abs(sx-ex)<Math.abs(sy-ey)){
				return super.onTouchEvent(event);
			}
			if(sx>ex && (sx-ex)>30){//手指向左滑动
				Intent intent = new Intent(this,LoginOrSignActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//				if(isIntentRight){
//					return super.onTouchEvent(event);
//				}
//				Intent intent = new Intent(this,BlessingActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//				isIntentRight = true;
			}else if(sx<ex && (ex-sx)>30){
//				if(isIntentLeft){
					return super.onTouchEvent(event);
//				}
//				finish();
//				overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
//				isIntentLeft = true;
			}else{
				return super.onTouchEvent(event);
			}
		}
		return super.onTouchEvent(event);
	}  
}
