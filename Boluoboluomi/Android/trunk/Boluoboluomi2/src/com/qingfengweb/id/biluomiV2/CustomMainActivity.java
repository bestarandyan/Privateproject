package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.MessageBox;

public class CustomMainActivity  extends BaseActivity{
	private Button backBtn;//顶部返回按钮
	private ImageView image1,image2,image3;//缩略图
	private TextView text1,text2,text3;//拍摄场景简单介绍  拍摄道具简单介绍 拍摄服装简单介绍
	private LinearLayout afreshLinear,affirmLinear,myCustom;//底部三个部分  用来做相应的功能  赋予了他们点击事件
	private Bitmap bitmap1 = null;// 图片
	private Bitmap bitmap2 = null;// 图片
	private Bitmap bitmap3 = null;// 图片
	private String ids = "";// 提交的服装，道具，场景的id，以逗号分隔
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_custom);
		findView();
		initData();
	}

	/****
	 * author Ring 初始化数据
	 */

	public void initData() {
		new Thread(getCustomTypeRunnable).start();
	}
	/**
	 * 获取我要定制的数据的线程
	 */
	Runnable getCustomTypeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = RequestServerFromHttp.getCustomType(UserBeanInfo.getInstant().getCurrentStoreId(), "", "");
		}
	};
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		text1 = (TextView) findViewById(R.id.content1);
		text2 = (TextView) findViewById(R.id.content2);
		text3 = (TextView) findViewById(R.id.content3);
		afreshLinear = (LinearLayout) findViewById(R.id.afreshLinear);
		affirmLinear = (LinearLayout) findViewById(R.id.affirmLinear);
		myCustom = (LinearLayout) findViewById(R.id.myCustom);
		backBtn.setOnClickListener(this);
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);
		afreshLinear.setOnClickListener(this);
		affirmLinear.setOnClickListener(this);
		myCustom.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		if(v == backBtn){
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
		}else if(v == afreshLinear){
			
		}else if(v == affirmLinear){
			
		}else if(v == myCustom){
			Intent intent = new Intent(this, MyCustomActivity.class);
			startActivity(intent);
			finish();
		}else if(v == image1){
//			MyApplication.getInstance(this).setType(1);
			Intent intent = new Intent(this, SelectMyCustomActivity.class);
			intent.putExtra("title", "拍摄场景");
			intent.putExtra("type", "1");
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		} else if (v == image2) {
//			MyApplication.getInstance(this).setType(2);
			Intent intent = new Intent(this, SelectMyCustomActivity.class);
			intent.putExtra("title", "拍摄道具");
			intent.putExtra("type", "2");
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		} else if (v == image3) {
//			MyApplication.getInstance(this).setType(3);
			Intent intent = new Intent(this, SelectMyCustomActivity.class);
			intent.putExtra("title", "拍摄服装");
			intent.putExtra("type", "3");
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		click_limit = true;
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 打开进度条
				progressdialog = new ProgressDialog(CustomMainActivity.this);
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
						CustomMainActivity.this);
				initData();
				break;
			case 4:// 提交失败给用户提示
				String submitmessage = "";
				if(reponse.equals("-1022")){
					submitmessage=getResources().getString(R.string.custom_submit_error1);
				}else if(reponse.equals("-1000")){
					submitmessage=getResources().getString(R.string.progress_timeout);
				}else{
					submitmessage=getResources().getString(R.string.custom_submit_error);
				}
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),submitmessage,
						CustomMainActivity.this);
				break;
			case 5:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						CustomMainActivity.this);
				break;
			}
		}
	};

}
