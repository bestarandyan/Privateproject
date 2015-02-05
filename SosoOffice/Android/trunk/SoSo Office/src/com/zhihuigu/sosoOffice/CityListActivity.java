/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhihuigu.sosoOffice.LinkmanActivity.ZimuListItemClickListener;
import com.zhihuigu.sosoOffice.LinkmanActivity.ZimuListTouchListener;
import com.zhihuigu.sosoOffice.Adapter.CityListAdapter;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.ChineseToEnglish;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.GetBuilds;
import com.zhihuigu.sosoOffice.utils.GetLocation;
import com.zhihuigu.sosoOffice.utils.ListDispose;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
import com.zhihuigu.sosoOffice.utils.ZoneUtil;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013/1/7 选择城市的类
 * 
 */
public class CityListActivity extends BaseActivity {
	private Button backBtn;
	private EditText searchEt;
	private ListView cityListView;
	private ListView zimuListView;
	private ArrayList<Map<String, Object>> list;// 所有房源列表
	private ArrayList<Map<String, Object>> searchList;// 通过搜索加载出来的列表
	private ArrayList zimuList;// 所有城市的字母列表
	private ArrayList searchZimuList;// 通过搜素加载出来的字母列表
	private TextView currentCity;
	// private ProgressDialog dialog;
	GetLocation getLocation;
	private LinearLayout cityLayout;// 显示当前城市的布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_citylist);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		zimuList = new ArrayList();
		searchZimuList = new ArrayList();
		backBtn.setOnClickListener(this);
		searchEt = (EditText) findViewById(R.id.cityEt);
		searchEt.addTextChangedListener(new MyTextWatcher());
		cityListView = (ListView) findViewById(R.id.cityList);
		currentCity = (TextView) findViewById(R.id.currentCityText);
		currentCity.setOnClickListener(this);
		cityLayout = (LinearLayout) findViewById(R.id.cityLayout);
		cityLayout.setOnClickListener(this);
		zimuListView = (ListView) findViewById(R.id.zimuList);
		zimuListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		new Thread(cityRunnable).start();
		notifiViewZimu();
		currentCity.setText("正在定位···");
		getLocation = new GetLocation(this);
		getLocation.getLocation();
		new Thread(runnable).start();
	}

	Runnable cityRunnable = new Runnable() {
		@Override
		public void run() {
			initListData();
			System.out.println(list.size() + "排序前");
			list = ListDispose.sortList1(list, zimuList, "zimu");// 将集合按拼音排序
			System.out.println(list.size() + "排序后");
			handler.sendEmptyMessage(1);
		}
	};
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			String currentCityStr = MyApplication.getInstance()
					.getCurrentCity();
			while (true) {
				currentCityStr = MyApplication.getInstance().getCurrentCity();
				if (currentCityStr != null && !currentCityStr.equals("")) {
					break;
				}
			}
			handler.sendEmptyMessage(0);
		}

	};
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String currentCityStr = MyApplication.getInstance()
						.getCurrentCity();
				if (currentCityStr != null) {
					currentCity.setText(currentCityStr);
				}
				// dialog.dismiss();
				getLocation.unRegisterLocation();
			} else if (msg.what == 1) {
				// dialog.dismiss();
				notifiListView(list);
			}

			super.handleMessage(msg);
		}

	};

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
			String str = Constant.py[(y > 0) ? (y < 26 ? (y - 1) : 25) : 0];
			int localPosition = binSearch(list, str); // 接收返回值
			if (localPosition != -1) {
				cityListView.setSelection(localPosition); // 让List指向对应位置的Item
				cityListView.setSelectionFromTop(localPosition, localPosition);
				// list.setSelectionAfterHeaderView();
			}
			return false;
		}

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
			int localPosition = binSearch(list, s); // 接收返回值
			if (localPosition != -1) {
				cityListView.setSelection(localPosition); // 让List指向对应位置的Item
				cityListView.setSelectionFromTop(localPosition, localPosition);
				// list.setSelectionAfterHeaderView();
			}
		}

	}

	/**
	 * @author 刘星星
	 * @createDate 2013/1/15 加载字母数据
	 */
	public void notifiViewZimu() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_zimu, Constant.py);
		zimuListView.setAdapter(adapter);
		zimuListView.setDivider(null);
		zimuListView.setOnItemClickListener(new ZimuListItemClickListener());
		zimuListView.setOnTouchListener(new ZimuListTouchListener());
		zimuListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	/**
	 * 将选中的py与stringArr的首字符进行匹配并返回对应字符串在数组中的位置
	 * 
	 * @param list
	 * @param s
	 * @return
	 */
	public static int binSearch(ArrayList<Map<String, Object>> list, String s) {
		for (int i = 0; i < list.size(); i++) {
//			String name = ChineseToEnglish.toEnglish(list.get(i).get("city")
//					.toString().charAt(0));
			if (s.equalsIgnoreCase("" + list.get(i).get("zimu").toString())) { // 不区分大小写
				return i;
			}
		}
		return -1;
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
				if (list != null && list.size() > 0) {
					notifiListView(list);
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
		if (searchList == null) {
			searchList = new ArrayList<Map<String, Object>>();
		} else {
			searchList.clear();
		}
		String sql = "select id,name,pinyin from region where (name like '"
				+ whereClause + "%' or pinyin like '" + whereClause
				+ "%') and type = '2' order by name";
		List<Map<String, Object>> selectresult = ZoneUtil.getregion(this, sql);
		searchZimuList.clear();
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, Object> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("name") != null
						&& selectresult.get(i).get("id") != null
						&& selectresult.get(i).get("pinyin") != null) {
					map = new HashMap<String, Object>();
					String name = selectresult.get(i).get("name").toString();
					String zimu = selectresult.get(i).get("pinyin").toString()
							.substring(0, 1);// ChineseToEnglish.toEnglish(name.charAt(0)).substring(0,
												// 1);
					map.put("id", selectresult.get(i).get("id").toString());
					map.put("city", name);
					map.put("zimu", zimu);
					if (!searchZimuList.contains(zimu)) {
						searchZimuList.add(zimu);
					}
					searchList.add(map);
					/*
					 * String name = selectresult.get(i).get("name").toString();
					 * cityList.add(new
					 * cityBean(selectresult.get(i).get("id").toString(),
					 * selectresult.get(i).get("name").toString(),
					 * ChineseToEnglish.toEnglish(name.charAt(0)).charAt(0) ));
					 */}
			}
		}

		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		searchList = ListDispose.sortList1(searchList, searchZimuList, "zimu");// 将集合按拼音排序
		notifiListView(searchList);
	}

	/***
	 * author by Ring 从数据库查出所有城市 whereClause查询条件
	 */
	public void initListData() {
		String sql = "";
		sql = "select id,name,pinyin from region where type = '2'";
		List<Map<String, Object>> selectresult = ZoneUtil.getregion(this, sql);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}

		list.clear();
		zimuList.clear();
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, Object> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("name") != null
						&& selectresult.get(i).get("id") != null
						&& selectresult.get(i).get("pinyin") != null) {
					map = new HashMap<String, Object>();
					String name = selectresult.get(i).get("name").toString();
					String zimu = selectresult.get(i).get("pinyin").toString()
							.substring(0, 1);// ChineseToEnglish.toEnglish(name.charAt(0)).substring(0,
												// 1);
					map.put("id", selectresult.get(i).get("id").toString());
					map.put("city", name);
					map.put("zimu", zimu);
					if (!zimuList.contains(zimu)) {
						zimuList.add(zimu);
					}
					list.add(map);
					/*
					 * String name = selectresult.get(i).get("name").toString();
					 * cityList.add(new
					 * cityBean(selectresult.get(i).get("id").toString(),
					 * selectresult.get(i).get("name").toString(),
					 * ChineseToEnglish.toEnglish(name.charAt(0)).charAt(0) ));
					 */}
			}
		}

		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}

	}

	private void notifiListView(ArrayList<Map<String, Object>> list) {
		// SimpleAdapter sa = new SimpleAdapter(this, list,
		// R.layout.item_city_list, new String[] { "city" },
		// new int[] { R.id.cityItemText });

		CityListAdapter adapter = new CityListAdapter(this, list);
		cityListView.setAdapter(adapter);
		cityListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		cityListView.setCacheColorHint(0);
		cityListView
				.setOnItemClickListener(new CityListViewItemClickListener());
	}

	class CityListViewItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (searchList != null && searchList.size() > 0) {
				MyApplication.getInstance(CityListActivity.this).setCityid(
						searchList.get(arg2).get("id").toString());
				MyApplication.getInstance(CityListActivity.this).setCityname(
						searchList.get(arg2).get("city").toString());
			} else {
				MyApplication.getInstance(CityListActivity.this).setCityid(
						list.get(arg2).get("id").toString());
				MyApplication.getInstance(CityListActivity.this).setCityname(
						list.get(arg2).get("city").toString());
			}
			Intent intent = new Intent(CityListActivity.this,
					MainTabActivity.class);
			intent.putExtra("tag", 1);
			startActivity(intent);
			getLocation.unRegisterLocation();
			CityListActivity.this.finish();
			// Window w = MainFirstTab.group.getLocalActivityManager()
			// .startActivity("MainActivity",intent);
			// View view = w.getDecorView();
			// MainFirstTab.group.setContentView(view);
			// TextView mainTopCityText = (TextView)
			// view.findViewById(R.id.mainTopCityText);
			// mainTopCityText.setText(list.get(arg2).get("city").toString());
			// System.out.println(list.get(arg2));
		}

	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub // super.onBackPressed(); // 后退跳转到第一个Activity界面 if
	 * (getIntent().getIntExtra("tag", 0) == 1) { finish(); } else { Intent
	 * intent = new Intent(this, MainActivity.class)
	 * .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * 
	 * Window w = MainFirstTab.group.getLocalActivityManager()
	 * .startActivity("MainActivity", intent); View view = w.getDecorView();
	 * MainFirstTab.group.setContentView(view); }
	 * 
	 * }
	 */
	/**
	 * 从数据库查出匹配城市 的id whereClause查询条件
	 * 
	 * 作者：Ring 创建于：2013-2-1
	 * 
	 * @param whereClause
	 */
	public String getCitybySomeThing(String whereClause) {
		String id = "";
		String sql = "";
		if (whereClause == null || whereClause.equals("")) {
			sql = "select id,name from region where type = '2'";
		} else {
			sql = "select id,name from region where (name like '" + whereClause
					+ "%' or pinyin like '" + whereClause
					+ "%') and type = '2'";
		}
		List<Map<String, Object>> selectresult = ZoneUtil.getregion(this, sql);
		if (selectresult != null && selectresult.size() > 0
				&& selectresult.get(0).get("id").toString() != null) {
			id = selectresult.get(0).get("id").toString();
		}

		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		return id;
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			if (getIntent().getIntExtra("tag", 0) == 1) {
				showExitDialog(this);
			} else {
				Intent intent = new Intent(CityListActivity.this,
						MainTabActivity.class);
				intent.putExtra("tag", 1);
				startActivity(intent);
				getLocation.unRegisterLocation();
				CityListActivity.this.finish();
			}
		} else if (v == currentCity) {// 确定城市按钮
			if (!currentCity.getText().toString().trim().equals("")
					&& !currentCity.getText().toString().trim().equals("null")
					&& !currentCity.getText().toString().trim()
							.equals("正在定位···")) {
				MyApplication.getInstance(CityListActivity.this).setCityname(
						currentCity.getText().toString());
				MyApplication.getInstance(CityListActivity.this).setCityid(
						getCitybySomeThing(currentCity.getText().toString()));
				Intent intent = new Intent(CityListActivity.this,
						MainTabActivity.class);
				intent.putExtra("tag", 1);
				CityListActivity.this.startActivity(intent);
				CityListActivity.this.finish();
			}
		} else if (v == cityLayout) {
			if (!currentCity.getText().toString().trim().equals("")
					&& !currentCity.getText().toString().trim().equals("null")
					&& !currentCity.getText().toString().trim()
							.equals("正在定位···")) {
				MyApplication.getInstance(CityListActivity.this).setCityname(
						currentCity.getText().toString());
				MyApplication.getInstance(CityListActivity.this).setCityid(
						getCitybySomeThing(currentCity.getText().toString()));
				Intent intent = new Intent(CityListActivity.this,
						MainTabActivity.class);
				intent.putExtra("tag", 1);
				CityListActivity.this.startActivity(intent);
				CityListActivity.this.finish();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getIntent().getIntExtra("tag", 0) == 1) {
				showExitDialog(this);
			} else {
				Intent intent = new Intent(CityListActivity.this,
						MainTabActivity.class);
				intent.putExtra("tag", 1);
				startActivity(intent);
				getLocation.unRegisterLocation();
				CityListActivity.this.finish();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		String userid = "0";
		if(MyApplication.getInstance().getSosouserinfo()!=null&&MyApplication.getInstance().getSosouserinfo().getUserID()!=null){
			userid = MyApplication.getInstance().getSosouserinfo().getUserID();
		}
		ContentValues values = new ContentValues();
		values.put("cityid", MyApplication.getInstance().getCityid());
		DBHelper.getInstance(this).update(
				"sososettinginfo",
				values,
				"userid = ?",
				new String[] {userid});
		if (values != null) {
			values.clear();
			values = null;
		}
	}

}
