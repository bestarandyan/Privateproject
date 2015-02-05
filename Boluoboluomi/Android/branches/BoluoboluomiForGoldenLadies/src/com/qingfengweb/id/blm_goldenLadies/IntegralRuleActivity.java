package com.qingfengweb.id.blm_goldenLadies;

import java.util.List;
import java.util.Map;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class IntegralRuleActivity  extends  BaseActivity{
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
	private WebView wv;
	
	
	private boolean click_limit = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_integralrule);
		findView();
		findBottomBtn();
		initData();
		IntegralStoreMainActivity.instantActivity = this;
	}
	private void findView(){
		wv = (WebView) findViewById(R.id.wv);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.setBackgroundColor(0);
	}
	/***
	 * author Ring
	 * 获取积分规则
	 */
	
	public void initData(){
		String content = "无内容";
		
		List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
				.selectRow("select content,title from instructionsinfo where type =2",
						null);
		
		if(selectresult!=null&&selectresult.size()>0){
			if(selectresult.get(selectresult.size()-1).get("content")!=null){
				content = selectresult.get(selectresult.size()-1).get("content").toString();
			}
		}
		
		wv.setBackgroundColor(0);
		wv.getSettings().setDefaultTextEncodingName("utf-8") ;
		wv.loadDataWithBaseURL("", content,
				"text/html", "utf-8", "");
	}
	private void findBottomBtn(){
		backBtn = (Button) findViewById(R.id.backBtn);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab5 = (LinearLayout) findViewById(R.id.tab5);
		tabBtn1 = (Button) findViewById(R.id.tab1Btn);
		tabBtn2 = (Button) findViewById(R.id.tab2Btn);
		tabBtn3 = (Button) findViewById(R.id.tab3Btn);
		tabBtn4 = (Button) findViewById(R.id.tab4Btn);
		tabBtn5 = (Button) findViewById(R.id.tab5Btn);
		backBtn.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab5.setOnClickListener(this);
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04_on);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05);
	}
	@Override
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		if (v == tab1) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab2) {
			if(UserBeanInfo.getInstant().isLogined){
				Intent i = new Intent(this, RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else{
				Intent i = new Intent(this,LoginActivity.class);
				i.putExtra("activityType", 6);
				startActivity(i);
			}
		} else if (v == tab3) {
			Intent i = new Intent(this, MyIntegralActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab4) {
			click_limit = true;
//			Intent i = new Intent(this, IntegralRuleActivity.class);
//			startActivity(i);
//			finish();
		} else if (v == tab5) {
			Intent i = new Intent(this, EcshopActiveActivity.class);
			startActivity(i);
			finish();
		}else if(v == backBtn){
			Intent i = new Intent(this,IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		super.onClick(v);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent i = new Intent(this,IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
