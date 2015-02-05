/**
 * 
 */
package com.qingfengweb.client.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.android.R;

/**
 * @author 刘星星
 * @createDate 2013、7、25
 *
 */
public class DetailJobActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;//返回按钮
	private Button joinBtn;//加入我们按钮
	private TextView jobName,title1,title2,title3;//工作名称，岗位描述，岗位职责，岗位要求
	private LinearLayout layout1,layout2,layout3;//岗位描述布局，岗位职责布局，岗位要求布局
	HashMap<String,Object> map = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailjob);
		findview();//找到控件
		initData();
	}
	
	private void findview(){
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		joinBtn = (Button) findViewById(R.id.joinBtn);
		jobName = (TextView) findViewById(R.id.jobName);
		title1 = (TextView) findViewById(R.id.title1);
		title2 = (TextView) findViewById(R.id.title2);
		title3 = (TextView) findViewById(R.id.title3);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		layout3 = (LinearLayout) findViewById(R.id.layout3);
		backBtn.setOnClickListener(this);
		joinBtn.setOnClickListener(this);
	}
	private void initData(){
		map = (HashMap<String, Object>) getIntent().getSerializableExtra("detail_job");
		String jName = map.get("name").toString();
		jobName.setText(jName);
		TextView t1 = getTextView();
		t1.setText("工作地址：上海市普陀区");
		layout1.addView(t1);
		TextView t2 = getTextView();
		t2.setText(map.get("responsibility").toString());
		layout2.addView(t2);
		TextView t3 = getTextView();
		t3.setText(map.get("requirements").toString());
		layout3.addView(t3);
	}
	private TextView getTextView(){
		TextView t = (TextView) LayoutInflater.from(this).inflate(R.layout.my_textview, null);
		return t;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){//返回键按钮事件监听
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == joinBtn){//申请职位按钮事件监听
			Intent intent = new Intent(this,ApplyJobActivity.class);
			intent.putExtra("jobid", map.get("id").toString());
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
	}

}
