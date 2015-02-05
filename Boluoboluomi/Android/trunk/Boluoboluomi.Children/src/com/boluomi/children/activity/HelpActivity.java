package com.boluomi.children.activity;

import java.util.List;
import java.util.Map;

import com.boluomi.children.R;
import com.boluomi.children.database.DBHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends BaseActivity {
	private WebView helpContent;
	private Button backBtn;
	private TextView title;
	private String instructionid = "";
	private int type;
	private Map<String,Object> map = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_help);
		map = (Map<String, Object>) getIntent().getSerializableExtra("helpContent");
		helpContent = (WebView) findViewById(R.id.webView);
		title = (TextView) findViewById(R.id.title);
		initData();
		// helpContent.loadUrl("file:///android_asset/main_help.html");
		backBtn = (Button) findViewById(R.id.backBtn);

		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		}
		super.onClick(v);
	}


	/***
	 * author Ring 获取帮助信息
	 */

	public void initData() {
		String titletext = "标题";
		String content = "无内容";
		titletext = map.get("title").toString();
		content = map.get("content").toString();
		title.setText(titletext);
		helpContent.setBackgroundColor(0);
		helpContent.getSettings().setDefaultTextEncodingName("utf-8");
		// helpContent.loadData(getIntent().getStringExtra("content").toString(),
		// "text/html", "utf-8");
		helpContent.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
	}
}
