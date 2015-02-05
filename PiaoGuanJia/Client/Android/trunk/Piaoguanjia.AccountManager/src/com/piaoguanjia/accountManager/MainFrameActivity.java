package com.piaoguanjia.accountManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainFrameActivity extends BaseActivity {
	public LinearLayout linearInclude, view5,noDataLayout;
	public TextView tv_title,noDataMsg,rl1_tv,rl2_tv;
	public RelativeLayout rl1, rl2;
	public LinearLayout rg_view;//切换的title
	public Button btn1, btn2;
	public ImageView btn_jiantou;
	public Button btn_back;
	public TextView message_person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_mainframe);
		findViewById(R.id.parent).setOnTouchListener(this);
		linearInclude = (LinearLayout) findViewById(R.id.view4);
		linearInclude.setOnTouchListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl1_tv = (TextView) findViewById(R.id.rl1_tv);
		rl2_tv = (TextView) findViewById(R.id.rl2_tv);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		view5 = (LinearLayout) findViewById(R.id.view5);
		rg_view = (LinearLayout) findViewById(R.id.view3);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn2.setBackgroundColor(Color.parseColor("#3ABFDE"));
		btn1.setBackgroundColor(Color.parseColor("#FF8201"));
		btn1.setOnTouchListener(mainviewOntouch);
		btn2.setOnTouchListener(mainviewOntouch);
		btn_jiantou = (ImageView) findViewById(R.id.btn_jiantou);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnTouchListener(backbtnOntouch);
		noDataLayout = (LinearLayout)findViewById(R.id.noDataLayout);
		noDataMsg = (TextView) findViewById(R.id.noDataMsg);
		message_person = (TextView) findViewById(R.id.message_person);
		initView("title", "rb1", "rb2", "btn1", "btn2", false);
		message_person.setVisibility(View.INVISIBLE);
	}

	public void initView(String title, String rb1, String rb2, String btn1,
			String btn2, boolean jiantou) {
		this.tv_title.setText(title);
		this.rl1_tv.setText(rb1);
		this.rl2_tv.setText(rb2);
		this.btn1.setText(btn1);
		this.btn2.setText(btn2);
		this.btn1.setOnClickListener(this);
		this.btn2.setOnClickListener(this);
		btn_jiantou.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		if (!jiantou) {
			btn_back.setVisibility(View.GONE);
			btn_jiantou.setVisibility(View.GONE);
		} else {
			btn_jiantou.setVisibility(View.VISIBLE);
			btn_back.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 添加中心布局
	 */
	public void addCenterView(View view) {
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(prarms);
		linearInclude.addView(view);
	}

	/**
	 * 头部tab布局是否显示
	 * 
	 * @param enable
	 */
	public void setTopEnable(boolean enable) {
		if (enable) {
			rg_view.setVisibility(View.VISIBLE);
		} else {
			rg_view.setVisibility(View.GONE);
		}
	}

	/**
	 * 底部button布局是否显示
	 * 
	 * @param enable
	 */
	public void setBottomEnable(boolean enable) {
		if (enable) {
			view5.setVisibility(View.VISIBLE);
		} else {
			view5.setVisibility(View.GONE);
		}
	}

	public void startActivity(Activity activity, Intent i) {
		activity.startActivity(i);
		activity.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_jiantou) {
			finish();
		}else if(v == btn_back){
			finish();
		}else if(v == rl1){
			rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
			rl2.setBackgroundColor(Color.parseColor("#ffffff"));
			rl1_tv.setTextColor(Color.parseColor("#ffffff"));
			rl2_tv.setTextColor(Color.parseColor("#666666"));
		}else if(v == rl2){
			rl2.setBackgroundColor(Color.parseColor("#3ABFDE"));
			rl1.setBackgroundColor(Color.parseColor("#ffffff"));
			rl2_tv.setTextColor(Color.parseColor("#ffffff"));
			rl1_tv.setTextColor(Color.parseColor("#666666"));
		}
	}
	
	public OnTouchListener backbtnOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btn_jiantou.setBackgroundResource(R.drawable.yz_jilu_ico2);
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				btn_jiantou.setBackgroundResource(R.drawable.cz_top_ico);
			}
			return false;
		}
	};

}
