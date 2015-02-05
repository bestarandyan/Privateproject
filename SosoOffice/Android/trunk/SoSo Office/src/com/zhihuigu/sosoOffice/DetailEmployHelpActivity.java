package com.zhihuigu.sosoOffice;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.zhihuigu.sosoOffice.Interface.Activity_interface;
/**
 * @createDate 2013/1/11
 * @author 刘星星
 * 使用帮助详情类
 *
 */
public class DetailEmployHelpActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;
	private TextView title;
	private WebView wv;
	private String name = "";
	private String content = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailemployhelp);
		if(getIntent().getStringExtra("name")!=null){
			name = getIntent().getStringExtra("name");
		}
		if(getIntent().getStringExtra("content")!=null){
			content = getIntent().getStringExtra("content");
		}
		findView();
		initView();
		initData();
		
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			setResult(RESULT_OK);
			finish();
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
		title = (TextView) findViewById(R.id.title);
		wv = (WebView) findViewById(R.id.detailHelpWebView);
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		title.setText(name);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
		wv.setBackgroundColor(0);
	}

	@Override
	public void notifiView() {
		
	}
	
}
