package com.qingfengweb.baoqi.insuranceShow;


import java.io.File;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfoActivity extends Activity {
	
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button pro_btn1;
	private Button pro_btn2;
	private Button pro_btn3;
	
	
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	
	private ImageView info;
	private Button homebtn;
	private Button h_backhomebtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_productinfo);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductInfoActivity.this, InsuranceShowMainActivity.class);
			  	ProductInfoActivity.this.startActivity(intent);
			  	ProductInfoActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductInfoActivity.this, InsuranceShowMainActivity.class);
			  	ProductInfoActivity.this.startActivity(intent);
			  	ProductInfoActivity.this.finish();
			}
		});
		Intent intent = getIntent();
		
		Bundle bundle = intent.getBundleExtra("infos");
		
		MyApplication.getInstance().setBundle(bundle);
		String text1 = bundle.getString("text1");
		String text2 = bundle.getString("text2");
		String text3 = bundle.getString("text4");
		String text4 = bundle.getString("text5");
		
		btn1 = (Button)findViewById(R.id._btn1);
		btn2 = (Button)findViewById(R.id._btn2);
		btn3 = (Button)findViewById(R.id._btn3);
		btn4 = (Button)findViewById(R.id._btn4);
		
		textview1 = (TextView) findViewById(R.id.text1);
		textview2 = (TextView) findViewById(R.id.text2);
		textview3 = (TextView) findViewById(R.id.text3);
		
		
		textview1.setText(text1+text2);
		textview2.setText(text3);
		textview3.setText(text4);
		
		pro_btn1 = (Button)findViewById(R.id.btn_info1);
		pro_btn2 = (Button)findViewById(R.id.btn_info2);
		pro_btn3 = (Button)findViewById(R.id.btn_info3);
		
		info = (ImageView)findViewById(R.id.info);
		
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(getSDPath());
				Intent it = new Intent(Intent.ACTION_VIEW);  
		           Uri uri = Uri.parse(getSDPath()+"/video.mp4");  
		           it.setDataAndType(uri , "video/mp4");  
		          ProductInfoActivity.this.startActivity(it);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("tag", 1);
				intent.setClass(ProductInfoActivity.this, AdvertiseActivity.class);
				ProductInfoActivity.this.startActivity(intent);
				ProductInfoActivity.this.finish();
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProductInfoActivity.this,PlanActivity.class);
				ProductInfoActivity.this.startActivity(intent);
				ProductInfoActivity.this.finish();
			}
		});
		
		btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProductInfoActivity.this,InsureActivity.class);
				ProductInfoActivity.this.startActivity(intent);
				ProductInfoActivity.this.finish();
			}
		});
		
		
		pro_btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag);
				info.setBackgroundResource(R.drawable.product_teise_text);
			}
		});
		pro_btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag);
				info.setBackgroundResource(R.drawable.product_view_text);
			}
		});
		pro_btn3.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag);
				info.setBackgroundResource(R.drawable.product_buy_text);
			}
});
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		 Intent intent = new Intent();
		 intent.setClass(ProductInfoActivity.this, ProductCenter.class);
		 ProductInfoActivity.this.startActivity(intent);
		 ProductInfoActivity.this.finish();
	  }
	  return true;
	}
	
	public String getSDPath(){ 
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在 
		if (sdCardExist) 
		{ 
		sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
		} 
		return sdDir.toString(); 

		}
}
