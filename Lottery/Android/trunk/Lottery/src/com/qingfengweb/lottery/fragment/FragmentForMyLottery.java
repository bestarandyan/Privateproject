/**
 * 
 */
package com.qingfengweb.lottery.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.AmendMyInfoActivity;
import com.qingfengweb.lottery.activity.ChargeHistoryActivity;
import com.qingfengweb.lottery.activity.ChargeMoneyActivity;
import com.qingfengweb.lottery.activity.LoginActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.activity.SumbitSuccessActivity;
import com.qingfengweb.lottery.adapter.MyCaiPiaoListViewAdapter;
import com.qingfengweb.lottery.bean.OrderBean;
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
 * @createDate 2013/11/22
 *
 */
@SuppressLint("HandlerLeak")
public class FragmentForMyLottery  extends Fragment implements OnTouchListener,IXListViewListener,OnClickListener,OnItemClickListener{
	private static final String ARG_TEXT = "com.qingfengweb.lottery.fragment";
	 View view = null;
	 XListView listView;
	 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	 TextView allDD,zhongjiangDD,weizhongjiangDD,daikaijiangDD,zhuiHaoDD;
	 LinearLayout allDDLayout,zhongjiangDDLayout,weizhongjiangDDLayout,daikaijiangDDLayout,zhuiHaoDDLayout;
	 TextView[] textArray;
	 LinearLayout topLayout;
	 View lineView;
	 TextView userNameTv;
	 DBHelper dbHelper;
	 TextView myMoneyTv;
	 MyCaiPiaoListViewAdapter adapter = null;
	 public String offsetStart = "0";//查询的起始位置
	 public String length = "50";//查询的数据条数
	 public int freshFlag = 0;//上拉下拉标记   2代表上拉   1代表下拉 0代表正常加载
	 String sql = "";
	 String balance = "";
	 public static FragmentForMyLottery newInstance(String text) {
		  FragmentForMyLottery f = new FragmentForMyLottery();
	      Bundle args = new Bundle();
	      args.putString(ARG_TEXT, text);
	      f.setArguments(args);

      return f;
  }
	 
	 @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		 System.out.println("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			balance = MyApplication.getInstance().getCurrentBalance();
			if(balance == null || balance.equals("0") || balance.equals("") ){
				balance = "0.00";
			}
			myMoneyTv.setText("￥"+balance);
			if(NetworkCheck.IsHaveInternet(getActivity())){
//				new Thread(getMoneyRunnable).start();//查询余额
//				new Thread(getOrderRunnable).start();
				initData();
			}
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onStart() {
		System.out.println("onStart");
		super.onStart();
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
	@Override
	public void onResume() {
		dbHelper.setBalance(myMoneyTv);
		isGoLogin = false;
		super.onResume();
	}
	Dialog dialog = null;
	 private void initData(){
		 dbHelper = DBHelper.getInstance(this.getActivity());
		 sql = "select * from "+OrderBean.tbName+" where username="+MyApplication.getInstance().getCurrentUserName()+" order by create_stmp desc";
		 textArray = new TextView[]{allDD,zhongjiangDD,weizhongjiangDD,daikaijiangDD,zhuiHaoDD};
		 mHandler = new Handler();
		 if(MainActivity.userMap != null){
//			 userNameTv.setText(MainActivity.userMap.get("nick_name").toString());
			 userNameTv.setText(MyApplication.getInstance().getCurrentUserName());
			 String identity = MainActivity.userMap.get("identity_card").toString();
			 if(identity!=null && !identity.equals("null") && identity.length()>0){
				 topLayout.setVisibility(View.GONE);
				 lineView.setVisibility(View.GONE);
			 }else{
				 topLayout.setVisibility(View.VISIBLE);
				 lineView.setVisibility(View.VISIBLE);
			 }
		 }else{
			 
		 }
		 	if(list!=null && list.size()>0){
				list.clear();
			}
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			if(NetworkCheck.IsHaveInternet(this.getActivity())){
				 new Thread(getMoneyRunnable).start();//查询余额
				 new Thread(getOrderRunnable).start();//查询订单
			 }else{
					AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
					alert.setTitle("提示：");
					alert.setIcon(android.R.drawable.ic_dialog_info);
					alert.setMessage("网络未连接，是否查看网络设置？");
					alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
				            startActivity(intent);
				            dialog.dismiss();
				            FragmentForMyLottery.this.dialog.dismiss();
						}
					});
					alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							FragmentForMyLottery.this.dialog.dismiss();
						}
					});
					dialog = alert.create();
					dialog.show();
				}
	 }
	 /**
	  * 查询订单
	  */
	 Runnable getOrderRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.queryOrder(offsetStart, length);
			if(msg.startsWith("[")){//有数据
				JsonData.jsonOrderData(msg, dbHelper.open());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
			}else if(msg.equals("0")){//无数据
				handler.sendEmptyMessage(1);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}
		}
	};
	boolean isGoLogin = false;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//获取订单成功
				if(freshFlag == 0){
					notifyAdapter();
				}else if(freshFlag == 1){//下拉
					notifyAdapter();
				}else if(freshFlag == 2){//上拉
					notifyAdapter();
					listView.setSelection(list.size()-2);
				}
			}else if(msg.what == 1){//无数据
				if(list == null || list.size() == 0){
					notifyAdapter();
				}
				listView.setFooterText();
			}else if(msg.what == 102){//token超时 或者错误
				if(!isGoLogin){
					isGoLogin = true;
					Toast.makeText(FragmentForMyLottery.this.getActivity(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(FragmentForMyLottery.this.getActivity(),LoginActivity.class);
					intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
					startActivity(intent);
				}
				
			}else if(msg.what == 3){
				balance = MyApplication.getInstance().getCurrentBalance();
				if(balance == null || balance.equals("0") || balance.equals("") ){
					balance = "0.00";
				}
				myMoneyTv.setText("￥"+balance);
			}else if(msg.what == 4){//刷新完数据之后
				onLoad();
			}
			super.handleMessage(msg);
		}
		
	};
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(DeviceTool.getCurrentTime());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}
	private void notifyAdapter(){
		adapter = new MyCaiPiaoListViewAdapter(this.getActivity(), list);
		listView.setAdapter(adapter);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.f_mylottery, null);
		view.setOnClickListener(this);
		findview();
		initData();
		notifyAdapter();
	}
	private void findview(){
		listView = (XListView) view.findViewById(R.id.listView);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		allDD = (TextView) view.findViewById(R.id.allDDTV);
		zhongjiangDD = (TextView) view.findViewById(R.id.zhongjiangDDTV);
		weizhongjiangDD = (TextView) view.findViewById(R.id.weizhongjiangDDTV);
		daikaijiangDD = (TextView) view.findViewById(R.id.daikaijiangDDTV);
		zhuiHaoDD = (TextView) view.findViewById(R.id.zhuihaoDDTV);
		topLayout = (LinearLayout) view.findViewById(R.id.topLayout);
		lineView = view.findViewById(R.id.lineView);
		userNameTv = (TextView) view.findViewById(R.id.userNameTv);
		allDDLayout = (LinearLayout) view.findViewById(R.id.allDDLayout);
		zhongjiangDDLayout = (LinearLayout) view.findViewById(R.id.zhongjiangDDLayout);
		weizhongjiangDDLayout = (LinearLayout) view.findViewById(R.id.weizhongjiangDDLayout);
		daikaijiangDDLayout = (LinearLayout) view.findViewById(R.id.daikaijiangDDLayout);
		zhuiHaoDDLayout = (LinearLayout) view.findViewById(R.id.zhuihaoDDLayout);
		topLayout.setOnClickListener(this);
		allDDLayout.setOnClickListener(this);
		zhongjiangDDLayout.setOnClickListener(this);
		weizhongjiangDDLayout.setOnClickListener(this);
		daikaijiangDDLayout.setOnClickListener(this);
		zhuiHaoDDLayout.setOnClickListener(this);
		view.findViewById(R.id.chongzhiTv).setOnClickListener(this);
		myMoneyTv = (TextView) view.findViewById(R.id.myMoneyTv);
		balance = MyApplication.getInstance().getCurrentBalance();
		if(balance == null || balance.equals("0") || balance.equals("") ){
			balance = "0.00";
		}
		myMoneyTv.setText("￥"+balance);
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onClick(View v) {
		String username = MyApplication.getInstance().getCurrentUserName();
		if(v == allDDLayout){//所有订单
			setBtnStyle(allDD);
			sql = "select * from "+OrderBean.tbName+" where username="+username+" order by create_stmp desc";
			list = dbHelper.selectRow(sql, null);
			notifyAdapter();
		}else if(v == zhongjiangDDLayout){//中奖订单
			setBtnStyle(zhongjiangDD);
			sql = "select * from "+OrderBean.tbName+" where username="+username+" and iswin='1' and ispaid='1' order by create_stmp desc";
			list = dbHelper.selectRow(sql, null);
			notifyAdapter();
		}else if(v == weizhongjiangDDLayout){//没有中奖的订单
			setBtnStyle(weizhongjiangDD);
			sql = "select * from "+OrderBean.tbName+" where username="+username+" and iswin='0' and ispaid='1' order by create_stmp desc";
			list = dbHelper.selectRow(sql, null);
			notifyAdapter();
		}else if(v == daikaijiangDDLayout){//待开奖的订单
			setBtnStyle(daikaijiangDD);
			sql = "select * from "+OrderBean.tbName+" where username="+username+" and isdeal='0' order by create_stmp desc";
			list = dbHelper.selectRow(sql, null);
			notifyAdapter();
		}else if(v == zhuiHaoDDLayout){//追号订单
			setBtnStyle(zhuiHaoDD);
			sql = "select * from "+OrderBean.tbName+" where username="+username+" and is_trace='1' order by create_stmp desc";
			list = dbHelper.selectRow(sql, null);
			notifyAdapter();
		}else if(v == topLayout){
			Intent intent = new Intent(this.getActivity(),AmendMyInfoActivity.class);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.chongzhiTv){//充值
			Intent intent = new Intent(this.getActivity(),ChargeMoneyActivity.class);
			this.getActivity().startActivity(intent);
		}
	}
	/**
	 * 设置类型按钮的样式
	 * @param tv
	 */
	private void setBtnStyle(TextView tv){
		for(int i=0;i<textArray.length;i++){
			if(textArray[i] == tv){
				textArray[i].setBackgroundResource(R.drawable.border_ddbtn);
				textArray[i].setTextColor(Color.WHITE);
			}else{
				textArray[i].setBackgroundColor(Color.TRANSPARENT);
				textArray[i].setTextColor(Color.parseColor("#103023"));
			}
		}
	}
	private Handler mHandler;
	@Override
	public void onRefresh() {
		if(NetworkCheck.IsHaveInternet(getActivity())){
			freshFlag = 1;
			offsetStart = "0";
			length = "50";
			new Thread(getOrderRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(4);
				}
			}, 1000);
			
		}else{
			Toast.makeText(getActivity(), "未检测到网络，请检查网络连接！", 2000).show();
			onLoad();
		}
	}

	@Override
	public void onLoadMore() {
		if(NetworkCheck.IsHaveInternet(getActivity())){
			
			freshFlag = 2;
			offsetStart = list.size()+"";
			length = "10";
			new Thread(getOrderRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(4);
				}
			}, 1000);
		}else{
			Toast.makeText(getActivity(), "未检测到网络，请检查网络连接！", 2000).show();
			onLoad();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String state = list.get(arg2).get("ispaid").toString();
		String qishu = list.get(arg2).get("order_no").toString();
		String result = list.get(arg2).get("order_no").toString();
		String typeSelect = "1";
		String number = list.get(arg2).get("order_no").toString();
		String payMoney = list.get(arg2).get("amount").toString();
		String zhuNumber = (int)(Float.parseFloat(payMoney)/2)+"";
		if(state!=null && state.equals("0")){//未付款
			Intent intent = new Intent(this.getActivity(),SumbitSuccessActivity.class);
			intent.putExtra("qishu", qishu);
			intent.putExtra("result", result);
			intent.putExtra("typeSelect", typeSelect+"");
			intent.putExtra("number", number.substring(0, number.length()-1));
			intent.putExtra("payMoney", payMoney);
			intent.putExtra("zhuNumber",zhuNumber+"");
			this.getActivity().startActivity(intent);
		}
	}
}
