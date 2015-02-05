package com.chinaLife.claimAssistant.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.bean.sc_GetText;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_GetClaimState;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;

public class Sc_CaseInfoActivity extends Activity implements OnClickListener {
	private Button btn1, btn2, btn3, back;// 底部返回按钮 赔按处理按钮 案件撤销按钮 顶部返回按钮
	private int tag;// 接受activity之间的参数传替

	private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11,
			tv12;
	private LinearLayout tv2copy;
	private String caseid;// 案件
	public static sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	
	private int back_activity = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_caseinfo);
		back_activity = getIntent().getIntExtra("back_activity", 0);
		Sc_ExitApplication.getInstance().context = Sc_CaseInfoActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_CaseInfoActivity.this);
		initView();
		initViewFun();
		initData();
	}

	private void initViewFun() {
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		back.setOnClickListener(this);
		tv2copy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCallDialog();
			}
		});
	}

	public void showCallDialog() {
		AlertDialog.Builder callDailog = new AlertDialog.Builder(
				Sc_CaseInfoActivity.this);
		callDailog.setMessage(tv2.getText().toString())
		.setPositiveButton("拨号", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent intent = new Intent(Intent.ACTION_CALL);
							intent.setData(Uri.parse("tel:"+tv2.getText().toString()));
							startActivity(intent);
						} catch (Exception e) {
							Log.e("SampleApp", "Failed to invoke call", e);
						}
					}
				}).setNegativeButton("取消", null).show();
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			sc_GetClaimState.startMyThread();
			sc_MyHandler.getInstance().sendEmptyMessage(-11);
		}
	};

	private void initData() {
		
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			tv1.setText("张强");
			tv2.setText("13688888888");
			tv3.setText("刘先生");
			tv4.setText("京A00000");
			tv5.setText("15988888888");
			tv6.setText("62012110000000001");
			tv7.setText("2012-10-18 08:50:00");
			tv8.setText("2012-10-18 14:13:34");
			tv9.setText("北京市西城区三里河东路");
			tv10.setText(sc_GetText.getMsg(Sc_MyApplication.getInstance()
					.getClaimidstate(), Sc_MyApplication.getInstance()
					.getCaseidstate(),Sc_MyApplication.getInstance().getServer_type()));
			if (!sc_GetText.isActive(
					Sc_MyApplication.getInstance().getClaimidstate(),
					Sc_MyApplication.getInstance().getCaseidstate())) {
				btn3.setClickable(false);
				btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
			}
			return;
		}

		tag = getIntent().getIntExtra("tag", 0);

		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		progressdialog.setMessage("系统处理中，请稍等...");
		if(back_activity == 0){
			progressdialog.show();
			new Thread(runnable).start();
		}
		caseid = Sc_MyApplication.getInstance().getCaseid();

		List<Map<String, Object>> selectresult = database.selectRow(
				"select * from caseinfo where caseid = '" + caseid + "'", null);

		try {
			tv1.setText(selectresult.get(0).get("claim_name").toString());
		} catch (Exception e) {
			tv1.setText("");
		}
		try {
			tv2.setText(selectresult.get(0).get("claim_phone_number")
					.toString());
		} catch (Exception e) {
			tv2.setText("");
		}
		try {
			tv3.setText(selectresult.get(0).get("contact_name").toString());
		} catch (Exception e) {
			tv3.setText("");
		}
		try {
			tv4.setText(selectresult.get(0).get("plate_number").toString());
		} catch (Exception e) {
			tv4.setText("");
		}
		try {
			tv5.setText(selectresult.get(0).get("contact_mobile_number")
					.toString());
		} catch (Exception e) {
			tv5.setText("");
		}
		try {
			tv6.setText(selectresult.get(0).get("case_number").toString());
		} catch (Exception e) {
			tv6.setText("");
		}

		try {
			tv7.setText(selectresult.get(0).get("accident_time").toString());
		} catch (Exception e) {
			tv7.setText("");
		}
		try {
			tv8.setText(selectresult.get(0).get("report_time").toString());
		} catch (Exception e) {
			tv8.setText("");
		}
		try {
			tv9.setText(selectresult.get(0).get("accident_address").toString());
		} catch (Exception e) {
			tv9.setText("");
		}
		try {
			tv11.setText(selectresult.get(0).get("status_text").toString().replace("结案", "结束"));
		} catch (Exception e) {
			tv11.setText("");
		}
		try {
			tv12.setText(selectresult.get(0).get("remark").toString());
		} catch (Exception e) {
			tv12.setText("");
		}
		List<Map<String, Object>> selectresult2 = database.selectRow(
				"select * from claiminfo where caseid ='"
						+ Sc_MyApplication.getInstance().getCaseid() + "'", null);
		int i = 1;
		if(selectresult2!=null
				&&selectresult2.size()>0
				&&selectresult2.get(selectresult2.size()-1)!=null
				&&selectresult2.get(selectresult2.size()-1).get("servertype")!=null){
			try {
				i = Integer.parseInt(selectresult2
						.get(selectresult2.size() - 1).get("servertype")
						.toString());
			} catch (Exception e) {
			}
			
		}
		tv10.setText(sc_GetText.getMsg(Sc_MyApplication.getInstance()
				.getClaimidstate(), Sc_MyApplication.getInstance()
				.getCaseidstate(),i));
		if (!sc_GetText.isActive(Sc_MyApplication.getInstance().getClaimidstate(),
				Sc_MyApplication.getInstance().getCaseidstate())) {
			btn3.setClickable(false);
			btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
			if (selectresult2.size() <= 0) {
				btn2.setClickable(false);
				btn2.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
			}
		} else {
			btn3.setClickable(true);
			btn3.setBackgroundResource(R.drawable.sc_aj_btn3);
		}
	}

	private void initView() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		back = (Button) findViewById(R.id.fanhui);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv2copy = (LinearLayout) findViewById(R.id.tv2copy);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv6 = (TextView) findViewById(R.id.tv6);
		tv7 = (TextView) findViewById(R.id.tv7);
		tv8 = (TextView) findViewById(R.id.tv8);
		tv9 = (TextView) findViewById(R.id.tv9);
		tv10 = (TextView) findViewById(R.id.tv10);
		tv11 = (TextView) findViewById(R.id.tv11);
		tv12 = (TextView) findViewById(R.id.tv12);

	}

	@Override
	public void onClick(View v) {
		if (v == btn1) {
			if (tag == 0) {
				Intent intent = new Intent(this, Sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			} else {/*
					 * Intent intent = new Intent(this,
					 * MessageListActivity.class); startActivity(intent);
					 * finish();
					 */
			}
		} else if (v == btn2) {
			if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
				Intent i = new Intent();
				Sc_MyApplication.getInstance().setCasedescription_tag(2);
				if (Sc_MyApplication.getInstance().getServer_type() == 0) {
					i.setClass(this, Sc_ConfirmServiceActivity.class);
				} else {
					if (Sc_MyApplication.getInstance().getServer_type() == 1) {
//						if(MyApplication.getInstance().getSelfHelpFlag() ==1){
							i.putExtra("tag", 3);
							i.setClass(this, Sc_CaseOfOnlyOneActivity.class);
//						}else{
//							i.setClass(this, CaseDescriptionActivity.class);
//						}
						Sc_MyApplication.getInstance().setCasedescription_tag(2);
					} else {
						Sc_MyApplication.getInstance().setCasedescription_tag(2);
						i.putExtra("tag", 1);
						i.setClass(this, Sc_CaseHandlerFlowActivity.class);
					}
				}

				this.startActivity(i);
				this.finish();
				return;
			}

			Intent i = new Intent();
			Sc_MyApplication.getInstance().setCasedescription_tag(2);
			List<Map<String, Object>> selectresult = database.selectRow(
					"select * from claiminfo where caseid ='"
							+ Sc_MyApplication.getInstance().getCaseid() + "'",
					null);

			if (selectresult.size() <= 0) {
				i.setClass(this, Sc_ConfirmServiceActivity.class);
				this.startActivity(i);
				this.finish();
			} else {
				if (Integer.parseInt(selectresult.get(0).get("service_type")
						.toString()) == 1) {
					sc_DBHelper database = sc_DBHelper.getInstance();
					String sql = "select claim_type_select from claiminfo where claimid='"
							+ Sc_MyApplication.getInstance().getClaimid() + "'";

					List<Map<String, Object>> list = database.selectRow(sql,
							null);
					Intent intent = new Intent();
//					try {
//						Map<String, Object> map = list.get(0);
//						String state = (String) map.get("claim_type_select");
//						if (Integer.parseInt(state) == 1) {
							intent.putExtra("tag", 3);
							intent.setClass(this, Sc_CaseOfOnlyOneActivity.class);
//						} else {
//							intent.setClass(this, CaseDescriptionActivity.class);
//						}
//					} catch (Exception e) {
//						intent.setClass(this, CaseDescriptionActivity.class);
//					}
					Sc_MyApplication.getInstance().setCasedescription_tag(2);
					this.startActivity(intent);
					this.finish();
				} else {
					Sc_MyApplication.getInstance().setCasedescription_tag(2);
					i.putExtra("tag", 1);
					i.setClass(this, Sc_CaseHandlerFlowActivity.class);
					this.startActivity(i);
					this.finish();
				}
			}

		} else if (v == btn3) {
			Intent i = new Intent();
			i.putExtra("tag", 0);
			i.setClass(Sc_CaseInfoActivity.this, Sc_ClaimRevokeActivity.class);
			Sc_CaseInfoActivity.this.startActivity(i);
			Sc_CaseInfoActivity.this.finish();
		} else if (v == back) {
			if (tag == 0) {
				Intent intent = new Intent(this, Sc_CaseListActivity.class);
				intent.putExtra("back_activity", 1);//返回
				startActivity(intent);
				finish();
			} else {
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
			} else {
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
}
