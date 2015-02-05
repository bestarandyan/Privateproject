/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.beans.PreferenceBean;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.JsonData;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;
import com.qingfengweb.weddingideas.filedownload.CallbackImpl;
import com.qingfengweb.weddingideas.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

/**
 * @author 刘星星
 * @createDate 2013、12、31
 * 亲友特惠
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) @SuppressLint("SetJavaScriptEnabled") public class PreferenceActivity extends BaseActivity{
	TextView dspView = null;
	TextView nameTv = null;
	TextView timeLimit = null;
	Button goumaiBtn,shouhou;
	DBHelper dbHelper;
	String storeid = "";
	List<Map<String,Object>> list = null;
	ImageView img = null;
	CallbackImpl callbackImpl = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_preference);
		dbHelper = DBHelper.getInstance(this);
		initView();
		storeid = LoadingActivity.configList.get(0).get("storeid");
	}
	@SuppressWarnings("deprecation")
	private void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		goumaiBtn = (Button) findViewById(R.id.goumaiBtn);
		goumaiBtn.setOnClickListener(this);
		shouhou = (Button) findViewById(R.id.shouhouBtn);
		shouhou.setOnClickListener(this);
		dspView = (TextView) findViewById(R.id.contentTv);
		nameTv = (TextView) findViewById(R.id.titleTv);
		timeLimit = (TextView) findViewById(R.id.timeLimit);
		img = (ImageView) findViewById(R.id.imgView);
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(MyApplication.getInstance().getScreenW()/2, LayoutParams.WRAP_CONTENT);
		param1.setMargins(0, 10, 0, 0);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(MyApplication.getInstance().getScreenW()/2, LayoutParams.WRAP_CONTENT);
		param2.setMargins(0, 10, 0, 0);
		goumaiBtn.setLayoutParams(param1);
		shouhou.setLayoutParams(param2);
		selectSql();
		showProgressDialog("正在加载特惠数据，请稍候...");
		new Thread(getdataRunnable).start();
	}
	Runnable getdataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.getPreferenceData(LoadingActivity.configList.get(0).get("storeid"),
					storeid, 
					"");
			if(msg!=null && msg.length()>0 && msg.startsWith("[")){
				JsonData.jsonProferenceData(msg, dbHelper.open(), LoadingActivity.configList.get(0).get("storeid"));
				selectSql();
			}
		}
	};
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				Bitmap bitmap;
				nameTv.setText(list.get(0).get("a_name").toString());
				timeLimit.setText("有效期："+list.get(0).get("s_time").toString()+"到"+list.get(0).get("e_time").toString());
				dspView.setText("特惠说明："+list.get(0).get("set_desc").toString());
				String imgUrl = list.get(0).get("t_pic").toString();
				if(imgUrl!=null && imgUrl.length()>0){
					imgUrl = "http://img.weddingideas.cn"+imgUrl;
					String[] fileStrings = imgUrl.trim().split("/");
					String  nameString = fileStrings[fileStrings.length-1];
					 File file = new File(ConstantValues.sdcardUrl+ConstantValues.preferenceImgUrl+nameString);
					 if(file.exists()){
						 bitmap = PicHandler.getDrawable(file.getAbsolutePath(), null);
						 if(bitmap!=null){
							 img.setImageBitmap(bitmap);
						 }else{
							    callbackImpl = new CallbackImpl(PreferenceActivity.this,img);
								ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
								imageLoad.loadImageFromUrl(PreferenceActivity.this, R.drawable.img_default, imgUrl, ConstantValues.preferenceImgUrl, callbackImpl, false, 480);
						 }
					 }else{
						 	callbackImpl = new CallbackImpl(PreferenceActivity.this,img);
							ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
							imageLoad.loadImageFromUrl(PreferenceActivity.this, R.drawable.img_default, imgUrl, ConstantValues.preferenceImgUrl, callbackImpl, false, 480);
					 }
				}
				if(progressDialog!=null && progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
			super.handleMessage(msg);
		}
	};
	private void selectSql(){
		String sql = "select * from "+PreferenceBean.tableName + " where storeid='"+storeid+"'";
		list = dbHelper.selectRow(sql, null);
		if(list!=null && list.size()>0){
			handler.sendEmptyMessage(0);
		}else{
			handler.sendEmptyMessage(1);
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v.getId() == R.id.goumaiBtn){//购买咨询
			showCallDialog(LoadingActivity.photoOne);
		}else if(v.getId() == R.id.shouhouBtn){//售后咨询
			showCallDialog(LoadingActivity.photoTwo);
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return false;
	}
}
