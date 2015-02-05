package com.boluomi.children.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.boluomi.children.R;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.SDKInfo;
import com.boluomi.children.model.StoreInfo;
import com.boluomi.children.util.MessageBox;

public class WelcomeActivity extends BaseActivity{
	private Button goIn ;
	private ViewPager pager;
	private ArrayList<View> pageViews; 
	ImageView dot1,dot2,dot3,dot4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_welcome);
		goIn = (Button) findViewById(R.id.goIn);
		goIn.setOnClickListener(this);
		goIn.setVisibility(View.GONE);
		pager = (ViewPager) findViewById(R.id.vp);
		dot1 = (ImageView) findViewById(R.id.image1);
		dot2 = (ImageView) findViewById(R.id.image2);
		dot3 = (ImageView) findViewById(R.id.image3);
		dot4 = (ImageView) findViewById(R.id.image4);
		pageViews = new ArrayList<View>();  
		pageViews.add(getChildView(R.layout.guide_one));
		pageViews.add(getChildView(R.layout.guide_two));
		pageViews.add(getChildView(R.layout.guide_three));
		pageViews.add(getChildView(R.layout.guide_four));
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
        		dot1.setBackgroundResource(R.drawable.stores_dot_on);
        		dot2.setBackgroundResource(R.drawable.stores_dot);
        		dot3.setBackgroundResource(R.drawable.stores_dot);
        		dot4.setBackgroundResource(R.drawable.stores_dot);
        		goIn.setVisibility(View.GONE);
        	}else if(arg0 == 1){
        		dot1.setBackgroundResource(R.drawable.stores_dot);
        		dot2.setBackgroundResource(R.drawable.stores_dot_on);
        		dot3.setBackgroundResource(R.drawable.stores_dot);
        		dot4.setBackgroundResource(R.drawable.stores_dot);
        		goIn.setVisibility(View.GONE);
        	}else if(arg0 == 2){
        		dot1.setBackgroundResource(R.drawable.stores_dot);
        		dot2.setBackgroundResource(R.drawable.stores_dot);
        		dot3.setBackgroundResource(R.drawable.stores_dot_on);
        		dot4.setBackgroundResource(R.drawable.stores_dot);
        		goIn.setVisibility(View.GONE);
        	}else if(arg0 == 3){
        		dot1.setBackgroundResource(R.drawable.stores_dot);
        		dot2.setBackgroundResource(R.drawable.stores_dot);
        		dot3.setBackgroundResource(R.drawable.stores_dot);
        		dot4.setBackgroundResource(R.drawable.stores_dot_on);
        		goIn.setVisibility(View.VISIBLE);
        	}
        }  
    }  
	private View getChildView(int layout){
		View view = LayoutInflater.from(this).inflate(layout, null);
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == goIn){
			new Thread(updateDataRunnable).start();
		}
		super.onClick(v);
	}
	Runnable updateDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			DBHelper dbHelper = DBHelper.getInstance(WelcomeActivity.this);
			ContentValues cv = new ContentValues();
			cv.put("ispassguide", "1");
			cv.put("isselectstore", "0");
			boolean b = dbHelper.update(SDKInfo.TableName, cv, null, null);
			if(!b){
				dbHelper.insert(SDKInfo.TableName, cv);
			}
			handler.sendEmptyMessage(0);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Intent i = new Intent(WelcomeActivity.this, CitySelectActivity.class);
			i.putExtra("tag", 2);
			startActivity(i);
			finish();
			super.handleMessage(msg);
		}
		
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
