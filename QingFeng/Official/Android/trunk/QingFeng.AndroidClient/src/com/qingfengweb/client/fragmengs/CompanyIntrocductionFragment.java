/**
 * 
 */
package com.qingfengweb.client.fragmengs;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.qingfengweb.android.R;
import com.qingfengweb.client.activity.BaseActivity;
import com.qingfengweb.client.activity.DetailCompanyIntroActivity;
import com.qingfengweb.client.activity.DetailServiceActivity;
import com.qingfengweb.client.activity.SubmitDemandActivity;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.ContentUpdateInfo;
import com.qingfengweb.client.bean.HomeImageInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;

/**
 * @author 刘星星
 * @createDate 2013/6/8
 *
 */
@SuppressLint("NewApi")
public class CompanyIntrocductionFragment extends Fragment implements View.OnClickListener{
	 private static final String ARG_TEXT = "net.simonvt.menudrawer.samples.SampleFragment.text12";
	 View view = null;
	 ImageButton leftButton;//打开菜单按钮
	 ImageButton rightButton;//关于按钮
	 LinearLayout pointsLayout;
	 ViewPager imagePager;
	 private ArrayList<View> pageViews = new ArrayList<View>();
	 EditText introView;//公司简介
	 LinearLayout item1Layout,item2Layout,item3Layout,item4Layout,item5Layout,item6Layout,item7Layout;//服务项目
//	 private ArrayList<String> photoList = new ArrayList<String>();//图片集合
	 private DBHelper dbHelper;
	 List<Map<String, Object>> contentList;//表示公司简介的内容。
	 ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	 public String imgIdLastUpdateTime = "";//团队数据的最后更新时间
		public String sysTime = "";//系统提供的最后更新时间  这个时间并不一定等于本地更新过的时间。
     public static CompanyIntrocductionFragment newInstance(String text) {
    	 CompanyIntrocductionFragment f = new CompanyIntrocductionFragment();

         Bundle args = new Bundle();
         args.putString(ARG_TEXT, text);
         f.setArguments(args);

         return f;
     }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.a_main, null);
		findview();
		initData();
	}
	
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		pageViews.clear();
		new Thread(getContentInfoRunnable).start();//获取内容列表更新内容的线程
		new Thread(getImageIdFromLocalRunnable).start();//获取首页图片的线程
	}
	Runnable getContentInfoRunnable = new Runnable() {
		@Override
		public void run() {
			String msg = AccessServer.getContents("", AccessServer.LIST_ACTION);
			if(AccessServer.isNoData(msg)){//无数据
				
			}else if(msg.equals("404")){//访问服务器失败
				
			}else{//访问服务器成功且有数据
				JsonData.jsonContentUpdateData(msg, getActivity(), dbHelper.open());
				String sql = "select * from "+ContentUpdateInfo.tableName+" where type = 7";
				contentList = dbHelper.selectRow(sql, null);
				handler.sendEmptyMessage(4);
			}
		}
	};
	/**
	 * 从本地查询图片id数据
	 */
	Runnable getImageIdFromLocalRunnable = new Runnable() {
		
		@Override
		public void run() {
			List<Map<String, Object>> list = dbHelper.selectAllRows(HomeImageInfo.TableName, "*", null);
			//查询更新表中的类型为3（ 即团队成员）的数据  判断上次更新的时间和系统更新的时间是否相等，
			String sql = "select * from "+UpdateSystemTime.tableName+" where type='2'";
			List<Map<String,Object>> updateList = dbHelper.selectRow(sql, null);
			String localTime = "";
			if(updateList!=null && updateList.size()>0){
				if(updateList.get(0).get("localtime")!=null && updateList.get(0).get("localtime").toString().length()>0){
					localTime = updateList.get(0).get("localtime").toString();
				}
				if(updateList.get(0).get("systime")!=null && updateList.get(0).get("systime").toString().length()>0){
					sysTime = updateList.get(0).get("systime").toString();
				}
			}
			if(list!=null && list.size()>0){//如果本地数据库有值
				Message msg1 = new Message();
				msg1.obj = list;
				msg1.what = 3;
				handler.sendMessage(msg1);
				
				//如果不相等则后台向服务器去更新数据。
				if(updateList!=null && updateList.size()>0){
					if(!localTime.equals(sysTime) || (localTime.equals("") && sysTime.equals(""))){////如果不相等则后台向服务器去更新数据。
						imgIdLastUpdateTime = localTime;//设置最后更新时间为上一次更新的时间，
						handler.sendEmptyMessage(1);//根据系统更新表中的数据判断团队成员数据是否有更新
					}
				}
			}else{
				imgIdLastUpdateTime = "";//设置最后更新时间为空，即如果数据库没有团队成员数据，不管更新表中的localTime和sysTime是否相等  都去服务器获取数据
				handler.sendEmptyMessage(1);
			}
		}
	};
	/**
	 * 从服务器下载图片的ID
	 */
	Runnable getImageIdFromServiceRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgStr = AccessServer.getContents(imgIdLastUpdateTime, AccessServer.HOME_IMAGE_ACTION);
			if(AccessServer.isNoData(msgStr)){
				System.out.println("获取首页图片ID数据时，服务器返回无数据或者参数错误。返回值为："+msgStr);
				handler.sendEmptyMessage(1);//无数据
			}else if(msgStr.equals("404")){
				System.out.println("获取首页图片ID数据时，访问服务器失败");
			}else{
				System.out.println("获取首页图片ID数据成功");
				JsonData.jsonHomeImageData(msgStr, getActivity(), dbHelper.open());
				List<Map<String, Object>> list = dbHelper.selectAllRows(HomeImageInfo.TableName, "*", null);
				if(list!=null && list.size()>0){
					ContentValues contentValues = new ContentValues();
					contentValues.put("localTime", sysTime);
					dbHelper.update(UpdateSystemTime.tableName, contentValues, "type=?", new String[]{"2"});
					Message msg1 = new Message();
					msg1.obj = list;
					msg1.what = 3;
					handler.sendMessage(msg1);
				}else{
					handler.sendEmptyMessage(2);
				}
			}
			
		}
	};
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//本地查询到了合格的数据
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notifyImagePager();
				}		
			}else if(msg.what == 1){//开始从服务器获取图片ＩＤ
				new Thread(getImageIdFromServiceRunnable).start();
			}else if(msg.what == 2){//从服务获取数据时   无数据
				Toast.makeText(getActivity(), "无数据", 3000).show();
			}else if(msg.what == 3){//取得图片id后显示图片控件并开始下载图片。
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) msg.obj;
				if(list!=null && list.size()>0){
					addPageView(list);//添加图片页面
					if(pageViews!=null && pageViews.size()>0){//如果图片控件添加成功
						handler.sendEmptyMessage(0);
					}else{//如果图片控件添加失败
						handler.sendEmptyMessage(2);
					}
				}else{
					handler.sendEmptyMessage(2);
				}
				
			}else if(msg.what ==4){//获取公司简介成功
				if(contentList!=null && contentList.size()>0){
					String content = contentList.get(0).get("summary").toString();
					if(content!=null && content.length()>0){
						introView.setText(content);
//						introView.setMovementMethod(ScrollingMovementMethod.getInstance());
					}
					
				}
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 根据得到的首页图片数据集合添加图片页
	 * @param list 装有图片数据的集合
	 */
	private void addPageView(List<Map<String, Object>> list){
		pageViews.clear();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			if(map!=null){
				ImageView imageView = new ImageView(getActivity());
				int id = Integer.parseInt(map.get("id").toString());
				File file = new File(FileUtils.SDPATH+FinalValues.HOME_IMG_URL+id+".png");
				if(file.exists()){//图片文件存在
					Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
					if(bitmap==null){//生成图片不成功
						file.delete();
						downImage(imageView,id);
					}else{
						int h = bitmap.getHeight();
						int w = bitmap.getWidth();
						int currentW = MyApplication.getInstant().getScreenW();
						int currentH = h*currentW/w;
						RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, currentH);
						imagePager.setLayoutParams(param);
						imageView.setImageBitmap(bitmap);
					}
				}else{//图片文件不存在
					downImage(imageView,id);
				}
				pageViews.add(imageView);
				if(list.size()>1){//添加表示图片数量的小点
					pointsLayout.addView(getPoint());
				}
			}
		}
		if (pointsLayout.getChildCount()>0) {//如果数量大于1  则把第一张图片高亮
			pointsLayout.getChildAt(0).setBackgroundResource(R.drawable.qf_dot2);
		}
	}
	/**
	 * 生成一个新的ImageView
	 * @param id
	 * @return
	 */
	private ImageView getImageView(int id){
		ImageView imageView = new ImageView(getActivity());
//		imageView.setImageResource(R.drawable.qf_photo);
		File file = new File(FileUtils.SDPATH+FinalValues.HOME_IMG_URL+id+".png");
		if(file.exists()){//图片文件存在
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			if(bitmap==null){//生成图片不成功
				file.delete();
				downImage(imageView,id);
			}else{
				imageView.setImageBitmap(bitmap);
			}
		}else{//图片文件不存在
			downImage(imageView,id);
		}
		return imageView;
	}
	/**
	 * 下载图片
	 * @param imageView
	 * @param id
	 */
	private void downImage(ImageView imageView,int id){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		CallbackImpl callbackImpl = new CallbackImpl(getActivity(),imageView);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", AccessServer.APPID));
		list.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
		list.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
		list.add(new BasicNameValuePair("imageid", String.valueOf(id)));
		list.add(new BasicNameValuePair("type", "1"));
		imageLoad.loadImageFromId(getActivity(), AccessServer.CONTENT_INTERFACE,
				list, R.drawable.qf_photo, FinalValues.HOME_IMG_URL, callbackImpl,true,false,MyApplication.getInstant().getScreenW()+20,0,0);
	}
	/**
	 * 得到一个表示图片数量的点
	 * @return
	 */
	private ImageView getPoint(){
		ImageView imageView = new ImageView(getActivity());
		imageView.setImageResource(R.drawable.qf_dot1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 5, 0);
		imageView.setLayoutParams(params);
		return imageView;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		if(v == leftButton){//控制菜单按钮
			BaseActivity.sm.showMenu();
		}else if (v == rightButton) {//关于按钮事件
			CustomViewAbove.isHaveRight = true;
//			BaseActivity.sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_right);
			BaseActivity.sm.showSecondaryMenu();
		}else if (v == introView) {//公司简介
			Intent intent = new Intent(getActivity(),DetailCompanyIntroActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item1Layout) {//ios
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item2Layout) {//android
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item3Layout) {//window
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "3");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item4Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "4");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item5Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "5");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item6Layout) {//html5
			Intent intent = new Intent(getActivity(),DetailServiceActivity.class);
			intent.putExtra("type", "6");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}else if (v == item7Layout) {//html5
			Intent intent = new Intent(getActivity(),SubmitDemandActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
		}
		
	}
	private void findview(){
		leftButton = (ImageButton) view.findViewById(R.id.intro_topLeftBtn);
		rightButton = (ImageButton) view.findViewById(R.id.intro_topRightBtn);
		imagePager =  (ViewPager) view.findViewById(R.id.MainImagePager);
		pointsLayout = (LinearLayout) view.findViewById(R.id.pointLayout);
		introView = (EditText) view.findViewById(R.id.introTv);
		item1Layout = (LinearLayout) view.findViewById(R.id.layoutItem1);
		item2Layout = (LinearLayout) view.findViewById(R.id.layoutItem2);
		item3Layout = (LinearLayout) view.findViewById(R.id.layoutItem3);
		item4Layout = (LinearLayout) view.findViewById(R.id.layoutItem4);
		item5Layout = (LinearLayout) view.findViewById(R.id.layoutItem5);
		item6Layout = (LinearLayout) view.findViewById(R.id.layoutItem6);
		item7Layout = (LinearLayout) view.findViewById(R.id.layoutItem7);
		introView.setOnClickListener(this);
		item1Layout.setOnClickListener(this);
		item2Layout.setOnClickListener(this);
		item3Layout.setOnClickListener(this);
		item4Layout.setOnClickListener(this);
		item5Layout.setOnClickListener(this);
		item6Layout.setOnClickListener(this);
		item7Layout.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}
	/**
	 * 刷新图片
	 */
	int oldx = 0;
	int nowx = 0;
	ViewPagerAdapter adapter = null;
	private void notifyImagePager(){
		adapter = new ViewPagerAdapter(pageViews);
		imagePager.setAdapter(adapter);
		imagePager.setOnPageChangeListener(new ImagePagerChangListener());
		imagePager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					oldx = (int) event.getX();
				}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					nowx = (int) event.getX();
					if(imagePager.getCurrentItem() == 0){
						if(nowx>oldx){//手指向右滑动 这个时候应该是要让侧边菜单出现的时候
							CustomViewAbove.currentPage =-1;
						}else{//手指向左边移动 这个时候是滚动图片的时候
							CustomViewAbove.currentPage =0;
						}
		        		
		        	}else if(imagePager.getCurrentItem() == pageViews.size()-1){
		        		CustomViewAbove.currentPage =2;
		        	}else{
		        		CustomViewAbove.currentPage =1;
		        	}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					CustomViewAbove.currentPage =-1;
				}
				return false;
			}
		});
	}
	/**
	 * 图片切换器的监听器
	 * @author 刘星星
	 * 
	 *
	 */
	class ImagePagerChangListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			for(int i=0;i<pointsLayout.getChildCount();i++){
				if(i == arg0){//将当前页的小点变色
					pointsLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_dot2);
				}else{
					pointsLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_dot1);
				}
			}
		}
		
	}
}
