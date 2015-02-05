package com.chinaLife.claimAssistant.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.Interface.sc_InitInterface;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.adapter.sc_GridViewAdapter;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.content.sc_Contentvalues;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_CountService;
import com.chinaLife.claimAssistant.thread.sc_GetKey;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_ThreadDemo;
import com.chinaLife.claimAssistant.thread.sc_UploadData1;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sqlcrypt.database.ContentValues;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class Sc_MainActivity extends Activity implements sc_InitInterface,OnClickListener{

	/**
	 * 参数声明 tab_tag标识当前tab的位置 i_arryimages 装载tab图标的id
	 */
	private GridView gv;
	public sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	public AlarmManager am;
	public static boolean timeron = true;// 任务开始工作
	public Timer timer = null;
	private Button backBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_main);
		Sc_ExitApplication.getInstance().context = Sc_MainActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_MainActivity.this);
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, Sc_CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		am.cancel(sender);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Sc_MyApplication.getInstance().setSw(dm.widthPixels);
		Sc_MyApplication.getInstance().setSh(dm.heightPixels);
		Sc_MyApplication.getInstance().setMaintoleft((int) getResources().getDimension(R.dimen.sc_main_xx_toleft));
		initView();
		new Thread(runnable2).start();
		initData();

		initViewFun();
		this.startService(new Intent(this, sc_CountService.class));
	}
	TimerTask task1 = new TimerTask() {
		public void run() {
			if(Sc_MyApplication.getInstance().getSelfHelpFlag()!=1){
				//System.out.println("隔了30秒钟");
				if (sc_ThreadDemo.isstart) {
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

	public void initView() {
		// TODO Auto-generated method stub
		gv = (GridView) findViewById(R.id.gv);
		backBtn = (Button) findViewById(R.id.backBtn);
	}

	public void initData() {
		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		SharedPreferences shared = sc_SharedPreferencesinfo.getdata(this);
		String phonenumber = shared.getString("phonenumber", "");
		String platenumber = shared.getString("platenumber", "");
		SharedPreferences.Editor sharedata = this.getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putInt("threaddemo", 1);
		sharedata.commit();
		Sc_MyApplication.getInstance().setPhonenumber(phonenumber);
		Sc_MyApplication.getInstance().setPlatenumber(platenumber);
		Sc_MyApplication.getInstance().setSelfHelpFlag(0);
		Sc_MyApplication.getInstance().setCasedescription_tag(2);
		timer = new Timer();
		if(Sc_MyApplication.getInstance().getTimer()==null){
			Sc_MyApplication.getInstance().setTimer(timer);
			Sc_MyApplication.getInstance().getTimer().schedule(task1, 0,30*1000);
		}

	}

	public void initViewFun() {
		// TODO Auto-generated method stub
		gv.setAdapter(new sc_GridViewAdapter(this, sc_Contentvalues.gvImage));
		gv.setOnItemClickListener(new gvItemListener());
		backBtn.setOnClickListener(this);
	}
	
	
/**
 * 主界面的宫格菜单监听事件
 * @author 刘星星
 *
 */
	class gvItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// gv.setClickable(false);
			// gv.setEnabled(false);
			// TODO 国庆
			switch (arg2) {
			case 0:
				showCallDialog();
				break;
			case 1:
				Sc_MyApplication.getInstance().setSelfHelpFlag(0);
				Intent i1 = new Intent(Sc_MainActivity.this,
						Sc_CaseManageActivity.class);
				startActivity(i1);
				finish();
				break;
			case 2:
				Sc_MyApplication.getInstance().setSelfHelpFlag(0);
				Sc_MyApplication.getInstance().setCasedescription_tag(1);
				Sc_MyApplication.getInstance().setPhoto_tag(2);
				Intent i2 = new Intent(Sc_MainActivity.this,
						Sc_CaseOfOnlyOneActivity.class);
				startActivity(i2);
				finish();
				break;
			case 3:
				Sc_MyApplication.getInstance().setSelfHelpFlag(1);
				Intent i3 = new Intent(Sc_MainActivity.this,
						Sc_CaseManageActivity.class);
				Sc_MainActivity.this.startActivity(i3);
				Sc_MainActivity.this.finish();
				break;
			case 4:
				Sc_MyApplication.getInstance().setSelfHelpFlag(0);
				Intent i4 = new Intent(Sc_MainActivity.this,
						Sc_MessageListActivity.class);
				Sc_MainActivity.this.startActivity(i4);
				Sc_MainActivity.this.finish();

				break;
			case 5:
				Sc_MyApplication.getInstance().setSelfHelpFlag(0);
				Intent i5 = new Intent(Sc_MainActivity.this, Sc_HelpActivity.class);
				Sc_MainActivity.this.startActivity(i5);
				Sc_MainActivity.this.finish();

				break;
			}
		}

	}

	/**
	 * 电话报案提示框函数 @author 刘星星
	 */
	/*public void showCallDialog() {
		AlertDialog.Builder callDailog = new AlertDialog.Builder(
				MainActivity.this);
		callDailog.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("是否拨打95519进行报案？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent intent = new Intent(Intent.ACTION_CALL);
							intent.setData(Uri.parse("tel:95519"));
							startActivity(intent);
						} catch (Exception e) {
							Log.e("SampleApp", "Failed to invoke call", e);
						}
					}
				}).setNegativeButton("否", null).show();
	}*/
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
				} catch (Exception e) {
//					if(Sc_MyApplication.opLogger!=null){
//						Sc_MyApplication.opLogger.error("主界面打电话出错",e);
//					}
//					
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
				} catch (Exception e) {
//					if(Sc_MyApplication.opLogger!=null){
//						Sc_MyApplication.opLogger.error("主界面打电话出错",e);
//					}
					
					Log.e("SampleApp", "Failed to invoke call", e);
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
//			Sc_ExitApplication.getInstance().showExitDialog();
			Sc_ExitApplication.getInstance().exitPro();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { // 创建菜单项，实现附加功能
		super.onCreateOptionsMenu(menu);
		/*MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.menu, menu);*/
		menu.add(0, Menu.FIRST, Menu.FIRST, "主界面");
		menu.add(0, Menu.FIRST+1, Menu.FIRST+1, "退出");
		menu.getItem(0).setIcon(R.drawable.sc_home_tu1);
		menu.getItem(1).setIcon(R.drawable.sc_home_tu2);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)
		case Menu.FIRST:
			Intent i = new Intent(this, Sc_MainActivity.class);
			startActivity(i);
			finish();
			break;
		case Menu.FIRST+1:
//			Sc_ExitApplication.getInstance().showExitDialog();
			Sc_ExitApplication.getInstance().exitPro();
			break;
		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

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
								// TODO Auto-generated method stub
								view.setBackgroundColor(Color.BLACK);
							}

						});
						return view;
					} catch (Exception e) {
//						if(Sc_MyApplication.opLogger!=null){
//							Sc_MyApplication.opLogger.error("主界面出错",e);
//						}
						
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

	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Sc_ExitApplication.getInstance().exitPro();
		}
	}
	
private Runnable runnable2 = new Runnable() {
		
		@Override
		public void run() {
			sc_GetKey.getKey(Sc_MainActivity.this);
			if(isLegendUpdate()){
				getLegendinfo();
			}
		}
	};
	
	/**
	 * 锟斤拷取图锟斤拷锟斤拷锟绞憋拷锟�
	 */
	public boolean isLegendUpdate() {

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("action", "checkoutLegend"));
		params.add(new BasicNameValuePair("updateTime", "getLegendUpdateTime"));
		sc_UploadData1 uploaddata = new sc_UploadData1(Sc_MyApplication.URL + "claim",
				params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		params.clear();
		params = null;
		return dealreponse1(reponse);
	}
	
	/**
	 * 
	 */
	public boolean dealreponse1(String reponse){
		if(reponse!=null&&reponse.equals("1")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 锟斤拷取图锟斤拷锟斤拷锟斤拷斜锟�
	 */
	
	public void getLegendinfo() {
		String updatetime = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select max(updatetime) max_updatetime from legendinfo", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"max_updatetime") != null) {
				updatetime = (selectresult.get(selectresult.size() - 1).get(
						"max_updatetime").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getLegendListByUpdateTime"));

		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("updateTime", updatetime));
		sc_UploadData1 uploaddata = new sc_UploadData1(Sc_MyApplication.URL + "claim",params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		dealreponse2(reponse);
		params.clear();
		params = null;
	}
	
	
	
	
	/**
	 * 
	 */
	public void dealreponse2(String reponse){
		if(reponse.contains("[")){
			Type listType = new TypeToken<LinkedList<sc_LegendInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<sc_LegendInfo> legendinfos = null;
			sc_LegendInfo legendinfo = new sc_LegendInfo();
			ContentValues values = new ContentValues();
			legendinfos = gson.fromJson(reponse, listType);
			if(legendinfos!=null&&legendinfos.size()>0){
				for (Iterator<sc_LegendInfo> iterator = legendinfos
						.iterator(); iterator.hasNext();) {
					legendinfo = (sc_LegendInfo) iterator.next();
					values.clear();
					int legendid = 0;
					try {
						legendid = Integer.parseInt(legendinfo
								.getLegendid());
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(legendid>=1&&legendid<=13){
						continue;
					}
					values.put("legendid", legendinfo.getLegendid());
					values.put("type", legendinfo.getType());
					values.put("code", legendinfo.getCode());
					values.put("legendimageurl", legendinfo.getExampleimage());
					values.put("legendtext", legendinfo.getName());
					values.put("maskimageurl", legendinfo.getMaskimage());
					values.put("masktext", legendinfo.getMaskimagedescription());
					values.put("remark", legendinfo.getRemark());
					values.put("updatetime", legendinfo.getUpdatetime());
					if (sc_DBHelper
							.getInstance()
							.selectRow(
									"select * from legendinfo where legendid = '"
											+ legendinfo.getLegendid()
											+ "'", null).size() <= 0) {
						sc_DBHelper.getInstance()
								.insert("legendinfo", values);
					} else {
						sc_DBHelper.getInstance()
								.update("legendinfo",
										values,
										"legendid = ?",
										new String[] {legendinfo.getLegendid()});
					}
				}
			}
		}
	}
}
