package com.zhihuigu.sosoOffice;

import java.util.List;
import java.util.Map;

import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.GetConfiguration;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.PhoneInfo;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class OtherSoftwareInfoActivity extends BaseActivity{
	private RelativeLayout personalDataLayout;//个人资料布局
	private RelativeLayout amendPasswordLayout;//修改密码布局
	private RelativeLayout setLayout;//设置布局
	private RelativeLayout messageInformLayout;//消息通知布局
	private RelativeLayout employHelpLayout;//使用帮助布局
	private RelativeLayout employFeedbackLayout;//使用反馈布局
	private RelativeLayout releaseUpdateLayout;//版本更新布局
//	private Button backBtn;//返回键
	private Button exitLogin;//退出登录按钮
	
	
	private GetConfiguration getconfiguration;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_othersoftware);
		findView();
	}
	private void findView(){
		personalDataLayout = (RelativeLayout) findViewById(R.id.personalData);
		amendPasswordLayout = (RelativeLayout) findViewById(R.id.amendPassword);
		setLayout = (RelativeLayout) findViewById(R.id.set);
		messageInformLayout = (RelativeLayout) findViewById(R.id.messageInform);
		employHelpLayout = (RelativeLayout) findViewById(R.id.employHelp);
		employFeedbackLayout = (RelativeLayout) findViewById(R.id.employFeedback);
		releaseUpdateLayout = (RelativeLayout) findViewById(R.id.releaseUpdate);
//		backBtn = (Button) findViewById(R.id.backBtn);
		exitLogin = (Button) findViewById(R.id.exitLogin);
		personalDataLayout.setOnClickListener(this);
		amendPasswordLayout.setOnClickListener(this);
		setLayout.setOnClickListener(this);
		messageInformLayout.setOnClickListener(this);
		employHelpLayout.setOnClickListener(this);
		employFeedbackLayout.setOnClickListener(this);
		releaseUpdateLayout.setOnClickListener(this);
//		backBtn.setOnClickListener(this);
		exitLogin.setOnClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if((MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null)){
			exitLogin.setVisibility(View.GONE);
		}else{
			exitLogin.setVisibility(View.VISIBLE);
		}
		MyApplication.getInstance().setSearchBack(false);
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		if(v == personalDataLayout){//个人信息
			if((MyApplication.getInstance().getSosouserinfo(this)==null
					||MyApplication.getInstance().getSosouserinfo(this).getUserID()==null)){
				return;
			}
			Intent intent = new Intent(this,PersonalDataSetActivity.class);
			startActivityForResult(intent,1);
		}else if(v == amendPasswordLayout){//修改密码
			if((MyApplication.getInstance().getSosouserinfo(this)==null
					||MyApplication.getInstance().getSosouserinfo(this).getUserID()==null)){
				return;
			}
			Intent intent = new Intent(this,AmendPasswordActivity.class);
			startActivityForResult(intent,2);
		}else if(v == setLayout){//设置
			Intent intent = new Intent(this,SoftwareSetActivity.class);
			startActivityForResult(intent,3);
		}else if(v == messageInformLayout){//消息通知
			Intent intent = new Intent(this,MessageInfromActivity.class);
			startActivityForResult(intent,3);
		}else if(v == employHelpLayout){//使用帮助
			Intent intent = new Intent(this,EmployHelpActivity.class);
			startActivityForResult(intent,4);
		}else if(v == employFeedbackLayout){//使用反馈
			Intent intent = new Intent(this,EmployFeedbackActivity.class);
			startActivityForResult(intent,5);
		}else if(v == releaseUpdateLayout){//版本更新
			new Thread(runnable).start();
			
		}/*else if(v == backBtn){//返回按钮
			MainTabActivity.mTabHost.setCurrentTab(0);
		}*/else if(v == exitLogin){//退出登录
			MyApplication.getInstance().setCityBack(false);
			MyApplication.getInstance().setRoomBack(false);
			MyApplication.getInstance().setLinkManBack(false);
			MyApplication.getInstance().setRoomListBack(false);
//			MyApplication.getInstance().setCollectRoom(false);
			MyApplication.getInstance().setSearchBack(false);
			MyApplication.getInstance().setNotlogin(0);
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
			this.getParent().finish();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return true;
	}
	
	
	/**
	 *检查更新，给用户提示
	 * 作者：Ring
	 * 创建于：2013-3-7
	 */
	public void checkUpdate() {
		List<Map<String, Object>> selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_versioninfo",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if(selectresult.get(selectresult.size() - 1) != null
					&&selectresult.get(selectresult.size() - 1).get("version_number") != null
					&&!selectresult.get(selectresult.size() - 1).get("version_number").toString().trim().equals("")
					&&selectresult.get(selectresult.size() - 1).get("url") != null
					&&!selectresult.get(selectresult.size() - 1).get("url").toString().trim().equals("")
					&&selectresult.get(selectresult.size() - 1).get("ismustupdate") != null
					&&!selectresult.get(selectresult.size() - 1).get("ismustupdate").toString().trim().equals("")){
				String appversion1 = PhoneInfo.getAppVersionName(this);
				String appversion2 = selectresult.get(selectresult.size() - 1).get("version_number").toString();
				final String url = selectresult.get(selectresult.size() - 1).get("url").toString();
				final String ismustupdate = selectresult.get(selectresult.size() - 1).get("ismustupdate").toString();
				boolean b = appversion1.compareTo(appversion2)<0;
				if (b) {
					new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("有新版本是否立即更新？")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if(ismustupdate.equals("1")){
								OtherSoftwareInfoActivity.this.finish();
								System.gc();
								System.exit(0);
							}
							return false;
						}
					})
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url =null;
							try {
								content_url = Uri.parse(url);
								intent.setData(content_url);
								OtherSoftwareInfoActivity.this.startActivity(intent);
							} catch (Exception e) {
								content_url = Uri.parse("http://"+url);
								intent.setData(content_url);
								OtherSoftwareInfoActivity.this.startActivity(intent);
							}
							OtherSoftwareInfoActivity.this.finish();
							System.gc();
							System.exit(0);
						}
					})
					.setNegativeButton("取消", null).show();
				}else{
					new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("已经是最新版本了")
					.setCancelable(false)
					.setPositiveButton("确定", null).show();
				}
			}
		}
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
			if (NetworkCheck.IsHaveInternet(OtherSoftwareInfoActivity.this)) {
				handler.sendEmptyMessage(5);
				if(getconfiguration==null){
					getconfiguration = new GetConfiguration(OtherSoftwareInfoActivity.this);
				}
				boolean b = getconfiguration.getConfigurations();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 从注册界面跳转到注册成功界面
				} else {
					handler.sendEmptyMessage(1);// 注册失败给用户提示
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
				checkUpdate();
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.sendlettlesuccess),
//						OtherSoftwareInfoActivity.this,true);
				break;
			case 2:
//				showDialog();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(
							R.string.checkversion_error);
				}
				MessageBox.CreateAlertDialog(message,
						OtherSoftwareInfoActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(OtherSoftwareInfoActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(OtherSoftwareInfoActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_checkverion));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (getconfiguration != null) {
							getconfiguration.overReponse();
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
