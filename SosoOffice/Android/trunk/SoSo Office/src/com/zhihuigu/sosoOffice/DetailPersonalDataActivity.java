package com.zhihuigu.sosoOffice;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.utils.CommonUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailPersonalDataActivity extends BaseActivity{
	private ImageView headImage;
	private TextView userName,name,gender,birthday,email,phone;
	private TextView userTypeTv;//用户类型
	private Bitmap bitmap = null;
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailpersonal);
		headImage = (ImageView) findViewById(R.id.headImage);
		userName = (TextView) findViewById(R.id.userName);
		name = (TextView) findViewById(R.id.name);
		gender = (TextView) findViewById(R.id.gender);
		birthday = (TextView) findViewById(R.id.birthday);
		email = (TextView) findViewById(R.id.email);
		phone = (TextView) findViewById(R.id.phone);
		backBtn = (Button) findViewById(R.id.backBtn);
		userTypeTv = (TextView) findViewById(R.id.userType);
		backBtn.setOnClickListener(this);
		Bundle bundle = getIntent().getBundleExtra("personalInfo");
		if(!bundle.getString("path").equals("")){
			String path = bundle.getString("path");
			bitmap = CommonUtils.getDrawable(path, headImage);
			headImage.setImageBitmap(bitmap);
		}
		if(MyApplication.getInstance(this).getSosouserinfo(this)!=null){
			String username = MyApplication.getInstance(this).getSosouserinfo(this).getUserName();
			if(username!=null){
				userName.setText(username);
			}
		}
		name.setText(bundle.getString("name"));
		gender.setText(bundle.getString("gender"));
		
		String servertime = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select value from soso_configurationinfo where name='SERVER_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("value") != null) {
				servertime = (selectresult.get(selectresult.size() - 1).get(
						"value").toString());
			}
		}
		
		birthday.setText(getAges(bundle.getString("birthday"), servertime)+"");
		email.setText(bundle.getString("email"));
		phone.setText(bundle.getString("phone"));
		int userType = MyApplication.getInstance().getRoleid();
		if(userType == Constant.TYPE_AGENCY){
			userTypeTv.setText("中介");
		}else if(userType == Constant.TYPE_CLIENT){
			userTypeTv.setText("客户");
		}else if(userType == Constant.TYPE_PROPRIETOR){
			userTypeTv.setText("业主");
		}else{
			userTypeTv.setText("你是不合法用户哦！");
		}
	}
	
	public int getAges(String pre,String now){
		int n = 0;
		if(pre==null||pre.trim().equals("")){
			return 0;
		}
		Date date1 = null;
		Date date2 = null;
		try {
			date2 = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse(now);
		} catch (ParseException e) {
			date2 = new Date();
		}
		try {
			date1 = new SimpleDateFormat("yyyy-dd-MM").parse(pre);
			n = date2.getYear()-date1.getYear();
		} catch (ParseException e) {
		}
		
		if(n<0){
			n = 0;
		}
		return n;
	}
	
	@Override
	protected void onDestroy() {
		if(bitmap!=null && !bitmap.isRecycled()){
			bitmap.recycle();
		}
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return true;
	}
}
