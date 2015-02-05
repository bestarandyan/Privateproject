package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.ContentValues;
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

public class AmendPasswordActivity extends BaseActivity{
	private Button backBtn;//���ذ�ť
	private EditText oldPassEt;//�����������
	private EditText newPassEt;//�����������
	private EditText sureNewPassEt;//�����������
	private Button submitBtn;//�ύ��ť
	
	
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private SoSoUploadData uploaddata;//�޸�������������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_amendpassword);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		oldPassEt = (EditText) findViewById(R.id.oldPassEt);
		newPassEt = (EditText) findViewById(R.id.newPassEt);
		sureNewPassEt = (EditText) findViewById(R.id.sureNewPassEt);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == submitBtn){
			CommonUtils.hideSoftKeyboard(this);
			if(textValidate()){
				new Thread(runnable).start();
			}
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
	
	/**
	 * author by Ring ������������޸����� return true�޸ĳɹ���false �޸�ʧ��
	 */
	public boolean amendPassword() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo().getUserID()));
		params.add(new BasicNameValuePair("OldpassWord", oldPassEt.getText().toString()));
		params.add(new BasicNameValuePair("NewPassword", newPassEt.getText().toString()));
		uploaddata = new SoSoUploadData(this, "ChangePassWord.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * author by Ring �����ύ��¼��Ϣ�ķ�������Ӧֵ
	 */

	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			values.put("soso_password", newPassEt.getText().toString());
			DBHelper.getInstance(AmendPasswordActivity.this).update("soso_userinfo",
					values, "soso_userid = ?",
					new String[] { MyApplication.getInstance(this).getSosouserinfo().getUserID() });
			MyApplication.getInstance(this).getSosouserinfo(this).setPassWord(newPassEt.getText().toString());
			
			if(values !=null){
				values.clear();
				values = null;
			}
			DBHelper.getInstance(AmendPasswordActivity.this).close();
		}
	}
	
	/**
	 * author by Ring ע��ǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
	 */
	public boolean textValidate() {
		if (oldPassEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.oldpasswordnull),
					AmendPasswordActivity.this);
			return false;
		} else if (MyApplication.getInstance(this).getSosouserinfo().getPassWord()!=null
				&&!MyApplication.getInstance(this).getSosouserinfo().getPassWord().equals("")
				&&!oldPassEt.getText().toString().trim().equals(MyApplication.getInstance(this).getSosouserinfo().getPassWord())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.oldpassworderror),
					AmendPasswordActivity.this);
			return false;
		}  else if (newPassEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.newpasswordnull),
					AmendPasswordActivity.this);
			return false;
		} else if (newPassEt.getText().toString().length() < 6
				&& newPassEt.getText().toString().length() > 15) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_count),
					AmendPasswordActivity.this);
			return false;
		} else if (oldPassEt.getText().toString().trim().equals(newPassEt.getText().toString().trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.oldpasswordequal),
					AmendPasswordActivity.this);
			return false;
		} else if (!sureNewPassEt.getText().toString()
				.equals(newPassEt.getText().toString())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_confirm),
					AmendPasswordActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * author by Ring ����ע���ʱ����
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(AmendPasswordActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = amendPassword();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// ��ע�������ת��ע��ɹ�����
				} else {
					handler.sendEmptyMessage(3);// ע��ʧ�ܸ��û���ʾ
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring ����ҳ����ת����
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ��ע�������ת��ע��ɹ�����
				MessageBox.CreateAlertDialog(getResources().getString(R.string.amendpasswordsucess),
						AmendPasswordActivity.this,true);
				break;
			case 2:// ��ע�������ת����¼����
				i.setClass(AmendPasswordActivity.this, LoginActivity.class);
				AmendPasswordActivity.this.startActivity(i);
				AmendPasswordActivity.this.finish();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(R.string.amendpasswordfaile);
				}
				MessageBox.CreateAlertDialog(message,
						AmendPasswordActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(AmendPasswordActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(AmendPasswordActivity.this);
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
}
