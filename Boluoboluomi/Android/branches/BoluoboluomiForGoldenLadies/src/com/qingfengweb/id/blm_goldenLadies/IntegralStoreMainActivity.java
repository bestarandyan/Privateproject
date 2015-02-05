package com.qingfengweb.id.blm_goldenLadies;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.adapter.IntegraMainListAdapter;
import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.IntegralTypeInfo;
import com.qingfengweb.model.SystemUpdateInfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class IntegralStoreMainActivity  extends BaseActivity {
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
//	private RelativeLayout linear1,linear2,linear3,linear4;
	private ListView listView;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
	private DBHelper dbHelper = null;
	public static Activity instantActivity = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_integralstoremain);
		findView();
		findBottomBtn();
		initData();
		instantActivity = this;
		new Thread(getIntegralTypeRunnable).start();
	}
	/**
	 * 获取节分类型
	 */
	Runnable getIntegralTypeRunnable = new Runnable() {
		
		@Override
		public void run() {
			selectLocalData();//查询数据库
			handler.sendEmptyMessage(0);
			//检查系统更新机制表  看该用户的相册是否有更新
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.Shop.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";//最新更新时间
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getIntegralType(UserBeanInfo.getInstant().getCurrentStoreId(), ""); //想服务器获取数据
					if(msgStr.startsWith("[")){//获取数据成功
						JsonData.jsonIntegralTypeData(msgStr, dbHelper.open(), UserBeanInfo.getInstant().getCurrentStoreId());//解析服务器返回的数据
						//更新更新机制中的最新更新时间
						ContentValues values = new ContentValues();
						values.put("localtime", systemTimeStr);
						dbHelper.update(SystemUpdateInfo.TableName, values, "storeid=? and type =?", new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),UpdateType.Shop.getValue()});
						selectLocalData();//查询数据库
						handler.sendEmptyMessage(0);
					}else if(msgStr.equals("404")){//访问服务器失败
						
					}else if(msgStr.length()<5){
						
					}
				}
			}
			
		}
	};
	private void selectLocalData(){
		String sql = "select * from "+IntegralTypeInfo.TableName +" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId();
		list1 = dbHelper.selectRow(sql, null);
		list.clear();
		for(int i=0;i<list1.size();i++){
			Map<String,Object> map = list1.get(i);
			if(map.get("name").toString().trim().equals("电器类")){
				map = new HashMap<String, Object>();
				map.put("type", "电器类");
				map.put("img1", R.drawable.integral11+"");
				map.put("img2", R.drawable.integral12+"");
				map.put("img3", R.drawable.integral13+"");
				map.put("img4", R.drawable.integral14+"");
				list.add(map);
			}else if(map.get("name").toString().trim().equals("生活类")){
				map = new HashMap<String, Object>();
				map.put("type", "生活类");
				map.put("img1", R.drawable.integral21+"");
				map.put("img2", R.drawable.integral22+"");
				map.put("img3", R.drawable.integral23+"");
				map.put("img4", R.drawable.integral24+"");
				list.add(map);
			}else if(map.get("name").toString().trim().equals("产品类")){
				map = new HashMap<String, Object>();
				map.put("type", "产品类");
				map.put("img1", R.drawable.integral31+"");
				map.put("img2", R.drawable.integral32+"");
				map.put("img3", R.drawable.integral33+"");
				map.put("img4", R.drawable.integral34+"");
				list.add(map);
			}else{
				map = new HashMap<String, Object>();
				map.put("type", "其他");
				map.put("img1", R.drawable.integral41+"");
				map.put("img2", R.drawable.integral42+"");
				map.put("img3", R.drawable.integral43+"");
				map.put("img4", R.drawable.integral44+"");
				list.add(map);
			}
		}
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter();
			}
			super.handleMessage(msg);
		}
		
	};
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("type", "电器类");
//		map.put("img1", R.drawable.integral11+"");
//		map.put("img2", R.drawable.integral12+"");
//		map.put("img3", R.drawable.integral13+"");
//		map.put("img4", R.drawable.integral14+"");
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("type", "生活类");
//		map.put("img1", R.drawable.integral21+"");
//		map.put("img2", R.drawable.integral22+"");
//		map.put("img3", R.drawable.integral23+"");
//		map.put("img4", R.drawable.integral24+"");
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("type", "产品类");
//		map.put("img1", R.drawable.integral31+"");
//		map.put("img2", R.drawable.integral32+"");
//		map.put("img3", R.drawable.integral33+"");
//		map.put("img4", R.drawable.integral34+"");
//		list.add(map);
//		map = new HashMap<String, Object>();
//		map.put("type", "其它类");
//		map.put("img1", R.drawable.integral41+"");
//		map.put("img2", R.drawable.integral42+"");
//		map.put("img3", R.drawable.integral43+"");
//		map.put("img4", R.drawable.integral44+"");
//		list.add(map);
//		notifyAdapter();
		
	}
	private void notifyAdapter(){
		IntegraMainListAdapter adapter = new IntegraMainListAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setCacheColorHint(0);
	}
	private void findView(){
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
//		linear1 = (RelativeLayout) findViewById(R.id.linear1);
//		linear2 = (RelativeLayout) findViewById(R.id.linear2);
//		linear3 = (RelativeLayout) findViewById(R.id.linear3);
//		linear4 = (RelativeLayout) findViewById(R.id.linear4);
//		linear1.setOnClickListener(this);
//		linear2.setOnClickListener(this);
//		linear3.setOnClickListener(this);
//		linear4.setOnClickListener(this);
	}
	private void findBottomBtn(){
		backBtn = (Button) findViewById(R.id.backBtn);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab5 = (LinearLayout) findViewById(R.id.tab5);
		tabBtn1 = (Button) findViewById(R.id.tab1Btn);
		tabBtn2 = (Button) findViewById(R.id.tab2Btn);
		tabBtn3 = (Button) findViewById(R.id.tab3Btn);
		tabBtn4 = (Button) findViewById(R.id.tab4Btn);
		tabBtn5 = (Button) findViewById(R.id.tab5Btn);
		backBtn.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab5.setOnClickListener(this);
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01_on);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05);
	}
	
	@Override
	protected void onResume() {
		click_limit = true;
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		/*if(v == linear1){
			Intent i = new Intent(this,IntegralExchangeActivity.class);
			i.putExtra("type", 1);//电器类
			startActivity(i);
			finish();
		}else if(v == linear2){
			Intent i = new Intent(this,IntegralExchangeActivity.class);
			i.putExtra("type",2);//生活类
			startActivity(i);
			finish();
		}else if(v == linear3){
			Intent i = new Intent(this,IntegralExchangeActivity.class);
			i.putExtra("type", 3);//产品类
			startActivity(i);
			finish();
		}else if(v == linear4){
			Intent i = new Intent(this,IntegralExchangeActivity.class);
			i.putExtra("type", 4);//其他类
			startActivity(i);
			finish();
		}else */if (v == tab1) {
			click_limit = true;
//			Intent i = new Intent(this, IntegralStoreMainActivity.class);
//			startActivity(i);
//			finish();
		} else if (v == tab2) {
			if(UserBeanInfo.getInstant().isLogined){
				Intent i = new Intent(this, RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else{
				Intent i = new Intent(this,LoginActivity.class);
				i.putExtra("activityType", 6);
				startActivity(i);
			}
		} else if (v == tab3) {
			Intent i = new Intent(this, MyIntegralActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab4) {
			Intent i = new Intent(this, IntegralRuleActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab5) {
			Intent i = new Intent(this, EcshopActiveActivity.class);
			startActivity(i);
			finish();
		}else if(v == backBtn){
			finish();
		}
		super.onClick(v);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this,IntegralExchangeActivity.class);
		i.putExtra("type", arg2);//产品类
		startActivity(i);
		finish();
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	@Override
	protected void onDestroy() {
		click_limit = true;
		super.onDestroy();
	}
	
}
