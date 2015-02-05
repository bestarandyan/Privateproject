package com.qingfengweb.pinhuo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class SuccessApproveActivity extends BaseActivity {
	TextView phoneLayout;
	String phone="13701628838";
	String info = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的认证信息已经上传到服务器，敬请等待系统审核，或拨打<font color='#0000FF' >"+phone+"</font>进行人工审核。 ";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_success_approve);
		phoneLayout = (TextView) findViewById(R.id.phoneLayout);
		phoneLayout.setText(Html.fromHtml(info));
		phoneLayout.setOnClickListener(this);
		findViewById(R.id.approveBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == phoneLayout){
			showCallDialog(phone);
		}else if(v.getId() == R.id.approveBtn){
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		super.onClick(v);
	}

}
