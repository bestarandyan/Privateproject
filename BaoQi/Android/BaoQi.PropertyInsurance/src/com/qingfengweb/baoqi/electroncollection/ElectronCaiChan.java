package com.qingfengweb.baoqi.electroncollection;

import com.qingfengweb.baoqi.collectInfo.CameraPreview;
import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ElectronCaiChan extends Activity{
private Button collection;
private String zhenDizhi=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.electron_caichan);
		collection=(Button) findViewById(R.id.collBtn1);
		 zhenDizhi = getIntent().getStringExtra("dangqiandizhi");	
		collection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ElectronCaiChan.this, CameraPreview.class);
				i.putExtra("btnvalue", 84);
				i.putExtra("commDiZhi", zhenDizhi);
				startActivityForResult(i, 1);
				finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(ElectronCaiChan.this,ElectronMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
