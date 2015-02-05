package com.qingfengweb.baoqi.insuranceShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.ext.IntroduceListAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ScrollView;
import android.widget.Toast;

public class IntroduceActivity extends Activity {
	
	
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	
	private ImageView image1,image2,image3,image4,image5;
	
	
	private ScrollView image;
	
	
	private String[] newstr = {
		"�й�����ȫ�����뿪չ�ۺ����������󵼹���","�й����ٱ��չɷ����޹�˾��������һһ�����ҵ��(A��)"
		,"�й����ٷ�ϴǮ�������������б���","��ǣ�ֹ��� �̶��й����ٶ��滭��Ʒչ��Ļ"
		,"���������Ļ� ֧��ԭ������ ���й����ٱ���������������ѧ��������������ԭ����","�����ͻ�Ȩ�� ������ҵ�·�"
		,"�й����ٷ�ϴǮ�������������б���","�����б���,�����б���-�й��������š�С��ȫ�Ҹ����������Ĵ�����ǩ��"
		,"���١���»���𡱡��������ǡ������������з���","�й���������Ӧ��Ԥ������Ϊ�����������"
		,"�ຣ������ǫ���й����ٶ�ͯ����Ժ��������","�й������ڹ��һ�������¡�ؾٰ조�й��������б���ʮ������䡱���"
	};
	
	private String[] timestr = {
			 "2012-3-27","2012-3-27"
			,"2012-3-15","2012-3-09"
			,"2012-3-05","2012-2-25"
			,"2012-2-20","2012-2-15"
			,"2012-2-09","2012-2-09"
			,"2012-2-05","2012-3-01"
		};
	
	private ListView listView;
	private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>(); // listview�е�������
	private LinearLayout linear;
	
	private int tag = 1;
	private int flag = 1;
	private Button h_backhomebtn;
	private Button homebtn;
	private int[] imagearray={R.drawable.company_about_text,R.drawable.company_service_text
			,R.drawable.company_cooperation_text,R.drawable.company_honor_text,R.drawable.news_view};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_introduce);
        
        
        
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 1);
        
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image1.destroyDrawingCache();
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image1 = null;
			  	image2 = null;
			  	image3 = null;
			  	image4 = null;
			  	image5 = null;
				Intent intent = new Intent();
			  	intent.setClass(IntroduceActivity.this, InsuranceShowMainActivity.class);
			  	IntroduceActivity.this.startActivity(intent);
			  	IntroduceActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image1.destroyDrawingCache();
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image1 = null;
			  	image2 = null;
			  	image3 = null;
			  	image4 = null;
			  	image5 = null;
				Intent intent = new Intent();
			  	intent.setClass(IntroduceActivity.this, InsuranceShowMainActivity.class);
			  	IntroduceActivity.this.startActivity(intent);
			  	IntroduceActivity.this.finish();
			  	System.gc();
			}
		});
        
        listView = (ListView)findViewById(R.id.listview);
        linear = (LinearLayout)findViewById(R.id.linear);
        
        getListItems();
        
        IntroduceListAdapter adapter = new IntroduceListAdapter(this, listItems);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        
        
        
        
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        btn4 = (Button)findViewById(R.id.btn_4);
        btn5 = (Button)findViewById(R.id.btn_5);
        
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        image4 = (ImageView)findViewById(R.id.image4);
        image5 = (ImageView)findViewById(R.id.image5);
        image = (ScrollView) findViewById(R.id.image);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tag = 0;
				linear.setVisibility(View.GONE);
				image.setVisibility(View.VISIBLE);
				try{
		        	image5.setBackgroundResource(imagearray[4]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
		        }
		        image1.destroyDrawingCache();
				image2.destroyDrawingCache();
				image3.destroyDrawingCache();
				image4.destroyDrawingCache();
				image1.destroyDrawingCache();
				
			}
		});
        
        if(flag  == 0){
        	tag = 0;
			linear.setVisibility(View.GONE);
			image.setVisibility(View.VISIBLE);
			try{
	        	image5.setBackgroundResource(imagearray[4]);
	        }catch(OutOfMemoryError e){
	        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
	        }
			image2.destroyDrawingCache();
			image3.destroyDrawingCache();
			image4.destroyDrawingCache();
			image1.destroyDrawingCache();
        }
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tag =1;
				linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				listView.setVisibility(View.VISIBLE);
				image1.setVisibility(View.GONE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				
				
			}
		});
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tag =1;
				linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
				btn2.setBackgroundResource(R.drawable.single_tag01_on);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				
				btn2.setTextColor(Color.BLACK);
				btn1.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				image1.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				try{
		        	image1.setBackgroundResource(imagearray[0]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
		        }
				image2.destroyDrawingCache();
				image3.destroyDrawingCache();
				image4.destroyDrawingCache();
				image5.destroyDrawingCache();				
			}
		});

        btn3.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		tag =1;
        		linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
        		btn3.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				
				btn3.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				
				image2.setVisibility(View.VISIBLE);
				image1.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				
				try{
		        	image2.setBackgroundResource(imagearray[1]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
		        }
				image1.destroyDrawingCache();
				image3.destroyDrawingCache();
				image4.destroyDrawingCache();
				image5.destroyDrawingCache();
        	}
        });
        btn4.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		tag =1;
        		linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
        		btn4.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				
				btn4.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				
				image3.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				try{
		        	image3.setBackgroundResource(imagearray[2]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
		        }
				image2.destroyDrawingCache();
				image1.destroyDrawingCache();
				image4.destroyDrawingCache();
				image5.destroyDrawingCache();
        	}
        });
        btn5.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		tag =1;
        		linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
        		btn5.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				
				btn5.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				
				image4.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				try{
		        	image4.setBackgroundResource(imagearray[3]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(IntroduceActivity.this, "�ڴ治�㣬�����´򿪴����", 4000).show();
		        }
				image2.destroyDrawingCache();
				image3.destroyDrawingCache();
				image1.destroyDrawingCache();
				image5.destroyDrawingCache();
        	}
        });
        
        
        
	}
	
	public List<HashMap<String, String>> getListItems() {
		
		listItems.clear();
		int i ;
		for(i=0;i<newstr.length;i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", newstr[i]);
			map.put("time", timestr[i]);
			listItems.add(map);
		}
		
//		HashMap<String, String> map1 = new HashMap<String, String>();
//		map1.put("content", "�й����ٱ��չɷ����޹�˾��������һһ�����ҵ��(A��)");
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
		  if(tag == 0){
			  tag =1;
				linear.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				listView.setVisibility(View.VISIBLE);
				image1.setVisibility(View.GONE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image1.destroyDrawingCache();
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
		  }else if(tag == 1){
			  	
			  	image1.destroyDrawingCache();
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	
			  
			  	image1 = null;
			  	image2 = null;
			  	image3 = null;
			  	image4 = null;
			  	image5 = null;
			  	
			  	Intent intent = new Intent();
			  	intent.setClass(IntroduceActivity.this, InsuranceShowMainActivity.class);
			  	IntroduceActivity.this.startActivity(intent);
			  	IntroduceActivity.this.finish();
			  	System.gc();
		  }
		 
	  }
	  return true;
	}
}
