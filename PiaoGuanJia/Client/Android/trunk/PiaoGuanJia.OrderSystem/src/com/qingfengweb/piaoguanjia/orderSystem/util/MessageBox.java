package com.qingfengweb.piaoguanjia.orderSystem.util;

import com.qingfengweb.piaoguanjia.orderSystem.view.CustomerDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MessageBox {
	/**
	 * 单纯的提示
	 * */
	public static void promptDialog(String msg, Context context) {
		Dialog dialog = null;
		CustomerDialog.Builder customBuilder = new CustomerDialog.Builder(
				context);
		customBuilder.setTitle("提示").setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog = customBuilder.create();
		dialog.show();

	}

	/**
	 * 提示，点击确认执行某些动作
	 * */
	public static void promptDialog(String msg, Context context,
			DialogInterface.OnClickListener clicklistener) {
		Dialog dialog = null;
		CustomerDialog.Builder customBuilder = new CustomerDialog.Builder(
				context);
		customBuilder.setTitle("提示").setMessage(msg)
				.setPositiveButton("确定", clicklistener);
		dialog = customBuilder.create();
		dialog.show();

	}

	/**
	 * 有取消有确定 确定有事件，取消无时间
	 * */
	/**
	 * 提示，点击确认执行某些动作
	 * */
	public static void promptTwoDialog(String msg, Context context,
			DialogInterface.OnClickListener clicklistener) {
		Dialog dialog = null;
		CustomerDialog.Builder customBuilder = new CustomerDialog.Builder(
				context);
		customBuilder.setTitle("提示").setMessage(msg)
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", clicklistener);
		dialog = customBuilder.create();
		dialog.show();

	}
	/**
	 * 有取消有确定 确定有事件，取消无时间
	 * */
	/**
	 * 取消，执行某些事件
	 * 提示，点击确认执行某些动作
	 * */
	public static void promptTwoDialog(String msg, Context context,
			DialogInterface.OnClickListener clicklistener1,DialogInterface.OnClickListener clicklistener) {
		Dialog dialog = null;
		CustomerDialog.Builder customBuilder = new CustomerDialog.Builder(
				context);
		customBuilder.setTitle("提示").setMessage(msg)
				.setNegativeButton("取消", clicklistener1)
				.setPositiveButton("确定", clicklistener);
		dialog = customBuilder.create();
		dialog.show();

	}
}
