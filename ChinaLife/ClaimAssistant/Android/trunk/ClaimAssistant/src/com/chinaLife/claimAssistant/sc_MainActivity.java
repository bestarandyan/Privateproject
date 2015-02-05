package com.chinaLife.claimAssistant;

import java.util.Timer;
import java.util.TimerTask;
import com.chinaLife.Interface.sc_InitInterface;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_CountService;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_ThreadDemo;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.chinaLife.claimAssistant.view.sc_MyCircleView;
import com.chinaLife.claimAssistant.view.sc_NewView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class sc_MainActivity extends Activity implements sc_InitInterface {

	/**
	 * 参数声明 tab_tag标识当前tab的位置 i_arryimages 装载tab图标的id
	 */
//	private GridView gv;
//	public sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	public AlarmManager am;
	public static boolean timeron = true;// 任务开始工作
	public Timer timer = null;
	RelativeLayout centerLayout;
	sc_MyCircleView myCircle = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_main);
		sc_ExitApplication.getInstance().context = sc_MainActivity.this;
		sc_ExitApplication.getInstance().addActivity(sc_MainActivity.this);
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, sc_CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		am.cancel(sender);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		sc_MyApplication.getInstance().setSw(dm.widthPixels);
		sc_MyApplication.getInstance().setSh(dm.heightPixels);
		sc_MyApplication.getInstance().setMaintoleft((int) getResources().getDimension(R.dimen.sc_main_xx_toleft));
		initView();
		initData();
		initViewFun();
		this.startService(new Intent(this, sc_CountService.class));
	}
	
	
	TimerTask task1 = new TimerTask() {
		public void run() {
			if(sc_MyApplication.getInstance().getSelfHelpFlag()!=1){
				//System.out.println("隔了30秒钟");
				if (sc_ThreadDemo.isstart) {
					AlarmManager am = (AlarmManager) sc_MainActivity.this.getSystemService(ALARM_SERVICE);
					Intent intent = new Intent(sc_MainActivity.this, sc_CallAlarm.class);
					PendingIntent sender = PendingIntent.getBroadcast(sc_MainActivity.this, 0, intent, 0);
					am.cancel(sender);
					handler.sendEmptyMessage(1);
				}
			}
		}
	};
	/**
	 * 更新消息条数
	 */
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			sc_ThreadDemo.startMyThread();

		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (timeron) {
					timeron = false;
					initViewFun();
				}
				new Thread(runnable).start();
				break;
			}
		}
	};
	int centerX = 0;
	int centerY = 0;
	int radiusDa = 0;
	int radiusXiao = 0;
	public void initView() {
		centerX = sc_MyApplication.getInstance().getSw()/2;
		if(sc_MyApplication.getInstance().getSw() <= 320){
			centerY = sc_MyApplication.getInstance().getSh()/2-60;
		}else{
			centerY = sc_MyApplication.getInstance().getSh()/2-90;
		}
		radiusDa = centerX-20;
		radiusXiao = sc_MyApplication.getInstance().getSw()/5;
		centerLayout = (RelativeLayout) findViewById(R.id.centerLayout);
		centerLayout.addView(new sc_MyCircleView(this));
		
	}
	public void initData() {
		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
//		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		SharedPreferences shared = sc_SharedPreferencesinfo.getdata(this);
		String phonenumber = shared.getString("phonenumber", "");
		String platenumber = shared.getString("platenumber", "");
		SharedPreferences.Editor sharedata = this.getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putInt("threaddemo", 1);
		sharedata.commit();
		sc_MyApplication.getInstance().setPhonenumber(phonenumber);
		sc_MyApplication.getInstance().setPlatenumber(platenumber);
		sc_MyApplication.getInstance().setSelfHelpFlag(0);
		sc_MyApplication.getInstance().setCasedescription_tag(2);
		timer = new Timer();
		if(sc_MyApplication.getInstance().getTimer()==null){
			sc_MyApplication.getInstance().setTimer(timer);
			sc_MyApplication.getInstance().getTimer().schedule(task1, 0,30*1000);
		}

	}

	public void initViewFun() {
		centerLayout.addView(new sc_NewView(this, centerX, centerY, (radiusDa+radiusXiao)/2));
	}

	public void showCallDialog() {
		Dialog alertDialog = new Dialog(this, R.style.sc_FullScreenDialog);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View reNameView = mLayoutInflater.inflate(R.layout.sc_dialog_phone, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		alertDialog.addContentView(reNameView, params);
		alertDialog.show();
		LinearLayout rb1 = (LinearLayout) reNameView.findViewById(R.id.rb1);
		LinearLayout rb2 = (LinearLayout) reNameView.findViewById(R.id.rb2);
		
		rb1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:95519"));
					startActivity(intent);
				} catch (final Exception e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							sc_LogUtil.sendLog(2, "一键报案调用打电话出现错误："+e.getMessage());
						}
					}).start();
//					if(MyApplication.opLogger!=null){
//						MyApplication.opLogger.error("主界面打电话出错",e);
//					}
					Log.e("SampleApp", "Failed to invoke call", e);
				}
			}
		});
		rb2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:4008695519"));
					startActivity(intent);
				} catch (final Exception e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							sc_LogUtil.sendLog(2, "一键报案调用打电话出现错误："+e.getMessage());
						}
					}).start();
//					if(MyApplication.opLogger!=null){
//						MyApplication.opLogger.error("主界面打电话出错",e);
//					}
					
					Log.e("SampleApp", "Failed to invoke call", e);
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			sc_ExitApplication.getInstance().showExitDialog();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) { // 创建菜单项，实现附加功能
//		super.onCreateOptionsMenu(menu);
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//
//			break;
//		case R.id.menu_exit:
//			sc_ExitApplication.getInstance().showExitDialog();
//			break;
//		default:
//			// 对没有处理的事件，交给父类来处理
//			return super.onOptionsItemSelected(item);
//		}
//		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
//		return true;
//	}

	// 关键代码就是重写Layout类的工厂方法onCreateView，这里对比绘制的View来替换系统中的
	protected void setMenuBackground() {
		this.getLayoutInflater().setFactory(new Factory() {
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {

							public void run() {
								view.setBackgroundColor(Color.BLACK);
							}

						});
						return view;
					} catch (final Exception e) {
//						if(MyApplication.opLogger!=null){
//							MyApplication.opLogger.error("主界面出错",e);
//						}
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "主界面系统自带设置布局出错（setMenuBackground方法）："+e.getMessage());
							}
						}).start();
					}
				}
				return null;
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
