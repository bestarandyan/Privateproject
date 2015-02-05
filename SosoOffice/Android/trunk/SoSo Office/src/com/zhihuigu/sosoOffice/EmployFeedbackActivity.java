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
 *���������
 */
public  class EmployFeedbackActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;
	private EditText ideaEt,titleEt;//�����ı���   �����ı���
	private Button submitBtn;//�ύ�����ť
	private RelativeLayout allIdeaLayout,myIdealayout;
	
	private SoSoUploadData uploaddata;// �������������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
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
		if(v == backBtn){//���ؼ�
			setResult(RESULT_OK);
			finish();
		}else if(v == submitBtn){//�ύ�����ť
			CommonUtils.hideSoftKeyboard(this);//���ؼ���
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
	 * �ύ��Ҋ����true,�ύ�ɹ���false �ύʧ��
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
	 * author by Ring ע��ǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
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
	 * author by Ring ����ɾ���ż���ʱ����
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
			case 1:// ���ͳɹ��������Ի�����ʾ�û�
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.sendEmployFeedbacksuccess),
						EmployFeedbackActivity.this,true);
				break;
			case 2:// ��ע�������ת����¼����
				i.setClass(EmployFeedbackActivity.this, LoginActivity.class);
				EmployFeedbackActivity.this.startActivity(i);
				EmployFeedbackActivity.this.finish();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
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
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(EmployFeedbackActivity.this);
				break;
			case 5:// �򿪽�����
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
}
