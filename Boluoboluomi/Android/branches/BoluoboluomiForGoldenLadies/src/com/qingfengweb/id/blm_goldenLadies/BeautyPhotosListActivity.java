/**
 * 
 */
package com.qingfengweb.id.blm_goldenLadies;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qingfengweb.adapter.GridViewAdapter;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.model.PictureInfo;
import com.qingfengweb.model.PictureThemes;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.util.CommonUtils;

/**
 * @author 刘星星
 * @createDate 2013、10、15
 * 美图欣赏照片列表界面
 *
 */
@SuppressLint("ShowToast")
public class BeautyPhotosListActivity extends BaseActivity{
	private Button backBtn;// 返回按钮
	private GridView topGv,bottomGv;// 图片网格控件
	private ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();//大图
	private List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();//缩略图
	private ImageView daTuImg;
	private ImageButton openMenuBtn;
	private DBHelper dbHelper;
	GridViewAdapter adapter = null;//
	int daW = 0;
	List<Map<String,Object>> localListData = null;//表示本地的照片集合
	RelativeLayout firstImgLayout = null;
	Map<String,Object> themeMap = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_photolist);
		findView();
		initData();
		new Thread(getPhotoRunnable).start();
	}
	private void initData(){
		themeMap = (Map<String, Object>) getIntent().getSerializableExtra("themeMap");
		daW = (MyApplication.getInstant().getWidthPixels())/2-10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(daW,daW);
		params.setMargins(5, 5, 0, 0);
		firstImgLayout.setLayoutParams(params);
		firstImgLayout.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(daW,daW);
		daTuImg.setLayoutParams(params1);
		dbHelper = DBHelper.getInstance(this);
		String sql = "select *from "+PictureInfo.TableName+" where storeid = '"+UserBeanInfo.getInstant().getCurrentStoreId()+"' and themeid='"+themeMap.get("id")+"'";
		localListData = dbHelper.selectRow(sql, null);
		handlerGvData();//处理gridview中的数据
	}
	/**
	 * @author 刘星星
	 * 处理两个gridview中的数据
	 */
	private void handlerGvData(){
		if(localListData.size() == 0){
			firstImgLayout.setVisibility(View.GONE);
		}else{
			firstImgLayout.setVisibility(View.VISIBLE);
			//处理第一张图片
			String firstPhoto = localListData.get(0).get("imageid").toString();
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB+firstPhoto+".png";
			if(firstPhoto.equals("")){
				firstPhotoUrl = localListData.get(0).get("imgpath").toString();
			}
			if(new File(firstPhotoUrl).exists()){//如果该图片存在 则直接显示该图片
				int w = MyApplication.getInstant().getWidthPixels()/2;
//				Bitmap bitmap = CommonUtils.getDrawable(firstPhotoUrl, w);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
				opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,700 * 700);
				opts.inJustDecodeBounds = false;
				// 如果图片还未回收，先强制回收该图片
				Bitmap bitmap = BitmapFactory.decodeFile(firstPhotoUrl, opts);
				if (bitmap != null) {
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int newWidth = w;
					// 计算缩放比例
					float scaleWidth = ((float) newWidth) / width;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleWidth);
					// 得到新的图片
					bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
				}
				daTuImg.setImageBitmap(bitmap);
			}else{//如果不存在 则去服务器下载
				if(firstPhoto!=null && !firstPhoto.equals("")){
					RequestServerFromHttp.downImage(BeautyPhotosListActivity.this,daTuImg,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB,R.drawable.photolist_defimg);
				}
			}
			list1.clear();
			list2.clear();
			if(localListData.size()>1){//大于5则代表会有三个部分
				for(int i=1;i<localListData.size();i++){
					Map<String, Object> map = localListData.get(i);
					if(i<5){
						list1.add(map);
					}else{
						list2.add(map);
					}
				}
			}
			if(list1.size()>0){
				notifyAdapter(topGv, list1,2,daW);
			}
			if(list2.size()>0){
				notifyAdapter(bottomGv, list2,4,daW);
			}
			
		}
	}
	/**
	 * 给gv控件加载适配器
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	private void notifyAdapter(GridView gv,List<Map<String, Object>> list,int numColumns,int daW) {
		int h = list.size()/numColumns+(list.size()%numColumns>0?1:0);//在垂直方向上有多少行
		int util = (daW-5)/2/*(MyApplication.getInstant().getWidthPixels()-25)/4*/;//单位高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(numColumns==4?LayoutParams.MATCH_PARENT:(MyApplication.getInstant().getWidthPixels()/2-5),
				h*(util+5));
		if(numColumns==4){
			params.setMargins(5, 5, 5, 20);
		}else{
			params.setMargins(5, 5, 5, 0);
		}
		gv.setLayoutParams(params);
		adapter = new GridViewAdapter(this, list,daW,1);
		gv.setAdapter(adapter);
		gv.setCacheColorHint(0);
	}

	/**
	 * 初始化控件函数
	 */
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		topGv = (GridView) findViewById(R.id.topGv);
		topGv.setOnItemClickListener(new gvItemListener());
		topGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		bottomGv = (GridView) findViewById(R.id.bottomgv);
		bottomGv.setOnItemClickListener(new gvItemListener());
		bottomGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		firstImgLayout = (RelativeLayout) findViewById(R.id.firstImgLayout);
		daTuImg = (ImageView) findViewById(R.id.datuImg);
		daTuImg.setOnClickListener(this);
		openMenuBtn = (ImageButton) findViewById(R.id.openMenuBtn);
		openMenuBtn.setVisibility(View.GONE);
//		openMenuBtn.setImageResource(R.drawable.qf_btn3);/
		backBtn.setOnClickListener(this);
		openMenuBtn.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		}else if(v.getId() == R.id.closeWindowBtn){//关闭菜单按钮
			dismiss();
		}else if(v == daTuImg){//第一张图片的点击事件
			if(localListData.size()>0){
				Intent intent = new Intent(BeautyPhotosListActivity.this,ImageSwitcher.class);
				intent.putExtra("photoList", (Serializable)localListData);
				intent.putExtra("localSDUrl", ConstantsValues.BEAUTYPHOTOS_IMG_URL);
				intent.putExtra("index", 0);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//				finish();
			}else{
				Toast.makeText(this, "赞无您的相片，您可以点击右上角的照相机按钮获取图片后，上传到服务器！", 3000).show();
			}
			
		}
		super.onClick(v);
	}
    /**
     * UI处理
     */
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter(bottomGv, list2,4,daW);
			}else if(msg.what == 1){
				initData();//这个函数为界面制作专用函数
			}else if(msg.what == 7){//连接服务器失败
				
			}else if(msg.what == 8){//提交失败。
				
			}else if(msg.what == 9){//更新相册
				new Thread(getPhotoRunnable).start();
				
			}
			super.handleMessage(msg);
		}
    	
    };

	/**
	 * 网格布局控件监听器
	 * 
	 * @author qingfeng
	 * 
	 */
	class gvItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(BeautyPhotosListActivity.this,ImageSwitcher.class);
			intent.putExtra("photoList", (Serializable)localListData);
			intent.putExtra("localSDUrl", ConstantsValues.BEAUTYPHOTOS_IMG_URL);
			if(arg0 == topGv){
				intent.putExtra("index", arg2+1);
			}else if(arg0 == bottomGv){
				intent.putExtra("index", arg2+5);
			}
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//			finish();
		}
	}
	
	/***
	 * @author 刘星星
	 * @createDate 2013、9、18
	 * 获取照片id线程
	 */
	private Runnable getPhotoRunnable = new Runnable() {

		@Override
		public void run() {
				//检查系统更新机制表  看该用户的相册是否有更新
				String sqlTime = "select updatetime from "+PictureThemes.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()+"' and id='"+themeMap.get("id")+"'";
				List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
				String systemTimeStr = "";//最新更新时间
				if(systemList!=null && systemList.size()>0){
					Map<String,Object> map = systemList.get(0);
					systemTimeStr = map.get("updatetime").toString();
					getPhoto(systemTimeStr);//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
				}
		}
	};
	/**
	 * 访问服务器获取照片的ID 
	 * @param updateTime 最新更新时间
	 * @param localTime 上一次更新的时间
	 */
	private void getPhoto(String updateTime){
		if (NetworkCheck.IsHaveInternet(BeautyPhotosListActivity.this)) {//判断网络状态
			String msgStr = RequestServerFromHttp.getBeautyPhotos(themeMap.get("id").toString(), updateTime);
			if(msgStr.equals("404")){
				
			}else if(JsonData.isNoData(msgStr)){
				
			}else if(msgStr.startsWith("[") && msgStr.length()>3){
				JsonData.jsonBeautyPhotos(msgStr, dbHelper.open(),UserBeanInfo.getInstant().getCurrentStoreId(),themeMap.get("id").toString());
				handler.sendEmptyMessage(1);
			}
		}else{//没有网络
			
		}
	}
}
