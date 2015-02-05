/**
 * 
 */
package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.util.Map;

import com.qingfengweb.id.blm_goldenLadies.R;
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
import com.tencent.tauth.Tencent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星 武国庆
 * @createDate 2013、8、26
 *
 */
public class DetailSeriesActivity extends BaseActivity implements OnClickListener{
	private Button backBtn,bottomBackBtn,zixunBtn,shareBtn;
	private ImageView topImg;
	private TextView title,oldMoney,nowMoney,zixun1,zixun2,tel,content;
	RelativeLayout parent;
	String shareFilePath = "";//美图路径
	Map<String,Object> seriesMap = null;
	String phone = "";
	String descrpitionStr = "";
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	LinearLayout topImgLayout;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailseries);
		findview();
		seriesMap = (Map<String, Object>) getIntent().getSerializableExtra("seriesMap");
		initData();
	}
	private void initData(){
		String titleStr = seriesMap.get("name").toString();
		String price1Str = seriesMap.get("price1").toString();
		String price2Str = seriesMap.get("price2").toString();
		descrpitionStr = seriesMap.get("description").toString();
		phone = UserBeanInfo.storeDetail.get(0).get("phonenumber").toString().trim();
		String QQ = UserBeanInfo.storeDetail.get(0).get("qq").toString();
		title.setText(titleStr);
		oldMoney.setText("原价："+price1Str);
		nowMoney.setText("限时特价："+price2Str);
		content.setText(descrpitionStr);
		tel.setText(phone);
		if(QQ!=null && !QQ.equals("")){
			zixun2.setText("QQ咨询:"+QQ);
		}else{
			zixun2.setText("");
		}
		
		
		String imgId = seriesMap.get("imageid").toString();
		if(imgId==null || imgId.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			topImg.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String photoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.RECOMMENDSERIES_IMG_URL+imgId+".png";
			if(new File(photoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				topImg.setImageBitmap(BitmapFactory.decodeFile(photoUrl));
			}else{//如果不存在 则去服务器下载
				topImg.setImageResource(R.drawable.photolist_defimg);
				int width = MyApplication.getInstant().getWidthPixels();
				int height = 200;
				RequestServerFromHttp.downImage(this,topImg,imgId,ImageType.recommendSeries.getValue(),ImgDownType.BigBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.RECOMMENDSERIES_IMG_URL,R.drawable.photolist_defimg);
			}
			shareFilePath = photoUrl;
		}
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.bottomBackBtn).setOnClickListener(this);
		findViewById(R.id.zixunBtn).setOnClickListener(this);
		findViewById(R.id.ShareBtn).setOnClickListener(this);
		findViewById(R.id.telLayout).setOnClickListener(this);
		topImg = (ImageView) findViewById(R.id.tuijianImg);
		title = (TextView) findViewById(R.id.itemTitle);
		oldMoney = (TextView) findViewById(R.id.itemOldMoney);
		nowMoney = (TextView) findViewById(R.id.itemNowMoney);
		zixun1 = (TextView) findViewById(R.id.zixun1);
		zixun2 = (TextView) findViewById(R.id.zixun2);
		tel = (TextView) findViewById(R.id.phoneNumber);
		content = (TextView) findViewById(R.id.tuijianContent);
		parent = (RelativeLayout) findViewById(R.id.parent);
		topImgLayout = (LinearLayout) findViewById(R.id.topImgLayout);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				MyApplication.getInstant().getHeightPixels()/4);
		topImgLayout.setLayoutParams(param);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
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
			intent.putExtra("msgStr", descrpitionStr);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(descrpitionStr, shareFilePath);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, "要结婚啦！");
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, descrpitionStr);
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
			emailShare.startSendToEmailIntent(descrpitionStr, shareFilePath);
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
					wechatShare.shareMSG("我们结婚啦",descrpitionStr, R.drawable.logo, shareFilePath, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG("我们结婚啦，快下载我们精心制作的app分享我们的幸福吧！", descrpitionStr, R.drawable.logo, shareFilePath, 2);
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
		}else if(v.getId() == R.id.telLayout){
			Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+phone));
			startActivity(intent);//开始意图(及拨打电话)
		}
		super.onClick(v);
	}
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, DetailSeriesActivity.this);
        
        final Activity activity = DetailSeriesActivity.this;
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
