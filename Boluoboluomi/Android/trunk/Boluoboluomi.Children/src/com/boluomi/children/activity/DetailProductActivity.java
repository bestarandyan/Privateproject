/**
 * 
 */
package com.boluomi.children.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.data.UserBeanInfo;
import com.qingfengweb.imagedownload.FileUtils;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.share.EmailShare;
import com.qingfengweb.share.SMSShare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星 武国庆
 * @createDate 2013、8、27
 * 产品详情界面
 *
 */
public class DetailProductActivity extends BaseActivity{
	RelativeLayout parent;
	TextView contentTv,titleTv;
	Map<String,Object> map = null;
	String description = "";
	String titleStr = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailproduct);
		findview();
		description = getIntent().getStringExtra("description");
		contentTv.setText(description);
		titleStr = getIntent().getStringExtra("name");
		titleTv.setText(titleStr);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.shareBtn){//弹出分享对话框
    		showShareDialog(parent);
    	}else if(v.getId() == R.id.sharelayout1){//新浪微博分享
    		Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.SINA_TYPE);
			intent.putExtra("msgStr", description);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(description, description);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.TENCENT_TYPE);
			intent.putExtra("msgStr", description);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(description, description);
		}else if(v.getId() == R.id.sharelayout5){//二维码分享
			
		}else if(v.getId() == R.id.sharelayout6){//更多分享
			Intent intent = new Intent(Intent.ACTION_SEND);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            intent.putExtra(Intent.EXTRA_TEXT, description);   //附带的说明信息  
            intent.putExtra(Intent.EXTRA_SUBJECT, "菠萝蜜照片分享");  
            intent.setType("image/*");   //分享图片  
            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.closeWindowBtn){
			dismiss();
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}
		super.onClick(v);
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.shareBtn).setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.titleTv);
//		downImg = findViewById(R.id)
		contentTv = (TextView) findViewById(R.id.contentTv);
		parent = (RelativeLayout) findViewById(R.id.parent);
	}
}
