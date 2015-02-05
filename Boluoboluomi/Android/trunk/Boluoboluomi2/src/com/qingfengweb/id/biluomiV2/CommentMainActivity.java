package com.qingfengweb.id.biluomiV2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.adapter.CommentMainActiviyAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.PersonInfo;
import com.qingfengweb.model.RecommendInfo;
import com.qingfengweb.model.SystemUpdateInfo;

public class CommentMainActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> list1;
	private List<Map<String, Object>> list2;
	private Button btn1, btn2;
	private Button backBtn;
	private GridView gv1, gv2;
	private TextView explain;
	private DBHelper dbHelper = null;
	private TextView refreshDataTv;
	private LinearLayout refreshLayout;
	ScrollView scrollViewLayout;
	private TextView noDataTv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_comment);
		findView();
		initListData();
	}

	private void findView() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		gv1 = (GridView) findViewById(R.id.gv1);
		gv2 = (GridView) findViewById(R.id.gv2);
		gv1.setOnItemClickListener(this);
		gv2.setOnItemClickListener(this);
		explain = (TextView) findViewById(R.id.tv);
		explain.setOnClickListener(this);
		refreshDataTv = (TextView) findViewById(R.id.refreshDataTv);
		refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
		scrollViewLayout = (ScrollView) findViewById(R.id.scrollViewLayout);
		scrollViewLayout.setVisibility(View.GONE);
		noDataTv = (TextView) findViewById(R.id.noDataTv);
		noDataTv.setOnClickListener(this);
	}

	private void initListData() {
		list = new ArrayList<Map<String, Object>>();
		list1 = new ArrayList<Map<String, Object>>();
		list2 = new ArrayList<Map<String, Object>>();
		dbHelper = DBHelper.getInstance(this);
		new Thread(getDataRunnable).start();
	}
Runnable getDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql1 = "select *from "+PersonInfo.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId()+" and position='1' limit 0,8";
			list1 = dbHelper.selectRow(sql1, null);
			if(list1!=null && list1.size()>0){
				handler.sendEmptyMessage(1);
			}
			
			String sql2 = "select *from "+PersonInfo.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId()+" and position='2' limit 0,8";
			list2 = dbHelper.selectRow(sql2, null);
			if(list2!=null && list2.size()>0){
				handler.sendEmptyMessage(2);
			}
			//检查系统更新机制表  看该用户的相册是否有更新
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and type = '" + UpdateType.StaffEvaluation.getValue()+"'";
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";//最新更新时间
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getValuationList(UserBeanInfo.getInstant().getCurrentStoreId(),"", "");//这里直接找到所有点评人好了
					System.out.println(msgStr);
					if(msgStr.startsWith("[")){//获取推荐套系成功
						JsonData.jsonValuationData(msgStr, dbHelper.open(), UserBeanInfo.getInstant().getCurrentStoreId());
						list1 = dbHelper.selectRow(sql1, null);
						if(list1!=null && list1.size()>0){
							handler.sendEmptyMessage(1);
						}
						
						list2 = dbHelper.selectRow(sql2, null);
						if(list2!=null && list2.size()>0){
							handler.sendEmptyMessage(2);
						}
					}else if(msgStr.equals("404")){//访问服务器失败
						
					}else if(JsonData.isNoData(msgStr)){//访问服务器失败
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					}else{//获取推荐套系失败
						
					}
				}
			}
			
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){//刷新摄影师布局
				notifyList(1);
			}else if(msg.what == 2){//刷新化妆师布局
				notifyList(2);
			}
			super.handleMessage(msg);
		}
		
	};
	private void notifyList(int position) {
		refreshLayout.setVisibility(View.GONE);
		scrollViewLayout.setVisibility(View.VISIBLE);
		int height = (MyApplication.getInstant().getWidthPixels()/4-10)*2+40;
		if(position == 1){//刷新摄影师布局
			if (list1!=null && list1.size() > 4) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
				gv1.setLayoutParams(param);
			}
			if(list1!=null && list1.size()>0){
				gv1.setAdapter(new CommentMainActiviyAdapter(this, list1));
				gv1.setOnItemClickListener(this);
			}
		}else if(position == 2){//刷新化妆师布局
			if (list2!=null && list2.size() > 4) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
				param.setMargins(0, 20, 0, 0);
				gv2.setLayoutParams(param);
			}
			if(list2!=null && list2.size()>0){
				gv2.setAdapter(new CommentMainActiviyAdapter(this, list2));
				gv2.setOnItemClickListener(this);
			}
		}
		if((list1==null && list2==null) || (list1.size()==0 && list2.size() == 0)){
			refreshLayout.setVisibility(View.GONE);
			noDataTv.setVisibility(View.VISIBLE);
			noDataTv.setText("暂无数据！");
			scrollViewLayout.setVisibility(View.GONE);
		}
		
	}
	public void onClick(View v) {
		if (v == btn1) {//进入摄影师列表界面
//			MyApplication.getInstant().setPosition(1);
			Intent intent = new Intent(this, CommentListActivity.class);
			intent.putExtra("position", 1);
			startActivity(intent);
			finish();
		} else if (v == btn2) {//进入化妆师列表界面
//			MyApplication.getInstant().setPosition(2);
			Intent intent = new Intent(this, CommentListActivity.class);
			intent.putExtra("position", 2);
			startActivity(intent);
			finish();
		} else if (v == backBtn) {//返回
			finish();
		} else if (v == explain) {//帮助说明
			Intent intent = new Intent(this, HelpActivity.class);
			intent.putExtra("tag", 2);
			intent.putExtra("type", 2);
			startActivity(intent);
		}else if(v == noDataTv){
			noDataTv.setText("正在刷新数据...");
			new Thread(getDataRunnable).start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == gv1) {
			if(!UserBeanInfo.getInstant().isLogined){
				Intent intent = new Intent(this,LoginActivity.class);
    			intent.putExtra("activityType", 3);
    			intent.putExtra("map", (Serializable)list1.get(position));
        		startActivity(intent);
        		finish();
			}else{
				Intent i = new Intent(this, CommentActivity.class);
				i.putExtra("title", "摄影师");
				i.putExtra("map", (Serializable)list1.get(position));
				startActivity(i);
				finish();
			}
		} else if (parent == gv2) {
			if(!UserBeanInfo.getInstant().isLogined){
				Intent intent = new Intent(this,LoginActivity.class);
    			intent.putExtra("activityType", 3);
    			intent.putExtra("map", (Serializable)list2.get(position));
        		startActivity(intent);
        		finish();
			}else{
				Intent i = new Intent(this, CommentActivity.class);
				i.putExtra("title", "化妆师");
				i.putExtra("map", (Serializable)list2.get(position));
				startActivity(i);
				finish();
			}
		}
	}
}
