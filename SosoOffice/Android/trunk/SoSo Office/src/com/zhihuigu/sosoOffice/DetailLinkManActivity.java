/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.zhihuigu.sosoOffice.Interface.Activity_interface;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/2/25
 * 联系人详情类
 *
 */
public class DetailLinkManActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;
	private TextView realName;
	private TextView gender;
	private TextView birthday;
	private TextView company;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detaillinkman);
		findView();
		initView();
		initData();
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
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		realName  = (TextView) findViewById(R.id.name);
		gender = (TextView) findViewById(R.id.gender);
		birthday = (TextView) findViewById(R.id.birthday);
		company = (TextView) findViewById(R.id.companyText);
	}
	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
	}
	@Override
	public void initData() {
		if(getIntent().getBundleExtra("personalInfo")!=null){
//			bundle.putString("path","");
//			bundle.putString("realname", list.get(position).get("realname").toString());
//			bundle.putString("gender", list.get(position).get("sex").toString());
//			bundle.putString("birthday", list.get(position).get("birthday").toString());
//			bundle.putString("company", list.get(position).get("company").toString());
//			intent.putExtra("personalInfo",bundle);
			Bundle b =getIntent().getBundleExtra("personalInfo");
			realName.setText(b.getString("realname"));
			gender.setText(b.getString("gender").equals("1")?"男":"女");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			birthday.setText(b.getString("birthday"));
			try {
				birthday.setText(sdf.format(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse(b.getString("birthday"))));
			} catch (ParseException e) {
				birthday.setText(b.getString("birthday"));
			}
			company.setText(b.getString("company"));
		}
	}
	@Override
	public void notifiView() {
		
	}
	
}
