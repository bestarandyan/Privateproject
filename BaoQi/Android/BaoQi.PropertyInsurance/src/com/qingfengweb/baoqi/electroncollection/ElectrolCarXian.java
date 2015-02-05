package com.qingfengweb.baoqi.electroncollection;

import com.qingfengweb.baoqi.collectInfo.CameraPreview;
import com.qingfengweb.baoqi.collectInfo.CollectInfoMainActivity;
import com.qingfengweb.baoqi.collectInfo.CommInfoActivity;
import com.qingfengweb.baoqi.propertyInsurance.PropertyInsuranceMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ElectrolCarXian extends Activity implements OnClickListener{
private Button btn1,btn2,btn3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ecectrol_car);
		btn1=(Button) findViewById(R.id.collBtn1);
		btn2=(Button) findViewById(R.id.collBtn2);
		btn3=(Button) findViewById(R.id.collBtn3);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(ElectrolCarXian.this,ElectronMainActivity.class);
			
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String zhenDizhi = getIntent().getStringExtra("dangqiandizhi");
		if(v == btn1){
			Intent i = new Intent(ElectrolCarXian.this, CameraPreview.class);
			i.putExtra("btnvalue", 81);
			i.putExtra("commDiZhi", zhenDizhi);
			startActivityForResult(i, 1);
			finish();
		}else if(v == btn2){
			Intent i = new Intent(ElectrolCarXian.this, CameraPreview.class);
				i.putExtra("btnvalue", 82);
				i.putExtra("commDiZhi", zhenDizhi);
				startActivityForResult(i, 1);
				finish();
		}else if(v == btn3){
			Intent i = new Intent(ElectrolCarXian.this, CameraPreview.class);
				i.putExtra("btnvalue", 83);
				i.putExtra("commDiZhi", zhenDizhi);
				startActivityForResult(i, 1);
				finish();
		}
	}
}
