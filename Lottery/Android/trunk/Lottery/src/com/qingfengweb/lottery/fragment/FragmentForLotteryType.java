package com.qingfengweb.lottery.fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.KuaiSanActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.adapter.TypeCaiAdapter;
import com.qingfengweb.lottery.bean.LastResultBean;
import com.qingfengweb.lottery.data.ConstantValues;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;
import com.qingfengweb.lottery.view.XListView;
import com.qingfengweb.lottery.view.XListView.IXListViewListener;

@SuppressLint("SimpleDateFormat")
@TargetApi(Build.VERSION_CODES.ECLAIR)
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
	public boolean isGoStopSelect = false;//判断是否已经进入了禁止选择界面
	DBHelper dbHelper = null;
	List<Map<String,Object>> lastResultInfoList = new ArrayList<Map<String,Object>>();//最新开奖结果查询
	public static String lastResult = "";//最新开奖结果
	public static String codeResult = "";
	public static String hotResult = "";
	public static String resultQishu = "20140320082";
	public static int no1,no2,no3;
	public static int hezhi = 0;
	TextView kaijiangNumberTv,code1,code2,code3,hot1,hot2,hot3;
	ImageView sz1,sz2,sz3,sz21,sz22,sz31,sz32,sz41,sz42;
	TextView tophezhiTv,typeTv;
	LinearLayout jiezhiLayout;
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
		dbHelper = DBHelper.getInstance(this.getActivity());
		view = LayoutInflater.from(getActivity()).inflate(R.layout.f_typecai, null);
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
		typeTv = (TextView) view.findViewById(R.id.typeTv);
		jiezhiLayout = (LinearLayout) view.findViewById(R.id.jiezhiLayout);
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
			new Thread(getLastResultRunnable).start();
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
						Message msg1 = new Message();
						msg1.what =1 ;
						handler.sendMessage(msg1);
					}
				};
				timer.schedule(task, 1000, 1000);
			}else if(msg.what == 1){//每一秒都会执行一下这里   
				/*if(Integer.parseInt(qishu.substring(qishu.length()-2))>82 || currentTime<firstTime){
//					qishu = "82";
					String mingtian = "";
					if(currentTime<firstTime){
						nextTime = firstTime - currentTime;
						Date d=new Date();  
						SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
						mingtian = df.format(new Date(d.getTime()));
						mingtian = mingtian+"001";
						currentTime+=1;
						nextTime+=1000;
					}else{
						nextTime = 24*60*60-currentTime+8*60*60;
						Date d=new Date();  
						SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
						mingtian = df.format(new Date(d.getTime() + 1 * 24 * 60 * 60 * 1000));
						mingtian = mingtian+"001";
						currentTime-=1;
						nextTime-=1000;
					}
					timeStr = StringUtils.calculatTimeNoHour(nextTime);
					
					
					timeTv.setText(timeStr);
					int preQi = Integer.parseInt(qishu.substring(qishu.length()-2))-1;
					currentQi.setText("0"+preQi);
					currentQi1.setText(mingtian);
					if(FragmentForLotteryInfo.preQi!=null){
						FragmentForLotteryInfo.preQi.setText("0"+preQi);
					}
					if(FragmentForLotteryInfo.jiezhiTV!=null){
						FragmentForLotteryInfo.jiezhiTV.setText(mingtian);
					}
					if(FragmentForLotteryInfo.timeTv!=null){
						FragmentForLotteryInfo.timeTv.setText(timeStr);
					}
					if(StopSelectActivity.timeTv!=null){
						StopSelectActivity.timeTv.setText(timeStr);
					}
//					System.out.println("今天的日期："+df.format(d));  
//					System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));  
//					System.out.println("明天后的日期：" + df.format(new Date(d.getTime() + 1 * 24 * 60 * 60 * 1000)));
				}else */if(nextTime<(10*60*1000) && nextTime>0){
					timeStr = StringUtils.calculatTimeNoHour(nextTime);
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
					if(FragmentForLotteryInfo.timeTv!=null){
						FragmentForLotteryInfo.timeTv.setText(timeStr);
					}
//					if(StopSelectActivity.timeTv!=null){
//						StopSelectActivity.timeTv.setText(timeStr);
//					}
//					if(!isGoStopSelect){
//						isGoStopSelect = true;
//						Intent intent = new Intent(FragmentForLotteryType.this.getActivity(),StopSelectActivity.class);
//						FragmentForLotteryType.this.getActivity().startActivity(intent);
//						FragmentForLotteryType.this.getActivity().overridePendingTransition(R.anim.anim_enter_fromtop, R.anim.anim_exit_fromtop);
//					}
//					
				}else if(nextTime<=0){
					if (NetworkCheck.IsHaveInternet(getActivity())) {//检查是否有网络连接
						new Thread(getLastResultRunnable).start();
					}
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
			}else if(msg.what == 5){
				lastResult = lastResultInfoList.get(0).get("result").toString();
				codeResult = lastResultInfoList.get(0).get("code").toString();
				hotResult = lastResultInfoList.get(0).get("hot").toString();
				resultQishu = lastResultInfoList.get(0).get("ticket_no").toString();
				if(lastResult == null || lastResult.equals("") || lastResult.equals("null")){
					return;
				}
				no1 = Integer.parseInt(lastResult.substring(0,1));
				no2 = Integer.parseInt(lastResult.substring(1,2));
				no3 = Integer.parseInt(lastResult.substring(2,3));
				hezhi = no1+no2+no3;
				
				kaijiangNumberTv.setText(lastResult);
				
				code1.setText(codeResult.substring(0, 1));
		    	code2.setText(codeResult.substring(1, 2));
		    	code3.setText(codeResult.substring(2, 3));
		    	
		    	hot1.setText(hotResult.substring(0, 1));
		    	hot2.setText(hotResult.substring(1, 2));
		    	hot3.setText(hotResult.substring(2, 3));
		    	
		    	sz1.setImageResource(ConstantValues.SZ_IMG_ARRAY[no1-1]);
		    	sz2.setImageResource(ConstantValues.SZ_IMG_ARRAY[no2-1]);
		    	sz3.setImageResource(ConstantValues.SZ_IMG_ARRAY[no3-1]);
		    	
		    	sz21.setImageResource(ConstantValues.SZ_IMG_ARRAY[no1-1]);
		    	sz22.setImageResource(ConstantValues.SZ_IMG_ARRAY[no2-1]);
		    	
		    	sz31.setImageResource(ConstantValues.SZ_IMG_ARRAY[no1-1]);
		    	sz32.setImageResource(ConstantValues.SZ_IMG_ARRAY[no3-1]);
		    	
		    	sz41.setImageResource(ConstantValues.SZ_IMG_ARRAY[no2-1]);
		    	sz42.setImageResource(ConstantValues.SZ_IMG_ARRAY[no3-1]);
		    	
		    	tophezhiTv.setText("和值"+(hezhi));
		    	
		    	if(hezhi%2 == 0 && hezhi > 10){
		    		typeTv.setText("双  大");
		    	}else if(hezhi%2 == 0 && hezhi <= 10){
		    		typeTv.setText("双  小");
		    	}else if(hezhi%2 == 1 && hezhi > 10){
		    		typeTv.setText("单  大");
		    	}else if(hezhi%2 == 1 && hezhi <= 10){
		    		typeTv.setText("单  小");
		    	}
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
     * 获取最新开奖结果信息
     */
    Runnable getLastResultRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+LastResultBean.tbName;
			lastResultInfoList = dbHelper.selectRow(sql, null);
			if(lastResultInfoList!=null && lastResultInfoList.size()>0){
				handler.sendEmptyMessage(5);
			}
			String msg = RequestServerFromHttp.getLastResult();
			if(msg!=null && msg.startsWith("{") && msg.length()>3){
				JsonData.jsonLastResultInfo(msg, dbHelper.open());
				lastResultInfoList = dbHelper.selectRow(sql, null);
				if(lastResultInfoList!=null && lastResultInfoList.size()>0){
					handler.sendEmptyMessage(5);
				}
			}else{
				
			}
		}
	};
	
	int currentTime = 0;
	int firstTime = 8*3600+48*60;//单位为秒
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
			currentTime = Integer.parseInt(bjtime.substring(8, 10))*3600+Integer.parseInt(bjtime.substring(10, 12))*60+Integer.parseInt(bjtime.substring(12, 14));//单位为秒
			System.out.println("当前北京时间的时间秒数："+currentTime);
			System.out.println("第一期彩票的时间秒数："+firstTime);
			int time = (int) (currentTime-firstTime);
			System.out.println("从第一期到现在走的时间秒数："+time);
			qishu = bjtime.substring(0,8)+((time/600+1)<10?("0"+(time/600+1)):(time/600+1));
			System.out.println("当前彩票期数："+qishu);
//			if(Integer.parseInt(qishu)>82){
//				timeTv.setText("今日已停售");
//				currentQi.setText("082");
//				currentQi1.setText("082");
//				jiezhiLayout.setVisibility(View.GONE);
//				return;
//			}
			int yushu = 600-time%600;//距离下一期还剩下的秒数
			Message msg = new Message();
			msg.what =0 ;
			msg.obj = currentTime;
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
		Intent intent = new Intent(this.getActivity(),KuaiSanActivity.class);
		if(v.getId() == R.id.typeLayout1){//和值
			intent.putExtra("type", 1);
		}else if(v.getId() == R.id.typeLayout2){//三同号
			intent.putExtra("type", 2);
		}else if(v.getId() == R.id.typeLayout3){//二同号
			intent.putExtra("type", 3);
		}else if(v.getId() == R.id.typeLayout4){//三不同
			intent.putExtra("type", 4);
		}else if(v.getId() == R.id.typeLayout5){//三连号
			intent.putExtra("type", 5);
		}else if(v.getId() == R.id.typeLayout6){//二不同
			intent.putExtra("type", 6);
		}
		this.getActivity().startActivity(intent);
		this.getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
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
