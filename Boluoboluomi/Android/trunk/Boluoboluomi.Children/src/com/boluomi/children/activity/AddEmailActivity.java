package com.boluomi.children.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.network.NetworkCheck;
import com.boluomi.children.network.UploadData;
import com.boluomi.children.util.MessageBox;
import com.boluomi.children.util.StringUtils;

public class AddEmailActivity  extends BaseActivity{
	private Button backBtn,sendBtn;
	private LinearLayout emailLinear;
	private TextView  address1;
	private EditText et1;//最后一个邮件地址
	private Button btn1;
	private List<TextView> textList = new ArrayList<TextView>() ;//用来保存TextView  
	private List<EditText> etList = new ArrayList<EditText>() ;//用来保存邮件地址控件
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String server_photo_path="";//图片上传成功后，服务器返回的图片地址
	private String templateid="";
	private String tempfile = null;//图片保存的临时文件
	private String[] addresses = null;
	
	
	private UploadData uploaddata = null;
	private boolean uploaddata_tag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_addemail);
		server_photo_path = getIntent().getStringExtra("server_photo_path");
		tempfile = getIntent().getStringExtra("tempfile");
		templateid = getIntent().getStringExtra("templateid");
		findView();//
		initEmailLinear();
	}
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		emailLinear = (LinearLayout) findViewById(R.id.emailLinear);
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		btn1 = (Button) findViewById(R.id.btn);
		btn1.setOnClickListener(this);
		et1 = (EditText) findViewById(R.id.et);
		address1 = (TextView) findViewById(R.id.text);
	}
	/**
	 * 初始化邮件列表控件
	 * 
	 */
	private void initEmailLinear(){
//		addEmailLinear();
	}
	/**
	 * 增加邮件设置界面
	 */
	private void addEmailLinear(){
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.item_email, null);
		linearLayout.setId(emailLinear.getChildCount());
		final TextView text = (TextView) linearLayout.findViewById(R.id.text);
		final Button btn = (Button) linearLayout.findViewById(R.id.btn);
		final EditText et = (EditText) linearLayout.findViewById(R.id.et);
		btn.setId(emailLinear.getChildCount());
		text.setTag(btn);
		linearLayout.setTag(text);
		address1.setText("地址"+(emailLinear.getChildCount()+2)+":");
		btn.setTag(R.layout.item_email,linearLayout);
		btn.setTag(R.id.text,text);
		btn.setTag(R.id.et,et);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				textList.remove(v.getTag(R.id.text));
				etList.remove(v.getTag(R.id.et));
				emailLinear.removeView((View) v.getTag(R.layout.item_email));
				address1.setText("地址"+(emailLinear.getChildCount()+1)+":");
				for(int i=0;i<textList.size();i++){
					textList.get(i).setText("地址"+(i+1)+":");
				}
			}
		});
		textList.add(text);
		etList.add(et);
		for(int i=0;i<textList.size();i++){
			textList.get(i).setText("地址"+(i+1)+":");
		}
		emailLinear.addView(linearLayout,emailLinear.getChildCount());
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent i = new Intent(this,TemplateImagePreviewActivity.class);
			i.putExtra("bundle", getIntent().getBundleExtra("bundle"));
			i.putExtra("type",  getIntent().getIntExtra("type", 0));
			startActivity(i);
			finish();
		}else if(v == sendBtn){
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == btn1){
			addEmailLinear();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this,TemplateImagePreviewActivity.class);
			i.putExtra("bundle", getIntent().getBundleExtra("bundle"));
			i.putExtra("type",  getIntent().getIntExtra("type", 0));
			startActivity(i);
			finish();
	
		}
		return false;
	}
	
	/**
	 * author by Ring 提交前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if(etList!=null&&etList.size()>0){
			System.out.println(etList.size());
			for(int i= 0;i<etList.size();i++){
				System.out.println(etList.get(i).getText().toString());
				if(etList.get(i)!=null&&etList.get(i).getText().toString().trim().equals("")){
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt), getResources()
									.getString(R.string.email_null),
							AddEmailActivity.this);
					return false;
				}else if(etList.get(i)!=null&&!StringUtils.isEmail(etList.get(i).getText().toString())){
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt), getResources()
									.getString(R.string.email_error),
							AddEmailActivity.this);
					return false;
				}
			}
		}
		System.out.println(et1.getText().toString());
		if(et1!=null&&et1.getText().toString().trim().equals("")){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.email_null),
							AddEmailActivity.this);
			return false;
		}else if(et1!=null&&!StringUtils.isEmail(et1.getText().toString())){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.email_error),
					AddEmailActivity.this);
			return false;
		}
		return true;
	}
	
	public String getTarget(){
		StringBuffer str = new StringBuffer();
		if(etList!=null&&etList.size()>0){
			for(int i= 0;i<etList.size();i++){
				if(etList.get(i)!=null&&!etList.get(i).getText().toString().trim().equals("")){
					str.append(etList.get(i).getText().toString());
					str.append(";");
				}
			}
		}
		if(et1!=null&&!et1.getText().toString().trim().equals("")){
			str.append(et1.getText().toString());
			str.append(";");
		}
		String address = str.substring(0, str.length()-1).toString();
		addresses = address.split(";");
		return address;
	}
	
	
	/***
	 * author by Ring
	 * 上传邀请数据
	 */
	
	public boolean uploadMyData(){
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		params.add(new BasicNameValuePair("action", "add_invitation"));
//		params.add(new BasicNameValuePair("username", MyApplication
//				.getInstance(this).getUserinfo().getUsername()));
//		params.add(new BasicNameValuePair("password", MyApplication
//				.getInstance(this).getUserinfo().getPassword()));
//		params.add(new BasicNameValuePair("type", 1+""));
//		params.add(new BasicNameValuePair("templateid", templateid));
//		params.add(new BasicNameValuePair("image", server_photo_path));
//		params.add(new BasicNameValuePair("target",  getTarget()));
//		uploaddata = new UploadData(this, "invitation.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		params.clear();
//		params = null;
//		if (reponse.equals("0")) {
//			return true;
//		} else {
//			return false;
//		}
		return true;
	}
	
	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
//			if (click_limit) {
//				click_limit = false;
//			} else {
//				return;
//			}
//			if (NetworkCheck.IsHaveInternet(AddEmailActivity.this)) {
//				uploaddata_tag = true;
//				UploadFile.upload_tag = true;
//				boolean c=false;
//				boolean b = false;
//				handler.sendEmptyMessage(5);
//				if(uploaddata_tag){
//					c = uploadFile();
//				}
//				if(uploaddata_tag){
//					b = uploadMyData();
//				}
//				handler.sendEmptyMessage(6);
//				if(!uploaddata_tag){
//					click_limit = true;
//					return;
//				}
//				if (b&&c) {
//					handler.sendEmptyMessage(1);// 跳转到成功界面
//				} else {
//					handler.sendEmptyMessage(3);// 提交失败给用户提示
//				}
//			} else {
//				handler.sendEmptyMessage(4);// 没有网络时给用户提示
//
//			}
//			click_limit = true;
		}
	};
	
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//			if(MyApplication.activity_tag!=4)
//				return;
//			Intent i = new Intent();
//			switch (msg.what) {
//			case 1:// 上传数据成功
//				startSendToEmailIntent();
//			 break;
//			case 3:// 上传数据失败
//				String errormsg = "";
//				if(reponse.equals("-1000")){
//					errormsg = getResources().getString(R.string.progress_timeout);
//				}else{
//					errormsg = getResources().getString(R.string.upload_photo_message2);
//				}
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.prompt),
//						errormsg,
//						AddEmailActivity.this);
//				break;
//			case 4:// 没有网络时给用户提示
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.prompt),
//						getResources().getString(R.string.person_error_net),
//						AddEmailActivity.this);
//				break;
//			case 5:// 打开进度条
//				progressdialog = new ProgressDialog(AddEmailActivity.this);
//				progressdialog.setMessage(getResources().getString(
//						R.string.photo_progress3));
//				progressdialog.setCanceledOnTouchOutside(false);
//				progressdialog.setOnKeyListener(new OnKeyListener() {
//
//					@Override
//					public boolean onKey(DialogInterface dialog, int keyCode,
//							KeyEvent event) {
//						uploaddata_tag = false;
//						UploadFile.upload_tag = false;
//						if (uploaddata != null) {
//							uploaddata.overReponse();
//						}
//						return false;
//					}
//				});
//				progressdialog.show();
//				break;
//			case 6:// 关闭进度条
//				if (progressdialog != null && progressdialog.isShowing()) {
//					progressdialog.dismiss();
//				}
//				break;
//			}
		};
	};
	
	/***
	 * author by Ring 发送邮件
	 */

	private void startSendToEmailIntent() {
		String contentDetails = "";

		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		intent.setType("text/plain");
		intent.setType("image/*"); // 分享图片信息类型
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
				intent, 0);

		if (!resInfo.isEmpty()) {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			for (ResolveInfo info : resInfo) {
				Intent targeted = new Intent(Intent.ACTION_SEND);
				targeted.setType("image/*"); // 分享图片信息类型
				targeted.setType("text/plain");
				targeted.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(tempfile)));
				targeted.putExtra(Intent.EXTRA_TEXT, "");
				targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
				targeted.putExtra(Intent.EXTRA_EMAIL, addresses);
				ActivityInfo activityInfo = info.activityInfo;
				if (activityInfo.packageName.contains("bluetooth")
						|| activityInfo.name.contains("bluetooth")) {
					continue; // 过滤蓝牙应用
				}
				if (activityInfo.packageName.contains("com.my.activity")
						|| activityInfo.name.contains("com.my.activity")) {
					continue; // 过滤自己的应用包
				}
				if (activityInfo.packageName.contains("gm")
						|| activityInfo.name.contains("mail")) {
					targeted.putExtra(Intent.EXTRA_TEXT, contentDetails);
				} else if (activityInfo.packageName.contains("zxing")) {
					continue; // 过滤自己的应用包
				} else {
					continue; // 过滤自己的应用包
				}
				targeted.setPackage(activityInfo.packageName);
				targetedShareIntents.add(targeted);
			}
			// 分享框标题
			if (targetedShareIntents != null && targetedShareIntents.size() > 0) {
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "选择程序分享");
				if (chooserIntent == null) {
					return;
				}
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				try {
					startActivity(chooserIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Intent i2 = new Intent(Intent.ACTION_VIEW,
							Uri.parse("mailto:" + "simple@163.com"));
					startActivity(i2);
				}
			} else {
				Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
						+ "simple@163.com"));
				startActivity(i3);
			}

		}
	}
	
	
	/***
	 * author by Ring
	 * 上传电子请帖图片
	 */
	public boolean uploadFile(){
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("appid",  MyApplication.getInstance(
//				this).getAPPID());
//		params.put("appkey", MyApplication.getInstance(
//				this).getAPPKEY());
//		params.put("action", "upload_invitation_image");
//		params.put("username", MyApplication
//				.getInstance(this).getUserinfo().getUsername());
//		params.put("password", MyApplication
//				.getInstance(this).getUserinfo().getPassword());
//		params.put("file", new File(tempfile).getName());
//		Map<String, File> filesmap = new HashMap<String, File>();
//		filesmap.put(new File(tempfile).getName(), new File(tempfile));
//		try {
//			reponse = UploadFile.post(MyApplication.getInstance(this).getURL()+"invitation.aspx", params, filesmap);
//		} catch (IOException e) {
//			reponse = "初始值";
//			e.printStackTrace();
//		}
//		System.out.println(params+"---"+reponse);
//		if (reponse.contains("/")) {
//			server_photo_path = reponse;
//			return true;
//		} else {
			return false;
//		}
	}
}
