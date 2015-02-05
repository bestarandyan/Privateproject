/**
 * 
 */
package com.zhihuigu.sosoOffice;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * @author 刘星星
 * @createDate 2013/1/11
 * 加载意见连接类
 *
 */
public class OpinionActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_opinion);
		WebView wv = (WebView) findViewById(R.id.opinionWv);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.loadUrl("http://www.baidu.com");
		wv.setBackgroundColor(0);
		Button btn = (Button) findViewById(R.id.backBtn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
