package com.qingfengweb.lottery.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.KuaiSanActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.adapter.TypeCaiAdapter;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;
import com.qingfengweb.lottery.view.XListView;
import com.qingfengweb.lottery.view.XListView.IXListViewListener;

public class FragmentForLotteryType  extends Fragment implements OnItemClickListener,OnClickListener,IXListViewListener{
	 View view = null;
	 ImageButton leftButton;//打开菜单按钮
	 ImageButton rightButton;//关于按钮
	 public static TextView timeTv,currentQi,currentQi1;
//	 XListView listView;
//	 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	 Timer timer = new Timer();  
	 TimerTask task = null;
	 public int nextTime = 0;
	 public static String qishu = "";//下一期期数
	 String timeStr = "";//当前剩的时间
	private static final String ARG_TEXT = "com.qingfengweb.lottery.fragment";
//	LinearLayout netStateLayout;
//	Button setNetBtn;
	public int freshFlag = 0;//上拉下拉标记   2代表上拉   1代表下拉 0代表正常加载
	private Handler mHandler;
	public boolean isFreshed = false;//判断是否刷新过数据
	public static FragmentForLotteryType newInstance(String text) {
		 FragmentForLotteryType f = new FragmentForLotteryType();
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
		view = LayoutInflater.from(getActivity()).inflate(R.layout.f_typecai, null);
//		listView = (XListView) view.findViewById(R.id.listView);
//		netStateLayout = (LinearLayout) view.findViewById(R.id.netStateLayout);
//		setNetBtn = (Button) view.findViewById(R.id.setNetBtn);
//		setNetBtn.setOnClickListener(this);
//		listView.setOnItemClickListener(this);
//		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
//		listView.setDividerHeight(15);
//		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		listView.setPullLoadEnable(true);
//		listView.setXListViewListener(this);
//		listView.setFooterViewState(View.GONE);
		view.findViewById(R.id.typeLayout1).setOnClickListener(this);
		view.findViewById(R.id.typeLayout2).setOnClickListener(this);
		view.findViewById(R.id.typeLayout3).setOnClickListener(this);
		view.findViewById(R.id.typeLayout4).setOnClickListener(this);
		view.findViewById(R.id.typeLayout5).setOnClickListener(this);
		view.findViewById(R.id.typeLayout6).setOnClickListener(this);
		timeTv = (TextView) view.findViewById(R.id.timeTv);
		currentQi = (TextView) view.findViewById(R.id.currentQiTv);
		currentQi1 = (TextView) view.findViewById(R.id.currentQiTv1);
		mHandler = new Handler();
		if (NetworkCheck.IsHaveInternet(getActivity())) {//检查是否有网络连接
			new Thread(getBJTimeRunnable).start();
		}else{
//			netStateLayout.setVisibility(View.VISIBLE);
		}
		super.onCreate(savedInstanceState);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//开始倒计时
				if(timer!=null ){
					timer.cancel();
				}
				if(task!=null ){
					task.cancel();
				}
				timer = new Timer();
				task = new TimerTask() {
					
					@Override
					public void run() {
						handler.sendEmptyMessage(1);
					}
				};
				timer.schedule(task, 1000, 1000);
			}else if(msg.what == 1){//每一秒都会执行一下这里   
				/*if(nextTime<(2*60*1000) && nextTime>0){
					nextTime-=1000;
					timeTv.setText(timeStr);
					int preQi = Integer.parseInt(qishu.substring(qishu.length()-2))-1;
					currentQi.setText("0"+preQi);
					currentQi1.setText(qishu);
					if(FragmentForLotteryInfo.preQi!=null){
						FragmentForLotteryInfo.preQi.setText("0"+preQi);
					}
					if(FragmentForLotteryInfo.jiezhiTV!=null){
						FragmentForLotteryInfo.jiezhiTV.setText(qishu);
					}
				}else */if(nextTime<=0){
					nextTime = 600000;
					qishu = (Integer.parseInt(qishu)+1)+"";
				}else{
					nextTime-=1000;
					timeStr = StringUtils.calculatTimeNoHour(nextTime);
					if(timeStr!=null)
					timeTv.setText(timeStr);
					int preQi = Integer.parseInt(qishu.substring(qishu.length()-2))-1;
					currentQi.setText("0"+preQi);
					currentQi1.setText(qishu);
					if(FragmentForLotteryInfo.preQi!=null){
						FragmentForLotteryInfo.preQi.setText("0"+preQi);
					}
					if(FragmentForLotteryInfo.jiezhiTV!=null){
						FragmentForLotteryInfo.jiezhiTV.setText(qishu);
					}
					if(FragmentForLotteryInfo.timeTv!=null){
						FragmentForLotteryInfo.timeTv.setText(timeStr);
					}
				}
				MyApplication.getInstance().setCurrentSurplusTime(nextTime);
//				adapter.notifyDataSetChanged();
				
			}else if(msg.what == 3){//获取时间失败
				
			}else if(msg.what == 4){//刷新完数据之后
				onLoad();
			}
			super.handleMessage(msg);
		}
		
	};
    private void onLoad() {
//		listView.stopRefresh();
//		listView.stopLoadMore();
//		listView.setRefreshTime(DeviceTool.getCurrentTime());
    	
	}
	/**
	 * 获取服务器时间
	 */
	Runnable getBJTimeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String bjtime ="";
			if(bjtime==null || bjtime.length()<1){
				bjtime = RequestServerFromHttp.getServerTime();
			}
			if(bjtime==null || bjtime.length()<1){
				isFreshed = false;
				 handler.sendEmptyMessage(3);
				return;
			}
				
			isFreshed = true;
			MyApplication.getInstance().setCurrentServerTime(bjtime);
			bjtime = bjtime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
			
			System.out.println("当前北京时间："+bjtime);
			if(bjtime.length()<12){
				return;
			}
			int currentTime = Integer.parseInt(bjtime.substring(8, 10))*3600+Integer.parseInt(bjtime.substring(10, 12))*60+Integer.parseInt(bjtime.substring(12, 14));//单位为秒
			System.out.println("当前北京时间的时间秒数："+currentTime);
			int firstTime = 8*3600+48*60;//单位为秒
			System.out.println("第一期彩票的时间秒数："+firstTime);
			int time = (int) (currentTime-firstTime);
			System.out.println("从第一期到现在走的时间秒数："+time);
			qishu = bjtime.substring(0,8)+((time/600+1)<10?("0"+(time/600+1)):(time/600+1));
			System.out.println("当前彩票期数："+qishu);
			int yushu = 600-time%600;//距离下一期还剩下的秒数
			Message msg = new Message();
			msg.what =0 ;
			nextTime = yushu*1000;
			handler.sendMessage(msg);
		}
	};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2 == 1){
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			this.getActivity().startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {
//		if(v == setNetBtn){//设置网络状态
//			Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
//            startActivity(intent);
//		}
		if(v.getId() == R.id.typeLayout1){//和值
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 1);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.typeLayout2){//三同号
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 2);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.typeLayout3){//二同号
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 3);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.typeLayout4){//三不同
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 4);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.typeLayout5){//三连号
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 5);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.typeLayout6){//二不同
			Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
			intent.putExtra("type", 6);
			this.getActivity().startActivity(intent);
		}
	}
	@Override
	public void onRefresh() {
//		if (NetworkCheck.IsHaveInternet(getActivity())) {//检查是否有网络连接
//			new Thread(getBJTimeRunnable).start();
//			netStateLayout.setVisibility(View.GONE);
//		}else{
//			netStateLayout.setVisibility(View.VISIBLE);
//		}
		freshFlag = 1;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(4);
			}
		}, 1000);
		
	}
	
	@Override
	public void onResume() {
//		if (NetworkCheck.IsHaveInternet(getActivity())) {//检查是否有网络连接
//			if(!isFreshed){
//				new Thread(getBJTimeRunnable).start();
//			}
//			netStateLayout.setVisibility(View.GONE);
//		}else{
//			netStateLayout.setVisibility(View.VISIBLE);
//		}
		super.onResume();
	}
	@Override
	public void onLoadMore() {
		onLoad();
//		freshFlag = 2;
//		new Thread(getBJTimeRunnable).start();
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				handler.sendEmptyMessage(4);
//			}
//		}, 1000);
		
	}
}
