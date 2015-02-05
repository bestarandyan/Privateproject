/**
 * 
 */
package com.boluomi.children.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.boluomi.children.R;
import com.boluomi.children.adapter.GridViewAdapter;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.JsonData;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.data.UpdateType;
import com.boluomi.children.data.UserBeanInfo;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.PictureInfo;
import com.boluomi.children.model.PictureThemes;
import com.boluomi.children.model.SystemUpdateInfo;
import com.boluomi.children.model.UserPhotoInfo;
import com.boluomi.children.network.NetworkCheck;
import com.boluomi.children.util.CommonUtils;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星
 * @createDate 2013、10、15
 * 美图欣赏照片列表界面
 *
 */
@SuppressLint("ShowToast")
public class BeautyPhotosListActivity extends BaseActivity{
	private Button backBtn;// 返回按钮
	private GridView bottomGv;// 图片网格控件
	private List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();//缩略图
	private ImageView daTuImg;
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
		daW = (MyApplication.getInstant().getWidthPixels()-13)/2-3;
		firstImgLayout.setVisibility(View.VISIBLE);
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
			String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.BEAUTYPHOTOS_IMG_URL+firstPhoto+".png";
			if(firstPhoto.equals("")){
				firstPhotoUrl = localListData.get(0).get("imgpath").toString();
			}
			if(new File(firstPhotoUrl).exists()){//如果该图片存在 则直接显示该图片
				int w = MyApplication.getInstant().getWidthPixels();
				Bitmap bitmap = PicHandler.getDrawable(firstPhotoUrl, daTuImg);
				bitmap = PicHandler.cutImg(bitmap, w, 250);
				daTuImg.setImageBitmap(bitmap);
			}else{//如果不存在 则去服务器下载
				if(firstPhoto!=null && !firstPhoto.equals("")){
					RequestServerFromHttp.downImage(BeautyPhotosListActivity.this,daTuImg,firstPhoto,ImageType.beautyPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.BEAUTYPHOTOS_IMG_URL_THUMB,R.drawable.photolist_defimg);
				}
			}
			list2.clear();
			if(localListData.size()>1){//大于5则代表会有三个部分
				for(int i=1;i<localListData.size();i++){
					Map<String, Object> map = localListData.get(i);
						list2.add(map);
				}
			}
			if(list2.size()>0){
				notifyAdapter(bottomGv, list2,daW);
			}
			
		}
	}
	/**
	 * 给gv控件加载适配器
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	private void notifyAdapter(GridView gv,List<Map<String, Object>> list,int daW) {
		int h = list.size()/3+(list.size()%3>0?1:0);//在垂直方向上有多少行
		int util = (MyApplication.getInstant().getWidthPixels()-8)/3;//单位高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,h*(util+2));
		params.setMargins(2, 0, 2, 2);
		gv.setLayoutParams(params);
		adapter = new GridViewAdapter(this, list,util);
		gv.setAdapter(adapter);
		gv.setCacheColorHint(0);
	}

	/**
	 * 初始化控件函数
	 */
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		bottomGv = (GridView) findViewById(R.id.bottomgv);
		bottomGv.setOnItemClickListener(new gvItemListener());
		bottomGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		firstImgLayout = (RelativeLayout) findViewById(R.id.firstImgLayout);
		daTuImg = (ImageView) findViewById(R.id.datuImg);
		daTuImg.setOnClickListener(this);
		backBtn.setOnClickListener(this);
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
				notifyAdapter(bottomGv, list2,daW);
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
			if(arg0 == bottomGv){
				intent.putExtra("index", arg2+1);
			}
			startActivity(intent);
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
