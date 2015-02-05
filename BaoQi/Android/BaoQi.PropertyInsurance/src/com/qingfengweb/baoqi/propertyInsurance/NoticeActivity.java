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
	private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>(); // listview�е�������
	private LinearLayout linear;
	private int tag = 0;
	private ImageView image;
	private Button h_backhomebtn;
	private Button homebtn;
	
	
	
	private String[] newstr = {
			"�й��ڲ�������Ʊ���","�й����ٿعɹɶ�������������ռ����Դ���ר���"
			,"�й����ٹ��ڸ߼�������Ա��ְ�Ĺ���","�й����ٶ�������2011�����ְ�������"
			,"�й�����2010����ȱ��油�乫��","�й�����2011����ڲ��������۱���"
			,"�й����ٵ�������»��ʮ���λ�����鹫��","�й����ٵ�������»��ʮ�ߴλ�����鹫��"
			,"�й�����H�ɹ���","�й����ٱ������빫��"
			,"�й��������׹���","�й����ٹ���2011���ҵ������ʾ�Թ���"
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
		//ȥ���ֻ��ϵı�����
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
//		map1.put("content", "��˾�������������ķ羰����˹���");
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
