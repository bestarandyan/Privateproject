package com.qingfengweb.lottery.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.payment.Keys;
import com.qingfengweb.lottery.payment.PaymentMain;
import com.qingfengweb.lottery.payment.Result;
import com.qingfengweb.lottery.payment.Rsa;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;

@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint({ "HandlerLeak", "ShowToast", "SimpleDateFormat" })
public class ChargeMoneyActivity extends Activity implements OnClickListener{
	private EditText moneyTv;
	private ProgressDialog progressdialog;
	private TextView userNameTv,myMoneyTv;
	DBHelper dbHelper = null;
	String balance = "";
	String currentTradeNo = "";//当前订单号
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_chargemoney);
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.chargeBtn).setOnClickListener(this);
		moneyTv = (EditText) findViewById(R.id.moneyEt);
		userNameTv = (TextView) findViewById(R.id.userNameTv);
		myMoneyTv = (TextView) findViewById(R.id.myMoneyTv);
		userNameTv.setText(MyApplication.getInstance().getCurrentUserName());
		dbHelper = DBHelper.getInstance(this);
		balance = MyApplication.getInstance().getCurrentBalance();
		myMoneyTv.setText(balance.length()>0?"￥"+balance:"");
		if (NetworkCheck.IsHaveInternet(this)) {//检查是否有网络连接
			new Thread(getMoneyRunnable).start();//查询余额
		}
		dbHelper.setBalance(myMoneyTv);
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
				handler.sendEmptyMessage(3);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else{
				
			}
		}
	};
	private void showDialog(){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage("正在为您充值！请稍候...");
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	/**
	 * 充值接口
	 */
	Runnable chargeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.userTrade(moneyTv.getText().toString(), "客户端支付宝充值","1");
			if(msg.length()>4){//充值成功
				currentTradeNo = msg;
				handler.sendEmptyMessage(4);
			}else if(msg.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(1);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else {
				handler.sendEmptyMessage(2);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0 ){//充值成功
				progressdialog.dismiss();
				Toast.makeText(ChargeMoneyActivity.this, "充值成功", 3000).show();
				Intent intent = new Intent(ChargeMoneyActivity.this,ChargeHistoryActivity.class);
				startActivity(intent);
			}else if(msg.what == 1){
				progressdialog.dismiss();
				Toast.makeText(ChargeMoneyActivity.this, "访问服务器失败", 3000).show();
			}else if(msg.what == 2){
				progressdialog.dismiss();
				Toast.makeText(ChargeMoneyActivity.this, "充值失败", 3000).show();
			}else if(msg.what == 3){
				balance = MyApplication.getInstance().getCurrentBalance();
				myMoneyTv.setText(balance.length()>0?"￥"+balance:"");
			}else if(msg.what == 4){//获取充值流水号成功
				PaymentMain paymentMain = new PaymentMain(ChargeMoneyActivity.this, mHandler);
				paymentMain.payMoney("656彩票行账户充值", "656彩票行账户充值", moneyTv.getText().toString().trim(),currentTradeNo);
			}else if(msg.what == 5){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String successMsg = RequestServerFromHttp.userTradeSuccess(currentTradeNo);
						if(successMsg.equals("1")){
							handler.sendEmptyMessage(0);
						}else{
							handler.sendEmptyMessage(2);
						}
						
					}
				}).start();
				
//				if(progressdialog!=null && progressdialog.isShowing()){
//					progressdialog.dismiss();
//				}
//				Toast.makeText(ChargeMoneyActivity.this,"充值成功！" ,Toast.LENGTH_SHORT).show();
//				moneyTv.setText("");
			}else if(msg.what == 102){//token超时
				Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(ChargeMoneyActivity.this,LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
			super.handleMessage(msg);
		}
		
	};
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				String resultStr = result.getResult();
				if(resultStr.equals("9000")){
					handler.sendEmptyMessage(5);
				}else{
					if(progressdialog!=null && progressdialog.isShowing()){
						progressdialog.dismiss();
					}
					Toast.makeText(ChargeMoneyActivity.this,"充值未成功！" ,Toast.LENGTH_SHORT).show();
				}
				
			}
				break;
			default:
				break;
			}
		};
	};
	
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.chargeBtn){
			if(moneyTv.getText().toString().equals("")){
				MessageBox.CreateAlertDialog("提示！","请输入充值金额",ChargeMoneyActivity.this);
			}else if (NetworkCheck.IsHaveInternet(this)) {//检查是否有网络连接
//				DeviceTool.disShowSoftKey(getApplicationContext());
				if(MyApplication.getInstance().getCurrentUserName().length()==0){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivity(intent);
					return ;
				}else{
					if(Float.parseFloat(moneyTv.getText().toString())<0.01f){
						Toast.makeText(ChargeMoneyActivity.this, "充值金额不能少于0.01", 3000).show();
					}else{
						showDialog();
						new Thread(chargeRunnable).start();
					}
					
				}
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
			            ChargeMoneyActivity.this.dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ChargeMoneyActivity.this.dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}
		}
	}
}
