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
 * @author ������
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
				if(AccessServer.isNoData(msgStr)){//����������������
					handler.sendEmptyMessage(1);
				}else if(msgStr.equals("404")){//���ʷ�����ʧ��
					handler.sendEmptyMessage(2);
				}else{//�ӷ������õ�����ȷ������
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
			if(msg.what == 0){//�õ����ݵĴ��� �������صĺͷ�����������
				content.setText(msg.obj.toString());
			}else if(msg.what == 1){//����������������
				
			}else if(msg.what == 2){//���ʷ�����ʧ��
				
			}else if(msg.what == 3){//����������
				content.setText(((List<Map<String,Object>>)msg.obj).get(0).get("content").toString());
			}else if(msg.what == 4){//����������  �������������ȡ���ݵ��̡߳�
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
