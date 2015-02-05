/**
 * 
 */
package com.qingfengweb.lottery.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.AmendMyInfoActivity;
import com.qingfengweb.lottery.activity.AmendPasswordActivity;
import com.qingfengweb.lottery.activity.KuaiSanActivity;
import com.qingfengweb.lottery.adapter.LotteryListViewAdapter;
import com.qingfengweb.lottery.bean.ResultInfo;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.view.XListView;
import com.qingfengweb.lottery.view.XListView.IXListViewListener;

/**
 * @author 刘星星
 * @createDate 2013、11、22
 *
 */
@SuppressLint("HandlerLeak")
public class FragmentForLotteryInfo  extends Fragment implements IXListViewListener,OnClickListener{
	private static final String ARG_TEXT = "com.qingfengweb.lottery.fragment";
	 View view = null;
	 XListView listView;
	 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	 LotteryListViewAdapter adapter = null;
	 DBHelper dbHelper = null;
	 private Handler mHandler;
	 public String startQishu = "";//起始查询期数
	 public String endQishu = "";//结束查询期数
	 public int freshFlag = 0;//上拉下拉标记   2代表上拉   1代表下拉 0代表正常加载
	 public static TextView timeTv,jiezhiTV,preQi;//截止期数
	 public static FragmentForLotteryInfo newInstance(String text) {
	   FragmentForLotteryInfo f = new FragmentForLotteryInfo();
       Bundle args = new Bundle();
       args.putString(ARG_TEXT, text);
       f.setArguments(args);

       return f;
   }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.f_lotteryinfo, null);
		findview();
		initData();
		
	}
	Dialog dialog = null;
	@Override
	public void onResume() {
//		DeviceTool.getBeiJingTime();
//		 System.out.println(DeviceTool.getStrTime("1291778220"));
		super.onResume();
	}
	private void findview(){
		listView = (XListView) view.findViewById(R.id.listView);
		listView.setPullLoadEnable(true);
		view.findViewById(R.id.touzhuBtn).setOnClickListener(this);
		listView.setXListViewListener(this);
		jiezhiTV = (TextView) view.findViewById(R.id.currentQiTv1);
		preQi = (TextView) view.findViewById(R.id.currentQiTv);
		timeTv = (TextView) view.findViewById(R.id.timeTv);
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(this.getActivity());
		mHandler = new Handler();
		
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			startQishu = "20130101050";
			endQishu = "20130101060";
			new Thread(getListInfoRunnable).start();
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
		            FragmentForLotteryInfo.this.dialog.dismiss();
				}
			});
			alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					FragmentForLotteryInfo.this.dialog.dismiss();
				}
			});
			dialog = alert.create();
			dialog.show();
		}
	}
	/**
	 * 获取开奖信息数据
	 */
	Runnable getListInfoRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select * from "+ResultInfo.tbName+" order by open_stmp desc";
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			String msg = RequestServerFromHttp.queryInfoResult(startQishu,endQishu);
			if(msg!=null && msg.startsWith("[")){//获取成功
				JsonData.jsonResultInfo(msg, dbHelper.open());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
			}else if(msg.equals("0")){//无数据
				handler.sendEmptyMessage(1);
			}else{//获取失败
				handler.sendEmptyMessage(2);
			}
		}
	};
	Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//获取数据成功
				if(freshFlag == 0){
					notifyAdapter();
				}else if(freshFlag == 1){//下拉
					notifyAdapter();
				}else if(freshFlag == 2){//上拉
					notifyAdapter();
					listView.setSelection(list.size()-2);
				}
			}else if(msg.what == 1){//无数据
				listView.setFooterText();
			}else if(msg.what == 2){//获取失败
				MessageBox.CreateAlertDialog("提示！","数据加载失败，请检查网络连接，或者下拉刷新！",FragmentForLotteryInfo.this.getActivity());
			}else if(msg.what == 3){
				onLoad();
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyAdapter(){
		adapter = new LotteryListViewAdapter(getActivity(), list);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.touzhuBtn){
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			this.getActivity().startActivity(intent);
		}
	}
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(DeviceTool.getCurrentTime());
	}
	@Override
	public void onRefresh() {//下拉
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			freshFlag = 1;
			String listfirst = list.get(0).get("ticket_no").toString();
			startQishu = listfirst;
			endQishu = (Long.parseLong(listfirst)+10)+"";
			new Thread(getListInfoRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(3);
				}
			}, 1000);
		}else{
			Toast.makeText(getActivity(), "未检测到网络，请检查网络连接...", 2000).show();
			onLoad();
		}
		
	}

	@Override
	public void onLoadMore() {//上拉
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			freshFlag = 2;
			String listLast = list.get(list.size()-1).get("ticket_no").toString();
			startQishu = (Long.parseLong(listLast)-10)+"";
			endQishu = listLast;
			new Thread(getListInfoRunnable).start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(3);
				}
			}, 1000);
			
		}else{
			Toast.makeText(getActivity(), "未检查到网络，请检查网络连接！", 2000).show();
			onLoad();
		}
		
	}
	
}

