package com.qingfengweb.id.blm_goldenLadies;

import com.qingfengweb.id.blm_goldenLadies.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class SuccessRegisterActivity extends BaseActivity{
	private Button backBtn,loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_successregister);
		backBtn = (Button) findViewById(R.id.backBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		backBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == backBtn){
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 3){//点评
				i.setClass(SuccessRegisterActivity.this,CommentActivity.class);
				i.putExtra("map", getIntent().getSerializableExtra("map"));
				startActivity(i);
				finish();
			}else if(getValue == 4){//查件系统
				i.setClass(SuccessRegisterActivity.this,SelectPhotoActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 5){//积分兑换
				i.setClass(SuccessRegisterActivity.this,DetailIntegralActivity.class);
				i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
				startActivity(i);
				finish();
			}else if(getValue == 6){//好友推荐
				IntegralStoreMainActivity.instantActivity.finish();
				i.setClass(SuccessRegisterActivity.this,RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 7){//微信分享
				i.setClass(SuccessRegisterActivity.this,WeiXinActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 8){//我要点评
				i.setClass(SuccessRegisterActivity.this,CommentMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 9){//我要定制
				i.setClass(SuccessRegisterActivity.this,CustomMainActivity.class);
				startActivity(i);
				finish();
			}
		}else if(v == loginBtn){
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 3){//点评
				i.setClass(SuccessRegisterActivity.this,CommentActivity.class);
				i.putExtra("map", getIntent().getSerializableExtra("map"));
				startActivity(i);
				finish();
			}else if(getValue == 4){//查件系统
				i.setClass(SuccessRegisterActivity.this,SelectPhotoActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 5){//积分兑换
				i.setClass(SuccessRegisterActivity.this,DetailIntegralActivity.class);
				i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
				startActivity(i);
				finish();
			}else if(getValue == 6){//好友推荐
				i.setClass(SuccessRegisterActivity.this,RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 7){//微信分享
				i.setClass(SuccessRegisterActivity.this,WeiXinActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 8){//我要点评
				i.setClass(SuccessRegisterActivity.this,CommentMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 9){//我要定制
				i.setClass(SuccessRegisterActivity.this,CustomMainActivity.class);
				startActivity(i);
				finish();
			}
			
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 3){//
				i.setClass(SuccessRegisterActivity.this,CommentActivity.class);
				i.putExtra("map", getIntent().getSerializableExtra("map"));
				startActivity(i);
				finish();
			}else if(getValue == 4){//查件系统
				i.setClass(SuccessRegisterActivity.this,SelectPhotoActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 5){//积分兑换
				i.setClass(SuccessRegisterActivity.this,DetailIntegralActivity.class);
				i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
				startActivity(i);
				finish();
			}else if(getValue == 6){//好友推荐
				i.setClass(SuccessRegisterActivity.this,RecommendFriendActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 7){//微信分享
				i.setClass(SuccessRegisterActivity.this,WeiXinActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 8){//我要点评
				i.setClass(SuccessRegisterActivity.this,CommentMainActivity.class);
				startActivity(i);
				finish();
			}else if(getValue == 9){//我要定制
				i.setClass(SuccessRegisterActivity.this,CustomMainActivity.class);
				startActivity(i);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
