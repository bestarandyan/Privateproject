/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import com.qingfengweb.weddingideas.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author qingfeng
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class ErweimaShareActivity extends BaseActivity{
	TextView weddingName,storeNameTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_erweimashare);
		findViewById(R.id.backBtn).setOnClickListener(this);
		weddingName = (TextView) findViewById(R.id.weddingName);
		weddingName.setText(MainVideoActivity.username);
		storeNameTv = (TextView) findViewById(R.id.storeNameTv);
		storeNameTv.setText("BY  "+LoadingActivity.configList.get(0).get("store_name"));
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		super.onClick(v);
	}
}
