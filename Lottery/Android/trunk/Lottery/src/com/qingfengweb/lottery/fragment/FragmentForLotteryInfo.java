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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.AmendMyInfoActivity;
import com.qingfengweb.lottery.activity.AmendPasswordActivity;
import com.qingfengweb.lottery.activity.KuaiSanActivity;
import com.qingfengweb.lottery.adapter.LotteryListViewAdapter;
import com.qingfengweb.lottery.bean.ResultInfo;
import com.qingfengweb.lottery.data.ConstantValues;
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
@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
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
	 
	 TextView kaijiangNumberTv,code1,code2,code3,hot1,hot2,hot3;
	 ImageView sz1,sz2,sz3,sz21,sz22,sz31,sz32,sz41,sz42;
	 TextView tophezhiTv,typeTv;
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
		kaijiangNumberTv = (TextView) view.findViewById(R.id.kaijiangNumberTv);
		code1 = (TextView) view.findViewById(R.id.code1);
		code2 = (TextView) view.findViewById(R.id.code2);
		code3 = (TextView) view.findViewById(R.id.code3);
		hot1 = (TextView) view.findViewById(R.id.hot1);
		hot2 = (TextView) view.findViewById(R.id.hot2);
		hot3 = (TextView) view.findViewById(R.id.hot3);
		sz1 = (ImageView) view.findViewById(R.id.sz1);
		sz2 = (ImageView) view.findViewById(R.id.sz2);
		sz3 = (ImageView) view.findViewById(R.id.sz3);
		sz21 = (ImageView) view.findViewById(R.id.sz21);
		sz22 = (ImageView) view.findViewById(R.id.sz22);
		sz31 = (ImageView) view.findViewById(R.id.sz31);
		sz32 = (ImageView) view.findViewById(R.id.sz32);
		sz41 = (ImageView) view.findViewById(R.id.sz41);
		sz42 = (ImageView) view.findViewById(R.id.sz42);
		tophezhiTv = (TextView) view.findViewById(R.id.hezhiTv);
		view.findViewById(R.id.touzhuBtn).setOnClickListener(this);
		listView.setXListViewListener(this);
		jiezhiTV = (TextView) view.findViewById(R.id.currentQiTv1);
		preQi = (TextView) view.findViewById(R.id.currentQiTv);
		timeTv = (TextView) view.findViewById(R.id.timeTv);
		typeTv = (TextView) view.findViewById(R.id.typeTv);
		if(FragmentForLotteryType.lastResult == null || FragmentForLotteryType.lastResult.equals("") || FragmentForLotteryType.lastResult.equals("null")){
			return;
		}
		kaijiangNumberTv.setText(FragmentForLotteryType.lastResult);
		
		code1.setText(FragmentForLotteryType.codeResult.substring(0, 1));
    	code2.setText(FragmentForLotteryType.codeResult.substring(1, 2));
    	code3.setText(FragmentForLotteryType.codeResult.substring(2, 3));
    	
    	hot1.setText(FragmentForLotteryType.hotResult.substring(0, 1));
    	hot2.setText(FragmentForLotteryType.hotResult.substring(1, 2));
    	hot3.setText(FragmentForLotteryType.hotResult.substring(2, 3));
    	
    	sz1.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no1-1]);
    	sz2.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no2-1]);
    	sz3.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no3-1]);
    	
    	sz21.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no1-1]);
    	sz22.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no2-1]);
    	
    	sz31.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no1-1]);
    	sz32.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no3-1]);
    	
    	sz41.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no2-1]);
    	sz42.setImageResource(ConstantValues.SZ_IMG_ARRAY[FragmentForLotteryType.no3-1]);
    	
    	tophezhiTv.setText("和值"+(FragmentForLotteryType.hezhi));
    	
    	if(FragmentForLotteryType.hezhi%2 == 0 && FragmentForLotteryType.hezhi > 10){
    		typeTv.setText("双  大");
    	}else if(FragmentForLotteryType.hezhi%2 == 0 && FragmentForLotteryType.hezhi <= 10){
    		typeTv.setText("双  小");
    	}else if(FragmentForLotteryType.hezhi%2 == 1 && FragmentForLotteryType.hezhi > 10){
    		typeTv.setText("单  大");
    	}else if(FragmentForLotteryType.hezhi%2 == 1 && FragmentForLotteryType.hezhi <= 10){
    		typeTv.setText("单  小");
    	}
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(this.getActivity());
		mHandler = new Handler();
		
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			startQishu = (Long.parseLong(FragmentForLotteryType.resultQishu)-10)+"";
			endQishu = FragmentForLotteryType.resultQishu;
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
	@SuppressWarnings("deprecation")
	@Override
	public void onRefresh() {//下拉
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			freshFlag = 1;
			String listfirst = list.get(0).get("ticket_no").toString();
			startQishu = listfirst;
			int shengyuqishu = Integer.parseInt(listfirst.substring(listfirst.length()-2));
			if(shengyuqishu>72){
				Date d = new Date(Integer.parseInt(listfirst.substring(0, 4)), 
						Integer.parseInt(listfirst.substring(4, 6).toString()), 
								Integer.parseInt(listfirst.substring(6, 8).toString()));
				 SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
				 endQishu = df.format(new Date(d.getTime() + 1 * 24 * 60 * 60 * 1000))+"0"+(10-(82-shengyuqishu));
			}else{
				endQishu = (Long.parseLong(listfirst)+10)+"";
			}
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

	@SuppressWarnings("deprecation")
	@Override
	public void onLoadMore() {//上拉
		if(NetworkCheck.IsHaveInternet(this.getActivity())){
			freshFlag = 2;
			if(list.size()<1){
				return ;
			}
			String listLast = list.get(list.size()-1).get("ticket_no").toString();
			int shengyuqishu = Integer.parseInt(listLast.substring(listLast.length()-2));
			if(shengyuqishu<10){
				Date d = new Date(Integer.parseInt(listLast.substring(0, 4))-1900, 
						Integer.parseInt(listLast.substring(4, 6).toString())-1, 
								Integer.parseInt(listLast.substring(6, 8).toString()));
				 SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
				 startQishu = df.format(new Date(d.getTime() - 1 * 24 * 60 * 60 * 1000))+"0"+(82-10+shengyuqishu);
				 
			}else{
				startQishu = (Long.parseLong(listLast)-10)+"";
			}
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

