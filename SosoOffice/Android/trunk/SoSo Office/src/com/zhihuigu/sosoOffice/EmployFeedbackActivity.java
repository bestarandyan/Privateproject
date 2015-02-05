package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
/**
 * @createDate 2013/1/11
 * @author qingfeng
 *意见反馈类
 */
public  class EmployFeedbackActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;
	private EditText ideaEt,titleEt;//内容文本框   标题文本框
	private Button submitBtn;//提交意见按钮
	private RelativeLayout allIdeaLayout,myIdealayout;
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_employfeedback);
		findView();
		initView();
		initData();
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){//返回键
			setResult(RESULT_OK);
			finish();
		}else if(v == submitBtn){//提交意见按钮
			CommonUtils.hideSoftKeyboard(this);//隐藏键盘
			if((MyApplication.getInstance().getSosouserinfo(this)==null
					||MyApplication.getInstance().getSosouserinfo(this).getUserID()==null)){
				return;
			}
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == allIdeaLayout){
			Intent intent = new Intent(this,OpinionActivity.class);
			startActivityForResult(intent, 0);
		}else if(v == myIdealayout){
			Intent intent = new Intent(this,OpinionActivity.class);
			startActivityForResult(intent, 1);
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		ideaEt = (EditText) findViewById(R.id.ideaEt);
		titleEt = (EditText) findViewById(R.id.titleEt);
		submitBtn  = (Button) findViewById(R.id.submitBtn);
		allIdeaLayout = (RelativeLayout) findViewById(R.id.AllCustomLayout);
		myIdealayout = (RelativeLayout) findViewById(R.id.myCustomLayout);
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		allIdeaLayout.setOnClickListener(this);
		myIdealayout.setOnClickListener(this);
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void notifiView() {
		
	}
	/**
	 * 提交意見反饋true,提交成功，false 提交失败
	 */
	public boolean sendEmployFeedback() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserID", MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID()));
		params.add(new BasicNameValuePair("Title", titleEt.getText().toString()));
		params.add(new BasicNameValuePair("Content", ideaEt.getText().toString()));
		params.add(new BasicNameValuePair("FdType", "1"));
		params.add(new BasicNameValuePair("State", "1"));

		uploaddata = new SoSoUploadData(this, "FeedbackAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (ideaEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.EmployFeedbackcontent_null),
					EmployFeedbackActivity.this);
			return false;
		}else if (titleEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.EmployFeedbacktitle_null),
					EmployFeedbackActivity.this);
			return false;
		}
		return true;
	}
	
	/**
	 * author by Ring 处理删除信件耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(EmployFeedbackActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = sendEmployFeedback();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = true;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 从注册界面跳转到注册成功界面
				} else {
					handler.sendEmptyMessage(3);// 注册失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 发送成功，跳出对话框提示用户
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.sendEmployFeedbacksuccess),
						EmployFeedbackActivity.this,true);
				break;
			case 2:// 从注册界面跳转到登录界面
				i.setClass(EmployFeedbackActivity.this, LoginActivity.class);
				EmployFeedbackActivity.this.startActivity(i);
				EmployFeedbackActivity.this.finish();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(
							R.string.sendEmployFeedbackerror);
				}
				MessageBox.CreateAlertDialog(message,
						EmployFeedbackActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(EmployFeedbackActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(EmployFeedbackActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
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
			}
		};
	};
}
