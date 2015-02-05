/**
 * 
 */
package com.qingfengweb.baoqi.propertyInsurance;

import com.qingfengweb.baoqi.ext.SalsemanshipAdapter;
import com.qingfengweb.baoqi.propertyInsurance.ext.InstructionAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author QingFeng
 *
 */
public class SalesmanshipitemActivity extends Activity implements OnClickListener {
	private ProgressDialog readWaitProgressDialog;
	private GridView gridView;
	private Button mSubmit=null;
	private Button mSave=null;
	private Button mBack=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.salsemanship_listitem_layout);
		
		mSubmit= (Button) findViewById(R.id.submit);
		  mSave= (Button) findViewById(R.id.save);
		  mBack= (Button) findViewById(R.id.back);
		 mSubmit.setOnClickListener(this);
		 mSave.setOnClickListener(this);
		 mBack.setOnClickListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK==keyCode){
			Intent intent=new Intent(this,SalesmanshipActivity.class);
			this.startActivity(intent);
			this.finish();
		}
	
		return true;
	}
	public void confirmShowWaitDialog() {
		if (readWaitProgressDialog == null) {
			readWaitProgressDialog = new ProgressDialog(this);
			readWaitProgressDialog
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			readWaitProgressDialog.setMessage("正在提交数据...");
			readWaitProgressDialog.setCancelable(true);
			readWaitProgressDialog.show();
		} else {
			readWaitProgressDialog.show();
		}

	}
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(readWaitProgressDialog!=null){
				readWaitProgressDialog.dismiss();
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "已提交数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "已保存数据", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
				
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.submit){
			new Thread(){
				public void run(){
					try {
						sleep(2000);
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
		}else if(v.getId()==R.id.save){
			new Thread(){
				public void run(){
					try {
						sleep(2000);
						handler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
			readWaitProgressDialog.setMessage("正在保存数据...");
		}else if(v.getId() == R.id.back){
			Intent intent=new Intent(this,SalesmanshipActivity.class);
			this.startActivity(intent);
			this.finish();
		}
	}
}
