package com.qingfengweb.piaoguanjia.orderSystem;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qingfengweb.piaoguanjia.orderSystem.view.SlipButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SettingActivity extends MainFrameActivity{

	@ViewInject(R.id.parent)
	private View parent;
	private SlipButton button;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_setting, null);
		addCenterView(view);
		ViewUtils.inject(this);
		tv_title.setText("设置");
		parent.setOnTouchListener(this);
		button=(SlipButton) findViewById(R.id.switchButton);
	}


}
