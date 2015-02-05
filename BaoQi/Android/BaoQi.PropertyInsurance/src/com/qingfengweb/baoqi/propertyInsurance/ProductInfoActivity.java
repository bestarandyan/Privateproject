package com.qingfengweb.baoqi.propertyInsurance;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
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
	private Button pro_btn1;
	private Button pro_btn2;
	private Button pro_btn3;
	private Button pro_btn4;
	private Button pro_btn5;
	
	
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	private TextView textview6;
	
	private ImageView info;
	private ImageView imageico;
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
		
		info = (ImageView)findViewById(R.id.info);
		imageico = (ImageView)findViewById(R.id.image);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductInfoActivity.this, PropertyInsuranceMainActivity.class);
			  	ProductInfoActivity.this.startActivity(intent);
			  	ProductInfoActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductInfoActivity.this, PropertyInsuranceMainActivity.class);
			  	ProductInfoActivity.this.startActivity(intent);
			  	ProductInfoActivity.this.finish();
			}
		});
		Intent intent = getIntent();
		
		Bundle bundle = intent.getBundleExtra("infos");
		
		MyApplication.getInstance().setBundle(bundle);
		String text1 = bundle.getString("text1");
		String text2 = bundle.getString("text2");
		String text3 = bundle.getString("text3");
		String text4 = bundle.getString("text4");
		String text5 = bundle.getString("text5");
		String text6 = bundle.getString("text6");
		int    image = bundle.getInt("image");
		
		btn1 = (Button)findViewById(R.id._btn1);
		
		textview1 = (TextView) findViewById(R.id.text1);
		textview2 = (TextView) findViewById(R.id.text2);
		textview3 = (TextView) findViewById(R.id.text3);
		textview4 = (TextView) findViewById(R.id.text4);
		textview5 = (TextView) findViewById(R.id.text5);
		textview6 = (TextView) findViewById(R.id.text6);
		imageico.setBackgroundResource(image);
		
		
		textview1.setText(text1);
		textview2.setText(text2);
		textview3.setText(text3);
		textview4.setText(text4);
		textview5.setText(text5);
		textview6.setText(text6);
		
		pro_btn1 = (Button)findViewById(R.id.btn_info1);
		pro_btn2 = (Button)findViewById(R.id.btn_info2);
		pro_btn3 = (Button)findViewById(R.id.btn_info3);
		pro_btn4 = (Button)findViewById(R.id.btn_info4);
		pro_btn5 = (Button)findViewById(R.id.btn_info5);

		
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.putExtra("tag", 1);
				intent.setClass(ProductInfoActivity.this, FreeInsuranceActivity.class);
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
				pro_btn4.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn5.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn1.setTextColor(Color.parseColor("#FF6600"));
				pro_btn2.setTextColor(Color.parseColor("#000000"));
				pro_btn3.setTextColor(Color.parseColor("#000000"));
				pro_btn4.setTextColor(Color.parseColor("#000000"));
				pro_btn5.setTextColor(Color.parseColor("#000000"));
				info.setBackgroundResource(R.drawable.productnew01);
			}
		});
		pro_btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn4.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn5.setBackgroundResource(R.drawable.product_view_tag);
				
				pro_btn2.setTextColor(Color.parseColor("#FF6600"));
				pro_btn1.setTextColor(Color.parseColor("#000000"));
				pro_btn3.setTextColor(Color.parseColor("#000000"));
				pro_btn4.setTextColor(Color.parseColor("#000000"));
				pro_btn5.setTextColor(Color.parseColor("#000000"));
				info.setBackgroundResource(R.drawable.productnew02);
			}
		});
		pro_btn3.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn4.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn5.setBackgroundResource(R.drawable.product_view_tag);
				
				pro_btn3.setTextColor(Color.parseColor("#FF6600"));
				pro_btn2.setTextColor(Color.parseColor("#000000"));
				pro_btn1.setTextColor(Color.parseColor("#000000"));
				pro_btn4.setTextColor(Color.parseColor("#000000"));
				pro_btn5.setTextColor(Color.parseColor("#000000"));
				info.setBackgroundResource(R.drawable.productnew03);
			}
		});
		pro_btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pro_btn4.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn5.setBackgroundResource(R.drawable.product_view_tag);
				
				pro_btn4.setTextColor(Color.parseColor("#FF6600"));
				pro_btn2.setTextColor(Color.parseColor("#000000"));
				pro_btn3.setTextColor(Color.parseColor("#000000"));
				pro_btn1.setTextColor(Color.parseColor("#000000"));
				pro_btn5.setTextColor(Color.parseColor("#000000"));
				info.setBackgroundResource(R.drawable.productnew04);
			}
		});
		pro_btn5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pro_btn5.setBackgroundResource(R.drawable.product_view_tag_on);
				pro_btn1.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn2.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn3.setBackgroundResource(R.drawable.product_view_tag);
				pro_btn4.setBackgroundResource(R.drawable.product_view_tag);
				
				pro_btn5.setTextColor(Color.parseColor("#FF6600"));
				pro_btn2.setTextColor(Color.parseColor("#000000"));
				pro_btn3.setTextColor(Color.parseColor("#000000"));
				pro_btn4.setTextColor(Color.parseColor("#000000"));
				pro_btn1.setTextColor(Color.parseColor("#000000"));
				info.setBackgroundResource(R.drawable.productnew05);
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
	
}
