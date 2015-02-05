package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LetterSendActivity extends Activity {

	private Button h_backhomebtn;
	private Button homebtn;
	private Button sendletter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_letter_send);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        sendletter = (Button)findViewById(R.id.send_btn);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(LetterSendActivity.this, InsuranceShowMainActivity.class);
			  	LetterSendActivity.this.startActivity(intent);
			  	LetterSendActivity.this.finish();
			}
		});
        sendletter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(LetterSendActivity.this, LetterActivity.class);
			  	LetterSendActivity.this.startActivity(intent);
			  	LetterSendActivity.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(LetterSendActivity.this, InsuranceShowMainActivity.class);
			  	LetterSendActivity.this.startActivity(intent);
			  	LetterSendActivity.this.finish();
			}
		});
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
			  	Intent intent = new Intent();
			  	intent.setClass(LetterSendActivity.this, LetterActivity.class);
			  	LetterSendActivity.this.startActivity(intent);
			  	LetterSendActivity.this.finish();
		 
	  }
	  return true;
	}
	
}
