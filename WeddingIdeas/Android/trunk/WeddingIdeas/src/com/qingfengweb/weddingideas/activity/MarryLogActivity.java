/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.adapter.MarryLogAdapter;
import com.qingfengweb.weddingideas.beans.WeddingLogBean;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.JsonData;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author 刘星星
 * @createDate 2013、1、3
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) @SuppressLint("HandlerLeak") public class MarryLogActivity extends BaseActivity/* implements OnTouchListener*/{
	ListView listView;
	List<Map<String,Object>> list;
	DBHelper dbHelper;
	MarryLogAdapter adapter = null;
	LinearLayout parentlayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_marrylog);
		initView();
		initData();
	}
	private void initView(){
		listView = (ListView) findViewById(R.id.listView);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		listView.setOnTouchListener(this);
		parentlayout = (LinearLayout) findViewById(R.id.parent);
		parentlayout.setBackgroundColor(Color.parseColor(LoadingActivity.templateList.get(0).get("log_bgcolor")));
		findViewById(R.id.backBtnMarry).setOnClickListener(this);
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
		list = new ArrayList<Map<String,Object>>();
		new Thread(getLogRunnable).start();
	}
	private void notifyAdapter(){
		adapter = new MarryLogAdapter(this, list);
		listView.setAdapter(adapter);
	}
	int sx = 0;
	int sy = 0;
	int ex = 0;
	int ey = 0;
	boolean isIntentRight = false;
	boolean isIntentLeft = false;
	public void onClick(View v) {
		if(v.getId() == R.id.backBtnMarry){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		super.onClick(v);
	}
	/**
	 * 
	 */
	Runnable getLogRunnable = new Runnable() {
		
		@Override
		public void run() {
			String storeid = LoadingActivity.configList.get(0).get("storeid");
			String madeid = LoadingActivity.configList.get(0).get("madeid");
			String sql = "select * from "+WeddingLogBean.tbName+" where userId='"+madeid+"' order by stimeString desc";
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(0);
			}
			String msgStr = RequestServerFromHttp.getweddingLog(storeid, "");
			if(msgStr.startsWith("[")){
				JsonData.jsonWeddingLogData(msgStr, dbHelper.open());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(0);
				}
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			sx = (int) event.getX();
//			sy = (int) event.getY();
//		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
////			return super.onTouchEvent(event);
//		}else if(event.getAction() == MotionEvent.ACTION_UP){
//			ex = (int) event.getX();
//			ey = (int) event.getY();
//			if(Math.abs(sx-ex)<Math.abs(sy-ey)){
//				return super.onTouchEvent(event);
//			}
//			if(sx>ex && (sx-ex)>30){//手指向左滑动
//				if(isIntentRight){
//					return super.onTouchEvent(event);
//				}
//				isIntentRight = true;
//			}else if(sx<ex && (ex-sx)>30){
//				if(isIntentLeft){
//					return super.onTouchEvent(event);
//				}
//				finish();
//				overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
//				isIntentLeft = true;
//			}else{
//				return super.onTouchEvent(event);
//			}
//		}
//		return super.onTouchEvent(event);
//	}
}
