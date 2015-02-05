package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.View.CustomProgressDialog;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoConfigurationInfo;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.GetConfiguration;
import com.zhihuigu.sosoOffice.utils.GetLocation;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

public class LoadActivity extends BaseActivity {
	private MainFrameTask mMainFrameTask = null;
	private CustomProgressDialog progressDialog = null;
	private SoSoUploadData uploaddata;// ����������ӿ�
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private String username = "";// �û�
	private String password = "";// ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_load);
		mMainFrameTask = new MainFrameTask(this);
		mMainFrameTask.execute();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstance(this).setScreenWidth(dm.widthPixels);
		MyApplication.getInstance(this).setScreenHeight(dm.heightPixels);
//		new Thread(runnable).start();
	}

	@Override
	protected void onDestroy() {
		stopProgressDialog();
		if (mMainFrameTask != null && !mMainFrameTask.isCancelled()) {
			mMainFrameTask.cancel(true);
		}
		runnable_tag = true;
		if (uploaddata != null) {
			uploaddata.overReponse();
		}
		super.onDestroy();
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("���ڼ�����...");
		}
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

	}

	public class MainFrameTask extends AsyncTask<Integer, String, Integer> {
		private LoadActivity mainFrame = null;

		public MainFrameTask(LoadActivity mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			stopProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			boolean b = false;
			if (NetworkCheck.IsHaveInternet(LoadActivity.this)) {
				long starttime = System.currentTimeMillis();
				if (checkLogin()) {
					b = userLogin(username, password);
				}
				GetConfiguration getConfiguration = new GetConfiguration(
						LoadActivity.this);
				getConfiguration.getConfigurations();
				long endtime = System.currentTimeMillis();
				if (endtime - starttime < 2000) {
					try {
						Thread.sleep(2000 - (endtime - starttime));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if (checkLogin()) {
					handler.sendEmptyMessage(1);// ��ת��������
				} else {
					handler.sendEmptyMessage(3);// ��ת����¼����
				}

				return null;
			}
			if (runnable_tag) {
				return null;
			}
			if (b) {
				handler.sendEmptyMessage(1);// ��ת��������
			} else {
				handler.sendEmptyMessage(3);// ��ת����¼����
			}
			// if (appState()) {
			// changeAppState();
			// handler.sendEmptyMessage(1);// ��ת����ҳ
			// } else {
			// handler.sendEmptyMessage(3);// ��ת��������
			// }
			return null;
		}

		@Override
		protected void onPreExecute() {
			startProgressDialog();
		}

		@Override
		protected void onPostExecute(Integer result) {
			stopProgressDialog();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	/***
	 * author by Ring ���ؽ�������ʱ�����ʱ����
	 */
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
		}
	};

	/***
	 * �ж��ǲ��ǵ�һ�ΰ�װ��� true ��Ҫ��ҳ
	 */
	public boolean appState() {
		// SharedPreferences preferences = SharedPreferencesinfo.getdata(this);
		// if (preferences.getInt("app_state", 0) == 0) {
		// return true;
		// }
		return false;
	}

	/***
	 * �������״̬
	 * 
	 */
	public void changeAppState() {
		// SharedPreferencesinfo.setdata(this, 1);
	}

	/***
	 * author by Ring ���������ת���Ӽ���ҳ��ת��������
	 * 
	 */

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ��ת��������
				if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& MyApplication.getInstance(LoadActivity.this)
								.getRoleid() == 0) {
					i.putExtra("tag", 1);
					i.setClass(LoadActivity.this, RegisterSecondActivity.class);
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				} else if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& (MyApplication.getInstance(LoadActivity.this)
								.getRoleid() == Constant.TYPE_AGENCY || MyApplication
								.getInstance(LoadActivity.this).getRoleid() == Constant.TYPE_PROPRIETOR)
						&& (MyApplication.getInstance(LoadActivity.this)
								.getSosouserinfo().getJobsImage() == null || MyApplication
								.getInstance(LoadActivity.this)
								.getSosouserinfo().getJobsImage().equals(""))) {
					if(MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoadActivity.this);
						builder.setMessage("��¼ʧ�ܣ����û����Ϸ���")
								.setTitle(LoadActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent i = new Intent();
										i.setClass(LoadActivity.this, LoginActivity.class);
										LoadActivity.this.startActivity(i);
										LoadActivity.this.finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
						DBHelper.getInstance(LoadActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoadActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.setClass(LoadActivity.this, RegisterThirdActivity.class);
					LoadActivity.this.startActivity(i);
					LoadActivity.this.finish();
				} else if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& MyApplication.getInstance(LoadActivity.this)
								.getSosouserinfo().getUserID() != null) {
					if(MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoadActivity.this);
						builder.setMessage("��¼ʧ�ܣ����û����Ϸ���")
								.setTitle(LoadActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent i = new Intent();
										i.setClass(LoadActivity.this, LoginActivity.class);
										LoadActivity.this.startActivity(i);
										LoadActivity.this.finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
						DBHelper.getInstance(LoadActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoadActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.putExtra("tag", 1);
					if (ApplicationSettingInfo.initSetting(LoadActivity.this,
							MyApplication.getInstance(LoadActivity.this)
									.getSosouserinfo().getUserID())) {
						i.setClass(LoadActivity.this, CityListActivity.class);
					} else {
						i.setClass(LoadActivity.this, MainTabActivity.class);
					}
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				} else {
					if(!ApplicationSettingInfo.isExistUser(LoadActivity.this,"0")){
						i.putExtra("tag", 1);
						i.setClass(LoadActivity.this, RegisterSecondActivity.class);
						LoadActivity.this.startActivity(i);
						stopProgressDialog();
						LoadActivity.this.finish();
						return;
					}
					i.putExtra("tag", 1);
					if (ApplicationSettingInfo.initSetting(LoadActivity.this,"0")) {
						i.setClass(LoadActivity.this, CityListActivity.class);
					} else {
						i.setClass(LoadActivity.this, MainTabActivity.class);
					}
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				}

				break;
			case 2:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoadActivity.this);
				builder.setMessage(
						LoadActivity.this.getResources().getString(
								R.string.error_net))
						.setTitle(
								LoadActivity.this.getResources().getString(
										R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										android.os.Process
												.killProcess(android.os.Process
														.myPid());
										stopProgressDialog();
										finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			case 3://
				if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& MyApplication.getInstance(LoadActivity.this)
								.getRoleid() == 0) {
					i.putExtra("tag", 1);
					i.setClass(LoadActivity.this, RegisterSecondActivity.class);
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				} else if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& (MyApplication.getInstance(LoadActivity.this)
								.getRoleid() == Constant.TYPE_AGENCY || MyApplication
								.getInstance(LoadActivity.this).getRoleid() == Constant.TYPE_PROPRIETOR)
						&& (MyApplication.getInstance(LoadActivity.this)
								.getSosouserinfo().getJobsImage() == null || MyApplication
								.getInstance(LoadActivity.this)
								.getSosouserinfo().getJobsImage().equals(""))) {
					if(MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder1 = new AlertDialog.Builder(
								LoadActivity.this);
						builder1.setMessage("��¼ʧ�ܣ����û����Ϸ���")
								.setTitle(LoadActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent i = new Intent();
										i.setClass(LoadActivity.this, LoginActivity.class);
										LoadActivity.this.startActivity(i);
										LoadActivity.this.finish();
									}
								});
						AlertDialog alert1 = builder1.create();
						alert1.show();
						DBHelper.getInstance(LoadActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoadActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.setClass(LoadActivity.this, RegisterThirdActivity.class);
					LoadActivity.this.startActivity(i);
					LoadActivity.this.finish();
				} else if (MyApplication.getInstance(LoadActivity.this)
						.getSosouserinfo() != null
						&& MyApplication.getInstance(LoadActivity.this)
								.getSosouserinfo().getUserID() != null) {
					if(MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&&MyApplication.getInstance(LoadActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder2 = new AlertDialog.Builder(
								LoadActivity.this);
						builder2.setMessage("��¼ʧ�ܣ����û����Ϸ���")
								.setTitle(LoadActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent i = new Intent();
										i.setClass(LoadActivity.this, LoginActivity.class);
										LoadActivity.this.startActivity(i);
										LoadActivity.this.finish();
									}
								});
						AlertDialog alert2 = builder2.create();
						alert2.show();
						DBHelper.getInstance(LoadActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoadActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.putExtra("tag", 1);
					if (ApplicationSettingInfo.initSetting(LoadActivity.this,
							MyApplication.getInstance(LoadActivity.this)
									.getSosouserinfo().getUserID())) {
						i.setClass(LoadActivity.this, CityListActivity.class);
					} else {
						i.setClass(LoadActivity.this, MainTabActivity.class);
					}
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				} else {
					if(!ApplicationSettingInfo.isExistUser(LoadActivity.this,"0")){
						i.putExtra("tag", 1);
						i.setClass(LoadActivity.this, RegisterSecondActivity.class);
						LoadActivity.this.startActivity(i);
						stopProgressDialog();
						LoadActivity.this.finish();
						return;
					}
					i.putExtra("tag", 1);
					if (ApplicationSettingInfo.initSetting(LoadActivity.this,"0")) {
						i.setClass(LoadActivity.this, CityListActivity.class);
					} else {
						i.setClass(LoadActivity.this, MainTabActivity.class);
					}
					LoadActivity.this.startActivity(i);
					stopProgressDialog();
					LoadActivity.this.finish();
				}
				break;
			}
		};
	};

	/***
	 * author by Ring �ж��Ƿ�Ϊ�Զ���¼ true ���Ԅӵ�� false ���Զ���¼
	 */

	public boolean checkLogin() {
		// "soso_userid text,"+//�û�id
		// "soso_username text,"+//�û���
		// "soso_password text,"+//����
		// "soso_realname text,"+//��ʵ����
		// "soso_registerdate text,"+//ע��ʱ��
		// "soso_userimageurl text,"+//�û�ͼ��url
		// "soso_userimagesd text,"+// �û�ͼ��sd������λ��
		// "soso_logindate text,"+// ��¼ʱ��
		// "soso_sex integer,"+// �Ա� 1��2Ů
		// "soso_roleid text,"+// ��ɫid
		// "soso_rolemc text,"+// ��ɫ����
		// "soso_region text,"+// ����
		// "soso_birthday text,"+// ����
		// "soso_email text,"+// �����ַ
		// "soso_telephone text,"+// �绰
		// "soso_bigintegral text,"+// ����
		// "soso_credibility text,"+// �û����ö�
		// "soso_customerstate integer,"+// �û�״̬ 0������1������
		// "soso_ownerphone text,"+//ҵ���绰
		// "soso_legalperson text,"+//��˾����
		// "soso_owneraddress text,"+//ҵ����ַ
		// "soso_businesslicenseurl text,"+//ҵ��Ӫҵִ��url
		// "soso_businesslicensesd text,"+//ҵ��Ӫҵִ��sd��λ��
		// "loginlast integer,"+// 1��������¼
		// "autologin integer" + //�Զ���¼0��1�Զ���¼
		List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
				.selectRow("select * from soso_userinfo where autologin= 1",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"soso_username") != null
					&& selectresult.get(selectresult.size() - 1).get(
							"soso_userid") != null
					&& selectresult.get(selectresult.size() - 1).get(
							"soso_password") != null) {
				username = selectresult.get(selectresult.size() - 1)
						.get("soso_username").toString();
				password = selectresult.get(selectresult.size() - 1)
						.get("soso_password").toString();
				SoSoUserInfo sosouserinfo = new SoSoUserInfo();
				try {
					sosouserinfo.setRealName(selectresult
							.get(selectresult.size() - 1).get("soso_realname")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setRealName("");
				}
				try {
					sosouserinfo.setRegisterDate(selectresult
							.get(selectresult.size() - 1)
							.get("soso_registerdate").toString());
				} catch (Exception e) {
					sosouserinfo.setRegisterDate("");
				}
				try {
					sosouserinfo.setHeadImage(selectresult
							.get(selectresult.size() - 1)
							.get("soso_userimageurl").toString());
				} catch (Exception e) {
					sosouserinfo.setHeadImage("");
				}
				try {
					sosouserinfo.setLoginDate(selectresult
							.get(selectresult.size() - 1).get("soso_logindate")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setLoginDate("");
				}
				try {
					sosouserinfo.setSex(Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("soso_sex")
							.toString()));
				} catch (Exception e) {
					sosouserinfo.setSex(0);
				}
				try {
					sosouserinfo.setRoleID(Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("soso_roleid")
							.toString()));
				} catch (Exception e) {
					sosouserinfo.setRoleID(0);
				}
				try {
					sosouserinfo.setRoleMC(selectresult
							.get(selectresult.size() - 1).get("soso_rolemc")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setRoleMC("");
				}
				try {
					sosouserinfo.setRegion(selectresult
							.get(selectresult.size() - 1).get("soso_region")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setRegion("");
				}
				try {
					sosouserinfo.setBirthday(selectresult
							.get(selectresult.size() - 1).get("soso_birthday")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setBirthday("");
				}
				try {
					sosouserinfo.setEmail(selectresult
							.get(selectresult.size() - 1).get("soso_email")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setEmail("");
				}
				try {
					sosouserinfo.setTelephone(selectresult
							.get(selectresult.size() - 1).get("soso_telephone")
							.toString());
				} catch (Exception e) {
					sosouserinfo.setTelephone("");
				}
				try {
					sosouserinfo.setBigintegral(selectresult
							.get(selectresult.size() - 1)
							.get("soso_bigintegral").toString());
				} catch (Exception e) {
					sosouserinfo.setBigintegral("");
				}
				try {
					sosouserinfo.setCredibility(selectresult
							.get(selectresult.size() - 1)
							.get("soso_credibility").toString());
				} catch (Exception e) {
					sosouserinfo.setCredibility("");
				}
				try {
					sosouserinfo.setCustomerState(Integer.parseInt(selectresult
							.get(selectresult.size() - 1)
							.get("soso_customerstate").toString()));
				} catch (Exception e) {
					sosouserinfo.setCustomerState(0);
				}
				try {
					sosouserinfo.setZJTele(selectresult
							.get(selectresult.size() - 1)
							.get("soso_ownerphone").toString());
				} catch (Exception e) {
					sosouserinfo.setZJTele("");
				}
				try {
					sosouserinfo.setZJAddress(selectresult
							.get(selectresult.size() - 1)
							.get("soso_owneraddress").toString());
				} catch (Exception e) {
					sosouserinfo.setZJAddress("");
				}
				try {
					sosouserinfo.setJobsImage(selectresult
							.get(selectresult.size() - 1)
							.get("soso_businesslicenseurl").toString());
				} catch (Exception e) {
					sosouserinfo.setJobsImage("");
				}
				
				try {
					sosouserinfo.setZJMC(selectresult
							.get(selectresult.size() - 1)
							.get("soso_legalperson").toString());
				} catch (Exception e) {
					sosouserinfo.setZJMC("");
				}
				try {
					sosouserinfo.setLastLoginDate(selectresult
							.get(selectresult.size() - 1)
							.get("soso_lastlogindate").toString());
				} catch (Exception e) {
					sosouserinfo.setLastLoginDate("1900-01-01 12:00:00");
				}
				sosouserinfo.setUserID(selectresult
						.get(selectresult.size() - 1).get("soso_userid")
						.toString());
				sosouserinfo.setUserName(selectresult
						.get(selectresult.size() - 1).get("soso_username")
						.toString());
				sosouserinfo.setPassWord(selectresult
						.get(selectresult.size() - 1).get("soso_password")
						.toString());
				MyApplication.getInstance(LoadActivity.this).setSosouserinfo(
						sosouserinfo);
				MyApplication.getInstance(LoadActivity.this).setRoleid(
						sosouserinfo.getRoleID());
			}
			if (selectresult != null) {
				selectresult.clear();
				selectresult = null;
			}
			DBHelper.getInstance(this).close();
			return true;
		}
		return false;

	}

	/***
	 * author by Ring �Զ���¼ʱ��⣬ÿ��������������ύ�������� return true �Ѿ��������� false δ��������
	 */
	public boolean checkThreeDay() {
		// String time1 = "";
		// String time2 = "";
		// List<Map<String, Object>> selectresult1 = DBHelper
		// .getInstance(this)
		// .selectRow(
		// "select value from settingsinfo where name='SERVER_TIME'",
		// null);
		// if (selectresult1 != null && selectresult1.size() > 0) {
		// if (selectresult1.get(selectresult1.size() - 1) != null
		// && selectresult1.get(selectresult1.size() - 1).get("value") != null)
		// {
		// time1 = selectresult1.get(selectresult1.size() - 1)
		// .get("value").toString();
		// }
		// }
		// List<Map<String, Object>> selectresult2 = DBHelper.getInstance(this)
		// .selectRow(
		// "select lastlogintime from userinfo where username='"
		// + MyApplication.getInstance(this).getUserinfo()
		// .getUsername()
		// + "' and password='"
		// + MyApplication.getInstance(this).getUserinfo()
		// .getPassword() + "'", null);
		// if (selectresult2 != null && selectresult2.size() > 0) {
		// if (selectresult2.get(selectresult2.size() - 1) != null
		// && selectresult2.get(selectresult2.size() - 1).get("value") != null)
		// {
		// time2 = selectresult2.get(selectresult2.size() - 1)
		// .get("value").toString();
		// }
		// }
		//
		// if (!time1.equals("") && !time2.equals("")) {
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// try {
		// Date date1 = dateFormat.parse(time1);
		// Date date2 = dateFormat.parse(time2);
		// int days = (date1.getDate() - date2.getDate())
		// / (1000 * 60 * 60 * 24);
		// if (days >= 3) {
		// return true;
		// }
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return false;
	}

	/**
	 * author by Ring ��������ύ��¼��Ϣ return true��¼�ɹ���false ��¼ʧ��
	 */
	public boolean userLogin(String username, String password) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("userName", username));
		params.add(new BasicNameValuePair("password", password));
		uploaddata = new SoSoUploadData(this, "UserLogin.aspx", params);
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
			Gson gson = new Gson();// ����Gson����
			SoSoUserInfo sosouserinfo = null;
			try {
				sosouserinfo = gson.fromJson(reponse, SoSoUserInfo.class);// ����json����
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosouserinfo != null && sosouserinfo.getUserName() != null
					&& sosouserinfo.getUserID() != null) {
				values.put("soso_userid", sosouserinfo.getUserID());
				values.put("soso_username", sosouserinfo.getUserName());
				values.put("soso_realname", sosouserinfo.getRealName());
				values.put("soso_registerdate", sosouserinfo.getRegisterDate());
				values.put("soso_userimageurl", sosouserinfo.getHeadImage());
				values.put("soso_logindate", sosouserinfo.getLoginDate());
				values.put("soso_sex", sosouserinfo.getSex());
				values.put("soso_roleid", sosouserinfo.getRoleID());
				values.put("soso_rolemc", sosouserinfo.getRoleMC());
				values.put("soso_region", sosouserinfo.getRegion());
				values.put("soso_birthday", sosouserinfo.getBirthday());
				values.put("soso_email", sosouserinfo.getEmail());
				values.put("soso_telephone", sosouserinfo.getTelephone());
				values.put("soso_bigintegral", sosouserinfo.getBigintegral());
				values.put("soso_credibility", sosouserinfo.getCredibility());
				// "soso_ownerphone text,"+//ҵ���绰
				// "soso_legalperson text,"+//��˾����
				// "soso_owneraddress text,"+//ҵ����ַ
				// "soso_businesslicenseurl text,"+//ҵ��Ӫҵִ��url
				values.put("soso_customerstate",
						sosouserinfo.getCustomerState());
				values.put("soso_ownerphone",
						sosouserinfo.getZJTele());
				values.put("soso_legalperson",
						sosouserinfo.getZJMC());
				values.put("soso_owneraddress",
						sosouserinfo.getZJAddress());
				values.put("soso_businesslicenseurl",
						sosouserinfo.getJobsImage());
				values.put("loginlast", 1);
				List<Map<String, Object>> selectresult = DBHelper.getInstance(
						LoadActivity.this).selectRow(
						"select * from soso_userinfo where soso_userid = '"
								+ sosouserinfo.getUserID() + "'", null);
				if (selectresult != null) {
					if (selectresult.size() <= 0) {
						values.put("soso_lastlogindate", "1900-01-01 12:00:00");
						DBHelper.getInstance(LoadActivity.this).insert(
								"soso_userinfo", values);
						sosouserinfo.setLastLoginDate("1900-01-01 12:00:00");
					} else {
						if(selectresult.get(selectresult.size()-1).get("soso_userimageurl")!=null
								&&!sosouserinfo.equals(selectresult.get(selectresult.size()-1).get("soso_userimageurl").toString())){
							DBHelper.getInstance(this).execSql("update soso_userinfo set soso_userimagesd = '' where soso_userid ='"+sosouserinfo.getUserID()+"'");
						}
						try{
							values.put("soso_lastlogindate", selectresult.get(selectresult.size()-1).get("soso_logindate").toString());
						}catch(Exception e){
							values.put("soso_lastlogindate", "1900-01-01 12:00:00");
						}
						DBHelper.getInstance(LoadActivity.this).update(
								"soso_userinfo", values, "soso_userid = ?",
								new String[] { sosouserinfo.getUserID() });
					}
					sosouserinfo.setUserID(selectresult
							.get(selectresult.size() - 1).get("soso_userid")
							.toString());
					sosouserinfo.setUserName(selectresult
							.get(selectresult.size() - 1).get("soso_username")
							.toString());
					sosouserinfo.setPassWord(selectresult
							.get(selectresult.size() - 1).get("soso_password")
							.toString());
					try {
						sosouserinfo.setLastLoginDate(selectresult
								.get(selectresult.size() - 1)
								.get("soso_lastlogindate").toString());
					} catch (Exception e) {
						sosouserinfo.setLastLoginDate("1900-01-01 12:00:00");
					}
				}
				MyApplication.getInstance(LoadActivity.this).setSosouserinfo(
						sosouserinfo);
				MyApplication.getInstance(LoadActivity.this).setRoleid(
						sosouserinfo.getRoleID());
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			DBHelper.getInstance(LoadActivity.this).close();
		}
	}

}
