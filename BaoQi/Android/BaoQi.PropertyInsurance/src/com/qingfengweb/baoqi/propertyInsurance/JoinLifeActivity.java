package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.IntroduceListAdapter;

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

public class JoinLifeActivity extends Activity {
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button h_backhomebtn;
	private Button homebtn;
	private ImageView image1,image2,image3,image4,image5,image6;
	private int[] imagearray={R.drawable.joinlifeimage1,R.drawable.joinlifeimage2
			,R.drawable.joinlifeimage3,R.drawable.joinlifeimage4,R.drawable.joinlifeimage5,R.drawable.joinlifeimage6};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_joinlife);
        
        
        
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        btn4 = (Button)findViewById(R.id.btn_4);
        btn5 = (Button)findViewById(R.id.btn_5);
        btn6 = (Button)findViewById(R.id.btn_6);
        
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
		  		image6.destroyDrawingCache();
		  		image1 = null;
		  		image2 = null;
		  		image3 = null;
		  		image4 = null;
		  		image5 = null;
		  		image6 = null;
				Intent intent = new Intent();
			  	intent.setClass(JoinLifeActivity.this, PropertyInsuranceMainActivity.class);
			  	JoinLifeActivity.this.startActivity(intent);
			  	JoinLifeActivity.this.finish();
			  	
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
		  		image6.destroyDrawingCache();
		  		image1 = null;
		  		image2 = null;
		  		image3 = null;
		  		image4 = null;
		  		image5 = null;
		  		image6 = null;
				Intent intent = new Intent();
			  	intent.setClass(JoinLifeActivity.this, PropertyInsuranceMainActivity.class);
			  	JoinLifeActivity.this.startActivity(intent);
			  	JoinLifeActivity.this.finish();
			  	
			  	System.gc();
			}
		});
        
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        image4 = (ImageView)findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        
        
        image1.setBackgroundResource(imagearray[0]);
	  	image2.destroyDrawingCache();
	  	image3.destroyDrawingCache();
	  	image4.destroyDrawingCache();
	  	image5.destroyDrawingCache();
	  	image6.destroyDrawingCache();
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				image1.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image5.setVisibility(View.GONE);
				image6.setVisibility(View.GONE);
				
				image1.setBackgroundResource(imagearray[0]);
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image6.destroyDrawingCache();
				
				
			}
		});
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn2.setBackgroundResource(R.drawable.single_tag01_on);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn2.setTextColor(Color.BLACK);
				btn1.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				image2.setVisibility(View.VISIBLE);
				image1.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image5.setVisibility(View.GONE);
				image6.setVisibility(View.GONE);
				
				
				image2.setBackgroundResource(imagearray[1]);
			  	image1.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image6.destroyDrawingCache();
			}
		});

        btn3.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		btn3.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn3.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				image3.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image5.setVisibility(View.GONE);
				image6.setVisibility(View.GONE);
				
				
				image3.setBackgroundResource(imagearray[2]);
			  	image2.destroyDrawingCache();
			  	image1.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image6.destroyDrawingCache();
        	}
        });
        btn4.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		btn4.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn4.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				image4.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				image5.setVisibility(View.GONE);
				image6.setVisibility(View.GONE);
				
				image4.setBackgroundResource(imagearray[3]);
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image1.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image6.destroyDrawingCache();
        	}
        });
        btn5.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		btn5.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn6.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn5.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn6.setTextColor(Color.WHITE);
				image5.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				image6.setVisibility(View.GONE);
				
				
				
				image5.setBackgroundResource(imagearray[4]);
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image1.destroyDrawingCache();
			  	image6.destroyDrawingCache();
        	}
        });
        
        btn6.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		btn6.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				
				
				btn6.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				image6.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				image4.setVisibility(View.GONE);
				image5.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				
				
				image6.setBackgroundResource(imagearray[5]);
			  	image2.destroyDrawingCache();
			  	image3.destroyDrawingCache();
			  	image4.destroyDrawingCache();
			  	image5.destroyDrawingCache();
			  	image1.destroyDrawingCache();
        	}
        });
        
	}
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  
		  		image1.destroyDrawingCache();
		  		image2.destroyDrawingCache();
		  		image3.destroyDrawingCache();
		  		image4.destroyDrawingCache();
		  		image5.destroyDrawingCache();
		  		image6.destroyDrawingCache();
		  		image1 = null;
		  		image2 = null;
		  		image3 = null;
		  		image4 = null;
		  		image5 = null;
		  		image6 = null;
			  	Intent intent = new Intent();
			  	intent.setClass(JoinLifeActivity.this, PropertyInsuranceMainActivity.class);
			  	JoinLifeActivity.this.startActivity(intent);
			  	JoinLifeActivity.this.finish();
			  	System.gc();
		 
	  }
	  return true;
	}
}
