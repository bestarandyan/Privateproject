package com.chinaLife.claimAssistant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.chinaLife.adapter.sc_MessageListAdapter;
import com.chinaLife.claimAssistant.bean.sc_NoticeInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_CountService;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.chinaLife.claimAssistant.tools.sc_TopBgSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class sc_MessageListActivity extends Activity implements OnClickListener {
	private ListView listview = null;
	private Button back;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	public static String ids = "";
	public Thread thread;
	public DaoJiShiClass mc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_messagelist);
		sc_ExitApplication.getInstance().exitActivity();
		sc_ExitApplication.getInstance().context = sc_MessageListActivity.this;
		sc_ExitApplication.getInstance().addActivity(sc_MessageListActivity.this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		sc_MyApplication.getInstance().setSw(dm.widthPixels);
		sc_MyApplication.getInstance().setSh(dm.heightPixels);
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, sc_CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		am.cancel(sender);
		initView();
		initData();
		initViewFun();
		this.stopService(new Intent(this, sc_CountService.class));
		NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
		
	}
	private void initViewFun() {
		back.setOnClickListener(this);
	}
	private void initData() {
		sc_MyApplication.getInstance().setMessageNumber(0);
		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		SharedPreferences.Editor sharedata = this.getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putInt("threaddemo", 1);
		sharedata.commit();
		sc_MyApplication.getInstance().setSelfHelpFlag(0);
		sc_MyApplication.getInstance().setCasedescription_tag(2);
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(true);
		sc_MyApplication.getInstance().setProgressdialog(progressdialog);
//		progressdialog.setMessage("系统处理中，请稍等...");
//		progressdialog.show();
//		if (!MyApplication.getInstance().getPhonenumber().equals("")
//				&& !MyApplication.getInstance().getPlatenumber().equals("")) {
			notifyAdapter();
			if (mc != null) {
				mc.cancel();
				mc = null;
			}
			mc = new DaoJiShiClass(10000, 1000);
			mc.start();
//			selectNotice();
//		}
	}
	private void initView() {
		back = (Button) findViewById(R.id.fanhui);
		listview = (ListView) findViewById(R.id.list);
	}

	private void notifyAdapter() {
		if(list == null
				||listview==null){
			return;
		}
		getList();
		int index = listview.getFirstVisiblePosition();
		sc_MessageListAdapter adapter = new sc_MessageListAdapter(this, list);
		listview.setSelectionFromTop(index, 0);
		listview.setAdapter(adapter);
		
	}
//	private void notifyListAdapter(){
//		MessageListAdapter adapter = new MessageListAdapter(this, list);
//		listview.setAdapter(adapter);
//	}
	public class DaoJiShiClass extends CountDownTimer {

		public DaoJiShiClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			if (thread != null) {
				thread.interrupt();
				thread = null;
			}
			thread = new Thread(refreshRunnable);
			thread.start();
			mc.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
		}

	}
	
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			notifyAdapter();// 刷新布局
			mc = new DaoJiShiClass(10000, 1000);
			mc.start();
			super.handleMessage(msg);
		}

	};
	private Runnable refreshRunnable = new Runnable() {

		@Override
		public void run() {
			
			handler.sendEmptyMessage(0);
		}

	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(list!=null){
			list.clear();
			list = null;
		}
		if (mc != null) {
			mc.cancel();
		}
		super.onDestroy();
	}
	
	private void getList() {
		sc_MyApplication.switch_tag = false;
//		while (!ThreadDemo.isstart) {
//		}
		list.clear();
		SharedPreferences shared = sc_SharedPreferencesinfo.getdata(this);
		String phonenumber = shared.getString("phonenumber", "");
		String platenumber = shared.getString("platenumber", "");
		sc_MyApplication.getInstance().setPhonenumber(phonenumber);
		sc_MyApplication.getInstance().setPlatenumber(platenumber);
		if ((!sc_MyApplication.getInstance().getPhonenumber().equals(""))
				&& (!sc_MyApplication.getInstance().getPlatenumber().equals(""))) {
			HashMap<String, String> map = null;
			String sql = "select caseinfo.caseid,caseinfo.status as casestatus," +
					" claiminfo.claimid,claiminfo.status as claimstatus,claiminfo.service_type," +
					" noticeinfo.create_time,noticeinfo.content,noticeinfo.noticeid,noticeinfo.claimstatus as clainstatuspre " +
					"from noticeinfo inner join claiminfo on noticeinfo.claimid = claiminfo.claimid"
					+ " inner join caseinfo on caseinfo.caseid = claiminfo.caseid"
					+ " where caseinfo.contact_mobile_number='"+
					sc_MyApplication.getInstance().getPhonenumber()
					+"' and caseinfo.plate_number ='"+
					sc_MyApplication.getInstance().getPlatenumber()
					+"' order by julianday(noticeinfo.create_time) desc";
			List<Map<String, Object>> selectresult = database.selectRow(sql, null);
			for (int i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, String>();
				
				ContentValues values = new ContentValues();
				values.put("status", "0");
				database.update("noticeinfo", values, "noticeid=?", new String[]{selectresult.get(i).get("noticeid").toString()});
//				database.execSql("update noticeinfo set status = 0 where noticeid ='"+
//						selectresult.get(i).get("noticeid").toString()+"'");
				map.put("claimid", selectresult.get(i).get("claimid").toString());
				map.put("claimstatus", selectresult.get(i).get("claimstatus").toString());
				map.put("clainstatuspre", selectresult.get(i).get("clainstatuspre").toString());
				map.put("casestatus", selectresult.get(i).get("casestatus").toString());
				map.put("caseid", selectresult.get(i).get("caseid").toString());
				map.put("noticeid", selectresult.get(i).get("noticeid").toString());
				map.put("content", selectresult.get(i).get("content").toString());
				map.put("create_time", selectresult.get(i).get("create_time")
						.toString());
				map.put("service_type", selectresult.get(i).get("service_type")
						.toString());
				list.add(map);
			}
		}
		sc_MyApplication.switch_tag = true;
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Intent intent = new Intent(this, sc_MainActivity.class);
			startActivity(intent);
			sc_MessageListActivity.this.finish();
		}else if(KeyEvent.KEYCODE_MENU == keyCode){
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			Intent intent = new Intent(this, sc_MainActivity.class);
			startActivity(intent);
			sc_MessageListActivity.this.finish();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) { // 创建菜单项，实现附加功能
//		super.onCreateOptionsMenu(menu);
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//			Intent i = new Intent(this,sc_MainActivity.class);
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
//		return true;
//	}

	public void parseJson() {
		Type listType = new TypeToken<LinkedList<sc_NoticeInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_NoticeInfo> noticeinfos = null;
		sc_NoticeInfo noticeinfo = null;
		noticeinfos = gson.fromJson(sc_MyApplication.getInstance()
				.getResponseword(), listType);
		StringBuilder messageids = new StringBuilder("");
		boolean b = false;
		for (Iterator<sc_NoticeInfo> iterator = noticeinfos.iterator(); iterator
				.hasNext();) {
			b = true;
			noticeinfo = (sc_NoticeInfo) iterator.next();
			values.put("claimid", noticeinfo.getClaimid());
			values.put("noticeid", noticeinfo.getNoticeid());
			messageids.append(noticeinfo.getNoticeid());
			messageids.append(",");
			values.put("content", noticeinfo.getContent());
			values.put("create_time", noticeinfo.getCreatetime());
			values.put("status", 1);
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(
							"select * from noticeinfo where noticeid = '"
									+ noticeinfo.getNoticeid() + "'", null);

			if (selectresult.size() <= 0) {
				sc_DBHelper.getInstance().insert("noticeinfo", values);
			} else {
				// if(Integer.parseInt(selectresult.get(0).get("review_result").toString())!=-1){
				sc_DBHelper.getInstance().update("noticeinfo", values,
						"noticeid = ?",
						new String[] { noticeinfo.getNoticeid() });
				// }

			}
			values.clear();
		}
		// if(messageids.length()<0){
		if (b) {
			ids = messageids.substring(0, messageids.length() - 1);
		}
		// }
		values = null;
		sc_DBHelper.getInstance().close();
		notifyAdapter();
	}
}
