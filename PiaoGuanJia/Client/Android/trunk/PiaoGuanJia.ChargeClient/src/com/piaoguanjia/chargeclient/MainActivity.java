package com.piaoguanjia.chargeclient;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnTouchListener{
	private Button mainBtn1,mainBtn2,mainBtn3,mainBtn4;
	private String hasAddString = "";
	private String hasauditString = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		hasAddString = MyApplication.getInstance().getHasAddPerm();
		hasauditString = MyApplication.getInstance().getHasAuditPerm();
		mainBtn1 = (Button) findViewById(R.id.mainBtn1);
		mainBtn2 = (Button) findViewById(R.id.mainBtn2);
		mainBtn3 = (Button) findViewById(R.id.mainBtn3);
		mainBtn4 = (Button) findViewById(R.id.mainBtn4);
		mainBtn1.setOnClickListener(this);
		mainBtn1.setOnTouchListener(this);
		mainBtn2.setOnClickListener(this);
		mainBtn2.setOnTouchListener(this);
		mainBtn3.setOnClickListener(this);
		mainBtn3.setOnTouchListener(this);
		mainBtn4.setOnClickListener(this);
		mainBtn4.setOnTouchListener(this);
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("未检测到网络，请检查您的网络连接！",MainActivity.this);
        }
	}
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
      	alert.show();
      }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Button tv = (Button) v;
		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			tv.setTextColor(getResources().getColor(R.color.main_btn_keydown_text_color));
//			tv.setBackgroundColor(getResources().getColor(R.color.main_btn_keydown_bg_color));
			tv.setBackgroundResource(R.drawable.cz_main_yellow_bg);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
//			tv.setTextColor(getResources().getColor(R.color.main_btn_keyup_text_color));
//			tv.setBackgroundColor(getResources().getColor(R.color.main_btn_keyup_bg_color));
			tv.setBackgroundResource(R.drawable.cz_main_blur_bg);
			if(v == mainBtn1){// ----------------充值
				MyApplication.getInstance().setPhtotPath("");//设置充值图片的初始化路径
				MyApplication.getInstance().setCurrentChargeType(1);//设置充值类型的初始类型
				ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
		        	dialogWronFun("未检测到网络，请检查您的网络连接！",MainActivity.this);
		        }else{
		        	if (hasAddString.equals("1")) {
		        		Intent i = new Intent(this,ChargeMoneyActivity.class);
						startActivity(i);
					}else {
						dialogWronFun("您没有添加充值的权限！",MainActivity.this);
					}
		        }
			}else if(v == mainBtn2){//------------------------审核
				ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
		        	dialogWronFun("未检测到网络，请检查您的网络连接！",MainActivity.this);
		        }else{
		        	if (hasauditString.equals("1")) {
		        		Intent i = new Intent(this,ChargeRecordActivity.class);
						i.putExtra("historyFlag", false);
						startActivity(i);
					}else {
						dialogWronFun("您没有审核充值的权限！",MainActivity.this);
					}
		        }
			}else if(v == mainBtn3){//-----------------------------历史记录
				Intent i = new Intent(this,ChargeRecordActivity.class);
				i.putExtra("historyFlag", true);
				startActivity(i);
			}else if(v == mainBtn4){//退出
				showExitDialog();
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		
		
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
									System.exit(0);
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
	

	
}
