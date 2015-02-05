/**
 * 
 */
package com.chinaLife.claimAssistant.activity;

import java.util.ArrayList;

import com.chinaLife.claimAssistant.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * @author 刘星星
 * @createDate 2013、7、10
 *
 */
public class Sc_GuideActivity extends Activity{
	ViewPager pager;
	private ArrayList<View> pageViews; 
	ImageView dot1,dot2,dot3,dot4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_guide);
		findview();
		addPagerView();
		Sc_ExitApplication.getInstance().context = Sc_GuideActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_GuideActivity.this);
	}
	
	private void findview(){
		pager  = (ViewPager) findViewById(R.id.guide_vp);
		dot1 = (ImageView) findViewById(R.id.dot1);
		dot2 = (ImageView) findViewById(R.id.dot2);
		dot3 = (ImageView) findViewById(R.id.dot3);
		dot4 = (ImageView) findViewById(R.id.dot4);
	}
	
	private void addPagerView(){
		pageViews = new ArrayList<View>();  
		pageViews.add(getChildView(R.layout.sc_guide_one));
		pageViews.add(getChildView(R.layout.sc_guide_two));
		pageViews.add(getChildView(R.layout.sc_guide_three));
		pageViews.add(getChildView(R.layout.sc_guide_four));
		pager.setAdapter(new GuidePageAdapter());
		pager.setOnPageChangeListener(new GuidePageChangeListener());
	}
	 // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {  
  	  
        @Override  
        public int getCount() {  
            return pageViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
    } 
    
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
        		dot1.setBackgroundResource(R.drawable.sc_dot3);
        		dot2.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot3.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot4.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        	}else if(arg0 == 1){
        		dot1.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot2.setBackgroundResource(R.drawable.sc_dot3);
        		dot3.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot4.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        	}else if(arg0 == 2){
        		dot1.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot2.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot3.setBackgroundResource(R.drawable.sc_dot3);
        		dot4.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        	}else if(arg0 == 3){
        		dot1.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot2.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot3.setBackgroundResource(R.drawable.sc_china_yd_dot1);
        		dot4.setBackgroundResource(R.drawable.sc_dot3);
        	}
        }  
    }  
	private View getChildView(int layout){
		View view = LayoutInflater.from(this).inflate(layout, null);
		if(layout == R.layout.sc_guide_four){
			ImageView btn = (ImageView) view.findViewById(R.id.enjoyMain);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor sharedata = Sc_MyApplication.getInstance().getContext2().getSharedPreferences("userinfo", 0)
							.edit();
					sharedata.putInt("first", 0);
					sharedata.commit();
					Intent intent = new Intent(Sc_GuideActivity.this,
							Sc_MainActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
		return view;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Sc_ExitApplication.getInstance().showExitDialog();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
	}
}
