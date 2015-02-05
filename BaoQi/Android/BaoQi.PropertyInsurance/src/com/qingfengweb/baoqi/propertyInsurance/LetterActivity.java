package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.EmailListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class LetterActivity extends Activity {

	private ListView listview;
	private List<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>(); // listview中的数据项
	private String[] str;
	private String[] str1 = {
		"本月佣金核对（公司财务部）"
		,"王丽，麻烦帮我看下这个计划书"
		,"营销二部会议通知"
		,"市公司表彰大会的通知"
	};
	private String[] str2 = {
			"下午是否有空，帮我拜访一下这个客户"
			,"营销二部会议通知"
			,"市公司表彰大会的通知"
			,"小丽，明天帮我把客户合同带过来"
		};
	
	
	
	private Button shoujian,fasong;
	private Button xiexin;
	
	
	private Button btn01,btn02,btn03,btn04,btn05,btn06,btn07,btn08;
	private Button h_backhomebtn;
	private Button homebtn;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_letter);
		
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(LetterActivity.this, PropertyInsuranceMainActivity.class);
			  	LetterActivity.this.startActivity(intent);
			  	LetterActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(LetterActivity.this, PropertyInsuranceMainActivity.class);
			  	LetterActivity.this.startActivity(intent);
			  	LetterActivity.this.finish();
			}
		});
		
		
		listview = (ListView)findViewById(R.id.listview);
		fasong = (Button)findViewById(R.id.fasong_btn);
		shoujian = (Button)findViewById(R.id.shoujian_btn);
		xiexin = (Button)findViewById(R.id.btn_write);
		
		
		btn01 = (Button)findViewById(R.id.btn01);
		btn05 = (Button)findViewById(R.id.btn05);
		
		
		btn01.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LetterActivity.this, LetterSendActivity.class);
				LetterActivity.this.startActivity(intent);
				LetterActivity.this.finish();
			}
		});
		btn05.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LetterActivity.this, LetterSendActivity.class);
				LetterActivity.this.startActivity(intent);
				LetterActivity.this.finish();
			}
		});
		
		
		xiexin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LetterActivity.this, LetterSendActivity.class);
				LetterActivity.this.startActivity(intent);
				LetterActivity.this.finish();
			}
		});
		fasong.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				str = str2;
				notifyadapter();
			}
		});
		
		shoujian.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				str = str1;
				notifyadapter();
			}
		});
		str = str1;
		
		notifyadapter();
		
	}
	
	
	public void notifyadapter(){
		getListItems();
		EmailListAdapter adapter = new EmailListAdapter(this, listItems);
		listview.setAdapter(adapter);
	}
	
	public List<HashMap<String, Object>> getListItems() {
		listItems.clear();
		HashMap<String, Object> map1 = new  HashMap<String, Object>();
		map1.put("text", str[0]);
		map1.put("email", BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.email_open)));
		listItems.add(map1);
		
		
		HashMap<String, Object> map2 = new  HashMap<String, Object>();
		map2.put("text", str[1]);
		map2.put("email", BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.email_close)));
		listItems.add(map2);
		
		HashMap<String, Object> map3 = new  HashMap<String, Object>();
		map3.put("text", str[2]);
		map3.put("email", BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.email_close)));
		listItems.add(map3);
		
		HashMap<String, Object> map4 = new  HashMap<String, Object>();
		map4.put("text", str[1]);
		map4.put("email", BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.email_close)));
		listItems.add(map4);
		listItems.add(map1);
		return listItems;
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
			  	Intent intent = new Intent();
			  	intent.setClass(LetterActivity.this, PropertyInsuranceMainActivity.class);
			  	LetterActivity.this.startActivity(intent);
			  	LetterActivity.this.finish();
		 
	  }
	  return true;
	}
	
	
}
