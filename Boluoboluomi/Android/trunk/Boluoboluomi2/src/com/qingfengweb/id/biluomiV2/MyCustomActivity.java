package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.qingfengweb.adapter.MyCustomAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.model.MyCustomInfo;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MD5;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyCustomActivity extends BaseActivity {
	private Button backBtn;
	private ListView listView;
	List<Map<String, Object>> list = null;
	private ProgressDialog progressdialog;
	private MyCustomAdapter mycustomadapter = null;
	public DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_mycustom);
		findView();
		initListView();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void initListView() {
		list = new ArrayList<Map<String, Object>>();
		listView.setCacheColorHint(0);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		dbHelper = DBHelper.getInstance(this);
		new Thread(getListDataRunnable).start();
	}
	public void notifyAdapter(){
		mycustomadapter=new MyCustomAdapter(this, list);
		listView.setAdapter(mycustomadapter);
	}
	/**
	 * 获取列表数据
	 */
	Runnable getListDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select * from "+MyCustomInfo.TableName+" where username="+UserBeanInfo.getInstant().getUserName()+" order by createtime desc";
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			String msg = RequestServerFromHttp.getMyCustom();
			if(msg!=null && msg.length()>3 && msg.startsWith("[")){//获取成功且有数据
				JsonData.jsonMyCustomData(msg, dbHelper.open());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
			}else if(JsonData.isNoData(msg) || msg.equals("[")){//无数据
				handler.sendEmptyMessage(1);
			}else if(msg.equals("404")){
				handler.sendEmptyMessage(2);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//获取数据成功
				notifyAdapter();
			}else if(msg.what == 1){//无数据
				
			}else if(msg.what == 2){//访问服务器失败
				
			}
			super.handleMessage(msg);
		}
		
	};
	private void getListData() {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("text", "2012年11月1日定制浪漫满屋");
		map1.put("image1", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tu));
		map1.put("image2", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tuli02));
		map1.put("image3", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tuli03));
		list.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("text", "2012年11月1日定制浪漫满屋");
		map2.put("image1", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tuli01));
		map2.put("image2", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tuli02));
		map2.put("image3", BitmapFactory.decodeResource(getResources(),
				R.drawable.customize_photo_tuli03));
		list.add(map2);

	}


	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			Intent intent = new Intent(this, CustomMainActivity.class);
			startActivity(intent);
			finish();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, CustomMainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
