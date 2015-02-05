/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

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

import android.app.Activity;
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

import com.qingfengweb.boluomi.share.ConstantsValuesShare;
import com.qingfengweb.boluomi.share.EmailShare;
import com.qingfengweb.boluomi.share.SMSShare;
import com.qingfengweb.boluomi.share.WechatShare;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.imagehandle.PicHandler;
import com.tencent.tauth.Tencent;

/**
 * @author 刘星星 武国庆
 * @createDate 2013、8、27
 * 产品详情界面
 *
 */
public class DetailProductActivity extends BaseActivity{
	RelativeLayout parent;
	TextView contentTv;
	LinearLayout downLayout;
	ImageView downImg;
	TextView downTV;
	Map<String,Object> map = null;
	String couponid = "";
	String shareFilePath = "";
	String description = "";
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailproduct);
		map = (Map<String, Object>) getIntent().getSerializableExtra("productInfo");
		couponid = map.get("couponid").toString();
		findview();
		shareFilePath = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL+couponid+".png";
		description = map.get("description").toString();
		contentTv.setText(description);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.bottomBackBtn){
    		finish();
    	}else if(v.getId() == R.id.zixunBtn){
    		Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+getResources().getString(R.string.dianjia_phone)));
			startActivity(intent);//开始意图(及拨打电话)
    	}else if(v.getId() == R.id.ShareBtn){//弹出分享对话框
    		showShareDialog(parent);
    	}else if(v.getId() == R.id.sharelayout1){//微博分享
    		Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", shareFilePath);
			intent.putExtra("msgStr", description);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(description, shareFilePath);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, "要结婚啦！");
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, description);
//            if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
            	params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, shareFilePath);
//            }
//        params.putString(shareType == Tencent.SHARE_TO_QQ_TYPE_IMAGE ? Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL 
//        		: Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, "weddingideas");
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, mExtarFlag);
       /* if (shareType == Tencent.SHARE_TO_QQ_TYPE_AUDIO) {
            params.putString(Tencent.SHARE_TO_QQ_AUDIO_URL, mEditTextAudioUrl.getText().toString());
        }*/
        doShareToQQ(params);
        
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(description, shareFilePath);
		}/*else if(v.getId() == R.id.sharelayout5){//二维码分享
			
		}*/else if(v.getId() == R.id.sharelayout6){//更多分享
//			showMoreShareDialog();
			showWechatShareDialog();
//			Intent intent = new Intent(Intent.ACTION_SEND);  
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(shareFilePath)));  //传输图片或者文件 采用流的方式  
//            intent.putExtra(Intent.EXTRA_TEXT, summary);   //附带的说明信息  
//            intent.putExtra(Intent.EXTRA_SUBJECT, "菠萝蜜照片分享");  
//            intent.setType("image/*");   //分享图片  
//            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.shareToGoodFriend){//分享到好友
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize(null,null)){
					wechatShare.shareMSG("我们结婚啦",description, R.drawable.logo, shareFilePath, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG("我们结婚啦，快下载我们精心制作的app分享我们的幸福吧！", description, R.drawable.logo, shareFilePath, 2);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
			
		}else if(v.getId() == R.id.cancle){
			wechatDialog.dismiss();
		}else if(v.getId() == R.id.closeWindowBtn){
			dismiss();
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}else if(v == downLayout){
			if(couponid!=null && !couponid.equals("")&& !couponid.equals("0")){
				if(downTV.getText().toString().trim().equals("查看优惠券")){
					String couponUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL+couponid+".png";
					Intent intent = new Intent(DetailProductActivity.this,ImagePreViewActivity.class);
					intent.putExtra("url", couponUrl);
					startActivity(intent);
				}else{
					downTV.setText("正在下载... ");
					downTV.setTextColor(Color.BLACK);
					new Thread(runnable).start();
				}
				
			}
		}
		super.onClick(v);
	}
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, DetailProductActivity.this);
        
        final Activity activity = DetailProductActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                tencent.shareToQQ(activity, params,null /*new IUiListener() {

                    @Override
                    public void onComplete(JSONObject response) {
                        Util.toastMessage(activity, "onComplete: " + response.toString());
                    }

                    @Override
                    public void onError(UiError e) {
                        Util.toastMessage(activity, "onComplete: " + e.errorMessage, "e");
                    }

                    @Override
                    public void onCancel() {
//                    	if(shareType != Tencent.SHARE_TO_QQ_TYPE_IMAGE){
//                    		Util.toastMessage(activity, "onCancel: ");
//                    	}
                    }

                }*/);
            }
        }).start();
    }

	public void downImage(Context context,ImageView imageView,String imgid,String image_type,
			String download_type,String width,String height,boolean isBackground,String imgUrl,int defId){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", RequestServerFromHttp.APPID));
		list.add(new BasicNameValuePair("appkey", RequestServerFromHttp.APPKEY));
		list.add(new BasicNameValuePair("action", RequestServerFromHttp.ACTION_DOWNLOAD));
		list.add(new BasicNameValuePair("imageid", imgid));
		list.add(new BasicNameValuePair("image_type",image_type));
		list.add(new BasicNameValuePair("download_type", download_type));
		list.add(new BasicNameValuePair("width", width));
		list.add(new BasicNameValuePair("height", height));
		Bitmap bmp = loadImageFromId(context, RequestServerFromHttp.INTERFACE_SYSTEM,list, imgUrl,200,200);
		if(bmp!=null){
			Message msg = new Message();
			msg.obj = imageView;
			msg.what = 0;
			handler.sendMessage(msg);
		}
	}
Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			downImage(DetailProductActivity.this,downImg,couponid,ImageType.couponImg.getValue(),ImgDownType.BigBitmap.getValue(),
					 MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL,R.drawable.photolist_defimg);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				((ImageView)(msg.obj)).setImageResource(R.drawable.customize_xuanze);
				downTV.setText("查看优惠券");
				downTV.setTextColor(Color.WHITE);
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 通过图片id下载图片
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	private  Bitmap loadImageFromId(Context context,String interfaceStr,List<NameValuePair> list,String toSD,final int imgW,final int imgH){
		Bitmap bitmap = null;
		String nameString = list.get(3).getValue()+".png";
		System.out.println("imageID--------------------------"+list.get(3).getValue());
		try {
			HttpPost httpPost = new HttpPost(interfaceStr);
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream isInputStream = entity.getContent();
			bitmap = BitmapFactory.decodeStream(isInputStream);
				File file = new File(FileUtils.SDPATH+toSD+nameString);
				if(bitmap!=null && !file.exists()){
					bitmap = PicHandler.scaleImg(bitmap, imgW,imgH);
					boolean b = PicHandler.OutPutImage(file,bitmap);
					System.out.println("图片存储========================"+(b?"成功":"失败"));
				}else if(bitmap == null && file.exists()){
					bitmap = PicHandler.getDrawable(file.getAbsolutePath(), imgW, imgH);
				}/*else if(bitmap ==null && !file.exists()){
					bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
				}*/
				isInputStream.close();
//			}
		} catch (IOException e) {
			/*if(defId!=0){
				bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
			}*/
			return bitmap;
		}
		return bitmap;
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.bottomBackBtn).setOnClickListener(this);
		findViewById(R.id.zixunBtn).setOnClickListener(this);
		findViewById(R.id.ShareBtn).setOnClickListener(this);
		downLayout = (LinearLayout) findViewById(R.id.downLayout);
		downImg = (ImageView) findViewById(R.id.downImg);
		downTV = (TextView) findViewById(R.id.downTv);
		downLayout.setOnClickListener(this);
//		downImg = findViewById(R.id)
		contentTv = (TextView) findViewById(R.id.contentTv);
		parent = (RelativeLayout) findViewById(R.id.parent);
		if(couponid==null || couponid.equals("") || couponid.equals("0")){
			downLayout.setVisibility(View.GONE);
		}else{
			String couponUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL+couponid+".png";
			File file = new File(couponUrl);
			if(file.exists()){
				downImg.setImageResource(R.drawable.customize_xuanze);
				downTV.setText("查看优惠券");
				downTV.setTextColor(Color.WHITE);
			}
		}
	}
}
