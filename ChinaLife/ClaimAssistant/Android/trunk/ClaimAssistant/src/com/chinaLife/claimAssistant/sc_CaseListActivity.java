package com.chinaLife.claimAssistant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.Interface.sc_InitInterface;
import com.chinaLife.adapter.sc_CaseListAdapter;
import com.chinaLife.claimAssistant.bean.sc_CaseClaimInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData3;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.chinaLife.claimAssistant.view.sc_XListView;
import com.chinaLife.claimAssistant.view.sc_XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class sc_CaseListActivity extends Activity implements OnClickListener,
		sc_InitInterface,IXListViewListener{
	private sc_XListView listview = null;
	private Button back;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public ProgressDialog progressdialog = null;
	public sc_UploadData3 uploaddata = null;
	// public int refreshtag = 0; //0 进入列表刷新，1，手动刷新
	public Thread thread;
//	public DaoJiShiClass mc;
	private int number = 0;	
	private int back_activity = 0;

	private boolean select_case_tag = true;
	public boolean click_limit = true;//时间准备点
	
	
	private Timer timer = null;
	private sc_CaseListAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_caselist);
		back_activity = getIntent().getIntExtra("back_activity", 0);
		sc_ExitApplication.getInstance().context = sc_CaseListActivity.this;
		sc_ExitApplication.getInstance().addActivity(sc_CaseListActivity.this);
		initView();
		initData();
//		if (MyApplication.getInstance().getSelfHelpFlag() != 1) {
//			listview.setOnRefreshListener(new OnRefreshListener() {
//				@Override
//				public void onRefresh() {
//					new GetDataTask().execute();
//				}
//			});
//		}

	}
	@Override
	public void initViewFun() {
	}
	public void initView() {
		back = (Button) findViewById(R.id.fanhui);
		listview = (sc_XListView) findViewById(R.id.list);
		back.setOnClickListener(this);
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this);
//		listview.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				
////				new GetDataTask().execute();
//			}
//		});
//		listview.onRefreshComplete();
	}

	public void initData() {
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			notifyAdapter();
			return;
		}
		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		notifyAdapter();
		timer = new Timer();
		timer.schedule(task,0,1000*60*2);
//		if(back_activity==0){
//			listview.prepareForRefresh();
//			listview.onRefresh();
//		}else{
//			if (mc != null) {
//				mc.cancel();
//				mc = null;
//			}
//			mc = new DaoJiShiClass(15000, 1000);
//			mc.start();
//			handler2.sendEmptyMessage(2);
//		}
	}


//	private Runnable runnable = new Runnable() {
//
//		@Override
//		public void run() {
//			uploaddata.Post();
//		}
//	};
//	private Runnable refreshRunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			selectCase();
//			handler.sendEmptyMessage(0);
//		}
//
//	};

//	public class DaoJiShiClass extends CountDownTimer {
//
//		public DaoJiShiClass(long millisInFuture, long countDownInterval) {
//			super(millisInFuture, countDownInterval);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public void onFinish() {
//			if (thread != null) {
//				thread.interrupt();
//				thread = null;
//			}
//			thread = new Thread(refreshRunnable);
//			thread.start();
//			mc.cancel();
//		}
//
//		@Override
//		public void onTick(long millisUntilFinished) {
//			int day = (int) (millisUntilFinished / 1000 / 60 / 60 / 24);
//			int hour = (int) (millisUntilFinished / 1000 / 60 / 60 % 24);
//			int minute = (int) (millisUntilFinished - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000) / 1000 / 60;
//			int miao = (int) (millisUntilFinished - day * 24 * 60 * 60 * 1000
//					- hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
//		}
//	}

	@Override
	protected void onDestroy() {
		if (list != null) {
			list.clear();
			list = null;
		}
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}

//	public Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			notifyAdapter();// 刷新布局
//			number++;
//			mc = new DaoJiShiClass(15000, 1000);
//			mc.start();
//			super.handleMessage(msg);
//		}
//
//	};

	private void notifyAdapter() {
		if (list == null || listview == null) {
			return;
		}
		getList();
		adapter = new sc_CaseListAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setCacheColorHint(0);
	}
	
	
	private void notifyList() {
		if (list == null || listview == null||adapter==null) {
			return;
		}
		getList();
		adapter.notifyDataSetChanged();
	}
	
	
	
	/**
	 * 根据手机号车牌号查询案件列表
	 */
	private String selectCase() {
		if (sc_MyApplication.getInstance().getPhonenumber() != null
				&& sc_MyApplication.getInstance().getPlatenumber() != null
				&& !sc_MyApplication.getInstance().getPhonenumber().equals("")
				&& !sc_MyApplication.getInstance().getPlatenumber().equals("")) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getCaseList"));
			params.add(new BasicNameValuePair("phoneNumber", sc_MyApplication
					.getInstance().getPhonenumber()));
			params.add(new BasicNameValuePair("plateNumber", sc_MyApplication
					.getInstance().getPlatenumber()));
			params.add(new BasicNameValuePair("password", sc_MyApplication
					.getInstance().getPassword()));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("lastUpdateTime", ""));
			uploaddata = new sc_UploadData3(sc_MyApplication.URL + "case", params);
			uploaddata.Post();
			String reponse = uploaddata.getResponse();
			parseJson1(reponse);
			return reponse;
		} else {
			SharedPreferences shared = sc_SharedPreferencesinfo.getdata(this);
			String phonenumber = shared.getString("phonenumber", "");
			String platenumber = shared.getString("platenumber", "");
			SharedPreferences.Editor sharedata = this.getSharedPreferences(
					"userinfo", 0).edit();
			sharedata.putInt("threaddemo", 1);
			sharedata.commit();
			sc_MyApplication.getInstance().setPhonenumber(phonenumber);
			sc_MyApplication.getInstance().setPlatenumber(platenumber);
		}
		return "";
	}
	
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (sc_NetworkCheck.IsHaveInternet(sc_CaseListActivity.this)) {
				if(selectCase().equals("-1000")){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else{
				handler.sendEmptyMessage(7);
			}
			handler.sendEmptyMessage(1);
			click_limit = true;
		}	
	};
	
	
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (sc_NetworkCheck.IsHaveInternet(sc_CaseListActivity.this)) {
				handler.sendEmptyMessage(0);
				if(selectCase().equals("-1000")){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(1);
			}
			click_limit = true;
		}
	};
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 调用刷新
				((ProgressBar)findViewById(R.id.probar)).setVisibility(View.VISIBLE);
				break;
			case 1:// 结束刷新
				listview.stopRefresh();
				((ProgressBar)findViewById(R.id.probar)).setVisibility(View.GONE);
				notifyList();
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(sc_CaseListActivity.this);
				progressdialog.setMessage("系统处理中，请稍等~~~");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (uploaddata != null) {
							uploaddata.overResponse();
						}
						return false;
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
				Builder callDailog = new AlertDialog.Builder(sc_CaseListActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("请检查网络是否连入").setPositiveButton("确定", null)
						.show();
				break;
			}
		}
		
	};

//	/**
//	 * 根据手机号车牌号查询案件列表
//	 */
//	private void selectCase() {
//		if(select_case_tag){
//			select_case_tag = false;
//		}else{
//			return;
//		}
//		if (MyApplication.getInstance().getPhonenumber() != null
//				&& MyApplication.getInstance().getPlatenumber() != null) {
//			if (NetworkCheck.IsHaveInternet(this)
//					&& !MyApplication.getInstance().getPhonenumber().equals("")
//					&& !MyApplication.getInstance().getPlatenumber().equals("")) {
//				List<Map<String, Object>> selectresult = DBHelper.getInstance()
//						.selectRow(
//								"select max(_id),time from syndatatime where platenumber = '"
//										+ MyApplication.getInstance()
//												.getPlatenumber()
//										+ "' and tablename='caseinfo' and phonenumber = '"
//										+ MyApplication.getInstance()
//												.getPhonenumber() + "'", null);
//				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//				params.add(new BasicNameValuePair("appid", MyApplication.APPID));
//				params.add(new BasicNameValuePair("appkey",
//						MyApplication.APPKEY));
//				params.add(new BasicNameValuePair("action", "getCaseList"));
//				params.add(new BasicNameValuePair("phoneNumber", MyApplication
//						.getInstance().getPhonenumber()));
//				params.add(new BasicNameValuePair("plateNumber", MyApplication
//						.getInstance().getPlatenumber()));
//				params.add(new BasicNameValuePair("IMEI", PhoneInfo
//						.getIMEI(this)));
//				try {
//					params.add(new BasicNameValuePair("lastUpdateTime",
//							selectresult.get(0).get("time").toString()));
//				} catch (Exception e) {
//					params.add(new BasicNameValuePair("lastUpdateTime", ""));
//				}
//				uploaddata = new UploadData(MyApplication.URL + "case", params,
//						17);
//				new Thread(runnable).start();
//
//			} else {
//				select_case_tag = true;
//				handler2.sendEmptyMessage(1);
//				handler2.sendEmptyMessage(2);
//			}
//		} else {
//			handler2.sendEmptyMessage(2);
//			select_case_tag = true;
//		}
//
//	}
//
//	public Handler handler2 = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				notifyAdapter();
//				break;
//
//			case 2:
//				listview.onRefreshComplete();
//				break;
//			}
//		}
//
//	};

	private void getList() {
		sc_MyApplication.switch_tag = false;
		// while (!ThreadDemo.isstart) {
		// }
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			list.clear();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("casestatus", sc_MyApplication.getInstance().getCaseidstate()
					+ "");
			map.put("casetime", "2012-10-18 14:13:34");
			map.put("casenumber", "62012110000000001");
			map.put("caseid", sc_MyApplication.getInstance().getCaseid());
			map.put("claimname", "张强");
			map.put("claimphonenumber", "15988888888");
			if (sc_MyApplication.getInstance().getServer_type() > 0) {
				map.put("haveclaim", "1");
				map.put("claimid", sc_MyApplication.getInstance().getClaimid());
				map.put("claimstatus", sc_MyApplication.getInstance()
						.getClaimidstate() + "");
				map.put("servertype", sc_MyApplication.getInstance()
						.getServer_type() + "");
			} else {
				map.put("haveclaim", "0");
				map.put("claimid", "");
				map.put("claimstatus", "-1");
			}
			list.add(map);
			return;
		}
		list.clear();
		HashMap<String, String> map = null;
		String sql = "select * from caseinfo where contact_mobile_number = '"
				+ sc_MyApplication.getInstance().getPhonenumber()
				+ "' and plate_number='"
				+ sc_MyApplication.getInstance().getPlatenumber()
				+ "' group by caseid order by julianday(update_time) desc";
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance().selectRow(sql, null);
		for (int i = 0; i < selectresult.size(); i++) {
			String sql1 = "select * from claiminfo where caseid = '"
					+ selectresult.get(i).get("caseid").toString() + "'";
			List<Map<String, Object>> selectresult1 = sc_DBHelper.getInstance().selectRow(sql1,
					null);
			map = new HashMap<String, String>();
			try {
				map.put("casestatus", selectresult.get(i).get("status")
						.toString());
			} catch (Exception e) {
				map.put("casestatus", "-1");
			}
			map.put("casetime", selectresult.get(i).get("report_time")
					.toString());
			map.put("casenumber", selectresult.get(i).get("case_number")
					.toString());
			map.put("caseid", selectresult.get(i).get("caseid").toString());
			map.put("claimname", selectresult.get(i).get("claim_name")
					.toString());
			map.put("claimphonenumber",
					selectresult.get(i).get("claim_phone_number").toString());
			if (selectresult1.size() > 0) {
				map.put("haveclaim", "1");
				map.put("claimid", selectresult1.get(0).get("claimid")
						.toString());
				map.put("claimstatus", selectresult1.get(0).get("status")
						.toString());
				map.put("servertype", selectresult1.get(0).get("service_type")
						.toString());
			} else {
				map.put("haveclaim", "0");
				map.put("claimid", "");
				map.put("claimstatus", "-1");
			}
			list.add(map);
		}
		sc_DBHelper.getInstance().close();
		sc_MyApplication.switch_tag = true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Intent intent = new Intent(this, sc_CaseManageActivity.class);
			startActivity(intent);
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

	public void onClick(View v) {
		if (v == back) {
			Intent intent = new Intent(this, sc_CaseManageActivity.class);
			startActivity(intent);
			finish();
		}
	}

	

//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//		@Override
//		protected void onPostExecute(String[] result) {
//			if (MyApplication.getInstance().getSelfHelpFlag() == 1) {
//				handler2.sendEmptyMessage(2);
//			} else {
//				selectCase();
//			}
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			return null;
//		}
//	}
//
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

	/**
	 * 处理获取的json数据
	 */

	public void parseJson1(String reponse) {
		if (!reponse.contains("{")) {
			return;
		}
		Type listType = new TypeToken<LinkedList<sc_CaseClaimInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_CaseClaimInfo> caseclaiminfos = null;
		sc_CaseClaimInfo caseclaiminfo = null;
		caseclaiminfos = gson.fromJson(reponse, listType);
		if (caseclaiminfos == null) {
			return;
		}
		for (Iterator<sc_CaseClaimInfo> iterator = caseclaiminfos.iterator(); iterator
				.hasNext();) {
			caseclaiminfo = (sc_CaseClaimInfo) iterator.next();
			values.put("caseid", caseclaiminfo.getCaseid());
			values.put("case_number", caseclaiminfo.getCasenumber());
			values.put("accident_address", caseclaiminfo.getAccidentaddress());
			values.put("contact_name", caseclaiminfo.getContactname());
			values.put("contact_mobile_number",
					caseclaiminfo.getContactmobilenumber());
			values.put("plate_number", caseclaiminfo.getPlatenumber());
			values.put("claim_userid", caseclaiminfo.getClaimuserid());
			values.put("claim_name", caseclaiminfo.getClaimname());
			values.put("claim_phone_number",
					caseclaiminfo.getClaimphonenumber());
			values.put("summary", caseclaiminfo.getSummary());
			values.put("status", caseclaiminfo.getCasestatus());
			values.put("status_text", caseclaiminfo.getCasestatustext());
			values.put("remark", caseclaiminfo.getRemark());
			values.put("report_time", caseclaiminfo.getReporttime());
			values.put("accident_time", caseclaiminfo.getAccidenttime());
			values.put("update_time", caseclaiminfo.getUpdatetime());
			values.put("create_time", caseclaiminfo.getCreatetime());
			if (sc_DBHelper.getInstance().selectRow(
					"select * from caseinfo where caseid = '"
							+ caseclaiminfo.getCaseid() + "'", null).size() <= 0) {
				sc_DBHelper.getInstance().insert("caseinfo", values);
			} else {
				sc_DBHelper.getInstance().update("caseinfo", values, "caseid = ?",
						new String[] { caseclaiminfo.getCaseid() });
			}
			values.clear();
			if ((!caseclaiminfo.getClaimid().equals(""))
					&& Integer.parseInt(caseclaiminfo.getClaimid()) != -1) {
				values.put("claimid", caseclaiminfo.getClaimid());
				values.put("caseid", caseclaiminfo.getCaseid());
				values.put("userid", caseclaiminfo.getClaimuserid());
				values.put("service_type", caseclaiminfo.getServicetype());
				values.put("claim_mode", caseclaiminfo.getClaimmode());
				values.put("status", caseclaiminfo.getClaimstatus());
				values.put("status_text", caseclaiminfo.getClaimstatustext());
				values.put("update_time", caseclaiminfo.getUpdatetime());
				values.put("create_time", caseclaiminfo.getCreatetime());
				values.put("claim_amount", caseclaiminfo.getClaimamount());
				values.put("claim_overview", caseclaiminfo.getClaimoverview());
				values.put("certificates", caseclaiminfo.getCertificates());
				values.put("bank_name", caseclaiminfo.getBankname());
				values.put("account_name", caseclaiminfo.getAccountname());
				values.put("bankcode", caseclaiminfo.getBankcode());
				values.put("iscomfirm", caseclaiminfo.getIsconfirmbankinfo());
				values.put("account_number", caseclaiminfo.getAccountnumber());
				values.put("insuredname", caseclaiminfo.getInsuredname());
				if (sc_DBHelper.getInstance().selectRow(
						"select * from claiminfo where caseid = '"
								+ caseclaiminfo.getCaseid()
								+ "' and claimid = '"
								+ caseclaiminfo.getClaimid() + "'", null)
						.size() <= 0) {
					sc_DBHelper.getInstance().insert("claiminfo", values);
				} else {
					sc_DBHelper.getInstance().update("claiminfo", values,
							"caseid = ? and claimid = ?",
							new String[] { caseclaiminfo.getCaseid(),
									caseclaiminfo.getClaimid() });
				}
				values.clear();
			}

		}

		values.put("phonenumber", sc_MyApplication.getInstance().getPhonenumber());
		values.put("platenumber", sc_MyApplication.getInstance().getPlatenumber());
		values.put("tablename", "caseinfo");
		try {
			values.put("time", caseclaiminfo.getUpdatetime());
		} catch (Exception e) {
			values.put("time", "");
		}
		String sql2 = "select max(_id),time from syndatatime where platenumber = '"
				+ sc_MyApplication.getInstance().getPhonenumber()
				+ "' and tablename='caseinfo' and phonenumber = '"
				+ sc_MyApplication.getInstance().getPlatenumber() + "'";

		if (sc_DBHelper.getInstance().selectRow(sql2, null).size() <= 0) {
			sc_DBHelper.getInstance().insert("syndatatime", values);
		} else {
			sc_DBHelper.getInstance().update("syndatatime", values,
					"phonenumber = ? and platenumber= ?", new String[] {
							sc_MyApplication.getInstance().getPhonenumber(),
							sc_MyApplication.getInstance().getPlatenumber() });
		}
		values.clear();
		values = null;
		sc_DBHelper.getInstance().close();
	}
	/**
	 * 处理获取的json数据
	 */

	public void parseJson(int tag, String reponse) {
		select_case_tag = true;
		if (tag == 2) {
//			handler2.sendEmptyMessage(2);
			return;
		}
		Type listType = new TypeToken<LinkedList<sc_CaseClaimInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_CaseClaimInfo> caseclaiminfos = null;
		sc_CaseClaimInfo caseclaiminfo = null;
		caseclaiminfos = gson.fromJson(reponse, listType);
		if (caseclaiminfos == null) {
			return;
		}
		for (Iterator<sc_CaseClaimInfo> iterator = caseclaiminfos.iterator(); iterator
				.hasNext();) {
			caseclaiminfo = (sc_CaseClaimInfo) iterator.next();
			values.put("caseid", caseclaiminfo.getCaseid());
			values.put("case_number", caseclaiminfo.getCasenumber());
			values.put("accident_address", caseclaiminfo.getAccidentaddress());
			values.put("contact_name", caseclaiminfo.getContactname());
			values.put("contact_mobile_number",
					caseclaiminfo.getContactmobilenumber());
			values.put("plate_number", caseclaiminfo.getPlatenumber());
			values.put("claim_userid", caseclaiminfo.getClaimuserid());
			values.put("claim_name", caseclaiminfo.getClaimname());
			values.put("claim_phone_number",
					caseclaiminfo.getClaimphonenumber());
			values.put("summary", caseclaiminfo.getSummary());
			values.put("status", caseclaiminfo.getCasestatus());
			values.put("status_text", caseclaiminfo.getCasestatustext());
			values.put("remark", caseclaiminfo.getRemark());
			values.put("report_time", caseclaiminfo.getReporttime());
			values.put("accident_time", caseclaiminfo.getAccidenttime());
			values.put("update_time", caseclaiminfo.getUpdatetime());
			values.put("create_time", caseclaiminfo.getCreatetime());
			if (sc_DBHelper.getInstance().selectRow(
					"select * from caseinfo where caseid = '"
							+ caseclaiminfo.getCaseid() + "'", null).size() <= 0) {
				sc_DBHelper.getInstance().insert("caseinfo", values);
			} else {
				sc_DBHelper.getInstance().update("caseinfo", values, "caseid = ?",
						new String[] { caseclaiminfo.getCaseid() });
			}
			values.clear();
			if ((!caseclaiminfo.getClaimid().equals(""))
					&& Integer.parseInt(caseclaiminfo.getClaimid()) != -1) {
				values.put("claimid", caseclaiminfo.getClaimid());
				values.put("caseid", caseclaiminfo.getCaseid());
				values.put("userid", caseclaiminfo.getClaimuserid());
				values.put("service_type", caseclaiminfo.getServicetype());
				values.put("claim_mode", caseclaiminfo.getClaimmode());
				values.put("status", caseclaiminfo.getClaimstatus());
				values.put("status_text", caseclaiminfo.getClaimstatustext());
				values.put("update_time", caseclaiminfo.getUpdatetime());
				values.put("create_time", caseclaiminfo.getCreatetime());
				values.put("claim_amount", caseclaiminfo.getClaimamount());
				values.put("claim_overview", caseclaiminfo.getClaimoverview());
				values.put("certificates", caseclaiminfo.getCertificates());
				values.put("bank_name", caseclaiminfo.getBankname());
				values.put("account_name", caseclaiminfo.getAccountname());
				values.put("bankcode", caseclaiminfo.getBankcode());
				values.put("iscomfirm", caseclaiminfo.getIsconfirmbankinfo());
				values.put("account_number", caseclaiminfo.getAccountnumber());
				values.put("insuredname", caseclaiminfo.getInsuredname());
				if (sc_DBHelper.getInstance().selectRow(
						"select * from claiminfo where caseid = '"
								+ caseclaiminfo.getCaseid()
								+ "' and claimid = '"
								+ caseclaiminfo.getClaimid() + "'", null)
						.size() <= 0) {
					sc_DBHelper.getInstance().insert("claiminfo", values);
				} else {
					sc_DBHelper.getInstance().update("claiminfo", values,
							"caseid = ? and claimid = ?",
							new String[] { caseclaiminfo.getCaseid(),
									caseclaiminfo.getClaimid() });
				}
				values.clear();
			}

		}

		values.put("phonenumber", sc_MyApplication.getInstance().getPhonenumber());
		values.put("platenumber", sc_MyApplication.getInstance().getPlatenumber());
		values.put("tablename", "caseinfo");
		try {
			values.put("time", caseclaiminfo.getUpdatetime());
		} catch (Exception e) {
			values.put("time", "");
		}
		String sql2 = "select max(_id),time from syndatatime where platenumber = '"
				+ sc_MyApplication.getInstance().getPhonenumber()
				+ "' and tablename='caseinfo' and phonenumber = '"
				+ sc_MyApplication.getInstance().getPlatenumber() + "'";

		if (sc_DBHelper.getInstance().selectRow(sql2, null).size() <= 0) {
			sc_DBHelper.getInstance().insert("syndatatime", values);
		} else {
			sc_DBHelper.getInstance().update("syndatatime", values,
					"phonenumber = ? and platenumber= ?", new String[] {
							sc_MyApplication.getInstance().getPhonenumber(),
							sc_MyApplication.getInstance().getPlatenumber() });
		}
		values.clear();
		values = null;
		sc_DBHelper.getInstance().close();
		notifyAdapter();
//		if (mc != null) {
//			mc.cancel();
//			mc = null;
//		}
//		mc = new DaoJiShiClass(15000, 1000);
//		mc.start();
//		handler2.sendEmptyMessage(2);
	}
	
	@Override
	protected void onPause() {
		select_case_tag = true;
		super.onPause();
	}
	@Override
	public void onRefresh() {
		new Thread(runnable).start();
	}
	@Override
	public void onLoadMore() {
		
	}
}
