package com.qingfengweb.lottery.activity;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.bean.UserBean;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.fragment.FragmentForLotteryType;
import com.qingfengweb.lottery.fragment.FragmentForUserMain;
import com.qingfengweb.lottery.util.NetworkCheck;
public class MainActivity extends BaseActivity implements OnClickListener{
	DBHelper dbHelper = null;
	public static List<Map<String,Object>> userList;
	public static Map<String,Object> userMap;
	public MainActivity() {
		super(R.string.left_and_right);
	}
	public static TextView titleTv;
	public static ImageButton openUserBtn;
	public static View rightLine,leftLine ;
	public static ImageView topLogoImg;
	public static RelativeLayout topLayout;
	AlertDialog alert;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = DBHelper.getInstance(this);
		setSlidingActionBarEnabled(true);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setContentView(R.layout.a_mainframe);
		findViewById(R.id.openMenuBtn).setOnClickListener(this);
		openUserBtn = (ImageButton) findViewById(R.id.openUserBtn);
		openUserBtn.setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.titleTv);
		rightLine = (View) findViewById(R.id.rightLine);
		leftLine = (View) findViewById(R.id.line1);
		topLogoImg = (ImageView) findViewById(R.id.topLogoImg);
		topLayout = (RelativeLayout) findViewById(R.id.topLayout);
		fragmentManager
		.beginTransaction()
		.add(R.id.content_frame, mainFragment)
		.commit();
		
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		fragmentManager
		.beginTransaction()
		.add(R.id.menu_frame_two, new FragmentForUserMain())
		.commit();
		currentFragment = FragmentForLotteryType.newInstance("CompanyIntrocductionFragment");
//		new ImageLoadFromUrl().loadDrawable(this, (Integer) null, null, null, new CallbackImpl(new ImageView(this)));
	}
	Runnable initDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			initData();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!sm.getMenu().isShown() && !sm.getSecondaryMenu().isShown()){
//				showDialog();
				exitPro();
			}
		}
		return true;
	}
	
	private void showDialog(){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("确定要退出应用程序吗？");
		alerBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.gc();
				finish();
			}
		});
		alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		Dialog dialog = alerBuilder.create();
		dialog.show();
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
		String sql = "";
			sql = "select * from "+UserBean.tbName +" where islastuser=1";
		userList = dbHelper.selectRow(sql, null);
		if(userList!=null && userList.size()>0){
			userMap = userList.get(0);
			MyApplication.getInstance().setCurrentToken(userMap.get("token").toString());
			MyApplication.getInstance().setCurrentUserName(userMap.get("username").toString());
			MyApplication.getInstance().setCurrentPassword(userMap.get("password").toString());
		}
	}
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.openMenuBtn){
			sm.showMenu();
		}else if(v.getId() == R.id.openUserBtn){
			CustomViewAbove.isHaveRight = true;
//			BaseActivity.sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_right);
			sm.showSecondaryMenu();
		}
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
