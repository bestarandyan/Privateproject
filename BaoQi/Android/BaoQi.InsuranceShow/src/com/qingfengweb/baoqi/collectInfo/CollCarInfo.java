package com.qingfengweb.baoqi.collectInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.qingfengweb.baoqi.insuranceShow.R;

public class CollCarInfo extends Activity implements OnClickListener{
private Button btn1,btn2,btn3;
String get_dizhi=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collcarxian);
		btn1=(Button) findViewById(R.id.car1);
		btn2=(Button) findViewById(R.id.car2);
		btn3=(Button) findViewById(R.id.car3);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		Intent inte=getIntent();
		get_dizhi=inte.getStringExtra("dangqiandizhi");
	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(this,CollectInfoMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.putExtra("putString", get_dizhi);
		if(v == btn1){
			intent.setClass(getApplicationContext(), CommInfoActivity.class);
			intent.putExtra("btnvalue", 1);
			startActivity(intent);
			finish();
		}else if(v == btn2){
			intent.setClass(getApplicationContext(), CommInfoActivity.class);
			intent.putExtra("btnvalue", 2);
			startActivity(intent);
			finish();
		}else if(v == btn3){
			intent.setClass(getApplicationContext(), CommInfoActivity.class);
			intent.putExtra("btnvalue", 3);
			startActivity(intent);
			finish();
		}
		
	}
}
