/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.util.Map;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.boluomi.share.ConstantsValuesShare;
import com.qingfengweb.boluomi.share.EmailShare;
import com.qingfengweb.boluomi.share.SMSShare;
import com.qingfengweb.boluomi.share.WechatShare;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.util.MessageBox;
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/26
 *  我要推荐页面
 */
public class TuiJianActivity extends BaseActivity implements OnClickListener{
	private RatingBar xing1,xing2,xing3;
	private ImageView imageView;//图片
	private TextView title;//主题名称
	RelativeLayout parent;
	String shareFilePath = "";//美图路径
	String summary ="";
	Map<String,Object> themeMap = null;
	private LinearLayout xingLayout;
	private TextView successTv;
	private String phone="";//咨询电话
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_tuijian);
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.submitBtn).setOnClickListener(this);
		findViewById(R.id.bottomBackBtn).setOnClickListener(this);
		findViewById(R.id.zixunBtn).setOnClickListener(this);
		findViewById(R.id.ShareBtn).setOnClickListener(this);
		xing1 = (RatingBar) findViewById(R.id.xing1);
		xing2 = (RatingBar) findViewById(R.id.xing2);
		xing3 = (RatingBar) findViewById(R.id.xing3);
		xingLayout = (LinearLayout) findViewById(R.id.xingLayout);
		successTv = (TextView) findViewById(R.id.successTv);
		successTv.setVisibility(View.GONE);
		imageView = (ImageView) findViewById(R.id.tuijianImg);
		title = (TextView) findViewById(R.id.tuijianTitle);
		parent = (RelativeLayout) findViewById(R.id.parent);
		initData();
	}
	
	
	 private void initData(){
		 	phone = UserBeanInfo.storeDetail.get(0).get("phonenumber").toString().trim();
	    	themeMap = (Map<String, Object>) getIntent().getSerializableExtra("themeMap");
	    	String thumb = themeMap.get("thumb").toString();
	    	String name = themeMap.get("name").toString();
	    	float x = Float.parseFloat(themeMap.get("commend").toString());
	    	summary = themeMap.get("description").toString();
	    	title.setText(name);
			if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				imageView.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL+thumb+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					imageView.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					int width = MyApplication.getInstant().getWidthPixels()/3;
					int height = width;
					imageView.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(this,imageView,thumb,ImageType.beautyPhotoThemes.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 width+"",height+"",false,ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL,R.drawable.photolist_defimg);
				}
				shareFilePath = firstPhotoUrl;
			}
	    }
	 Runnable tuijianRunnable = new Runnable() {
		
		@Override
		public void run() {
			String themeid = themeMap.get("id").toString();
			String valuation  = (int)xing1.getRating()+","+(int)xing2.getRating()+","+(int)xing3.getRating();
			String msgStr = RequestServerFromHttp.tuiJianBeautyPhotos(themeid, valuation, UserBeanInfo.getInstant().getUserName(), UserBeanInfo.getInstant().getPassword());
			if(msgStr.startsWith("0")){//推荐提交成功
				handler.sendEmptyMessage(0);
			}else if(msgStr.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(1);
			}else{//推荐失败
				handler.sendEmptyMessage(1);
			}
			
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				xingLayout.setVisibility(View.GONE);
				successTv.setVisibility(View.VISIBLE);
			}else if(msg.what == 1){
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"推荐失败，您可以重新试一下哦！",
						TuiJianActivity.this);
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.submitBtn){
			System.out.println("服装造型："+xing1.getRating());
			System.out.println("拍摄风格："+xing2.getRating());
			System.out.println("总体评分："+xing3.getRating());
			if(xing1.getRating()==0){
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"请对“服装造型”进行评价！",
						TuiJianActivity.this);
				return ;
			}
			
			if(xing2.getRating()==0){
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"请对“拍摄风格”进行评价！",
						TuiJianActivity.this);
				return ;
			}
			
			if(xing3.getRating()==0){
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"请对“总体评分”进行评价！",
						TuiJianActivity.this);
				return ;
			}
			new Thread(tuijianRunnable).start();
		}else if(v.getId() == R.id.bottomBackBtn){
    		finish();
    	}else if(v.getId() == R.id.zixunBtn){
    		Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+phone));
			startActivity(intent);//开始意图(及拨打电话)
    	}else if(v.getId() == R.id.ShareBtn){
    		showShareDialog(parent);
    	}else if(v.getId() == R.id.sharelayout1){//微博分享
    		Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", shareFilePath);
			intent.putExtra("msgStr", summary);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(summary, shareFilePath);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, "要结婚啦！");
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, summary);
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
			emailShare.startSendToEmailIntent(summary, shareFilePath);
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
					wechatShare.shareMSG("我们结婚啦",summary, R.drawable.logo, shareFilePath, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG("我们结婚啦，快下载我们精心制作的app分享我们的幸福吧！", summary, R.drawable.logo, shareFilePath, 2);
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
		}
		super.onClick(v);
	}
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, TuiJianActivity.this);
        
        final Activity activity = TuiJianActivity.this;
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

}
