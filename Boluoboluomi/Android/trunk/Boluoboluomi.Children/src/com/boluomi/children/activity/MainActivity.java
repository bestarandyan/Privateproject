/**
 * 
 */
package com.boluomi.children.activity;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.JsonData;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.data.UpdateType;
import com.boluomi.children.data.UserBeanInfo;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.StoreInfo;
import com.boluomi.children.model.SystemUpdateInfo;
import com.boluomi.children.model.UserInfo;

/**
 * @author 刘星星、武国庆
 * @createDate 2013/8/20
 * 程序主界面
 */
public class MainActivity extends BaseActivity implements OnClickListener{
	private LinearLayout layout1,layout2,layout3,layout7,layout10;//每一个功能单员控件   用来点击产生跳转事件
	private Button layout0Dot,layout1Dot,layout2Dot,layout3Dot,layout7Dot,layout10Dot;//显示数字的控件
	TextView companyName,phone/*,score*/;//公司名称   联系电话    积分
	Button cancleUser;//注销按钮
	ImageView topImg = null;//顶部的logo图片  这里是可以在后台改变图片的
	DBHelper dbHelper = null;
	String currentStoreId = "";//当前门店ID
	String currentUserId = "";//当前用户ID
	public List<Map<String,Object>> systemList = new ArrayList<Map<String,Object>>();//系统更新集合
	public Button[] btnArray = null;//
	ImageView explainIv;
	RelativeLayout bottomLinear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		dbHelper = DBHelper.getInstance(this);
		findview();
	}
	/**
	 * 系统更新机制线程
	 */
	Runnable systemUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+currentStoreId
					+"' and userid = '"+currentUserId+"' and type = " + UpdateType.ServerTime.getValue();
			systemList = dbHelper.selectRow(sqlTime, null);
			String systemTimeStr = "";
			if(systemList!=null && systemList.size()>0){
				handler.sendEmptyMessage(0);//处理UI 显示该显示的首页小圆圈
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
			}
			System.out.println("本次更新时间======"+systemTimeStr);
			if(currentStoreId==null || currentStoreId.equals("")){//如果没有门店ID则不进行对服务器的访问
				return;
			}
			String msgStr = RequestServerFromHttp.systemUpdate(currentStoreId, systemTimeStr);//请求服务器获取更新内容
			if(msgStr.equals("404")){//访问服务器失败
				System.out.println("本次系统更新接口访问服务器失败");
			}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
				System.out.println("本次系统更新接口返回无数据");
			}else{//请求成功并且有有效数据
				JsonData.jsonUpdateTimeData(msgStr, dbHelper.open());//解析数据并将数据保存到数据库
				String sql = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+currentStoreId+"' and userid = '"+currentUserId+"'";
				systemList = dbHelper.selectRow(sql, null);
				System.out.println(systemList.size()+"");
			}
			handler.sendEmptyMessage(0);//处理UI
		}
	};
	/**
	 * 获取门店详情
	 */
	Runnable getSotreDetailRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(currentStoreId.length()==0 || currentStoreId.equals("")){
					return ;
				}
				String sql = "select * from "+StoreInfo.TableName+" where id="+currentStoreId;
				UserBeanInfo.storeDetail = dbHelper.selectRow(sql, null);//查询本地数据库是否有门店详情数据
				if(UserBeanInfo.storeDetail.size()>0){//如果本地有数据   先检查是否需要更新，
//					handler.sendEmptyMessage(3);
					String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+currentStoreId+
							"' and userid = '"+currentUserId+"' and type='"+UpdateType.Store.getValue()+"'";
					List<Map<String,Object>> list = dbHelper.selectRow(sqlTime, null);
					String updateTime = "";//最新更新的时间
						if(list.size()>0){
							updateTime = list.get(0).get("updatetime").toString();//最新更新的时间
						}
					String localTime = "";//更新过门店详情的时间
					if(list.get(0).get("localtime")!=null){
						localTime = list.get(0).get("localtime").toString();//更新过门店详情的时间
					}
					if(!updateTime.equals(localTime)){
						getStoreDetail(localTime,updateTime);//如果本地数据库没有门店详情数据 直接访问服务器获取
					}
				}else{
					getStoreDetail("","");//如果本地数据库没有门店详情数据 直接访问服务器获取
				}
				
			}
		};
		/**
		 * 从服务器获取门店详情
		 */
		private void getStoreDetail(String localTime,String updateTime){
			String msgStr = RequestServerFromHttp.getStoreDetail(currentStoreId);
			if(msgStr.equals("404")){//访问服务器失败
				
			}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
				
			}else if(msgStr.startsWith("{")){//请求成功并且有有效数据
				JsonData.jsonSotreDetailData(msgStr, dbHelper.open(),UserBeanInfo.getInstant().getCurrentCityId());//解析数据并将数据保存到数据库
				String sql = "select * from "+StoreInfo.TableName+" where id="+currentStoreId;
				UserBeanInfo.storeDetail = dbHelper.selectRow(sql, null);
				if(!updateTime.equals("")){
					ContentValues values = new ContentValues();
					values.put("localtime", updateTime);
					dbHelper.update(SystemUpdateInfo.TableName, values, "storeid=? and type =? and userid=?", 
							new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),UpdateType.Store.getValue(),UserBeanInfo.getInstant().getUserid()});
				}
				System.out.println(UserBeanInfo.storeDetail.size()+"");
//				handler.sendEmptyMessage(2);
			}
		}
		/**
		 * UI处理
		 */
		Handler handler  = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0){//改变首页数字图标按钮的数字
					new Thread(getSotreDetailRunnable).start();
					for(int i=0;i<systemList.size();i++){
						int type = Integer.parseInt(systemList.get(i).get("type").toString());
						String number = systemList.get(i).get("number").toString();
						System.out.println("type========"+type+":"+number);
//						if(!number.equals("0")){
//							btnArray[type].setVisibility(View.VISIBLE);
//							btnArray[type].setText(number);
//						}else{
//							btnArray[type].setVisibility(View.GONE);
//						}
					}
				}else if(msg.what == 1){//注销用户
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, LoginActivity.class);
					intent.putExtra("activityType", 0);
					startActivity(intent);
				}else if(msg.what == 2){//首页图片下载
					if(currentStoreId.length()==0 || currentStoreId.equals("")){
						return ;
					}
					String imgId = UserBeanInfo.storeDetail.get(0).get("store_logo").toString();
					String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.HOME_IMG_URL+imgId+".png";
					File file = new File(fileUrl);
					if(file.exists()){
						Bitmap bitmap = BitmapFactory.decodeFile(fileUrl);
						topImg.setImageBitmap(bitmap);
					}else{
						if(imgId!=null && !imgId.equals("")){
							topImg.setBackgroundResource(R.drawable.id_logo);
							RequestServerFromHttp.downImage(MainActivity.this,topImg,imgId,ImageType.HomeImage.getValue(),ImgDownType.BigBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()+"","0",true,ConstantsValues.HOME_IMG_URL,R.drawable.id_logo);
						}
					}
					
				}else if(msg.what == 3){//设置首页图片
					if(UserBeanInfo.storeDetail.size()>0){//如果本地有数据   先检查是否需要更新，
						String homeImgName = "";
						if(UserBeanInfo.storeDetail.get(0).get("store_logo")!=null){
							homeImgName = UserBeanInfo.storeDetail.get(0).get("store_logo").toString();
							String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.HOME_IMG_URL+homeImgName+".png";
							File file = new File(fileUrl);
							if(file.exists()){
								Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
								BitmapDrawable background = new BitmapDrawable(bitmap);
								topImg.setBackgroundDrawable(background);
							}
						}
					}
				}
				super.handleMessage(msg);
			}
			
		};
	/**
	 *  当点击了主页其中一项时更新首页数字
	 * @param type 更新的类型
	 * @author 刘星星
	 * @return
	 */
	private boolean updateNumber(String type){
		ContentValues cv = new ContentValues();
		cv.put("number", "0");
		return dbHelper.update(SystemUpdateInfo.TableName, cv, "storeid=? and type =? and userid=?", 
				new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),type,UserBeanInfo.getInstant().getUserid()});
	}
	@Override
	public void onClick(View v) {
		final Intent i = new Intent();
		if(v == layout1){//店家首页
			updateNumber(UpdateType.Store.getValue());
			layout1Dot.setVisibility(View.GONE);
			i.setClass(this,DianJiaMainActivity.class);
			startActivity(i);
		}else if(v == layout2){//我的相册
			if(UserBeanInfo.getInstant().isLogined){
				updateNumber(UpdateType.UserAlbum.getValue());//将数字变为0
				layout2Dot.setVisibility(View.GONE);
				i.setClass(this,AlbumMainActivity.class);
				startActivity(i);
			}else{
				i.setClass(this,LoginActivity.class);
				i.putExtra("activityType", 2);
				startActivity(i);
			}
		}else if(v == layout3){//样照欣赏
			updateNumber(UpdateType.StoreAlbum.getValue());
			layout3Dot.setVisibility(View.GONE);
			i.setClass(this,BeautyPhotoAdmireActivity.class);
			startActivity(i);
		}else if(v == layout7){//电子请帖
			updateNumber(UpdateType.Invitation.getValue());
			layout7Dot.setVisibility(View.GONE);
			i.setClass(this, InvitationExplainActivity.class);
			startActivity(i);
		}else if(v == layout10){//百宝箱
			updateNumber(UpdateType.Partner.getValue());
			layout10Dot.setVisibility(View.GONE);
			i.setClass(this, TreasureChestActivity.class);
			startActivity(i);
		}else if(v == cancleUser){//注销用户
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(R.string.prompt)
					.setMessage(R.string.user_message_cancle)
					.setPositiveButton(R.string.comfrim,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									new Thread(cancleUserRunnable).start();//耗时操作交给线程处理
								}
							}).setNegativeButton(R.string.cancel, null);
			callDailog.show();
		}else if(v == explainIv){//使用说明
			Intent intent = new Intent(this,HelpListActivity.class);
			startActivity(intent);
		}else if(v == companyName){//门店名称
			MyApplication.getInstant().setSelectCityFromMainActivity(true);
			Intent intent = new Intent(this,CitySelectActivity.class);
			startActivity(intent);
			finish();
		}
		super.onClick(v);
	}
	/**
	 * 注销用户线程
	 * 将数据库的自动登陆改成不是自动登陆  要不然再进入软件的时候不会跳入登陆界面
	 */
	Runnable cancleUserRunnable = new Runnable() {
		
		@Override
		public void run() {
			ContentValues cv = new ContentValues();
			cv.put("isautologin", "0");//将该用户设置为不是自动登陆
			boolean b = dbHelper.update(UserInfo.TableName, cv, "userid=?", new String[]{UserBeanInfo.getInstant().getUserid()});
			MyApplication.getInstant().setUserinfo(null);
			UserBeanInfo.getInstant().setLogined(false);
			handler.sendEmptyMessage(1);
		}
	};
	private void findview(){
		layout1 = (LinearLayout) findViewById(R.id.btnLayout1);
		layout2 = (LinearLayout) findViewById(R.id.btnLayout2);
		layout3 = (LinearLayout) findViewById(R.id.btnLayout3);
		layout7 = (LinearLayout) findViewById(R.id.btnLayout7);
		layout10 = (LinearLayout) findViewById(R.id.btnLayout10);
		explainIv = (ImageView) findViewById(R.id.explainIv);
		bottomLinear = (RelativeLayout) findViewById(R.id.bottomLinear);
		bottomLinear.getBackground().setAlpha(180);
		explainIv.setOnClickListener(this);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout3.setOnClickListener(this);
//		layout4.setOnClickListener(this);
		layout7.setOnClickListener(this);
		layout10.setOnClickListener(this);
		topImg = (ImageView) findViewById(R.id.topImg);
		layout0Dot = new Button(this);
		layout1Dot = (Button) findViewById(R.id.layout1Dot);
		layout2Dot = (Button) findViewById(R.id.layout2Dot);
		layout3Dot = (Button) findViewById(R.id.layout3Dot);
//		layout4Dot = (Button) findViewById(R.id.layout4Dot);
		layout7Dot = (Button) findViewById(R.id.layout7Dot);
		layout10Dot = (Button) findViewById(R.id.layout10Dot);
		companyName = (TextView) findViewById(R.id.company);
		companyName.setText(UserBeanInfo.getInstant().getCurrentStore());
		phone = (TextView) findViewById(R.id.phone);
		cancleUser = (Button) findViewById(R.id.cancleUser);
		cancleUser.setOnClickListener(this);
		if(UserBeanInfo.getInstant().isLogined()){
			phone.setText(UserBeanInfo.getInstant().getUserName());
		}else{
			phone.setVisibility(View.GONE);
			cancleUser.setVisibility(View.GONE);
		}
		
		companyName.setOnClickListener(this);
		btnArray = new Button[]{layout0Dot,layout1Dot,layout2Dot,layout3Dot,layout10Dot,layout7Dot};//
	}
	
	@Override
	protected void onResume() {
		currentStoreId = UserBeanInfo.getInstant().getCurrentStoreId();
		currentUserId = UserBeanInfo.getInstant().getUserid();
		if(companyName!=null){
			companyName.setText(UserBeanInfo.getInstant().getCurrentStore());
		}
		if(UserBeanInfo.getInstant().isLogined()){
			phone.setVisibility(View.VISIBLE);
			cancleUser.setVisibility(View.VISIBLE);
			phone.setText(UserBeanInfo.getInstant().getUserName());
		}else{
			phone.setVisibility(View.GONE);
			cancleUser.setVisibility(View.GONE); 
		}
		new Thread(systemUpdateRunnable).start();
		super.onResume();
	}
	/**
	 * 控制主页的显示消息条数的控件
	 * @param view
	 * @param value
	 */
	private void setDotValue(Button view,int value){
		if(value > 0){
			view.setVisibility(View.VISIBLE);
			view.setText(value+"");
		}else{
			view.setVisibility(View.GONE);
		}
	}
	/***
	 * author:Ring 
	 * 逻辑数据处理判断是否显示主页下面信息栏
	 */
	public boolean isShowBottom() {
		UserInfo userinfo = MyApplication.getInstant().getUserinfo();
		if (userinfo != null) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return false;
	}
}
