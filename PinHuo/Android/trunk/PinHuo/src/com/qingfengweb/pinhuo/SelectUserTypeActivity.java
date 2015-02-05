package com.qingfengweb.pinhuo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectUserTypeActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_user_type);
		findViewById(R.id.typeLayout1).setOnClickListener(this);
		findViewById(R.id.typeLayout2).setOnClickListener(this);
		findViewById(R.id.typeLayout3).setOnClickListener(this);
		findViewById(R.id.backBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.typeLayout1){//货主
			Intent intent = new Intent(this,RegisterActivity.class);
			intent.putExtra("typeUser", "1");
			startActivity(intent);
		}else if(v.getId() == R.id.typeLayout2){//司机
			Intent intent = new Intent(this,RegisterActivity.class);
			intent.putExtra("typeUser", "2");
			startActivity(intent);
		}else if(v.getId() == R.id.typeLayout3){//专线
			Intent intent = new Intent(this,RegisterActivity.class);
			intent.putExtra("typeUser", "3");
			startActivity(intent);
		}else if(v.getId() == R.id.backBtn){
			finish();
		}
		super.onClick(v);
	}

}
