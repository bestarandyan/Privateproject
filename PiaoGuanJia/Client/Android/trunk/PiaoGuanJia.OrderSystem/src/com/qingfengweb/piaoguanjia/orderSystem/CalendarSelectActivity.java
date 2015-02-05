package com.qingfengweb.piaoguanjia.orderSystem;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qingfengweb.piaoguanjia.orderSystem.view.KCalendar;

import android.os.Bundle;
import android.view.View;

public class CalendarSelectActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = new KCalendar(this);
		addCenterView(view);
		ViewUtils.inject(this);
		tv_title.setText("选择日期");
		parent.setOnTouchListener(this);
	}
}
