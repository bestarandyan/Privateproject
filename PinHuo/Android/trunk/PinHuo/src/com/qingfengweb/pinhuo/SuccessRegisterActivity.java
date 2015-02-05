package com.qingfengweb.pinhuo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SuccessRegisterActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_success_register);
		findViewById(R.id.approveBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.approveBtn){
			Intent intent = new Intent(this,ApproveDriverActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		super.onClick(v);
	}

}
