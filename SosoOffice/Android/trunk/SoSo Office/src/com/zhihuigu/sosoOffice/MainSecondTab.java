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
	 * һ����̬��ActivityGroup���������ڹ���Group�е�Activity
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
		//�Ѻ����¼�������Activity����
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
		
		//Ҫ��ת�Ľ���
		Intent intent = new Intent(this, StationInLetterActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//��һ��Activityת����һ��View
		Window w = group.getLocalActivityManager().startActivity("StationInLetterActivity",intent);
	    View view = w.getDecorView();
	    //��View��Ӵ�ActivityGroup��
	    group.setContentView(view);
	}
}
