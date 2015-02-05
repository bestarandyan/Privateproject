package com.qingfengweb.id.blm_goldenLadies;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.adapter.HelpListAdapter;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.HelpInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;

public class HelpListActivity extends BaseActivity {
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ListView helpList;
	private Button backBtn;
	private List<Map<String, Object>> list;
	private ProgressDialog progressdialog;
	private  TextView feedbackText;
	private UploadData uploaddata = null;
	private DBHelper db = null;
	private HelpListAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_helplist);
		findView();// 初始化控件
		db = DBHelper.getInstance(this);
		new Thread(getHelpDataRunnable).start();
	}
	private void findView() {
		helpList = (ListView) findViewById(R.id.helpList);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		feedbackText = (TextView) findViewById(R.id.feedbackText);
		feedbackText.setVisibility(View.INVISIBLE);
		feedbackText.setOnClickListener(this);
	}

	
	public void notifyAdapter() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("title", "意见反馈");
		list.add(map);
		adapter = new HelpListAdapter(this, list);
		helpList.setAdapter(adapter);
		helpList.setCacheColorHint(0);
		helpList.setOnItemClickListener(new listItemClickListener());
	}

	class listItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position == list.size()-1){
				Intent intent = new Intent(HelpListActivity.this,FeedbackMainActivity.class);
				startActivity(intent);
			}else{
				Intent i = new Intent(HelpListActivity.this, HelpActivity.class);
				i.putExtra("helpContent", (Serializable) list.get(position));
				startActivity(i);
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		}else if(v == feedbackText){
			Intent intent = new Intent(this,FeedbackMainActivity.class);
			startActivity(intent);
		}
		super.onClick(v);
	}

	/**
	 * @author 刘星星
	 *  获取帮助数据
	 */
	public Runnable getHelpDataRunnable = new Runnable() {
		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			String sql = "select *from "+HelpInfo.TbName +" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId();
			list = db.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(3);
			}
			if (NetworkCheck.IsHaveInternet(HelpListActivity.this)) {
				String msgStr = RequestServerFromHttp.getHelpData(UserBeanInfo.getInstant().getCurrentStoreId(), "");
				if(msgStr.startsWith("[")){//获取数据成功
					JsonData.jsonHelpData(msgStr, db.open());
					list = db.selectRow(sql, null);
					if(list!=null && list.size()>0){
						handler.sendEmptyMessage(3);
					}
				}else if(msgStr.equals("404")){
					
				}else{
					
				}
			}
			click_limit = true;
		}
	};

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 打开进度条
				progressdialog = new ProgressDialog(HelpListActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 2:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 3:// 刷新布局
				notifyAdapter();
				break;
			}
		}
	};
}
