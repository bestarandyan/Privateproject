package com.chinaLife.claimAssistant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.chinaLife.Interface.sc_InitInterface;
import com.chinaLife.adapter.sc_OptionsAdapter;
import com.chinaLife.claimAssistant.bean.sc_CaseClaimInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData3;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.chinaLife.claimAssistant.tools.sc_StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class sc_CaseManageActivity extends Activity implements OnClickListener,
		Callback, sc_InitInterface {
	private Button btn_select, btn_back;// 查询按钮 返回按钮
	private EditText et_phone, et_carNumber;// 联系人手机号码 车牌号码
	private CheckBox cb1, cb2;// 复选框
	private ImageButton btn2;
	private byte btn2state = 0;// 0未展开，1展开
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private sc_OptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	// 下拉框依附组件
	private LinearLayout parent;
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// // 恢复数据源按钮
	// private Button button;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 用来处理选中或者删除下拉项消息
	private Handler handler;
	// 是否初始化完成标志
	private boolean flag = true;
	public String sermsg = "";
	public ProgressDialog progressdialog = null;
	public boolean initWedget_tag = true;
	public sc_UploadData3 uploaddata = null;

	public boolean click_limit = true;// 时间准备点
	public String reponse = ""; // 服务器反映值
	// public RadioButton yidong, liantong, dianxin, neiwang;
	// private RadioGroup radioGroup;

	public LinearLayout passwordlayout = null;// 整个passwordlayout布局
	public EditText passwordEt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_casemanage);
		sc_ExitApplication.getInstance().context = sc_CaseManageActivity.this;
		sc_ExitApplication.getInstance()
				.addActivity(sc_CaseManageActivity.this);
		initView();
		initData();
		initViewFun();
	}

	public void initView() {
		// radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		btn_back = (Button) findViewById(R.id.fanhui);
		btn_select = (Button) findViewById(R.id.bt1);
		et_phone = (EditText) findViewById(R.id.et1);
		et_carNumber = (EditText) findViewById(R.id.et2);
		et_carNumber
				.setFilters(new InputFilter[] { new InputFilter.AllCaps() });
		passwordlayout = (LinearLayout) findViewById(R.id.passwordlayout);
		passwordlayout.setVisibility(View.GONE);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		et_carNumber.addTextChangedListener(new MyTextWatcher(et_carNumber));
		et_phone.addTextChangedListener(new MyTextWatcher(et_phone));
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			et_phone.setFocusable(false);
			et_phone.setClickable(false);
			et_phone.setEnabled(false);
			et_carNumber.setFocusable(false);
			et_carNumber.setClickable(false);
			et_carNumber.setEnabled(false);
		}
		// yidong = (RadioButton) findViewById(R.id.radioBtn1);
		// liantong = (RadioButton) findViewById(R.id.radioBtn2);
		// dianxin = (RadioButton) findViewById(R.id.radioBtn3);
		// neiwang = (RadioButton) findViewById(R.id.radioBtn4);
		cb1 = (CheckBox) findViewById(R.id.cb1);
		cb2 = (CheckBox) findViewById(R.id.cb2);
		cb1.setOnCheckedChangeListener(new checkeListener());
		cb2.setOnCheckedChangeListener(new checkeListener());
		// radioGroup.setOnCheckedChangeListener(new radioCheckListener());
	}

	/**
	 * 记住联系人手机号和记住车牌号的事件监听器
	 * 
	 * @author 刘星星
	 * 
	 */
	public class checkeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == cb1) {
				if (!isChecked) {
					cb2.setChecked(false);
				}
			} else if (buttonView == cb2) {
				if (isChecked) {
					cb1.setChecked(true);
				}
			}
		}
	}

	// public class radioCheckListener implements
	// android.widget.RadioGroup.OnCheckedChangeListener {
	//
	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId) {
	// // TODO Auto-generated method stub*
	// if (checkedId == R.id.radioBtn1) {
	// MyApplication.URL = MyApplication.MOVE_URL;
	// MyApplication.getInstance().setSim_state(1);
	// } else if (checkedId == R.id.radioBtn2) {
	// MyApplication.URL = MyApplication.LINK_URL;
	// MyApplication.getInstance().setSim_state(2);
	// } else if (checkedId == R.id.radioBtn3) {
	// MyApplication.URL = MyApplication.TELECOM_URL;
	// MyApplication.getInstance().setSim_state(3);
	// } else if (checkedId == R.id.radioBtn4) {
	// MyApplication.URL = MyApplication.NEI_NET_URL;
	// }
	// }
	//
	// }
	//

	class MyTextWatcher implements TextWatcher {
		private String tmp = "";
		private EditText editText;

		public MyTextWatcher(final EditText editText) {
			this.editText = editText;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			tmp = s.toString();// 获得改变前的字符串
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (str.equals(tmp)) {
				return;// 如果tmp==str则返回，因为这是我们设置的结果。否则会形成死循环。
			} else {
				String sql = "select * from userinfo where contact_mobile_number = '"
						+ et_phone.getText().toString().replace(" ", "")
						+ "' and plate_number ='"
						+ et_carNumber.getText().toString().replace(" ", "")
						+ "'";

				String password = "";
				List<Map<String, Object>> selectresult = sc_DBHelper
						.getInstance().selectRow(sql, null);
				if (selectresult != null && selectresult.size() > 0) {
					if (selectresult.get(selectresult.size() - 1) != null
							&& selectresult.get(selectresult.size() - 1).get(
									"password") != null) {
						password = (selectresult.get(selectresult.size() - 1)
								.get("password").toString());
					}
				}
				sc_MyApplication.getInstance().setPassword(password);
				passwordEt.setText("");
				passwordlayout.setVisibility(View.GONE);
			}
		}
	}

	public void initData() {
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			return;
		}
		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		String sql = "";
		try {
			if (!sc_MyApplication.getInstance().getPhonenumber().equals("")) {
				sql = "select * from userinfo where "
						+ "contact_mobile_number_keep = 1 and contact_mobile_number='"
						+ sc_MyApplication.getInstance().getPhonenumber()
						+ "' and plate_number ='"
						+ sc_MyApplication.getInstance().getPlatenumber() + "'";
			} else {
				sql = "select * from userinfo where "
						+ "contact_mobile_number_keep = 1";
			}
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(sql, null);
			if (selectresult.size() > 0) {
				if (!(selectresult.get(0).get("contact_mobile_number")
						.toString().equals("") || selectresult.get(0)
						.get("plate_number").toString() == null)) {
					if (Integer.parseInt(selectresult.get(0)
							.get("contact_mobile_number_keep").toString()) == 1) {
						et_phone.setText(selectresult.get(0)
								.get("contact_mobile_number").toString());

						if (Integer.parseInt(selectresult.get(0)
								.get("plate_number_keep").toString()) == 1) {
							et_carNumber.setText(selectresult.get(0)
									.get("plate_number").toString());
						} else {
							et_carNumber.setText("");
						}

						cb1.setChecked(Integer.parseInt(selectresult.get(0)
								.get("contact_mobile_number_keep").toString()) == 1);
						cb2.setChecked(Integer.parseInt(selectresult.get(0)
								.get("plate_number_keep").toString()) == 1);
					} else if (Integer.parseInt(selectresult.get(0)
							.get("plate_number_keep").toString()) == 1) {
						cb1.setChecked(Integer.parseInt(selectresult.get(0)
								.get("contact_mobile_number_keep").toString()) == 1);
						cb2.setChecked(Integer.parseInt(selectresult.get(0)
								.get("plate_number_keep").toString()) == 1);
						et_phone.setText(selectresult.get(0)
								.get("contact_mobile_number").toString());
						if (Integer.parseInt(selectresult.get(0)
								.get("plate_number_keep").toString()) == 1) {
							et_carNumber.setText(selectresult.get(0)
									.get("plate_number").toString());
						} else {
							et_carNumber.setText("");
						}
					}
				}
			}
			sc_DBHelper.getInstance().close();
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "查询界面初始化数据出现异常如下：" + e.getMessage());
				}
			}).start();
			// if (MyApplication.opLogger != null) {
			// MyApplication.opLogger.error("查询界面出错", e);
			// }

		}
	}

	public void initViewFun() {
		btn_select.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		// TelephonyManager telManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);
		// String operator = telManager.getSimOperator();
		// if(operator!=null){
		// if(operator.equals("46000") || operator.equals("46002")){
		// //中国移动
		// MyApplication.getInstance().setSim_state(1);
		// }else if(operator.equals("46001")){
		// //中国联通
		// MyApplication.getInstance().setSim_state(2);
		// }else if(operator.equals("46003")){
		// //中国电信
		// MyApplication.getInstance().setSim_state(3);
		// }
		// }
		// int sim_state = MyApplication.getInstance().getSim_state();
		// if (sim_state == 0) {
		// yidong.setChecked(true);
		// liantong.setChecked(false);
		// dianxin.setChecked(false);
		// neiwang.setChecked(false);
		// MyApplication.URL = MyApplication.MOVE_URL;
		// } else if (sim_state == 1) {// 移动
		// yidong.setChecked(true);
		// liantong.setChecked(false);
		// dianxin.setChecked(false);
		// neiwang.setChecked(false);
		// MyApplication.URL = MyApplication.MOVE_URL;
		// } else if (sim_state == 2) {// 联通
		// yidong.setChecked(false);
		// liantong.setChecked(true);
		// dianxin.setChecked(false);
		// neiwang.setChecked(false);
		// MyApplication.URL = MyApplication.LINK_URL;
		// } else if (sim_state == 3) {// 电信
		// yidong.setChecked(false);
		// liantong.setChecked(false);
		// dianxin.setChecked(true);
		// neiwang.setChecked(false);
		// MyApplication.URL = MyApplication.TELECOM_URL;
		// }
	}

	public void onClick(View v) {
		if (v == btn_select) {
			if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
				String sql = "select * from uploadinfo where claimid= 'x00000001'";
				try {
					sc_DBHelper.getInstance().delete("uploadinfo",
							"claimid = ?", new String[] { "x00000001" });
					sc_DBHelper.getInstance().delete("claimphotoinfo",
							"claimid = ?", new String[] { "x00000001" });
				} catch (final Exception e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							sc_LogUtil.sendLog(2,
									"查询界面数据库出现异常：" + e.getMessage());
						}
					}).start();
					// if (MyApplication.opLogger != null) {
					// MyApplication.opLogger.error("查询界面出错", e);
					// }

				}
				sc_MyApplication.getInstance().setServer_type(0);
				sc_MyApplication.getInstance().setCaseid("x00000001");
				sc_MyApplication.getInstance().setCaseidstate(0);
				sc_MyApplication.getInstance().setClaimid("");
				sc_MyApplication.getInstance().setClaimidstate(-1);
				Intent i = new Intent(this, sc_CaseListActivity.class);
				startActivity(i);
				finish();

			} else {
				if (sc_NetworkCheck.IsHaveInternet(this)) {
					if (sc_MyApplication.hasnewverson
							&& sc_MyApplication.isqiangzhi == 1) {
						List<Map<String, Object>> selectresult1 = sc_DBHelper
								.getInstance().selectRow(
										"select * from appinfo", null);
						showDownloadDialog(selectresult1.get(0).get("url")
								.toString());
						return;
					}
				}
				if (yanzheng()) {
					new Thread(runnableQuest).start();
				}
			}

		} else if (v == btn_back) {
			if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
				sc_MyApplication.getInstance().setSelfHelpFlag(0);
				sc_MyApplication.getInstance().setServer_type(0);
				sc_MyApplication.getInstance().setCaseid("");
				sc_MyApplication.getInstance().setCaseidstate(0);
				sc_MyApplication.getInstance().setClaimid("");
				sc_MyApplication.getInstance().setClaimidstate(-1);
			}
			Intent intent = new Intent(this, sc_MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// /**
	// * 根据手机号车牌号查询案件列表
	// */
	// private void selectCase() {
	// if (NetworkCheck.IsHaveInternet(this)) {
	// if (MyApplication.hasnewverson && MyApplication.isqiangzhi == 1) {
	// List<Map<String, Object>> selectresult1 =
	// DBHelper.getInstance().selectRow(
	// "select * from appinfo", null);
	// showDownloadDialog(selectresult1.get(0).get("url").toString());
	// return;
	// }
	// }
	// if (!yanzheng()) {
	// return;
	// }
	// if (NetworkCheck.IsHaveInternet(this)) {
	// sermsg = "查询赔案失败，请确保您已报案，并保持网络畅通。";
	// List<Map<String, Object>> selectresult =
	// DBHelper.getInstance().selectRow(
	// "select max(_id),time from syndatatime where platenumber = '"
	// + et_carNumber.getText().toString()
	// .replace(" ", "")
	// + "' and tablename='caseinfo' and phonenumber = '"
	// + et_phone.getText().toString().replace(" ", "")
	// + "'", null);
	// List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("appid", MyApplication.APPID));
	// params.add(new BasicNameValuePair("appkey", MyApplication.APPKEY));
	// params.add(new BasicNameValuePair("action", "getCaseList"));
	// params.add(new BasicNameValuePair("phoneNumber", et_phone.getText()
	// .toString().replace(" ", "")));
	// params.add(new BasicNameValuePair("plateNumber", et_carNumber
	// .getText().toString().replace(" ", "")));
	// params.add(new BasicNameValuePair("IMEI", PhoneInfo.getIMEI(this)));
	// try {
	// params.add(new BasicNameValuePair("lastUpdateTime",
	// selectresult.get(0).get("time").toString()));
	// } catch (Exception e) {
	// params.add(new BasicNameValuePair("lastUpdateTime", ""));
	// }
	// uploaddata = new UploadData(MyApplication.URL + "case", params, 2);
	// progressdialog.setMessage("系统处理中，请稍等...");
	// progressdialog.show();
	// progressdialog.setCancelable(false);
	// progressdialog.setCanceledOnTouchOutside(false);
	// new Thread(runnable3).start();
	// } else {
	// sermsg = "查询案件失败，请检查网络是否畅通！";
	// toNextActivity();
	// }
	// }

	/**
	 * 显示是否要下载新版本的对话框
	 */
	public void showDownloadDialog(final String url) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert).setTitle("提示")
				.setMessage("有新版本是否立即更新？").setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(url);
						intent.setData(content_url);
						sc_CaseManageActivity.this.startActivity(intent);
						sc_CaseManageActivity.this.finish();
						System.exit(0);
					}
				}).setNegativeButton("取消", null).show();
	}

	private boolean yanzheng() {

		if (et_carNumber.getText().toString().replace(" ", "").equals("")) {
			tiXing("车牌号不能为空！");
			return false;
		} else if (et_phone.getText().toString().replace(" ", "").equals("")) {
			tiXing("电话号码不能为空！");
			return false;
		} else if (!sc_StringUtils.isCellphone(et_phone.getText().toString()
				.replace(" ", ""))) {
			tiXing("电话号码格式不对！");
			return false;
		}

		if (!passwordEt.getText().toString().trim().equals("")) {
			sc_MyApplication.getInstance().setPassword(
					passwordEt.getText().toString().trim());
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
				sc_MyApplication.getInstance().setSelfHelpFlag(0);
				sc_MyApplication.getInstance().setServer_type(0);
				sc_MyApplication.getInstance().setCaseid("");
				sc_MyApplication.getInstance().setCaseidstate(0);
				sc_MyApplication.getInstance().setClaimid("");
				sc_MyApplication.getInstance().setClaimidstate(-1);
			}
			Intent intent = new Intent(this, sc_MainActivity.class);
			startActivity(intent);
			sc_CaseManageActivity.this.finish();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (initWedget_tag) {
			initView();
			initData();
			initViewFun();
			initWedget();
			initWedget_tag = false;
		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	// MenuInflater inflater = new MenuInflater(getApplicationContext());
	// inflater.inflate(R.menu.menu, menu);
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// // 响应每个菜单项(通过菜单项的ID)
	// case R.id.menu_main:
	// Intent i = new Intent(this, sc_MainActivity.class);
	// startActivity(i);
	// finish();
	// break;
	// case R.id.menu_exit:
	// sc_ExitApplication.getInstance().showExitDialog();
	// break;
	// default:
	// // 对没有处理的事件，交给父类来处理
	// return super.onOptionsItemSelected(item);
	// }
	// // 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * 初始化界面控件
	 */
	private void initWedget() {
		// 初始化Handler,用来处理消息
		handler = new Handler(sc_CaseManageActivity.this);
		// 初始化界面组件
		parent = (LinearLayout) findViewById(R.id.parent);
		btn2 = (ImageButton) findViewById(R.id.jiantou);
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			btn2.setVisibility(View.GONE);
		}
		btn2state = 0;
		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;
		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flag) {
					// 显示PopupWindow窗口
					flag = false;
					btn2.setImageResource(R.drawable.sc_sj_on);
					popupWindwShowing();
				} else {
					flag = true;
					btn2.setImageResource(R.drawable.sc_sj);
					dismiss();
				}
			}
		});

		// 初始化PopupWindow
		initPopuWindow();

		// button = (Button) findViewById(R.id.refresh);
		// // 设置点击事件，恢复下拉框列表数据，没有什么作用，纯粹是为了方便多看几次效果而设置
		// button.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// initDatas();
		// optionsAdapter.notifyDataSetChanged();
		// }
		// });
	}

	/**
	 * 初始化填充Adapter所用List数据
	 */
	private void initDatas() {
		list.clear();
		HashMap<String, String> map = null;

		List<Map<String, Object>> selectresult = sc_DBHelper
				.getInstance()
				.selectRow(
						"select * from userinfo where contact_mobile_number_keep = 1",
						null);
		for (int i = 0; i < selectresult.size(); i++) {
			map = new HashMap<String, String>();
			if (Integer.parseInt(selectresult.get(i).get("plate_number_keep")
					.toString()) == 1) {
				map.put("phonenumber",
						selectresult.get(i).get("contact_mobile_number")
								.toString());
				map.put("platenumber", selectresult.get(i).get("plate_number")
						.toString());
				try {
					map.put("password", selectresult.get(i).get("password")
							.toString());
				} catch (Exception e) {
					map.put("password", "");
				}
			} else {
				map.put("phonenumber",
						selectresult.get(i).get("contact_mobile_number")
								.toString());
				map.put("platenumber", "");
				try {
					map.put("password", selectresult.get(i).get("password")
							.toString());
				} catch (Exception e) {
					map.put("password", "");
				}
			}
			list.add(map);
		}
		sc_DBHelper.getInstance().close();
		if (selectresult.size() <= 0) {
			btn2.setVisibility(View.INVISIBLE);
		} else {
			btn2.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化自助演示下的下拉数据
	 */
	private void initSelf() {
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("phonenumber", "1388888888");
		map1.put("platenumber", "京A00000");
		list.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("phonenumber", "15837867887");
		map2.put("platenumber", "粤B88888");
		list.add(map2);
		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.put("phonenumber", "18917674736");
		map3.put("platenumber", "粤C99999");
		list.add(map3);
		HashMap<String, String> map4 = new HashMap<String, String>();
		map4.put("phonenumber", "13774876798");
		map4.put("platenumber", "粤D00000");
		list.add(map4);
		et_phone.setText(list.get(0).get("phonenumber").toString());
		et_carNumber.setText(list.get(0).get("platenumber").toString());
	}

	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		if (sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			initSelf();
		} else {
			initDatas();
		}
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.sc_options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list2);

		// 设置自定义Adapter
		optionsAdapter = new sc_OptionsAdapter(this, handler, list);
		listView.setAdapter(optionsAdapter);
		selectPopupWindow = new PopupWindow(loginwindow, pwidth, 200);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
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
		selectPopupWindow.showAsDropDown(parent, 0, -6);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		btn2.setImageResource(R.drawable.sc_sj);
		selectPopupWindow.dismiss();
	}

	/**
	 * 处理Hander消息
	 */
	public boolean handleMessage(Message message) {
		Bundle data = message.getData();
		switch (message.what) {
		case 1:
			// 选中下拉项，下拉框消失
			int selIndex = data.getInt("selIndex");
			et_phone.setText(list.get(selIndex).get("phonenumber").toString());
			et_carNumber.setText(list.get(selIndex).get("platenumber")
					.toString());
			sc_MyApplication.getInstance().setPassword(
					list.get(selIndex).get("password").toString());
			dismiss();
			break;
		case 2:
			// 移除下拉项数据
			int delIndex = data.getInt("delIndex");
			if (sc_MyApplication.getInstance().getSelfHelpFlag() != 1) {
				sc_DBHelper.getInstance().delete(
						"userinfo",
						"contact_mobile_number=? and plate_number =?",
						new String[] {
								list.get(delIndex).get("phonenumber")
										.toString(),
								list.get(delIndex).get("platenumber")
										.toString() });
			}
			list.remove(delIndex);
			// 刷新下拉列表
			if (list.size() <= 0) {
				btn2.setVisibility(View.INVISIBLE);
				dismiss();
				break;
			}
			optionsAdapter.notifyDataSetChanged();
			break;
		case 4:
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					sc_CaseManageActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage("请求失败，请检查网络是否连接！")
					.setPositiveButton("确定", null).show();
			break;
		}
		return false;
	}

	/**
	 * 处理获取的json数据
	 */

	public void parseJson(int type, String reponse) {
		if (type == 2) {
			if (reponse.equals("-1000")) {
				sermsg = "查询赔案失败，请在良好的网络环境下重新查询";
			} else {
				sermsg = "查询赔案失败，请确保您已报案，并保持网络畅通。";
			}
			toNextActivity();
			return;
		}
		// likedlist是双向列表
		sc_MyApplication.getInstance().setPhonenumber(
				et_phone.getText().toString().replace(" ", ""));
		sc_MyApplication.getInstance().setPlatenumber(
				et_carNumber.getText().toString().replace(" ", ""));
		Type listType = new TypeToken<LinkedList<sc_CaseClaimInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		try {
			FileOutputStream file = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/1.txt");
			try {
				file.write(reponse.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<sc_CaseClaimInfo> caseclaiminfos = null;
		// reponse =
		// "[{\"caseid\":\"2249\",\"casenumber\":\"11111123123123213\",\"accidentaddress\":\"\u5317\u4eac\u5e02\",\"contactname\":\"\u5f20\u5341\u4e09\",\"contactmobilenumber\":\"18621008891\",\"platenumber\":\"\u4eacA12345\",\"claimname\":\"\u738b\u4e8c\u5c0f\",\"claimphonenumber\":\"010-99988888\",\"casestatus\":\"0\",\"casestatustext\":\"\u5f85\u5904\u7406\u6848\u4ef6\",\"reporttime\":\"2012-11-12 00:19:00\",\"accidenttime\":\"2012-11-12 00:19:00\",\"updatetime\":\"2012-11-12 00:16:27\",\"casereason\":\"-1\",\"claimid\":\"-1\",\"servicetype\":\"-1\",\"claimmode\":\"-1\",\"claimstatus\":\"-1\",\"claimamount\":\"-1.0"}]";
		caseclaiminfos = gson.fromJson(reponse, listType);
		if (caseclaiminfos == null) {
			return;
		}

		if (caseclaiminfos.size() > 0) {
			values.put("contact_mobile_number", et_phone.getText().toString()
					.replace(" ", ""));
			values.put("plate_number", et_carNumber.getText().toString()
					.replace(" ", ""));
			values.put("plate_number_keep", cb2.isChecked() ? 1 : 0);
			values.put("contact_mobile_number_keep", cb1.isChecked() ? 1 : 0);

			String sql = "select * from userinfo where contact_mobile_number = '"
					+ et_phone.getText().toString().replace(" ", "")
					+ "' and plate_number ='"
					+ et_carNumber.getText().toString().replace(" ", "") + "'";

			if (sc_DBHelper.getInstance().selectRow(sql, null).size() <= 0) {
				sc_DBHelper.getInstance().insert("userinfo", values);
			} else {
				sc_DBHelper.getInstance().update(
						"userinfo",
						values,
						"contact_mobile_number = ? and plate_number= ?",
						new String[] {
								et_phone.getText().toString().replace(" ", ""),
								et_carNumber.getText().toString()
										.replace(" ", "") });
			}
			values.clear();
			Date date = new Date();
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置显示格式
			String time = df2.format(date);
			values.put("phonenumber",
					et_phone.getText().toString().replace(" ", ""));
			values.put("platenumber", et_carNumber.getText().toString()
					.replace(" ", ""));
			values.put("tablename", "caseinfo");
			values.put("time", time);

			String sql2 = "select max(_id),time from syndatatime where platenumber = '"
					+ et_phone.getText().toString().replace(" ", "")
					+ "' and tablename='caseinfo' and phonenumber = '"
					+ et_phone.getText().toString().replace(" ", "") + "'";

			if (sc_DBHelper.getInstance().selectRow(sql2, null).size() <= 0) {
				sc_DBHelper.getInstance().insert("syndatatime", values);
			} else {
				sc_DBHelper.getInstance().update(
						"syndatatime",
						values,
						"phonenumber = ? and platenumber= ?",
						new String[] {
								et_phone.getText().toString().replace(" ", ""),
								et_carNumber.getText().toString()
										.replace(" ", "") });
			}

			values.clear();
		}

		values = null;
		sc_DBHelper.getInstance().close();

		toNextActivity();
	}

	public void tiXing(String msg) {
		try {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					sc_CaseManageActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage(msg)
					.setPositiveButton("我知道了", null).show();
		} catch (Exception e) {
		}
	}

	public void toNextActivity() {
		String sql = "select * from userinfo where contact_mobile_number = '"
				+ et_phone.getText().toString().replace(" ", "")
				+ "' and plate_number ='"
				+ et_carNumber.getText().toString().replace(" ", "") + "'";
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
				.selectRow(sql, null);
		sc_MyApplication.getInstance().setPhonenumber(
				et_phone.getText().toString().replace(" ", ""));
		sc_MyApplication.getInstance().setPlatenumber(
				et_carNumber.getText().toString().replace(" ", ""));
		if (selectresult.size() <= 0) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					sc_CaseManageActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage(sermsg)
					.setPositiveButton("我知道了", null).show();
		} else {
			sc_SharedPreferencesinfo.setdata(this, sc_MyApplication
					.getInstance().getPhonenumber(), sc_MyApplication
					.getInstance().getPlatenumber());
			Intent i = new Intent(this, sc_CaseListActivity.class);
			this.startActivity(i);
			this.finish();
		}
	}

	/**
	 * 请求的线程
	 */
	public Runnable runnableQuest = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (sc_NetworkCheck.IsHaveInternet(sc_CaseManageActivity.this)) {
				handlerQuest.sendEmptyMessage(5);
				int n = selectCase1();
				handlerQuest.sendEmptyMessage(6);
				switch (n) {
				case 0:
					// 密码，车牌号，手机号都验证成功，进入案件列表界面
					handlerQuest.sendEmptyMessage(0);
					break;
				case 1:
					// 传密码了，但校验失败
					handlerQuest.sendEmptyMessage(1);
					break;
				case 2:
					// 还从未设置过密码
					handlerQuest.sendEmptyMessage(2);
					break;
				case 3:
					// 设置过密码，但未传密码值
					handlerQuest.sendEmptyMessage(3);
					break;
				case -1:
					// 其他错误
					handlerQuest.sendEmptyMessage(-1);
					break;
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};

	/**
	 * 根据手机号车牌号查询案件列表 return 1 密码校验失败，2，密码未设置 3，密码已设置未传入 0成功
	 */
	private int selectCase1() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getCaseList"));
		params.add(new BasicNameValuePair("phoneNumber", et_phone.getText()
				.toString().replace(" ", "")));
		params.add(new BasicNameValuePair("plateNumber", et_carNumber.getText()
				.toString().replace(" ", "")));
		params.add(new BasicNameValuePair("password", sc_MyApplication
				.getInstance().getPassword()));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("lastUpdateTime", ""));
		uploaddata = new sc_UploadData3(sc_MyApplication.URL + "case", params);
		uploaddata.Post();
		reponse = uploaddata.getResponse();
		params.clear();
		params = null;
		boolean b = true;
		int n = -1;
		if (reponse.contains("[")) {
			n = 0;
		} else if (reponse.equals("-31")) {
			n = 1;
		} else if (reponse.equals("-30")) {
			n = 2;
		} else if (reponse.equals("-33")) {
			n = 3;
		} else {
			b = false;
		}
		if (b) {
			dealmsg();
		}
		return n;
	}

	/***
	 * 处理服务器返回值
	 * 
	 * @param reponse2
	 */
	private void dealmsg() {
		if (reponse.equals("[]")) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("contact_mobile_number", et_phone.getText().toString()
				.replace(" ", ""));
		values.put("plate_number",
				et_carNumber.getText().toString().replace(" ", ""));
		values.put("plate_number_keep", cb2.isChecked() ? 1 : 0);
		values.put("contact_mobile_number_keep", cb1.isChecked() ? 1 : 0);
		values.put("password", sc_MyApplication.getInstance().getPassword());

		String sql = "select * from userinfo where contact_mobile_number = '"
				+ et_phone.getText().toString().replace(" ", "")
				+ "' and plate_number ='"
				+ et_carNumber.getText().toString().replace(" ", "") + "'";

		if (sc_DBHelper.getInstance().selectRow(sql, null).size() <= 0) {
			sc_DBHelper.getInstance().insert("userinfo", values);
		} else {
			sc_DBHelper.getInstance()
					.update("userinfo",
							values,
							"contact_mobile_number = ? and plate_number= ?",
							new String[] {
									et_phone.getText().toString()
											.replace(" ", ""),
									et_carNumber.getText().toString()
											.replace(" ", "") });
		}

	}

	/**
	 * UI处理
	 */

	public Handler handlerQuest = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent i = null;
			switch (msg.what) {
			case 0:// 密码，车牌号，手机号都验证成功，进入案件列表界面
				if (reponse.equals("[]")) {
					tiXing("查询赔案失败，请确保您已报案，并保持网络畅通。");
					return;
				}
				sc_MyApplication.getInstance().setPhonenumber(
						et_phone.getText().toString());
				sc_MyApplication.getInstance().setPlatenumber(
						et_carNumber.getText().toString());
				sc_SharedPreferencesinfo.setdata(sc_CaseManageActivity.this,
						sc_MyApplication.getInstance().getPhonenumber(),
						sc_MyApplication.getInstance().getPlatenumber());
				i = new Intent(sc_CaseManageActivity.this,
						sc_CaseListActivity.class);
				sc_CaseManageActivity.this.startActivity(i);
				sc_CaseManageActivity.this.finish();
				break;
			case 1:
				passwordlayout.setVisibility(View.VISIBLE);
				tiXing("密码校验失败，请重新输入密码");
				// 传密码了，但校验失败
				break;
			case 2:
				sc_MyApplication.getInstance().setPlatenumber(
						et_carNumber.getText().toString());
				sc_MyApplication.getInstance().setPhonenumber(
						et_phone.getText().toString());
				sc_SharedPreferencesinfo.setdata(sc_CaseManageActivity.this,
						sc_MyApplication.getInstance().getPhonenumber(),
						sc_MyApplication.getInstance().getPlatenumber());
				i = new Intent(sc_CaseManageActivity.this,
						sc_PasswordSetupActivity.class);
				sc_CaseManageActivity.this.startActivity(i);
				sc_CaseManageActivity.this.finish();
				// 还从未设置过密码
				break;
			case 3:
				// 设置过密码，但未传密码值
				passwordlayout.setVisibility(View.VISIBLE);
				tiXing("请输入您的查询密码");
				break;
			case -1:
				// 其他错误
				if (reponse.equals("-998")) {
					tiXing("查询赔案失败，请确保您已报案，并保持网络畅通。");
					// sermsg = "查询赔案失败，请确保您已报案，并保持网络畅通。";
				} else if (reponse.equals("-1000")) {
					tiXing("查询赔案失败，请在良好的网络环境下重新查询");
				} else if (reponse.equals("-27")) {
					tiXing("查询过于频繁，请稍后重试");
				} else {
					tiXing("服务器异常，请联系客服");
				}
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(sc_CaseManageActivity.this);
				progressdialog.setMessage("系统处理中，请稍等~~~");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (uploaddata != null) {
							uploaddata.overResponse();
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
			}
		}

	};

}
