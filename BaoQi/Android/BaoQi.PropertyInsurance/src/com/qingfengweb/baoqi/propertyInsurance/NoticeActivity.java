package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.IntroduceListAdapter;
import com.qingfengweb.baoqi.propertyInsurance.ext.NoticeListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NoticeActivity extends Activity {
	


	private ListView listView;
	private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>(); // listview中的数据项
	private LinearLayout linear;
	private int tag = 0;
	private ImageView image;
	private Button h_backhomebtn;
	private Button homebtn;
	
	
	
	private String[] newstr = {
			"中国内部控制审计报告","中国人寿控股股东及其他关联方占用资源情况专项报告"
			,"中国人寿关于高级管理人员任职的公告","中国人寿独立董事2011年度履职情况报告"
			,"中国人寿2010年年度报告补充公告","中国人寿2011年度内部控制评价报告"
			,"中国人寿第三届监事会第十六次会议决议公告","中国人寿第三届监事会第十七次会议决议公告"
			,"中国人寿H股公告","中国人寿保费收入公告"
			,"中国关联交易公告","中国人寿关于2011年度业绩的提示性公告"
		};
		
		private String[] timestr = {
				 "2012-3-27","2012-3-27"
				,"2012-3-15","2012-3-09"
				,"2012-3-05","2012-2-25"
				,"2012-2-20","2012-2-15"
				,"2012-2-09","2012-2-09"
				,"2012-2-05","2012-3-01"
			};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_notice);
        
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image.destroyDrawingCache();
				image = null;
				Intent intent = new Intent();
			  	intent.setClass(NoticeActivity.this, PropertyInsuranceMainActivity.class);
			  	NoticeActivity.this.startActivity(intent);
			  	NoticeActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image.destroyDrawingCache();
				image =null;
				Intent intent = new Intent();
			  	intent.setClass(NoticeActivity.this, PropertyInsuranceMainActivity.class);
			  	NoticeActivity.this.startActivity(intent);
			  	NoticeActivity.this.finish();
			  	System.gc();
			}
		});
        
        listView = (ListView)findViewById(R.id.listview);
        image = (ImageView)findViewById(R.id.image);
        getListItems();
        
        NoticeListAdapter adapter = new NoticeListAdapter(this, listItems);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        
        
       
        
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				image.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				image.setBackgroundResource(R.drawable.news_view);
				tag = 1;
			}
		});
        
	}
	
	public List<HashMap<String, String>> getListItems() {
		
		listItems.clear();
		int i ;
		for(i=0;i<newstr.length;i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", newstr[i]);
			map.put("ower", "admin");
			map.put("time", timestr[i]);
			listItems.add(map);
		}
//		HashMap<String, String> map1 = new HashMap<String, String>();
//		map1.put("content", "公司将发动机发生的风景克里斯多夫");
//		map1.put("ower", "admin");
//		map1.put("time", "2012-02-07");
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
//		listItems.add(map1);
		
		return listItems;
	}
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  if(tag == 1){
			  tag =0;
			  	listView.setVisibility(View.VISIBLE);
			  	image.setVisibility(View.GONE);
			  	image.destroyDrawingCache();
		  }else if(tag == 0){
			  image.destroyDrawingCache();
			  image =null;
			  Intent intent = new Intent();
			  intent.setClass(NoticeActivity.this, PropertyInsuranceMainActivity.class);
			  NoticeActivity.this.startActivity(intent);
			  NoticeActivity.this.finish();
			  System.gc();
		  }
	  }
	  return true;
	}
}
