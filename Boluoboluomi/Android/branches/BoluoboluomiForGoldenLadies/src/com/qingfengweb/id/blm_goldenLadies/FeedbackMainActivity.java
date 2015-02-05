package com.qingfengweb.id.blm_goldenLadies;

import com.qingfengweb.id.blm_goldenLadies.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedbackMainActivity extends BaseActivity{
	private Button backBtn ;
	private Button pingtaiBtn,fuwuBtn;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.a_feedbackmain);
			backBtn = (Button) findViewById(R.id.backBtn);
			backBtn.setOnClickListener(this);
			pingtaiBtn = (Button) findViewById(R.id.pingtaiBtn);
			fuwuBtn = (Button) findViewById(R.id.fuwuBtn);
			pingtaiBtn.setOnClickListener(this);
			fuwuBtn.setOnClickListener(this);
		}
		@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		} else if (v == pingtaiBtn) {
			Intent i = new Intent(this, FeedbackForPingdaiActivity.class);
			i.putExtra("type", 1);
			startActivity(i);
		} else if (v == fuwuBtn) {
			Intent i = new Intent(this, FeedbackForPingdaiActivity.class);
			i.putExtra("type", 2);
			startActivity(i);
		}
		super.onClick(v);
	}
}
