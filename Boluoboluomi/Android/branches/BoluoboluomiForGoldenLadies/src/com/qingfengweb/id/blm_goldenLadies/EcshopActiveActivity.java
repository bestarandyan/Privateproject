package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.StoreInfo;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MessageBox;

public class EcshopActiveActivity extends BaseActivity {
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
	private WebView wv;
	private String reponse = "";// 服务器响应值
	private ProgressDialog progressdialog;
	private ImageView image;// 顶部图片
	private TextView text;// 文字描述
	private String description;// 活动描述
	private Bitmap bitmap = null;// 活动图片
	private String activelink = "";// 活动链接
	private boolean click_limit = true;
	private TextView storeName,QQTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_ecshop);
		findView();
		findBottomBtn();
		initView();
		IntegralStoreMainActivity.instantActivity = this;
	}

	/***
	 * author Ring 初始化界面
	 */
	private void initView() {
		List<Map<String, Object>> selectresult = UserBeanInfo.storeDetail;
		if(selectresult!=null && selectresult.size()>0){
			 description = selectresult.get(0).get("shop_activity_text").toString();
		}else{
			return;
		}
		text.setText(description);
		QQTv.setText(selectresult.get(0).get("qq").toString().length()>0?"QQ咨询："+selectresult.get(0).get("qq").toString():"");
		int width = MyApplication.getInstant().getWidthPixels();
		int height = width/2;
		String logo = selectresult.get(0).get("shop_activity_image").toString();
		if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			image.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.INTEGRAL_IMG_URL+logo+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				image.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(this,image,logo,ImageType.MerchanProductImg.getValue(),ImgDownType.BigBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.INTEGRAL_IMG_URL,R.drawable.photolist_defimg);
			}
		}
		image.setImageBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}

	private void findView() {
		// wv = (WebView) findViewById(R.id.wv);
		// wv.getSettings().setDefaultTextEncodingName("utf-8");
		// wv.setBackgroundColor(0);
		image = (ImageView) findViewById(R.id.image);
		text = (TextView) findViewById(R.id.text);
		storeName = (TextView) findViewById(R.id.storeName);
		QQTv = (TextView) findViewById(R.id.QQTv);
		storeName.setText(UserBeanInfo.getInstant().getCurrentStore());
	}

	private void setWvContent() {
		wv.loadDataWithBaseURL("", "深圳的法士大夫<p>1.是短发的法师发<p>2. 史蒂芬金哦啊的房价江苏大丰",
				"text/html", "utf-8", "");
	}

	private void findBottomBtn() {

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
		image.setOnClickListener(this);
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05_on);
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
			Intent i = new Intent(this, IntegralRuleActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab5) {
			click_limit = true;
//			Intent i = new Intent(this, EcshopActiveActivity.class);
//			startActivity(i);
//			finish();
		} else if (v == backBtn) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if(v == image){
			if(activelink!=null&&
					!activelink.equals("")){
				try {
					Uri uri = Uri.parse(activelink);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		click_limit  =true;
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * author by Ring 服务器响应后处理的方法（获取图片）
	 */

	public void dealReponse2(String path) {
		if (reponse.equals("0")) {
			List<Map<String, Object>> selectresult = DBHelper
					.getInstance(this)
					.selectRow(
							"select * from settingsinfo settingsinfo where name='POINT_IMAGE'",
							null);
			ContentValues values = new ContentValues();
			values.put("value", path);
			if (selectresult != null && selectresult.size() > 0) {
				DBHelper.getInstance(this).update("settingsinfo", values,
						"name = ? ", new String[] { "POINT_IMAGE" });
			} else {
				DBHelper.getInstance(this).insert("settingsinfo", values);
			}
		}
	}

	/***
	 * author by Ring 处理界面跳转，从加载页跳转到主界面
	 * 
	 */

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//			if(MyApplication.activity_tag!=12)
//				return;
//			switch (msg.what) {
//			case 1:
//				MessageBox.CreateAlertDialog("提示", "请检测sd卡是否已插入！",
//						EcshopActiveActivity.this);
//				break;
//			case 2:// 登录失败给用户提示
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.prompt),
//						getResources().getString(R.string.login_error_check),
//						EcshopActiveActivity.this);
//				break;
//			case 3:// 没有网络时给用户提示
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.prompt),
//						getResources().getString(R.string.error_net),
//						EcshopActiveActivity.this);
//				break;
//			case 4:// 打开进度条
//				progressdialog = new ProgressDialog(EcshopActiveActivity.this);
//				progressdialog.setMessage(getResources().getString(
//						R.string.progress_message_loading));
//				progressdialog.setCanceledOnTouchOutside(false);
//				progressdialog.show();
//				break;
//			case 5:// 关闭进度条
//				if (progressdialog != null && progressdialog.isShowing()) {
//					progressdialog.dismiss();
//				}
//				break;
//			case 6:// 初始化界面
//				initView();
//				break;
//			}
		};
	};

}
