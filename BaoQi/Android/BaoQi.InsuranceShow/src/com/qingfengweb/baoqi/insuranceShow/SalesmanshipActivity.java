/**
 * 
 */
package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.ext.SalsemanshipAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;
import com.qingfengweb.baoqi.insuranceShow.ext.InstructionAdapter;

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
public class SalesmanshipActivity extends Activity implements OnClickListener {
	private ProgressDialog readWaitProgressDialog;
	private GridView gridView;
	private  ScrollView linearlayout=null;
	private  LinearLayout bottom_layout;
	private Button mSubmit=null;
	private Button mSave=null;
	private Button mBack=null;
	private int tab=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.salsemanship_list_layout);
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setAdapter(new SalsemanshipAdapter(this));	
		linearlayout=(ScrollView) findViewById(R.id.linearlayout);
		bottom_layout=(LinearLayout) findViewById(R.id.bottom_layout);
		 mSubmit= (Button) findViewById(R.id.submit);
		  mSave= (Button) findViewById(R.id.save);
		  mBack= (Button) findViewById(R.id.back);
		 mSubmit.setOnClickListener(this);
		 mSave.setOnClickListener(this);
		 mBack.setOnClickListener(this);
		 tab=1;
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
//				switch(position){
//				case 0: 
				 tab=2;
					gridView.setVisibility(View.GONE);
					linearlayout.setVisibility(View.VISIBLE);
					bottom_layout.setVisibility(View.VISIBLE);
//				break;
//				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		super.onKeyDown(keyCode, event);
		if( tab==1){
		Intent intent=new Intent(this,InsuranceShowMainActivity.class);
		this.startActivity(intent);
		this.finish();
		}else if(tab==2){
			tab=1;
			gridView.setVisibility(View.VISIBLE);
			linearlayout.setVisibility(View.GONE);
			bottom_layout.setVisibility(View.GONE);
			return false;
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
						handler.sendEmptyMessage(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			confirmShowWaitDialog();
			readWaitProgressDialog.setMessage("正在保存数据...");
		}else if(v.getId()==R.id.back){
			tab=1;
			gridView.setVisibility(View.VISIBLE);
			linearlayout.setVisibility(View.GONE);
			bottom_layout.setVisibility(View.GONE);
			
		}
	}
}
