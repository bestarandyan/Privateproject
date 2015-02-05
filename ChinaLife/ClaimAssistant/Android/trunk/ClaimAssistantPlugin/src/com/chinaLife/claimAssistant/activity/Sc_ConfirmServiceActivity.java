package com.chinaLife.claimAssistant.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.bean.sc_ClaimInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class Sc_ConfirmServiceActivity extends Activity implements OnClickListener {

	private ImageButton imagebtn1, imagebtn2;
	private Button back;
	private int tag = 0;// 0 caselistActivity 1 caseinfoActivity

	public static sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	sc_UploadData uploaddata = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_confirmservice);
		Sc_ExitApplication.getInstance().context = Sc_ConfirmServiceActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_ConfirmServiceActivity.this);
		initView();
		initData();
		initViewFun();
		myHandler.sendEmptyMessage(1);
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_ConfirmServiceActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
						.setMessage(getResources().getString(R.string.sc_confirmstate1)
								+"\n\n"+
								getResources().getString(R.string.sc_confirmstate2))
						.setPositiveButton("知道了", null);
				callDailog.show();
			}
			
		}
	};
	private void initViewFun() {
		imagebtn1.setOnClickListener(this);
		imagebtn2.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private void initData() {
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			return;
		}
		

		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);

	}

	private void initView() {
		imagebtn1 = (ImageButton) findViewById(R.id.imagebtn1);
		imagebtn2 = (ImageButton) findViewById(R.id.imagebtn2);
		back = (Button) findViewById(R.id.fanhui);
	}

	@Override
	public void onClick(View v) {
		if (v == imagebtn1) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					Sc_ConfirmServiceActivity.this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
					.setMessage("您确定选择自助查勘服务吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									comfirmService(1);// 自助查勘
								}
							}).setNegativeButton("取消", null).show();
		} else if (v == imagebtn2) {

			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					Sc_ConfirmServiceActivity.this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
					.setMessage("您确定选择查勘定损服务吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									comfirmService(2);// 查勘定损
								}
							}).setNegativeButton("取消", null).show();
		} else if (v == back) {
			if (tag == 0) {
				Intent intent = new Intent(this, Sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			} else if (tag == 2) {
				Intent intent = new Intent(this, Sc_MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(this, Sc_CaseInfoActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (tag == 0) {
				Intent intent = new Intent(this, Sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			} else if (tag == 2) {
				Intent intent = new Intent(this, Sc_MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(this, Sc_CaseInfoActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			}

		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

	public void comfirmService(int type) {
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			if (type == 1) {
				Sc_MyApplication.getInstance().setCasedescription_tag(2);
				Sc_MyApplication.getInstance().setPhoto_tag(1);
				Intent i = new Intent(this, Sc_CaseOfOnlyOneActivity.class);
				this.startActivity(i);
				this.finish();
			} else {
				Intent i = new Intent(this, Sc_CaseHandlerFlowActivity.class);
				this.startActivity(i);
				this.finish();
			}
			Sc_MyApplication.getInstance().setServer_type(type);
			Sc_MyApplication.getInstance().setClaimidstate(3);
			Sc_MyApplication.getInstance().setClaimid("x00000001");
			return;
		}
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "serviceType"));
			params.add(new BasicNameValuePair("caseid", Sc_MyApplication
					.getInstance().getCaseid()));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("type", type + ""));
			progressdialog.setMessage("系统处理中，请稍等...");
			progressdialog.show();
			uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 4);
			new Thread(runnable).start();

		}
	}
	
	public Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			uploaddata.Post();
		}
	};

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
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

	public void parseJson(int type,String reponse) {
		progressdialog.dismiss();
		if(type == 2) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage("选择的服务方式提交到服务器失败，请确认网络是否畅通。")
					.setPositiveButton("我知道了", null).show();
			return;
		}
		Gson gson = new Gson();// 创建Gson对象，
		ContentValues values = new ContentValues();
		sc_ClaimInfo claiminfo = gson.fromJson(reponse, sc_ClaimInfo.class);
		values.put("claimid", claiminfo.getClaimid());
		Sc_MyApplication.getInstance().setClaimid(claiminfo.getClaimid());
		Sc_MyApplication.getInstance().setClaimidstate(
				Integer.parseInt(claiminfo.getStatus()));
		values.put("caseid", claiminfo.getCaseid());
		values.put("service_type", claiminfo.getServicetype());
		values.put("status", claiminfo.getStatus());
		values.put("status_text", claiminfo.getStatustext());
		database.insert("claiminfo", values);
		database.close();
		//System.out.println(Integer.parseInt(claiminfo.getServicetype()));
		if (Integer.parseInt(claiminfo.getServicetype()) == 1) {
			Sc_MyApplication.getInstance().setCasedescription_tag(2);
			Sc_MyApplication.getInstance().setPhoto_tag(1);
			Intent i = new Intent(this, Sc_CaseOfOnlyOneActivity.class);
			this.startActivity(i);
			this.finish();
		} else {
			Intent i = new Intent(this, Sc_CaseHandlerFlowActivity.class);
			this.startActivity(i);
			this.finish();
		}

	}

}
