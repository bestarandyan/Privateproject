package com.qingfengweb.pinhuo;

import com.qingfengweb.pinhuo.datamanage.MyApplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
	public void yaodongView(View v){
		
	}
	public Dialog alertDialog = null;
	public void showCallDialog(final String tell) {
		alertDialog = new Dialog(this, R.style.sc_FullScreenDialog);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View reNameView = mLayoutInflater.inflate(R.layout.dialog_phone, null);
		((TextView)reNameView.findViewById(R.id.tellTv)).setText("是否拨打"+tell+"？");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (MyApplication.getInstance().getScreenW()*0.8),LayoutParams.WRAP_CONTENT);
		alertDialog.addContentView(reNameView, params);
		alertDialog.show();
		Button  rb1 = (Button) reNameView.findViewById(R.id.rb1);
		Button rb2 = (Button) reNameView.findViewById(R.id.rb2);
		
		rb1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					alertDialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+tell));
					startActivity(intent);
				} catch (final Exception e) {
					
				}
			}
		});
		rb2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			return;
			}
		});

	}
}
