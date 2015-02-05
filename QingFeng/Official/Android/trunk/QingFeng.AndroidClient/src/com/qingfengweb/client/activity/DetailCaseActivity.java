/**
 * 
 */
package com.qingfengweb.client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.qingfengweb.android.R;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.CaseInfo;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.share.AccessTokenKeeper;
import com.qingfengweb.share.ConstantsValues;
import com.qingfengweb.share.EmailShare;
import com.qingfengweb.share.WechatShare;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

/**
 * @author 刘星星
 * @createDate 2013/6/17
 * 案例详情界面
 *
 */
public class DetailCaseActivity extends Activity implements OnClickListener{
	ViewPager casePager;
	private ArrayList<View> pageViews = new ArrayList<View>();  
	LinearLayout dotLayout;
	ImageButton backBtn,shareBtn;
	DBHelper dbHelper = null;
	private String caseId = "";
	TextView nameTv,typeTv,descriptionTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailcase);
		findview();
		initData();
	}
	
	private void findview(){
		nameTv = (TextView) findViewById(R.id.name);
		typeTv = (TextView) findViewById(R.id.type);
		descriptionTv = (TextView) findViewById(R.id.describe);
		casePager = (ViewPager) findViewById(R.id.caseVP);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		backBtn = (ImageButton) findViewById(R.id.detailCase_topLeftBtn);
		backBtn.setOnClickListener(this);
		shareBtn = (ImageButton) findViewById(R.id.detailCase_topRightBtn);
		shareBtn.setOnClickListener(this);
	}
	
	private void notifyPager(){
		casePager.setAdapter(new ViewPagerAdapter(pageViews));
		casePager.setOnPageChangeListener(new CasePageChangeListener());
	}
	
	
	private void initData(){
		caseId = getIntent().getStringExtra("id");
		dbHelper = DBHelper.getInstance(this);
		String sql = "select * from "+CaseInfo.TableName+" where id="+caseId;
		List<Map<String,Object>> imgIdList = dbHelper.selectRow(sql, null);
		if(imgIdList!=null && imgIdList.size()>0){
			String idString = imgIdList.get(0).get("photos").toString();
			String name = imgIdList.get(0).get("name").toString();
			String type = imgIdList.get(0).get("type").toString();
			String descrip = imgIdList.get(0).get("description").toString();
			nameTv.setText(name);
			typeTv.setText(type);
			descriptionTv.setText(descrip);
			String[] strArray = idString.split(",");
			for (int i = 0; i < strArray.length; i++) {
//				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qf_case_zstu);
				pageViews.add(getView(strArray[i]));
				dotLayout.addView(getDotView(R.drawable.qf_case_dot2));
			}
			if(dotLayout.getChildCount()>0){
				dotLayout.getChildAt(0).setBackgroundResource(R.drawable.qf_case_dot1);
			}
			notifyPager();
		}
		
		
	}
	/**
	 * 获取图片控件
	 */
	ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	private View getView(String id){
		ImageView imageView = new ImageView(this);
		File file = new File(FileUtils.SDPATH+FinalValues.CASE_IMG_URL+id+".png");
		if(file.exists()){
			Bitmap bitmap = PicHandler.getDrawable(file.getAbsolutePath(), MyApplication.getInstant().getScreenW(), 100);
			if(bitmap!=null){
				imageView.setImageBitmap(bitmap);
			}else{
				CallbackImpl callbackImpl = new CallbackImpl(this,imageView);
				List<NameValuePair> list1 = new ArrayList<NameValuePair>();
				list1.add(new BasicNameValuePair("appid", AccessServer.APPID));
				list1.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
				list1.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
				list1.add(new BasicNameValuePair("imageid",id));
				list1.add(new BasicNameValuePair("type", "2"));//1代表下载原图
				imageLoad.loadImageFromId(this, AccessServer.CONTENT_INTERFACE, list1, R.drawable.soso, FinalValues.CASE_IMG_URL, 
						callbackImpl, false,false, MyApplication.getInstant().getScreenW(),0,0);
			}
		}else{
			CallbackImpl callbackImpl = new CallbackImpl(this,imageView);
			List<NameValuePair> list1 = new ArrayList<NameValuePair>();
			list1.add(new BasicNameValuePair("appid", AccessServer.APPID));
			list1.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
			list1.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
			list1.add(new BasicNameValuePair("imageid",id));
			list1.add(new BasicNameValuePair("type", "2"));//1代表下载原图
			imageLoad.loadImageFromId(this, AccessServer.CONTENT_INTERFACE, list1, R.drawable.soso, FinalValues.CASE_IMG_URL, 
					callbackImpl, false,false, MyApplication.getInstant().getScreenW(),0,0);
		}
		return imageView;
	}
	private View getDotView(int id){
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(id);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 0, 0);
		imageView.setLayoutParams(params);
		return imageView;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
		}
		return true;
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			i.putExtra("type", 3);
			i.setClass(DetailCaseActivity.this, WeiBoActivity.class);
			DetailCaseActivity.this.startActivity(i);
		}
		
	};
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.setClass(this, WeiBoActivity.class);
		if (v == backBtn) {
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		} else if (v == shareBtn) {
			showDialog();
		} else if (v == shareLayout1) {// 分享到新浪微博
			i.putExtra("type", ConstantsValues.SINA_TYPE);
			this.startActivity(i);
		} else if (v == shareLayout2) {// 腾讯微博
			i.putExtra("type", ConstantsValues.TENCENTWEIBO_TYPE);
			this.startActivity(i);
		} else if (v == shareLayout3) {// 微信
			WechatShare wechat = new WechatShare(this);
			if (wechat.isAuthorize()) {
				wechat.sendMSG("微信测试", "", ConstantsValues.WECHAT_INFO_TYPE);
			} else {
				WechatShare.showAlert(this, "检测到手机未安装微信客户端不能进行分享内容！", "提示");
			}
		} else if (v == shareLayout4) {// 朋友圈
			WechatShare wechat = new WechatShare(this);
			if (wechat.isAuthorize()) {
				wechat.sendMSG("微信测试", "", ConstantsValues.WECHAT_FRIEND_TYPE);
			} else {
				WechatShare.showAlert(this, "检测到手机未安装微信客户端不能进行分享内容！", "提示");
			}
		} else if (v == shareToEmail) {// 分享到邮件
			EmailShare emailshare = new EmailShare(this);
			emailshare.startSendToEmailIntent("测试",
					"/sdcard/qweibosdk2/logo_QWeibo.jpg");
		} else if (v == shareToMsg) {// 分享到短信
			i.putExtra("type", ConstantsValues.TENCENT_TYPE);
			this.startActivity(i);
			// SMSShare smsshare = new SMSShare(this);
			// smsshare.startSMSShare("测试",
			// "/sdcard/qweibosdk2/logo_QWeibo.jpg");
		} else if (v == cancleBtn) {// 取消
			dialog.dismiss();
		}

	}
	Dialog dialog = null;
	LinearLayout shareLayout1,shareLayout2,shareLayout3,shareLayout4;
	TextView shareToMsg,shareToEmail;
	Button cancleBtn;
	private void showDialog(){
		dialog = new Dialog(this,R.style.dialog);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_detailcase, null);
		LayoutParams params = new LayoutParams((int) (MyApplication.getInstant().getScreenW()*0.9),
				LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, params);
		shareLayout1 = (LinearLayout) view.findViewById(R.id.shareLayout1);
		shareLayout2 = (LinearLayout) view.findViewById(R.id.shareLayout2);
		shareLayout3 = (LinearLayout) view.findViewById(R.id.shareLayout3);
		shareLayout4 = (LinearLayout) view.findViewById(R.id.shareLayout4);
		shareToMsg = (TextView) view.findViewById(R.id.shareToDuanxin);
		shareToEmail = (TextView) view.findViewById(R.id.shareToEmail);
		cancleBtn = (Button) view.findViewById(R.id.cancleBtn);
		shareLayout1.setOnClickListener(this);
		shareLayout2.setOnClickListener(this);
		shareLayout3.setOnClickListener(this);
		shareLayout4.setOnClickListener(this);
		shareToEmail.setOnClickListener(this);
		shareToMsg.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		dialog.show();
		
	}
	 // 指引页面更改事件监听器
    class CasePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	for(int i=0;i<5;i++){
        		if(arg0 == i){
        			dotLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_case_dot1);
        		}else{
        			dotLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_case_dot2);
        		}
        	}
        }  
    }  
	/*
     * 通过读取OAuthV2AuthorizeWebView返回的Intent，获取用户授权信息
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        if (requestCode==ConstantsValues.TENCENTWEIBO_TYPE) {
            if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE)    {
            	OAuthV2 oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
                if(oAuth.getStatus()==0){
                	AccessTokenKeeper.keepAccessToken(this,
        					AccessTokenKeeper.TENCENT_PREFERENCES_NAME,
        					oAuth.getAccessToken(), oAuth.getOpenid(),
        					oAuth.getExpiresIn(),oAuth.getOpenkey());
//                	Intent i = new Intent();
//        			i.setClass(this, WeiBoActivity.class);
//        			i.putExtra("type", ConstantsValues.TENCENTWEIBO_TYPE);
//        			startActivity(i);
//                	Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
