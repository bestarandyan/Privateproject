/**
 * 
 */
package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.model.StoreInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/21
 * 店家首页类
 */
public class DianJiaMainActivity extends BaseActivity implements OnClickListener{
	private Button backBtn,telBtn;//返回键    打电话的按钮
	private RelativeLayout jianjieLayout,connectLayout;//公司简介    联系方式
	private ImageView topImg;//顶部图片
	private TextView QQTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_dianjiamain);
		findview();
		new Thread(downImgRunnable).start();
	}
	/**
	 * 获取店家首页的图片
	 */
	Runnable downImgRunnable = new Runnable() {
		
		@Override
		public void run() {
//			handler.sendEmptyMessage(1);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				String imgId = UserBeanInfo.storeDetail.get(0).get("store_home_logo").toString();
				String fileUrl = ConstantsValues.SDCARD_ROOT_PATH + ConstantsValues.STORE_HOME_IMG_URL+imgId+".png";
				String phoneNumber = UserBeanInfo.storeDetail.get(0).get("phonenumber").toString();
				String qq = UserBeanInfo.storeDetail.get(0).get("qq").toString();
				if(qq !=null && !qq.equals("")){
					QQTv.setText("QQ咨询："+qq);
				}else{
					QQTv.setText("");
				}
				telBtn.setText(phoneNumber);
				File file = new File(fileUrl);
				if(file.exists()){
					Bitmap bitmap = BitmapFactory.decodeFile(fileUrl);
					topImg.setImageBitmap(bitmap);
				}else if(imgId!=null && !imgId.equals("")){
					RequestServerFromHttp.downImage(DianJiaMainActivity.this,topImg,imgId,ImageType.StoreHomeImage.getValue(),
							ImgDownType.BigBitmap.getValue(),MyApplication.getInstant().getWidthPixels()+"","0",
							true,ConstantsValues.STORE_HOME_IMG_URL,R.drawable.id_dianjia_logo);
				}
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onClick(View v) {
		if(v == backBtn){//返回按钮
			finish();
		}else if(v == jianjieLayout){//简介跳转
			Intent intent = new Intent(this,CompanyJianJieActivity.class);
			startActivity(intent);
		}else if(v == connectLayout){//联系方式
			Intent intent = new Intent(this,CompanyConnectWayActivity.class);
			startActivity(intent);
		}else if(v == telBtn){//电话
			String phoneNumber = UserBeanInfo.storeDetail.get(0).get("phonenumber").toString();
			Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+phoneNumber.trim()));
			startActivity(intent);//开始意图(及拨打电话)
		}
		super.onClick(v);
	}
	private void findview(){
		backBtn = (Button) findViewById(R.id.backBtn);
		jianjieLayout = (RelativeLayout) findViewById(R.id.jianjieLayout);
		telBtn = (Button) findViewById(R.id.telBtn);
		connectLayout = (RelativeLayout) findViewById(R.id.connectLayout);
		topImg = (ImageView) findViewById(R.id.topImg);
		jianjieLayout.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		connectLayout.setOnClickListener(this);
		telBtn.setOnClickListener(this);
		QQTv = (TextView) findViewById(R.id.QQTv);
	}
}
