/**
 * 
 */
package com.qingfengweb.client.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qingfengweb.android.R;
import com.qingfengweb.client.bean.CompanyInfo;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.database.DBHelper;

/**
 * @author 刘星星
 * @createDate 2013/6/20
 *
 */
public class DetailCompanyIntroActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;
	private TextView content;
	DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailcompany);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		content = ((TextView)findViewById(R.id.companyIntro));
		backBtn.setOnClickListener(this);
		dbHelper = DBHelper.getInstance(this);
		new Thread(getlocalRunnable).start();
	}
	
	private Runnable getlocalRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select * from "+CompanyInfo.TableName;
			List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				Message msg = new Message();
				msg.obj = list;
				msg.what = 3;
				handler.sendMessage(msg);
			}else{
				handler.sendEmptyMessage(4);
			}
		}
	};
	
	private Runnable getServiceRunnable = new Runnable() {
			
			@Override
			public void run() {
				String msgStr = AccessServer.getContentDetail("7");
				if(AccessServer.isNoData(msgStr)){//服务器返回无数据
					handler.sendEmptyMessage(1);
				}else if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(2);
				}else{//从服务器得到了正确的数据
					ContentValues contentValues = new ContentValues();
					contentValues.put("content", msgStr);
					dbHelper.insert(CompanyInfo.TableName, contentValues);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = msgStr;
					handler.sendMessage(msg);
				}
				
			}
		};
		
	Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//得到数据的处理， 包括本地的和服务器的数据
				content.setText(msg.obj.toString());
			}else if(msg.what == 1){//服务器返回无数据
				
			}else if(msg.what == 2){//访问服务器失败
				
			}else if(msg.what == 3){//本地有数据
				content.setText(((List<Map<String,Object>>)msg.obj).get(0).get("content").toString());
			}else if(msg.what == 4){//本地无数据  开启向服务器获取数据的线程。
				new Thread(getServiceRunnable).start();
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		
	}
}
