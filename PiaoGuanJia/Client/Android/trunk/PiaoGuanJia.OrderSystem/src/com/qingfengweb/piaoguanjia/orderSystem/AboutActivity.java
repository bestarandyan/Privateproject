package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingfengweb.piaoguanjia.orderSystem.util.CommonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.view.SelectPicPopupWindow;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

public class AboutActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;

	// 自定义的弹出框类
	public SelectPicPopupWindow menuWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// type = getIntent().getIntExtra("type", 0);
		View view = LayoutInflater.from(this).inflate(R.layout.activity_about,
				null);
		addCenterView(view);
		ViewUtils.inject(this);
		tv_title.setText("关于票管家");
		parent.setOnTouchListener(this);
	}

	@OnClick({ R.id.btn_linear1, R.id.btn_linear2, R.id.btn_linear3,
			R.id.btn_linear4 })
	public void btnonClick(View v) {
		if (v.getId() == R.id.btn_linear3) {
			Intent i = new Intent(this, FeedBackActivity.class);
			startActivity(i);
		} else if (v.getId() == R.id.btn_linear1) {
			Intent i = new Intent(this, AdsListActivity.class);
			startActivity(i);
		} else if (v.getId() == R.id.btn_linear4) {
			CommonUtils.hideSoftKeyboard(this);
			// 实例化SelectPicPopupWindow
			menuWindow = new SelectPicPopupWindow(this, null);
			// 显示窗口
			menuWindow.showAtLocation(parent,
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
		}
		super.onClick(v);
	}
}
