/**
 * 
 */
package com.boluomi.children.activity;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.data.UserBeanInfo;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.share.EmailShare;
import com.qingfengweb.share.SMSShare;

/**
 * @author qingfeng
 *
 */
public class DetailBeautyActivity extends BaseActivity implements OnClickListener{
	private Button backBtn,bottomBackBtn,zixunBtn,shareBtn;
	private TextView tuijianTv,admireTv,nameTv,contentTv,titleTv;
	private ImageView photoImg;
	private RatingBar ratingBar;//推荐热度
	RelativeLayout parent;
	String shareFilePath = "";//美图路径
	Map<String,Object> themeMap = null;
	String summary = "";
	private String phone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.a_detailbeauty);
    	findview();
    	initData();
    }
    private void initData(){
    	themeMap = (Map<String, Object>) getIntent().getSerializableExtra("theme");
    	phone = UserBeanInfo.storeDetail.get(0).get("phonenumber").toString().trim();
    	String thumb = themeMap.get("thumb").toString();
    	String name = themeMap.get("name").toString();
    	float x = Float.parseFloat(themeMap.get("commend").toString());
    	summary = themeMap.get("description").toString();
    	nameTv.setText(name);
    	contentTv.setText(summary);
    	titleTv.setText(name);
    	ratingBar.setIsIndicator(true);
    	ratingBar.setRating(x);
		if(thumb==null || thumb.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			photoImg.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL+thumb+".png";
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				int w = MyApplication.getInstant().getWidthPixels();
				Bitmap bitmap = PicHandler.getDrawable(firstPhotoUrl, photoImg);
				bitmap = PicHandler.cutImg(bitmap, w, 250);
				photoImg.setImageBitmap(bitmap);
			}else{//如果不存在 则去服务器下载
				int width = MyApplication.getInstant().getWidthPixels()/4;
				int height = width;
				photoImg.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(this,photoImg,thumb,ImageType.beautyPhotoThemes.getValue(),ImgDownType.ThumbBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.BEAUTYPHOTO_THEMES_IMG_URL,R.drawable.photolist_defimg);
			}
			shareFilePath = firstPhotoUrl;
		}
    }
    @Override
    public void onClick(View v) {
    	if(v == backBtn){
    		finish();
    	}else if(v == bottomBackBtn){
    		finish();
    	}else if(v == zixunBtn){
    		Intent intent = new Intent(Intent.ACTION_CALL,             //创建一个意图                     
					Uri.parse("tel:"+phone));
			startActivity(intent);//开始意图(及拨打电话)
    	}else if(v == shareBtn){
    		showShareDialog(parent);
    	}else if(v == tuijianTv){//推荐
    		if(!UserBeanInfo.getInstant().isLogined){
    			Intent intent = new Intent(this,LoginActivity.class);
    			intent.putExtra("activityType", 1);
    			intent.putExtra("themeMap", (Serializable)themeMap);
        		startActivity(intent);
    		}else{
    			Intent intent = new Intent(this,TuiJianActivity.class);
        		intent.putExtra("themeMap", (Serializable)themeMap);
        		startActivity(intent);
    		}
    	}else if(v == admireTv){//欣赏美图
    		Intent intent = new Intent(this,BeautyPhotosListActivity.class);
    		intent.putExtra("themeMap", (Serializable)themeMap);
    		startActivity(intent);
    	}else if(v.getId() == R.id.sharelayout1){//微博分享
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", shareFilePath);
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.SINA_TYPE);
			intent.putExtra("msgStr", summary);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(summary, shareFilePath);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", shareFilePath);
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.TENCENT_TYPE);
			intent.putExtra("msgStr", summary);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(summary, shareFilePath);
		}else if(v.getId() == R.id.sharelayout5){//二维码分享
			
		}else if(v.getId() == R.id.sharelayout6){//更多分享
//			showMoreShareDialog();
			Intent intent = new Intent(Intent.ACTION_SEND);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(shareFilePath)));  //传输图片或者文件 采用流的方式  
            intent.putExtra(Intent.EXTRA_TEXT, summary);   //附带的说明信息  
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
    	parent = (RelativeLayout) findViewById(R.id.parent);
    	backBtn = (Button) findViewById(R.id.backBtn);
    	bottomBackBtn = (Button) findViewById(R.id.bottomBackBtn);
    	zixunBtn = (Button) findViewById(R.id.zixunBtn);
    	shareBtn = (Button) findViewById(R.id.ShareBtn);
    	tuijianTv = (TextView) findViewById(R.id.tuijianTv);
    	admireTv = (TextView) findViewById(R.id.admireTv);
    	nameTv = (TextView) findViewById(R.id.nameTv);
    	contentTv = (TextView) findViewById(R.id.jieshaoTv);
    	titleTv = (TextView) findViewById(R.id.title);
    	photoImg = (ImageView) findViewById(R.id.photo);
    	ratingBar = (RatingBar) findViewById(R.id.xing);
    	ratingBar.setIsIndicator(true);
    	backBtn.setOnClickListener(this);
    	bottomBackBtn.setOnClickListener(this);
    	zixunBtn.setOnClickListener(this);
    	shareBtn.setOnClickListener(this);
    	tuijianTv.setOnClickListener(this);
    	admireTv.setOnClickListener(this);
    	
    }
}
