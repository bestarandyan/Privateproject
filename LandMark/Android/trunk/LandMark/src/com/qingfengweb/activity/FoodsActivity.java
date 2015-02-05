package com.qingfengweb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.adapter.FoodAdapter;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.database.DataAnalyzing;
import com.qingfengweb.database.MyAppLication;
import com.qingfengweb.network.GetData;

/**
 * Created by 鍒樻槦鏄�on 13-5-23.
 * 浼樻儬鍒稿尯
 */
public class FoodsActivity extends BaseActivity implements OnClickListener{
	private ListView foodListView;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	private String msgString="";/*"{'res':{'res_code':'200','count':34}," +
			"'items':[{'floor_name':'2F','food_tel':'123456789','condition':'三公里'," +
			"'delivery_time2':'12:00-15:00','delivery_time3':'17:00-18:30'," +
			"'delivery_time1':'09:00-10:00','brand_id':'3','food_tel2':'2345678'," +
			"'floor_id':'4','food_pic':'http://222.73.173.53:8003/tmp/a2.png'," +
			"'brand_name':'多样屋','info_id':'70'}]}";*/
	LinearLayout proLayout;
//	public ProgressDialog progressDialog;
	DBHelper dbHelper;
	SQLiteDatabase database;
	public  TextView failText;
	ImageButton leftBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.a_foods);
    	foodListView = (ListView) findViewById(R.id.foodListView);
    	proLayout = (LinearLayout) findViewById(R.id.proLayout);
    	failText = (TextView) findViewById(R.id.connectFailText);
    	failText.setOnClickListener(this);
    	dbHelper = new DBHelper(this);
    	leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
    	new Thread(getData).start();//更新数据
    }
    @Override
    protected void onResume() {
    	
    	super.onResume();
    }
//    private void createProgress(String msg){
//    	progressDialog = new ProgressDialog(this);
//    	progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//    	progressDialog.setCancelable(false);
//    	progressDialog.setCanceledOnTouchOutside(false);
//    	progressDialog.setMessage(msg);
//    	progressDialog.show();
//    }
    Runnable getData = new Runnable() {
		
		@Override
		public void run() {
				list = MyAppLication.getInstant().getFoodList();
				handler.sendEmptyMessage(0);//刷新列表
		    	handler.sendEmptyMessage(3);//开启线程
		}
	};
    	
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyLv();
				if(list.size()!=0){
					failText.setVisibility(View.GONE);
				}
				proLayout.setVisibility(View.GONE);
			}else if(msg.what == 1){
//				dialogWronFun("未检测到网络，请检查网络连接！", FoodsActivity.this);
				if(list.size()!=0){
					failText.setVisibility(View.GONE);
				}else{
					failText.setVisibility(View.VISIBLE);
				}
				proLayout.setVisibility(View.GONE);
			}else if (msg.what == 2) {
//				Toast.makeText(FoodsActivity.this, "加载失败！", 3000).show();
//				showDialog();
				if(list.size()!=0){
					failText.setVisibility(View.GONE);
				}else{
					failText.setVisibility(View.VISIBLE);
				}
				proLayout.setVisibility(View.GONE);
			}else if(msg.what == 3){
				if(list.size()==0){
					proLayout.setVisibility(View.VISIBLE);
				}else{
					proLayout.setVisibility(View.GONE);
				}
				new Thread(getFoodsRunnable).start();
			}
//			if(progressDialog!=null){
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
				synchronized (writeLock) {
					new Thread(getFoodsRunnable).start();
		        }
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
//    protected void onResume() {
//    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
//        	dialogWronFun("未检测到网络，请检查网络连接！", FoodsActivity.this);
//        }
//    };
    private boolean proFlag = false;//false代表显示    不隐藏
    Runnable getFoodsRunnable  = new Runnable() {
		
		@Override
		public void run() {
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
	        	handler.sendEmptyMessage(1);
	        }else {
	        	msgString = GetData.getListData("food");
	        	if(msgString.length()>3){
	        		proFlag = true;
	        		synchronized (writeLock) {
	        			ArrayList<String > strings = DataAnalyzing.analyzingData(msgString);
	        			database = dbHelper.getWritableDatabase();
						DataAnalyzing.jsonFoodData(strings.get(1), FoodsActivity.this,database);
						getDatabaseData();
	        		}
	        	}else {
	        		handler.sendEmptyMessage(2);
				}
				
			}
			
			
		}
	};
	/**
	 * 查询数据库数据
	 */
	@SuppressLint("NewApi")
	public void getDatabaseData(){
		Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseSql.FOOD_TABLENAME_STRING, null, null, null, null, null, null);
		list.clear();
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("delivery_time1", cursor.getString(cursor.getColumnIndex("delivery_time1")));
			map.put("food_tel", cursor.getString(cursor.getColumnIndex("food_tel")));
			map.put("floor_name", cursor.getString(cursor.getColumnIndex("floor_name")));
			map.put("condition", cursor.getString(cursor.getColumnIndex("condition")));
			map.put("delivery_time2", cursor.getString(cursor.getColumnIndex("delivery_time2")));
			map.put("delivery_time3", cursor.getString(cursor.getColumnIndex("delivery_time3")));
			map.put("brand_name", cursor.getString(cursor.getColumnIndex("brand_name")));
			map.put("brand_id", cursor.getString(cursor.getColumnIndex("brand_id")));
			map.put("food_pic", cursor.getString(cursor.getColumnIndex("food_pic")));
			map.put("info_id", cursor.getString(cursor.getColumnIndex("info_id")));
			list.add(map);
		}
		cursor.close();
		handler.sendEmptyMessage(0);
	}
    private void notifyLv(){
    	if(list==null || list.size()==0)return;
    	FoodAdapter adapter = new FoodAdapter(this, list,database);
    	foodListView.setAdapter(adapter);
    }
	@Override
	public void onClick(View v) {
		if(v == failText && failText.getVisibility() == View.VISIBLE){
			if(list.size()==0){
				proLayout.setVisibility(View.VISIBLE);
			}else{
				proLayout.setVisibility(View.GONE);
			}
			failText.setVisibility(View.GONE);
			new Thread(getFoodsRunnable).start();
		}else if(v == leftBtn){
    		MainActivity.mHost.setCurrentTab(0);
    	}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
//			alerBuilder.setTitle("提示！");
//			alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//			alerBuilder.setMessage("确定要退出程序吗？");
//			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					android.os.Process.killProcess(android.os.Process.myPid());
//					finish();
//					System.gc();
//				}
//			});
//			alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					return ;
//				}
//			});
//			Dialog dialog = alerBuilder.create();
//			
//			dialog.show();
			MainActivity.mHost.setCurrentTab(0);
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		database.close();
		database.releaseReference();
		database.releaseMemory();
		super.onDestroy();
	}
}
