/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.AutoCompleteAdapter;
import com.zhihuigu.sosoOffice.Adapter.LoupanListAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.ReleaseNewRoomActivity.dialogListItemClickListener;
import com.zhihuigu.sosoOffice.View.AdvancedAutoCompleteTextView;
import com.zhihuigu.sosoOffice.View.AreaSelectView;
import com.zhihuigu.sosoOffice.View.MyScrollView;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.KeywordInfo;
import com.zhihuigu.sosoOffice.model.Messages;
import com.zhihuigu.sosoOffice.model.SoSoMetroInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
import com.zhihuigu.sosoOffice.utils.ZoneUtil;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

/**
 * @author 刘星星
 * @createDate 2013/1/29 精确搜索类
 * 
 */
public class AccurateSearchActivity extends BaseActivity implements
		Activity_interface {
	LinearLayout priceLayout, acreageLayout;
	private Button backBtn, searchBtn;
	private Button roomTypeSpinner,// 房源类型
			citySpinner, areaSpinner, businessSpinner;// 城市 区 商圈
//			metroSpinner;// 地铁
	private TextView keytEt;// 关键字
	private ImageView cancleBtn;//清空关键字按钮
//	private AutoCompleteTextView tv;
	private AreaSelectView priceAreaSelect = null;// 价格选择
	// private AreaSelectView acreageAreaSelect = null;//面积上下限选择
	private TextView minPrice, maxPrice/* ,minAcreage,maxAcreage */;// 价格以及面积的上限下限
	public MyScrollView scrollView;
	public EditText minAcreageEt, maxAcreageEt;

//	private ArrayList<HashMap<String, String>> keytList = new ArrayList<HashMap<String, String>>();// 关键字集合
	private ArrayList<HashMap<String, String>> roomTypeList = new ArrayList<HashMap<String, String>>();// 房源类型集合
	private ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();// 城市集合
	private ArrayList<HashMap<String, String>> acreageList = new ArrayList<HashMap<String, String>>();// 区域集合
	private ArrayList<HashMap<String, String>> businessList = new ArrayList<HashMap<String, String>>();// 商圈集合
	private ArrayList<HashMap<String, String>> metroList = new ArrayList<HashMap<String, String>>();// 地铁集合
	private ListView dialogListView;
	private TextView noText;
	private TextView dialogTitle;
	private Button dialogBackBtn;
	private Button dialogCloseBtn;
	private LinearLayout parent = null;
	private PopupWindow selectPopupWindow = null;
	private int dialog_flag = 0;// 依次代表弹出框表示的是什么 1代表关键字 2类型 3城市 4区 5商圈 6地铁
	private RelativeLayout titleLayout;

	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private String cityid = "";// 城市id
	private String cityname = "";// 城市名称
	private String zoneid = "";// 区域id
	private String zonename = "";// 区域名称
	private String businessid = "";// 商圈id
	private String businessname = "";// 商圈名称
	private String officetype = "";// 房源类型
	private String officetypename = "";// 房源类型名称
//	private String metroid = "";// 线路id
//	private String metroname = "";// 线路名称
	private String priceup = "20";// 价格上限
	private String pricedown = "0";// 价格下限
	private String areaup = "100000";// 面积上限
	private String areadown = "0";// 面积下限
	private String keywordstr = "";// 关键字
//	ArrayList<String> mOriginalValues = new ArrayList<String>();// 关键字集合
	
	
	public ArrayList<HashMap<String,String>> metroSelectList = new ArrayList<HashMap<String,String>>();//选中的地铁的集合
	public StringBuffer metroStr = null;//选择的地铁线拼接字符串
	public ArrayList<Messages> messageList = new ArrayList<Messages>();//保存被选中的地铁沿线
	private String latitude_zone= "";
	private String longitude_zone = "";
	private String latitude_business = "";
	private String longitude_business = "";
	private String userid = "0";
	// private LoupanListAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_accuratesearch);
		
		if(MyApplication.getInstance().getSosouserinfo()!=null&&
				MyApplication.getInstance().getSosouserinfo().getUserID()!=null){
			userid =  MyApplication.getInstance(this)
					.getSosouserinfo(this).getUserID() ;
		}
		findView();
		initView();
		initData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		setSearchBundle();//保存数据
		super.onPause();
	}
	/**
	 * 在点击搜索按钮后先将搜索条件保存起来，在返回时赋值
	 */
	private void setSearchBundle(){
		Bundle b = new Bundle();
		b.putString("cityid", cityid);
		b.putString("cityname", citySpinner.getText().toString());// 城市名称
		b.putString("zoneid", zoneid);// 区域id
		b.putString("zonename", areaSpinner.getText().toString());// 区域名称
		b.putString("businessid", businessid);// 商圈id
		b.putString("businessname", businessSpinner.getText().toString());// 商圈名称
		b.putString("officetype", officetype);
		// 房源类型
		b.putString("officetypename", roomTypeSpinner.getText().toString());
		// 房源类型名称
//		b.putString("metroid", metroid);
//		// 线路id
//		b.putString("metroname", metroSpinner.getText().toString());
		// 线路名称
		b.putString("priceup", priceAreaSelect.maxText.getText().toString());
		// 价格上限
		b.putString("pricedown", priceAreaSelect.minText.getText().toString());
		// 价格下限
		b.putString("areaup", maxAcreageEt.getText().toString());
		// 面积上限
		b.putString("areadown", minAcreageEt.getText().toString());
		// 面积下限
		b.putString("keywordstr", keytEt.getText()
				.toString());
		if(businessid.equals("")){
			b.putString("latitude_business", "");
			;// 面积下限
			b.putString("longitude_business", "");
			;// 关键字
		}else{
			b.putString("latitude_business", latitude_business);
			;// 面积下限
			b.putString("longitude_business", longitude_business);
			;// 关键字
		}
		
		if(zoneid.equals("")){
			b.putString("latitude_zone", "");
			b.putString("longitude_zone", "");
		}else{
			b.putString("latitude_zone", latitude_zone);
			b.putString("longitude_zone", longitude_zone);
		}
		
		MyApplication.getInstance().setSearchbundle(b);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().isSearchRoomBackVisibility()){
			backBtn.setVisibility(View.VISIBLE);
		}else{
			backBtn.setVisibility(View.GONE);
		}
		Bundle b = MyApplication.getInstance().getSearchbundle();
		if (b != null) {
			cityid = b.getString("cityid");
			cityname = b.getString("cityname");// 城市名称
			zoneid = b.getString("zoneid");// 区域id
			zonename = b.getString("zonename");// 区域名称
			if (zonename != null && zonename.length() == 0)
				zonename = "不限";
			businessid = b.getString("businessid");// 商圈id
			businessname = b.getString("businessname");// 商圈名称
			if (businessname != null && businessname.length() == 0)
				businessname = "不限";
			officetype = b.getString("officetype");// 房源类型
			officetypename = b.getString("officetypename");// 房源类型名称
//			metroid = b.getString("metroid");// 线路id
//			metroname = b.getString("metroname");// 线路名称
//			if (metroname != null && metroname.length() == 0)
//				metroname = "请选择地铁线路";
			priceup = b.getString("priceup");// 价格上限
			pricedown = b.getString("pricedown");// 价格下限
			areaup = b.getString("areaup");// 面积上限
			areadown = b.getString("areadown");// 面积下限
			keywordstr = b.getString("keywordstr");// 关键字
			latitude_business = b.getString("latitude_business");
			longitude_business = b.getString("longitude_business");
			latitude_zone = b.getString("latitude_zone");
			longitude_zone = b.getString("longitude_zone");
			if (MyApplication.getInstance().getCurrentKeyt().equals("")) {
				keytEt.setText("");
			} else {
				keytEt.setText(keywordstr);
			}
			keytEt.setText(keywordstr);
			if (keytEt.getText().toString().trim().length() > 0) {
				cancleBtn.setVisibility(View.VISIBLE);
			} else {
				cancleBtn.setVisibility(View.GONE);
			}
			roomTypeSpinner.setText(officetypename);
			citySpinner.setText(cityname);
			areaSpinner.setText(zonename);
			businessSpinner.setText(businessname);
			maxAcreageEt.setText(areaup);
			minAcreageEt.setText(areadown);
//			metroSpinner.setText(metroname);

		} else {
			if (MyApplication.getInstance().getCurrentKeyt().equals("")) {
				keytEt.setText("");
				cancleBtn.setVisibility(View.GONE);
				roomTypeSpinner.setText("");
				maxAcreageEt.setText("");
				minAcreageEt.setText("");
//				metroSpinner.setText("请选择地铁沿线");
				String cityStr = MyApplication.getInstance().getCityname();
				citySpinner.setText(cityStr);
				cityid = MyApplication.getInstance().getCityid();
				initAcreageListData();
				areaSpinner.setText("不限");
				businessSpinner.setText("不限");
				zoneid = "";
				zonename="";
				businessid = "";
				businessname = "";
				latitude_business="";
				longitude_business="";
				latitude_zone="";
				longitude_zone="";
//				if (acreageList.size() > 1) {
//					areaSpinner.setText(acreageList.get(1).get("name")
//							.toString());
//					zoneid = acreageList.get(1).get("id").toString();
//					latitude_zone = acreageList.get(1).get("latitude")
//							.toString();
//					longitude_zone = acreageList.get(1).get("longitude")
//							.toString();
//				} else {
//					areaSpinner.setText("不限");
//					zoneid = "";
//				}
//				initBusinessListData();
//				if (businessList.size() > 1) {
//					businessSpinner.setText(businessList.get(1).get("name")
//							.toString());
//					businessid = businessList.get(1).get("id").toString();
//					latitude_business = businessList.get(1)
//							.get("latitude").toString();
//					longitude_business = businessList.get(1)
//							.get("longitude").toString();
//				} else {
//					businessSpinner.setText("不限");
//					businessid = "";
//				}
				// keytEt.setText("");
				// cancleBtn.setVisibility(View.GONE);
				// roomTypeSpinner.setText("");
				// maxAcreageEt.setText("");
				// minAcreageEt.setText("");
				// metroSpinner.setText("请选择地铁沿线");
				// String cityStr = MyApplication.getInstance().getCityname();
				// citySpinner.setText(cityStr);
				// cityid = MyApplication.getInstance().getCityid();
				// initAcreageListData();
				// if (acreageList.size() > 1) {
				// areaSpinner.setText(acreageList.get(1).get("name").toString());
				// zoneid = acreageList.get(1).get("id").toString();
				// latitude = acreageList.get(1).get("latitude").toString();
				// longitude = acreageList.get(1).get("longitude").toString();
				// } else {
				// areaSpinner.setText("不限");
				// zoneid = "";
				// }
				// initBusinessListData();
				// if (businessList.size() > 1) {
				// businessSpinner.setText(businessList.get(1).get("name")
				// .toString());
				// businessid = businessList.get(1).get("id").toString();
				// latitude = businessList.get(1).get("latitude").toString();
				// longitude = businessList.get(1).get("longitude").toString();
				// } else {
				// businessSpinner.setText("不限");
				// businessid = "";
				// }
				// >>>>>>> .r14457
			}
		}
		new Thread(priceRunnable).start();
		roomTypeSpinner.setFocusable(true);
		roomTypeSpinner.setFocusableInTouchMode(true);
		roomTypeSpinner.requestFocus();
		super.onResume();
	}

	@Override
	public void findView() {
		priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
		acreageLayout = (LinearLayout) findViewById(R.id.acreageLayout);
		scrollView = (MyScrollView) findViewById(R.id.scrollView);
		 backBtn = (Button) findViewById(R.id.backBtn);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		keytEt = (TextView) findViewById(R.id.keytEt);
		roomTypeSpinner = (Button) findViewById(R.id.typeSpinner);
		citySpinner = (Button) findViewById(R.id.citySpinner);
		areaSpinner = (Button) findViewById(R.id.areaSpinner);
		businessSpinner = (Button) findViewById(R.id.businessSpinner);
//		metroSpinner = (Button) findViewById(R.id.metroSpinner);
		minAcreageEt = (EditText) findViewById(R.id.minAcreageEt);
		maxAcreageEt = (EditText) findViewById(R.id.maxAcreageEt);
		cancleBtn = (ImageView) findViewById(R.id.cancleBtn);

	}

	Handler handlerDialog = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				return;
			}else{
				dialog_flag = 2;
				initDialogWindow();
				initRoomTypeListData();// 初始化楼盘数据
			}
			
			super.handleMessage(msg);
		}

	};
	Runnable runnableDialog = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				handlerDialog.sendEmptyMessage(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	@Override
	public void onClick(View v) {
		  if (v == backBtn) {
			  CommonUtils.hideSoftKeyboard(this);
			  MyApplication.getInstance().setCurrentKeyt("");
			  MyApplication.getInstance().setSearchbundle(null);
			  if (MyApplication.getInstance().getRoleid() == Constant.TYPE_CLIENT) { 
				  MyApplication.getInstance().setSearchRoomBackVisibility(false);
				  MainTabActivity.mTabHost.setCurrentTab(0); 
					  }else { 	// 要跳转的界面 
			Intent intent = new Intent(this, MainActivity.class)
				 .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 把一个Activity转换成一个View
				  Window w = MainFirstTab.group.getLocalActivityManager()
				 .startActivity("MainActivity", intent); View view = w.getDecorView();
				 // 把View添加大ActivityGroup中 
				 MainFirstTab.group.setContentView(view);
				  }
		  }else	if (v == searchBtn) {
			Intent i = new Intent();
			i.putExtra("whereclause", getWhereClause());
			i.putExtra("districtid", businessid);
			i.putExtra("officetype", officetype);
			i.putExtra("cityid", cityid);
			i.putExtra("areaid", zoneid);
			i.putExtra("keyword", keytEt.getText().toString());
			i.putExtra("priceup", priceAreaSelect.maxText.getText().toString());
			i.putExtra("pricedown", priceAreaSelect.minText.getText()
					.toString());
			i.putExtra("areaup", maxAcreageEt.getText().toString());
			i.putExtra("areadown", minAcreageEt.getText().toString());
			i.putExtra("businessname", businessSpinner.getText());
			if(!businessid.equals("")){
				i.putExtra("longitude",longitude_business);
				i.putExtra("latitude",latitude_business) ;
			}else if(!zoneid.equals("")){
				i.putExtra("longitude",longitude_zone);
				i.putExtra("latitude",latitude_zone) ;
			}else{
				i.putExtra("longitude","");
				i.putExtra("latitude","") ;
			}
			
			i.setClass(AccurateSearchActivity.this,
					SearchRoomsListActivity.class);
			if (MyApplication.getInstance().getRoleid() == Constant.TYPE_AGENCY) {
				MyApplication.getInstance().setSearchBack(true);
			}
			setSearchBundle();//保存数据
			AccurateSearchActivity.this.startActivity(i);
		} else if (v == roomTypeSpinner) {// 房源类型
			CommonUtils.hideSoftKeyboard(this);// 隐藏可能弹出来了的软键盘
			new Thread(runnableDialog).start();

		} else if (v == citySpinner) {// 城市列表
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				return;
			}else{
				dialog_flag = 3;
				initCityListData();
				initDialogWindow();
				notifyAdapter(1);
			}
		} else if (v == areaSpinner) {// 区列表
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				return;
			}else{
				dialog_flag = 4;
				initAcreageListData();
				initDialogWindow();
				notifyAdapter(2);
			}
		} else if (v == businessSpinner) {// 商圈列表
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				return;
			}else{
				dialog_flag = 5;
				initBusinessListData();
				initDialogWindow();
				notifyAdapter(3);
			}
			

		}/* else if (v == metroSpinner) {// 地铁列表
			CommonUtils.hideSoftKeyboard(this);
			new Thread(runnable).start();
		} */else if (v == dialogCloseBtn) {
			dismiss();
		}else if(v == dialogBackBtn){//完成选择地铁线按钮
//			metroList.get(arg2).get("name").toString());
//			metroid = metroList.get(arg2).get("id").toString();
//			metroSelectList.clear();
//			metroStr = new StringBuffer();
//			for(int i=0;i<MetroAdapter.messageList.size();i++){
//				if(MetroAdapter.messageList.get(i).isChecked){
//					metroSelectList.add(metroList.get(i));
//					metroStr.append(metroList.get(i).get("name").toString()+",");
//				}
//			}
//			messageList = MetroAdapter.messageList;
//			if(metroStr!=null&&metroStr.length()>0){
//				metroSpinner.setText(metroStr.toString().substring(0, metroStr.toString().length()-1));
//			}else{
//				metroSpinner.setText("请选择地铁沿线");
//			}
			dismiss();
		}else if(v == keytEt){//关键字
			
			Intent intent = new Intent(this,InputSelectActivity.class);
			intent.putExtra("back", true);
			intent.putExtra("content", keytEt.getText().toString());
			startActivityForResult(intent, 1);
		}else if(v == cancleBtn){
			keytEt.setText("");
			cancleBtn.setVisibility(View.GONE);
			MyApplication.getInstance().setCurrentKeyt("");
		}
		super.onClick(v);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(data!=null){
				keytEt.setText(data.getStringExtra("keyt").toString());
				if(keytEt.getText().toString().trim().length()>0){
					cancleBtn.setVisibility(View.VISIBLE);
				}else{
					cancleBtn.setVisibility(View.GONE);
				}
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 整理的搜索字段字符
	 * 
	 * 作者：Ring 创建于：2013-2-28
	 * 
	 * @return
	 */
	public String getWhereClause() {
		StringBuffer whereclause = new StringBuffer("");
		String keystring = keytEt.getText()
				.toString();
		String areaup = maxAcreageEt.getText().toString();
		String areadown = minAcreageEt.getText().toString();
		String priceup = priceAreaSelect.maxText.getText().toString();
		String pricedown = priceAreaSelect.minText.getText().toString();
		whereclause.append(" ischecked=1 and isrent=0 and");
		if (areaup != null && !areaup.trim().equals("")) {
			whereclause.append(" AreaUP<='" + areaup + "' and");
		}
		if (areadown != null && !areadown.trim().equals("")) {
			whereclause.append(" AreaUP>='" + areadown + "' and");
		}
		
		Double priceup_ = 0.0;
		try{
			priceup_ = Double.parseDouble(priceup);
		}catch(Exception e){
			
		}
		if(priceup_<20&&priceup_>0){
			whereclause.append(" PriceUP<='"+priceup+"' and");
		}
		Double pricedown_ = 0.0;
		try{
			pricedown_ = Double.parseDouble(pricedown);
		}catch(Exception e){
			
		}
		if(pricedown_>0&&pricedown_<20){
			whereclause.append(" PriceUP>='"+pricedown+"' and");
		}
		if (officetype != null && !officetype.trim().equals("")) {
			whereclause.append(" OfficeType='" + officetype + "' and");
		}
		whereclause.append(" BuildID in ( select BuildID from TBuild where");
		if (businessid != null && !businessid.trim().equals("")) {
			whereclause.append(" DistrictID='" + businessid + "' and");
		}
		if(zoneid!=null&&!zoneid.trim().equals("")){
			whereclause.append(" AreaID='"+zoneid+"' and");
		}
//		String metroid = getMetros();
//		if(metroid!=null&&metroid.length()>0){
//			whereclause.append(metroid+" and");
//		}
//		this.metroid = metroid;
		if (keystring != null && !keystring.trim().equals("")) {
			if (cityid != null && !cityid.trim().equals("")) {
				whereclause.append(" CityID='" + cityid + "') and");
			}
			whereclause.append(" ((Keywords like '%" + keystring + "%' or");
			whereclause.append(" FYJJ like '%" + keystring + "%') or");
			whereclause.append(" BuildID in ( select BuildID from TBuild where");
			whereclause.append(" (BuildMC like '%" + keystring + "%' or");
			whereclause.append(" Address like '%" + keystring + "%')) and");
		}else{
			if (cityid != null && !cityid.trim().equals("")) {
				whereclause.append(" CityID='" + cityid + "' and");
			}
		}
		String whereString = "1=1";
		if (whereclause != null && whereclause.length() > 3) {
			whereString = whereclause.subSequence(0, whereclause.length() - 3)
					.toString() + ")";
		}
		return whereString;
	}
	
//	/**
//	 * 根据officetype搜索
//	 *
//	 * 作者：Ring
//	 * 创建于：2013-2-28
//	 * @return
//	 */
//	public String getMetros(){
//		//纯0写字楼  4商务中心 3园区  1商住两用2酒店式公寓
////		private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
//		StringBuffer whereclause = new StringBuffer("");
//		if(metroSelectList!=null&&metroSelectList.size()>0){
//			int i;
//			for(i = 0;i<metroSelectList.size();i++){
//				if(metroSelectList.get(i).get("id")!=null
//						&&!metroSelectList.get(i).get("id").toString().trim().equals("")){
//					whereclause.append(" Metro='" + metroSelectList.get(i).get("id").toString() + "' or");
//				}
//			}
//		}
//		String whereString = "";
//		if(whereclause!=null&&whereclause.length()>2){
//			whereString=whereclause.subSequence(0, whereclause.length()-2).toString();
//			if(whereString.contains("or")){
//				whereString = "("+whereString+")";
//			}
//		}
//		return whereString;
//	}

	/**
	 * 监听搜索的字符改变
	 * 
	 * @author Ring
	 * 
	 */
//	class MyTextWatcher implements TextWatcher {
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			// handler.sendEmptyMessage(7);
//			// 当关键字输入框内不为空时，执行关键字查询请求
//			if (!tv.getText().toString().trim()
//					.equals("")&&tv.isFocusable()) {
//				new Thread(runnable1).start();
//			}
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyApplication.getInstance().setSearchbundle(null);
			if(backBtn.getVisibility() == View.VISIBLE){
				MyApplication.getInstance().setCurrentKeyt("");
				if (MyApplication.getInstance().getRoleid() == Constant.TYPE_CLIENT) { 
					MyApplication.getInstance().setSearchRoomBackVisibility(false);
					  MainTabActivity.mTabHost.setCurrentTab(0); 
						  }else { 	// 要跳转的界面 
				Intent intent = new Intent(this, MainActivity.class)
					 .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 把一个Activity转换成一个View
					  Window w = MainFirstTab.group.getLocalActivityManager()
					 .startActivity("MainActivity", intent); View view = w.getDecorView();
					 // 把View添加大ActivityGroup中 
					 MainFirstTab.group.setContentView(view);
					  }
			}else if(backBtn.getVisibility() == View.GONE){
				showExitDialog();
			}
				
		}
		return true;
	}

	/**
	 * 取得房源类型的数据
	 * 
	 * @createDate 2013/1/30
	 * @author 刘星星
	 */
	public void initRoomTypeListData() {
		if (roomTypeList == null) {
			roomTypeList = new ArrayList<HashMap<String, String>>();
		}
		roomTypeList.clear();
		HashMap<String, String> map = null;
		for (OfficeType accidentType : OfficeType.values()) {
			map = new HashMap<String, String>();
			map.put("id", accidentType.getValue() + "");
			map.put("name", accidentType.getName());
			roomTypeList.add(map);
		}
		notifyRoomTypeSpinner();
		dialogTitle.setText("房源类型");
	}

	/**
	 * 加载房源类型布局
	 */
	public void notifyRoomTypeSpinner() {
		LoupanListAdapter adapter = new LoupanListAdapter(this, roomTypeList,
				false);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
		if (roomTypeList.size() == 0) {
			noText.setVisibility(View.VISIBLE);
		} else {
			noText.setVisibility(View.GONE);
		}
	}

	/**
	 * 取得城市的数据
	 * 
	 * @createDate 2013/1/30
	 * @author 刘星星
	 */
	public void initCityListData() {
		if (cityList == null) {
			cityList = new ArrayList<HashMap<String, String>>();
		}
		cityList.clear();
		List<Map<String, Object>> selectresult = null;
		List<Map<String, Object>> selectresult1 = null;
		selectresult1 = ZoneUtil.getregion(this,
				"select parentid from region where type = '2' and id = '"
						+ MyApplication.getInstance().getCityid() + "'");
		if (selectresult1 == null || selectresult1.size() == 0) {
			return;
		}
		selectresult = ZoneUtil.getregion(
				this,
				"select id,name from region where type = '2' and parentid='"
						+ selectresult1.get(selectresult1.size() - 1)
								.get("parentid").toString() + "'");
		if (selectresult != null) {
			int i;
			HashMap<String, String> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, String>();
				map.put("name", selectresult.get(i).get("name").toString());
				map.put("id", selectresult.get(i).get("id").toString());
				cityList.add(map);
			}
		}
	}

	/**
	 * 加载城市布局
	 */
	public void notifyCitySpinner() {
		LoupanListAdapter adapter = new LoupanListAdapter(this, cityList, false);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
		if (cityList.size() == 0) {
			noText.setVisibility(View.VISIBLE);
		} else {
			noText.setVisibility(View.GONE);
		}
	}

	/**
	 * 取得区的数据
	 * 
	 * @createDate 2013/1/30
	 * @author 刘星星
	 */
	public void initAcreageListData() {
		if (acreageList == null) {
			acreageList = new ArrayList<HashMap<String, String>>();
		}
		acreageList.clear();
		List<Map<String, Object>> selectresult = null;
		selectresult = ZoneUtil.getregion(this,
				"select id,name,latitude,longitude from region where type = '3' and parentid = '"
						+ cityid + "'");
		if (selectresult != null) {
			int i;
			HashMap<String, String> map = null;
			map = new HashMap<String, String>();
			map.put("name", "不限");
			map.put("id", "");
			map.put("latitude", "");
			map.put("longitude", "");
			acreageList.add(map);
			for (i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, String>();
				map.put("name", selectresult.get(i).get("name").toString());
				map.put("id", selectresult.get(i).get("id").toString());
				try{
					map.put("latitude", selectresult.get(i).get("latitude").toString());
				}catch(Exception e){
					map.put("latitude", "");
				}
				try{
					map.put("longitude", selectresult.get(i).get("longitude").toString());
				}catch(Exception e){
					map.put("longitude", "");
				}
				acreageList.add(map);
			}
		}
	}

	/**
	 * 刷新弹出的对话框列表数据
	 * 
	 * 作者：Ring 创建于：2013-2-27
	 * 
	 * @param type
	 */
	public void notifyAdapter(int type) {
		if (type == 1) {
			notifyCitySpinner();
			dialogTitle.setText("城市选择");
		} else if (type == 2) {
			notifyAcreageSpinner();
			dialogTitle.setText("区域选择");
		} else if (type == 3) {
			notifyBusinessSpinner();
			dialogTitle.setText("商圈");
		}
	}

	/**
	 * 加载房源类型布局
	 */
	public void notifyAcreageSpinner() {
		LoupanListAdapter adapter = new LoupanListAdapter(this, acreageList,
				false);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
		if (acreageList.size() == 0) {
			noText.setVisibility(View.VISIBLE);
		} else {
			noText.setVisibility(View.GONE);
		}
	}

	/**
	 * 取得商圈的数据
	 * 
	 * @createDate 2013/1/30
	 * @author 刘星星
	 */
	public void initBusinessListData() {
		if (businessList == null) {
			businessList = new ArrayList<HashMap<String, String>>();
		}
		businessList.clear();
		List<Map<String, Object>> selectresult = null;
		selectresult = ZoneUtil.getregion(this,
				"select id,name,latitude,longitude from district where regionid='" + zoneid + "'");
		if (selectresult != null) {
			int i;
			HashMap<String, String> map = null;
			map = new HashMap<String, String>();
			map.put("name", "不限");
			map.put("id", "");
			map.put("latitude", latitude_business);
			map.put("longitude", longitude_business);
			businessList.add(map);
			for (i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, String>();
				map.put("name", selectresult.get(i).get("name").toString());
				map.put("id", selectresult.get(i).get("id").toString());
				try{
					map.put("latitude", selectresult.get(i).get("latitude").toString());
				}catch(Exception e){
					map.put("latitude", "");
				}
				try{
					map.put("longitude", selectresult.get(i).get("longitude").toString());
				}catch(Exception e){
					map.put("longitude", "");
				}
				businessList.add(map);
			}
		}
	}

	/**
	 * 加载房源类型布局
	 */
	public void notifyBusinessSpinner() {
		LoupanListAdapter adapter = new LoupanListAdapter(this, businessList,
				false);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
		if (businessList.size() == 0) {
			noText.setVisibility(View.VISIBLE);
		} else {
			noText.setVisibility(View.GONE);
		}
	}

	/**
	 * 取得地铁的数据
	 * 
	 * @createDate 2013/1/30
	 * @author 刘星星
	 */
	public void initMetroListData() {
		if (metroList == null) {
			metroList = new ArrayList<HashMap<String, String>>();
		}
		metroList.clear();
		List<Map<String, Object>> selectresult = null;
		String sql = "select * from soso_metroinfo where cityid = '"
				+ MyApplication.getInstance().getCityid() + "'";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, String> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("metroid") != null
						&& !selectresult.get(i).get("metroid").toString()
								.equals("")) {
					map = new HashMap<String, String>();
					map.put("id", selectresult.get(i).get("metroid").toString());
					map.put("cityid", selectresult.get(i).get("cityid")
							.toString());
					map.put("name", selectresult.get(i).get("metroname")
							.toString());
					metroList.add(map);
				}
			}
		}
		notifyMetroSpinner();
		dialogTitle.setText("地铁沿线");
	}

	/**
	 * 加载房源类型布局
	 */
	LoupanListAdapter MetroAdapter = null;
	public void notifyMetroSpinner() {
		MetroAdapter = new LoupanListAdapter(this, metroList,
				false,true,messageList);
		dialogBackBtn.setVisibility(View.VISIBLE);
		dialogBackBtn.setText("确定");
		dialogBackBtn.setOnClickListener(this);
		dialogListView.setAdapter(MetroAdapter);
	dialogListView
				.setOnItemClickListener(null);
		if (metroList.size() == 0) {
			noText.setVisibility(View.VISIBLE);
		} else {
			noText.setVisibility(View.GONE);
		}
	}

	/**
	 * 对话框中列表的Item的点击事件
	 * 
	 * @author 刘星星
	 * @createDate 2013.1.30
	 * 
	 */
	class dialogListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (dialog_flag) {
			case 1:

				break;
			case 2:
				dismiss();
				roomTypeSpinner.setText(roomTypeList.get(arg2).get("name")
						.toString());
				officetype = roomTypeList.get(arg2).get("id").toString();
				break;
			case 3:
				dismiss();
				String cityStr = cityList.get(arg2).get("name").toString();
				citySpinner.setText(cityStr);
				
				if(!cityid.equals(cityList.get(arg2).get("id").toString())){
					cityid = cityList.get(arg2).get("id").toString();
					areaSpinner.setText("不限");
					businessSpinner.setText("不限");
					zoneid = "";
					zonename="";
					businessid = "";
					businessname = "";
					latitude_business="";
					longitude_business="";
					latitude_zone="";
					longitude_zone="";
				}
				initAcreageListData();
//				if (acreageList.size() > 1) {
//					areaSpinner.setText(acreageList.get(1).get("name")
//							.toString());
//					zoneid = acreageList.get(1).get("id").toString();
//					latitude_zone = acreageList.get(1).get("latitude_zone").toString();
//					longitude_zone = acreageList.get(1).get("longitude_zone").toString();
//				} else {
//					areaSpinner.setText("不限");
//					zoneid = "";
//				}
//				initBusinessListData();
//				if (businessList.size() > 1) {
//					businessSpinner.setText(businessList.get(1).get("name")
//							.toString());
//					businessid = businessList.get(1).get("id").toString();
//					latitude_business = businessList.get(1).get("latitude").toString();
//					longitude_business = businessList.get(1).get("longitude").toString();
//				} else {
//					businessSpinner.setText("不限");
//					businessid = "";
//				}
				break;
			case 4:
				dismiss();
				if(!zoneid.equals("")&&!zoneid.equals(acreageList.get(arg2).get("id").toString())){
					zoneid = acreageList.get(arg2).get("id").toString();
					areaSpinner.setText(acreageList.get(arg2).get("name")
							.toString());
					businessSpinner.setText("不限");
					businessid = "";
					businessname = "";
					latitude_business="";
					longitude_business="";
				}else if(zoneid.equals("")){
					zoneid = acreageList.get(arg2).get("id").toString();
					areaSpinner.setText(acreageList.get(arg2).get("name")
							.toString());
				}
				if(zoneid.equals("")){
					areaSpinner.setText("不限");
					zoneid = "";
					zonename="";
				}
				
				latitude_zone = acreageList.get(arg2).get("latitude").toString();
				longitude_zone = acreageList.get(arg2).get("longitude").toString();
				initBusinessListData();
//				if (businessList.size() > 1) {
//					businessSpinner.setText(businessList.get(1).get("name")
//							.toString());
//					businessid = businessList.get(1).get("id").toString();
//					latitude_business = businessList.get(1).get("latitude").toString();
//					longitude_business = businessList.get(1).get("longitude").toString();
//				} else {
//					businessSpinner.setText("不限");
//					businessid = "";
//				}
				break;
			case 5:
				dismiss();
				businessSpinner.setText(businessList.get(arg2).get("name")
						.toString());
				businessid = businessList.get(arg2).get("id").toString();
				latitude_business = businessList.get(arg2).get("latitude").toString();
				longitude_business = businessList.get(arg2).get("longitude").toString();
				
				break;
			case 6:
//				dismiss();
//				metroSpinner
//						.setText(metroList.get(arg2).get("name").toString());
//				metroid = metroList.get(arg2).get("id").toString();
			/*	CheckBox checkBox = (CheckBox) arg1.findViewById(R.id.checkBox);
				if(checkBox.isChecked()){
					checkBox.setChecked(false);
				}else{
					checkBox.setChecked(true);
				}*/
				
				break;
			}

		}

	}

	@Override
	public void initView() {
		priceAreaSelect = new AreaSelectView(this, AccurateSearchActivity.this,
				0.0f, 20f);
		// priceAreaSelect.initData();
		// acreageAreaSelect = new
		// AreaSelectView(this,AccurateSearchActivity.this,0.0f,15000f);
		priceLayout.addView(priceAreaSelect.getView("元"));
		// acreageLayout.addView(acreageAreaSelect.getView("m²"));
		minPrice = priceAreaSelect.minText;// 从对象中取得控件
		maxPrice = priceAreaSelect.maxText;// 从对象中取得控件
		// minAcreage = acreageAreaSelect.minText;//从对象中取得控件
		// maxAcreage = acreageAreaSelect.maxText;//从对象中取得控件
		 backBtn.setOnClickListener(this);
		roomTypeSpinner.setOnClickListener(this);
		citySpinner.setOnClickListener(this);
		citySpinner.setSingleLine(true);
		areaSpinner.setSingleLine(true);
		businessSpinner.setSingleLine(true);
		areaSpinner.setOnClickListener(this);
		businessSpinner.setOnClickListener(this);
//		metroSpinner.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		keytEt.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);

	}

//	/**
//	 * 初始化关键字数据以及布局
//	 * 
//	 * @author 刘星星
//	 * @createDate 2013/2/27
//	 */
//	public void initKeytEtData() {
//		AutoCompleteAdapter keytAdapter = new AutoCompleteAdapter(this,
//				mOriginalValues, 10);
//		keytEt.setThreshold(2);
//		keytEt.setAdapter(keytAdapter);
//	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		scrollView.setScrollable(true);
		return super.onTouchEvent(event);
	}

	@Override
	public void initData() {
		// initKeytEtData();//初始化关键数据
	}

	@Override
	public void notifiView() {

	}
	/**
	 * 初始化弹出框窗口的布局
	 * 
	 * @author 刘星星
	 * @creageDate 2013/1/30
	 */
	private void initDialogWindow() {
		
		int width = 0;
		int height = 0;
		LinearLayout loupanwindow = (LinearLayout) this.getLayoutInflater()
				.inflate(R.layout.dialog_select_loupan, null);
		loupanwindow.getBackground().setAlpha(220);
		dialogListView = (ListView) loupanwindow.findViewById(R.id.listView);
		dialogTitle = (TextView) loupanwindow.findViewById(R.id.textView);
		dialogCloseBtn = (Button) loupanwindow.findViewById(R.id.closeBtn);
		dialogBackBtn = (Button) loupanwindow.findViewById(R.id.dialogBackBtn);
		titleLayout = (RelativeLayout) loupanwindow
				.findViewById(R.id.titleLayout);
		noText = (TextView) loupanwindow.findViewById(R.id.noText);
		dialogCloseBtn.setOnClickListener(this);
		dialogBackBtn.setOnClickListener(this);
		if (dialog_flag == 1) {// 依次代表弹出框表示的是什么 1代表关键字 2类型 3城市 4区 5商圈 6地铁
			parent = (LinearLayout) findViewById(R.id.etParent);
			selectPopupWindow = new PopupWindow(loupanwindow,
					parent.getWidth(), 200, true);
			// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
			// 没有这一句则效果不能出来，但并不会影响背景
			selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dialog_border));
			selectPopupWindow.getBackground().setAlpha(220);
//			selectPopupWindow.setFocusable(false);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应

			selectPopupWindow.setOutsideTouchable(true);
			selectPopupWindow.showAsDropDown(parent, 0, 0);
			titleLayout.setVisibility(View.GONE);
		} else {
			parent = (LinearLayout) findViewById(R.id.parent);
			width = (int) (parent.getWidth() * 0.8f);
			height = width + 100;
			selectPopupWindow = new PopupWindow(loupanwindow, width, height,
					true);
			// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
			// 没有这一句则效果不能出来，但并不会影响背景
			selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dialog_border));
			selectPopupWindow.getBackground().setAlpha(220);
			selectPopupWindow.setOutsideTouchable(true);
//			selectPopupWindow.setFocusable(false);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
			selectPopupWindow.showAsDropDown(parent,
					(parent.getWidth() - width) / 2,
					-(parent.getHeight() - (parent.getHeight() - height) / 2));
		}

		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	/**
	 * 处理耗时操作
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(AccurateSearchActivity.this
					.getParent())) {
				boolean b = false;
				handler.sendEmptyMessage(5);// 开启进度条
				b = getMetro();
				handler.sendEmptyMessage(6);// 关闭进度条
				if (b) {
					handler.sendEmptyMessage(1);// 刷新列表
				} else {
					handler.sendEmptyMessage(3);// 刷新列表
				}
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};
	/**
	 * 处理逻辑业务
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 刷新列表
				if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
					return;
				}else{
					initDialogWindow();
					initMetroListData();
					dialog_flag = 6;
				}
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					errormsg = "该城市还未开通地铁沿线";
				} else {
					errormsg = "获取超时，请稍后重试！";
				}
				Toast.makeText(AccurateSearchActivity.this.getParent(),
						errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(AccurateSearchActivity.this
						.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						AccurateSearchActivity.this.getParent());
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7://
//				initKeytEtData();
				break;
			case 8://
				priceAreaSelect.initData();
				float scale = priceAreaSelect.leftScale;
				Double mypriceup = 20.0;
				try {
					mypriceup = Double.parseDouble(priceup);
				} catch (Exception e) {
					// TODO: handle exception
				}
				Double mypricedown = 0.0;
				try {
					mypricedown = Double.parseDouble(pricedown);
				} catch (Exception e) {
					// TODO: handle exception
				}
				int wMin = (int) (mypricedown / scale);
				int wMax = (int) ((20 - mypriceup) / scale);
				priceAreaSelect.minText.setText(mypricedown + "");
				priceAreaSelect.maxText.setText(mypriceup + "");
				LinearLayout.LayoutParams paramLeft = new LinearLayout.LayoutParams(
						wMin + priceAreaSelect.leftBtn.getWidth(),
						priceAreaSelect.leftImage.getHeight());
				priceAreaSelect.leftImage.setLayoutParams(paramLeft);

				LinearLayout.LayoutParams paramRight = new LinearLayout.LayoutParams(
						wMax, priceAreaSelect.rightImage.getHeight());
				paramRight.setMargins(-2, 0, 0, 0);
				priceAreaSelect.rightImage.setLayoutParams(paramRight);
				break;
			}
		};
	};

	/**
	 * 获取城市的地铁线路
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 */
	public boolean getMetro() {
		String updatedate = "1970-01-01 12:00:00";
		if (MyApplication.getInstance().getCityid() == null
				|| MyApplication.getInstance().getCityid().equals("")) {
			return false;
		}
		if(MyApplication.getInstance().getSosouserinfo()!=null&&
				MyApplication.getInstance().getSosouserinfo().getUserID()!=null){
			userid =  MyApplication.getInstance(this)
					.getSosouserinfo(this).getUserID() ;
		}else{
			userid = "1970-01-01 12:00:00";
		}
		if (DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TMETRO_TIME' and updatedate = value and value<>'' and userid = '"
								+ userid
								+ "'", null).size() > 0) {
			return true;
		}
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TMETRO_TIME' and userid = '"
								+ userid
								+ "'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"updatedate") != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(
						"updatedate").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}

		// params 请求的参数列表
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("CityID", MyApplication.getInstance()
				.getCityid()));
		params.add(new BasicNameValuePair("UpdateTime", updatedate));
		uploaddata = new SoSoUploadData(this, "MetroSelectByCityId.aspx",
				params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance().getCityid());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 * 
	 * @param cityid
	 */
	private void dealReponse(String cityid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this)
					.execSql(
							"update soso_configurationinfo set updatedate = value where name='TMETRO_TIME' and userid = '"
									+ userid
									+ "'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this)
					.execSql(
							"update soso_configurationinfo set updatedate = value where name='TMETRO_TIME' and userid = '"
									+ userid
									+ "'");
			Type listType = new TypeToken<LinkedList<SoSoMetroInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoMetroInfo> soSoMetroInfos = null;
			SoSoMetroInfo soSoMetroInfo = null;
			ContentValues values = new ContentValues();
			soSoMetroInfos = gson.fromJson(reponse, listType);
			if (soSoMetroInfos != null && soSoMetroInfos.size() > 0) {
				for (Iterator<SoSoMetroInfo> iterator = soSoMetroInfos
						.iterator(); iterator.hasNext();) {
					soSoMetroInfo = (SoSoMetroInfo) iterator.next();
					if (soSoMetroInfo != null
							&& soSoMetroInfo.getMetroID() != null) {
						values.put("metroid", soSoMetroInfo.getMetroID());
						values.put("cityid", cityid);
						values.put("metroname", soSoMetroInfo.getMetroName());
						if (DBHelper
								.getInstance(AccurateSearchActivity.this)
								.selectRow(
										"select * from soso_metroinfo where metroid = '"
												+ soSoMetroInfo.getMetroID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(AccurateSearchActivity.this)
									.insert("soso_metroinfo", values);
						} else {
							DBHelper.getInstance(AccurateSearchActivity.this)
									.update("soso_metroinfo",
											values,
											"metroid = ?",
											new String[] { soSoMetroInfo
													.getMetroID() });
						}

						values.clear();
					}
				}
				if (soSoMetroInfos != null) {
					soSoMetroInfos.clear();
					soSoMetroInfos = null;
				}
				if (values != null) {
					values.clear();
					values = null;
				}

			}
		}
	}

//	/**
//	 * 关键字的获取
//	 * 
//	 * 作者：Ring 创建于：2013-2-23 wherec
//	 */
//	public boolean getKeyword() {
//		// params 请求的参数列表
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		params.add(new BasicNameValuePair("KeyWord", keytEt
//				.getText().toString()));
//		uploaddata = new SoSoUploadData(this, "KeyWordSelect.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		dealReponse();
//		params.clear();
//		params = null;
//		if (StringUtils.CheckReponse(reponse)) {
//			return true;
//		} else {
//			return false;
//		}
//	}

//	/**
//	 * author by Ring 处理用户名唯一性验证耗时操作
//	 */
//	public Runnable runnable1 = new Runnable() {
//
//		@Override
//		public void run() {
//			if (uploaddata != null) {
//				uploaddata.overReponse();
//			}
//			getKeyword();
//			if(mOriginalValues.size()>0){
//				handler.sendEmptyMessage(7);
//			}
//			
//		}
//	};

	Runnable priceRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				handler.sendEmptyMessage(8);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

//	/**
//	 * 处理服务器响应值，将关键字封装到list中
//	 * 
//	 * 作者：Ring 创建于：2013-1-31
//	 * 
//	 * @param districtid
//	 */
//	private void dealReponse() {
//		mOriginalValues.clear();
//		if (StringUtils.CheckReponse(reponse)) {
//			Type listType = new TypeToken<LinkedList<KeywordInfo>>() {
//			}.getType();
//			Gson gson = new Gson();
//			LinkedList<KeywordInfo> keywordInfos = null;
//			KeywordInfo keywordInfo = null;
//			keywordInfos = gson.fromJson(reponse, listType);
//			if (keywordInfos != null && keywordInfos.size() > 0) {
//				for (Iterator<KeywordInfo> iterator = keywordInfos.iterator(); iterator
//						.hasNext();) {
//					keywordInfo = (KeywordInfo) iterator.next();
//					if (keywordInfo != null && keywordInfo.getKeyWord() != null) {
//						// if(mOriginalValues.size()>20){
//						// return;
//						// }
//						mOriginalValues.add(keywordInfo.getKeyWord());
//					}
//				}
//				if (keywordInfos != null) {
//					keywordInfos.clear();
//					keywordInfos = null;
//				}
//			}
//		}
//	}
}
