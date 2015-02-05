package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.util.MessageBox;
import com.tencent.tauth.Tencent;

public class DetailIntegralActivity extends BaseActivity {
	private Button backBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
	private TextView title;// 标题和内容
	private Button submitBtn;// 申请兑换按钮
	private ImageView topImage;// 商品图片介绍
	private LinearLayout linear1, linear2;//
	private WebView content,content1;// 显示申请兑换 、确认兑换页面的信息
	private EditText address, phone;
	private TextView name;
	private Button affirmBtn;// 确认兑换按钮
	private int flag = 1;// 用来判断是在哪个页面 1代表在申请兑换页面 2代表在确认兑换界面 3代表在分享界面
	private String goodid = "";// 产品id 通过产品id能够查询产品的相关信息

	private String sdpath = "";// 商品大图
	private String price = "";// 价格
	private String goodsname = "";// 商品名称
	private String description = "";// 商品描述
	private Bitmap bitmap = null;// 商品展示
	private String reponse = "";// 从服务器获取响应值
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private File myfile = null;
	
	private Button shareBtn1, shareBtn2, shareBtn3, shareBtn4, cancleBtn;// 分享功能的对应四个按钮
	public boolean initWedget_tag = true;
	private boolean flag1 = true;
	private RelativeLayout parent = null;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	public Map<String,Object> map = null;
	String firstPhotoUrl = "";
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailintegral);
		findView();
		findBottomBtn();
		initData();
		askForExchange();
	}

	private void initData(){
		map = (Map<String, Object>) getIntent().getSerializableExtra("integralMap");
		goodid = map.get("id").toString();
		description = map.get("description").toString();
		goodsname = map.get("name").toString();
		price = map.get("price").toString();
		title.setText(goodsname);
		content.loadDataWithBaseURL("", description, "text/html", "utf-8", null);
		sdpath = map.get("imageid").toString();
		int width = MyApplication.getInstant().getWidthPixels();
		int height = width/2;
		firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.SAVE_PATH_IN_SDCARD+sdpath+".png";
		if(sdpath==null || sdpath.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
			topImage.setImageResource(R.drawable.photolist_defimg);
		}else{//如果id存在
			if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
				topImage.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
			}else{//如果不存在 则去服务器下载
				topImage.setImageResource(R.drawable.photolist_defimg);
				RequestServerFromHttp.downImage(this,topImage,sdpath,ImageType.integral.getValue(),ImgDownType.BigBitmap.getValue(),
						 width+"",height+"",false,ConstantsValues.SAVE_PATH_IN_SDCARD,R.drawable.photolist_defimg);
			}
		}
		
	}
	private void findView() {
		title = (TextView) findViewById(R.id.title);
		content = (WebView) findViewById(R.id.content);
		submitBtn = (Button) findViewById(R.id.exchangeBtn);
		topImage = (ImageView) findViewById(R.id.topImage);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		content1 = (WebView) findViewById(R.id.content1);
		name = (TextView) findViewById(R.id.name);
		name.setText(UserBeanInfo.getInstant().getUserName());
		name.setText(UserBeanInfo.getInstant().getUserName());
		address = (EditText) findViewById(R.id.address);
		phone = (EditText) findViewById(R.id.phone);
		affirmBtn = (Button) findViewById(R.id.affirmBtn);
		submitBtn.setOnClickListener(this);
		affirmBtn.setOnClickListener(this);
		content1.setBackgroundColor(0);
		content.setBackgroundColor(0);
		parent = (RelativeLayout) findViewById(R.id.parent);
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
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01_on);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05);
	}

	@Override
	public void onClick(View v) {
		if (v == tab1) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab2) {
			Intent i = new Intent(this, RecommendFriendActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab3) {
			Intent i = new Intent(this, MyIntegralActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab4) {
			Intent i = new Intent(this, IntegralRuleActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab5) {
			Intent i = new Intent(this, EcshopActiveActivity.class);
			startActivity(i);
			finish();
		} else if (v == backBtn) {
			Intent i = new Intent(this, IntegralExchangeActivity.class);
			startActivity(i);
			finish();
		} else if (v == submitBtn) {//申请兑换
			if (flag == 1) {
				submitBtn.setText("立即分享");
				linear1.setVisibility(View.GONE);
				linear2.setVisibility(View.VISIBLE);
				affirmExchange();
			} else if (flag == 3) {
				submitBtn.setTag("申请兑换");
				if (flag1) {
					// 显示PopupWindow窗口
					flag1 = false;
					showShareDialog(parent);
				} else {
					flag1 = true;
					dismiss();
				}
			}
		} else if (v == affirmBtn){// 确认兑换按钮
			if (textValidate()) {
				new Thread(exchangeRunnable).start();
			}
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}else if(v.getId() == R.id.sharelayout1){//微博分享
    		Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", firstPhotoUrl);
			intent.putExtra("msgStr", description);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(description, firstPhotoUrl);
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, "要结婚啦！");
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, description);
//            if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
            	params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, firstPhotoUrl);
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
			emailShare.startSendToEmailIntent(description, firstPhotoUrl);
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
					wechatShare.shareMSG("我们结婚啦",description, R.drawable.logo, firstPhotoUrl, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG("我们结婚啦，快下载我们精心制作的app分享我们的幸福吧！", description, R.drawable.logo, firstPhotoUrl, 2);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
			
		}else if(v.getId() == R.id.cancle){
			wechatDialog.dismiss();
		}
    	super.onClick(v);
    }
    
    /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, DetailIntegralActivity.this);
        
        final Activity activity = DetailIntegralActivity.this;
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

	@Override
	protected void onDestroy() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
	
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		while (initWedget_tag) {
//			initPopuWindow();
			initWedget_tag = false;
		}

		super.onWindowFocusChanged(hasFocus);
	}
	/**
	 * 申请兑换页面数据
	 */
	private void askForExchange() {
//		topImage.setImageBitmap(bitmap);
//		topImage.setBackgroundDrawable(new BitmapDrawable(bitmap));
		if(goodsname.length()>10){
			title.setText(goodsname.substring(0, 10)+"...");
		}else{
			title.setText(goodsname);			
		}
		content.getSettings().setDefaultTextEncodingName("utf-8");
		content.loadDataWithBaseURL("", description,
				"text/html", "utf-8", "");
	}

	/**
	 * 确认兑换页面数据
	 */
	private void affirmExchange() {
		flag = 2;
		if(goodsname.length()>10){
			title.setText(goodsname.substring(0, 10)+"...");
		}else{
			title.setText(goodsname);			
		}
		content1.getSettings().setDefaultTextEncodingName("utf-8");
		content1.loadDataWithBaseURL(
				"",
				"\t您选择的商品为：<span style='color:red;font-size:16px;'>"
						+ goodsname
						+ "</span>一台，"
						+ "本商品需"
						+ price
						+ "积分兑换，自您确认兑换日起，我司将于五个工作日内有专业客服与您取得联系。请详细填写以下信息，确保能准确的联系您。",
				"text/html", "utf-8", "");
	}

	/**
	 * 分享兑换页面数据
	 */
	private void shareExchange() {
		flag = 3;
		submitBtn.setText("立即分享");
		title.setText("分享");
//		topImage.setImageBitmap(bitmap);
//		topImage.setBackgroundDrawable(new BitmapDrawable(bitmap));
		content.getSettings().setDefaultTextEncodingName("utf-8");
		content.loadDataWithBaseURL("","亲们，这是我在菠萝菠萝蜜里用积分兑换到的" + goodsname
				+ "。菠萝菠萝蜜里功能齐全，不仅能兑换礼品还能第一时间得到"
				+ "最新最优惠的婚纱服务！还等什么？快来和我一起菠萝菠萝蜜吧！","text/html", "utf-8", "");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, IntegralExchangeActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * @author 刘星星
	 * @createDate 2013、9、17
	 * 确认兑换。
	 */
	public Runnable exchangeRunnable = new Runnable() {

		@Override
		public void run() {
			String msgStr = RequestServerFromHttp.exchangeGoodsData(goodid, phone.getText().toString().trim(), address.getText().toString().trim());//访问服务器 提交兑换申请
			if(msgStr.startsWith("0")){//积分兑换申请提交成功
				handler.sendEmptyMessage(0);
			}else if(msgStr.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(1);
			}else if(msgStr.startsWith("-402")){//积分不足
				handler.sendEmptyMessage(2);
			}else if(msgStr.startsWith("-406")){//兑换失败
				handler.sendEmptyMessage(3);
			}
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == 0){//积分兑换申请提交成功
				linear2.setVisibility(View.GONE);
				linear1.setVisibility(View.VISIBLE);
				shareExchange();
				handler.sendEmptyMessage(6);
			}else if(msg.what == 1){//访问服务器失败
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"连接服务器失败！",
						DetailIntegralActivity.this);
			}else if(msg.what == 2){//积分不足
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"您的积分不足，请先获取积分！",
						DetailIntegralActivity.this);
			}else if(msg.what == 3){//兑换失败
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						"兑换失败！",
						DetailIntegralActivity.this);
			}
		};
	};

	/**
	 * author by Ring 登录前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (name.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.name_null),
					DetailIntegralActivity.this);
			return false;
		} else if (address.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.address_null),
					DetailIntegralActivity.this);
			return false;
		} else if (phone.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.phone_null),
					DetailIntegralActivity.this);
			return false;
		}
		return true;
	}
}
