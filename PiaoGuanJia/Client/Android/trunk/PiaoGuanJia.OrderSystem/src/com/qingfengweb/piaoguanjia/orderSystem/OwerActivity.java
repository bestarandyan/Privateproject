package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class OwerActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;

	@ViewInject(R.id.linear_btn1)
	private LinearLayout linear_btn1;
	@ViewInject(R.id.linear_btn2)
	private LinearLayout linear_btn2;
	@ViewInject(R.id.linear_btn3)
	private LinearLayout linear_btn3;
	@ViewInject(R.id.linear_btn4)
	private LinearLayout linear_btn4;
	@ViewInject(R.id.linear_btn5)
	private LinearLayout linear_btn5;
	@ViewInject(R.id.linear_btn6)
	private LinearLayout linear_btn6;
	@ViewInject(R.id.linear_btn7)
	private LinearLayout linear_btn7;
	@ViewInject(R.id.linear_btn8)
	private LinearLayout linear_btn8;
	@ViewInject(R.id.linear_btn9)
	private LinearLayout linear_btn9;
	@ViewInject(R.id.linear_btn10)
	private LinearLayout linear_btn10;
	@ViewInject(R.id.linear_btn11)
	private LinearLayout linear_btn11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.activity_ower,
				null);
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initview();
	}

	private void initview() {
		tv_title.setText("我的票管家");
	}

	@OnClick({ R.id.linear_btn1, R.id.linear_btn2, R.id.linear_btn3,
			R.id.linear_btn4, R.id.linear_btn5, R.id.linear_btn6,
			R.id.linear_btn7, R.id.linear_btn8, R.id.linear_btn9,
			R.id.linear_btn10, R.id.linear_btn11 })
	public void onClick(View v) {
		if (v == linear_btn1) {// 我的账户
			Intent i = new Intent(this, OwerAccountActivity.class);
			startActivity(i);
		} else if (v == linear_btn2) {// 我的收藏
			Intent i = new Intent(this, NewProductActivity.class);
			i.putExtra("type", 5);
			startActivity(i);
		} else if (v == linear_btn3) {// 我的游玩人
			Intent i = new Intent(this, ContactActivity.class);
			startActivity(i);
		} else if (v == linear_btn4) {// 待支付
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 0);
			startActivity(i);
		} else if (v == linear_btn5) {// 待处理订单
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 0);
			startActivity(i);
		} else if (v == linear_btn6) {// 景点门票订单
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 1);
			startActivity(i);
		} else if (v == linear_btn7) {// 酒店订单
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 2);
			startActivity(i);
		} else if (v == linear_btn8) {// 自由行订单
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 3);
			startActivity(i);
		} else if (v == linear_btn9) {// 跟团游订单
			Intent i = new Intent(this, OrderListActivity.class);
			i.putExtra("type", 4);
			startActivity(i);
		} else if (v == linear_btn10) {// 设置
			Intent i = new Intent(this, SettingActivity.class);
			i.putExtra("type", 4);
			startActivity(i);
		} else if (v == linear_btn11) {// 关于票管家
			Intent i = new Intent(this, AboutActivity.class);
			i.putExtra("type", 4);
			startActivity(i);
		}
		super.onClick(v);
	}
}
