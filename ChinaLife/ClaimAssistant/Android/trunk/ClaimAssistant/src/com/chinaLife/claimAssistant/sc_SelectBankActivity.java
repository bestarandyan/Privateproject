/**
 * 
 */
package com.chinaLife.claimAssistant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author 刘星星
 * @createDate 2013/1/17
 *选择银行总行的类
 */
public class sc_SelectBankActivity extends Activity{
	private Button btn;//搜索按钮
	private ListView listView;//银行列表
	private EditText et;//搜索输入框
	private List<Map<String, Object>> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_selectbank);
		btn = (Button) findViewById(R.id.searchBtn);
		listView = (ListView) findViewById(R.id.bankList);
		et = (EditText) findViewById(R.id.searchEt);
		et.addTextChangedListener(new MyTextWatcher());
		initListData();
	}
	
	/***
	 * 监听文本改变
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			initListData();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
		
	}
	
	private void initListData(){
		if(list == null){
			list = new ArrayList<Map<String,Object>>();
		}
		list.clear();
		list = getBank(et.getText().toString());
		notifiListView();
	}
	private void notifiListView(){
		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.sc_item_banklist,
				new String[]{"name"}, new int[]{R.id.cityItemText});
		listView.setAdapter(sa);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(new CityListViewItemClickListener());
	}
	
	class CityListViewItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			intent.putExtra("bank_name", list.get(arg2).get("name").toString());
			intent.putExtra("bank_code", list.get(arg2).get("code").toString());
			setResult(RESULT_OK,intent);
			finish();
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	SQLiteDatabase db;
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ChinaLife";
	private String DATABASE_FILENAME = "bank.sqlite.sqlite";

	// 初始化数据库
	private SQLiteDatabase openDatabase() {
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			if (!dir.exists())
				dir.mkdir();
			if (!(new File(databaseFilename)).exists()) {
				InputStream is = getAssets().open("bank.sqlite.sqlite");
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return db;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getBank(String str) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Cursor mCursor = null;
		String sql = "select name,code from bank";
		if(!str.trim().equals("")){
			sql = "select name,code from bank where name like '%"+str+"%'";
		}
		try {
			db = openDatabase();
			mCursor = db.rawQuery(sql, null);
			Map<String, Object> map = null;
			int iColumnCount = mCursor.getColumnCount();
			while (mCursor.moveToNext()) {
				map = new HashMap<String, Object>();
				for (int i = 0; i < iColumnCount; i++) {
					map.put(mCursor.getColumnName(i).toLowerCase(),
							mCursor.getString(i));
				}
				result.add(map);
			}
		} catch (Exception e) {
			Log.e("bank_dbhelp","selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		return result;
	}
}
