package com.qingfengweb.piaoguanjia.orderSystem;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class FeedBackActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_feedback, null);
		addCenterView(view);
		ViewUtils.inject(this);
		tv_title.setText("意见反馈");
		parent.setOnTouchListener(this);
	}
}
