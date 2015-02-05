package com.chinaLife.claimAssistant.activity;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.tools.sc_TopBgSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Sc_ArticleidContent extends Activity implements OnClickListener{

	private Button back;
	public TextView title,content;//标题    内容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_articleidcontent);
		Sc_ExitApplication.getInstance().context = Sc_ArticleidContent.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_ArticleidContent.this);
		back = (Button)findViewById(R.id.fanhui);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		title.setText(getIntent().getStringExtra("title"));
		content.setText(getIntent().getStringExtra("content"));
		initTopBg();
	}
	private void initTopBg(){
		ImageView top_image = (ImageView) findViewById(R.id.top_image);
		new sc_TopBgSet().initTopBg(top_image);
	}
	@Override
	public void onClick(View v) {
		if(v==back){
			this.finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			this.finish();
		}else if(KeyEvent.KEYCODE_MENU == keyCode){
			return false;
		}
		return true;
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) { // 创建菜单项，实现附加功能
//		super.onCreateOptionsMenu(menu);
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//			Intent i = new Intent(this,sc_MainActivity.class);
//			startActivity(i);
//			finish();
//			break;
//		case R.id.menu_exit:
//			sc_ExitApplication.getInstance().showExitDialog();
//			break;
//		default:
//			// 对没有处理的事件，交给父类来处理
//			return super.onOptionsItemSelected(item);
//		}
//		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
//		return true;
//	}
}
