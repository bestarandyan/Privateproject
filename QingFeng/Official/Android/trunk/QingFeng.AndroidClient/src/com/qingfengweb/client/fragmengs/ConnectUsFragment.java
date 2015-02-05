/**
 * 
 */
package com.qingfengweb.client.fragmengs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.activity.DetailJobActivity;
import com.qingfengweb.client.adapter.JoinUsAdapter;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.JobListInfo;
import com.qingfengweb.client.bean.ServicesInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.database.DBHelper;

/**
 * @author 刘星星
 * @createDate 2013/6/17
 * 联系我们界面
 *
 */
@SuppressLint("NewApi")
public class ConnectUsFragment extends Fragment implements OnClickListener,OnItemClickListener{
	public static String ARG_TEXT = "connect";
	public static int pageValue = -1;//表示在这一页的页值
	public static ConnectUsFragment newInstance(String value) {
		ConnectUsFragment f = new ConnectUsFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TEXT, value);
		f.setArguments(bundle);
		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return viewGroup;
	}
	private ViewGroup viewGroup;
	private ViewPager vPager;
	private ArrayList<View> pageViews;  
	private RelativeLayout connectUsLayout,joinUsLayout;
	private TextView cuTextView,juTextView;
	private ImageView cuImageView,juImageView;
	private ImageButton openMenuButton;
	
	private ImageView connectImg1,connectImg2,connectImg3,connectImg4,connectImg5,connectImg6,
	connectImg7,connectImg8,connectImg9;
	private TextView connectWay;
	
	private ListView joinListView;
	private List<Map<String, Object>> jobList = new ArrayList<Map<String,Object>>();
	private DBHelper dbHelper = null;
	
	public String jobLastUpdateTime = "";//团队数据的最后更新时间
	public String sysTime = "";//系统提供的最后更新时间  这个时间并不一定等于本地更新过的时间。
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findview();
		initData();
	}
	
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		pageViews = new ArrayList<View>();  
        pageViews.add(getConnectLayout());
        pageViews.add(getJoinLayout());
        notifyPager();
        jobList.clear();
		new Thread(getJobListFromLocalData).start();
		new Thread(getConnectWayFormLocalRunnable).start();
	}
	Runnable getJobListFromLocalData = new Runnable() {
		
		@Override
		public void run() {
			jobList = dbHelper.selectAllRows(JobListInfo.TableName, "*", null);
			//查询更新表中的类型为3（ 即团队成员）的数据  判断上次更新的时间和系统更新的时间是否相等，
			String sql = "select * from "+UpdateSystemTime.tableName+" where type='5'";
			List<Map<String,Object>> updateList = dbHelper.selectRow(sql, null);
			String localTime = "";
			if(updateList!=null && updateList.size()>0){
				if(updateList.get(0).get("localtime")!=null && updateList.get(0).get("localtime").toString().length()>0){
					localTime = updateList.get(0).get("localtime").toString();
				}
				if(updateList.get(0).get("systime")!=null && updateList.get(0).get("systime").toString().length()>0){
					sysTime = updateList.get(0).get("systime").toString();
				}
			}
			if(jobList!=null && jobList.size()>0){
				handler.sendEmptyMessage(0);//本地数据获取到
				
				//如果不相等则后台向服务器去更新数据。
				if(updateList!=null && updateList.size()>0){
					if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////如果不相等则后台向服务器去更新数据。
						jobLastUpdateTime = localTime;//设置最后更新时间为上一次更新的时间，
						handler.sendEmptyMessage(2);//根据系统更新表中的数据判断团队成员数据是否有更新
					}
				}
			}else{
				jobLastUpdateTime = "";//设置最后更新时间为空，即如果数据库没有团队成员数据，不管更新表中的localTime和sysTime是否相等  都去服务器获取数据
				handler.sendEmptyMessage(2);//本地无数据 开启线程向服务器获取
			}
		}
	};
	/**
	 * 获取加入我们的数据
	 */
	Runnable getJobListFromServicesData = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = AccessServer.getContents(jobLastUpdateTime, AccessServer.JOB_ACTION);
			if(AccessServer.isNoData(msgStr)){
				System.out.println("获取招聘信息数据时，服务器返回无数据或者参数错误。返回值为："+msgStr);
				handler.sendEmptyMessage(1);//无数据
			}else if(msgStr.equals("404")){
				System.out.println("获取招聘信息数据时，访问服务器失败");
			}else{
				System.out.println("获取招聘信息数据成功。");
				JsonData.jsonJobInfoData(msgStr, getActivity(), dbHelper.open());
				jobList = dbHelper.selectAllRows(JobListInfo.TableName, "*", null);
				if(jobList!=null && jobList.size()>0){
					ContentValues contentValues = new ContentValues();
					contentValues.put("localTime", sysTime);
					dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"5"});
					handler.sendEmptyMessage(0);//获取数据成功
				}else{
					handler.sendEmptyMessage(1);//无数据
				}
			}
			
		}
	};
	
	Runnable getConnectWayFormLocalRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select * from "+ServicesInfo.tableName+" where type = '8'";
			List<Map<String,Object>> msgList = dbHelper.selectRow(sql, null);
			if(msgList!=null && msgList.size()>0){
				Message msg = new Message();
				msg.what = 3;
				msg.obj = msgList.get(0).get("summary");
				handler.sendMessage(msg);
			}else{
				System.out.println("去服务器获取联系方式。。。。");
				String msgStr = AccessServer.getContentDetail("8");
				if(AccessServer.isNoData(msgStr)){
					
				}else if(msgStr.equals("404")){
					
				}else{
					Message msg = new Message();
					msg.what = 3;
					msg.obj = msgStr;
					handler.sendMessage(msg);
				}
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//获取到数据
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notifyView();
				}		
			}else if(msg.what == 1){//服务器无数据
				
			}else if(msg.what == 2){
				new Thread(getJobListFromServicesData).start();
			}else if (msg.what == 3){
				String way = msg.obj.toString();
				if(way!=null)
					connectWay.setText(way);
			}
			super.handleMessage(msg);
		}
		
	};
	JoinUsAdapter adapter = null;
	private void notifyView(){
		adapter = new JoinUsAdapter(getActivity(), jobList);
		joinListView.setAdapter(adapter);
		
	}
	private View getConnectLayout(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.connectus, null);
		connectWay = (TextView) view.findViewById(R.id.wayOfConnect);
		connectImg1 = (ImageView) view.findViewById(R.id.connectImg1);
		connectImg2 = (ImageView) view.findViewById(R.id.connectImg2);
		connectImg3 = (ImageView) view.findViewById(R.id.connectImg3);
		connectImg4 = (ImageView) view.findViewById(R.id.connectImg4);
		connectImg5 = (ImageView) view.findViewById(R.id.connectImg5);
		connectImg6 = (ImageView) view.findViewById(R.id.connectImg6);
		connectImg7 = (ImageView) view.findViewById(R.id.connectImg7);
		connectImg8 = (ImageView) view.findViewById(R.id.connectImg8);
		connectImg9 = (ImageView) view.findViewById(R.id.connectImg9);
		connectImg1.setOnClickListener(this);
		connectImg2.setOnClickListener(this);
		connectImg3.setOnClickListener(this);
		connectImg4.setOnClickListener(this);
		connectImg5.setOnClickListener(this);
		connectImg6.setOnClickListener(this);
		connectImg7.setOnClickListener(this);
		connectImg8.setOnClickListener(this);
		connectImg9.setOnClickListener(this);
		return view;
	}
	int oldX = 0;
	int newX = 0;
	private View getJoinLayout(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.joinus, null);
		joinListView = (ListView) view.findViewById(R.id.joinListView);
		joinListView.setOnItemClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == openMenuButton){
			BaseActivity.sm.showMenu();
		}else if(v == connectUsLayout){
			showPrevious();
			vPager.setCurrentItem(0, true);
		}else if (v == joinUsLayout) {
			showNext();
			vPager.setCurrentItem(1, true);
		}else if (v == connectImg1) {
			
		}else if (v == connectImg2) {
			
		}else if (v == connectImg3) {
			
		}else if (v == connectImg4) {
			
		}else if (v == connectImg5) {
			
		}else if (v == connectImg6) {
			
		}else if (v == connectImg7) {
			
		}else if (v == connectImg8) {
			
		}else if (v == connectImg9) {
			
		}
	}
	
	private void showNext(){
		cuImageView.setVisibility(View.INVISIBLE);
		juImageView.setVisibility(View.VISIBLE);
		cuTextView.setTextColor(getResources().getColor(R.color.team_text_2));
		juTextView.setTextColor(Color.WHITE);
		connectUsLayout.setClickable(true);
		joinUsLayout.setClickable(false);
	}
	
	private void showPrevious(){
		cuImageView.setVisibility(View.VISIBLE);
		juImageView.setVisibility(View.INVISIBLE);
		cuTextView.setTextColor(Color.WHITE);
		juTextView.setTextColor(getResources().getColor(R.color.team_text_2));
		connectUsLayout.setClickable(false);
		joinUsLayout.setClickable(true);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(),DetailJobActivity.class);
//		intent.putExtra("detail_job", jobList.get(arg2));
		intent.putExtra("detail_job",(Serializable) jobList.get(arg2));
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
	}
	

	private void findview(){
		viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.a_connectus, null);
		vPager  = (ViewPager) viewGroup.findViewById(R.id.connectUs_vp);
		connectUsLayout = (RelativeLayout) viewGroup.findViewById(R.id.connectUsLayout);
		joinUsLayout = (RelativeLayout) viewGroup.findViewById(R.id.joinUsLayout);
		cuTextView = (TextView) viewGroup.findViewById(R.id.connectUsTv);
		juTextView = (TextView) viewGroup.findViewById(R.id.joinUsTv);
		cuImageView = (ImageView) viewGroup.findViewById(R.id.connectUsIv);
		juImageView = (ImageView) viewGroup.findViewById(R.id.joinUsIv);
		connectUsLayout.setOnClickListener(this);
		joinUsLayout.setOnClickListener(this);
		openMenuButton = (ImageButton) viewGroup.findViewById(R.id.LeftBtn);
		openMenuButton.setOnClickListener(this);
		
	}
	
	private void notifyPager(){
		vPager.setAdapter(new ViewPagerAdapter(pageViews));  
		vPager.setOnPageChangeListener(new GuidePageChangeListener());  
	}
    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	if(arg0 == 0){
        		showPrevious();
        		CustomViewAbove.currentPage = 0;
        		pageValue = 0;
        	}else{
        		showNext();
        		CustomViewAbove.currentPage = 1;
        		pageValue = 1;
        	}
        }  
    }  
}
