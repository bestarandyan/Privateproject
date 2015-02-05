package com.zhihuigu.sosoOffice;

import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;


public class MainFirstTab extends ActivityGroup {
	
	/**
	 * 一个静态的ActivityGroup变量，用于管理本Group中的Activity
	 */
	public static ActivityGroup group;
	public static boolean type = false;
	public static int flag = 0;
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
			
			if(MainFirstTab.group.getCurrentActivity().getClass() == MainActivity.class){
				showExitDialog();
			}else if(MainFirstTab.group.getCurrentActivity().getClass() == AddDemandActivity.class){
				Intent intent = new Intent(this, DemandManagerActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity",intent);
			    View view = w.getDecorView();
			    MainFirstTab.group.setContentView(view);
			}else{
				Intent intent = new Intent(this, MainActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity",intent);
			    View view = w.getDecorView();
			    MainFirstTab.group.setContentView(view);
			}
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
									String userid = "0";
									ContentValues values = new ContentValues();
									values.put("roleid", MyApplication.getInstance().getRoleid());
									DBHelper.getInstance(MainFirstTab.this).update(
											"sososettinginfo",
											values,
											"userid = ?",
											new String[] {userid});
									if (values != null) {
										values.clear();
										values = null;
									}
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
		Intent intent = new Intent();
		if(MyApplication.getInstance().isRoomBack()){//房源管理
			MyApplication.getInstance().setRoomBack(false);
			intent.setClass(this, RoomListActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			MainTabActivity.mTabHost.setCurrentTab(0);
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("MainActivity",intent);
//		    View view = w.getDecorView();
//		    MainFirstTab.group.setContentView(view);
		    
		}else if(MyApplication.getInstance().isDemandBack()){//需求管理
			MyApplication.getInstance().setDemandBack(false);
			intent.setClass(this, DemandManagerActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("id", intent);
//			View view = w.getDecorView();
//			MainFirstTab.group.setContentView(view);
			
			
		}else if(MyApplication.getInstance().isLinkManBack()){//联系人
			intent.setClass(this, LinkmanActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//把一个Activity转换成一个View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //把View添加大ActivityGroup中
//		    MainFirstTab.group.setContentView(view);
			intent.putExtra("haveMenu", true);
		    MyApplication.getInstance().setLinkManBack(false);
		}else if(MyApplication.getInstance().isRoomListBack()){//房源列表
			intent.setClass(this, RoomListActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//把一个Activity转换成一个View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //把View添加大ActivityGroup中
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setLinkManBack(false);
		}else if(MyApplication.getInstance().isSearchBack()){//房源搜索
			intent.setClass(this, AccurateSearchActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//把一个Activity转换成一个View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //把View添加大ActivityGroup中
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setSearchBack(false);
		}else if(MyApplication.getInstance().isPotentialDemandBack()){//潜在需求
			intent.setClass(this, PotentialDemandActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//把一个Activity转换成一个View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //把View添加大ActivityGroup中
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setPotentialDemandBack(false);
		}else{
		//要跳转的界面
			intent.setClass(this, MainActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		//把一个Activity转换成一个View
		Window w = group.getLocalActivityManager().startActivity("MainActivity",intent);
	    View view = w.getDecorView();
	    //把View添加大ActivityGroup中
	    group.setContentView(view);
	}
	
	
}
