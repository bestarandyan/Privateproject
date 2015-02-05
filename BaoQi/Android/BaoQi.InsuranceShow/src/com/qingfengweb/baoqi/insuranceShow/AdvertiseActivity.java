package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

public class AdvertiseActivity extends Activity {
	
	private int tag = 0;
	
	
	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.l_advertise);
		image = (ImageView)findViewById(R.id.image);
		image.setBackgroundResource(R.drawable.product_dianye02);
		Intent intent = getIntent();
		tag = intent.getIntExtra("tag", 0);
	}
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  image.destroyDrawingCache();
		  image = null;
		  if(tag == 0){
			  	Intent intent = new Intent();
			  	intent.setClass(AdvertiseActivity.this, ProductCenter.class);
			  	AdvertiseActivity.this.startActivity(intent);
			  	AdvertiseActivity.this.finish();
		  }else if(tag == 1){
			  	Intent intent = new Intent();
			  	intent.putExtra("infos", MyApplication.getInstance().getBundle());
			  	intent.setClass(AdvertiseActivity.this, ProductInfoActivity.class);
			  	AdvertiseActivity.this.startActivity(intent);
			  	AdvertiseActivity.this.finish();
		  }
		  System.gc();
		 
	  }
	  return true;
	}
}
