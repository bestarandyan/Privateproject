package com.qingfengweb.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingfengweb.adapter.CuXiaoAdapter;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.database.DataAnalyzing;
import com.qingfengweb.database.MyAppLication;
import com.qingfengweb.network.GetData;

/**
 * Created by 
 */
@SuppressLint("HandlerLeak")
public class CuXiaoActivity extends BaseActivity implements View.OnClickListener,OnItemClickListener{
	View v = null;
	private ListView listView;
	private String msgString = "";
	public static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	ImageButton leftBtn;
	LinearLayout proLayout;
	DBHelper dbHelper;
	SQLiteDatabase database;
	public boolean loading = false;//判断是否已经再加载或者已经加载过。
	ProgressBar progressBar;
	TextView proText;
	public TextView failText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.a_cuxiao);
    	findview();
    	dbHelper = new DBHelper(this);
    	getDbData();
    	
    }
    @Override
    protected void onResume() {
//    if(loadData && !loading){
//    	getDbData();
//    	loadData = false;
//    	loading = true;
//    }
    	super.onResume();
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    }
    private void getDbData(){
    	new Thread(getDbRunnable).start();
    }
    
    Runnable getDbRunnable = new Runnable() {
		
		@Override
		public void run() {
			list = MyAppLication.getInstant().getCuxiaoList();
			handler.sendEmptyMessage(0);//这里是刷新列表
			handler.sendEmptyMessage(4);//这里是开启线程。去更新数据
		}
	};
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void findview(){
    	listView = (ListView)findViewById(R.id.cuxiaoListView);
    	progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    	proText = (TextView) findViewById(R.id.stateText);
    	failText = (TextView) findViewById(R.id.connectFailText);
    	failText.setOnClickListener(this);
    	failText.setVisibility(View.GONE);
    	leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
//    	listView.setOnItemClickListener(this);
    	
    	proLayout = (LinearLayout) findViewById(R.id.proLayout);
//    	detailWvView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    	
    }
    public void clearCache(){
    	CuXiaoActivity.this.deleteDatabase("webview.db");
    	CuXiaoActivity.this.deleteDatabase("webviewCache.db");
    }
    Runnable getListRunnable  = new Runnable() {
		
		@Override
		public void run() {
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
	        	handler.sendEmptyMessage(2);//网络未连接
	        }else {
	        	msgString = GetData.getListData("promotion");
				if (msgString.length()>3) {
					proFlag = true;
					getData(msgString);
				}else {
					handler.sendEmptyMessage(1);//访问服务器失败
				}
			}
			
			
		}
	};
	private boolean proFlag = false;//false代表显示    不隐藏
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0 && list.size()>0) {//数据处理成功
				try{
					notifyView();
				}catch(NullPointerException e){
					System.out.println("这里报了空指针异常");
				}
				proLayout.setVisibility(View.GONE);
			}else if (msg.what == 1){//访问服务器失败
//				new Thread(getListRunnable).start();
				if(list==null || list.size()==0){
					failText.setVisibility(View.VISIBLE);
				}else{
						failText.setVisibility(View.GONE);
					notifyView();
				}
				proLayout.setVisibility(View.GONE);
			}else if(msg.what == 2){
//				dialogWronFun("未检测到网络，请检查网络连接！", CuXiaoActivity.this);
				if(list==null || list.size()==0){
					failText.setVisibility(View.VISIBLE);
				}else{
					failText.setVisibility(View.GONE);
					notifyView();
				}
				proLayout.setVisibility(View.GONE);
			}else if(msg.what == 4){//从这里才开始更新数据
				if(list.size()==0){
					proLayout.setVisibility(View.VISIBLE);
				}
				new Thread(getListRunnable).start();
			}else if(msg.what == 5){//更新数据后刷新列表
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notifyView();
				}
				
			}
//			if (progressDialog!=null) {
//				progressDialog.dismiss();
//			}
			if(proFlag){
				proLayout.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}
		
	};
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("提示");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setMessage("可能网络状态不好，数据加载失败，是否重试？");
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				proLayout.setVisibility(View.VISIBLE);
				new Thread(getListRunnable).start();
			}
		});
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		Dialog dialog = alertDialog.create();
		dialog.show();
	}
	private void getData(String msString){
		synchronized (writeLock) {
			ArrayList<String> arrayList = DataAnalyzing.analyzingData(msString);
			database = dbHelper.getWritableDatabase();
			DataAnalyzing.jsonPromotionData(arrayList.get(1), this,database);
			getDatabaseData();
		}
	}
	
	@SuppressLint("NewApi")
	public void getDatabaseData() {
		try{
			database = dbHelper.getReadableDatabase();
			Cursor cursor = database.query(DatabaseSql.CUXIAO_TABLENAME_STRING, 
					null, null, null, null, null, null);
			list.clear();
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", cursor.getString(cursor.getColumnIndex("title")));
				map.put("brand_name", cursor.getString(cursor.getColumnIndex("brand_name")));
				map.put("floor_name", cursor.getString(cursor.getColumnIndex("floor_name")));
				map.put("brand_id", cursor.getString(cursor.getColumnIndex("brand_id")));
				map.put("floor_id", cursor.getString(cursor.getColumnIndex("floor_id")));
				map.put("start_date", cursor.getString(cursor.getColumnIndex("start_date")));
				map.put("end_date", cursor.getString(cursor.getColumnIndex("end_date")));
				map.put("info_id", cursor.getString(cursor.getColumnIndex("info_id")));
				map.put("promotion_type", cursor.getString(cursor.getColumnIndex("promotion_type")));
				map.put("promotion_url", cursor.getString(cursor.getColumnIndex("promotion_url")));
				list.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		handler.sendEmptyMessage(5);
	}
	CuXiaoAdapter adapter = null;
	private void notifyView(){
		 adapter = new CuXiaoAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setCacheColorHint(0);
	}
    @Override
    public void onClick(View v) {
    	if(v == failText){
    		if(v.getVisibility() == View.VISIBLE){
    			proLayout.setVisibility(View.VISIBLE);
    			failText.setVisibility(View.GONE);
    			new Thread(getListRunnable).start();
    		}
    	}else if(v == leftBtn){
    		MainActivity.mHost.setCurrentTab(0);
    	}
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
//			showDialog1();
    		MainActivity.mHost.setCurrentTab(0);
		}
		return true;
	}
    
    private void showDialog1(){
    	AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示！");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("确定要退出程序吗？");
		alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				finish();
				System.gc();
			}
		});
		alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return ;
			}
		});
			Dialog dialog = alerBuilder.create();
			dialog.show();
    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
//        	dialogWronFun("未检测到网络，请检查网络连接！", CuXiaoActivity.this);
//        }else {
//        	detailWvView.setVisibility(View.VISIBLE);
//        	if(list.get(arg2).get("promotion_url").toString().trim().length() == 0){
//        		detailWvView.loadUrl("http://www.baidu.com");
//        	}else {
//        		detailWvView.loadUrl(list.get(arg2).get("promotion_url").toString().trim());
//			}
//    		MyAppLication.getInstant().setBackFlag(2);
//		}
		
	}
	@Override
	protected void onDestroy() {
		database.close();
		database.releaseReference();
		database.releaseMemory();
		super.onDestroy();
	}
}
