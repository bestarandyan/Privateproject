package com.chinaLife.claimAssistant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chinaLife.adapter.sc_SayListAdapter;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_SendMsg;
import com.chinaLife.claimAssistant.thread.sc_ThreadDemo;

public class sc_SayActivity extends Activity implements OnClickListener {

	private Button btn1, btn2;
	private EditText ed1;
	public List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private ListView listview;
	// private boolean startgetmsg = true;
	private Timer timer = null;
	private TimerTask task = null;
	// private int tag = 0;
	public int sender;
	public String content;
	public String create_time;
	private sc_DBHelper database = null;
	public ProgressDialog progressdialog = null;
	public sc_SayListAdapter say = null;
	private boolean state = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_say);
		initView();
		initData();
		initViewFun();
		sc_MyApplication.getInstance().setSayNumber(0);
	}
	private void initView() {
		btn1 = (Button) findViewById(R.id.fanhui);
		btn2 = (Button) findViewById(R.id.fasong);
		ed1 = (EditText) findViewById(R.id.ed2);
		listview = (ListView) findViewById(R.id.itemlist);
	}

	
	
	private void initData() {
		sc_MyApplication.getInstance().setSayNumber(0);
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		if(sc_MyApplication.getInstance().getSelfHelpFlag()==1){
			return;
		}
		timer = new Timer(false);
		task = new TimerTask() {
			@Override
			public void run() {
				new Thread(runnable2).start();
			}
		};
		timer.schedule(task, 0, 1000*5);
	}
	
	private Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			sc_SendMsg.sendMsg();
		}
	};
	
	private Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			if(state){
				state = false;
				getList();
				handler.sendEmptyMessage(1);
				state = true;
			}
		}
	};
	

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyAdapter();
				break;
			default:
				break;
			}
		}
		
	};
	
//	public void getmsg() {
//		if(MyApplication.list.size()>=0){
//			list.addAll(MyApplication.list);
//			MyApplication.list.clear();
//			notifyAdapter1();
//		}
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void notifyAdapter() {
		if(say==null){
			say = new sc_SayListAdapter(this, list);
			listview.setAdapter(say);
			listview.setSelection(say.getCount());
			listview.setClickable(false);
			listview.setItemsCanFocus(false);
			listview.setCacheColorHint(0);
		}else{
			say.notifyDataSetChanged();
		}
	}

	private void getList() {
		list.clear();
		HashMap<String, Object> map = null;
		List<Map<String, Object>> selectresult = database.selectRow(
				"select * from messageinfo where claimid = '"
						+ sc_MyApplication.getInstance().getClaimid()
						+ "' order by messageid asc", null);
		for (int i = 0; i < selectresult.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("sender", selectresult.get(i).get("sender").toString());
			map.put("content", selectresult.get(i).get("content").toString());
			map.put("create_time", selectresult.get(i).get("create_time")
					.toString());
			list.add(map);
		}
		
		ContentValues value = new ContentValues();
		value.put("status", 0);
		sc_DBHelper.getInstance().update("messageinfo", value, "sender <> ?", 
				new String[]{1+""});
		database.close();
	}

	public void addItem() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sender", sender);
		map.put("content", content);
		map.put("create_time", create_time);
		list.add(map);
		notifyAdapter();
		listview.setSelection(say.getCount());
	}
	/**
	 * 读取文本框中的内容进行发送
	 * 
	 * @author  刘星星
	 */
	private boolean keepMsg() {
		if (ed1.getText().toString().trim().equals("")) {
			Toast.makeText(this, "请输入消息内容。。", 2000).show();
			return false;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {

		} else {
			ContentValues values = new ContentValues();
			values.put("sender", 1);
			values.put("content", ed1.getText().toString());
			values.put("create_time", time);
			values.put("status", 1);
			values.put("claimid", sc_MyApplication.getInstance().getClaimid());
			database.insert("messageinfo", values);
		}
		sender = 1;
		content = ed1.getText().toString();
		create_time = time;
		addItem();
		ed1.setText("");
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ed1.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		return true;
	}

	private void initViewFun() {
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn1) {
			finish();
		} else if (v == btn2) {
			if(keepMsg()){
				new Thread(runnable1).start();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return false;
	}

}
