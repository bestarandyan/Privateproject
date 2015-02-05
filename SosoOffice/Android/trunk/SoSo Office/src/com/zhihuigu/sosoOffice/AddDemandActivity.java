/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.Adapter.LoupanListAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.AreaSelectView;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoCusReleaseInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/2/1
 * 添加房源类
 *
 */
public class AddDemandActivity extends BaseActivity implements Activity_interface{
	private Button backBtn;//返回按鈕
	//标题   联系人   房租金额  面积  简介
	private EditText titleEt,linkManEt,introEt,minAcreage,maxAcreage;
	private Button typeRoomBtn;//房源类型选择
	private CheckBox checkBox;//是否包含物业费的选择控件
	private Button saveAndSend;//保存并发布按钮
	private LinearLayout priceLayout,acreageLayout;//放价格上下限选择的控件    放面积上下限选择的控件
	private AreaSelectView priceAreaSelect = null;//价格选择
//	private AreaSelectView acreageAreaSelect = null;//面积上下限选择
	
	private ListView dialogListView;
	private TextView dialogTitle;
	private Button dialogBackBtn;
	private Button dialogCloseBtn;
	private LinearLayout parent = null;
	private PopupWindow selectPopupWindow = null;
	private ArrayList<HashMap<String,String>> typeList = new ArrayList<HashMap<String,String>>();//房源类型
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	
	private String officetype = "";// 房源类型
	
	
	private LinearLayout phoneCheckLayout,titleCheckLayout,typeCheckLayout,acreageUpLayout,acreageDownLayout,jianjieCheckLayout;
	private TextView phoneCheckTv,titleCheckTv,typeCheckTv,acreageUpCheckTv,acreageDownCheckTv,jianjieCheckTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_adddemand);
		findView();
		initView();
		MainFirstTab.flag =1 ;
	}
	Handler handlerDialog = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			initDialogWindow();
			initTypeRoomList();//初始化房源类型数据
			super.handleMessage(msg);
		}
	
	};
	
	/***
	 * author by Ring 文本焦点监听事件
	 */
	private OnFocusChangeListener textfocuschange = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				return;
			}
			Message msg = new Message();
			Bundle b = new Bundle();
			if (v == linkManEt
					&&(linkManEt.getText().toString().trim().equals("")
					||!StringUtils.istelephone(linkManEt.getText().toString().trim()))) {
				String info = "";
				if(linkManEt.getText().toString().trim().equals("")){
					info = getResources().getString(R.string.telephone_null);
				}else if(!StringUtils.istelephone(linkManEt.getText().toString().trim())){
					info = getResources().getString(R.string.phone_error);
				}
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",info);
				msg.setData(b);
				msg.what = 7;
				handler.sendMessage(msg);
			}else if (v == linkManEt) {
				phoneCheckLayout.setVisibility(View.GONE);
			}
			
			if (v == introEt
					&&introEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","请您输入需求简介");
				msg.setData(b);
				msg.what = 8;
				handler.sendMessage(msg);
			}else if (v == introEt) {
				jianjieCheckLayout.setVisibility(View.GONE);
			}
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
		if(v == typeRoomBtn){
			CommonUtils.hideSoftKeyboard(this);//隐藏可能弹出来了的软键盘
			new Thread(runnableDialog).start();
		}else if(v == dialogCloseBtn){
			dismiss();
		}else if(v == backBtn){//返回上一个界面
			finish();
		}else if(v == saveAndSend){//保存并发布
			CommonUtils.hideSoftKeyboard(this);//隐藏软键盘
			if(textValidate()){
				new Thread(runnable).start();
			}
		}
		super.onClick(v);
	}
	/**
	 * 初始化房源类型
	 * @author 刘星星
	 * @createDate 2013.2.1
	 */
	public void initTypeRoomList(){
		if(typeList==null){
			typeList = new ArrayList<HashMap<String,String>>();
		}
		typeList.clear();
		HashMap<String, String> map = null;
		for (OfficeType accidentType : OfficeType.values()) {
			map = new HashMap<String, String>();
			map.put("id", accidentType.getValue()+"");
			map.put("name", accidentType.getName());
			typeList.add(map);
		}
		notifyTypeRoomSpinner();
		dialogTitle.setText("房源类型");
		dialogBackBtn.setVisibility(View.GONE);
		
	}
	/**
	 * 刷新房源类型列表
	 * @author 刘星星
	 * @createDate 2013/2/1
	 */
	public void notifyTypeRoomSpinner(){
		LoupanListAdapter adapter = new LoupanListAdapter(this,typeList,false);
		dialogListView.setAdapter(adapter);
		dialogListView.setOnItemClickListener(new dialogListItemClickListener());
	}
	/**
	 * 
	 * @author 刘星星
	 * @createDate 2013.2.1
	 *
	 */
	class dialogListItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String type = typeList.get(arg2).get("name").toString();
			officetype = typeList.get(arg2).get("id").toString();
			typeRoomBtn.setText(type);
			dismiss();
		}
		
	}
	
	/**
	 * 初始化楼盘窗口。
	 * @author 刘星星
	 * @createDate 2013.1.22
	 * 
	 */
	private void initDialogWindow(){
		int width = 0;
		int height = 0;
		LinearLayout loupanwindow = (LinearLayout) this.getLayoutInflater().
				inflate(R.layout.dialog_select_loupan, null);
		loupanwindow.getBackground().setAlpha(220);
		dialogListView = (ListView) loupanwindow.findViewById(R.id.listView);
		dialogTitle = (TextView) loupanwindow.findViewById(R.id.textView);
		dialogCloseBtn = (Button) loupanwindow.findViewById(R.id.closeBtn);
		dialogBackBtn = (Button) loupanwindow.findViewById(R.id.dialogBackBtn);
		dialogCloseBtn.setOnClickListener(this);
		dialogBackBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		width = (int)(parent.getWidth()*0.8f);
		height = width + 100;
		selectPopupWindow = new PopupWindow(loupanwindow, width,	height, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.getBackground().setAlpha(220);
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.showAsDropDown(parent, (parent.getWidth()-width)/2, -(parent.getHeight()-(parent.getHeight()-height)/2));
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
//	/**
//	 * 回退
//	 */
//	private void onBack(){
//		Intent intent = new Intent(this, DemandManagerActivity.class)
//		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		Window w = MainFirstTab.group.getLocalActivityManager()
//				.startActivity("zzxc", intent);
//		View view = w.getDecorView();
//		MainFirstTab.group.setContentView(view);
//	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		titleEt = (EditText) findViewById(R.id.titleEt);
		linkManEt = (EditText) findViewById(R.id.linkManEt);
		linkManEt.setOnFocusChangeListener(textfocuschange);
		introEt = (EditText) findViewById(R.id.introEt);
		introEt.setOnFocusChangeListener(textfocuschange);
		typeRoomBtn = (Button) findViewById(R.id.typeSpinner);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		saveAndSend = (Button) findViewById(R.id.saveAndSend);
		priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
		acreageLayout = (LinearLayout) findViewById(R.id.acreageLayout);
		minAcreage = (EditText) findViewById(R.id.minAcreageEt);
		maxAcreage = (EditText) findViewById(R.id.maxAcreageEt);
		phoneCheckLayout = (LinearLayout) findViewById(R.id.phoneCheckLayout);
		phoneCheckTv = (TextView) findViewById(R.id.phoneCheckTv);
		titleCheckLayout = (LinearLayout) findViewById(R.id.titleCheckLayout);
		titleCheckTv = (TextView) findViewById(R.id.titleCheck);
		typeCheckLayout = (LinearLayout) findViewById(R.id.typeCheckLayout);
		typeCheckTv = (TextView) findViewById(R.id.typeCheck);
		acreageUpLayout = (LinearLayout) findViewById(R.id.acreageUpCheckLayout);
		acreageUpCheckTv = (TextView) findViewById(R.id.acreageUpCheck);
		acreageDownLayout = (LinearLayout) findViewById(R.id.acreageDownCheckLayout);
		acreageDownCheckTv = (TextView) findViewById(R.id.acreageDownCheck);
		jianjieCheckLayout = (LinearLayout) findViewById(R.id.jianjieCheckLayout);
		jianjieCheckTV = (TextView) findViewById(R.id.jianjieCheck);
	}

	@Override
	public void initView() {
		priceAreaSelect = new AreaSelectView(this,0.0f,Constant.DEMAND_PRICE_MAX_VALUE);
//		acreageAreaSelect = new AreaSelectView(this,0.0f,Constant.DEMAND_ACREAGE_MAX_VALUE);
		priceLayout.addView(priceAreaSelect.getView("元"));
//        acreageLayout.addView(acreageAreaSelect.getView("m²"));
		backBtn.setOnClickListener(this);
		typeRoomBtn.setOnClickListener(this);
		saveAndSend.setOnClickListener(this);
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void notifiView() {
		
	}
	
	
	/**
	 * 发布需求
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:14
	 * @return true 发布成功，false 发布失败
	 */
	public boolean addReleaseInfo() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserID", MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID()));
		params.add(new BasicNameValuePair("Title", titleEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("Tele", linkManEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("Contact", linkManEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("AreaUp", maxAcreage.getText().toString()));
		params.add(new BasicNameValuePair("AreaDown", minAcreage.getText().toString()));
		params.add(new BasicNameValuePair("PriceUp", priceAreaSelect.maxText.getText()+""));// 房源名称
		params.add(new BasicNameValuePair("PriceDown", priceAreaSelect.minText.getText()+""));
		params.add(new BasicNameValuePair("Description", introEt
				.getText().toString()));
		params.add(new BasicNameValuePair("OfficeType", officetype));
		params.add(new BasicNameValuePair("ReleaseState", "1"));
		uploaddata = new SoSoUploadData(this, "CusReleaseAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 提交信息前验证信息是否完整和合法
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:17
	 * @return true 验证通过 false 验证失败
	 */

	public boolean textValidate() {
		if (linkManEt.getText().toString().trim().equals("")) {
			// 联系人不能为空
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.linkman_null),
					AddDemandActivity.this);
			return false;
		} else if (!StringUtils.istelephone(linkManEt.getText().toString().trim())) {
			// 判断手机号码是否正确
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					AddDemandActivity.this);
			return false;
		}/* else if (maxAcreage.getText().toString().trim().equals("")) {
			// 面积上限
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.maxacreage),
					AddDemandActivity.this);
			return false;
		} */else if (introEt.getText().toString().trim().equals("")) {
			//
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.intro_message),
					AddDemandActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * @author Ring
	 * @since 2013-01-25 15:13
	 */
	public void dealReponse(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			Gson gson = new Gson();// 创建Gson对象
			SoSoCusReleaseInfo soSoCusReleaseInfo = null;
			try {
				soSoCusReleaseInfo = gson.fromJson(reponse, SoSoCusReleaseInfo.class);// 解析json对象
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (soSoCusReleaseInfo != null && soSoCusReleaseInfo.getReleaseID() != null) {
				values.put("releaseid", soSoCusReleaseInfo.getReleaseID());
				values.put("userid", userid);
				values.put("username", "");
				values.put("title", titleEt.getText().toString());
				values.put("tele", linkManEt.getText().toString());
				values.put("contact", linkManEt.getText().toString());
				values.put("areaup", maxAcreage.getText().toString());
				values.put("areadown", minAcreage.getText().toString());
				values.put("priceup", priceAreaSelect.maxText.getText()+"");
				values.put("pricedown", priceAreaSelect.minText.getText()+"");
				values.put("description",introEt
						.getText().toString());
				values.put("officetype",officetype);
				values.put("unit", "");
				if (DBHelper
						.getInstance(AddDemandActivity.this)
						.selectRow(
								"select * from soso_cusreleaseinfo where releaseid = '"
										+ soSoCusReleaseInfo.getReleaseID()
										+ "'", null).size() <= 0) {
					DBHelper.getInstance(AddDemandActivity.this)
							.insert("soso_cusreleaseinfo", values);
				} else {
					DBHelper.getInstance(AddDemandActivity.this)
							.update("AddDemandActivity",
									values,
									"releaseid = ?",
									new String[] { soSoCusReleaseInfo
											.getReleaseID() });
				}

				values.clear();
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			DBHelper.getInstance(this).close();
		}
	}
	
	
	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(AddDemandActivity.this)) {
				handler.sendEmptyMessage(5);// 开启进度条
				boolean b = false;// 添加房源信息是否成功的标识
				b = addReleaseInfo();
				handler.sendEmptyMessage(6);// 关闭进度条
				if (b) {
					handler.sendEmptyMessage(1);// 发布成功

				} else {
					handler.sendEmptyMessage(3);// 发布失败
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
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			Bundle b = null;
			switch (msg.what) {
			case 1:// 从登录界面跳转到主界面
				AlertDialog.Builder builder = new AlertDialog.Builder(AddDemandActivity.this);
				builder.setMessage(getResources().getString(
						R.string.releaseroom_success))
						.setTitle(getResources().getString(R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								finish();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else {
					errormsg = getResources().getString(
							R.string.releaserelease_failure);
				}
				MessageBox.CreateAlertDialog(errormsg,
						AddDemandActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(AddDemandActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(AddDemandActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
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
			case 7://电话验证
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						phoneCheckLayout.setVisibility(View.VISIBLE);
						phoneCheckTv.setText(b.getString("msg"));
					}
				} else {
					phoneCheckLayout.setVisibility(View.GONE);
				}
				break;
			case 8://简介
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						jianjieCheckLayout.setVisibility(View.VISIBLE);
						jianjieCheckTV.setText(b.getString("msg"));
					}
				} else {
					jianjieCheckLayout.setVisibility(View.GONE);
				}
				break;
			}
		};
	};

}
