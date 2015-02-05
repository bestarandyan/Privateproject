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
	 * һ����̬��ActivityGroup���������ڹ���Group�е�Activity
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
		//�Ѻ����¼�������Activity����
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
	 * �˳�ϵͳʱ����ʾ���� @author ������
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("��ʾ")
					.setMessage("��ȷ��Ҫ�˳�ϵͳ��")
					.setPositiveButton("ȷ��",
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
							}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
		//�ѽ����л��ŵ�onResume����������Ϊ��������ѡ��л�����ʱ��
		//���ø����onResume����
		Intent intent = new Intent();
		if(MyApplication.getInstance().isRoomBack()){//��Դ����
			MyApplication.getInstance().setRoomBack(false);
			intent.setClass(this, RoomListActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			MainTabActivity.mTabHost.setCurrentTab(0);
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("MainActivity",intent);
//		    View view = w.getDecorView();
//		    MainFirstTab.group.setContentView(view);
		    
		}else if(MyApplication.getInstance().isDemandBack()){//�������
			MyApplication.getInstance().setDemandBack(false);
			intent.setClass(this, DemandManagerActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("id", intent);
//			View view = w.getDecorView();
//			MainFirstTab.group.setContentView(view);
			
			
		}else if(MyApplication.getInstance().isLinkManBack()){//��ϵ��
			intent.setClass(this, LinkmanActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//��һ��Activityת����һ��View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //��View��Ӵ�ActivityGroup��
//		    MainFirstTab.group.setContentView(view);
			intent.putExtra("haveMenu", true);
		    MyApplication.getInstance().setLinkManBack(false);
		}else if(MyApplication.getInstance().isRoomListBack()){//��Դ�б�
			intent.setClass(this, RoomListActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//��һ��Activityת����һ��View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //��View��Ӵ�ActivityGroup��
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setLinkManBack(false);
		}else if(MyApplication.getInstance().isSearchBack()){//��Դ����
			intent.setClass(this, AccurateSearchActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//��һ��Activityת����һ��View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //��View��Ӵ�ActivityGroup��
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setSearchBack(false);
		}else if(MyApplication.getInstance().isPotentialDemandBack()){//Ǳ������
			intent.setClass(this, PotentialDemandActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//��һ��Activityת����һ��View
//			Window w = MainFirstTab.group.getLocalActivityManager()
//					.startActivity("activityid",intent);
//		    View view = w.getDecorView();
//		    //��View��Ӵ�ActivityGroup��
//		    MainFirstTab.group.setContentView(view);
		    MyApplication.getInstance().setPotentialDemandBack(false);
		}else{
		//Ҫ��ת�Ľ���
			intent.setClass(this, MainActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		//��һ��Activityת����һ��View
		Window w = group.getLocalActivityManager().startActivity("MainActivity",intent);
	    View view = w.getDecorView();
	    //��View��Ӵ�ActivityGroup��
	    group.setContentView(view);
	}
	
	
}
