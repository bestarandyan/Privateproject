/**
 * 
 */
package com.qingfengweb.lottery.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.adapter.ChargeHistoryAdapter;
import com.qingfengweb.lottery.bean.ChargeHistoryBean;
import com.qingfengweb.lottery.bean.UserBalanceBean;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.view.XListView;
import com.qingfengweb.lottery.view.XListView.IXListViewListener;

/**
 * @author 刘星星
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint({ "HandlerLeak", "ShowToast" })
public class ChargeHistoryActivity extends Activity implements OnClickListener,IXListViewListener{
	private XListView listView;
	private List<Map<String,Object>> list;
	private DBHelper dbHelper;
	TextView userNameTv,balanceTv;
	boolean isToLogin = false;//判断是否去了登陆界面了
	 public String offsetStart = "0";//查询的起始位置
	 public String length = "20";//查询的数据条数
	 public int freshFlag = 0;//上拉下拉标记   2代表上拉   1代表下拉 0代表正常加载
	 private Handler mHandler;
	 String sql = "";//数据查询语句
	 String balance = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_chargehistory);
		findview();
		initData();
		
	}
	Dialog dialog = null;
	@Override
	protected void onResume() {
		dbHelper.setBalance(balanceTv);
		if(NetworkCheck.IsHaveInternet(this)){
				isToLogin = false;
				new Thread(getMoneyRunnable).start();
				new Thread(getListDataRunnable).start();
			}else{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("提示：");
				alert.setIcon(android.R.drawable.ic_dialog_info);
				alert.setMessage("网络未连接，是否查看网络设置？");
				alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			            startActivity(intent);
			            dialog.dismiss();
			            dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}
		super.onResume();
	}
	private void findview(){
		listView = (XListView) findViewById(R.id.listView);
		findViewById(R.id.backBtn).setOnClickListener(this);
		userNameTv = (TextView) findViewById(R.id.userNameTv);
		findViewById(R.id.chongzhiTv).setOnClickListener(this);
		balanceTv = (TextView) findViewById(R.id.myMoneyTv);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
	}
	private void initData(){
		userNameTv.setText(MyApplication.getInstance().getCurrentUserName());
		list = new ArrayList<Map<String,Object>>();
		dbHelper = DBHelper.getInstance(this);
		balance = MyApplication.getInstance().getCurrentBalance();
		if(balance == null || balance.equals("0") || balance.equals("") ){
			balance = "0.00";
		}
		balanceTv.setText("￥"+balance);
		mHandler = new Handler();
		sql = "select * from "+ChargeHistoryBean.tbName +" where username ='"+MyApplication.getInstance().getCurrentUserName()+"' order by create_stmp desc";
		list = dbHelper.selectRow(sql, null);
		if(list!=null && list.size()>0){
			handler.sendEmptyMessage(0);
		}
	}
	/**
	 * 得到充值历史列表数据
	 */
	Runnable getListDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.userTradeHistory(offsetStart, length);
			if(msg.startsWith("[") && msg.length()>4){//获取成功 而且有数据
				JsonData.jsonChargeHistoryData(msg, dbHelper.open());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
			}else if(msg.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(1);
			}else if(msg.equals("0")){//无数据
				handler.sendEmptyMessage(2);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//获取成功 而且有数据
				if(freshFlag == 0){
					notifyAdapter();
				}else if(freshFlag == 1){//下拉
					notifyAdapter();
				}else if(freshFlag == 2){//上拉
					notifyAdapter();
					listView.setSelection(list.size()-2);
				}
			}else if(msg.what == 1){//访问服务器失败
				Toast.makeText(ChargeHistoryActivity.this, "访问服务器失败", 3000).show();
			}else if(msg.what == 2){//无数据
				Toast.makeText(ChargeHistoryActivity.this, "没有更多数据了！！", 3000).show();
				listView.setFooterText();
			}else if(msg.what == 3){//查询余额成功
				balance = MyApplication.getInstance().getCurrentBalance();
				if(balance == null || balance.equals("0") || balance.equals("") ){
					balance = "0.00";
				}
				balanceTv.setText("￥"+balance);
			}else if(msg.what == 4){//刷新完数据之后
				onLoad();
			}else if(msg.what == 102){//token超时 或者错误
				if(!isToLogin){
					isToLogin = true;
					Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(ChargeHistoryActivity.this,LoginActivity.class);
					intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
					startActivity(intent);
					overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
				}
			}
			super.handleMessage(msg);
		}
		
	};
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(DeviceTool.getCurrentTime());
	}
	private void notifyAdapter(){
		ChargeHistoryAdapter adapter  = new ChargeHistoryAdapter(this, list);
		listView.setAdapter(adapter);
	}
	
	/**
	 * 查询余额
	 */
	Runnable getMoneyRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.queryMoney();
			if(msg.startsWith("{") && msg.length()>3){//查询成功
				JsonData.jsonBalanceData(msg, dbHelper.open(),handler);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else{
				
			}
		}
	};
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.chongzhiTv){
			Intent intent = new Intent(this,ChargeMoneyActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onRefresh() {
		if(NetworkCheck.IsHaveInternet(this)){
			freshFlag = 1;
			offsetStart = "0";
			length = "20";
			new Thread(getListDataRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(4);
				}
			}, 1000);
		}else{
			Toast.makeText(this, "未检测到网络，请查看您的网络设置！", 3000).show();
			onLoad();
		}
		
	}

	@Override
	public void onLoadMore() {
		if(NetworkCheck.IsHaveInternet(this)){
			freshFlag = 2;
			offsetStart = list.size()+"";
			length = "10";
			new Thread(getListDataRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(4);
				}
			}, 1000);
		}else{
			Toast.makeText(this, "未检测到网络，请查看您的网络设置！", 3000).show();
			onLoad();
		}
		
	}
}
