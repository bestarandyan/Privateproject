package com.qingfengweb.piaoguanjia.orderSystem;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class OrderDetailActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;
	
	private int type = 0;//0待处理订单，1景点门票订单2酒店订单3自由行订单4跟团游订单
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_orderdetail, null);
		addCenterView(view);
		ViewUtils.inject(this);
		tv_title.setText("订单详情");
		parent.setOnTouchListener(this);
	}
}
