package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class InstructionDemoActivity extends Activity {
	private Button h_backhomebtn;
	private Button homebtn;
	private ImageView image = null;
//    android:background="@drawable/explain_text"
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_instructiondemo);
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        image = (ImageView)findViewById(R.id.image1);
        image.setBackgroundResource(R.drawable.explain_text);
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image.destroyDrawingCache();
				image = null;
				Intent intent = new Intent();
			  	intent.setClass(InstructionDemoActivity.this, InsuranceShowMainActivity.class);
			  	InstructionDemoActivity.this.startActivity(intent);
			  	InstructionDemoActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image.destroyDrawingCache();
				image = null;
				Intent intent = new Intent();
			  	intent.setClass(InstructionDemoActivity.this, InsuranceShowMainActivity.class);
			  	InstructionDemoActivity.this.startActivity(intent);
			  	InstructionDemoActivity.this.finish();
			  	System.gc();
			}
		});
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  image.destroyDrawingCache();
		  image = null;
		 Intent intent = new Intent();
		 intent.setClass(InstructionDemoActivity.this, InstructionActivity.class);
		 InstructionDemoActivity.this.startActivity(intent);
		 InstructionDemoActivity.this.finish();
		 System.gc();
	  }
	  return true;
	}
}
