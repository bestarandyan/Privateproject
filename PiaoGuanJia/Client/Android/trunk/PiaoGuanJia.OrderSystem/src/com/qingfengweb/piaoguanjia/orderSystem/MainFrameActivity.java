package com.qingfengweb.piaoguanjia.orderSystem;

import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainFrameActivity extends BaseActivity {
	public LinearLayout include;
	public LinearLayout linear_back;// 返回键背景
	public TextView tv_title;// 标题
	public LinearLayout linear_shoucang;// 收藏背景
	public ImageView iv_shoucang;// 收藏图标
	public LinearLayout linear_phone;// 电话背景
	public LinearLayout linear_home;// 主页背景

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainframe);
		include = (LinearLayout) findViewById(R.id.include);
		linear_back = (LinearLayout) findViewById(R.id.linear_back);
		linear_shoucang = (LinearLayout) findViewById(R.id.linear_shoucang);
		linear_phone = (LinearLayout) findViewById(R.id.linear_phone);
		linear_home = (LinearLayout) findViewById(R.id.linear_home);
		linear_back.setOnTouchListener(this);
		linear_shoucang.setOnTouchListener(this);
		linear_phone.setOnTouchListener(this);
		linear_home.setOnTouchListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_shoucang = (ImageView) findViewById(R.id.iv_shoucang);
	}

	/**
	 * 添加中心布局
	 */
	public void addCenterView(View view) {
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(prarms);
		include.addView(view);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (v == linear_back || v == linear_home || v == linear_phone
					|| v == linear_shoucang) {
				v.setBackgroundColor(Color.GRAY);
				v.getBackground().setAlpha(150);
			}
			// 按下
			break;
		case MotionEvent.ACTION_MOVE:
			// 移动
			break;
		case MotionEvent.ACTION_UP:
			if (v == linear_back || v == linear_home || v == linear_phone
					|| v == linear_shoucang) {
				v.setBackgroundColor(Color.TRANSPARENT);
			}
			// 抬起
			if (v == linear_back) {
				finish();
			} else if (v == linear_home) {
				MyApplication.clearActivity();
			} else if (v == linear_phone) {
				MessageBox.promptTwoDialog("确定要拨打客服电话0000-00000000吗？", this,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Intent phoneIntent = new Intent(
										"android.intent.action.CALL", Uri
												.parse("tel:0000-0000000"));
								// 启动
								startActivity(phoneIntent);
							}
						});
			} else if (v == linear_shoucang) {

			}
			break;
		}
		return super.onTouch(v, event);
	}
}
