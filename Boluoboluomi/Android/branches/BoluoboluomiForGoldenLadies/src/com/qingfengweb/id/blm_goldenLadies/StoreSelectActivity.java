/**
 * 
 */
package com.qingfengweb.id.blm_goldenLadies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.adapter.CitySelectListAdapter;
import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.LocalStaticData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.SDKInfo;
import com.qingfengweb.model.StoreInfo;
import com.qingfengweb.util.MessageBox;

/**
 * @author 刘星星
 * @createDate 2013/6/28
 *
 */
public class StoreSelectActivity extends BaseActivity{
	private ExpandableListView mainListView;// 显示商圈的列表
	private ArrayList<HashMap<String, String>> groupArray;
	private ArrayList<ArrayList<HashMap<String, String>>> childArray;
	private ArrayList<HashMap<String,String>> searchGroupArray;
	private ArrayList<ArrayList<HashMap<String, String>>> searchChildArray;
	private TextView currentCity;
	private Button backBtn;
	private Bundle bundle = null;//保存该城市的名称和ID
	private String cityId = "";//城市id
	public DBHelper db1 = null;
	
	private EditText searchEt;
	LinearLayout proLayout;
	TextView failTextView;
	ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_storeselect);
		findview();
		initView();
		initAreaList() ;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent intent = new Intent(StoreSelectActivity.this,CitySelectActivity.class);
			startActivity(intent);
			finish();
		}else if(v == failTextView){
			failTextView.setVisibility(View.GONE);
			if(groupArray.size()==0){
				proLayout.setVisibility(View.VISIBLE);
			}else{
				proLayout.setVisibility(View.GONE);
			}
			new Thread(getStoreListRunnable).start();
		}
		super.onClick(v);
	}
	private void findview(){
		mainListView = (ExpandableListView) findViewById(R.id.mainListView);
		currentCity = (TextView) findViewById(R.id.currentCityText);
		backBtn = (Button) findViewById(R.id.backBtn);
		searchEt = (EditText) findViewById(R.id.cityEt);
		searchEt.addTextChangedListener(new MyTextWatcher());
		proLayout = (LinearLayout) findViewById(R.id.proLayout);
		failTextView = (TextView) findViewById(R.id.connectFailText);
		failTextView.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}
	/**
	 * 创建进度条
	 */
	private void createProgressDialog(){
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage("正在加载门店数据，请稍候!");
		progress.show();
	}
	/***
	 * 监听文本改变
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			if (searchEt.getText().toString().length() > 0) {
				initSearchListData(searchEt.getText().toString());
			} else {
				if (groupArray != null && groupArray.size() > 0) {
					notifyListView(groupArray,childArray);
				}
			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}
	
	/**
	 * 加载搜索出来的房子的列表
	 * 
	 * @author 刘星星
	 * @createDate 2013/3/15
	 */
	private void initSearchListData(String whereClause) {
		if (searchGroupArray == null) {
			searchGroupArray = new ArrayList<HashMap<String, String>>();
		} else {
			searchGroupArray.clear();
			searchChildArray.clear();
		}
		String sql = "select * from "+StoreInfo.TableName+" where (name like '"
				+ whereClause + "%' or district like '"+whereClause+"%') and cityid='"+cityId+"'";
		List<Map<String,Object>> list = db1.selectRow(sql, null);
		Map<String,Object> map = null;
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			String parent = (String) map.get("district");
			HashMap<String,String> gMap = new HashMap<String, String>();
			gMap.put("name", parent);
			if(!searchGroupArray.contains(gMap)){
				searchGroupArray.add(gMap);
			}
		}
		for(int i=0;i<searchGroupArray.size();i++){
			ArrayList<HashMap<String, String>> tempArray = new ArrayList<HashMap<String, String>>();
			for(int j=0;j<list.size();j++){
				map = list.get(j);
				String parent = (String) map.get("district");
				String store = (String) map.get("name");
				String storeId = (String) map.get("id");
				if(parent.equals(searchGroupArray.get(i).get("name"))){
					HashMap<String,String> cMap = new HashMap<String, String>();
					cMap.put("name", store);
					cMap.put("id", storeId);
					tempArray.add(cMap);
				}
			}
			searchChildArray.add(tempArray);
		}
		notifyListView(searchGroupArray, searchChildArray);
	}
	private void initView(){
		mainListView.setOnChildClickListener(new AreaListChildClickListener());
	}
	
	/**
	 * 商圈列表的子项点击事件监听类
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/7
	 */
	private class AreaListChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			
			if(searchEt.getText().toString().length()>0){
				UserBeanInfo.getInstant().setCurrentStore(searchChildArray.get(groupPosition).get(childPosition).get("name"));
				UserBeanInfo.getInstant().setCurrentStoreId(searchChildArray.get(groupPosition).get(childPosition).get("id"));
			}else{
				UserBeanInfo.getInstant().setCurrentStore(childArray.get(groupPosition).get(childPosition).get("name"));
				UserBeanInfo.getInstant().setCurrentStoreId(childArray.get(groupPosition).get(childPosition).get("id"));
			}
/*			if(MyApplication.getInstant().isSelectCityFromMainActivity){
				MyApplication.getInstant().setSelectCityFromMainActivity(false);
				finish();
			}else{
*/				MyApplication.getInstant().setSelectCityFromMainActivity(false);
				createProgressDialog();
				new Thread(getSotreDetailRunnable).start();
//			}
			
		 return true;
		}
	}
	/**
	 * 获取门店详情
	 */
	Runnable getSotreDetailRunnable = new Runnable() {
			
			@Override
			public void run() {
				String currentStoreId = UserBeanInfo.getInstant().getCurrentStoreId();
				if(currentStoreId ==null || currentStoreId.length()==0 || currentStoreId.equals("")){
					return ;
				}
				String msgStr = RequestServerFromHttp.getStoreDetail(UserBeanInfo.getInstant().getCurrentStoreId());
				if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(4);
				}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
					handler.sendEmptyMessage(4);
				}else if(msgStr.startsWith("{")){//请求成功并且有有效数据
					JsonData.jsonSotreDetailData(msgStr, db1.open(),UserBeanInfo.getInstant().getCurrentCityId());//解析数据并将数据保存到数据库
					String sql = "select * from "+StoreInfo.TableName+" where id="+currentStoreId;
					UserBeanInfo.storeDetail = db1.selectRow(sql, null);
					ContentValues cv = new ContentValues();
					cv.put("ispassguide", "1");
					cv.put("isselectstore", "1");
					cv.put("store", UserBeanInfo.getInstant().getCurrentStore());
					cv.put("storeid", UserBeanInfo.getInstant().getCurrentStoreId());
					boolean b = db1.update(SDKInfo.TableName, cv, null, null);
					if(!b){
						db1.insert(SDKInfo.TableName, cv);
					}
					handler.sendEmptyMessage(3);
				}
			}
		};
	/**
	 * 初始化区域数据
	 * 
	 * @createDate 2013/1/7
	 */
	private void initAreaList() {
		groupArray = new ArrayList<HashMap<String, String>>();
		childArray = new ArrayList<ArrayList<HashMap<String, String>>>();
		searchGroupArray = new ArrayList<HashMap<String, String>>();
		searchChildArray = new ArrayList<ArrayList<HashMap<String, String>>>();
		bundle = getIntent().getBundleExtra("cityBundle");
		cityId = bundle.getString("cityId");
		UserBeanInfo.getInstant().setCurrentCityId(cityId);
		currentCity.setText(bundle.getString("cityName"));
		groupArray.clear();
		childArray.clear();
		searchChildArray.clear();
		searchGroupArray.clear();
		db1 = DBHelper.getInstance(this);
		selectDB();//查询数据库
		notifyListView(groupArray,childArray);
		if(!LocalStaticData.isSelectedStore.contains(cityId)){
			if(groupArray.size()==0){
				proLayout.setVisibility(View.VISIBLE);
			}else{
				proLayout.setVisibility(View.GONE);
			}
			new Thread(getStoreListRunnable).start();
		}
	}
	/**
	 * 获取门店列表数据线程
	 */
	Runnable getStoreListRunnable = new Runnable() {
		@Override
		public void run() {
			if(bundle==null)
				return;
			String msgStr = RequestServerFromHttp.getStoresList(cityId);
			System.out.println(msgStr);
			if(msgStr.equals("404") || msgStr.equals("-4")){
				handler.sendEmptyMessage(2);
			}else if(JsonData.isNoData(msgStr)){
				handler.sendEmptyMessage(1);
			}else{
				JsonData.jsonStoresData(msgStr, db1.open(), cityId);
				selectDB();//查询数据库
				handler.sendEmptyMessage(0);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//数据获取成功
				proLayout.setVisibility(View.GONE);
				notifyListView(groupArray, childArray);
				if(groupArray.size() == 0){
					failTextView.setVisibility(View.VISIBLE);
				}
			}else if(msg.what == 1){//无数据处理
				proLayout.setVisibility(View.GONE);
				notifyListView(groupArray, childArray);
				if(groupArray.size() == 0){
					failTextView.setVisibility(View.VISIBLE);
					failTextView.setText("您选择的城市暂无门店！");
					failTextView.setClickable(false);
				}
			}else if(msg.what == 2){//访问服务器失败
				proLayout.setVisibility(View.GONE);
				notifyListView(groupArray, childArray);
				if(groupArray.size() == 0){
					failTextView.setVisibility(View.VISIBLE);
					failTextView.setText("获取失败，请检查网络连接或者点击屏幕重试！");
					failTextView.setClickable(true);
				}
			}else if(msg.what == 3){//获取门店详情成功 跳转到登陆界面
				progress.dismiss();
//				if(MyApplication.getInstant().isSelectCityFromMainActivity){
//					finish();
//				}else{
					Intent i1 = new Intent(StoreSelectActivity.this, MainActivity.class);
					StoreSelectActivity.this.startActivity(i1);
					StoreSelectActivity.this.finish();
//				}
			}else if(msg.what == 4){//获取门店详情失败
				progress.dismiss();
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt), "门店详情获取失败！",
						StoreSelectActivity.this);
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 查询数据库
	 */
	private void selectDB(){
		String sql = "select *from "+StoreInfo.TableName+" where cityid = "+cityId;
		List<Map<String,Object>> list = db1.selectRow(sql, null);
		Map<String,Object> map = null;
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			String parent = (String) map.get("district");
			HashMap<String,String> gMap = new HashMap<String, String>();
			gMap.put("name", parent);
			if(!groupArray.contains(gMap)){
				groupArray.add(gMap);
			}
		}
		for(int i=0;i<groupArray.size();i++){
			ArrayList<HashMap<String, String>> tempArray = new ArrayList<HashMap<String, String>>();
			for(int j=0;j<list.size();j++){
				map = list.get(j);
				String parent = (String) map.get("district");
				String store = (String) map.get("name");
				String storeId = (String) map.get("id");
				if(parent.equals(groupArray.get(i).get("name"))){
					HashMap<String,String> cMap = new HashMap<String, String>();
					cMap.put("name", store);
					cMap.put("id", storeId);
					tempArray.add(cMap);
				}
			}
			childArray.add(tempArray);
		}
	}
	/**
	 * 刷新列表布局 给区域列表控件加载数据
	 */
	private void notifyListView(ArrayList<HashMap<String,String>> groupArray,ArrayList<ArrayList<HashMap<String, String>>> childArray) {
		mainListView.setAdapter(new CitySelectListAdapter(this, groupArray,
				childArray));
		setExpandableViewOpen();
	}
	
	/**
	 * 设置ExpandableListView默认为子项全部打开
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/7
	 */
	private void setExpandableViewOpen() {
		int groupCount = mainListView.getCount();
		for (int i = 0; i < groupCount; i++) {
			mainListView.expandGroup(i);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(StoreSelectActivity.this,CitySelectActivity.class);
			startActivity(intent);
			finish();
		}
		return true;
	}
}
