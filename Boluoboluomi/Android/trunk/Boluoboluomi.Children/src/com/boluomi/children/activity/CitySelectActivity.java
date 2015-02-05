/**
 * 
 */
package com.boluomi.children.activity;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.boluomi.children.R;
import com.boluomi.children.adapter.CitySelectListAdapter;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.database.AssetsDatabaseManager;
import com.boluomi.children.util.ListDispose;
/**
 * @author 刘星星
 * @createDate 2013/6/28
 *
 */
public class CitySelectActivity extends BaseActivity{
	private ExpandableListView cityListView;// 
	private ArrayList<HashMap<String,String>> groupArray;
	private ArrayList<HashMap<String,String>> searchGroupArray;
	private ArrayList zimuList;// 所有城市的字母列表
	private ArrayList<ArrayList<HashMap<String, String>>> childArray;
	private ArrayList<HashMap<String, String>> childArrayAll;
	private ArrayList<ArrayList<HashMap<String, String>>> searchChildArray;
	private TextView currentCity;
	private LinearLayout cityLayout;
	public SQLiteDatabase db1 = null;
	public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    
    private EditText searchEt;
	public  final String[]  py= { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
		"X", "Y", "Z" };
	private ListView zimuListView;//字母列表
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_cityselect);
		findview();
		initData();
		initView();
		getLocation();
		notifiViewZimu();
	}
	private void getLocation(){
		mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类   
		mLocationClient.registerLocationListener(myListener );   	//注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())	{
			mLocationClient.requestLocation();
		}else 	{
			Log.d("LocSDK3", "locClient is null or not started");
		}
	}
	private void findview(){
		cityListView = (ExpandableListView) findViewById(R.id.cityListView);
		zimuListView = (ListView) findViewById(R.id.zimuList);
		currentCity = (TextView) findViewById(R.id.currentCityText);
		cityLayout = (LinearLayout) findViewById(R.id.cityLayout);
		searchEt = (EditText) findViewById(R.id.cityEt);
		searchEt.addTextChangedListener(new MyTextWatcher());
		cityLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentCity.getText()!=null && currentCity.getText().toString().trim().length()>0){
					String sql = "select *from region where length(layer)=9 and name like '上海%'";
					Cursor c = db1.rawQuery(sql, null);
					Bundle bundle = new Bundle();
					while(c.moveToNext()){
						String province = c.getString(c.getColumnIndex("name")).toString();
						String id = c.getString(c.getColumnIndex("id")).toString();
						bundle.putString("cityName", province);
						bundle.putString("cityId", id);
					}
					Intent intent = new Intent(CitySelectActivity.this,StoreSelectActivity.class);
					intent.putExtra("cityBundle", bundle);
					startActivity(intent);
					finish();
				}
			}
		});
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
		String sql = "select * from region where (name like '"
				+ whereClause + "%' or pinyin like '" + whereClause
				+ "%') and length(layer)=9 and layer like '022%' order by name";
		Cursor c = db1.rawQuery(sql, null);
		ArrayList<HashMap<String, String>> tem = new ArrayList<HashMap<String,String>>();
		while(c.moveToNext()){
			HashMap<String, String> map = new HashMap<String, String>();
			String zimu = c.getString(c.getColumnIndex("pinyin")).toString().substring(0, 1);
			String name = c.getString(c.getColumnIndex("name")).toString();
			String id = c.getString(c.getColumnIndex("id")).toString();
			map.put("name", name);
			map.put("zimu", zimu);
			map.put("id", id);
			tem.add(map);
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("name", zimu);
			if(!searchGroupArray.contains(map1)){
				searchGroupArray.add(map1);
			}
		}
		searchChildArray.add(tem);
		notifyListView(searchGroupArray, searchChildArray);
	}
	private void initView(){
		cityListView.setOnChildClickListener(new AreaListChildClickListener());
		currentCity.setText("正在定位...");
	}
	@Override
	protected void onDestroy() {
		mLocationClient.stop();
		
		super.onDestroy();
	}
	
	public class MyLocationListener implements BDLocationListener {	
		@Override	
		public void onReceiveLocation(BDLocation location) {
			if (location == null)		
				return ;	
			
			String city = "";
			if(location.getCity()!=null){
				city = location.getCity().toString();
			}
			if(city.length()>0){
				currentCity.setText(city);
				mLocationClient.stop();
			}else{
				currentCity.setText("定位失败...");
			}
			
			}
		public void onReceivePoi(BDLocation poiLocation) {	
				if (poiLocation == null){		
					return ;		
					}		
		}
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
			Intent intent = new Intent(CitySelectActivity.this,StoreSelectActivity.class);
			Bundle bundle = new Bundle();
			if(searchEt.getText().toString().length()>0){
				bundle.putString("cityName", searchChildArray.get(groupPosition).get(childPosition).get("name").toString());
				bundle.putString("cityId", searchChildArray.get(groupPosition).get(childPosition).get("id").toString());
			}else{
				bundle.putString("cityName", childArray.get(groupPosition).get(childPosition).get("name").toString());
				bundle.putString("cityId", childArray.get(groupPosition).get(childPosition).get("id").toString());
			}
			intent.putExtra("cityBundle", bundle);
			startActivity(intent);
			finish();
					return true;
		}
	}
	@SuppressWarnings("unchecked")
	private void initData(){
		groupArray = new ArrayList<HashMap<String,String>>();
		childArray = new ArrayList<ArrayList<HashMap<String, String>>>();
		searchGroupArray = new ArrayList<HashMap<String,String>>();
		searchChildArray = new ArrayList<ArrayList<HashMap<String, String>>>();
		childArrayAll = new ArrayList<HashMap<String, String>>();
		zimuList = new ArrayList<String>();
		zimuList.clear();
		AssetsDatabaseManager.initManager(getApplication());  
		// 获取管理对象，因为数据库需要通过管理对象才能够获取   
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  
		// 通过管理对象获取数据库   
		db1 = mg.getDatabase(ConstantsValues.cityDBName);  
		HashMap<String,String> mapHot = new HashMap<String, String>();
		mapHot.put("name", "热门城市");
		if(!groupArray.contains(mapHot)){
			groupArray.add(mapHot);
		}
		String sqlHot = "select *from region where length(layer)=9 and layer like '022%' and ishot = '1'";
		Cursor cHot = db1.rawQuery(sqlHot, null);
		ArrayList<HashMap<String, String>> tempArrayHot = new ArrayList<HashMap<String, String>>();
		while(cHot.moveToNext()){
			String province = cHot.getString(cHot.getColumnIndex("name")).toString();
			String id = cHot.getString(cHot.getColumnIndex("id")).toString();
			String zimu = cHot.getString(cHot.getColumnIndex("pinyin")).toString().substring(0, 1);
			HashMap<String, String> mapHotChild = new HashMap<String, String>();
			mapHotChild.put("name", province);
			mapHotChild.put("id", id);
			mapHotChild.put("zimu", zimu);
			tempArrayHot.add(mapHotChild);
		}
		this.childArray.add(tempArrayHot);
		// 对数据库进行操作   
		String sql = "select *from region where length(layer)=9 and layer like '022%'";
		Cursor c = db1.rawQuery(sql, null);
		ArrayList<HashMap<String, String>> childArrayAll = new ArrayList<HashMap<String, String>>();
		HashMap<String,String> map = null;
		while(c.moveToNext()){
			String province = c.getString(c.getColumnIndex("name")).toString();
			String id = c.getString(c.getColumnIndex("id")).toString();
			String zimu = c.getString(c.getColumnIndex("pinyin")).toString().substring(0, 1);
			map = new HashMap<String, String>();
			map.put("name", province);
			map.put("id", id);
			map.put("zimu", zimu);
			childArrayAll.add(map);
			if (!zimuList.contains(zimu)) {
				zimuList.add(zimu);
			}
			
		}
		childArrayAll = ListDispose.sortList1(childArrayAll, zimuList, "zimu");// 将集合按拼音排序
		for (int a = 0; a < childArrayAll.size(); a++) {
			HashMap<String,String> map1 = new HashMap<String, String>();
			map1.put("name", childArrayAll.get(a).get("zimu"));
			if(!groupArray.contains(map1)){
				groupArray.add(map1);
			}
		}
		for(int i=1;i<groupArray.size();i++){
			ArrayList<HashMap<String, String>> tempArray = new ArrayList<HashMap<String, String>>();
			for(int j=0;j<childArrayAll.size();j++){
				if(childArrayAll.get(j).get("zimu").equals(groupArray.get(i).get("name"))){
					tempArray.add(childArrayAll.get(j));
				}
			}
			this.childArray.add(tempArray);
		}
		notifyListView(groupArray,childArray);
	}
	
	/**
	 * 刷新列表布局 给区域列表控件加载数据
	 */
	private void notifyListView(ArrayList<HashMap<String,String>> groupArray,ArrayList<ArrayList<HashMap<String, String>>> childArray) {
		cityListView.setAdapter(new CitySelectListAdapter(this, groupArray,
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
		int groupCount = cityListView.getCount();
		for (int i = 0; i < groupCount; i++) {
			cityListView.expandGroup(i);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return false;
	}
	
	/**
	 * @author 刘星星
	 * @createDate 2013/1/15 加载字母数据
	 */
	public void notifiViewZimu() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_zimu, py);
		zimuListView.setAdapter(adapter);
		zimuListView.setDivider(null);
		zimuListView.setOnItemClickListener(new ZimuListItemClickListener());
		zimuListView.setOnTouchListener(new ZimuListTouchListener());
		zimuListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	/**
	 * 字母控件的触屏事件监听
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/15
	 * 
	 */
	class ZimuListTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				zimuListView
						.setBackgroundResource(R.drawable.zimulist_background);
				zimuListView.getBackground().setAlpha(180);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				zimuListView.setBackgroundColor(Color.TRANSPARENT);
			}
			int itemY = v.getHeight() / 26;
			int y = (int) (event.getY() / itemY);
			String str = py[(y > 0) ? (y < 26 ? (y - 1) : 25) : 0];
			int localPosition = binSearch(groupArray, str); // 接收返回值
			if (localPosition != -1) {
//				cityListView.setSelection(localPosition); // 让List指向对应位置的Item
				
				cityListView.setSelectedGroup(localPosition);
				// list.setSelectionAfterHeaderView();
			}
			return false;
		}

	}
	/**
	 * 将选中的py与stringArr的首字符进行匹配并返回对应字符串在数组中的位置
	 * 
	 * @param list
	 * @param s
	 * @return
	 */
	public static int binSearch(ArrayList<HashMap<String, String>> list, String s) {
		for (int i = 0; i < list.size(); i++) {
//			String name = ChineseToEnglish.toEnglish(list.get(i).get("city")
//					.toString().charAt(0));
			if (s.equalsIgnoreCase("" + list.get(i).get("name").toString())) { // 不区分大小写
				return i;
			}
		}
		return -1;
	}
	/**
	 * 字母控件的点击事件监听
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/15
	 * 
	 */
	class ZimuListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String s = ((TextView) arg1).getText().toString().toLowerCase();
			int localPosition = binSearch(groupArray, s); // 接收返回值
			if (localPosition != -1) {
				cityListView.setSelectedGroup(localPosition);
			}
		}

	}

	
}
