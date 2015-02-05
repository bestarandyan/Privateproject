/**
 * 
 */
package com.piaoguanjia.chargeclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;

/**
 * @author ¡ı–«–«
 * @createDate 2013/5/6
 *º”‘ÿ“≥
 */
public class LoadingActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstance().setScreenW(dm.widthPixels);
		MyApplication.getInstance().setScreenH(dm.heightPixels);
		new Thread(runnable).start();
		
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return true;
	}
}
