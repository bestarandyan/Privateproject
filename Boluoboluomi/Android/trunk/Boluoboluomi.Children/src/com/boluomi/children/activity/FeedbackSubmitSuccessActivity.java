package com.boluomi.children.activity;

import com.boluomi.children.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedbackSubmitSuccessActivity extends BaseActivity{
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_feedbacksubmitsuccess);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
