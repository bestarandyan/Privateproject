package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.MyIntegralInfo;
import com.qingfengweb.model.UserPhotoInfo;
import com.qingfengweb.network.UploadData;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyIntegralActivity extends BaseActivity {
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1, tab2, tab3, tab4, tab5;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private int integral_total = 0;// 总分
	private int integral_register = 0;// 注册所送积分
	private int integral_recommendfriend = 0;// 推荐好友所得积分
	private int integral_recommendfriend_success = 0;// 推荐好友成功后所获积分
	private int integral_recommend_false = 0;// 虚假推荐扣除分数
	private int integral_share = 0;// 微博分享
	private int integral_comment = 0;// 点评获得积分
	private int integral_order = 0;// 积分兑换扣除分数
	private int integral_login = 0;//用户登录积分
	private TextView countIntegral, tv1, tv2, tv3, tv4,tv5,tv6,tv7,tv8;
	private String[] integralStr;
	DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_myintegral);
		findView();
		findBottomBtn();
		dbHelper = DBHelper.getInstance(this);
		IntegralStoreMainActivity.instantActivity = this;
		new Thread(getMyIntegralRunnable).start();
	}
	
	/**
	 * 获取我的积分线程。
	 */
	Runnable  getMyIntegralRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+MyIntegralInfo.TableName+" where username="+UserBeanInfo.getInstant().getUserName();
			List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				integralStr = list.get(0).get("integralstr").toString().split(",");
				handler.sendEmptyMessage(4);
			}
			String msgStr = RequestServerFromHttp.getMyIntegral();
			if(msgStr.startsWith("[")){
				System.out.println(msgStr);
				msgStr = msgStr.substring(1, msgStr.length()-1);
				System.out.println(msgStr);
				ContentValues contentValues = new ContentValues();
				contentValues.put("integralstr", msgStr);
				contentValues.put("username", UserBeanInfo.getInstant().getUserName());
				contentValues.put("password", UserBeanInfo.getInstant().getPassword());
				boolean a = dbHelper.update(MyIntegralInfo.TableName, contentValues, "username=?", new String[]{UserBeanInfo.getInstant().getUserName()});
				if(!a){
					dbHelper.insert(MyIntegralInfo.TableName, contentValues);
				}
				integralStr = msgStr.split(",");
				handler.sendEmptyMessage(4);
			}else{
				System.out.println(msgStr);
			}
		}
	};
	private void findView() {
		countIntegral = (TextView) findViewById(R.id.countIntegral);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv6 = (TextView) findViewById(R.id.tv6);
		tv7 = (TextView) findViewById(R.id.tv7);
		tv8 = (TextView) findViewById(R.id.tv8);
	}


	private void findBottomBtn() {
		backBtn = (Button) findViewById(R.id.backBtn);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab5 = (LinearLayout) findViewById(R.id.tab5);
		tabBtn1 = (Button) findViewById(R.id.tab1Btn);
		tabBtn2 = (Button) findViewById(R.id.tab2Btn);
		tabBtn3 = (Button) findViewById(R.id.tab3Btn);
		tabBtn4 = (Button) findViewById(R.id.tab4Btn);
		tabBtn5 = (Button) findViewById(R.id.tab5Btn);
		backBtn.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab5.setOnClickListener(this);
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03_on);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05);
	}

	@Override
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		if (v == tab1) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab2) {
			if(UserBeanInfo.getInstant().isLogined){
				Intent i = new Intent(this, RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else{
				Intent i = new Intent(this,LoginActivity.class);
				i.putExtra("activityType", 6);
				startActivity(i);
			}
		} else if (v == tab3) {
			click_limit = true;
//			Intent i = new Intent(this, MyIntegralActivity.class);
//			startActivity(i);
//			finish();
		} else if (v == tab4) {
			Intent i = new Intent(this, IntegralRuleActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab5) {
			Intent i = new Intent(this, EcshopActiveActivity.class);
			startActivity(i);
			finish();
		} else if (v == backBtn) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		click_limit = true;
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				Intent i = new Intent();
				switch (msg.what) {
				case 1:// 打开进度条
					progressdialog = new ProgressDialog(MyIntegralActivity.this);
					progressdialog.setMessage(getResources().getString(
							R.string.progress_message_loading));
					progressdialog.setCanceledOnTouchOutside(false);
					progressdialog.show();
					break;
				case 2:// 关闭进度条
					if (progressdialog != null && progressdialog.isShowing()) {
						progressdialog.dismiss();
					}
					break;
				case 3:// 加载网络数据
					break;
				case 4: //获取我的积分成功
					countIntegral.setText(integralStr[0]);
					UserBeanInfo.getInstant().setUserScore(integralStr[0]);
					tv1.setText(integralStr[8]);
					tv2.setText(integralStr[1]);
					tv3.setText(integralStr[2]);
					tv4.setText(integralStr[3]);
					tv5.setText(integralStr[4]);
					tv6.setText(integralStr[5]);
					tv7.setText(integralStr[6]);
					tv8.setText("-"+integralStr[7]);
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

}
