package com.qingfengweb.id.biluomiV2;


import com.qingfengweb.id.biluomiV2.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class CommentSuccessActivity extends BaseActivity{
	private Button backBtn;
	private WebView wv;
	private Button dianPing,helpBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_commentsuccess);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		dianPing = (Button) findViewById(R.id.dianping);
		helpBtn = (Button) findViewById(R.id.helpBtn);
		helpBtn.setOnClickListener(this);
		dianPing.setOnClickListener(this);
		if(getIntent().getIntExtra("position",1)==1){
			dianPing.setText("点评化妆师");
		}else{
			dianPing.setText("点评摄影师");
		}
		wv = (WebView) findViewById(R.id.wv);
		wv.setBackgroundColor(Color.parseColor("#F5F5F5"));
//		wv.getBackground().setAlpha(0);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.loadDataWithBaseURL("", "<span style='size:18px;margin-left:20px;'>感谢您对<span style='color:red'>金夫人摄影</span>的支持与督促，我们将根据您的点评意见提升更好的服务，本次点评" +
				"您将获得<span style='color:red'>100</span>个系统赠予积分。</span>", 
				"text/html", "utf-8", "");
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent i = new Intent(this, CommentListActivity.class);
			i.putExtra("position", getIntent().getIntExtra("position",1)==1?2:1);
			startActivity(i);
			finish();
		}else if(v == dianPing){
			Intent i = new Intent(this, CommentListActivity.class);
			i.putExtra("position", getIntent().getIntExtra("position",1)==1?2:1);
			startActivity(i);
			finish();
		}else if(v == helpBtn){
			Intent i = new Intent(this, HelpActivity.class);
			i.putExtra("type", 2);
			i.putExtra("tag", 1);
			startActivity(i);
			finish();
		}
		super.onClick(v);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, CommentMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
