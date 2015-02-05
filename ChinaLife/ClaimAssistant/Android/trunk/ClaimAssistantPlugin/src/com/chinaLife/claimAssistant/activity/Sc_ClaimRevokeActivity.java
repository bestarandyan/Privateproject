package com.chinaLife.claimAssistant.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.adapter.sc_SpinnerAdapter;
import com.chinaLife.claimAssistant.bean.sc_GetText;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Sc_ClaimRevokeActivity extends Activity implements OnClickListener {

	private EditText ed;// 撤销描述
	private Spinner sp;// 撤销原因
	private Button btn1, btn2, back;// 提交按钮 返回按钮 顶部返回按钮

	private int tag = 0; // 0 caseInfoActivity,1 CaseListActivity
							// 接受activity之间的跳转参数

	private String caseid;// 案件
	public static sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_claimrevoked);
		Sc_ExitApplication.getInstance().context = Sc_ClaimRevokeActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_ClaimRevokeActivity.this);
		initView();
		initData();
		initViewFun();
	}
	private void initView() {
		ed = (EditText) findViewById(R.id.ed1);
		sp = (Spinner) findViewById(R.id.sp1);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		back = (Button) findViewById(R.id.fanhui);
	}

	private void initData() {

		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);

		caseid = Sc_MyApplication.getInstance().getCaseid();
		
		Intent i = getIntent();
		tag = i.getIntExtra("tag", 0);
		String numbers[]=new String[] {
						"车辆损失轻微，无需修理", "自行修理，无需保险公司处理",
						"本软件操作过于繁琐", "其他" };
		sc_SpinnerAdapter adapter = new sc_SpinnerAdapter(this,
	            android.R.layout.simple_spinner_item, numbers);
		sp.setPrompt("请选择撤销原因：");
	    sp.setAdapter(adapter);
		
	}
	
	private void initViewFun() {
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	public void showDialog(){
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setIcon(android.R.drawable.ic_dialog_dialer);
		ab.setTitle("提示:");
		ab.setMessage("案件撤销后，本次事故赔偿责任将终止，您是否继续撤销案件？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
					Intent i = new Intent(Sc_ClaimRevokeActivity.this,Sc_CaseListActivity.class);
					Sc_MyApplication.getInstance().setCaseidstate(Sc_MyApplication.getInstance().getCaseidstate()+4);
					Sc_MyApplication.getInstance().setClaimidstate(Sc_MyApplication.getInstance().getClaimidstate()+32768);
					Sc_ClaimRevokeActivity.this.startActivity(i);
					Sc_ClaimRevokeActivity.this.finish();
					return;
				}
				if(!sc_GetText.isActive(Sc_MyApplication.getInstance().getClaimidstate()
						, Sc_MyApplication.getInstance().getCaseidstate())){
					AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_ClaimRevokeActivity.this);
					callDailog.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("提示").setMessage("该案件已撤销")
							.setPositiveButton("确定", null).show();
					return;
				}
				progressdialog.setMessage("系统处理中，请稍等...");
				progressdialog.show();
				new Thread(runnable).start();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		ab.show();
	}
	@Override
	public void onClick(View v) {
		if (v == btn1) {
			showDialog();
		} else if (v == btn2) {
			if (tag == 0) {
				Intent i = new Intent(this, Sc_CaseInfoActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			} else {
				Intent i = new Intent(this, Sc_CaseListActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			}
		} else if (v == back) {
			if (tag == 0) {
				Intent i = new Intent(this, Sc_CaseInfoActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			} else {
				Intent i = new Intent(this, Sc_CaseListActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			}
		}
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			revokeCase();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (tag == 0) {
				Intent i = new Intent(this, Sc_CaseInfoActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			} else {
				Intent i = new Intent(this, Sc_CaseListActivity.class);
				i.putExtra("back_activity", 1);//返回
				startActivity(i);
				this.finish();
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//			Intent i = new Intent(this, sc_MainActivity.class);
//			startActivity(i);
//			finish();
//			break;
//		case R.id.menu_exit:
//			sc_ExitApplication.getInstance().showExitDialog();
//			break;
//		default:
//			// 对没有处理的事件，交给父类来处理
//			return super.onOptionsItemSelected(item);
//		}
//		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
//		return super.onOptionsItemSelected(item);
//	}

	public void revokeCase() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "cancelCase"));
			params.add(new BasicNameValuePair("reason", (sp
					.getSelectedItemPosition() + 1) + ""));
			params.add(new BasicNameValuePair("caseid", caseid));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("remark", ed.getText().toString()));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "case", params, 3);
			uploaddata.Post();
//			UploadData.startMyThread(MyApplication.URL + "case", params, 3);

		}else{
			handler.sendEmptyMessage(1);
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				if(progressdialog.isShowing()){
					progressdialog.dismiss();
				}
			}
			
		}
		
	};

	public void parseJson(int type) {
		if(type == 2){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_ClaimRevokeActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage("对不起，案件撤销失败！")
					.setPositiveButton("确定", null).show();
			return;
		}
		ContentValues values = new ContentValues();
		values.put("case_reason", sp.getSelectedItem().toString());
		values.put("case_remark", ed.getText().toString());
		values.put("status", 4);
		if((Sc_MyApplication.getInstance().getCaseidstate()&4)!=4){
			Sc_MyApplication.getInstance().setCaseidstate(Sc_MyApplication.getInstance().getCaseidstate()+4);
		}
		if((Sc_MyApplication.getInstance().getClaimidstate()&32768)!=32768){
			Sc_MyApplication.getInstance().setCaseidstate(Sc_MyApplication.getInstance().getCaseidstate()+32768);
		}
		
		database.update("caseinfo", values, "caseid = ?",
				new String[] { caseid });
		database.close();
		
		AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_ClaimRevokeActivity.this);
		callDailog.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("提示").setMessage("您已成功撤销赔案。")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						if(Sc_MyApplication.getInstance().getMytimer()!=null){
							Sc_MyApplication.getInstance().getMytimer().cancel();
						}
						if (tag == 0) {
							Intent i = new Intent(Sc_ClaimRevokeActivity.this, Sc_CaseInfoActivity.class);
							startActivity(i);
							Sc_ClaimRevokeActivity.this.finish();
						} else {
							Intent i = new Intent(Sc_ClaimRevokeActivity.this, Sc_CaseListActivity.class);
							startActivity(i);
							Sc_ClaimRevokeActivity.this.finish();
						}
					}
				}).show();
		
	}
	
}
