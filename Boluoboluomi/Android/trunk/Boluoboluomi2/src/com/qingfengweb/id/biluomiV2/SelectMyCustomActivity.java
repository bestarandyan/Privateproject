package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.adapter.SelectMyCustomAdapter;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.CustomTypeInfo;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.util.MessageBox;

public class SelectMyCustomActivity extends BaseActivity {
	private Button backBtn;
	private Button affirmSubmitBtn;
	private TextView title;
	private GridView gv;
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> list2;
	private int type = 0;
	private String id = "";
	private ProgressDialog progressdialog;
	private DBHelper dbHelper;
	/***
	 * author Ring
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_selectmycustom);
		findView();
		initListDate();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		affirmSubmitBtn = (Button) findViewById(R.id.affirmSubmitBtn);
		title = (TextView) findViewById(R.id.title);
		gv = (GridView) findViewById(R.id.gv);
		gv.setCacheColorHint(0);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		backBtn.setOnClickListener(this);
		affirmSubmitBtn.setOnClickListener(this);
		title.setText(getIntent().getStringExtra("title"));
	}

	private void initListDate() {
		list = new ArrayList<Map<String, Object>>();
		list2 = new ArrayList<Map<String,Object>>();
		dbHelper = DBHelper.getInstance(this);
		getListDate();
		
		
	}
	private void notifyAdapter(){
		SelectMyCustomAdapter adapter = new SelectMyCustomAdapter(this, list,list2);
		gv.setAdapter(adapter);
	}
	/**
	 * 获取我要定制的数据的线程
	 */
	Runnable getCustomTypeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String type = getIntent().getStringExtra("type");
			String sql = "select *from "+CustomTypeInfo.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId()+" and type="+type;
			list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessage(6);
			}
			String msgStr = RequestServerFromHttp.getCustomType(UserBeanInfo.getInstant().getCurrentStoreId(), type, "");
			if(msgStr.startsWith("[")){//获取成功
				JsonData.jsonCustomTypeData(msgStr, dbHelper.open(), UserBeanInfo.getInstant().getCurrentStoreId());
				list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					handler.sendEmptyMessage(6);
				}
			}else if(msgStr.equals("404")){//访问服务器失败
				
			}else{//获取失败
				
			}
		}
	};
	/**
	 * author by 刘星星处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 打开进度条
				progressdialog = new ProgressDialog(SelectMyCustomActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_sumbit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 2:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 3://提交成功给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.custom_submit_success),
						SelectMyCustomActivity.this);
				break;
			case 4:// 提交失败给用户提示
				String submitmessage = "";
					submitmessage=getResources().getString(R.string.custom_submit_error);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),submitmessage,
						SelectMyCustomActivity.this);
				break;
			case 5:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						SelectMyCustomActivity.this);
				break;
			case 6:
				notifyAdapter();
				break;
			}
		}
	};
	private void getListDate() {
//		for (int i = 0; i < 9; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			Bitmap b = CommonUtils.scaleImg(BitmapFactory.decodeResource(
//					getResources(), R.drawable.customize_photo_tuli01), 100,
//					100);
//			map.put("image", b);
//			map.put("", "");
//			list.add(map);
//		}
		new Thread(getCustomTypeRunnable).start();
	}


	@Override
	protected void onDestroy() {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("image") != null) {
					((Bitmap) list.get(i).get("image")).recycle();
				}
			}
			list.clear();
			list = null;
		}

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			Intent intent = new Intent(this, CustomMainActivity.class);
			startActivity(intent);
			finish();
		} else if (v == affirmSubmitBtn) {
			if (id.equals("")) {
				Toast.makeText(this, "对不起，您还未作出选择！", 2000).show();
			} else {
//				dealData();
				Intent intent = new Intent(this, CustomMainActivity.class);
				startActivity(intent);
				finish();
			}

		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, CustomMainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
