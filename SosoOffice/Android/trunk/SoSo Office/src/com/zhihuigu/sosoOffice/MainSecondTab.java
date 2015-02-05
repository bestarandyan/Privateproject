package com.zhihuigu.sosoOffice;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class MainSecondTab extends ActivityGroup{
	/**
	 * 一个静态的ActivityGroup变量，用于管理本Group中的Activity
	 */
	public static ActivityGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		group = this;
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		//把后退事件交给子Activity处理
		group.getLocalActivityManager()
			.getCurrentActivity().onBackPressed();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showExitDialog();
		}
		return true;
	}
	/**
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process.killProcess(android.os.Process.myPid());
									finish();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//把界面切换放到onResume方法中是因为，从其他选项卡切换回来时，
		//调用搞得是onResume方法
		
		//要跳转的界面
		Intent intent = new Intent(this, StationInLetterActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//把一个Activity转换成一个View
		Window w = group.getLocalActivityManager().startActivity("StationInLetterActivity",intent);
	    View view = w.getDecorView();
	    //把View添加大ActivityGroup中
	    group.setContentView(view);
	}
}
