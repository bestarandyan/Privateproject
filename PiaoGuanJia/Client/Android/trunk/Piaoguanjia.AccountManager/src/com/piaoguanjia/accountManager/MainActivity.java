package com.piaoguanjia.accountManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity {

	private LinearLayout linearbtn1, linearbtn2, linearbtn3, linearbtn4,
			linearbtn5, linearbtn6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		linearbtn1 = (LinearLayout) findViewById(R.id.linearbtn1);
		linearbtn2 = (LinearLayout) findViewById(R.id.linearbtn2);
		linearbtn3 = (LinearLayout) findViewById(R.id.linearbtn3);
		linearbtn4 = (LinearLayout) findViewById(R.id.linearbtn4);
		linearbtn5 = (LinearLayout) findViewById(R.id.linearbtn5);
		linearbtn6 = (LinearLayout) findViewById(R.id.linearbtn6);
		linearbtn1.setOnTouchListener(mainviewOntouch);
		linearbtn2.setOnTouchListener(mainviewOntouch);
		linearbtn3.setOnTouchListener(mainviewOntouch);
		linearbtn4.setOnTouchListener(mainviewOntouch);
		linearbtn5.setOnTouchListener(mainviewOntouch);
		linearbtn6.setOnTouchListener(mainviewOntouch);
		linearbtn1.setOnClickListener(this);
		linearbtn2.setOnClickListener(this);
		linearbtn3.setOnClickListener(this);
		linearbtn4.setOnClickListener(this);
		linearbtn5.setOnClickListener(this);
		linearbtn6.setOnClickListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		AccountApplication.widthPixels = dm.widthPixels;
		AccountApplication.heightPixels = dm.heightPixels;
		System.out.println("宽：" + dm.widthPixels + "-高：" + dm.heightPixels);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		super.onClick(v);
		if (v == linearbtn1) {// 充值
			// 在你的代码中执行这个动画就行啊
			if ((AccountApplication.permissions & 1) != 1) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			} else {
				i.setClass(this, RechargeActivity.class);
				startActivity(i);
			}
		} else if (v == linearbtn2) {// 审核
			i.setClass(this, CheckManagerActivity.class);
			startActivity(i);
		} else if (v == linearbtn3) {// 充值记录
			i.setClass(this, RechargeRecordsActivity.class);
			startActivity(i);
		} else if (v == linearbtn4) {// 专用账户
			if ((AccountApplication.permissions & 8) != 8) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			} else {
				i.setClass(this, SpecialAccountActivity.class);
				startActivity(i);
			}
		} else if (v == linearbtn5) {// 关机
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).setNegativeButton("取消", null);
			Dialog dialog = callDailog.create();
			dialog.show();
		} else if (v == linearbtn6) {// 额度
			if ((AccountApplication.permissions & 64) != 64) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			} else {
				i.setClass(this, LimitAccountActivity.class);
				startActivity(i);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
