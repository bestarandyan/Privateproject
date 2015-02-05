package com.chinaLife.claimAssistant.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.adapter.sc_ArticleListAdapter;
import com.chinaLife.claimAssistant.bean.sc_HelpInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;




public class Sc_HelpActivity extends Activity implements OnItemClickListener,OnClickListener{
	private ListView listview = null;
	private Button back;
	public sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉手机上的标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_help);
		Sc_ExitApplication.getInstance().context = Sc_HelpActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_HelpActivity.this);
		initView();
		initData();
		initViewFun();
	}
	private void initView() {
		listview = (ListView)findViewById(R.id.tab4_listview);
		back = (Button)findViewById(R.id.fanhui);
	}

	private void initData() {
		

		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		notifyAdapter();
		progressdialog.setMessage("正在获取帮助信息！");
		progressdialog.show();
		new Thread(runnable).start();
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			selectNotice();
		}
	};
	
	/**
	 * 查询帮助信息
	 */
	private void selectNotice() {
		if (sc_NetworkCheck.IsHaveInternet(this)&&Sc_MyApplication.getInstance().isHelpupdate()) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getHelp"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
//			params.add(new BasicNameValuePair("photoid", "9087"));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 12);
			uploaddata.Post();

		}else{
			progressdialog.dismiss();
		}
	}

	private void initViewFun() {
		listview.setOnItemClickListener(this);
		back.setOnClickListener(this);
	}

	public void notifyAdapter() {
		if(list == null
				||listview == null){
			return;
		}
		getListdata();
		sc_ArticleListAdapter adapter = new sc_ArticleListAdapter(this,list);
		listview.setAdapter(adapter);
	}
	
	private void getListdata() {
		list.clear();
		HashMap<String, String> map = null;
		String sql = "select * from helpinfo";
		List<Map<String, Object>> selectresult = database.selectRow(sql, null);
		for (int i = 0; i < selectresult.size(); i++) {
			map = new HashMap<String, String>();
			map.put("helpid", selectresult.get(i).get("helpid").toString());
			map.put("title", selectresult.get(i).get("title")
					.toString());
			map.put("content", selectresult.get(i).get("content")
					.toString());
			list.add(map);
		}
		database.close();
//		list.clear();
//		String s1 = "假设我买了保险后出国，保险合同是否有效？";
//		String s2 = "在国外发生了保险责任事故，是否可以理赔？";
//		String s3 = "什么是保险激活卡";
//		String s4 = "保险激活卡是保单吗？";
//		String s5 = "作为人寿保险的投保人，需要哪些条件？";
//		String s6 = "“国寿1+N”是什么？";
//		String s7 = "“国寿1+N”具体能给我带来什么保单服务？";
//		String s8 = "“国寿1+N”具体能给我带来什么附加值服务...";
//		String s9 = "国寿鹤卡是什么？";
//		String s10 = "保险激活卡是保单吗？";
//		String s11 = "作为人寿保险的投保人，需要哪些条件？";
//		String s12 = "“国寿1+N”是什么？";
//		String s13 = "“国寿1+N”具体能给我带来什么保单服务？";
//		String s14 = "“国寿1+N”具体能给我带来什么附加值服务...";
//		String s15 = "国寿鹤卡是什么？";
//		list.add(s1);
//		list.add(s2);
//		list.add(s3);
//		list.add(s4);
//		list.add(s5);
//		list.add(s6);
//		list.add(s7);
//		list.add(s8);
//		list.add(s9);
//		list.add(s10);
//		list.add(s11);
//		list.add(s12);
//		list.add(s13);
//		list.add(s14);
//		list.add(s15);
		
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Intent intent = new Intent();
			intent.setClass(Sc_HelpActivity.this, Sc_MainActivity.class);
			Sc_HelpActivity.this.startActivity(intent);
			Sc_HelpActivity.this.finish();
		}else if(KeyEvent.KEYCODE_MENU == keyCode){
			return false;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.putExtra("content", list.get(arg2).get("content").toString());
		intent.putExtra("title", list.get(arg2).get("title").toString());
		intent.setClass(Sc_HelpActivity.this, Sc_ArticleidContent.class);
		Sc_HelpActivity.this.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v==back){
			Intent intent = new Intent();
			intent.setClass(Sc_HelpActivity.this, Sc_MainActivity.class);
			Sc_HelpActivity.this.startActivity(intent);
			Sc_HelpActivity.this.finish();
		}
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
//			Intent i = new Intent(this,sc_MainActivity.class);
//			startActivity(i);
//			finish();
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
								// TODO Auto-generated method stub
								view.setBackgroundColor(Color.BLACK);
							}

						});
						return view;
					} catch (final Exception e) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "帮助界面出现错误："+e.getMessage());
							}
						}).start();
//						if(MyApplication.opLogger!=null){
//							MyApplication.opLogger.error("自助查勘界面出错",e);
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
		if(list !=null){
			list.clear();
			list =null;
		}
	}
	
	public void parseJson() {

		Type listType = new TypeToken<LinkedList<sc_HelpInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_HelpInfo> helpinfos = null;
		sc_HelpInfo helpinfo = null;
		helpinfos = gson.fromJson(Sc_MyApplication.getInstance().getResponseword(), listType);
		for (Iterator<sc_HelpInfo> iterator = helpinfos.iterator(); iterator
				.hasNext();) {
			helpinfo = (sc_HelpInfo) iterator.next();
			values.put("helpid", helpinfo.getHelpid());
			values.put("title", helpinfo.getTitle());
			values.put("content", helpinfo.getContent());
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(
							"select * from helpinfo where helpid = '"
									+ helpinfo.getHelpid() + "'", null);

			if (selectresult.size() <= 0) {
				sc_DBHelper.getInstance().insert("helpinfo", values);
			} else {
				sc_DBHelper.getInstance().update("helpinfo", values,
						"helpid = ?",
						new String[] { helpinfo.getHelpid() });

			}
			values.clear();
		}
		values = null;
		sc_DBHelper.getInstance().close();
		notifyAdapter();
		Sc_MyApplication.getInstance().setHelpupdate(false);
	}


}
