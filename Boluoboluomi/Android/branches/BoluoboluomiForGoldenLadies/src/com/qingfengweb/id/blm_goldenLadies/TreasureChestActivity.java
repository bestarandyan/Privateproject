package com.qingfengweb.id.blm_goldenLadies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.adapter.TreasureChesViewAdapter;
import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.model.TreasureInfo;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.DomAnalysisXml;

public class TreasureChestActivity extends BaseActivity implements OnItemClickListener{
	private Button backBtn;
	private ListView listView;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private String partnerid = "";// 合作伙伴id;
	private String partnername = "";// 合作伙伴名称;
	private ProgressDialog progressdialog;

	private boolean runnable_tag = true;
	private UploadData uploaddata =null;
	public final String SDPATH = Environment.getExternalStorageDirectory()
			+ "";
	public DBHelper dbHelper = null;
	public String currentStoreId = "";
	List<Map<String,Object>> listTreasure =null;//图片集合
	private TextView refreshDataTv;
	private LinearLayout refreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_treasurechest);
		dbHelper = DBHelper.getInstance(this);
		currentStoreId = UserBeanInfo.getInstant().getCurrentStoreId();
		findView();
		listTreasure = DomAnalysisXml.getTreasureImgFromXml(this, "icons.xml");
		System.out.println(listTreasure.size()+"");
	}
	
	
	@Override
	protected void onResume() {
		new Thread(systemUpdateRunnable).start();
		super.onResume();
	}
	/**
	 * 系统更新机制线程
	 */
	Runnable systemUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.ServerTime.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
			}
			System.out.println("本次更新时间======"+systemTimeStr);
			if(UserBeanInfo.getInstant().getCurrentStoreId()==null || UserBeanInfo.getInstant().getCurrentStoreId().equals("")){//如果没有门店ID则不进行对服务器的访问
				return;
			}
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.systemUpdate(UserBeanInfo.getInstant().getCurrentStoreId(), localTimeStr);//请求服务器获取更新内容
					System.out.println("用户id为"+UserBeanInfo.getInstant().getUserid());
					System.out.println("门店id为"+UserBeanInfo.getInstant().getCurrentStoreId());
					if(msgStr.equals("404")){//访问服务器失败
						System.out.println("本次系统更新接口访问服务器失败");
					}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
						System.out.println("本次系统更新接口返回无数据");
					}else{//请求成功并且有有效数据
						JsonData.jsonUpdateTimeData(msgStr, dbHelper.open());//解析数据并将数据保存到数据库
						ContentValues values = new ContentValues();
						values.put("localtime", systemTimeStr);
						dbHelper.update(SystemUpdateInfo.TableName, values, "type=?", new String[]{UpdateType.ServerTime.getValue()});
					}
				}
			
			handler.sendEmptyMessage(3);
		}
	};
	/**
	 * 获取百宝箱类型列表
	 */
	Runnable getTreasureChestListRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+TreasureInfo.TableName +" where storeid="+currentStoreId;
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			//检查系统更新机制表  看该用户的相册是否有更新
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+currentStoreId
					+"' and type = " + UpdateType.Partner.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";//最新更新时间
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.getTreasureChestList(currentStoreId, localTimeStr);//向服务器获取最新数据
					if(msgStr.startsWith("[") && msgStr.length()>3){//成功
						JsonData.jsonTreasureListData(msgStr, dbHelper.open(), currentStoreId);
						list = dbHelper.selectRow(sql, null);
						if(list!=null && list.size()>0){
							ContentValues values = new ContentValues();
							values.put("localtime", systemTimeStr);
							dbHelper.update(SystemUpdateInfo.TableName, values, "storeid=? and type =?", new String[]{currentStoreId,UpdateType.Partner.getValue()});
							handler.sendEmptyMessage(0);
						}
					}else if(msgStr.equals("404")){//访问服务器失败
						handler.sendEmptyMessage(1);
					}else {//更新失败
						handler.sendEmptyMessage(2);
					}
				}
			}
			
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter();
			}else if(msg.what == 3){
				new Thread(getTreasureChestListRunnable).start();
			}
			super.handleMessage(msg);
		}
		
	};
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		listView = (ListView) findViewById(R.id.listView);
		backBtn.setOnClickListener(this);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(this);
		refreshDataTv = (TextView) findViewById(R.id.refreshDataTv);
		refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
	}
	private void notifyAdapter(){
		refreshLayout.setVisibility(View.GONE);
		Map<String,Object> map = null;
		Resources res=getResources();
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			list.get(i).put("imgDrawable", 
					res.getIdentifier(//这里是根据图片在drawable中的名字获取该图片的资源id
							listTreasure.get(0).get(map.get("icon")).toString().replace(".png", "").trim(),//得到图片的名字，这里是不能直接写图片名称的，因为图片名称是后台给的，根据id去xml配置文件中获取
							"drawable",getPackageName()));//drawable 代表资源类型   
		}
		TreasureChesViewAdapter adapter = new TreasureChesViewAdapter(this, list);
		listView.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}
		super.onClick(v);
	}
	/**
	 * 根据名字获取资源文件res中的图片
	 * @param name
	 * @return
	 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = getApplicationInfo();
		int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(getResources(), resID);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this,MerchantListActivity.class);
		intent.putExtra("typeid", list.get(arg2).get("id").toString());
		startActivity(intent);
	}

	
}
