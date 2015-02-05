package com.qingfengweb.pinhuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qingfengweb.pinhuo.adapter.MainGridViewAdapter;
import com.qingfengweb.pinhuo.datamanage.DBHelper;
import com.qingfengweb.pinhuo.datamanage.MyApplication;

public class MainActivity extends BaseActivity  implements OnClickListener,OnItemClickListener{
	 ViewPager viewPager;
	 LinearLayout dotLayout;
	 GridView gridView;
	 public ArrayList<View> pageViews;
	DBHelper dbHelper = null;
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				dbHelper = DBHelper.getInstance(this);
				setContentView(R.layout.a_main);
				initView();
				initData();
			}
	public void initView(){
		viewPager = (ViewPager) findViewById(R.id.imgLayout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (MyApplication.getInstance().getScreenH()*0.4));
		viewPager.setLayoutParams(params);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.getBackground().setAlpha(180);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setOnItemClickListener(this);
	}
	public ImageView getDot(){
		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 0, 0);
		imageView.setLayoutParams(params);
		imageView.setImageResource(R.drawable.dot1);
		return imageView;
	}
	public void initData(){
		pageViews = new ArrayList<View>(); 
		ImageView imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		imageView.setBackgroundResource(R.drawable.car1);
		pageViews.add(imageView);
		dotLayout.addView(getDot());
		imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		imageView.setBackgroundResource(R.drawable.car2);
		pageViews.add(imageView);
		dotLayout.addView(getDot());
		imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		imageView.setBackgroundResource(R.drawable.car3);
		pageViews.add(imageView);
		dotLayout.addView(getDot());
		imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		imageView.setBackgroundResource(R.drawable.car4);
		pageViews.add(imageView);
		dotLayout.addView(getDot());
		notifyPager();
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.main_ico1));
		map.put("name", "货物跟踪");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.mainico2));
		map.put("name", "城际拼货");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.mainico3));
		map.put("name", "我的");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.mainico4));
		map.put("name", "添加");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.mainico4));
		map.put("name", "nodata");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", BitmapFactory.decodeResource(getResources(), R.drawable.mainico4));
		map.put("name", "nodata");
		list.add(map);
		notifyGridView();
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){//过两秒后返回键按的第一次失效
				backFlag =0;
			}
			super.handleMessage(msg);
		}
		
	};
	public int backFlag = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(backFlag == 0){
				handler.sendEmptyMessageDelayed(1, 2000);
				backFlag = 1;
				Toast.makeText(this, "再按一次，退出56拼货网！", 2000).show();
			}else if(backFlag == 1){
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	MainGridViewAdapter gvAdapter = null;
	private void notifyGridView(){
		gvAdapter = new MainGridViewAdapter(this, list);
		gridView.setAdapter(gvAdapter);
	}
	
	private void notifyPager(){
		viewPager.setAdapter(new pagerAdapter());  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());  
	}
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
	        	}else if(arg0 == 1){
	        	}else if(arg0 == 2){
	        	}else if(arg0 == 3){
	        	}else if(arg0 == 4){
	        	}else if(arg0 == 5){
	        	}
	        }  
	    }  
	public class pagerAdapter extends PagerAdapter{
   	@Override
   	public int getCount() {
   		// TODO Auto-generated method stub
   		return pageViews.size();
   	}

   	@Override
   	public boolean isViewFromObject(View arg0, Object arg1) {
   		// TODO Auto-generated method stub
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
//       	ImageView imageView = (ImageView) pageViews.get(arg1);
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
	@Override
	public void onClick(View v) {
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2 == 2){
			Intent intent  = new Intent(this,MyActivity.class);
			startActivity(intent);
		}
	}

}
