package com.qingfengweb.baoqi.collectInfo;

import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class CollJiSunXian extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.colljisunxian);
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
}

