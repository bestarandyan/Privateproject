package com.qingfengweb.piaoguanjia.orderSystem;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

	@ViewInject(R.id.parent)
	private View parent;
	
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initview();
	}
	private void initview() {
		tv_title.setText("我的票管家");
	}
}
