package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
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
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterSecondActivity extends BaseActivity{
	//��һ����ť
	private  Button submitBtn;
	//���¿ؼ�  ��type��ͷ�Ĵ��������û��Ĵ�ͼ    image��ͷ�Ĵ���
	private ImageView type1,type2,type3,image1,image2,image3;
	
	private int userType = -1;//�����û�����
	
	private SoSoUploadData uploaddata;// ע���������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private TextView introUserType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_registersecond);
		findView();
		if((MyApplication.getInstance().getRoleid()==UserType.UserTypeCustomer.getValue()
				||MyApplication.getInstance().getRoleid()==UserType.UserTypeOwner.getValue()
				||MyApplication.getInstance().getRoleid()==UserType.UserTypeIntermediary.getValue()
				)){
			switch(MyApplication.getInstance().getRoleid()){
			case 4:
				image3.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image1.setVisibility(View.GONE);
			userType = Constant.TYPE_CLIENT;//�ͻ�
			introUserType.setText(getResources().getString(R.string.client_intro_text));
			break;
			case 1:
				image1.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_PROPRIETOR;//ҵ��
			introUserType.setText(getResources().getString(R.string.proprietor_intro_text));
				break;
			case 2:
				image2.setVisibility(View.VISIBLE);
			image1.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_AGENCY;//�н�
			introUserType.setText(getResources().getString(R.string.agency_intro_text));
				break;
			}
		}
	}
	private void findView(){
		submitBtn = (Button) findViewById(R.id.submitBtn);
		type1 = (ImageView) findViewById(R.id.type1);
		type2 = (ImageView) findViewById(R.id.type2);
		type3 = (ImageView) findViewById(R.id.type3);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		introUserType = (TextView) findViewById(R.id.introUserType);
		submitBtn.setOnClickListener(this);
		type1.setOnClickListener(this);
		type2.setOnClickListener(this);
		type3.setOnClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		String userid = "0";
		if(MyApplication.getInstance().getSosouserinfo()!=null&&MyApplication.getInstance().getSosouserinfo().getUserID()!=null){
			userid = MyApplication.getInstance().getSosouserinfo().getUserID();
		}
		ContentValues values = new ContentValues();
		values.put("roleid", MyApplication.getInstance().getRoleid());
		DBHelper.getInstance(this).update(
				"sososettinginfo",
				values,
				"userid = ?",
				new String[] {userid});
		if (values != null) {
			values.clear();
			values = null;
		}
		super.onDestroy();
	}
	
	/**
	 * author by Ring ע��ǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
	 */
	public boolean textValidate() {
		if(userType == -1){
			MessageBox.CreateAlertDialog("�Բ����㻹δѡ���û������޷�������һ����",
					RegisterSecondActivity.this);
			return false;
		} else if (MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null) {
			ContentValues values = new ContentValues();
			values.put("roleid", userType);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] {"0"});
			if (values != null) {
				values.clear();
				values = null;
			}
			Intent i = new Intent();
			MyApplication.getInstance().setRoleid(userType);
			i.putExtra("tag", 1);
			if(ApplicationSettingInfo.initSetting(RegisterSecondActivity.this,"0")){
				i.setClass(RegisterSecondActivity.this, CityListActivity.class);
			}else{
				i.setClass(RegisterSecondActivity.this, MainTabActivity.class);
			}
			RegisterSecondActivity.this.startActivity(i);
			RegisterSecondActivity.this.finish();
			return false;
		} 
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == submitBtn){
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == type1){
			image1.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_PROPRIETOR;//ҵ��
			introUserType.setText(getResources().getString(R.string.proprietor_intro_text));
		}else if(v == type2){
			image2.setVisibility(View.VISIBLE);
			image1.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_AGENCY;//�н�
			introUserType.setText(getResources().getString(R.string.agency_intro_text));
		}else if(v == type3){
			image3.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image1.setVisibility(View.GONE);
			userType = Constant.TYPE_CLIENT;//�ͻ�
			introUserType.setText(getResources().getString(R.string.client_intro_text));
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(getIntent().getIntExtra("tag", 0)==1){
				showExitDialog(this);
			}else{
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return true;
	}
	
	/**
	 * author by Ring �����û�����
	 * return true���ĳɹ���false ����ʧ��
	 */
	public boolean alterUserinfo() {
		if(MyApplication.getInstance(this).getSosouserinfo()!=null
				&&MyApplication.getInstance(this).getSosouserinfo().getUserID()!=null){
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					this).getAPPKEY()));
			params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo().getUserID()));
			params.add(new BasicNameValuePair("RoleID", userType + ""));
			uploaddata = new SoSoUploadData(this, "UserUpdateRoleID.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse(userType);
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
		
	}
	/**
	 * author by Ring ��������û����͵Ľṹ
	 */
	public void dealReponse(int roleid) {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_userinfo set soso_roleid = '"+roleid+"' where soso_userid = '"
		+MyApplication.getInstance(this).getSosouserinfo().getUserID()+"'");
			MyApplication.getInstance(this).getSosouserinfo().setRoleID(roleid);
			MyApplication.getInstance(this).setRoleid(roleid);
		}
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
			if (NetworkCheck.IsHaveInternet(RegisterSecondActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = alterUserinfo();
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
			case 1:// ��ע�����2��ת��ע��ɹ�����3
				if(MyApplication.getInstance(RegisterSecondActivity.this).getRoleid()==Constant.TYPE_CLIENT){
					i.putExtra("tag", 1);
					if(ApplicationSettingInfo.initSetting(RegisterSecondActivity.this,MyApplication.getInstance(RegisterSecondActivity.this)
							.getSosouserinfo().getUserID())){
						i.setClass(RegisterSecondActivity.this, CityListActivity.class);
						RegisterSecondActivity.this.startActivity(i);
						RegisterSecondActivity.this.finish();
					}else{
						if(MyApplication.getInstance().getNotlogin()==1){
							MyApplication.getInstance().setNotlogin(0);
							RegisterSecondActivity.this.finish();
						}else{
							i.setClass(RegisterSecondActivity.this, MainTabActivity.class);
							RegisterSecondActivity.this.startActivity(i);
							RegisterSecondActivity.this.finish();
						}
						
						
					}
				}else{
					i.setClass(RegisterSecondActivity.this,
							RegisterThirdActivity.class);
					i.putExtra("userType", userType);
					RegisterSecondActivity.this.startActivity(i);
					RegisterSecondActivity.this.finish();
				}
				
				break;
			case 2:// ��ע�������ת����¼����
//				i.setClass(RegisterFirstActivity.this, LoginActivity.class);
//				RegisterFirstActivity.this.startActivity(i);
//				RegisterFirstActivity.this.finish();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(R.string.dialog_message_submit_error);
				}
				MessageBox.CreateAlertDialog(message,
						RegisterSecondActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(RegisterSecondActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(RegisterSecondActivity.this);
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
