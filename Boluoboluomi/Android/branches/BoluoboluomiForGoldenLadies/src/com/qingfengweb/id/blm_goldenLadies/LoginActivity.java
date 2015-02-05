package com.qingfengweb.id.blm_goldenLadies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.qingfengweb.adapter.OptionsAdapter;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.model.UserInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.MessageBox;
import com.qingfengweb.util.StringUtils;

public class LoginActivity extends BaseActivity {
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private Button backBtn, loginBtn, registerBtn;//
	private EditText userName, passWord;
	private ImageButton selectBtn;
	private CheckBox saveName, savePass, autoLogin;
	private boolean flag = true;
	private LinearLayout parent;
	public boolean initWedget_tag = true;
	private List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();// 下拉框选项数据源
	private ProgressDialog progressdialog;
	private UploadData uploaddata = null;
	private boolean runnable_tag = true;
	private DBHelper db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_login);
		findView();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		userName = (EditText) findViewById(R.id.unsername);
		passWord = (EditText) findViewById(R.id.password);
		saveName = (CheckBox) findViewById(R.id.radioBtn1);
		savePass = (CheckBox) findViewById(R.id.radioBtn2);
		autoLogin = (CheckBox) findViewById(R.id.radioBtn3);
		selectBtn = (ImageButton) findViewById(R.id.selectBtn);
		parent = (LinearLayout) findViewById(R.id.parent);
		backBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		selectBtn.setOnClickListener(this);
		saveName.setOnClickListener(this);
		savePass.setOnClickListener(this);
		autoLogin.setOnClickListener(this);
		String oldUserName = UserBeanInfo.getInstant().getUserName();
		String oldPass = UserBeanInfo.getInstant().getPassword();
		if(oldUserName!=null && !oldUserName.equals("")){
			userName.setText(oldUserName);
			saveName.setChecked(true);
			if(oldPass!=null && !oldPass.equals("")){
				passWord.setText(oldPass);//这里不用做判断是否记住了密码  因为只有选择了保存密码  密码才会保存到数据库
				savePass.setChecked(true);
			}
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int getValue = getIntent().getIntExtra("activityType",0);
			Intent i = new Intent();
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				finish();
			}else if(getValue == 3){//从主页的我的相册跳入的
				i.setClass(LoginActivity.this,CommentListActivity.class);
				i.putExtra("position", Integer.parseInt(((Map<String,Object>)getIntent().getSerializableExtra("map")).get("position").toString()));
				startActivity(i);
				finish();
			}else if(getValue ==4){//查件系统
				finish();
			}else if(getValue ==5){//积分兑换
				i.setClass(LoginActivity.this,IntegralExchangeActivity.class);
				startActivity(i);
				finish();
			}else if(getValue ==6){//推荐好友
				finish();
			}else if(getValue ==7){//提交微信
				finish();
			}else if(getValue ==8){//我要点评
				finish();
			}else if(getValue ==9){//我要定制
				finish();
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		if(listdata!=null){
			listdata.clear();
			listdata = null;
		}
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		if (v == registerBtn) {//注册按钮
			handler.sendEmptyMessage(2);// 从登录界面跳转到注册界面
		} else if (v == backBtn) {//返回按钮
			int getValue = getIntent().getIntExtra("activityType",0);
			Intent i = new Intent();
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				finish();
			}else if(getValue == 3){//从主页的我的相册跳入的
				i.setClass(LoginActivity.this,CommentListActivity.class);
				i.putExtra("position", Integer.parseInt(((Map<String,Object>)getIntent().getSerializableExtra("map")).get("position").toString()));
				startActivity(i);
				finish();
			}else if(getValue ==4){//从查件系统跳入来的
				finish();
			}else if(getValue ==5){//积分兑换
				i.setClass(LoginActivity.this,IntegralExchangeActivity.class);
				startActivity(i);
				finish();
			}else if(getValue ==6){//推荐好友
				finish();
			}else if(getValue ==7){//提交微信
				finish();
			}else if(getValue ==8){//我要点评
				finish();
			}else if(getValue ==9){//我要定制
				finish();
			}
		} else if (v == loginBtn) {//登陆按钮
			if (textValidate()) {
				new Thread(loginRunnable).start();// 从登录验证
			}
		} else if (v == selectBtn) {
			if (flag) {
				// 显示PopupWindow窗口
				flag = false;
				selectBtn.setImageResource(R.drawable.sj_on);
				popupWindwShowing();
			} else {
				flag = true;
				selectBtn.setImageResource(R.drawable.sj);
				dismiss();
			}
		} else if (v == saveName) {
			if (!saveName.isChecked()) {
				autoLogin.setChecked(false);
				savePass.setChecked(false);
			}
		} else if (v == savePass) {
			if (savePass.isChecked()) {
				saveName.setChecked(true);
			} else {
				autoLogin.setChecked(false);
			}
		} else if (v == autoLogin) {
			if (autoLogin.isChecked()) {
				saveName.setChecked(true);
				savePass.setChecked(true);
			}
		}
		super.onClick(v);
	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (initWedget_tag) {
			findView();
			initPopuWindow();
			initWedget_tag = false;
		}

	}

	/**
	 * 初始化PopupWindow
	 */
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private OptionsAdapter optionsAdapter = null;

	/**
	 * 初始化下拉列表控件
	 */
	private void initPopuWindow() {
		db = DBHelper.getInstance(this);
		initListdata();
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list2);
		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, listdata);
		listView.setAdapter(optionsAdapter);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth()-5, LayoutParams.WRAP_CONTENT);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.gender_border));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		selectPopupWindow.showAsDropDown(parent, 2, -parent.getHeight() / 2);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectBtn.setImageResource(R.drawable.sj);
		selectPopupWindow.dismiss();
	}

	/**
	 * author by Ring 登录前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (userName.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.username_null),
					LoginActivity.this);
			return false;
		} else if (passWord.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.password_null),
					LoginActivity.this);
			return false;
		} else if (!StringUtils.isCellphone(userName.getText().toString()
				.trim())) {
//			MessageBox.CreateAlertDialog(
//					getResources().getString(R.string.prompt), getResources()
//							.getString(R.string.phone_error),
//					LoginActivity.this);
//			return false;
		}
		return true;
	}

	/**
	 * author:Ring 处理耗时操作
	 */
	public Runnable loginRunnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(LoginActivity.this)) {
				runnable_tag =true;
				handler.sendEmptyMessage(5);//开启进度条
				String msgStr = RequestServerFromHttp.loginUser(userName.getText().toString().trim(), passWord.getText().toString().trim());
				handler.sendEmptyMessage(6);//关闭进度条
				if(!runnable_tag){
					click_limit = true;
					return;
				}
				if (msgStr.startsWith("{")) {//登陆成功
					UserBeanInfo.getInstant().setLogined(true);
					JsonData.jsonUserData(msgStr, db.open(),/*savePass.isChecked()?*/passWord.getText().toString().trim()/*:""*/,autoLogin.isChecked()?"1":"0");//如果选择了保存密码，则将密码存入数据库
					handler.sendEmptyMessage(1);// 从登录界面跳转到主界面
				}else if(msgStr.equals("404")){
					handler.sendEmptyMessage(9);// 访问服务器失败
				} else {
					handler.sendEmptyMessage(3);// 登录失败给用户提示
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
			try{

				Intent i = new Intent();
				switch (msg.what) {
				case 1:// 从登录界面跳转到主界面
					int getValue = getIntent().getIntExtra("activityType",0);
					if(getValue==0){
						if(!MainActivity.mainActivity.isFinishing()){
							finish();
						}else{
							i.setClass(LoginActivity.this, MainActivity.class);
							LoginActivity.this.startActivity(i);
							finish();
						}
					}else if(getValue ==1){//从样照欣赏的推荐跳入来的
						i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
						i.setClass(LoginActivity.this, TuiJianActivity.class);
						LoginActivity.this.startActivity(i);
						finish();
					}else if(getValue == 2){//从主页的我的相册跳入的
						i.setClass(LoginActivity.this,AlbumMainActivity.class);
						startActivity(i);
						finish();
					}else if(getValue == 3){//从我要点评跳入的
						i.setClass(LoginActivity.this,CommentActivity.class);
						i.putExtra("map", getIntent().getSerializableExtra("map"));
						startActivity(i);
						finish();
					}else if(getValue == 4){//从查件系统跳入的
						i.setClass(LoginActivity.this,SelectPhotoActivity.class);
						startActivity(i);
						finish();
					}else if(getValue == 5){//积分对换
						i.setClass(LoginActivity.this,DetailIntegralActivity.class);
						i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
						startActivity(i);
						finish();
					}else if(getValue ==6){//推荐好友
						IntegralStoreMainActivity.instantActivity.finish();
						i.setClass(LoginActivity.this,RecommendFriendActivity.class);
						startActivity(i);
						finish();
					}else if(getValue ==7){//提交微信
						i.setClass(LoginActivity.this,WeiXinActivity.class);
						startActivity(i);
						finish();
					}else if(getValue ==8){//我要点评
						i.setClass(LoginActivity.this,CommentMainActivity.class);
						startActivity(i);
						finish();
					}else if(getValue ==9){//我要定制
						i.setClass(LoginActivity.this,CustomMainActivity.class);
						startActivity(i);
						finish();
					}
					break;
				case 2:// 从登录界面跳转到注册界面
					int getValue1 = getIntent().getIntExtra("activityType",0);
					if(getValue1==0){
						i.setClass(LoginActivity.this, RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						LoginActivity.this.startActivity(i);
						finish();
					}else if(getValue1 ==1){//从样照欣赏的推荐跳入来的
						i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
						i.setClass(LoginActivity.this, RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						LoginActivity.this.startActivity(i);
						finish();
					}else if(getValue1 == 2){//从主页的我的相册跳入的
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 == 3){//从主页的我的相册跳入的
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						i.putExtra("map", getIntent().getSerializableExtra("map"));
						startActivity(i);
						finish();
					}else if(getValue1 == 4){//从主页的我的相册跳入的
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 == 5){//积分兑换
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 ==6){//推荐好友
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 ==7){//提交微信
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 ==8){//我要点评
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}else if(getValue1 ==9){//我要定制
						i.setClass(LoginActivity.this,RegisterActivity.class);
						i.putExtra("activityType", getValue1);
						startActivity(i);
						finish();
					}
					break;
				case 3:// 登录失败给用户提示
					String errormsg = "";
					if(reponse.equals("-1000")){
						errormsg = getResources().getString(R.string.progress_timeout);
					}else{
						errormsg = getResources().getString(R.string.login_error_check);
					}
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt),
							errormsg,
							LoginActivity.this);
					break;
				case 4:// 没有网络时给用户提示
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt),
							getResources().getString(R.string.error_net),
							LoginActivity.this);
					break;
				case 5:// 打开进度条
					progressdialog = new ProgressDialog(LoginActivity.this);
					progressdialog.setMessage(getResources().getString(
							R.string.progress_message_login));
					progressdialog.setCanceledOnTouchOutside(false);
					progressdialog.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode,
								KeyEvent event) {
							runnable_tag = false;
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
				case 7:
					Bundle data = msg.getData();
					// 选中下拉项，下拉框消失
					int selIndex = data.getInt("selIndex");
						userName.setText(listdata.get(selIndex).get("username").toString());
						passWord.setText(listdata.get(selIndex).get("password").toString());
						if(listdata.get(selIndex).get("isautologin").equals("1")){
							saveName.setChecked(true);
							savePass.setChecked(true);
							autoLogin.setChecked(true);
						}else{
							saveName.setChecked(false);
							savePass.setChecked(false);
							autoLogin.setChecked(false);
						}
					dismiss();
					break;
				case 8:
					Bundle data1 = msg.getData();
					// 移除下拉项数据
					int delIndex = data1.getInt("delIndex");
					db.delete(UserInfo.TableName,"username=? and storeid=?",new String[] {listdata.get(delIndex).get("username").toString(),listdata.get(delIndex).get("storeid").toString() });
					listdata.remove(delIndex);
					// 刷新下拉列表
					optionsAdapter.notifyDataSetChanged();
					if (listdata.size() <= 0) {
						selectBtn.setVisibility(View.INVISIBLE);
						dismiss();
						break;
					}
					break;
				case 9:
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt),
							"登陆失败，请重试！",
							LoginActivity.this);
					break;
				}
			
			}catch (Exception e) {
				e.printStackTrace();
			}
		};
	};


	/***
	 * 登录用户名框的下拉列表的数据源
	 */
	public void initListdata() {
		listdata.clear();
		listdata = db.selectRow("select * from "+UserInfo.TableName,null);
		db.close();
		if (listdata != null && listdata.size() > 0) {
			selectBtn.setVisibility(View.VISIBLE);
		}else{
			selectBtn.setVisibility(View.GONE);
		}
		}

}
