/**
 * 
 */
package com.qingfengweb.lottery.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.fragment.FragmentForLotteryType;
import com.qingfengweb.lottery.fragment.SampleListFragment;
import com.qingfengweb.lottery.payment.PaymentMain;
import com.qingfengweb.lottery.payment.Result;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.view.KeyboardListenRelativeLayout;
import com.qingfengweb.lottery.view.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;

/**
 * @author 刘星星
 *
 */
@SuppressLint("ShowToast")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SumbitSuccessActivity extends Activity implements OnClickListener,IOnKeyboardStateChangedListener{
	TextView infoTv,payMoneyTv,balanceTv;
	LinearLayout infoLayout;
	String number = "";
	String payMoney = "";
	String typeSelect = "";
	String zhuNumber = "";
	String qishu = "";
	String result = "";
	TextView payWayTv;
	CheckBox radioBtn1;
	DBHelper dbHelper = null;
	LinearLayout paywayLayout;
	private PopupWindow selectPopupWindow = null;
	private ProgressDialog progressdialog;//进度条
	public int paywayType = 1;//支付类型 1代表账户余额支付   2代表支付宝支付
	public LinearLayout zhuiBtnLayout;
	public EditText qishuEt,beishuEt;
	public String currentTradeNo = "";
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_submitsuccess);
		findview();
		initData();
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		paywayLayout = (LinearLayout) findViewById(R.id.payWayLayout);
		paywayLayout.setOnClickListener(this);
		findViewById(R.id.payBtn).setOnClickListener(this);
		findViewById(R.id.chargeMoneyTv).setOnClickListener(this);
		infoTv = (TextView) findViewById(R.id.infoTv);
		payMoneyTv = (TextView) findViewById(R.id.payMoneyTv);
		payWayTv = (TextView) findViewById(R.id.payWayTv);
		infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
		balanceTv = (TextView) findViewById(R.id.balanceTv);
		KeyboardListenRelativeLayout layout = (KeyboardListenRelativeLayout) findViewById(R.id.parent);
		layout.setOnKeyboardStateChangedListener(this);
		zhuiBtnLayout = (LinearLayout) findViewById(R.id.zhuiBtnLayout);
		qishuEt = (EditText) findViewById(R.id.qiEt);
		beishuEt = (EditText) findViewById(R.id.beiEt);
		qishuEt.addTextChangedListener(textWatcher);
		beishuEt.addTextChangedListener(textWatcher);
		radioBtn1 = (CheckBox) findViewById(R.id.radioBtn1);
	}
	TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
			if(qishuEt.getText().toString().startsWith("0")){
				qishuEt.setText("1");
			}
			if(beishuEt.getText().toString().startsWith("0")){
				beishuEt.setText("1");
			}
			int qishu = 0;
			int beishu = 0;
			if(qishuEt.getText().toString().trim().length() == 0){
				qishu = 1;
			}else{
				qishu = Integer.parseInt(qishuEt.getText().toString().trim());
			}
			if(beishuEt.getText().toString().trim().length() == 0){
				beishu = 1;
			}else{
				beishu = Integer.parseInt(beishuEt.getText().toString().trim());
			}
			if(qishu>82){
				qishuEt.setText("82");
				qishu = 82;
			}
			if(beishu>99){
				beishuEt.setText("99");
				beishu = 99;
			}
			
			infoTv.setText("总共"+zhuNumber+"注x"+qishu+"期x"+beishu+"倍x2=");
			payMoneyTv.setText("￥"+Integer.parseInt(zhuNumber)*qishu*beishu*2+".00");
			KuaiSanActivity.countPayMoney = Integer.parseInt(zhuNumber)*qishu*beishu*2;
			
		}
	};
	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		View v = LayoutInflater.from(this).inflate(R.layout.payway_select, null);
		int pwidth = paywayLayout.getWidth();
		v.findViewById(R.id.payway1).setOnClickListener(this);
		v.findViewById(R.id.payway2).setOnClickListener(this);
		selectPopupWindow = new PopupWindow(v, pwidth, LayoutParams.WRAP_CONTENT,true);//这里的true很重要，为true时点击原来的控件窗口才会消失
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.border_payway));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}
	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	public void showPopupWindow(){
		if(selectPopupWindow.isShowing()){
			dismiss();
		}else{
			selectPopupWindow.showAsDropDown(paywayLayout, 0, -10);
		}
		
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
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				String resultStr = result.getResult();
				if(resultStr.equals("9000")){
					showDialog("正在通知服务器，请稍候...");
					new Thread(getOrderRunnable).start();
				}else{
					Toast.makeText(SumbitSuccessActivity.this,"支付失败" ,Toast.LENGTH_SHORT).show();
				}
				
			}
				break;
			default:
				break;
			}
		};
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){//支付成功
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
				Toast.makeText(SumbitSuccessActivity.this, "支付成功！", 3000).show();				
				new Thread(getMoneyRunnable).start();//查询余额
//				Message message = new Message();
//				message.arg1 = 2;
//				message.what = 1;
//				SampleListFragment.handler.sendMessage(message);
//				if(!KuaiSanActivity.kuaiSanActivity.isFinishing()){
//					KuaiSanActivity.kuaiSanActivity.finish();
//				}
				finish();
			}else if(msg.what == 2){//支付失败
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
			}else if(msg.what == 3){//获取余额成功，设置余额值
				String balance = MyApplication.getInstance().getCurrentBalance();
				if(balance==null || balance.equals("")|| balance.length()==0){
					balance = "0.00";
				}
				balanceTv.setText("￥"+balance);
			}else if(msg.what == 4){//获取充值流水号成功
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
				//调用支付宝接口
				PaymentMain paymentMain = new PaymentMain(SumbitSuccessActivity.this, mHandler);
				paymentMain.payMoney("656彩票行账户充值", "656彩票行账户充值", KuaiSanActivity.countPayMoney+"",currentTradeNo);
			}else if(msg.what == 5){//失败
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
				Toast.makeText(SumbitSuccessActivity.this, "访问服务器失败", 3000).show();
			}else if(msg.what == 6){//失败
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
				Toast.makeText(SumbitSuccessActivity.this, "支付失败", 3000).show();
			}else if(msg.what == 102){//token超时
				if(progressdialog!=null && progressdialog.isShowing()){
					progressdialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(SumbitSuccessActivity.this,LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onResume() {
		dbHelper.setBalance(balanceTv);
		if(NetworkCheck.IsHaveInternet(this) && MyApplication.getInstance().getCurrentToken().length()>0){
			new Thread(getMoneyRunnable).start();//查询余额
		}
		super.onResume();
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
		number = getIntent().getStringExtra("number");
		payMoney = getIntent().getIntExtra("payMoney",0)+"";
		typeSelect = getIntent().getStringExtra("typeSelect");
		zhuNumber = getIntent().getStringExtra("zhuNumber");
		qishu =  getIntent().getStringExtra("qishu");
		result =  getIntent().getStringExtra("result");
		if(typeSelect.equals("1")){
			typeSelect = "和值";
			String number2 =  number.replace(",", "  ");
			infoLayout.addView(addInfoLayout(number2,typeSelect,zhuNumber,payMoney));
		}else if(typeSelect.equals("2")){
			if(number.contains(";")){//表示肯定既有三同号，也有三同号通选
				String number1 = number.substring(0, number.indexOf(";"));
				number1 = number1.replace(",", "  ");
				String type1 = "三同号单选";
				infoLayout.addView(addInfoLayout(number1,type1,Integer.parseInt(zhuNumber)-1+"",Integer.parseInt(payMoney)-2+""));
				String number2 = "三同号通选";
				String type2 = "三同号通选";
				infoLayout.addView(addInfoLayout(number2,type2,"1","2"));
			}else if(number.equals("三同号通选")){
				String number2 = "三同号通选";
				String type2 = "三同号通选";
				infoLayout.addView(addInfoLayout(number2,type2,"1","2"));
			}else{
				String number1 =  number.replace(",", "  ");
				String type1 = "三同号单选";
				infoLayout.addView(addInfoLayout(number1,type1,zhuNumber+"",payMoney+""));
			}
		}else if(typeSelect.equals("3")){
			typeSelect = "二同号";
			if(number.contains(";")){//代表选中了二同号单选
				if(number.contains("*")){//代表既有二同号单选 又有二同号复选
					String number1 = number.substring(0, number.indexOf(";"));
					number1 = number1.replace(",", "  ");
					number1 = number1.substring(0, number1.length()-1);
					String type1 = "二同号单选";
					int dan = number1.substring(0, number1.length()).split("  ").length;
					infoLayout.addView(addInfoLayout(number1,type1,dan+"",dan*2+""));
					String number2 = number.substring(number.indexOf(";")+1, number.length());
					number2 = number2.replace(",", "  ");
//					number2 = number2+"*";
					String type2 = "二同号复选";
					int shuang = number2.substring(0, number2.length()).split("  ").length;
					infoLayout.addView(addInfoLayout(number2,type2,shuang+"",shuang*2+""));
				}else{
					String number1 =  number.substring(0, number.length()-1).replace(",", "  ");
					String type1 = "二同号单选";
					int dan = number1.substring(0, number1.length()).split("  ").length;
					infoLayout.addView(addInfoLayout(number1,type1,dan+"",dan*2+""));
				}
			}else{//如果没有二同号单选，那选中的肯定就是二同号复选   因为如果没有选择的话  是不会容许进入该界面的
				String number2 =  number.replace(",", "  ");
				String type2 = "二同号复选";
				int shuang = number2.substring(0, number2.length()).split("  ").length;
				infoLayout.addView(addInfoLayout(number2,type2,zhuNumber,payMoney));
			}
		}else if(typeSelect.equals("4")){
			typeSelect = "三不同号";
			String number1 = number.replace(",", "  ");
			infoLayout.addView(addInfoLayout(number1,typeSelect,Integer.parseInt(zhuNumber)+"",Integer.parseInt(payMoney)+""));
		}else if(typeSelect.equals("5")){
			typeSelect = "三连号";
			if(number.contains(";")){
				if(number.contains(",")){
					String number1 = "三连号通选";
					infoLayout.addView(addInfoLayout(number1,number1,"1","2"));
					String number2 = number.substring(number.indexOf(";")+1,number.length()-1);
					number2 = number2.replace(",", "  ");
					String type2 = "三连号单选";
					int shuang = number2.split("  ").length;
					infoLayout.addView(addInfoLayout(number2,type2,shuang+"",shuang*2+""));
				}else{
					String number1 = "三连号通选";
					infoLayout.addView(addInfoLayout(number1,number1,"1","2"));
				}
			}else{
				String number2 = number.substring(0,number.length()-1);
				number2 = number2.replace(",", "  ");
				String type2 = "三连号单选";
				int shuang = number2.split("  ").length;
				infoLayout.addView(addInfoLayout(number2,type2,shuang+"",shuang*2+""));
			}
			
		}else if(typeSelect.equals("6")){
			typeSelect = "二不同号";
			String number1 =  number.replace(",", "  ");
			String type1 = "二不同号";
			infoLayout.addView(addInfoLayout(number1,type1,zhuNumber+"",payMoney+""));
		}
		infoTv.setText("总共"+zhuNumber+"注x"+KuaiSanActivity.countQishu+"期x"+KuaiSanActivity.countBeishu+"倍x2=");
		payMoneyTv.setText("￥"+KuaiSanActivity.countPayMoney+".00");
		qishuEt.setText(KuaiSanActivity.countQishu+"");
		beishuEt.setText(KuaiSanActivity.countBeishu+"");
	}
	public View addInfoLayout(String number,String type,String zhushuStr,String payMoney){
		View view = LayoutInflater.from(this).inflate(R.layout.layout_order, null);
		TextView numberTv = (TextView) view.findViewById(R.id.numberTv);
		TextView typeTv = (TextView) view.findViewById(R.id.typeTv);
		TextView zhushu = (TextView) view.findViewById(R.id.number);
		TextView payMoneyTv = (TextView) view.findViewById(R.id.payMoneyTv);
		numberTv.setText(number);
		typeTv.setText(type);
		zhushu.setText(zhushuStr+"注");
		payMoneyTv.setText("￥"+payMoney+".00");
		
		return view;
	}
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.payWayLayout){//支付方式选择
//			showSelectPayWay();
			initPopuWindow();
			showPopupWindow();
		}else if(v.getId() == R.id.payBtn){//立即支付
			if(!NetworkCheck.IsHaveInternet(this)){
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
			            SumbitSuccessActivity.this.dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SumbitSuccessActivity.this.dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
				return;
			}else if(MyApplication.getInstance().getCurrentUserName().length()==0){
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				return ;
			}else{
				if(paywayType == 1){//余额支付
					if(Integer.parseInt(MyApplication.getInstance().getCurrentBalance())<KuaiSanActivity.countPayMoney){
						Toast.makeText(this, "余额已不足，请先充值！", Toast.LENGTH_LONG).show();
						return ;
					}
					showDialog("正在提交，请稍候...");
					new Thread(getOrderRunnable).start();
				}else if(paywayType == 2){//支付宝支付
//					showDialog("正在提交请求，请稍候...");
					new Thread(chargeRunnable).start();
				}
			}
			
			
		}else if(v.getId() == R.id.payway1){
				dismiss();
				payWayTv.setText("账户余额支付");
				paywayType = 1;
		}else if(v.getId() == R.id.payway2){
				dismiss();
				payWayTv.setText("支付宝客户端支付");
				paywayType = 2;
		}else if(v.getId() == R.id.chargeMoneyTv){
			Intent intent = new Intent(this,ChargeMoneyActivity.class);
			startActivity(intent);
		}
	}
	/**
	 * 获取充值流水
	 */
	Runnable chargeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.userTrade(KuaiSanActivity.countPayMoney+"", "","1");
			if(msg.length()>4){//获取充值流水号成功
				currentTradeNo = msg;
				handler.sendEmptyMessage(4);
			}else if(msg.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(5);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else {
				handler.sendEmptyMessage(6);
			}
		}
	};
	/**
	 * 生成订单
	 */
	Runnable getOrderRunnable = new Runnable() {
	
		@Override
		public void run() {
			String msg = RequestServerFromHttp.getOrder(KuaiSanActivity.countPayMoney+"", //支付金额
					KuaiSanActivity.countQishu>1?"1":"0",//是否追号
					KuaiSanActivity.countQishu>1?KuaiSanActivity.countQishu+"":"0", //追号总期数
					radioBtn1.isChecked()?"1":"0", //中奖后是否停止追号
					KuaiSanActivity.countBeishu+"", qishu,result);
			if(msg.length()>4){
//				MyApplication.getInstance().setCurrentOrder(msg);
//				String successMsg = RequestServerFromHttp.getOrderSuccess(msg);
//				if(successMsg.equals("1")){
					handler.sendEmptyMessage(1);
//				}else {
//					handler.sendEmptyMessage(2);
//				}
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else{
				handler.sendEmptyMessage(4);
			}
		}
	};
	private void showDialog(String msg){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage(msg);
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	@Override
	public void onKeyboardStateChanged(int state) {
		System.out.println("操作软键盘的返回值为===="+state);
		if(state == 1){
//			zhuiBtnLayout.setVisibility(View.VISIBLE);
		}else if(state == -1){
//			zhuiBtnLayout.setVisibility(View.GONE);
		}
	}
}
