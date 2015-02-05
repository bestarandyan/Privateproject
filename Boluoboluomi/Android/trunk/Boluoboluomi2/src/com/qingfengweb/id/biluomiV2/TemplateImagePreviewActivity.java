package com.qingfengweb.id.biluomiV2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.qingfengweb.boluomi.share.ConstantsValuesShare;
import com.qingfengweb.boluomi.share.EmailShare;
import com.qingfengweb.boluomi.share.SMSShare;
import com.qingfengweb.boluomi.share.WechatShare;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.util.MessageBox;
import com.qingfengweb.util.MyHorizontalScrollView;
import com.qingfengweb.util.MyVerticalScrollView;
import com.qingfengweb.xmlparse.ParseXmlFromDom;
import com.tencent.tauth.Tencent;

public class TemplateImagePreviewActivity extends BaseActivity{
	private Button backBtn,shareBtn;
	public ImageView image,photo;
	private Bundle bundle;
	private Bitmap bitmap;
	private int type = 0;
//	private ZoomImageView zoomImage;
	private List<Map<String,Object>> images = new ArrayList<Map<String,Object>>();
	private InputStream xmlInputStream;
	private PopupWindow selectPopupWindow = null;
	public boolean initWedget_tag = true;
	private boolean flag = true;
	private Button shareBtn0,shareBtn1, shareBtn2, shareBtn3, shareBtn4, cancleBtn;// 分享功能的对应四个按钮
	private RelativeLayout parent = null;
//	private HorizontalScrollView scrollView;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private MyHorizontalScrollView horizontalScrollView;//横向滚动条
	private MyVerticalScrollView verticalScrollView;//纵向滚动条
	private int left,top,right,bottom;
	private Bitmap photoBit = null;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private Bitmap bitmap_photo = null;//整合后的图片
	private int click_state = 0;// 0未点击任何按钮时，1点击分享，2，点击保存，3，点击分享新浪，4，点击分享人人网
	private File tempfile = null;//图片保存的临时文件
	private String server_photo_path="";//图片上传成功后，服务器返回的图片地址
	private String templateid="1";//模板id；
	private int templateId = 0;
	private Button prohibitBtn;
	public String tempid = "";
	public String shareTitle ="我们结婚啦！";//分享的内容
	public String shareText ="";//分享的内容
	private int mExtarFlag = 0x00;
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_IMAGE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_templateimagepreview);
//		MyApplication.activity_tag=18;
		templateid = MyApplication.getInstant().getTemplateid();
		bundle = getIntent().getBundleExtra("bundle");
//		templateId = bundle.getInt("templateId", R.drawable.f5);
		type = getIntent().getIntExtra("type", 0);
		findView();
		getSAXData();
		shareText = "我们结婚啦！诚邀您参加我们的婚礼，期待您的祝福！";
//		initView();
			click_state = 6;
			new Thread(runnable).start();
	}
	/**
	 * 得到当前模板的信息的位置
	 */
	private void getSAXData(){
		tempid = MyApplication.getInstant().getTemplateid();
		File file = new File(ConstantsValues.SDCARD_ROOT_PATH+"/"+ConstantsValues.INVITATION_IMG_URL+"invitation"+Integer.parseInt(tempid)+".xml");
//			mSAXParser.parse(file, mSaxHandler);
//			images = mSaxHandler.getImages();
			try {
				xmlInputStream = new FileInputStream(file);
				images = ParseXmlFromDom.parseXml(xmlInputStream, "template", "field"); 
				System.out.println(images.size()); 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	int lastX = 0, lastY = 0;  
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!scroolBoo){
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix); // 把原始 Matrix对象保存起来
				start.set(event.getX(), event.getY()); // 设置x,y坐标
				mode = DRAG;
				lastX = (int) event.getRawX();   
		        lastY = (int) event.getRawY(); 
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event); // 求出手指两点的中点
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x,event.getY() - start.y);
					photo.invalidate();//重绘    
					/*   int dx = (int) event.getRawX() - lastX;   
			           int dy = (int) event.getRawY() - lastY;   
			           int left = photo.getLeft() + dx;   
			           int top = photo.getTop() + dy;   
			           int right = photo.getRight() + dx;   
			           int bottom = photo.getBottom() + dy;   
			           photo.layout(left, top, right, bottom);   
			           photo.postInvalidate();
			           photo.refreshDrawableState();
			           lastX = (int) event.getRawX();   
			           lastY = (int) event.getRawY();  */
			           break;   
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
					}
				break;
			}
			photo.setImageMatrix(matrix);
	/*		photoBit = Bitmap.createBitmap(photoBit, 0, 0, photoBit.getWidth(), photoBit.getHeight(), matrix, true);
			photo.setImageBitmap(photoBit);*/
			return true;
		}else{
			int x1 = 0,x2=0,y1=0,y2=0;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				x1=(int) event.getX();
				y1 = (int) event.getY();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				x2=(int) event.getX();
				y2 = (int) event.getY();
				if(Math.abs(x1-x2) > Math.abs(y1-y2)){
					horizontalScrollView.arrawScroll();
					verticalScrollView.prohibitScroll();
				}else{
					horizontalScrollView.prohibitScroll();
					verticalScrollView.arrawScroll();
				}
				break;
			}
			return true;
		}
	}
	// 求两点距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 求两点间中点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	private void initView(){
		image.setImageBitmap(bitmap);
		if(photoBit!=null){
			photo.setMinimumWidth(bitmap.getWidth());
			photo.setMinimumHeight(MyApplication.getInstant().getHeightPixels());
			photo.setMaxWidth(photoBit.getWidth());
			photo.setImageBitmap(photoBit);
			if(getIntent().getIntExtra("activity_type", 0) == 0){//只有从TemplateInfoActivity跳转过来的时候才让照片在左上角
				Matrix m = MyApplication.getInstant().getMatrix();
				if(m!=null){
					photo.setImageMatrix(m);
					matrix = m;
				}
			}
			
			
		}
		if(type!=1 && type != 4 && type!=7){
			prohibitBtn.setVisibility(View.VISIBLE);
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(R.string.template_prompt)
					.setMessage(R.string.dialog_message_templateimage)
					.setPositiveButton("我知道了",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
			callDailog.show();
		}else {
			prohibitBtn.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onDestroy() {
		if(bitmap_photo!=null){
			bitmap_photo.recycle();
			bitmap_photo = null;
		}
		if(photoBit!=null){
			photoBit.recycle();
			photoBit = null;
		}
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
		if(image!=null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable)image.getDrawable();   
	        //如果图片还未回收，先强制回收该图片    
	        if(bitmapDrawable !=null &&bitmapDrawable.getBitmap()!=null&&!bitmapDrawable.getBitmap().isRecycled()){   
	            bitmapDrawable.getBitmap().recycle();   
	        }  
		}
		if(photo!=null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable)photo.getDrawable();   
	        //如果图片还未回收，先强制回收该图片    
	        if(bitmapDrawable !=null &&bitmapDrawable.getBitmap()!=null&& !bitmapDrawable.getBitmap().isRecycled()){   
	            bitmapDrawable.getBitmap().recycle();   
	        }  
		}
		super.onDestroy();
	}
	
/**
 * 更具模板获取最终的模板图片
 * 先把照片画入画板  然后再把模板画入画板  这样就能保证模板的的空白处显示出照片
 * 创建画板时 必须使用Argb_8888可以保证最终模板的质量，也可以保证模板的透明区域能显示照片   否则为黑色
 * @author 刘星星
 * @param bmpSrc
 * @return
 */
	public  Bitmap getRoundedCornerBitmap(Bitmap bmpSrc)
    {
        if (null == bmpSrc)
        {
            return null;
        }

        int bmpSrcWidth = bmpSrc.getWidth();
        int bmpSrcHeight = bmpSrc.getHeight();

        Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight, Config.ARGB_8888);
        if (null != bmpDest)
        {
            Canvas canvas = new Canvas(bmpDest);
             Paint paint = new Paint();
             paint.setAntiAlias(true);
     		 paint.setColor(Color.WHITE);
     		 paint.setStyle(Paint.Style.FILL);
//             paint.setDither(true);
//             paint.setStyle(Paint.Style.STROKE);
//             paint.setStrokeJoin(Paint.Join.ROUND);
//             paint.setStrokeCap(Paint.Cap.ROUND);
     		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
             if(photoBit!=null){
            	 canvas.drawBitmap(photoBit, matrix, paint);
             }
             canvas.drawBitmap(bmpSrc, 0,0, paint);
         
        }
        return bmpDest;
    }
public Bitmap getMatrixBitmap(){
	int bmpSrcWidth = photoBit.getWidth();
    int bmpSrcHeight = photoBit.getHeight();
    Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight, Config.ARGB_8888);
    if (null != bmpDest)
    {
        Canvas canvas = new Canvas(bmpDest);
         Paint paint = new Paint();
         canvas.drawBitmap(photoBit, matrix, paint);
    }
    return bmpDest;
}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	//shouyaoName xinlangName xinniangName year  month ri week
	//longliyue longliri shijian dianming address phone
	public  Bitmap doodle(Bundle bundle,int position){
		BitmapDrawable bitmapDrawable = (BitmapDrawable)image.getDrawable();   
        //如果图片还未回收，先强制回收该图片    
        if(bitmapDrawable !=null && !bitmapDrawable.getBitmap().isRecycled()){   
            bitmapDrawable.getBitmap().recycle();   
        }   
        String filePath =ConstantsValues.SDCARD_ROOT_PATH+"/"+ConstantsValues.INVITATION_IMG_URL+images.get(0).get("background").toString();
		Bitmap template = null;
		template = BitmapFactory.decodeFile(filePath);
		Bitmap waterBitmap = Bitmap.createBitmap(template.getWidth(), template.getHeight(),	Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Paint p = new Paint();
		Canvas canvas = new Canvas(waterBitmap);
		p.setAlpha(255);
		canvas.drawBitmap(template, 0, 0, p);// 在 0，0坐标开始画入原图片src
		p.setAntiAlias(true);
		p.setColor(Color.RED);
		for(int i=1;i<images.size();i++){
			p.setTextSize(SToI(images.get(i).get("font-size").toString()));
			p.setColor(Color.parseColor(images.get(i).get("font-color").toString()));
			String text = bundle.getString(images.get(i).get("name").toString());
			if(text!=null){
				canvas.drawText(text, SToI(images.get(i).get("x").toString()), SToI(images.get(i).get("y").toString()), p);
			}
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return waterBitmap;
	}
	
	public int SToI(String str){
		return Integer.parseInt(str);
	}

	private boolean scroolBoo = true;
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		image = (ImageView) findViewById(R.id.image);
		backBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		photo = (ImageView) findViewById(R.id.photo);
		parent = (RelativeLayout) findViewById(R.id.parent);
		horizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.scrollView);
		verticalScrollView = (MyVerticalScrollView) findViewById(R.id.verticalScrollView);
		prohibitBtn = (Button) findViewById(R.id.btn);
		prohibitBtn.setOnClickListener(this);
	
	}
	@Override
	public void onClick(View v) {
//		if (click_limit) {
//			click_limit = false;
//		} else {
//			return;
//		}
		MyApplication.getInstant().setMatrix(matrix);
		Intent i = new Intent();
		if (v == backBtn) {
			i.setClass(this, TemplateInfoActivity.class);
			i.putExtra("type", getIntent().getIntExtra("type", 0));
			i.putExtra("bundle1", bundle);
			startActivity(i);
			finish();
		}else if(v == prohibitBtn){//控制按钮
			if(scroolBoo){
				horizontalScrollView.prohibitScroll();
				verticalScrollView.prohibitScroll();
				scroolBoo = false;
				prohibitBtn.setBackgroundResource(R.drawable.dingzi_on);
			}else{
				horizontalScrollView.arrawScroll();
				verticalScrollView.arrawScroll();
				scroolBoo = true;
				prohibitBtn.setBackgroundResource(R.drawable.dingzi);
			}
		} else if (v == shareBtn) {//点击发送
			/*bitmap_photo = getRoundedCornerBitmap(bitmap);
			photo.setVisibility(View.GONE);
			image.setImageBitmap(bitmap_photo);*/
			if(photo.isShown()){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(R.string.prompt)
					.setMessage(R.string.dialog_message_sendandkeep)
					.setPositiveButton(R.string.comfrim,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									click_limit = false;
									click_state = 1;
									if (flag) {
										new Thread(runnable).start();
									} else {
										flag = true;
										dismiss();
										click_limit = true;
									}
								}
							}).setNegativeButton(R.string.cancel, null);
			callDailog.show();
			click_limit = true;
			}else{
				handler.sendEmptyMessage(7);
				click_limit = true;
			}
		}else if(v.getId() == R.id.sharelayout1){//微博分享
			Intent intent = new Intent(this,ShareActivity.class);
				  intent.putExtra("filePath", tempfile.getAbsolutePath());
			intent.putExtra("msgStr", shareText);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(shareText, "");
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, shareTitle);
//            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://api.91blm.com:99");
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, shareText);
            if(new File(tempfile.getAbsolutePath()).exists()){
            	params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, tempfile.getAbsolutePath());
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
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(shareText, "");
		}/*else if(v.getId() == R.id.sharelayout5){//二维码分享
//			Intent intent = new Intent(this,ErweimaShareActivity.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}*/else if(v.getId() == R.id.sharelayout6){//微信
			showWechatShareDialog();
//			showMoreShareDialog();
//			Intent intent = new Intent(Intent.ACTION_SEND);  
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
////            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile((File) pathes.get(mIndex).get("file")));  //传输图片或者文件 采用流的方式  
//            intent.putExtra(Intent.EXTRA_TEXT, "分享分享微博");   //附带的说明信息  
//            intent.putExtra(Intent.EXTRA_SUBJECT, "标题");  
//            intent.setType("image/*");   //分享图片  
//            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.closeWindowBtn){
			dismiss();
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}else if(v.getId() == R.id.shareToGoodFriend){//分享到好友
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize(null, null)){
					wechatShare.shareImage(tempfile.getAbsolutePath(), 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareImage(tempfile.getAbsolutePath(), 2);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
			
		}else if(v.getId() == R.id.shareWeixin){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareSina){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareTenxun){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareKongjian){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareDuanxin){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareEmail){
			moreShareDialog.dismiss();
		}else if(v.getId() == R.id.shareMoreClose){
			moreShareDialog.dismiss();
		}
		super.onClick(v);
	}
	
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, TemplateImagePreviewActivity.this);
        
        final Activity activity = TemplateImagePreviewActivity.this;
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, TemplateInfoActivity.class);
			i.putExtra("type", getIntent().getIntExtra("type",0));
			i.putExtra("bundle1", bundle);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	/***
	 * author by Ring 将图片保存到相册
	 */

	public String keepPhotoes() {
		try {
			File file1 = null;
			File dir = new File(android.os.Environment
					.getExternalStorageDirectory()+"/"
					+getResources().getString(R.string.root_directory)+"/"
					+getResources().getString(R.string.photo_template));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file1 = new File(dir, tempfile.getName());
			if (file1.exists() && file1.isDirectory()) {
				file1.delete();
			}
			tempfile.renameTo(file1);
			tempfile.delete();// 删除
			tempfile = new File(Environment.getExternalStorageDirectory() + "/"
					+ getResources().getString(R.string.root_directory) + "/"
					+ getResources().getString(R.string.photo_template_temp));
			if (tempfile != null && tempfile.exists() && tempfile.isDirectory()) {
				tempfile.delete();
			}
			return file1.getAbsolutePath();
		} catch (Exception e) {
			return "";
		}
	}

	/***
	 * author by Ring 将图片保存到临时文件
	 */

	public boolean keepTempPhotoes() {
		try {
			tempfile = new File(ConstantsValues.SDCARD_ROOT_PATH+"/"+ConstantsValues.INVITATION_IMG_URL+PicHandler.getFileName());
			if(bitmap_photo!=null){
				PicHandler.OutPutImage(tempfile, bitmap_photo);
			}else{
				PicHandler.OutPutImage(tempfile, bitmap);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//			if(MyApplication.activity_tag!=18)
//				return;
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 保存图片成功给用户提示
				Bundle b = msg.getData();
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.keep_photo_success)+"文件保存在："+b.getString("msg"),
						TemplateImagePreviewActivity.this);
				break;
			case 2:// 登录失败给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.keep_photo_error),
						TemplateImagePreviewActivity.this);
				break;
			case 3:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						TemplateImagePreviewActivity.this);
				break;
			case 4:// 打开进度条1//保存图片
				progressdialog = new ProgressDialog(
						TemplateImagePreviewActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.photo_progress));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 5:// 打开进度条2//生成图片
				progressdialog = new ProgressDialog(
						TemplateImagePreviewActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.photo_progress1));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 生成图片成功
					// 显示PopupWindow窗口
				flag = false;
//				popupWindwShowing();
				showShareDialog(parent);
				photo.setVisibility(View.GONE);
				if(bitmap_photo!=null){
					image.setImageBitmap(bitmap_photo);
				}
				break;
			case 8:// 生成图片失败
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.keep_photo_error1),
						TemplateImagePreviewActivity.this);
				break;
			case 9://向服务器提交图片
				progressdialog = new ProgressDialog(
						TemplateImagePreviewActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.photo_progress2));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 10://上传图片失败
				String errormsg = "";
				if(reponse.equals("-1000")){
					errormsg = getResources().getString(R.string.progress_timeout);
				}else{
					errormsg = getResources().getString(R.string.upload_photo_message1);
				}
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						errormsg,
						TemplateImagePreviewActivity.this);
			case 11://发送电子邮件
				i.setClass(TemplateImagePreviewActivity.this,AddEmailActivity.class);
				i.putExtra("bundle", bundle);
				i.putExtra("type", type);
				i.putExtra("server_photo_path", server_photo_path);
				i.putExtra("tempfile", tempfile.getAbsolutePath());
				i.putExtra("templateid", templateid);
				
				startActivity(i);
				finish();
				break;
			case 12://发送新浪微博
//				i.setClass(TemplateImagePreviewActivity.this, WeiBoActivity.class);
//				i.putExtra("filepath", tempfile.getAbsolutePath());
//				i.putExtra("type1", 2);
//				i.putExtra("type", type);
//				i.putExtra("tag", 1);
//				i.putExtra("bundle", bundle);
//				i.putExtra("decription", "");
//				TemplateImagePreviewActivity.this.startActivity(i);
//				finish();
				break;
			case 13://发送人人网
//				i.setClass(TemplateImagePreviewActivity.this, WeiBoActivity.class);
//				i.putExtra("filepath", tempfile.getAbsolutePath());
//				i.putExtra("type1", 3);
//				i.putExtra("type", type);
//				i.putExtra("tag", 1);
//				i.putExtra("bundle", bundle);
//				i.putExtra("decription", "");
//				TemplateImagePreviewActivity.this.startActivity(i);
//				finish();
				break;
			case 14:
				initView();
				break;
		
			}
		};
	};
	
	/***
	 * author by Ring 处理耗时操作
	 */

	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			boolean b = false;
			switch (click_state) {
			case 1:
				handler.sendEmptyMessage(5);// 开启进度条
				if(photoBit!=null){
					bitmap_photo = getRoundedCornerBitmap(bitmap);
				}
				b = keepTempPhotoes();
				handler.sendEmptyMessage(6);// 关闭进度条
				if (b) {
					handler.sendEmptyMessage(7);// 打开分享弹出框
					
				} else {
					handler.sendEmptyMessage(6);// 关闭进度条
				}
				break;
			case 2:
				handler.sendEmptyMessage(4);// 开启进度条
				String msg = keepPhotoes();
				handler.sendEmptyMessage(6);// 关闭进度条
				if (msg.length()>0) {
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("msg", msg);
					message.what =1;
					message.setData(bundle);
					handler.sendMessage(message); 
				} else {
					handler.sendEmptyMessage(6);// 关闭进度条
				}
				break;
			case 3:
//				handler.sendEmptyMessage(9);// 开启进度条
//				b = uploadFile();
//				handler.sendEmptyMessage(6);// 关闭进度条
//				if (b) {
					handler.sendEmptyMessage(11);// 
//				} else {
//					handler.sendEmptyMessage(6);// 关闭进度条
//					handler.sendEmptyMessage(10);
//				}
				break;
			case 4:
//				handler.sendEmptyMessage(9);// 开启进度条
//				b = uploadFile();
//				handler.sendEmptyMessage(6);// 关闭进度条
//				if (b) {
					handler.sendEmptyMessage(12);// 
//				} else {
//					handler.sendEmptyMessage(6);// 关闭进度条
//					handler.sendEmptyMessage(10);
//				}
				break;
			case 5:
//				handler.sendEmptyMessage(9);// 开启进度条
//				b = uploadFile();
//				handler.sendEmptyMessage(6);// 关闭进度条
//				if (b) {
					handler.sendEmptyMessage(13);// 
//				} else {
//					handler.sendEmptyMessage(6);// 关闭进度条
//					handler.sendEmptyMessage(10);
//				}
				break;
			case 6:
				handler.sendEmptyMessage(5);// 开启进度条
				bitmap = doodle(bundle,type);
				if(type!=1 && type != 4 && type!=7){
					photoBit = getDrawable(bundle.getString("image"));
				}
				handler.sendEmptyMessage(6);// 关闭进度条
				handler.sendEmptyMessage(14);
				break;
		
			}
			click_limit = true;
		}
	};

	/**
	 * author by Ring
	 * 用当前时间给图片命名
	 * 
	 */
	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'img'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	
	/***
	 * author by Ring
	 * 上传电子请帖图片
	 */
	public boolean uploadFile(){
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("appid",  MyApplication.getInstance(
//				this).getAPPID());
//		params.put("appkey", MyApplication.getInstance(
//				this).getAPPKEY());
//		params.put("action", "upload_invitation_image");
//		params.put("username", MyApplication
//				.getInstance(this).getUserinfo().getUsername());
//		params.put("password", MyApplication
//				.getInstance(this).getUserinfo().getPassword());
//		params.put("file", tempfile.getName());
//		Map<String, File> filesmap = new HashMap<String, File>();
//		filesmap.put(tempfile.getName(), tempfile);
//		try {
//			reponse = UploadFile.post(MyApplication.getInstance(this).getURL()+"invitation.aspx", params, filesmap);
//		} catch (IOException e) {
//			reponse = "初始值";
//			e.printStackTrace();
//		}
//		System.out.println(params+"---"+reponse);
//		if (reponse.contains("/")) {
//			server_photo_path = reponse;
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}
	
	
	private Bitmap getDrawable(String pathName) {
		BitmapFactory.Options opts =  new  BitmapFactory.Options();
		Bitmap   bmp = null;
		try {
	        opts.inJustDecodeBounds =  true ;
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;   
	        BitmapFactory.decodeFile(pathName, opts);
	         opts.inSampleSize = computeSampleSize(opts, -1 , 1000 * 1000 ); 
	         opts.inJustDecodeBounds =  false ;
	              bmp = BitmapFactory.decodeFile(pathName, opts);
//	        	  bmp = BitmapCache.getInstance().getBitmap(id, MyCameraActivity.this,opts);
	              bmp = scaleImg(bmp,bmp.getWidth(),bmp.getHeight());
		} catch (OutOfMemoryError e) {
			bmp = scaleImg(bmp,800,600);
		}
		
			return bmp;
	}
	
	
	/**
	 * 等比压缩图片
	 * 
	 * @param bm
	 *            被压缩的图片
	 * @param newWidth
	 *            压缩后的宽度
	 * @param newHeight
	 *            压缩后的图片高度
	 * @return 一张新的图片
	 * @author 刘星星
	 */
	

	public Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		// Bitmap newbm = null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		float newWidth1 = newWidth;
		float newHeight1 = newHeight;
		/*
		 * if(width<newWidth1 && height <newHeight1){ return bm; }
		 */
		// 计算缩放比例
		if (height > width) {
			float scaleHeight = ((float) newHeight1) / height;
			newWidth1 = ((float) (width * (float) newHeight1) / height);
			float scaleWidth = ((float) newWidth1) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap	scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
			return scaleBit;
		} else {
			float scaleWidth = ((float) newWidth1) / width;
			newHeight1 = ((float) (height * (float) newWidth1) / width);
			float scaleHeight = ((float) newHeight1) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
		
			Bitmap	scaleBit = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,	true);
			// Bitmap bmp = scaleBit;
			return scaleBit;
		}

	}
	/**
	 * 动态计算出图片的inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 返回图片的最佳inSampleSize
	 */
	public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
