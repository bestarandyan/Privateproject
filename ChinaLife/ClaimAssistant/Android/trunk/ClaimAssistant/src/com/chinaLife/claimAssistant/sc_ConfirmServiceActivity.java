package com.chinaLife.claimAssistant;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.bean.sc_ClaimInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.thread.sc_UploadData3;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
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
import android.widget.ImageButton;
import android.widget.Toast;

public class sc_ConfirmServiceActivity extends Activity implements
		OnClickListener {

	private ImageButton imagebtn1, imagebtn2;
	private Button back;
	private int tag = 0;// 0 caselistActivity 1 caseinfoActivity
	public static sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	public int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_confirmservice);
		sc_ExitApplication.getInstance().context = sc_ConfirmServiceActivity.this;
		sc_ExitApplication.getInstance().addActivity(
				sc_ConfirmServiceActivity.this);
		initView();
		initData();
		initViewFun();
		myHandler.sendEmptyMessage(1);
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(
						sc_ConfirmServiceActivity.this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
						.setMessage(
								getResources().getString(
										R.string.sc_confirmstate1)
										+ "\n\n"
										+ getResources().getString(
												R.string.sc_confirmstate2))
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
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			return;
		}

		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();

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
					sc_ConfirmServiceActivity.this);
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
					sc_ConfirmServiceActivity.this);
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
				Intent intent = new Intent(this, sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);// 返回
				startActivity(intent);
				finish();
			} else if (tag == 2) {
				Intent intent = new Intent(this, sc_MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(this, sc_CaseInfoActivity.class);
				intent.putExtra("back_activity", 1);// 返回
				startActivity(intent);
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (tag == 0) {
				Intent intent = new Intent(this, sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);// 返回
				startActivity(intent);
				finish();
			} else if (tag == 2) {
				Intent intent = new Intent(this, sc_MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(this, sc_CaseInfoActivity.class);
				intent.putExtra("back_activity", 1);// 返回
				startActivity(intent);
				finish();
			}

		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

	public void comfirmService(int type) {
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			if (type == 1) {
				sc_MyApplication.getInstance().setCasedescription_tag(2);
				sc_MyApplication.getInstance().setPhoto_tag(1);
				Intent i = new Intent(this, sc_CaseOfOnlyOneActivity.class);
				this.startActivity(i);
				this.finish();
			} else {
				Intent i = new Intent(this, sc_CaseHandlerFlowActivity.class);
				this.startActivity(i);
				this.finish();
			}
			sc_MyApplication.getInstance().setServer_type(type);
			sc_MyApplication.getInstance().setClaimidstate(3);
			sc_MyApplication.getInstance().setClaimid("x00000001");
			return;
		}
		this.type = type;
		new Thread(runnable).start();
	}

	/**
	 * 确定服务账号
	 */
	public boolean serviceConfirm() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "serviceType"));
		params.add(new BasicNameValuePair("caseid", sc_MyApplication
				.getInstance().getCaseid()));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("type", type + ""));
		sc_UploadData3 uploaddata = new sc_UploadData3(sc_MyApplication.URL
				+ "claim", params);
		uploaddata.Post();
		String response = uploaddata.getResponse();
		parseJson(response);
		params.clear();
		params = null;
		if (response.startsWith("{")) {
			return true;
		}
		return false;
	}

	public void parseJson(int type, String reponse) {
		progressdialog.dismiss();
		if (type == 2) {
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
		sc_MyApplication.getInstance().setClaimid(claiminfo.getClaimid());
		sc_MyApplication.getInstance().setClaimidstate(
				Integer.parseInt(claiminfo.getStatus()));
		values.put("caseid", claiminfo.getCaseid());
		values.put("service_type", claiminfo.getServicetype());
		values.put("status", claiminfo.getStatus());
		values.put("status_text", claiminfo.getStatustext());
		database.insert("claiminfo", values);
		database.close();
		if (Integer.parseInt(claiminfo.getServicetype()) == 1) {
			sc_MyApplication.getInstance().setCasedescription_tag(2);
			sc_MyApplication.getInstance().setPhoto_tag(1);
			Intent i = new Intent(this, sc_CaseOfOnlyOneActivity.class);
			this.startActivity(i);
			this.finish();
		} else {
			Intent i = new Intent(this, sc_CaseHandlerFlowActivity.class);
			this.startActivity(i);
			this.finish();
		}
	}

	public void parseJson(String reponse) {
		if (reponse.startsWith("{")) {
			Gson gson = new Gson();// 创建Gson对象，
			ContentValues values = new ContentValues();
			sc_ClaimInfo claiminfo = gson.fromJson(reponse, sc_ClaimInfo.class);
			values.put("claimid", claiminfo.getClaimid());
			sc_MyApplication.getInstance().setClaimid(claiminfo.getClaimid());
			sc_MyApplication.getInstance().setClaimidstate(
					Integer.parseInt(claiminfo.getStatus()));
			values.put("caseid", claiminfo.getCaseid());
			values.put("service_type", claiminfo.getServicetype());
			values.put("status", claiminfo.getStatus());
			values.put("status_text", claiminfo.getStatustext());
			database.insert("claiminfo", values);
			database.close();
		}
	}

	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (sc_NetworkCheck.IsHaveInternet(sc_ConfirmServiceActivity.this)) {
				handlerrunnable.sendEmptyMessage(5);
				boolean b = serviceConfirm();
				if(b){
					handlerrunnable.sendEmptyMessage(1);
				}else{
					handlerrunnable.sendEmptyMessage(2);
				}
				handlerrunnable.sendEmptyMessage(4);
				handlerrunnable.sendEmptyMessage(6);
			} else {
				handlerrunnable.sendEmptyMessage(7);// 没有网络时给用户提示
			}
		}
	};

	private Handler handlerrunnable = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder callDailog = null;
			switch (msg.what) {
			case 1:
				if (type == 1) {
					sc_MyApplication.getInstance().setCasedescription_tag(2);
					sc_MyApplication.getInstance().setPhoto_tag(1);
					Intent i = new Intent(sc_ConfirmServiceActivity.this,
							sc_CaseOfOnlyOneActivity.class);
					sc_ConfirmServiceActivity.this.startActivity(i);
					sc_ConfirmServiceActivity.this.finish();
				} else {
					Intent i = new Intent(sc_ConfirmServiceActivity.this,
							sc_CaseHandlerFlowActivity.class);
					sc_ConfirmServiceActivity.this.startActivity(i);
					sc_ConfirmServiceActivity.this.finish();
				}
				break;
			case 2:
				callDailog = new AlertDialog.Builder(
						sc_ConfirmServiceActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("选择的服务方式提交到服务器失败，请稍后重试。")
						.setPositiveButton("确定", null).show();
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						sc_ConfirmServiceActivity.this);
				progressdialog.setMessage("系统处理中，请稍等~~~");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						return true;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;

			case 7:// 关闭进度条
				callDailog = new AlertDialog.Builder(
						sc_ConfirmServiceActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("选择的服务方式提交到服务器失败，请确认网络是否畅通。")
						.setPositiveButton("确定", null).show();
				break;
			}
		}

	};

}
