/**
 * 
 */
package com.qingfengweb.weddingideas.activity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.adapter.MarryLogAdapter;
import com.qingfengweb.weddingideas.adapter.PhotoListAdapter;
import com.qingfengweb.weddingideas.adapter.RightMenuAdapter;
import com.qingfengweb.weddingideas.adapter.ViewPagerAdapter;
import com.qingfengweb.weddingideas.beans.UserBean;
import com.qingfengweb.weddingideas.beans.WeddingLogBean;
import com.qingfengweb.weddingideas.customview.CustomTextView;
import com.qingfengweb.weddingideas.customview.CustomViewPager;
import com.qingfengweb.weddingideas.customview.MyListView;
import com.qingfengweb.weddingideas.customview.MyListView.MyListViewFling;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.JsonData;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;
import com.qingfengweb.weddingideas.devicetools.DeviceTools;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;
import com.qingfengweb.weddingideas.share.ConstantsValuesShare;
import com.qingfengweb.weddingideas.share.EmailShare;
import com.qingfengweb.weddingideas.share.SMSShare;
import com.qingfengweb.weddingideas.share.WechatShare;
import com.qingfengweb.weddingideas.utils.MessageBox;
import com.tencent.tauth.Tencent;
/**
 * @author 刘星星
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainVideoActivity extends BaseActivity implements OnTouchListener,
GestureDetector.OnGestureListener, OnItemClickListener, MyListViewFling {
	ImageView playBtn,shareBtn,blessingBtn,menuBtn,openMenuBtn;
	boolean isClicked = false;
	RelativeLayout parent;
//	ScrollView scrollView;
	boolean isIntent = false;
	public int sdkVersion = 11;//手机系统SDK版本号
	
	private ArrayList<View> pageViews;  
	public  CustomViewPager viewPager;
	
	private String title[] = { "掌上商铺", "亲友特惠", "关于影楼" };
	private RelativeLayout layout_left;
	private LinearLayout layout_right;
	private MyListView lv_set;
	private boolean hasMeasured = false;// 是否Measured.
	private int window_width;// 屏幕的宽度
	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 30;

	private final static int sleep_time = 5;
	private String TAG = "jj";
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private GestureDetector mGestureDetector;// 手势
	private View view = null;// 点击的view
	LinearLayout layout1,layout2,layout3;//祝福页中的控件
	RelativeLayout zhufuLayout;//祝福页
	LinearLayout fuyanLayout;//赴宴
	VideoView videoView;
	boolean isPause = false;//视频播放是否暂停
	ImageView videoPlayImg,videoPlayBtn;
	TextView yaoqingInfo,zhuren,keren;
	Button fullBtn;
	DBHelper dbHelper;
	public String currentUsername = "";
	public static String typeUser = "0";
	LinearLayout photoListView = null;
	EditText zhufuEt,fuyanNameEt,fuyanNumberEt;
	Button zhufuBtn,fuyanSendBtn,zhufuCancleBtn,fuyanCancleBtn;
//	List<Map<String,Object>> photoList = new ArrayList<Map<String,Object>>();
	String invite_msg = "";//邀请文字
	public static String shareText = "";//分享的文字
	public static String username = "";//新郎新娘名字
	public static String shareUrl = "http://www.weddingideas.cn/a.html";
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	public static MediaPlayer mp = null;
	private int mExtarFlag = 0x00;
	TextView photoMsgTv ;//照片封面的文字
	Button copyBtn = null;//复制文本的按钮
	Button weddingAddressCopyBtn =null;
	View blessingPage = null;//送祝福页
	LinearLayout blessingLayoutTop,blessingLayoutCenter;
	String storeid = "";//影楼id
	String isHaveVideoValue = "";//配置文件中的值
	boolean isHaveVideo = false;//是否有视频封面
	ImageView photoIv = null;
	boolean isReturnFromMenu = false;//是否从更多菜单中回来
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		initView();
		sdkVersion = DeviceTools.getAndroidSDKVersion();
		initData();
		initRigntmenuData();
		username = LoadingActivity.configList.get(0).get("username");
		username = username.replace("与", "&");
		shareUrl = LoadingActivity.configList.get(0).get("app_url");
		shareText=LoadingActivity.configList.get(0).get("share_content");
		shareText = shareText.replace("与", "&");
		
		System.out.println("最开始的值=================="+username);
	}
	

	private void notifyPager(){
		viewPager.setAdapter(new ViewPagerAdapter(pageViews));  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());  
	}
	private void initData(){
		storeid = LoadingActivity.configList.get(0).get("storeid");
		mp =MediaPlayer.create(this, R.raw.music_weddingideas);
		mp.setLooping(true);
		try{
			tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, MainVideoActivity.this);
		}catch(NoClassDefFoundError e){
			Toast.makeText(this, "手机系统版本过低，可能不能使用分享功能！", 3000).show();
		}
		
		dbHelper = DBHelper.getInstance(this);
		String sql = "select *from "+UserBean.tbName+" where islogin='1'";
		List<Map<String,Object>> userList  = dbHelper.selectRow(sql, null);
		if(userList!=null && userList.size()>0){
			if(userList.get(0).get("name")!=null){
				currentUsername = userList.get(0).get("name").toString();
			}
			typeUser = userList.get(0).get("type").toString();
		}
		pageViews = new ArrayList<View>();  
		isHaveVideoValue = LoadingActivity.configList.get(0).get("photo_album_is_have");
		if(isHaveVideoValue.equals("1")){
			ConstantsValuesShare.WECHAT_CONSUMER_ID = "wxe89d2dffc6cb6669";
			pageViews.add(getMainVideoView());
			isHaveVideo = true;
		}else{
			ConstantsValuesShare.WECHAT_CONSUMER_ID = "wx12ec4a988e4e72cc";
			isHaveVideo = false;
		}
		pageViews.add(getMainPhotoView());
		pageViews.add(getPhotoListView());
		if(isHaveVideoValue.equals("1")){
			pageViews.add(getWeddingInfoView());
		}else{
			backBtn1.setVisibility(View.GONE);
		}
		pageViews.add(getBlessingView());
		if(MainVideoActivity.typeUser.equals("1")){
			pageViews.add(getMarryLog());
		}
		
		notifyPager();
		
	}
	RelativeLayout playLayout;
	/**
	 * 初始化视频封面页
	 * @return
	 */
	public View getMainVideoView(){
		View v = LayoutInflater.from(this).inflate(R.layout.a_mainvideo, null);
		playLayout = (RelativeLayout) v.findViewById(R.id.playLayout);
		RelativeLayout videoViewlayout  = (RelativeLayout) v.findViewById(R.id.videoViewlayout);
		int height = 150;
		int w = MyApplication.getInstance().getScreenW();
		if(w == 320){
			height = 150;
		}else if(w == 480){
			height = 300;
		}else if(w == 540){
			height = 350;
		}else if(w == 640){
			height = 350;
		}else if(w == 720){
			height = 450;
		}else if(w == 800){
			height = 450;
		}else if(w == 1080){
			height = 600;
		}else{
			height = 150;
		}
		LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
		videoViewlayout.setLayoutParams(param0);
		
		RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
		playLayout.setLayoutParams(param1);
		
		
		
		String bgHave = LoadingActivity.templateList.get(0).get("video_bg_is_have");
		if(bgHave.equals("0")){
			v.findViewById(R.id.topLayout).getBackground().setAlpha(0);
			v.findViewById(R.id.bottomLayout).getBackground().setAlpha(0);
		}else{
			v.findViewById(R.id.topLayout).getBackground().setAlpha(180);
			v.findViewById(R.id.bottomLayout).getBackground().setAlpha(180);
		}
		TextView titleTv = (TextView) v.findViewById(R.id.topView);
		titleTv.setText(LoadingActivity.configList.get(0).get("store_name"));
//		scrollView = (ScrollView) v.findViewById(R.id.scrollView);
//		scrollView.setOnTouchListener(this);
		videoPlayBtn = (ImageView) v.findViewById(R.id.playVideoBtn);
	    videoPlayImg = (ImageView) v.findViewById(R.id.playVideoImg);
		videoView = (VideoView) v.findViewById(R.id.videoView);
		yaoqingInfo = (TextView) v.findViewById(R.id.yaoqingInfo);
		zhuren = (TextView) v.findViewById(R.id.zhuren);
		keren = (TextView) v.findViewById(R.id.kerenName);
		fullBtn = (Button) v.findViewById(R.id.fullBtn);
		username = LoadingActivity.configList.get(0).get("username");
		username = username.replace("与", "&");
		System.out.println("这次出来了吧。。。。。。。。。。。。。。"+username);
		zhuren.setText(username);
		if(typeUser.equals("0")){//客人
			keren.setText("亲爱的"+currentUsername);
		}else {//主人
			keren.setText("亲爱的"+username);
		}
		int width = MyApplication.getInstance().getScreenW();
		//根据屏幕调整文字大小
		yaoqingInfo.setLineSpacing(0f, 1.5f);
		yaoqingInfo.setTextSize(Integer.parseInt(getString(R.string.main_info_text_size))*(float)width/320f);                 
		invite_msg = LoadingActivity.configList.get(0).get("invite_msg");
		invite_msg = invite_msg.replace("与", "&");
        //设置TextView
        yaoqingInfo.setText(invite_msg);    
//        handler.sendEmptyMessageDelayed(2, 1000);
//		yaoqingInfo.setText("伴随着春天脚步的临近，"+username+"即将走入婚姻的殿堂。幸福即将启航，我们在此诚挚的邀请您共同见证！");
		fullBtn.setOnClickListener(this);
		videoView.setOnTouchListener(this);
		videoPlayBtn.setOnClickListener(this);
		handler.sendEmptyMessageDelayed(8, 300);
		return v;
	}
	
	boolean isFirstPlay = true;//是否为第一次播放
	/**
	 * 视频播放
	 */
	public void playVideo(){
		
		//if your file is named sherif.mp4 and placed in /raw
		//use R.raw.sherif
		
//		MediaController	mediaController = new MediaController(this);  
//		videoView.setMediaController(mediaController);  
//        // 设置MediaController与VideView建立关联  
//        mediaController.setMediaPlayer(videoView);  
		Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.video_weddingideas); //do not add any extension
		videoView.setVideoURI(video);
		videoView.requestFocus();  
        videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				if(isFirstPlay){
					videoPlay();
				}
				
			}
		});
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
//				videoView.stopPlayback();
//				videoView.clearAnimation();
//				videoView.destroyDrawingCache();
				fullBtn.setVisibility(View.GONE);
				System.gc();
				videoPlayBtn.setVisibility(View.VISIBLE);
				videoPlayImg.setVisibility(View.VISIBLE);
			}
		});
		
	}
	ImageView photoMainImg = null;
	ImageButton backBtn1 = null;
	/**
	 * 初始化相册封面页
	 * @return
	 */
	public View getMainPhotoView(){
		View v = LayoutInflater.from(this).inflate(R.layout.a_mainphoto, null);
		v.setTag("mainphoto");
		backBtn1 = (ImageButton) v.findViewById(R.id.backBtn1);
		backBtn1.setOnClickListener(this);
		photoMsgTv = (TextView) v.findViewById(R.id.msgTv);
		 photoMsgTv.setOnClickListener(this);
		 photoMsgTv.setText(LoadingActivity.configList.get(0).get("wedding_album_msg"));
		 copyBtn = (Button) v.findViewById(R.id.copyBtn);
		 copyBtn.setOnClickListener(this);
		 copyBtn.getBackground().setAlpha(200);
		 copyBtn.setVisibility(View.GONE);
		 photoMainImg = (ImageView) v.findViewById(R.id.imgView);
		 TextView titleTv = (TextView) v.findViewById(R.id.photomain_titleTv);
		 titleTv.setText(LoadingActivity.configList.get(0).get("wedding_album_title"));
//		 photoMainImg.setImageBitmap((Bitmap) photoList.get(0).get("bitmap"));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(MyApplication.getInstance().getScreenW()*0.6), 
					(int)(MyApplication.getInstance().getScreenW()*0.8));
			photoMainImg.setLayoutParams(params);
		return v;
	}
	/**
	 * 初始化相册列表
	 * @return
	 */
	public View getPhotoListView(){
		View v = LayoutInflater.from(this).inflate(R.layout.a_photolist, null);
		v.findViewById(R.id.photoListBackBtn).setOnClickListener(this);
		photoListView = (LinearLayout) v.findViewById(R.id.imgLayout);
//		photoListView.setOnItemClickListener(this);
//		photoListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		v.setBackgroundColor(Color.parseColor(LoadingActivity.templateList.get(0).get("photo_list_bg_color")));
//		photoListView.setDivider(new ColorDrawable(Color.parseColor(LoadingActivity.templateList.get(0).get("photo_list_bg_color"))));
//		photoListView.setDividerHeight(5);
		new Thread(photoListRunnable).start();
		return v;
	}
	Runnable photoListRunnable = new Runnable() {
		@Override
		public void run() {
			photoListInfo.clear();
			getPhotoListData();
			
		}
	};
	
	@Override
	protected void onDestroy() {
		for(int i=0;i<photoListView.getChildCount();i++){
			if (photoListView.getChildAt(i) != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)photoListView.getChildAt(i)).getDrawable();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
		}
		photoListView.removeAllViews();
		photoListView.destroyDrawingCache();
		pageViews.clear();
//		photoList.clear();
		viewPager.destroyDrawingCache();
		
		if(isHaveVideo){
			videoView.stopPlayback();
			videoView.destroyDrawingCache();
			videoView.clearAnimation();
		}
		if(mp!=null){
			mp.release();
		}
		System.gc();
		super.onDestroy();
	}
	public Bitmap bitmap = null;
	private void getPhotoListData(){
		String path = "photos";
        try {
            String str[] = this.getAssets().list(path);
            if (str.length > 0) {//如果是目录
                for (String string : str) {
                	Message msg = new Message();
                	msg.what =1;
                	msg.obj = path + "/" + string;
                	handler.sendMessage(msg);
                }
            } else {//如果是文件
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	ListView marryListView;
	List<Map<String,Object>> marryList;
	MarryLogAdapter marryAdapter = null;
	LinearLayout parentlayout;
	public View getMarryLog(){
		View v = LayoutInflater.from(this).inflate(R.layout.a_marrylog, null);
		marryListView = (ListView) v.findViewById(R.id.listView);
		marryListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		listView.setOnTouchListener(this);
		parentlayout = (LinearLayout) v.findViewById(R.id.parent);
		parentlayout.setBackgroundColor(Color.parseColor(LoadingActivity.templateList.get(0).get("log_bgcolor")));
		v.findViewById(R.id.backBtnMarry).setOnClickListener(this);
		marryList = new ArrayList<Map<String,Object>>();
		
		return v;
	}
	
	/**
	 * 
	 */
	Runnable getLogRunnable = new Runnable() {
		
		@Override
		public void run() {
			String storeid = LoadingActivity.configList.get(0).get("storeid");
			String madeid = LoadingActivity.configList.get(0).get("madeid");
			String sql = "select * from "+WeddingLogBean.tbName+" where userId='"+madeid+"' order by stimeString desc";
			marryList = dbHelper.selectRow(sql, null);
			if(marryList!=null && marryList.size()>0){
				handler.sendEmptyMessage(11);
			}
			String msgStr = RequestServerFromHttp.getweddingLog(storeid, "");
			if(msgStr.startsWith("[")){
				JsonData.jsonWeddingLogData(msgStr, dbHelper.open());
				marryList = dbHelper.selectRow(sql, null);
				if(marryList!=null && marryList.size()>0){
					handler.sendEmptyMessage(11);
				}
			}
		}
	};
	private void notifyMarryAdapter(){
		marryAdapter = new MarryLogAdapter(this, marryList);
		marryListView.setAdapter(marryAdapter);
	}
	public static List<Map<String,Object>> photoListInfo = new ArrayList<Map<String,Object>>();;
	private ImageView getImgView(String string){
		bitmap = null;
		ImageView imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		try {
			InputStream is = this.getAssets().open(string);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//			BitmapFactory.decodeStream(is, null, opts);
			opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,800 * 800);
			opts.inJustDecodeBounds = false;
			// 如果图片还未回收，先强制回收该图片
			bitmap = BitmapFactory.decodeStream(is, null, opts);
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				int newWidth = MyApplication.getInstance().getScreenW() - 20;
				// 计算缩放比例
				float scaleWidth = ((float) newWidth) / width;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleWidth);
				// 得到新的图片
				bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
			}
			if(bitmap!=null){
				imageView.setImageBitmap(bitmap);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, bitmap.getHeight());
				param.setMargins(0, photoListView.getChildCount() == 0?10:0, 0, 10);
				imageView.setLayoutParams(param);
				imageView.setTag(photoListView.getChildCount());
				imageView.setOnClickListener(new ListItemOnClickListener());
				
				Map<String,Object> map = new HashMap<String, Object>();
          		map.put("imageid", "");
          		map.put("bitmap", bitmap);
          		photoListInfo.add(map);
			}else{
				return null;
			}
			bitmap = null;
			is.close();
			System.gc();
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return imageView;
	}
	/**
	 * 相册列表的照片点击事件
	 * @author qingfeng
	 *
	 */
	class ListItemOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainVideoActivity.this,ImageSwitcher.class);
//			intent.putExtra("photoList", (Serializable)photoList.);
			intent.putExtra("index", (Integer)(v.getTag()));
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		
	}
	private void notifyPhotoListView(List<Map<String,Object>> list,ListView listView){
		PhotoListAdapter adapter = new PhotoListAdapter(this, list);
		listView.setAdapter(adapter);
	}
	ImageView infoImg1,infoImg2,infoImg3;
	/**
	 * 初始化婚宴信息页
	 * @return
	 */
	public View getWeddingInfoView(){
		View v = LayoutInflater.from(this).inflate(R.layout.a_info_seats, null);
		v.findViewById(R.id.backBtn2).setOnClickListener(this);
		TextView addressTv = (TextView) v.findViewById(R.id.addressTv);
		TextView reachTv = (TextView)(v.findViewById(R.id.reachTv));
		TextView jiudian = ((TextView)(v.findViewById(R.id.jiudian)));
		TextView dateTv = ((TextView)(v.findViewById(R.id.dateTv)));
		TextView text01 = (TextView)(v.findViewById(R.id.text01));
		TextView text02 = ((TextView)(v.findViewById(R.id.text02)));
		TextView text03 = ((TextView)(v.findViewById(R.id.text03)));
		infoImg1 = (ImageView) v.findViewById(R.id.infoImg1);
		infoImg2 = (ImageView) v.findViewById(R.id.infoImg2);
		infoImg3 = (ImageView) v.findViewById(R.id.infoImg3);
		int height = 30;
		int totop = 16;
		int textsize = 16;
		int w = MyApplication.getInstance().getScreenW();
		if(w == 320){
			height = 30;
			totop = 10;
			textsize = 14;
		}else if(w == 480){
			height = 60;
			totop = 15;
			textsize = 16;
		}else if(w == 540){
			height = 100;
			totop = 20;
			textsize = 16;
		}else if(w == 640){
			height = 60;
			totop = 10;
			textsize = 16;
		}else if(w == 720){
			height = 140;
			totop = 20;
			textsize = 20;
		}else if(w == 800){
			height = 140;
			totop = 20;
			textsize = 20;
		}else if(w == 1080){
			height = 155;
			totop = 30;
			textsize = 22;
		}else{
			height = 90;
			totop = 15;
			textsize = 16;
		}
		
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
		infoImg1.setLayoutParams(param1);
		
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
		param2.setMargins(0, totop, 0, 0);
		infoImg2.setLayoutParams(param2);
		
		RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
		param3.setMargins(0, totop, 0, 0);
		infoImg3.setLayoutParams(param3);
		addressTv.setTextSize(textsize);
		reachTv.setTextSize(textsize);
		jiudian.setTextSize(textsize);
		dateTv.setTextSize(textsize);
		text01.setTextSize(textsize);
		text02.setTextSize(textsize);
		text03.setTextSize(textsize);
		reachTv.setText(LoadingActivity.configList.get(0).get("traffic_route"));
		jiudian.setText(LoadingActivity.configList.get(0).get("wedding_seats"));
		dateTv.setText(LoadingActivity.configList.get(0).get("wedding_time"));
		
		addressTv.setText(LoadingActivity.configList.get(0).get("wedding_address"));
		v.findViewById(R.id.mapBtn).setOnClickListener(this);
		weddingAddressCopyBtn = (Button) v.findViewById(R.id.copyBtn);
		weddingAddressCopyBtn.setOnClickListener(this);
		addressTv.setOnClickListener(this);
		return v;
	}
	/**
	 * 初始化祝福页控件
	 * @return
	 */
	public View getBlessingView(){
		blessingPage = LayoutInflater.from(this).inflate(R.layout.a_blessing, null);
		TextView title = (TextView) blessingPage.findViewById(R.id.titletext1);
		title.setTextColor(Color.parseColor(LoadingActivity.templateList.get(0).get("blessing_title_text_color")));
		title = (TextView) blessingPage.findViewById(R.id.titletext2);
		title.setTextColor(Color.parseColor(LoadingActivity.templateList.get(0).get("blessing_title_text_color")));
		title = (TextView) blessingPage.findViewById(R.id.titletext3);
		title.setTextColor(Color.parseColor(LoadingActivity.templateList.get(0).get("blessing_title_text_color")));
		blessingPage.findViewById(R.id.backBtn3).setOnClickListener(this);
		layout1 = (LinearLayout) blessingPage.findViewById(R.id.layout1);
		layout2 = (LinearLayout) blessingPage.findViewById(R.id.layout2);
		layout3 = (LinearLayout) blessingPage.findViewById(R.id.layout3);
		blessingLayoutTop = (LinearLayout) blessingPage.findViewById(R.id.layoutTop);
		blessingLayoutCenter = (LinearLayout) blessingPage.findViewById(R.id.layoutCenter);
		zhufuLayout = (RelativeLayout) blessingPage.findViewById(R.id.zhufuLayout);
		fuyanLayout = (LinearLayout) blessingPage.findViewById(R.id.fuyanLayout);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
		zhufuEt = (EditText) blessingPage.findViewById(R.id.zhufuEt);
		zhufuBtn = (Button) blessingPage.findViewById(R.id.zhuFuSendBtn);
		fuyanNameEt = (EditText) blessingPage.findViewById(R.id.nameEt);
		fuyanNumberEt = (EditText) blessingPage.findViewById(R.id.peopleNumberEt);
		fuyanNumberEt.addTextChangedListener(watcher);
		fuyanSendBtn = (Button) blessingPage.findViewById(R.id.sendBtn);
		zhufuCancleBtn = (Button) blessingPage.findViewById(R.id.zhuFuCancleBtn);
		zhufuCancleBtn.setOnClickListener(this);
		fuyanCancleBtn = (Button) blessingPage.findViewById(R.id.fuyanCancleBtn);
		fuyanCancleBtn.setOnClickListener(this);
		zhufuBtn.setOnClickListener(this);
		fuyanSendBtn.setOnClickListener(this);
		fuyanNameEt.setClickable(false);
		fuyanNumberEt.setClickable(false);
		fuyanNameEt.setEnabled(false);
		fuyanNumberEt.setEnabled(false);
//		blessingPage.setOnClickListener(this);
		ImageView szImg3 = (ImageView) blessingPage.findViewById(R.id.szImg3);
		if(!isHaveVideo){
			blessingLayoutCenter.setVisibility(View.GONE);
			szImg3.setImageResource(R.drawable.icon_two);
		}else{
			blessingLayoutCenter.setVisibility(View.VISIBLE);
			szImg3.setImageResource(R.drawable.icon_three);
		}
		return blessingPage;
	}
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(fuyanNumberEt.getText().toString().length()>0 && Integer.parseInt(fuyanNumberEt.getText().toString())>100){
				fuyanNumberEt.setText("100");
			}
			
		}
	};
	/**
	 * 初始化更多菜单的数据
	 */
	private void initRigntmenuData(){
		List<Map<String,Object>> rightList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", "掌上商铺");
		map.put("ico", R.drawable.more_shangpu_ico);
		rightList.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "亲友特惠");
		map.put("ico", R.drawable.more_tehui_ico);
		rightList.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "关于影楼");
		map.put("ico", R.drawable.more_guanyu_ico);
		rightList.add(map);
		lv_set.setAdapter(new RightMenuAdapter(this, rightList));
	}
	private void initView(){
		viewPager = (CustomViewPager) findViewById(R.id.viewPager);
		playBtn = (ImageView) findViewById(R.id.playBtn);
		shareBtn = (ImageView) findViewById(R.id.shareBtn);
		blessingBtn = (ImageView) findViewById(R.id.blessingBtn);
		menuBtn = (ImageView) findViewById(R.id.menuBtn);
		openMenuBtn = (ImageView) findViewById(R.id.openMenuBtn);
		layout_left = (RelativeLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		lv_set = (MyListView) findViewById(R.id.lv_set);
		parent = (RelativeLayout) findViewById(R.id.parent);
		// listview item滑动监听
		lv_set.setMyListViewFling(this);
		lv_set.setOnItemClickListener(this);
		menuBtn.setOnClickListener(this);
		viewPager.setOnTouchListener(this);
		layout_right.setOnTouchListener(this);
//		layout_left.setOnTouchListener(this);
		playBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		blessingBtn.setOnClickListener(this);
//		menuBtn.setOnClickListener(this);
		openMenuBtn.setOnClickListener(this);
		mGestureDetector = new GestureDetector(this);
		// 禁用长按监听
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
		
	}
	/**
	 * 播放视频
	 */
	public void videoPlay(){
		isFirstPlay = false;
		fullBtn.setVisibility(View.VISIBLE);
		videoView.start();
		videoPlayBtn.setVisibility(View.GONE);
		videoPlayImg.setVisibility(View.GONE);
	}
	/***
	 * 获取移动距离 移动的距离其实就是layout_left的宽度
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_right.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = lv_set
							.getLayoutParams();
					// 注意： 设置layout_left的宽度。防止被在移动的时候控件被挤压
					layoutParams.width = window_width;
					layout_left.setLayoutParams(layoutParams);

					// 设置layout_right的初始位置.
					layoutParams_1.leftMargin = window_width;
					layout_right.setLayoutParams(layoutParams_1);
					// 注意：设置lv_set的宽度防止被在移动的时候控件被挤压
					layoutParams_2.width = MAX_WIDTH;
					lv_set.setLayoutParams(layoutParams_2);

					Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
							+ window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}
	
	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		layoutParams.leftMargin -= mScrollX;
		layoutParams_1.leftMargin = window_width + layoutParams.leftMargin;
		if (layoutParams.leftMargin >= 0) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.leftMargin = 0;
			layoutParams_1.leftMargin = window_width;

		} else if (layoutParams.leftMargin <= -MAX_WIDTH) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.leftMargin = -MAX_WIDTH;
			layoutParams_1.leftMargin = window_width - MAX_WIDTH;
		}
		Log.v(TAG, "layoutParams.leftMargin=" + layoutParams.leftMargin
				+ ",layoutParams_1.leftMargin =" + layoutParams_1.leftMargin);

		layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}
	int scrollW = 0;
	boolean isViewPagerScrolling = false;
	 // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        	viewPager.setScanScroll(true);
        	//这一段是为了辅助解决完成当有第6页  而且第6页 也就是最后一页为列表时往右边滑动时也弹出右边菜单的bug
        	//以上任何一个条件不满足改代码将失效
        	//这是一段曲线救国的代码，因为只要是最后一页是可以上下滚动的，那么在滑出右边菜单后的操作就有问题
        	//这里的4是指在第六页   手指往右滑动时 第5页慢慢的出现了   4代表第五页的下标值
        	if(pageViews.size()>4+1 && arg0==4){
        		isViewPagerScrolling = true;
        	}else{
        		isViewPagerScrolling = false;
        	}
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	copyBtn.setVisibility(View.GONE);
        	if(weddingAddressCopyBtn!=null){
        		weddingAddressCopyBtn.setVisibility(View.GONE);
        	}
        	DeviceTools.disShowSoftKey(MainVideoActivity.this, zhufuEt);
        	if(isClicked){
        		closeMenu();
        	}
        	if(arg0 == 0){
        		
        	}else if(arg0 == 1){
        		if(isHaveVideo){
        			videoPause();
        		}
        	}else if(arg0 == 2){
        		if(isHaveVideo){
        			videoPause();
        		}
        	}else if(arg0 == 3){
        		if(isHaveVideo){
        			videoPause();
        		}
        	}else if(arg0 == 4){
        		if(isHaveVideo){
        			videoPause();
        		}
        	}else if(arg0 == 5){
        		if(isHaveVideo){
        			videoPause();
        		}
        	}
        }  
    }  
    /**
     * 打开菜单   播放 分享  祝福  更多
     * playbtn  的直线方程为   y = 26x;
     *shareBtn 的直线方程为   y = (21/11)x
	 *	blessingBtn 的直线方程为  y = (2/3)x
	 *	menuBtn 的直线方程为  y = (1/21)x
     */
    private void openMenu(){
    	playBtn.clearAnimation();
		shareBtn.clearAnimation();
		blessingBtn.clearAnimation();
		menuBtn.clearAnimation();
		if(sdkVersion>=11){
			if(MyApplication.getInstance().getScreenW()>=1080){
				moveObject(playBtn,0,-18,0,-468);
				moveObject(shareBtn,0,-209,0,-399);
				moveObject(blessingBtn,0,-360,0,-240);
				moveObject(menuBtn,0,-399,0,-19);
			}else if(MyApplication.getInstance().getScreenW()<400){
				moveObject(playBtn,0,-5,0,-130);
				moveObject(shareBtn,0,-55,0,-105);
				moveObject(blessingBtn,0,-90,0,-60);
				moveObject(menuBtn,0,-105,0,-5);
			}else{
				moveObject(playBtn,0,-10,0,-260);
				moveObject(shareBtn,0,-110,0,-210);
				moveObject(blessingBtn,0,-180,0,-120);
				moveObject(menuBtn,0,-210,0,-10);
			}
		}else{
			if(MyApplication.getInstance().getScreenW()>=1080){
				moveBtn(playBtn,-18,-468);
				moveBtn(shareBtn,-209,-399);
				moveBtn(blessingBtn,-360,-240);
				moveBtn(menuBtn,-399,-19);
			}else if(MyApplication.getInstance().getScreenW()<400){
				moveBtn(playBtn,-5,-130);
				moveBtn(shareBtn,-55,-105);
				moveBtn(blessingBtn,-90,-60);
				moveBtn(menuBtn,-105,-5);
			}else{
				moveBtn(playBtn,-10,-260);
				moveBtn(shareBtn,-110,-210);
				moveBtn(blessingBtn,-180,-120);
				moveBtn(menuBtn,-210,-10);
			}
		}
		isClicked = true;
    }
    /**
     * 关闭菜单  播放 分享  祝福  更多
     */
    private void closeMenu(){
    	handler.sendEmptyMessageDelayed(6, 700);
    	playBtn.clearAnimation();
		shareBtn.clearAnimation();
		blessingBtn.clearAnimation();
		menuBtn.clearAnimation();
		if(sdkVersion>=11){
			handler.sendEmptyMessageDelayed(0, 300);
			if(MyApplication.getInstance().getScreenW()>=1080){
				moveObject(playBtn,-18,-19,-468,-494);
				moveObject(shareBtn,-209,-231,-399,-441);
				moveObject(blessingBtn,-360,-411,-240,-274);
				moveObject(menuBtn,-399,-441,-19,-21);
			}else{
				moveObject(playBtn,-10,-11,-260,-286);
				moveObject(shareBtn,-110,-132,-210,-252);
				moveObject(blessingBtn,-180,-231,-120,-154);
				moveObject(menuBtn,-210,-252,-10,-12);
			}
		}else{
			handler.sendEmptyMessageDelayed(0, 340);
			moveBtn(playBtn,-1,-26);
			moveBtn(shareBtn,-11,-21);
			moveBtn(blessingBtn,-18,-12);
			moveBtn(menuBtn,-21,-1);
		}
		isClicked = false;
    }
    /**
     * 设置菜单中按钮的显示和隐藏
     * @param state
     */
    private void setBtnVisibility(int state){
    	playBtn.setVisibility(state);
    	shareBtn.setVisibility(state);
    	blessingBtn.setVisibility(state);
    	menuBtn.setVisibility(state);
    }
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onClick(View v) {
		// TODO 控件监听
		if(v == openMenuBtn){//打开菜单按钮
			//playbtn  的直线方程为   y = 26x;
			//shareBtn 的直线方程为   y = (21/11)x
			//blessingBtn 的直线方程为  y = (2/3)x
			//menuBtn 的直线方程为  y = (1/21)x
			openMenuBtn.setClickable(false);
			if(!isClicked){
				handler.sendEmptyMessageDelayed(5, 400);
				setBtnVisibility(View.VISIBLE);
				openMenu();
			}else{
				handler.sendEmptyMessageDelayed(5, 700);
				closeMenu();
			}
//			handler.sendEmptyMessageDelayed(0, 500);
		}else if(v == playBtn){//播放按钮
//			closeMenu();
			if(isHaveVideo){
				videoPause();
			}
			Intent intent = new Intent(this,ImageSwitcher.class);
//			intent.putExtra("photoList", (Serializable)photoList.);
			intent.putExtra("index", 0);
			intent.putExtra("autoPlay", true);
			startActivity(intent);
		}else if(v == shareBtn){//分享按钮
//			closeMenu();
			showShareDialog(parent);
			
		}else if(v == blessingBtn){//祝福按钮
			viewPager.setCurrentItem(pageViews.size()-(MainVideoActivity.typeUser.equals("1")?2:1), true);
			if(zhufuLayout.getVisibility() == View.VISIBLE){
			
			}else{
				zhufuLayout.setVisibility(View.VISIBLE);
				TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,-layout2.getBottom(), 0);
				translateAnimation.setDuration(300);
				translateAnimation.setFillAfter(true);
				zhufuLayout.startAnimation(translateAnimation);
				setLayoutParams(10,60);
			}
			fuyanLayout.clearAnimation();
			fuyanLayout.setVisibility(View.GONE);
			handler.sendEmptyMessageDelayed(5, 700);
			closeMenu();
		}else if(v == menuBtn){//更多菜单
//			closeMenu();
			viewPager.setScanScroll(false);
			new AsynMove().execute(-SPEED);
		}else if(v.getId() == R.id.sharelayout1){//微博分享
			videoPause();
			Intent intent = new Intent(this,ShareActivity.class);
			  if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
				  intent.putExtra("filePath", ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png");
	            }
			intent.putExtra("msgStr", shareText);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			videoPause();
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(shareText, "");
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			videoPause();
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, LoadingActivity.configList.get(0).get("share_title"));
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, shareUrl);
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, shareText);
            if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
            	params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png");
            }
//        params.putString(shareType == Tencent.SHARE_TO_QQ_TYPE_IMAGE ? Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL 
//        		: Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, "weddingideas");
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, mExtarFlag);
       /* if (shareType == Tencent.SHARE_TO_QQ_TYPE_AUDIO) {
            params.putString(Tencent.SHARE_TO_QQ_AUDIO_URL, mEditTextAudioUrl.getText().toString());
        }*/
        doShareToQQ(params);
//			Intent intent = new Intent(this,ShareActivity.class);
//			intent.putExtra("filePath", "");
//			intent.putExtra("msgStr", shareText);
//			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.TENCENT_TYPE);
//			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			videoPause();
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(shareText, "");
		}else if(v.getId() == R.id.sharelayout5){//二维码分享
			videoPause();
			Intent intent = new Intent(this,ErweimaShareActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v.getId() == R.id.sharelayout6){//微信
			videoPause();
			showWechatShareDialog();
//			showMoreShareDialog();
//			Intent intent = new Intent(Intent.ACTION_SEND);  
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
////            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile((File) pathes.get(mIndex).get("file")));  //传输图片或者文件 采用流的方式  
//            intent.putExtra(Intent.EXTRA_TEXT, "分享分享微博");   //附带的说明信息  
//            intent.putExtra(Intent.EXTRA_SUBJECT, "标题");  
//            intent.setType("image/*");   //分享图片  
//            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.closeWindowBtn1){//关闭对话框按钮
			selectPopupWindow.dismiss();
		}else if(v.getId() == R.id.shareToGoodFriend){//分享到好友
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize(null, null)){
					wechatShare.shareMSG(LoadingActivity.configList.get(0).get("share_title"), shareText, R.drawable.img_share_photo, shareUrl, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG(LoadingActivity.configList.get(0).get("share_title"), shareText, R.drawable.img_share_photo, shareUrl, 2);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
			
		}else if(v.getId() == R.id.cancle){
			wechatDialog.dismiss();
		}else if(v == backBtn1){
			viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
		}else if(v.getId() == R.id.photoListBackBtn){
			viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
		}else if(v.getId() == R.id.backBtn2){
			viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
		}else if(v.getId() == R.id.backBtn3){
			viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
		}else if(v.getId() == R.id.backBtnMarry){
			viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
		}if(v == layout1){//送祝福
			if(zhufuLayout.getVisibility() == View.VISIBLE){
				handler.sendEmptyMessageDelayed(3, 300);
//				TranslateAnimation translateAnimation = new TranslateAnimation(0, MyApplication.getInstance().getScreenW(), 0,0);
//				translateAnimation.setDuration(300);
//				translateAnimation.setFillAfter(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
				alphaAnimation.setDuration(300);
				alphaAnimation.setFillAfter(true);
				zhufuLayout.startAnimation(alphaAnimation);
				zhufuBtn.setClickable(false);
				zhufuCancleBtn.setClickable(false);
				fuyanSendBtn.setClickable(false);
				fuyanCancleBtn.setClickable(false);
			}else{
				zhufuLayout.setVisibility(View.VISIBLE);
//				TranslateAnimation translateAnimation = new TranslateAnimation(-(MyApplication.getInstance().getScreenW()), 0,0, 0);
//				translateAnimation.setDuration(300);
//				translateAnimation.setFillAfter(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(300);
				alphaAnimation.setFillAfter(true);
				zhufuLayout.startAnimation(alphaAnimation);
				zhufuBtn.setClickable(true);
				zhufuCancleBtn.setClickable(true);
				fuyanSendBtn.setClickable(false);
				fuyanCancleBtn.setClickable(false);
				setLayoutParams(10,60);
			}
			fuyanLayout.clearAnimation();
			fuyanLayout.setVisibility(View.GONE);
		}else if(v == layout2){//我要赴宴
			if(fuyanLayout.getVisibility() == View.VISIBLE){
				handler.sendEmptyMessageDelayed(4, 300);
//				TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,(fuyanLayout.getHeight()*2));
//				translateAnimation.setDuration(300);
//				translateAnimation.setFillAfter(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
				alphaAnimation.setDuration(300);
				alphaAnimation.setFillAfter(true);
				fuyanLayout.startAnimation(alphaAnimation);
				fuyanNameEt.setClickable(false);
				fuyanNumberEt.setClickable(false);
				fuyanNameEt.setEnabled(false);
				fuyanNumberEt.setEnabled(false);
				zhufuBtn.setClickable(false);
				zhufuCancleBtn.setClickable(false);
				fuyanSendBtn.setClickable(false);
				fuyanCancleBtn.setClickable(false);
			}else{
				fuyanLayout.setVisibility(View.VISIBLE);
//				TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,layout3.getBottom(), 0);
//				translateAnimation.setDuration(300);
//				translateAnimation.setFillAfter(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(300);
				alphaAnimation.setFillAfter(true);
				fuyanLayout.startAnimation(alphaAnimation);
				fuyanNameEt.setClickable(true);
				fuyanNumberEt.setClickable(true);
				fuyanNameEt.setEnabled(true);
				fuyanNumberEt.setEnabled(true);
				zhufuBtn.setClickable(false);
				zhufuCancleBtn.setClickable(false);
				fuyanSendBtn.setClickable(true);
				fuyanCancleBtn.setClickable(true);
				setLayoutParams(60,10);
			}
			zhufuLayout.setVisibility(View.GONE);
			zhufuLayout.clearAnimation();
		}else if(v == layout3){//分享幸福
			fuyanLayout.setVisibility(View.GONE);
			zhufuLayout.setVisibility(View.GONE);
			zhufuBtn.setClickable(false);
			zhufuCancleBtn.setClickable(false);
			fuyanSendBtn.setClickable(false);
			fuyanCancleBtn.setClickable(false);
			zhufuLayout.clearAnimation();
			fuyanLayout.clearAnimation();
			setLayoutParams(60,60);
			showShareDialog(parent);
		}else if(v.getId() == R.id.playVideoImg){//播放视频
			Intent intent = new Intent(this,VideoPlayActivity.class);
			startActivity(intent);
		}else if(v == videoPlayBtn){//播放按钮
			videoPlay();
		}else if(v == fullBtn){//设置全屏播放按钮
			Intent intent = new Intent(this,VideoPlayActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_scale_enter, R.anim.anim_scale_exit);
			videoPause();
		}else if(v == zhufuBtn){//发送祝福
			DeviceTools.disShowSoftKey(this,zhufuEt);
			if (zhufuEt.getText().toString().trim().equals("")) {
				MessageBox.CreateAlertDialog("提示！","请先输入祝福语",this);
			}else{
				showProgressDialog("正在发送，请稍候...");
				new Thread(zhufuRunnable).start();
			}
		}else if(v == fuyanSendBtn){//发送赴宴
				DeviceTools.disShowSoftKey(this,zhufuEt);
			if(signValidate()){
				showProgressDialog("正在发送，请稍候...");
				new Thread(fuyanRunnable).start();
			}
		}else if(v.getId() == R.id.addressTv){//点击地址  可以复制
			if(weddingAddressCopyBtn.getVisibility() == View.VISIBLE){
				weddingAddressCopyBtn.setVisibility(View.GONE);
			}else{
				weddingAddressCopyBtn.setVisibility(View.VISIBLE);
			}
		}else if(v == weddingAddressCopyBtn){//复制文本控件
			weddingAddressCopyBtn.setVisibility(View.GONE);
			DeviceTools.copyText(this, LoadingActivity.configList.get(0).get("wedding_address"));
		}else if(v.getId() == R.id.mapBtn){//地图
			 String latitude ="31.1602382590";
		        String la = LoadingActivity.configList.get(0).get("wedding_hotel_latitude");
		        if(la!=null && !la.equals("") && !la.equals("null")){
		        	latitude = la;
		        }
		        
		        
		        String longitude ="121.4456978307";
		        String lg = LoadingActivity.configList.get(0).get("wedding_hotel_longitude");
		        if(lg!=null && !lg.equals("") && !lg.equals("null")){
		        	longitude = lg;
		        }
		        
			Intent intent = new Intent(this,BaiDuMapActivity.class);
			//31.2079500186,121.3846414905西郊宾馆经纬度
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.putExtra("title", "宴设地址");
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v == photoMsgTv){//相册封面文字控件
			if(copyBtn.getVisibility() == View.VISIBLE){
				copyBtn.setVisibility(View.GONE);
			}else{
				copyBtn.setVisibility(View.VISIBLE);
			}
		}else if(v == copyBtn){//复制文本控件
			copyBtn.setVisibility(View.GONE);
			DeviceTools.copyText(this, LoadingActivity.configList.get(0).get("wedding_album_msg"));
		}else if(v.getId() == R.id.zhuFuCancleBtn || v.getId() == R.id.fuyanCancleBtn){//送祝福页
			if(zhufuLayout.getVisibility() == View.VISIBLE){
				zhufuBtn.setClickable(false);
				zhufuCancleBtn.setClickable(false);
				handler.sendEmptyMessageDelayed(3, 300);
				TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,-(zhufuLayout.getHeight()*2));
				translateAnimation.setDuration(300);
				translateAnimation.setFillAfter(true);
				setLayoutParams(60,60);
				zhufuLayout.startAnimation(translateAnimation);
			}else if(fuyanLayout.getVisibility() == View.VISIBLE){
				fuyanSendBtn.setClickable(false);
				fuyanCancleBtn.setClickable(false);
				handler.sendEmptyMessageDelayed(4, 300);
				TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,(fuyanLayout.getHeight()*2));
				translateAnimation.setDuration(300);
				translateAnimation.setFillAfter(true);
				fuyanLayout.startAnimation(translateAnimation);
				setLayoutParams(60,60);
			}
		}
		super.onClick(v);
	}
	/**
	 * 我要赴宴的接口访问
	 */
	Runnable fuyanRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.sendFuYan(fuyanNameEt.getText().toString(), fuyanNumberEt.getText().toString());
			String status = JsonData.jsonSuccessData(msg);
			if(status.equals("0")){
				handler.sendEmptyMessage(9);
			}else{
				handler.sendEmptyMessage(10);
			}
		}
	};
	

		/**
		 * 发送祝福的接口访问
		 */
		Runnable zhufuRunnable = new Runnable() {
			
			@Override
			public void run() {
				String sql = "select *from "+UserBean.tbName+" where islogin='1'";
				List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
				if(list!=null && list.size()>0){
					String msg = RequestServerFromHttp.sendZhuFu(zhufuEt.getText().toString(),
							list.get(0).get("name")!=null?(list.get(0).get("name").toString()):username, list.get(0).get("username").toString());
					String status = JsonData.jsonSuccessData(msg);
					if(status.equals("0")){
						handler.sendEmptyMessage(9);
					}else{
						handler.sendEmptyMessage(10);
					}
				}
				
		}
	};
	/**
	 * 
	 * @param value1  我要赴宴布局相对于上面控件的距离
	 * @param value2  分享幸福相对于上面的控件的距离
	 */
	private void setLayoutParams(int value1,int value2){
		if(blessingLayoutCenter.getVisibility() == View.VISIBLE){
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params1.setMargins(0, value1, 0, 0);
			blessingLayoutCenter.setLayoutParams(params1);
		}
		
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, value2, 0, 0);
		layout3.setLayoutParams(params2);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("分享成功过了");
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onPause() {
		videoPause();
		if(isReturnFromMenu){
			for(int i=0;i<photoListView.getChildCount();i++){
				if (photoListView.getChildAt(i) != null) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)photoListView.getChildAt(i)).getDrawable();
					if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
						bitmapDrawable.getBitmap().recycle();
					}
				}
			}
			photoListView.removeAllViews();
			photoListView.destroyDrawingCache();
			for(int i = 0;i<photoListInfo.size();i++){
				if(((Bitmap) photoListInfo.get(i).get("bitmap"))!=null && !((Bitmap) photoListInfo.get(i).get("bitmap")).isRecycled()){
					((Bitmap) photoListInfo.get(i).get("bitmap")).recycle();
				}
			}
			photoListInfo.clear();
		}
		super.onPause();
	}
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, MainVideoActivity.this);
        
        final Activity activity = MainVideoActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                tencent.shareToQQ(activity, params, null/*new IUiListener() {

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                        Util.toastMessage(activity, "onComplete: " + response.toString());
                    }

                    @Override
                    public void onError(UiError e) {
                        Util.toastMessage(activity, "onComplete: " + e.errorMessage, "e");
                    }

                    @Override
                    public void onCancel() {
                    	if(shareType != Tencent.SHARE_TO_QQ_TYPE_IMAGE){
//                    		Util.toastMessage(activity, "onCancel: ");
                    	}
                    }

                }*/);
            }
        }).start();
    }
	/**
	 * 视频暂停播放
	 */
	private void videoPause(){
		if(isHaveVideo){
			videoView.pause();
			videoPlayBtn.setVisibility(View.VISIBLE);
			videoPlayImg.setVisibility(View.VISIBLE);
			isPause = true;
		}
	}
	public boolean signValidate() {
		if (fuyanNameEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","请输入您的真实姓名",this);
			return false;
		}else if (fuyanNumberEt.getText().toString().trim().equals("")) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","输入赴宴人数",this);
			return false;
		} 
		return true;
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				if(sdkVersion>=11){
					if(MyApplication.getInstance().getScreenW()>=1080){
						moveObject(playBtn,-19,0,-494,0);
						moveObject(shareBtn,-231,0,-441,0);
						moveObject(blessingBtn,-411,0,-274,0);
						moveObject(menuBtn,-441,0,-21,0);
					}else{
						moveObject(playBtn,-11,0,-286,0);
						moveObject(shareBtn,-132,0,-252,0);
						moveObject(blessingBtn,-221,0,-154,0);
						moveObject(menuBtn,-252,0,-12,0);
					}
				}else if(MyApplication.getInstance().getScreenW() < 400){
					moveBtn(playBtn,6,156);
					moveBtn(shareBtn,66,126);
					moveBtn(blessingBtn,108,72);
					moveBtn(menuBtn,126,6);
				}else{
					moveBtn(playBtn,11,286);
					moveBtn(shareBtn,121,231);
					moveBtn(blessingBtn,198,132);
					moveBtn(menuBtn,231,11);
				}
			}else if(msg.what == 1){//刷新照片列表布局
//				notifyPhotoListView(photoList,photoListView);
				 photoIv = null;
				 photoIv = getImgView(msg.obj.toString());
				if(photoIv!=null){
					photoListView.addView(photoIv);
				}
			}else if(msg.what == 2){
				CustomTextView.justify(yaoqingInfo,yaoqingInfo.getWidth());
			}else if(msg.what == 3){//隐藏祝福输入框
				zhufuLayout.clearAnimation();
				zhufuLayout.setVisibility(View.GONE);
				setLayoutParams(60,60);
			}else if(msg.what == 4){
				zhufuLayout.clearAnimation();
				fuyanLayout.setVisibility(View.GONE);
				setLayoutParams(60,60);
			}else if(msg.what == 5){//让按钮可以点击
				openMenuBtn.setClickable(true);
			}else if(msg.what == 6){//让按钮们隐藏
				setBtnVisibility(View.GONE);
			}else if(msg.what == 7){//过两秒后返回键按的第一次失效
				backFlag =0;
			}else  if(msg.what ==8){
				playVideo();//视频播放
			}else if(msg.what == 9){//发送成功
				progressDialog.dismiss();
				zhufuEt.setText("");
				MessageBox.CreateAlertDialog("提示！","发送成功",MainVideoActivity.this);
				fuyanNameEt.setText("");
				fuyanNumberEt.setText("");
			}else if(msg.what == 10){//发送失败
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","发送失败",MainVideoActivity.this);
			}else if(msg.what == 11){
				notifyMarryAdapter();
			}else if(msg.what == 20){//切换到全屏后  设置暂停
				videoPlayBtn.setVisibility(View.VISIBLE);
				videoPlayImg.setVisibility(View.VISIBLE);
			}
//			playBtn.layout(100, 200, MyApplication.getInstance().getScreenW()-100,
//					MyApplication.getInstance().getScreenH()-200);
			super.handleMessage(msg);
		}
		
	};
	private void moveObject(Object object,float fromx,float tox,float fromy,float toy){
		  AnimatorSet set = new AnimatorSet();  
		  ObjectAnimator animX = ObjectAnimator .ofFloat(object, "translationX",fromx, tox);
		  animX.setDuration(300);
		  ObjectAnimator animY = ObjectAnimator .ofFloat(object, "translationY",fromy, toy);
		  animY.setDuration(300);
		  
		  ObjectAnimator animRoate = ObjectAnimator.ofFloat(object, "rotation", 0f, 720f);
		  animRoate.setDuration(300);
		  set.playTogether(animX,animY,animRoate);
	      set.start();
	}
	/**
	 * 移动并旋转控件
	 * @param view
	 * @param tox
	 * @param toy
	 */
	private void moveBtn(final View view,final int tox,final int toy){
		AnimationSet set = new AnimationSet(true);
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.anim_roate);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin);  
		operatingAnim.setFillAfter(true);
		set.addAnimation(operatingAnim);
		
		Animation mTranslateAnimation = new TranslateAnimation(0, tox,0,toy);// 移动
	    mTranslateAnimation.setDuration(300);
		mTranslateAnimation.setFillAfter(true);
		set.addAnimation(mTranslateAnimation);
		
		set.setFillAfter(true);
		set.setRepeatMode(Animation.RESTART);
		set.setRepeatCount(Animation.INFINITE);
		set.setDuration(300);
		
		view.startAnimation(set);
		mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
	        @Override
	        public void onAnimationStart(Animation animation) {
	        }
	        
	        @Override
	        public void onAnimationRepeat(Animation animation) {
	        }
	        
	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	view.clearAnimation();
	        	view.layout(view.getLeft()+tox, view.getTop()+toy, view.getRight()+tox, view.getBottom()+toy);
//	        	view.setVisibility(View.VISIBLE);
	        }
	    });
	}
	@Override
	protected void onResume() {
		isIntent = false;
		if(MainVideoActivity.typeUser.equals("1")){
			new Thread(getLogRunnable).start();
		}
		if(isReturnFromMenu){
			new Thread(photoListRunnable).start();
		}
		isReturnFromMenu = false;
		super.onResume();
	}
	int sx = 0;
	int sy = 0;
	int ex = 0;
	int ey = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == videoView){
			videoView.pause();
			videoPlayBtn.setVisibility(View.VISIBLE);
			isPause = true;
			return true;
		}
		view = v;// 记录点击的控件
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			viewPager.setScanScroll(true);
			sx = (int) event.getX();
			sy = (int) event.getY();
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			ex = (int) event.getX();
			ey = (int) event.getY();
			
//			if(Math.abs(sx-ex)<Math.abs(sy-ey)){
//				return super.onTouchEvent(event);
//			}else{
//				return false;
//			}
		}else if (event.getAction() == MotionEvent.ACTION_UP) {
			ex = (int) event.getX();
			ey = (int) event.getY();
			if(layoutParams.leftMargin < 0 && ex>sx){
				new AsynMove().execute(SPEED);
//				viewPager.setOnTouchListener(this);
//				parent.setOnTouchListener(null);
				viewPager.setScanScroll(true);
			}else if(viewPager.getCurrentItem() == pageViews.size()-1 && ex<sx && sx-ex>20 
					&& layoutParams.leftMargin == 0 && !isViewPagerScrolling){
//				viewPager.setOnTouchListener(null);
//				parent.setOnTouchListener(this);
				viewPager.setScanScroll(false);
				new AsynMove().execute(-SPEED);
			}else{
				viewPager.setScanScroll(true);
			}
//			ex = (int) event.getX();
//			ey = (int) event.getY();
//			if(Math.abs(sx-ex)<Math.abs(sy-ey)){
//				return super.onTouchEvent(event);
//			}
//			if(sx>ex && (sx-ex)>30){//手指向左滑动
//				if(isIntent){
//					return super.onTouchEvent(event);
//				}
//				Intent intent = new Intent(this,MainPhotoActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//				isIntent = true;
//			}else{
//				return super.onTouchEvent(event);
//			}
		}
//		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
//		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
//			// 缩回去
//			if (layoutParams.leftMargin < -window_width / 2) {
//				new AsynMove().execute(-SPEED);
//			} else {
//				new AsynMove().execute(SPEED);
//			}
//		}if (MotionEvent.ACTION_UP == event.getAction() && layoutParams.leftMargin < 0) {
//			new AsynMove().execute(SPEED);
//			return false;
//		}
		return super.onTouchEvent(event);
//		return mGestureDetector.onTouchEvent(event);
	}
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
		
		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], 0);
				layoutParams_1.leftMargin = Math.min(layoutParams_1.leftMargin
						+ values[0], window_width);
				Log.v(TAG, "layout_left右" + layoutParams.leftMargin
						+ ",layout_right右" + layoutParams_1.leftMargin);
			} else {
				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], -MAX_WIDTH);
				layoutParams_1.leftMargin = Math.max(layoutParams_1.leftMargin
						+ values[0], window_width - MAX_WIDTH);
				Log.v(TAG, "layout_left左" + layoutParams.leftMargin
						+ ",layout_right左" + layoutParams_1.leftMargin);
			}
			layout_right.setLayoutParams(layoutParams_1);
			layout_left.setLayoutParams(layoutParams);

		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg0 == lv_set){//右边菜单栏
			videoPause();
			if(arg2 == 0){
				isReturnFromMenu = true;
				Intent intent = new Intent(this,WeddingStoreActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}else if(arg2 == 1){
				Intent intent = new Intent(this,PreferenceActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}else if(arg2 == 2){
				Intent intent = new Intent(this,AboutUsActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
		}/*else if(arg0 == photoListView){//我的相册列表
			Intent intent = new Intent(this,ImageSwitcher.class);
//			intent.putExtra("photoList", (Serializable)photoList.);
			intent.putExtra("index", arg2);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}*/
	}
	@Override
	public boolean onDown(MotionEvent e) {
		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 点击的不是layout_left
				if (view != null && view == menuBtn) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					// 左移动
					if (layoutParams.leftMargin >= 0) {
						new AsynMove().execute(-SPEED);
						lv_set.setSelection(0);// 设置为首位.
					} else {
						// 右移动
						new AsynMove().execute(SPEED);
					}
				} else if (view != null && view == viewPager) {
					RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					if (layoutParams.leftMargin < 0) {
						// 说明layout_left处于移动最左端状态，这个时候如果点击layout_left应该直接所以原有状态.(更人性化)
						// 右移动
						new AsynMove().execute(SPEED);
					}
				}

				return true;
	}
	@Override
	public void doFlingLeft(float distanceX) {
		// 执行滑动.
		doScrolling(distanceX);
		
	}
	@Override
	public void doFlingRight(float distanceX) {
		// 执行滑动.
		doScrolling(distanceX);
		
	}
	@Override
	public void doFlingOver(MotionEvent event) {
		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 缩回去
			if (layoutParams.leftMargin < -window_width / 2) {
				new AsynMove().execute(-SPEED);
			} else {
				new AsynMove().execute(SPEED);
			}
		}
		
	}
	
	public int backFlag = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				new AsynMove().execute(SPEED);
				return false;
			}else{
				if(viewPager.getCurrentItem() == 0){
					if(backFlag == 0){
						handler.sendEmptyMessageDelayed(7, 2000);
						backFlag = 1;
						Toast.makeText(this, "再按一次，退出I喜帖！", 2000).show();
					}else if(backFlag == 1){
						finish();
					}
				}else{
					viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
				}
			}
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
}
