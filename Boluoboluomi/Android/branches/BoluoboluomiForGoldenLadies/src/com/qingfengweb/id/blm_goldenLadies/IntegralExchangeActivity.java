package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.qingfengweb.adapter.IntegralExchangeAdapter;
import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.GoodsInfo;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MD5;

public class IntegralExchangeActivity extends BaseActivity {
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
	private ListView listView;
	private List<Map<String, Object>> list;
	private boolean click_limit = true;
	private DBHelper dbHelper = null;
	private String currentStoreid = "";
	private String currentTypeid = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_integralexchange);
		findView();
		findBottomBtn();
		dbHelper = DBHelper.getInstance(this);
		currentStoreid = UserBeanInfo.getInstant().getCurrentStoreId();
		currentTypeid = getIntent().getStringExtra("typeid");
		new Thread(getShopListRunnable).start();
	}
	
	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable getShopListRunnable = new Runnable() {

		@Override
		public void run() {
			String sql = "select *from "+GoodsInfo.TableName+" where storeid = "+currentStoreid+" order by typeid";
			list = dbHelper.selectRow(sql, null);
			handler.sendEmptyMessage(0);
			String msgStr = RequestServerFromHttp.getGoodsList(currentStoreid, "", "");
			if(msgStr.equals("404")){//访问服务器失败
				
			}else if(msgStr.length()<5){
				
			}else if(msgStr.startsWith("[")){//访问服务器成功
				JsonData.jsonGoodsData(msgStr, dbHelper.open(), currentStoreid);
				list = dbHelper.selectRow(sql, null);
				handler.sendEmptyMessage(0);
			}
		}

	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter();
			}
			super.handleMessage(msg);
		}
		
	};
	private void findView() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setCacheColorHint(0);
	}
	
	private void notifyAdapter(){
		IntegralExchangeAdapter adapter = new IntegralExchangeAdapter(this, list);
		listView.setAdapter(adapter);
		
//		listView.setTop(top)
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(UserBeanInfo.getInstant().isLogined){
			Intent i = new Intent(IntegralExchangeActivity.this,DetailIntegralActivity.class);
			i.putExtra("integralMap", (Serializable)list.get(arg2));
			startActivity(i);
			finish();
		}else{
			Intent i = new Intent(IntegralExchangeActivity.this,LoginActivity.class);
			i.putExtra("activityType", 5);
			i.putExtra("integralMap", (Serializable)list.get(arg2));
			startActivity(i);
			finish();
		}
		
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	

	@Override
	protected void onDestroy() {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("image") != null)
					((Bitmap) list.get(i).get("image")).recycle();
			}
			list.clear();
			list = null;
		}

		super.onDestroy();
	}

	private void findBottomBtn() {
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
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		if (v == tab1) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab2) {
			Intent i = new Intent(this, RecommendFriendActivity.class);
			startActivity(i);
			finish();
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
		}else if (v == backBtn) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		click_limit = true;
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
